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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.openminds.training.giftsforfriends.domain.ContactInformation;

// I'm using support fragments, since they are updated regularly - and some
// classes of the support lib need them anyway; prefer them over stock Android
// fragments whenever possible - that way, you have more control over updates
// than if you had to rely solely on device vendors updating devices
public class FriendDetailFragment extends Fragment{

    private long detailId;
    private TextView txtFriendName;
    private TextView txtGiftName;

    // Android might need to recreate your Fragment - and for this Android relies on a no-args
    // constructor. For this reason a pattern to create Fragments has emerged:
    // You create a new instance not by using a constructor but by using a static factory method.
    // You pass any needed information to this method as arguments.
    // You store those arguments into a Bundle (for non-primitive objects you should use Parcelables).
    // Set this Bundle as arguments with setArguments() to the fragment instance.
    // You extract the information in the onCreate() method of your fragment.
    // Android takes care to reconstruct the arguments when recreating your fragment.
    public static FriendDetailFragment newInstance(long id) {
        FriendDetailFragment f = new FriendDetailFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.KEY_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        detailId = b.getLong(Constants.KEY_ID, -1L);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate your layout and return this at the end of the method
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        txtFriendName = (TextView)rootView.findViewById(R.id.txt_name);
        txtGiftName = (TextView)rootView.findViewById(R.id.txt_gift_name);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.ACTION_DETAILRESULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localReceiver, filter);
        // start the service to retriev data:
        Intent serviceIntent = new Intent(getActivity(), GiftlistService.class);
        serviceIntent.putExtra(Constants.KEY_ID, detailId);
        serviceIntent.setAction(GiftlistService.ACTION_LOAD_DETAILS);
        getActivity().startService(serviceIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localReceiver);
    }

        // The Receiver to get the result from the service
    BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_DETAILRESULT.equals(intent.getAction())) {
                ContactInformation info = intent.getParcelableExtra(Constants.KEY_CONTACTINFO);
                Log.v("training", "result: " + info.name);
                txtFriendName.setText(info.name);
            }
        }
    };

}
