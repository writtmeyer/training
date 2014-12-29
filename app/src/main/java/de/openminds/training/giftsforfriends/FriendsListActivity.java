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

public class FriendsListActivity extends FragmentActivity implements FriendsListFragment.FriendSelectedListener {

    private static String DETAILS_FRAGMENT_TAG = "friendsDetailsTag";
    private RecyclerView rv;
    private GiftlistAdapter adapter;
    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTwoPane = getResources().getBoolean(R.bool.two_pane);
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

    @Override
    public void onFriendSelected(long id) {
        if (isTwoPane) {
            FriendDetailFragment f =
                    (FriendDetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
            if (f != null) {
                f.switchFriend(id);
            }
            else {
                f = FriendDetailFragment.newInstance(id);
                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.container_detail_fragment, f, DETAILS_FRAGMENT_TAG).
                        commit();
            }
        }
        else {
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("id", (id));
            startActivity(intent);
        }
    }
}
