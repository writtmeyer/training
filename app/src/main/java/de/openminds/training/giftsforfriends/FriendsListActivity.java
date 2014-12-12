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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class FriendsListActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftlist);

        // RecyclerView - you need to set the adapter and a layoutmanager
        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerview_giftlist);
        rv.setAdapter(new GiftlistAdapter(PseudoData.getAllContactInformations(), this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_single_giftlistitem) {
            // start detail activity
            PseudoData.ContactInformation info = (PseudoData.ContactInformation)v.getTag();
            if (info != null) {
                Intent intent = new Intent(this, FriendDetailActivity.class);
                intent.putExtra("id", (info.id));
                startActivity(intent);
            }
        }
    }

}
