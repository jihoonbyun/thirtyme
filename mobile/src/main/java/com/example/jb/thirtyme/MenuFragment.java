package com.example.jb.thirtyme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jb.thirtyme.Feed.FeedActivity;
import com.example.jb.thirtyme.Main.MainActivity;

/**
 * Created by jb on 2016-02-26.
 */
public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View fragmentview =  inflater.inflate(R.layout.fragment_bottombar, container, false);
        TextView feedbtn = (TextView) fragmentview.findViewById(R.id.feedbtn);
        feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), FeedActivity.class);
                startActivity(intent);
            }
        });

     return fragmentview;
    }
}
