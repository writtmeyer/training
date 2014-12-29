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
import android.support.v4.app.FragmentActivity;
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

public class FriendsListActivity extends FragmentActivity {

    private RecyclerView rv;
    private GiftlistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftlist);
        if (savedInstanceState == null) {
            // in case of config change the bundle is not null
            // in that case Android takes care of reviving the fragment and
            // you should not recreate it yourself
            FriendsListFragment f = FriendsListFragment.newInstance();
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container_master_fragment, f).
                    commit();
        }
    }

}
