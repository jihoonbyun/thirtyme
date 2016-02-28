package com.example.jb.thirtyme.Feed;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jb.thirtyme.R;

import java.io.File;

/**
 * Created by jb on 2016-02-17.
 */
/*
public class FeedCamera extends AppCompatActivity {

    Button button;
    ImageView imageView;
    static final int CAM_REQUEST =1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.id.activity_camera);
        button = (Button) findViewById(R.id.button);
        imageView =  findViewById(R.id.image_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);


            }
        });
    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");
        if(!folder.exists()){
            folder.mkdir();
        }

        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = "sdcard/camera_app/camera_image.jpg";
        imageView =  findViewById(R.id.image_view);
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }
}
*/