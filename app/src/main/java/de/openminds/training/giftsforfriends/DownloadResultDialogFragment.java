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

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import de.openminds.training.giftsforfriends.domain.Post;

public class DownloadResultDialogFragment extends DialogFragment {

    private Post post;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = getArguments().getParcelable(Constants.KEY_POST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(post.getTitle());
        View view = inflater.inflate(R.layout.fragment_downloadresult, container, false);
        TextView txtId = (TextView)view.findViewById(R.id.txt_id);
        txtId.setText(Long.toString(post.getId()));
        TextView txtUserId = (TextView)view.findViewById(R.id.txt_user_id);
        txtUserId.setText(Long.toString(post.getUserId()));
        TextView txtTitle = (TextView)view.findViewById(R.id.txt_title);
        txtTitle.setText(post.getTitle());
        TextView txtBody = (TextView)view.findViewById(R.id.txt_body);
        txtBody.setText(post.getBody());
        return view;
    }

    public static DownloadResultDialogFragment newInstance(Post post) {
        DownloadResultDialogFragment f = new DownloadResultDialogFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constants.KEY_POST, post);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        // IMHO dirty hack to set the height;
        // DialogFragment otherwise uses wrap_content - which is not wide enough in this case
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
