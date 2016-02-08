package com.example.jb.thirtyme.Login;

/**
 * Created by jb on 2016-02-08.
 */
import android.content.BroadcastReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.example.jb.thirtyme.Modification.Preference;

public class LoginPageFragment extends Fragment {
    public static final String TAG= "LoginPageFragment";
    private LoginButton mFacebookLoginButton;
    private callbackManager fbCallbackManger;
    private GraphRequest mgGraphRequest;

    private AccessTokenTracker mAccessTokenTracker;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private OnLoginPageFragmentInteractionListener mLoginPageFragmentInteractionListener;
    private SessionCallback mSessionCallback;


    public  LoginPageFragment(){
        //constructor!
    }
   public static LoginPageFragment newInstance(){
       LoginPageFragment fragment = new LoginPageFragment();
       return fragment;
   }
}

@Override
public void onCreate(Bundle savedInstanceState){
   super.onCreate(savedInstanceState);
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    super.onCreateView(view, savedInstanceState);
    configureFacebookLoginButton(view);
    configureFacebookButtonSize();
}

@Override
public void onResume(){
    super.onResume();
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Preference.REGISTRATION_COMPLETE));
}

@Override
public void onPause(){
    LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
    super.onPause();
}
    @Override
	public void onDestroy() {
		super.onDestroy();
		Session.getCurrentSession().removeCallback(mSessionCallback);
	}

@Override
public void onDetach(){
    super.onDetach();
    mLoginPageFragmentInteractionListener = null;
    if(mAccessTokenTracker != null){
        mAccessTokenTracker.stopTracking();
    }
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
    if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
        return;
    }

    fbCallbackManger.onActivityResult(RequestCode, resultCode, data);
    super.onActivityResult(requestCode, ResultCode, data);
    Log.d(TAG, "onActivityResult");
}

public void configureFacebookLoginButtonSize(){
    float fbIconScale = 1.45F;
    Drawable drawable = getDrawble(com.facebook.R.drawble.com_facebook_button_icon);
    drawable.setBounds(0,0,(int)(drawable.getIntrinsicWidth() * fbIconScale), (int)(drawable.getIntrinsicHeight()*fbIconScale));
    mFacebookLoginButton.setCompoundDrawables(drawable, null, null, null);
    mFacebookLoginButton.setCompoundDrawblePadding(17dp);
    mFacebookLoginButton.setPadding(10dp, 13dp, 0, 13dp);
}

public void configureFacebookLoginButton(View rootView){
   mFacebookLoginButton = (LoginButton)  rootView.findViewById(R.id.button_fb_login);
    mFacebookLoginButton.setAlpha(0, 0F);
    mFacebookLoginButton.setVisibility(View.GONE);
    mFacebookLoginButton.setReadPermissions("email");
    mFacebookLoginButton.setFragment(this);
   fbCallbackManger = CallbackManager.Factory.create();
    mFacebookLoginButton.registerCallback(fbCallbackManger, new FacebookCallback<LoginResukt>()){
         @Override
         public void onSuccess(LoginResult loginresult){
            hideLoginButtons();
            CookieGenerator.getInstance().setSnsId(loginResult.getAccessToken().getUserId());
            UserSignUpInfoHolder userSignUpInfoHolder = UserSignUpInfoHolder.getInstance();
            UserSignUpInfoHolder.setAccessToken(loginResult.getAccessToken().getToken());

            facebooklogin("http://www.thirtyme.com/android/login/facebook", userSignUpInfoHolder.getLoginRequestBody().toString()); }
        @Override
        public void onCancel(){

        }
        @Override
                public void onError(){

        }
    }
}

private void facebooklogin(String requestUrl,String requestBody){
    Volleyer.volleyer
}


