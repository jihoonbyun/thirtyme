package com.example.jb.thirtyme.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jb.thirtyme.Main.MainActivity;
import com.example.jb.thirtyme.R;
import com.like.LikeButton;

import java.util.List;

/**
 * @author danielme.com
 */

public class RecyclerMainViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Item> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    private static final int TYPE_ROLLING = 0;
    private static final int TYPE_FiXEDBANNER = 1;
    private static final int TYPE_THUMBS = 2;
    private static final int TYPE_ONGOING = 3;
    private static final int TYPE_FOOTER = 4;

    public RecyclerMainViewAdapter(@NonNull List<Item> data, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;


    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return TYPE_ROLLING;

        } else if(position == 1){
           return TYPE_FiXEDBANNER;
        } else if( position == 2){
           return TYPE_THUMBS;
        } else if (data.get(position) instanceof Content) {
            return TYPE_ONGOING;
        } else if (data.get(position) instanceof Footer) {
            return TYPE_FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ROLLING) {

            View rolling  =LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_rolling, parent, false);
            RollingViewHolder roll = new RollingViewHolder(rolling, recyclerViewOnItemClickListener);
            return roll;

        } else if(viewType == TYPE_FiXEDBANNER){

            View fixedbanner  =LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_fixed,parent, false);
            FixedViewHolder bn = new FixedViewHolder(fixedbanner, recyclerViewOnItemClickListener);
            return bn;

        } else if(viewType ==TYPE_THUMBS){

            View thumbs  =LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_thumbs, parent, false);
            ThumbsViewHolder th = new ThumbsViewHolder(thumbs, recyclerViewOnItemClickListener);
            return th;

        }   else if (viewType == TYPE_ONGOING) {

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

        if(position == 0){

            RollingViewHolder rollingViewHolder = (RollingViewHolder) holder;
            rollingViewHolder.setImages(holder.itemView);

        }
        else if(position == 1){
            FixedViewHolder fixedViewHolder = (FixedViewHolder) holder;
            fixedViewHolder.setImages(holder.itemView);
        }

        else if(position == 2){
            ThumbsViewHolder thumbsViewHolder = (ThumbsViewHolder) holder;
            thumbsViewHolder.setImages();
        }

        else if (holder instanceof PostingViewHolder) {


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


    public static class RollingViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView rolling1, rolling2, rolling3;
        private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

       public RollingViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
          super(itemView);

           rolling1 = (ImageView) itemView.findViewById(R.id.main_rolling_1);
           rolling2 = (ImageView) itemView.findViewById(R.id.main_rolling_2);
           rolling3 = (ImageView) itemView.findViewById(R.id.main_rolling_3);

           this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
           itemView.setOnClickListener(this);
       }

        @Override
       public void onClick(View v){
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }

       public void setImages(View v){
           //Glide.with(v.getContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(rolling1);
           //Glide.with(v.getContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(rolling2);
           //Glide.with(v.getContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(rolling3);
       }

    }

    public static class FixedViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView fixedbanner;
       private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
        public  FixedViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
            super(itemView);

            fixedbanner = (ImageView) itemView.findViewById(R.id.fixed_banner);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }

        public void setImages(View v){
            //Glide.with(v.getContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(fixedbanner);
        }
    }

    public static class ThumbsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        GridView thumbs;
        private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
        public  ThumbsViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
            super(itemView);

            thumbs = (GridView) itemView.findViewById(R.id.main_thumbs);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }

        public void setImages(){
            GridView gridView = (GridView) itemView.findViewById(R.id.main_thumbs);
            gridView.setAdapter(new SampleImagesAdapter(itemView.getContext()));
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