package com.example.jb.thirtyme.Login;

/**
 * Created by jb on 2016-02-08.
 */
import android.content.BroadcastReceiver;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.*;


import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.example.jb.thirtyme.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPageFragment extends Fragment {
    public static final String TAG = "LoginPageFragment";
    private LoginButton mFacebookLoginButton;
    private callbackManager fbCallbackManger;
    private GraphRequest mgGraphRequest;

    private AccessTokenTracker mAccessTokenTracker;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private OnLoginPageFragmentInteractionListener mLoginPageFragmentInteractionListener;
    private SessionCallback mSessionCallback;


    public LoginPageFragment() {
        //constructor!
    }

    public static LoginPageFragment newInstance() {
        LoginPageFragment fragment = new LoginPageFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(view, savedInstanceState);
        configureFacebookLoginButton(view);
        configureFacebookButtonSize();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Preference.REGISTRATION_COMPLETE));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginPageFragmentInteractionListener = null;
        if (mAccessTokenTracker != null) {
            mAccessTokenTracker.stopTracking();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        fbCallbackManger.onActivityResult(RequestCode, resultCode, data);
        super.onActivityResult(requestCode, ResultCode, data);
        Log.d(TAG, "onActivityResult");
    }

    public void configureFacebookLoginButtonSize() {
        float fbIconScale = 1.45F;
        Drawable drawable = getDrawble(com.facebook.R.drawble.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale), (int) (drawable.getIntrinsicHeight() * fbIconScale));
        mFacebookLoginButton.setCompoundDrawables(drawable, null, null, null);
        mFacebookLoginButton.setCompoundDrawblePadding(17dp);
        mFacebookLoginButton.setPadding(10dp, 13dp, 0, 13dp);
    }

    public void configureFacebookLoginButton(View rootView) {
        mFacebookLoginButton = (LoginButton) rootView.findViewById(R.id.button_fb_login);
        mFacebookLoginButton.setAlpha(0, 0F);
        mFacebookLoginButton.setVisibility(View.GONE);
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.setFragment(this);
        fbCallbackManger = CallbackManager.Factory.create();
        mFacebookLoginButton.registerCallback(fbCallbackManger, new FacebookCallback<LoginResukt>())
        {
            @Override
            public void onSuccess (LoginResult loginresult){
            hideLoginButtons();
            CookieGenerator.getInstance().setSnsId(loginResult.getAccessToken().getUserId());
            UserSignUpInfoHolder userSignUpInfoHolder = UserSignUpInfoHolder.getInstance();
            UserSignUpInfoHolder.setAccessToken(loginResult.getAccessToken().getToken());

            facebooklogin("http://www.thirtyme.com/android/login/facebook", userSignUpInfoHolder.getLoginRequestBody().toString());
        }
            @Override
            public void onCancel () {

        }
            @Override
            public void onError () {

        }
        }
    }

    private void facebooklogin(String requestUrl, String requestBody) {

        //param1
        Response.Listener responseListen = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //param2
        Response.ErrorListener responseFailed = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
        //request
        JsonObjectRequest facebook_login_request = new JsonObjectRequest(Request.Method.POST, requestUrl, null, responseListen, responseFailed);
        {

            @Override
            protected Map<String, String> getParams ()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("site", "code");
                params.put("network", "tutsplus");
                return params;
            }

        }

        //queing
        Volley.newRequestQueue(this).add(facebook_login_request);
    }

    private void writeUserNumberInSharedPreference(int userNumber) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.edit().putInt(MTPleasePreferences.PREF_USER_NO, userNumber).apply();
    }

    private void callMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void doFacebookLogin() {
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    CookieGenerator.getInstance().setSnsId(currentAccessToken.getUserId());
                    UserSignUpInfoHolder.getInstance().setAccessToken(currentAccessToken.getToken());
                }
            }
        };

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            UserSignUpInfoHolder.getInstance().setAccessToken(accessToken.getToken());
            CookieGenerator.getInstance().setSnsId(accessToken.getUserId());

            login(getString(R.string.api_facebook_login_url), UserSignUpInfoHolder.getInstance().getLoginRequestBody().toString());
        } else {
            Log.d(TAG, "Access Token is null");
            showLoginButtons();
        }
    }

    public interface OnLoginPageFragmentInteractionListener {
        void onShowSignUpPage();
    }

}


