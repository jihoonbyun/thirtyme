package com.example.jb.thirtyme.Login;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.jb.thirtyme.R;

import android.support.v4.app.FragmentManager;

public class LoginActivity extends AppCompatActivity implements SignupEmailFragment
{
   @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);

       Button email_signup_btn = findViewById(R.id.signup_email);
       email_signup_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FragmentManager fm = getSupportFragmentManager();
               SignupEmailFragment dialogFragment = new SignupEmailFragment();
               dialogFragment.show(fm, "fragment_dialog_test");

               // remove dialog title
               getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

               // remove dialog background
               getDialog().getWindow().setBackgroundDrawable(
                       new ColorDrawable(android.graphics.Color.TRANSPARENT));

           }
       });

   }
}

