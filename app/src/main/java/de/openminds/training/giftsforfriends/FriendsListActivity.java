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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import de.openminds.training.giftsforfriends.domain.ContactInformation;


/////////////////////////////////////////////
//
// This app has no edit activity, yet.
// Thus you either have to add data manually using sqlite3
// or programmatically.
//
// I have done the latter in the onCreate() method
// of the SQLiteOpenHelper
//
/////////////////////////////////////////////

public class FriendsListActivity extends Activity implements View.OnClickListener {

    private RecyclerView rv;
    private GiftlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(Constants.ACTION_LISTRESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, filter);
        // start the service to retriev data:
        Intent serviceIntent = new Intent(this, GiftlistService.class);
        serviceIntent.setAction(GiftlistService.ACTION_LOAD_LIST);
        this.startService(serviceIntent);

        setContentView(R.layout.activity_giftlist);

        // RecyclerView - you need to set the adapter and a layoutmanager
        // Kepp in mind. We load the data asynchronously and do not have
        // it ready yet. Thus I had to add the adapter as a instance variable
        // and add data later on.
        rv = (RecyclerView)findViewById(R.id.recyclerview_giftlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new GiftlistAdapter(null, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_single_giftlistitem) {
            // start detail activity
            ContactInformation info = (ContactInformation)v.getTag();
            if (info != null) {
                Intent intent = new Intent(this, FriendDetailActivity.class);
                intent.putExtra("id", (info.id));
                startActivity(intent);
            }
        }
    }

    private void swapData(List<ContactInformation> infos) {
        adapter.swapData(infos);
    }

    // The Receiver to get the result from the service
    BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LISTRESULT.equals(intent.getAction())) {
                List<ContactInformation> infos =
                        intent.getParcelableArrayListExtra(Constants.KEY_GIFTLIST);
                Log.v("training", "result: " + infos.get(0).name);
                swapData(infos);
            }
        }
    };
}
