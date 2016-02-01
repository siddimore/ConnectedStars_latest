/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.parse.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import com.parse.twitter.Twitter;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

/**
 * Fragment for the user login screen.
 */
public class ParseLoginFragment extends ParseLoginFragmentBase implements httpParseResponse {

    public interface httpResponseListner {
        public void myCallBack();
    }

    public interface ParseLoginFragmentListener {
        public void onSignUpClicked(String username, String password);

        public void onLoginHelpClicked();

        public void onLoginSuccess();
    }

    private static final String LOG_TAG = "ParseLoginFragment";
    private static final String USER_OBJECT_NAME_FIELD = "name";

    private View parseLogin;
    private EditText usernameField;
    private EditText passwordField;
    private TextView parseLoginHelpButton;
    private Button parseLoginButton;
    private Button parseSignupButton;
    private Button facebookLoginButton;
    private Button twitterLoginButton;
    private httpResponseListner listenResponse;
    private ParseLoginFragmentListener loginFragmentListener;
    private ParseOnLoginSuccessListener onLoginSuccessListener;
    private httpParseResponse newResponse;
    private httpParseClass newhttpParseClass;
    private ParseLoginConfig config;
    private String PREF_KEY_TWITTER_LOGIN;
    public static final String mypreference = "LoginDetails";
    // Login button
    Button btnLoginTwitter;
    // Update status button
    Button btnUpdateStatus;
    // Logout button
    Button btnLogoutTwitter;
    // EditText for update
    EditText txtUpdate;
    // lbl update
    TextView lblUpdate;
    TextView lblUserName;

    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    public static ParseLoginFragment newInstance(Bundle configOptions) {
        ParseLoginFragment loginFragment = new ParseLoginFragment();
        loginFragment.setArguments(configOptions);

        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        config = ParseLoginConfig.fromBundle(getArguments(), getActivity());

        View v = inflater.inflate(R.layout.com_parse_ui_parse_login_fragment,
                parent, false);
        ImageView appLogo = (ImageView) v.findViewById(R.id.app_logo);
        parseLogin = v.findViewById(R.id.parse_login);
        usernameField = (EditText) v.findViewById(R.id.login_username_input);
        passwordField = (EditText) v.findViewById(R.id.login_password_input);
        parseLoginHelpButton = (Button) v.findViewById(R.id.parse_login_help);
        parseLoginButton = (Button) v.findViewById(R.id.parse_login_button);
        parseSignupButton = (Button) v.findViewById(R.id.parse_signup_button);
        facebookLoginButton = (Button) v.findViewById(R.id.facebook_login);
        twitterLoginButton = (Button) v.findViewById(R.id.twitter_login);
        parseLoginButton.setBackgroundColor(Color.DKGRAY);
        parseSignupButton.setBackgroundColor(Color.DKGRAY);

        if (appLogo != null && config.getAppLogo() != null) {
            appLogo.setImageResource(config.getAppLogo());
        }
        if (allowParseLoginAndSignup()) {
            setUpParseLoginAndSignup();
        }
        if (allowFacebookLogin()) {
            setUpFacebookLogin();
        }
        if (allowTwitterLogin()) {
            setUpTwitterLogin();
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        if (activity instanceof ParseLoginFragmentListener) {
            loginFragmentListener = (ParseLoginFragmentListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseLoginFragmentListener");
        }

        if (activity instanceof ParseOnLoginSuccessListener) {
            onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginSuccessListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }

        if (activity instanceof httpParseResponse) {
            newResponse = (httpParseResponse) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseLoginFragmentListener");
        }
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void setUpParseLoginAndSignup() {
        parseLogin.setVisibility(View.VISIBLE);

        if (config.isParseLoginEmailAsUsername()) {
            usernameField.setHint(R.string.com_parse_ui_email_input_hint);
            usernameField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        if (config.getParseLoginButtonText() != null) {
            parseLoginButton.setText(config.getParseLoginButtonText());
        }

        parseLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if (username.length() == 0) {
                    if (config.isParseLoginEmailAsUsername()) {
                        showToast(R.string.com_parse_ui_no_email_toast);
                    } else {
                        showToast(R.string.com_parse_ui_no_username_toast);
                    }
                } else if (password.length() == 0) {
                    showToast(R.string.com_parse_ui_no_password_toast);
                } else {

                    loadingStart(true);
                    //TO-DO Remove hardcoded values
                    String urlParameter = "grant_type=password&username=" + username  + "&password=" + password;
                    new httpParseClass(newResponse, urlParameter);
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (isActivityDestroyed()) {
                                return;
                            }
                        }
                    });
                }
            }
        });

        if (config.getParseSignupButtonText() != null) {
            parseSignupButton.setText(config.getParseSignupButtonText());
        }

        parseSignupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                loginFragmentListener.onSignUpClicked(username, password);
            }
        });

        if (config.getParseLoginHelpText() != null) {
            parseLoginHelpButton.setText(config.getParseLoginHelpText());
        }

        parseLoginHelpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragmentListener.onLoginHelpClicked();
            }
        });
    }

    private void setUpFacebookLogin() {
        //String APP_ID = getResources().getString(R.string.facebook_app_id)
        //Facebook facebook = new Facebook()
        //facebookLoginButton.setVisibility(View.VISIBLE);

//    if (config.getFacebookLoginButtonText() != null) {
//      facebookLoginButton.setText(config.getFacebookLoginButtonText());
//    }

//        facebookLoginButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadingStart(true);
//                ParseFacebookUtils.logIn(config.getFacebookLoginPermissions(),
//                        getActivity(), new LogInCallback() {
//                            @Override
//                            public void done(ParseUser user, ParseException e) {
//                                if (isActivityDestroyed()) {
//                                    return;
//                                }
//
//                                if (user == null) {
//                                    loadingFinish();
//                                    if (e != null) {
//                                        showToast(R.string.com_parse_ui_facebook_login_failed_toast);
//                                        debugLog(getString(R.string.com_parse_ui_login_warning_facebook_login_failed) +
//                                                e.toString());
//                                    }
//                                } else if (user.isNew()) {
//                                    Request.newMeRequest(ParseFacebookUtils.getSession(),
//                                            new Request.GraphUserCallback() {
//                                                @Override
//                                                public void onCompleted(GraphUser fbUser,
//                                                                        Response response) {
//                      /*
//                        If we were able to successfully retrieve the Facebook
//                        user's name, let's set it on the fullName field.
//                      */
//                                                    ParseUser parseUser = ParseUser.getCurrentUser();
//                                                    if (fbUser != null && parseUser != null
//                                                            && fbUser.getName().length() > 0) {
//                                                        parseUser.put(USER_OBJECT_NAME_FIELD, fbUser.getName());
//                                                        parseUser.saveInBackground(new SaveCallback() {
//                                                            @Override
//                                                            public void done(ParseException e) {
//                                                                if (e != null) {
//                                                                    debugLog(getString(
//                                                                            R.string.com_parse_ui_login_warning_facebook_login_user_update_failed) +
//                                                                            e.toString());
//                                                                }
//                                                                loginSuccess();
//                                                            }
//                                                        });
//                                                    }
//                                                    loginSuccess();
//                                                }
//                                            }
//                                    ).executeAsync();
//                                } else {
//                                    loginSuccess();
//                                }
//                            }
//                        });
//            }
//        });
    }

    private void setUpTwitterLogin() {
        twitterLoginButton.setVisibility(View.VISIBLE);

        if (config.getTwitterLoginButtonText() != null) {
            twitterLoginButton.setText(config.getTwitterLoginButtonText());
        }

        twitterLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                loginToTwitter();

//                loadingStart(false); // Twitter login pop-up already has a spinner
//                ParseTwitterUtils.logIn(getActivity(), new LogInCallback() {
//                    @Override
//                    public void done(ParseUser user, ParseException e) {
//                        if (isActivityDestroyed()) {
//                            return;
//                        }
//
//                        if (user == null) {
//                            loadingFinish();
//                            if (e != null) {
//                                showToast(R.string.com_parse_ui_twitter_login_failed_toast);
//                                debugLog(getString(R.string.com_parse_ui_login_warning_twitter_login_failed) +
//                                        e.toString());
//                            }
//                        } else if (user.isNew()) {
//                            Twitter twitterUser = ParseTwitterUtils.getTwitter();
//                            if (twitterUser != null
//                                    && twitterUser.getScreenName().length() > 0) {
//                /*
//                  To keep this example simple, we put the users' Twitter screen name
//                  into the name field of the Parse user object. If you want the user's
//                  real name instead, you can implement additional calls to the
//                  Twitter API to fetch it.
//                */
//                                user.put(USER_OBJECT_NAME_FIELD, twitterUser.getScreenName());
//                                user.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if (e != null) {
//                                            debugLog(getString(
//                                                    R.string.com_parse_ui_login_warning_twitter_login_user_update_failed) +
//                                                    e.toString());
//                                        }
//                                        loginSuccess();
//                                    }
//                                });
//                            }
//                        } else {
//                            loginSuccess();
//                        }
//                    }
//                });
            }
        });
    }

    @Override
    public void callback() {
        System.out.println("callback");
    }

    ;

    private boolean allowParseLoginAndSignup() {
        if (!config.isParseLoginEnabled()) {
            return false;
        }

        if (usernameField == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_username_field);
        }
        if (passwordField == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_password_field);
        }
        if (parseLoginButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_button);
        }
        if (parseSignupButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_signup_button);
        }
        if (parseLoginHelpButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_help_button);
        }

        boolean result = (usernameField != null) && (passwordField != null)
                && (parseLoginButton != null) && (parseSignupButton != null)
                && (parseLoginHelpButton != null);

        if (!result) {
            debugLog(R.string.com_parse_ui_login_warning_disabled_username_password_login);
        }
        return result;
    }

    private boolean allowFacebookLogin() {
        if (!config.isFacebookLoginEnabled()) {
            return false;
        }

        if (facebookLoginButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_disabled_facebook_login);
            return false;
        } else {
            return true;
        }
    }

    private boolean allowTwitterLogin() {
        if (!config.isTwitterLoginEnabled()) {
            return false;
        }

        if (twitterLoginButton == null) {
            debugLog(R.string.com_parse_ui_login_warning_disabled_twitter_login);
            return false;
        } else {
            return true;
        }
    }

    private void loginSuccess() {
        onLoginSuccessListener.onLoginSuccess();
    }

    public String POST(String urlParameters) {
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpURLConnection connection = null;
            String targetURL = "https://connectedstars.com/service/token";
            URL url = null;
            try {
                url = new URL(targetURL);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            try {
                if (null != url) {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Length", "" +
                            Integer.toString(urlParameters.getBytes().length));
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Language", "en-US");
                    //send the POST out
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.print(urlParameters);
                    out.close();
                }

                int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    System.out.println("Login Failed");
                } else {
                    //HttpContent entity = connection.gegetContent();
                    System.out.println("Login Pass");
                    InputStream instream = connection.getInputStream();

                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    System.out.println(sb.toString());
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    System.out.println("role" + jsonObject.getString("role"));


                    connection.disconnect();
                    //Intent intent = new Intent(this, AutoPost.class);
                    //startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }

    //Async Task Post Login
    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingStart(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            loadingFinish();

        }
    }

    //Helper to Convert InputStream to JSON String
    private static String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        try {

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (Exception e) {
                System.out.println("Stream Exception");
            }

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());

        }
        return total.toString();
    }

    /**
     * Function to login twitter
     * */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {

            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
            {
                new TwitterAuthenticateTask().execute();
            }
        }
        else
        {
            //Intent intent = new Intent(getActivity(), TwitterActivity.class);
            //startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
            startActivity(intent);
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {

        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(mypreference,false);
    }

    private void savePreference(String key)
    {
        SharedPreferences prefs;
        Editor editor;

        editor = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE).edit();
        editor.putBoolean("LogIn", true).commit();


    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//        }
//    }

}
