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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GiftlistAdapter extends RecyclerView.Adapter<GiftlistAdapter.GiftlistViewHolder> {

    List<PseudoData.ContactInformation> items;
    View.OnClickListener listener;

    public GiftlistAdapter(List<PseudoData.ContactInformation> items, View.OnClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public GiftlistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View itemview = inflater.inflate(R.layout.single_giftlist_item, viewGroup, false);
        GiftlistViewHolder viewHolder = new GiftlistViewHolder(itemview);
        viewHolder.txtGifts = (TextView)itemview.findViewById(R.id.txt_gifts);
        viewHolder.txtName = (TextView)itemview.findViewById(R.id.txt_name);
        viewHolder.itemView.setOnClickListener(listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GiftlistViewHolder giftlistViewHolder, int i) {
        PseudoData.ContactInformation info = items.get(i);
        giftlistViewHolder.txtGifts.setText(Integer.toString(info.noOfGifts)) ;
        giftlistViewHolder.txtName.setText(info.name);
        giftlistViewHolder.itemView.setTag(items.get(i));
        giftlistViewHolder.itemView.setTag(info);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        else {
            return items.size();
        }
    }

    public static final class GiftlistViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtGifts;
        public GiftlistViewHolder(View itemView) {
            super(itemView);
        }
    }

}
