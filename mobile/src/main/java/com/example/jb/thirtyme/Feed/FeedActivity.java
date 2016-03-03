package com.example.jb.thirtyme.Feed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jb.thirtyme.List.Content;
import com.example.jb.thirtyme.List.DividerItemDecoration;
import com.example.jb.thirtyme.List.Footer;
import com.example.jb.thirtyme.List.Item;
import com.example.jb.thirtyme.List.RecyclerFeedViewAdapter;
import com.example.jb.thirtyme.List.RecyclerMainViewAdapter;
import com.example.jb.thirtyme.List.RecyclerViewOnItemClickListener;
import com.example.jb.thirtyme.R;
import com.example.jb.thirtyme.Volley.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jb on 2016-02-17.
 */
public class FeedActivity extends AppCompatActivity {
    private List<Item> contents;
    private boolean hasMore;
    private AsyncTask asyncTask;
    private RecyclerView recyclerView;
    private int breadcomb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        contents= buildContent();
        hasMore = true;
        Log.d("rrr", "main");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerFeedViewAdapter(contents, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (contents.get(position) instanceof Content) {
                    Toast toast = Toast.makeText(FeedActivity.this, String.valueOf(position), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (hasMore && !(hasFooter())) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    //position starts at 0
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 2) {

                        asyncTask = new BackgroundTask();
                        Void[] params = null;
                        asyncTask.execute(params);
                    }
                }
            }
        });
    }


    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            contents.add(new Footer());
            recyclerView.getAdapter().notifyItemInserted(contents.size() - 1);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int size = contents.size();
            contents.remove(size - 1);//removes footer

            contents.addAll(buildContent());
            recyclerView.getAdapter().notifyItemRangeChanged(size - 1, contents.size() - size);
        }

    }

    private boolean hasFooter() {
        return contents.get(contents.size() - 1) instanceof Footer;
    }





    private ArrayList<Item> buildContent() {
        ArrayList<Item> contents= new ArrayList<Item>(10);

        Volley volley = new Volley();
        //JSONObject result = volley.jsonget("http://www.naver.com/dev/blahblah/id=?&pagenum=?");


       /*
         sample data
         */
        JSONArray jarr = new JSONArray();
        for(int i=0; i < 10; i++) {
            JSONObject obj = new JSONObject();
            try{
                obj.put("username", "Brian");
                obj.put("imagepath", "http://i.imgur.com/DvpvklR.png");
                obj.put("hashtag", "#fantastic #cute #modern #sexy");
                obj.put("followcnt", 24);
                obj.put("likecnt",10);
                jarr.put(obj);

            }
            catch(JSONException E){

            }
        }
        JSONObject result = new JSONObject();
        try {
            result.put("data", jarr);
        }catch (JSONException e){

        }


        JSONArray jsons = null;
        try {
            jsons = result.getJSONArray("data");
            for(int i= 0; i <10; i++){
                JSONObject object = jsons.getJSONObject(i);
                String username = object.getString("username");
                String imagepath = object.getString("imagepath");
                String hashtag = object.getString("hashtag");
                int followcnt = object.getInt("followcnt");
                int likecnt = object.getInt("likecnt");
                contents.add(new Content(username, imagepath, hashtag, followcnt,likecnt));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return contents;
    }
}


