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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.openminds.training.giftsforfriends.domain.ContactInformation;

// For more information about how to properly create fragments see the FriendDetailFragment.
public class FriendsListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv;
    private GiftlistAdapter adapter;
    private FriendSelectedListener listener;

    public static interface FriendSelectedListener {
        void onFriendSelected(long id);
    }

    public static FriendsListFragment newInstance() {
        FriendsListFragment f = new FriendsListFragment();
        // without arguments you can skip the Bundle part, of course
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_giftlist, container, false);

        // RecyclerView - you need to set the adapter and a layoutmanager
        // Kepp in mind. We load the data asynchronously and do not have
        // it ready yet. Thus I had to add the adapter as a instance variable
        // and add data later on.
        rv = (RecyclerView)rootView.findViewById(R.id.recyclerview_giftlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new GiftlistAdapter(null, this);
        rv.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FriendSelectedListener) {
            listener = (FriendSelectedListener)activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.ACTION_LISTRESULT);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localReceiver, filter);
        // start the service to retriev data:
        Intent serviceIntent = new Intent(getActivity(), GiftlistService.class);
        serviceIntent.setAction(GiftlistService.ACTION_LOAD_LIST);
        getActivity().startService(serviceIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_single_giftlistitem) {
            // start detail activity
            ContactInformation info = (ContactInformation)v.getTag();
            if (info != null && listener != null) {
                listener.onFriendSelected(info.id);
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
