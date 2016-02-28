package com.example.jb.thirtyme.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jb.thirtyme.R;

/**
 * Created by jb on 2016-02-23.
 */
import java.util.ArrayList;
import java.util.List;

final class SampleImagesAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;

    public SampleImagesAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        mItems.add(new Item("sample1","4,000", "http://i.imgur.com/DvpvklR.png"));
        mItems.add(new Item("sample2","3,000","http://i.imgur.com/DvpvklR.png"));
        mItems.add(new Item("sample3","2,000", "http://i.imgur.com/DvpvklR.png"));
        mItems.add(new Item("sample4","1,000", "http://i.imgur.com/DvpvklR.png"));

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView pname;
        TextView price;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.pname, v.findViewById(R.id.pname));
            v.setTag(R.id.price, v.findViewById(R.id.price));
        }

        picture = (ImageView) v.findViewById(R.id.picture);
        pname = (TextView) v.getTag(R.id.pname);
        price = (TextView) v.getTag(R.id.price);

        Item item = getItem(i);

        pname.setText(item.name);
        price.setText(item.price);
        Glide.with(v.getContext()).load(item.picture).placeholder(R.drawable.image_photo_loading).error(R.drawable.image_photo_unavailable).centerCrop().crossFade().into(picture) ;


        return v;
    }

    private static class Item {
        public final String name;
        public final String price;
        public final String picture;

        Item(String name, String price, String picture) {
            this.name = name;
            this.price=price;
            this.picture = picture;
        }
    }
}
