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

import java.util.ArrayList;

import de.openminds.training.giftsforfriends.data.Data;
import de.openminds.training.giftsforfriends.domain.ContactInformation;

public class GiftlistService extends IntentService {

    public static final String ACTION_LOAD_LIST = "loadList";
    public static final String ACTION_SOME_OTHER_ACTION = "someOther";

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
        } else if (ACTION_SOME_OTHER_ACTION.equals(action)) {
            // so something
        } else {
            throw new IllegalArgumentException("Unknown action: " + action);
        }
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
}
