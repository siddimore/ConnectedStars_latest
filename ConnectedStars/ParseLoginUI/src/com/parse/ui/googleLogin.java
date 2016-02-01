//package com.parse.ui;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Sid on 6/20/2015.
// */
//public class googleLogin extends Activity {
//
//    private static String CLIENT_ID = "842537427973-a381vrch0t5cgrgvtr02lik77a5bc8o7.apps.googleusercontent.com";
//    //Use your own client id
//    private static String CLIENT_SECRET = "EEOeMFHQpLtaHDU4Rr8k-l3N";
//    //Use your own client secret
//    private static String REDIRECT_URI = "http://localhost";
//    private static String GRANT_TYPE = "authorization_code";
//    private static String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
//    private static String OAUTH_URL = "https://accounts.google.com/o/oauth2/auth";
//    private static String OAUTH_SCOPE = "https://www.googleapis.com/auth/urlshortener";
//    //Change the Scope as you need
//    WebView web;
//    SharedPreferences pref;
//    TextView Access;
//    private ProgressDialog pd;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        final Context context = this;
//        super.onCreate(savedInstanceState);
//        //get the webView from the layout
//        web.getSettings().setJavaScriptEnabled(true);
//        web.getSettings().setUseWideViewPort(true);
//        web.getSettings().setLoadWithOverviewMode(true);
//        web = (WebView) findViewById(R.id.webView);
//
//        //Request focus for the webview
//        web.requestFocus(View.FOCUS_DOWN);
//        pd = ProgressDialog.show(this, "", "Loading...", true);
//
//        web.requestFocus(View.FOCUS_DOWN);
//        web.loadUrl(OAUTH_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scopes=" + OAUTH_SCOPE);
//
//        //Set a custom web view client
//        web.setWebViewClient(new WebViewClient() {
//            boolean authComplete = false;
//            Intent resultIntent = new Intent();
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("GoogleLogin", "Should override URL Loading!!!" + url);
//                view.loadUrl(url);
//                return true;
//            }
//
//            String authCode;
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                //This method will be executed each time a page finished loading.
//                //The only we do is dismiss the progressDialog, in case we are showing any.
////
//                super.onPageFinished(view, url);
//                Log.i("google", "url is " + url);
//                if (url.contains("?code=") && authComplete != true) {
//                    Uri uri = Uri.parse(url);
//                    authCode = uri.getQueryParameter("code");
//                    authComplete = true;
//                    resultIntent.putExtra("code", authCode);
//                    googleLogin.this.setResult(Activity.RESULT_OK, resultIntent);
//                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("Code", authCode);
//                    edit.commit();
//                    new TokenGet().execute();
//                    Log.i("GoogleLogin", "Authorization Code is  " + authCode);
//                    Toast.makeText(getApplicationContext(), "Authenticated & Logged In!!!: ", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else if (url.contains("error=access_denied")) {
//                    Log.i("GoogleLogin", "ACCESS_DENIED_HERE");
//                    resultIntent.putExtra("code", authCode);
//                    authComplete = true;
//                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    Toast.makeText(getApplicationContext(), "Error occur", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public boolean shouldOverrideKeyEvent(WebView web, KeyEvent event) {
//
//                if (web.canGoBack()) {
//                    web.goBack();
//                    return true;
//                }
//                return super.shouldOverrideKeyEvent(web, event);
//            }
//
//        });
//
//    }
//
//    // get access token
//    private class TokenGet extends AsyncTask<String, String, JSONObject> {
//        private ProgressDialog pDialog;
//        String Code;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(googleLogin.this);
//            pDialog.setMessage("Contacting  ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            Code = pref.getString("Code", "");
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            RequestGetJsonObject jParser = new RequestGetJsonObject();
//            // sending to get authorization Grant
//            JSONObject json = jParser.gettoken(TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            pDialog.dismiss();
//            if (json != null){
//                try {
//                    // wait for get access token
//                    String tok = json.getString("access_token");
//                    String expire = json.getString("expires_in");
//                    String refresh = json.getString("refresh_token");
//                    SharedPreferences.Editor edit = pref.edit();
//                    edit.putString("Acc_Token", tok);
//                    edit.commit();
//                    Log.d("Token Access", tok);
//                    Log.d("Expire", expire);
//                    Log.d("Refresh", refresh);
//
//                    Access.setText("Access Token:"+tok+"\nExpires:"+expire+"\nRefresh Token:"+refresh);
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }else{
//                Toast.makeText(getApplicationContext(), "Network Error try click again", Toast.LENGTH_SHORT).show();
//                pDialog.dismiss();
//            }
//        }
//    }
//
//    // get the session list
//    private class JSONParse extends AsyncTask<String, String, JSONObject> {
//        private ProgressDialog pDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(googleLogin.this);
//            pDialog.setMessage("Getting Data ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            RequestGetJsonObject jParser = new RequestGetJsonObject();
//            // Getting JSON from URL
//            JSONObject json = null;
//            try {
//                String token = pref.getString("Acc_Token", "0");
//                Log.i(TAG, "get the token from preference" + token);
//                json = jParser.getJSONFromUrl(url, token);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            pDialog.dismiss();
//            Log.i("googlelogin","test json inside onPost" + json.toString());
//            if (json != null){
//                try {
//                    JSONArray JsonArray = json.getJSONArray("APIResult");
//                    for (int i = 0; i < JsonArray.length(); i++){
//                        JSONObject e = JsonArray.getJSONObject(i);
//                        Log.i("goolelogin", "Print array index" + i + "sessionid  session id is" + e.getString("SessionId").toString());
//                    }
//                    // Create a new intent to launch the ExplicitlyLoadedActivity class
//                    //Intent explicitIntent = new Intent(LoginPage.this, MainActivity.class);
//                    // Start an Activity using that intent and the request code defined above
//                    //explicitIntent.putExtra("JsonArray",JsonArray.toString());
//                    //Intent explicitIntent = new Intent(LoginPage.this, WorkSpace.class);
//                    //explicitIntent.putExtra("JsonArray",JsonArray.toString());
//                    //startActivity(explicitIntent);
//                } catch (JSONException e) {
//                    Log.e("googleLogin","test json inside onPost error" + e.toString());
//                    e.printStackTrace();
//                }
//            }else{
//                Toast.makeText(getApplicationContext(), "Network Error try click again", Toast.LENGTH_SHORT).show();
//                pDialog.dismiss();
//            }
//        }
//    }
//}
