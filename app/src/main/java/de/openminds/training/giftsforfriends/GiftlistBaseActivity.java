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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.openminds.training.giftsforfriends.domain.Post;

public class GiftlistBaseActivity extends ActionBarActivity  {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                // default for all Activities - unless overriddem
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_download_normal:
                doDownloadData(GiftlistService.ACTION_DOWNLOAD_STOCK);
                break;
            case R.id.action_download_ion:
                doDownloadData(GiftlistService.ACTION_DOWNLOAD_ION);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void doDownloadData(String action) {
        Intent downloadIntent = new Intent(this, GiftlistService.class);
        downloadIntent.setAction(action);
        startService(downloadIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.ACTION_DOWNLOADRESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, filter);
    }

    // The Receiver to get the result from the service
    BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_DOWNLOADRESULT.equals(intent.getAction())) {
                Post post = intent.getParcelableExtra(Constants.KEY_POST);
                DialogFragment newFragment = DownloadResultDialogFragment.newInstance(post);
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        }
    };

}
