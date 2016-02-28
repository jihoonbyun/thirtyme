package com.example.jb.thirtyme.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jb.thirtyme.R;
import com.like.LikeButton;

import java.lang.Override;
import java.lang.RuntimeException;
import java.util.List;

/**
 * @author danielme.com
 */

public class RecyclerFeedViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Item> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    private static final int TYPE_TOPMENU = 0;
    private static final int TYPE_ONGOING = 1;
    private static final int TYPE_FOOTER = 2;

    public RecyclerFeedViewAdapter(@NonNull List<Item> data, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;


    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0) {
            return TYPE_TOPMENU;
        }
        else {
            if (data.get(position) instanceof Content) {
                return TYPE_ONGOING;
            } else if (data.get(position) instanceof Footer) {
                return TYPE_FOOTER;
            } else {
                throw new RuntimeException("ItemViewType unknown");
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_TOPMENU){

            View menu  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed_menu, parent, false);
            FeedMenuViewHolder fmv = new FeedMenuViewHolder(menu, recyclerViewOnItemClickListener);
            return fmv;

        } else if (viewType == TYPE_ONGOING) {

            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_row, parent, false);
            PostingViewHolder pvh = new PostingViewHolder(row, recyclerViewOnItemClickListener);
            return pvh;
        } else {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_progress, parent, false);
            FooterViewHolder vh = new FooterViewHolder(row);
            return vh;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof PostingViewHolder) {

            Log.i("reclycler POSITION",String.valueOf(position));

            if(position == 0){
                Log.i("position = 0?", "yes");

            }
            Content content = (Content) data.get(position);
            PostingViewHolder postingViewHolder = (PostingViewHolder) holder;
            postingViewHolder.getUsername().setText(String.valueOf(content.getName()));
            //ImageView iv = postingViewHolder.getImage();
            //Glide.with().load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(iv);
            postingViewHolder.getHashtag().setText(String.valueOf(content.getHashtag()));
            //postingViewHolder.getLikecnt().setText(String.valueOf(content.getLikecnt()));



            //like button
            /*
            postingViewHolder.getThumbButton().setLiked(true);
            postingViewHolder.getThumbButton().setIconSizeDp(10);
            postingViewHolder.getThumbButton().setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton thumbButton) {
                    //Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void unLiked(LikeButton thumbButton) {
                    //Toast.makeText(this, "UnLiked!", Toast.LENGTH_SHORT).show();
                }
            });
            */



        }
        //FOOTER: nothing to do

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FeedMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Button mynews_write;
        Button feedsort;
        Button feedfilter;


        private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
        private FeedMenuViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
          super(itemView);
            mynews_write = (Button) itemView.findViewById(R.id.mynews_write);
            feedsort = (Button) itemView.findViewById(R.id.feedsort);
            feedfilter = (Button) itemView.findViewById(R.id.feedfilter);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

       @Override
       public void onClick(View v){
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public static class PostingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView username;
        private ImageView image;
        private TextView followcnt;
        private TextView likecnt;
        private TextView hashtag;
        private LikeButton thumbButton;

        private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
        public PostingViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
            super(itemView);

            username =(TextView) itemView.findViewById(R.id.username);
            image = (ImageView) itemView.findViewById(R.id.image);
            likecnt = (TextView) itemView.findViewById(R.id.likecnt);
            hashtag =(TextView) itemView.findViewById(R.id.hashtag);
           //thumbButton = (LikeButton) itemView.findViewById(R.id.thumb_button);

            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

       public TextView getUsername(){
           return username;
       }
       public ImageView getImage(){
           return image;
       }
        public TextView getLikecnt(){
            return likecnt;
        }
        public TextView getHashtag(){
            return hashtag;
        }
        //public LikeButton getThumbButton(){return thumbButton;}


        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer);
        }
    }

}