package com.example.jb.thirtyme.Login;

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

public class LoginActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        LoginPageFragment.OnLoginPageFragmentInteractionListener,
        SignUpPageOneFragment.OnCheckAgreeToAccessTermsCheckBoxListener,
        SignUpPageTwoFragment.OnTypeEditTextListener,
        SignUpPageThreeFragment.OnCheckRadioButtonListener,
        SignUpPageFourFragment.OnCheckUserUsualMonthsOfMTListener {
    private static final String TAG = "LoginActivity";

    private static final int NUMBER_OF_PAGES = 5;
    private static final int LOGIN_PAGE_POSITION = 0;
    private static final int SIGN_UP_PAGE_1 = 1;
    private static final int SIGN_UP_PAGE_2 = 2;
    private static final int SIGN_UP_PAGE_3 = 3;
    private static final int SIGN_UP_PAGE_4 = 4;

    private Button mConfirmButton;
    private TextView mGoNextTextView;
    private TextView mGoPreviousTextView;

    private RadioGroup mPageIndicatorRadioGroup;
    private RadioButton mPageOneRadioButton;
    private RadioButton mPageTwoRadioButton;
    private RadioButton mPageThreeRadioButton;

    private LoginActivityViewPager mSignUpPageFragmentViewPager;
    private SignUpPageFragmentPagerAdapter mSignUpPageFragmentPagerAdapter;
    private int mCurrentPage = 0;
    private int mCurrentPosition = LOGIN_PAGE_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check whether it is new installation or not
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(MTPleasePreferences.PREF_NEWLY_INSTALLED_USER, true)) {
            sharedPreferences.edit().putBoolean(MTPleasePreferences.PREF_NEWLY_INSTALLED_USER, true).apply();
        }

        configurePageIndicatorView();
        configureFragmentViewPager();
        configureButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!checkAutoLoginState()) {
            LoginManager.getInstance().logOut();
        }
        super.onBackPressed();
    }

    private boolean checkAutoLoginState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userNumber = sharedPreferences.getInt(MTPleasePreferences.PREF_USER_NO, -1);

        Log.d(TAG, "Saved user number:" + userNumber);

        // check if user number is saved in sharedPreference or not(which means the user already login to the service)
        return userNumber != -1;
    }

    private void configurePageIndicatorView() {
        mPageIndicatorRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_page_indicator_page_sign_up);
        mPageIndicatorRadioGroup.setAlpha(0.0F);
        mPageIndicatorRadioGroup.setClickable(false);
        mPageIndicatorRadioGroup.setVisibility(View.GONE);
        mPageIndicatorRadioGroup.setFocusable(true);
        mPageIndicatorRadioGroup.setFocusableInTouchMode(true);
        mPageOneRadioButton = (RadioButton) findViewById(R.id.radioButton_page_1_page_sign_up);
        mPageTwoRadioButton = (RadioButton) findViewById(R.id.radioButton_page_2_page_sign_up);
        mPageThreeRadioButton = (RadioButton) findViewById(R.id.radioButton_page_3_page_sign_up);
    }

    private void configureButtons() {
        mConfirmButton = (Button) findViewById(R.id.button_confirm_access_terms_page_login);
        mConfirmButton.setVisibility(View.GONE);
        mConfirmButton.setAlpha(0.0F);
        mConfirmButton.setClickable(false);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserSignUpInfoHolder.getInstance().isAgreeToAccessTermsCheckBoxChecked()) {
                    mGoPreviousTextView.setClickable(true);
                    mGoPreviousTextView.setVisibility(View.VISIBLE);
                    mGoNextTextView.setClickable(true);
                    mGoNextTextView.setVisibility(View.VISIBLE);

                    mPageIndicatorRadioGroup.setVisibility(View.VISIBLE);

                    mSignUpPageFragmentViewPager.setCurrentItem(++mCurrentPage, true);
                } else {
                    Toast.makeText(v.getContext(), R.string.please_agree_the_terms, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGoNextTextView = (TextView) findViewById(R.id.textView_to_next_page_sign_up);
        mGoNextTextView.setVisibility(View.GONE);
        mGoNextTextView.setAlpha(0.0F);
        mGoNextTextView.setClickable(false);
        mGoNextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageIndicatorRadioGroup.requestFocus();
                UserSignUpInfoHolder userSignUpInfoHolder = UserSignUpInfoHolder.getInstance();

                JSONArray userSocietyNames = userSignUpInfoHolder.getUserGroup();

                switch (mCurrentPage) {
                    case SIGN_UP_PAGE_2:
                        if (isValidEmail(userSignUpInfoHolder.getEmailAddress()) &&
                                doInputAtLeastOneSocietyNames(userSocietyNames) &&
                                doInputSocietyNameProperly(userSocietyNames)) {
                            break;
                        } else if (!isValidEmail(userSignUpInfoHolder.getEmailAddress()) &&
                                doInputAtLeastOneSocietyNames(userSocietyNames) &&
                                doInputSocietyNameProperly(userSocietyNames)) {
                            Toast.makeText(v.getContext(), R.string.please_type_in_email_address, Toast.LENGTH_SHORT).show();
                        } else if (isValidEmail(userSignUpInfoHolder.getEmailAddress()) &&
                                !doInputAtLeastOneSocietyNames(userSocietyNames) &&
                                !doInputSocietyNameProperly(userSocietyNames)) {
                            Toast.makeText(v.getContext(), R.string.please_type_in_user_society, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), R.string.please_fill_in_all_the_blanks, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    case SIGN_UP_PAGE_3:
                        if (userSignUpInfoHolder.getGender() != -1 && userSignUpInfoHolder.getUserJob() != -1) {
                            break;
                        } else if (userSignUpInfoHolder.getGender() == -1 && userSignUpInfoHolder.getUserJob() != -1) {
                            Toast.makeText(v.getContext(), R.string.please_select_your_gender, Toast.LENGTH_SHORT).show();
                        } else if (userSignUpInfoHolder.getGender() != -1 && userSignUpInfoHolder.getUserJob() == -1) {
                            Toast.makeText(v.getContext(), R.string.please_select_your_occupation, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), R.string.please_select_unselected, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    case SIGN_UP_PAGE_4:
                        for (int i = 0; i < 12; i++) {
                            if (userSignUpInfoHolder.getCheckedMonthList()[i]) {
                                break;
                            } else if (i == 11 && !userSignUpInfoHolder.getCheckedMonthList()[11]) {
                                Toast.makeText(v.getContext(), R.string.please_select_at_least_one_month, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                }
                if (mCurrentPage != SIGN_UP_PAGE_4) {
                    mSignUpPageFragmentViewPager.setCurrentItem(++mCurrentPage, true);
                } else {
                    if (userSignUpInfoHolder.getLoginType() == UserSignUpInfoHolder.KAKAO_LOGIN) {
                        signUp(getResources().getString(R.string.api_kakao_login_url), userSignUpInfoHolder.getSignUpRequestBody().toString());
                    } else {
                        signUp(getResources().getString(R.string.api_facebook_login_url), userSignUpInfoHolder.getSignUpRequestBody().toString());
                    }
                }
            }
        });
        mGoPreviousTextView = (TextView) findViewById(R.id.textView_to_previous_page_sign_up);
        mGoPreviousTextView.setVisibility(View.GONE);
        mGoPreviousTextView.setAlpha(0.0F);
        mGoPreviousTextView.setClickable(false);
        mGoPreviousTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage - 1 == SIGN_UP_PAGE_1) {
                    mConfirmButton.setClickable(true);
                    mConfirmButton.setVisibility(View.VISIBLE);
                }

                mSignUpPageFragmentViewPager.setCurrentItem(--mCurrentPage, true);
            }
        });
    }


    private void signUp(String requestUrl, String requestBody) {
        Log.d(TAG, "signUp called");
        Volleyer.volleyer().post(requestUrl).addHeader("Content-Type", "application/json").withBody(requestBody)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response + " / " + "Login successful");
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            writeUserNumberInSharedPreference(responseObject.optInt("userNo"));
                            sendUserDataToGA(responseObject.optInt("userNo"));
                            callMainActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).withErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Login failed");
                Toast.makeText(LoginActivity.this, R.string.fail_to_sign_up, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void writeUserNumberInSharedPreference(int userNumber) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putInt(MTPleasePreferences.PREF_USER_NO, userNumber).apply();
    }

    private void sendUserDataToGA(int userNo) {
        String userGroup = "";

        JSONArray temp = UserSignUpInfoHolder.getInstance().getUserGroup();
        for (int i = 0; i < temp.length(); i++) {
            if (i == 0) {
                userGroup += temp.optString(i);
            } else {
                userGroup += " / " + temp.optString(i);
            }
        }

        // get GA tracker from application class
        ApplicationClass application = (ApplicationClass) getApplication();
        Tracker tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder().setCategory(getResources().getString(R.string.ga_user_event_category_3))
                .setLabel("User no." + userNo + "/ User group: " + userGroup).build());
    }

    private void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void configureFragmentViewPager() {
        mSignUpPageFragmentViewPager = (LoginActivityViewPager) findViewById(R.id.viewPager_page_sign_up);
        mSignUpPageFragmentViewPager.setSwipingEnabled(false);
        mSignUpPageFragmentViewPager.setOffscreenPageLimit(1);
        mSignUpPageFragmentPagerAdapter = new SignUpPageFragmentPagerAdapter(getSupportFragmentManager());
        mSignUpPageFragmentViewPager.setAdapter(mSignUpPageFragmentPagerAdapter);
        mSignUpPageFragmentViewPager.setCurrentItem(LOGIN_PAGE_POSITION);
        mSignUpPageFragmentViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
            case LOGIN_PAGE_POSITION:
                if (positionOffset <= 0.99F) {
                    mConfirmButton.setAlpha(positionOffset);
                }
                return;
            case SIGN_UP_PAGE_1:
                if (positionOffset <= 0.99F) {
                    mConfirmButton.setAlpha((1 - positionOffset));
                    mPageIndicatorRadioGroup.setAlpha(positionOffset);
                    mGoPreviousTextView.setAlpha(positionOffset);
                    mGoNextTextView.setAlpha(positionOffset);
                }
                return;
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;

        switch (position) {
            case SIGN_UP_PAGE_2:
                mPageOneRadioButton.setChecked(true);
                return;
            case SIGN_UP_PAGE_3:
                mPageTwoRadioButton.setChecked(true);
                mGoNextTextView.setText(R.string.to_next);
                mGoNextTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                return;
            case SIGN_UP_PAGE_4:
                mPageThreeRadioButton.setChecked(true);
                mGoNextTextView.setText(R.string.the_end);
                mGoNextTextView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.go_next_button_width_page_login);
                return;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            switch (mCurrentPosition) {
                case LOGIN_PAGE_POSITION:
                    mConfirmButton.setAlpha(0.0F);
                    mConfirmButton.setClickable(false);
                    mConfirmButton.setVisibility(View.GONE);
                    mPageIndicatorRadioGroup.setAlpha(0.0F);
                    mPageIndicatorRadioGroup.setVisibility(View.GONE);
                    mGoPreviousTextView.setAlpha(0.0F);
                    mGoPreviousTextView.setClickable(false);
                    mGoPreviousTextView.setVisibility(View.GONE);
                    mGoNextTextView.setAlpha(0.0F);
                    mGoNextTextView.setClickable(false);
                    mGoNextTextView.setVisibility(View.GONE);
                    return;
                case SIGN_UP_PAGE_1:
                    mPageIndicatorRadioGroup.setAlpha(0.0F);
                    mPageIndicatorRadioGroup.setVisibility(View.GONE);
                    mGoPreviousTextView.setAlpha(0.0F);
                    mGoPreviousTextView.setClickable(false);
                    mGoPreviousTextView.setVisibility(View.GONE);
                    mGoNextTextView.setAlpha(0.0F);
                    mGoNextTextView.setClickable(false);
                    mGoNextTextView.setVisibility(View.GONE);
                    return;
                case SIGN_UP_PAGE_2:
                    mConfirmButton.setClickable(false);
                    mConfirmButton.setAlpha(0.0F);
                    mConfirmButton.setVisibility(View.GONE);
                    return;
            }
        }
    }

    private class SignUpPageFragmentPagerAdapter extends FragmentPagerAdapter {

        public SignUpPageFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case LOGIN_PAGE_POSITION:
                    return LoginPageFragment.newInstance();
                case SIGN_UP_PAGE_1:
                    return SignUpPageOneFragment.newInstance();
                case SIGN_UP_PAGE_2:
                    return SignUpPageTwoFragment.newInstance();
                case SIGN_UP_PAGE_3:
                    return SignUpPageThreeFragment.newInstance();
                case SIGN_UP_PAGE_4:
                    return SignUpPageFourFragment.newInstance();
                default:
                    return null;
            }
        }
    }

    public boolean isValidEmail(String email) {
        boolean err = false;

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        if (m.matches()) {
            err = true;
        }
        return err;
    }

    public boolean doInputAtLeastOneSocietyNames(JSONArray societyNames) {
        if (societyNames.optJSONObject(0) != null) {
            String temp = societyNames.optJSONObject(0).optString("societyName");
            if (temp.matches("\\s+") || temp.length() == 0 || temp.equals("")) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean doInputSocietyNameProperly(JSONArray societyNames) {
        for (int i = 0; i < societyNames.length(); i++) {
            String temp = societyNames.optJSONObject(i).optString("societyName");
            Log.d(TAG, "Society:" + temp);
            if (temp.matches("\\s+") || temp.length() == 0 || temp.equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method from LoginPageFragment
     */
    @Override
    public void onShowSignUpPage() {
        mSignUpPageFragmentViewPager.setCurrentItem(++mCurrentPage, true);
        mConfirmButton.setClickable(true);
        mConfirmButton.setVisibility(View.VISIBLE);
    }

    /**
     * Method from SignUpPageOneFragment
     *
     * @param isChecked whether the checkbox is checked or not
     */
    @Override
    public void onCheckAgreeToAccessTermsCheckBox(boolean isChecked) {
        UserSignUpInfoHolder.getInstance().setIsAgreeToAccessTermsCheckBoxChecked(isChecked);
    }

    /**
     * Method from SignUpPageTwoFragment
     *
     * @param emailAddress emailAddress that the user typed in
     */
    @Override
    public void onTypeEmailAddress(String emailAddress) {
        UserSignUpInfoHolder.getInstance().setEmailAddress(emailAddress);
    }

    /**
     * Method from SignUpPageTwoFragment
     */
    @Override
    public void onTypeUserGroupOne(int index, String groupName) {
        UserSignUpInfoHolder.getInstance().addUserGroup(index, groupName);
    }

    /**
     * Method from SignUpPageTwoFragment
     */
    @Override
    public void onTypeUserGroupTwo(int index, String groupName) {
        UserSignUpInfoHolder.getInstance().addUserGroup(index, groupName);
    }


    /**
     * Method from SignUpPageTwoFragment
     */
    @Override
    public void onTypeUserGroupThree(int index, String groupName) {
        UserSignUpInfoHolder.getInstance().addUserGroup(index, groupName);
    }


    /**
     * Method from SignUpPageThreeFragment
     *
     * @param gender gender that the user chosen
     */
    @Override
    public void onCheckGenderRadioButton(int gender) {
        UserSignUpInfoHolder.getInstance().setGender(gender);
    }

    /**
     * Method from SignUpPageThreeFragment
     *
     * @param userOccupation occupation of the user that user chosen
     */
    @Override
    public void onCheckUserOccupationRadioButton(int userOccupation) {
        UserSignUpInfoHolder.getInstance().setUserJob(userOccupation);
    }

    /**
     * Method from SignUpPageFourFragment
     *
     * @param checkedMonthList list of months that the user checked
     */
    @Override
    public void onCheckUserUsualMonthsOfMT(boolean[] checkedMonthList) {
        UserSignUpInfoHolder.getInstance().setCheckedMonthList(checkedMonthList);
    }


    ///////////////////////////////////////////////////////////////////////////////
    private void sendNotification(String roomInfoData, String message, String userInfoData) {
        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        builder.setColor(getResources().getColor(R.color.mtplease_color_primary));
        builder.setContentTitle(getResources().getString(R.string.app_name));
        builder.setContentText(message);
        builder.setLights(0x58C6FF, 1000, 2000);
        long[] vibratePattern = {0, 1500};
        builder.setVibrate(vibratePattern);
        builder.setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setSummaryText(getString(R.string.app_name));
        bigTextStyle.setBigContentTitle("후기를 부탁해!");
        bigTextStyle.bigText("엠티 잘 다녀오셨나요?? " + "잉여방에 대한 후기를 적어주시면 감사하겠습니다~^^");
        builder.setStyle(bigTextStyle);

        // Creates an Intent for the Activity
        Intent notifyIntent = new Intent(this, ReviewActivity.class);
        notifyIntent.putExtra(MTPleaseGcmListenerService.REVIEW_INTENT_FLAG_USER_INFO, userInfoData);
        notifyIntent.putExtra(MTPleaseGcmListenerService.REVIEW_INTENT_FLAG_ROOM_INFO, roomInfoData);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyPendingIntent);
        // Notifications are issued by sending them to the
        // NotificationManager system service.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds an anonymous Notification object from the builder, and-
        // passes it to the NotificationManager
        notificationManager.notify(1, builder.build());
    }

    private void loadUserInfoFromServer(final String roomInfo, final String message) {
        Volleyer.volleyer().get(getResources().getString(R.string.api_root_url) + "user/info")
                .addHeader("Cookie", "session=\"" + CookieGenerator.getInstance().generateCookie() + "\"")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response : " + response);
                        sendNotification(roomInfo, message, response);
                    }
                }).withErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }).execute();
    }
}

