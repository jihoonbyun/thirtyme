package com.example.jb.thirtyme.Login;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jb.thirtyme.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jb on 2016-02-09.
 */
public class SignupEmailFragment extends DialogFragment {

    public SignupEmailFragment(){};

    private Button signup_complete_btn;
    public EditText email;
    private EditText password;
    private EditText passwordcheck;
    private EditText nickname;

    public static SignupEmailFragment newInstance(){
        SignupEmailFragment fragment = new SignupEmailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_signupemail, container,false);
    }
    @Override
    public void onViewCreated(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.fragment_signup_email, container, false);
        super.onViewCreated(fragmentView, savedInstanceState);
        signup_complete_btn = (Button) fragmentView.findViewById(R.layout.email_signup_complete);
        email = (EditText) fragmentView.findViewById(R.layout.email_signup_email);
        password = (EditText) fragmentView.findViewById(R.layout.email_signup_password);
        passwordcheck = (EditText) fragmentView.findViewById(R.layout.email_signup_passwordcheck);
        nickname = (EditText) fragmentView.findViewById(R.layout.email_signup_nickname);

        signup_complete_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //check fields
                String EMAIL = email.getText().toString();
                String PASSWORD = password.getText().toString();
                String PASSWORDCHECK = passwordcheck.getText().toString();
                String NICKNAME = nickname.getText().toString();

                if (TextUtils.isEmpty(EMAIL)) {
                    email.setError("이메일을 입력해주세요");
                    return;
                } else {
                    Boolean isVaidMail = isEmail(EMAIL);
                    if (!isVaidMail) {
                        Toast.makeText(this, "not valid email", 10).show();
                    } else {
                        if (TextUtils.isEmpty(PASSWORD)) {
                            email.setError("패스워드 입력해주세요");
                            return;
                        } else {
                            if (TextUtils.isEmpty(PASSWORDCHECK)) {
                                email.setError("패스워드를 확인해주세요");
                                return;
                            } else {
                                if (PASSWORD.length() < 4) {
                                    Toast.makeText(this, "password should be at least 4+ ", 10).show();
                                    return;
                                } else {
                                    if (!PASSWORD.equals(PASSWORDCHECK)) {
                                        Toast.makeText(this, "password not same!", 10).show();
                                        return;
                                    } else {
                                        if (TextUtils.isEmpty(NICKNAME)) {
                                            email.setError("닉네임을 입력해주세요");
                                            return;
                                        } else {

                                            //SUCCESS!!!
                                            //GO SERVER!


                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }

        });
    }

    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }

    public static void signupEmail(String requsetUrl, String requestBody){
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
            JsonObjectRequest email_signup_request= new JsonObjectRequest(Request.Method.POST, requsetUrl, null, responseListen, responseFailed);
            {

                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("site", "code");
                    params.put("network", "tutsplus");
                    return params;
                }

            }

            //queing
            Volley.newRequestQueue(this).add(email_signup_request);
    }

}
