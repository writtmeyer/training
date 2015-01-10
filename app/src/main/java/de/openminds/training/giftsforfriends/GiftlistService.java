/*
 * Copyright 2014 Wolfram Rittmeyer / OpenMinds UG (haftungsbeschränkt)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.openminds.training.giftsforfriends;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.openminds.training.giftsforfriends.data.Data;
import de.openminds.training.giftsforfriends.domain.ContactInformation;
import de.openminds.training.giftsforfriends.domain.Post;

public class GiftlistService extends IntentService {

    public static final String ACTION_LOAD_LIST = "loadList";
    public static final String ACTION_LOAD_DETAILS = "loadDetails";
    public static final String ACTION_DOWNLOAD_STOCK = "actionDownloadStock";
    public static final String ACTION_DOWNLOAD_ION = "actionDownloadIon";

    public GiftlistService() {
        super(GiftlistService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            Log.e("training", "action is null; aborting");
            // do not call stopSelf();
            // Intentservice talkes care of that if no more tasks
            // are in the queue!
            return;
        }
        if (ACTION_LOAD_LIST.equals(action)) {
            doLoadList(intent);
        } else if (ACTION_LOAD_DETAILS.equals(action)) {
            doLoadDetails(intent);
        } else if (ACTION_DOWNLOAD_STOCK.equals(action)) {
            doDownloadAndroidWay();
        } else if (ACTION_DOWNLOAD_ION.equals(action)) {
            doDownloadIonWay();
        } else {
            throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private void doDownloadAndroidWay() {
        Log.v("training", "downloading the stock Android way");
        URL url = null;
        try {
            // the following site offers some demo data
            // This code downloads the post with id "2"
            url = new URL("http://jsonplaceholder.typicode.com/posts/2");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // for post methods you have to call "setDoOutput(true)",
            // obtain tge OutputStream and write to it _before_
            // getting the resonse code. See the following commented lines:
//            conn.setDoOutput(true);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            String body = URLEncoder.encode("someKey=somevalue&someOtherKey=someOtherValue", StandardCharsets.UTF_8.name());
//            writer.write(body);
            Gson gson = new Gson();
            int statuscode = conn.getResponseCode();
            Post post;
            if (statuscode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String result = IOUtils.toString(in, "UTF-8");
                Log.v("training", "result: " + result);
                post = gson.fromJson(result, Post.class);
                transferPostToRecipients(post);

            }
        } catch (MalformedURLException e) {
            Log.e("training", "URL invalid", e);
        } catch (IOException e) {
            Log.e("training", "Problem with network access", e);
        }
    }

    private void transferPostToRecipients(Post post) {
        // transfer the result via broadcast:
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(Constants.ACTION_DOWNLOADRESULT);
        broadCastIntent.putExtra(Constants.KEY_POST, post);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadCastIntent);
    }

    private void doDownloadIonWay() {
        Log.v("training", "downloading the Ion way");
        // Ion makes loading network access that much easier
        // It offers plenty of options. In the following code the result
        // gets transformed to the domain object by Ion itself.
        // Ion is also great to load images to ImageViews.
        // More on Ion on the project page:
        // https://github.com/koush/ion

        // Even though Service is a context object itself you need
        // to use the applicaztion context here. Otherwise your service might
        // have stopped - thus your service might be gone - and you won't see
        // any results since Ion couldn't send the broadcast.
        Ion.with(this.getApplicationContext()).
                load("http://jsonplaceholder.typicode.com/posts/2").
                as(Post.class).
                setCallback(new FutureCallback<Post>() {
                    @Override
                    public void onCompleted(Exception e, Post post) {
                        if (e != null) {
                            Log.e("dailword", "Problem loading post: " + e.getMessage(), e);
                            return;
                        }
                        if (post == null) {
                            Log.e("dailword", "Post is null in onCompleted() even though no exception was thrown");
                            return;
                        }
                        transferPostToRecipients(post);
                    }
                });
    }

    private void doLoadList(Intent intent) {
        // you could check the intent for some extras;
        // it's not used here and just added to show you
        // how you could use the intent to get data from
        // the caller
        String someData = intent.getStringExtra("someKey");
        Log.v("training", "someData: " + someData);

        Data data = new Data(this);
        ArrayList<ContactInformation> infos = data.getAllContactData();

        // transfer the result via broadcast:
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(Constants.ACTION_LISTRESULT);
        broadCastIntent.putParcelableArrayListExtra(Constants.KEY_GIFTLIST, infos);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadCastIntent);
    }

    private void doLoadDetails(Intent intent) {
        long id = intent.getLongExtra(Constants.KEY_ID, -1);
        Log.v("training", "id: " + id);
        Data data = new Data(this);
        ContactInformation info = data.getDetail(id);
        // transfer the result via broadcast:
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(Constants.ACTION_DETAILRESULT);
        broadCastIntent.putExtra(Constants.KEY_CONTACTINFO, info);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadCastIntent);
    }
}
