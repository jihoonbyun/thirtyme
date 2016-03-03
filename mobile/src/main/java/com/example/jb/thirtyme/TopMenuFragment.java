package com.example.jb.thirtyme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jb.thirtyme.Feed.FeedActivity;

/**
 * Created by jb on 2016-02-28.
 */
public class TopMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View fragmentview =  inflater.inflate(R.layout.fragment_topbar, container, false);
        TextView searchbtn = (TextView) fragmentview.findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            }
        });

        return fragmentview;
    }
}
