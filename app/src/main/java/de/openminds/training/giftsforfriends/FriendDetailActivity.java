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

import de.openminds.training.giftsforfriends.data.Data;
import de.openminds.training.giftsforfriends.domain.ContactInformation;

public class FriendDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        long id = getIntent().getLongExtra("id", -1L);
        // we are not working with a db, so we simply cast to an int to fulfill the API of List:
        ContactInformation info = new Data(this).getAllContactData().get((int) id - 1);
        TextView friend = (TextView)findViewById(R.id.txt_name);
        friend.setText(info.name);

        // There's still no RecyclerView for gifts, so I simply
        // set the first gift - if there's any
        TextView giftName = (TextView)findViewById(R.id.txt_gift_name);

        // since gifts are not part of the db yet, the list of gifts is not used either
    }
}

