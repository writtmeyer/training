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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class FriendDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        long id = getIntent().getLongExtra("id", -1L);
        // we are not working with a db, so we simply cast to an int to fulfill the API of List:
        PseudoData.ContactInformation info = PseudoData.getAllContactInformations().get((int)id - 1);
        TextView friend = (TextView)findViewById(R.id.txt_name);
        friend.setText(info.name);

        // There's still no RecyclerView for gifts, so I simply
        // set the first gift - if there's any
        TextView giftName = (TextView)findViewById(R.id.txt_gift_name);
        List<PseudoData.Gift> gifts = PseudoData.getAllGiftsForContact(id);
        if (gifts != null && gifts.size() > 0) {
            giftName.setText(gifts.get(0).name);
        }
    }
}

