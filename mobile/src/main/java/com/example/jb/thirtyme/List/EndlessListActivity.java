package com.example.jb.thirtyme.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.jb.thirtyme.R;
import com.example.jb.thirtyme.Volley.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Integer;
import java.lang.InterruptedException;
import java.lang.Override;
import java.lang.String;
import java.lang.Thread;
import java.lang.Void;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danielme.com
 */
public class EndlessListActivity extends AppCompatActivity {

    private List<Item> contents;
    private boolean hasMore;
    private AsyncTask asyncTask;
    private RecyclerView recyclerView;
    private int breadcomb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contents= buildContent();

        hasMore = true;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerFeedViewAdapter(contents, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (contents.get(position) instanceof Content) {
                    Toast toast = Toast.makeText(EndlessListActivity.this, String.valueOf(position), Toast.LENGTH_SHORT);
                    int content= android.graphics.Color.parseColor(((Content) contents.get(position)).getImagepath());
                    toast.getView().setBackgroundColor(content);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
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
        JSONObject result = volley.jsonget("http://www.naver.com/dev/blahblah/id=?&pagenum=?");
        JSONArray jsons = null;
        try {
            jsons = result.getJSONArray("data");
            for(int i= 0; i <result.length(); i++){
                JSONObject object = jsons.getJSONObject(i);
                String username = object.getString("username");
                String imagepath = object.getString("imagepath");
                String hashtag = object.getString("hashtag");
                int followint = object.getInt("followint");
                int likeint = object.getInt("likeint");
                contents.add(new Content(username, imagepath, hashtag, followint,likeint));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return contents;
    }

    //returns the string hex value of a color in colors.xml
    private String getColorString(int content) {
        return "#" + Integer.toHexString(getResources().getColor(content)).toUpperCase().substring(2);
    }

}