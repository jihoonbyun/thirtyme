package com.example.jb.thirtyme.Login;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.jb.thirtyme.Main.MainActivity;
import com.example.jb.thirtyme.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.pm.Signature;

public class LoginActivity extends AppCompatActivity
{
    CallbackManager callbackManager;
    @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        //Facebook login
       callbackManager = CallbackManager.Factory.create();
       LoginButton facebookbtn = (LoginButton) findViewById(R.id.button_fb_login);
        facebookbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Set wtf = new HashSet();
                wtf = loginResult.getRecentlyGrantedPermissions();
                Iterator iterator = wtf.iterator();
                while(iterator.hasNext()){
                    String el = (String) iterator.next();
                    Log.d("PPPPP", el);
                }

                Log.d("activity-success!!!", loginResult.getAccessToken().getToken().toString());
            }

            @Override
            public void onCancel() {
                Log.d("oncancel!!!", "hahahah");
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Error", "lalalal");
                finish();
            }
        });

        //signup
       Button email_signup_btn =(Button) findViewById(R.id.signup_email);
       email_signup_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               FragmentTransaction ft = getFragmentManager().beginTransaction();
               android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
               if(prev != null){
                   ft.remove(prev);
               }
                ft.addToBackStack(null);

               DialogFragment newFragment   = SignupEmailFragment.newInstance();
               newFragment.show(ft, "dialog");



           }
       });

        Button email_login = (Button) findViewById(R.id.login_email);
        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

   }
    public void generateKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature  : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("HASH KEY : ", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        }
        catch (PackageManager.NameNotFoundException e){

        }
        catch(NoSuchAlgorithmException E){

        }

    }
}

