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
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class FriendsListActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftlist);
        findViewById(R.id.container_single_giftlistitem).setOnClickListener(this);
        // test data; eventually this will be replaced with database data
        // since this will be replaced anyway, text is _not_ moved to strings.xml
        TextView txtName = (TextView)findViewById(R.id.txt_name);
        txtName.setText("Beate");
        TextView txtGifts = (TextView)findViewById(R.id.txt_gifts);
        txtGifts.setText("5 gifts");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_single_giftlistitem) {
            // andere activity starten
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("id", 5);
            startActivity(intent);
        }
    }

}
