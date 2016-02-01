package com.parse.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sid on 4/12/2015.
 */

public class jobQuery extends ActionBarActivity implements postInterface{
    protected ParseOnLoadingListener onLoadingListener;
    private static final String LOG_TAG = "ParseLoginFragment";
    private static final String USER_OBJECT_NAME_FIELD = "name";
    private callAble newResponse;
    private JSONObject jobSearchObject = null;
    private EditText jobQueryField;
    private EditText locationField;
    public callbackClassCheck.MyCallbackClass cb;
    private SharedPreferences prefs;
    private boolean signOut = false;
    public static final String mypreference = "LoginDetails";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobsearch);

       Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        jobQueryField = (EditText) findViewById(R.id.job_query_keyword_input);
        locationField = (EditText) findViewById(R.id.job_query_location_input);

        View pauseButton = findViewById(R.id.job_query_search_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jobQuery = jobQueryField.getText().toString();
                String location = locationField.getText().toString();

                if (jobQuery.length() == 0) {
                    showToast("Please enter keyword");
                    return;
                }
                else if (location.length() == 0) {
                    showToast("Please enter location");
                    return;
                }
                else
                {
                    try {
                        jobSearchObject = new JSONObject();
                        //TO-DO: Remove HardCoded value for ProfileID, Location
                        //TO-DO: Use Shared Preferences
                        jobSearchObject.put("ProfileId", "dc1eb417-dede-44ac-b8de-9d72c9f1f01a");
                        jobSearchObject.put("PageNumber", "1");
                        jobSearchObject.put("NumberofItemsPerPage", "10");
                        jobSearchObject.put("CompanyDepartmentId", null);
                        jobSearchObject.put("KeyWordOrOnCompanySkillTitle", jobQuery); // Set the first name/pair
                        jobSearchObject.put("KeyWordAndOnAll", null);
                        jobSearchObject.put("KeyWordAndOnAll", null);
                        jobSearchObject.put("KeyWordInJobTitle", null);
                        jobSearchObject.put("WithinDays", -1);
                        jobSearchObject.put("IsCandidateView", "true");
                        jobSearchObject.put("KeyWordOnLocation", "Redmond, WA, United States");

                        jobSearchObject.put("KeyWordOnLocationAdvance", null);
                        jobSearchObject.put("CompanyLocationId", null);
                        jobSearchObject.put("FilterByProfileId", null);
                        jobSearchObject.put("JobByMeOnly", null);
                        if (null != jobSearchObject) {

                            Intent myTriggerActivityIntent=new Intent(jobQuery.this,jobList.class);
                            myTriggerActivityIntent.putExtra("b", jobSearchObject.toString());
                            startActivity(myTriggerActivityIntent);
                        }
                    }
                    catch(Exception ex)
                    {
                        System.out.println("Exception:" + ex.toString());
                    }
                }
            }
        });


        View postButton = findViewById(R.id.autopostButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListener();
            }
    });

        View userprofileButton = findViewById(R.id.profileButton);
        userprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileLaunch();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void loadingStart(boolean showSpinner)
    {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingStart(showSpinner);
            String urlParameter = "grant_type=password&username="+"siddimore@live.com"+"&password="+"Saanika123";
            new HttpPostAsyncTask().execute(urlParameter);
        }
    }

    protected void loadingFinish() {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingFinish();
        }
    }


    public String POST(String urlParameters) {
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpURLConnection connection = null;
            String targetURL = "https://dit.connectedstars.com/service/token";
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
                }
                else
                {
                    System.out.println("Login Pass");
                    InputStream instream = connection.getInputStream();

                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    System.out.println(sb.toString());
                    connection.disconnect();
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
        protected  void onPreExecute()
        {

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

    @Override
    public void postClickListener() {
       Intent postIntent = new Intent(jobQuery.this, autoPost.class);
        startActivity(postIntent);
    }

    public void userProfileLaunch()
    {
        //TO-DO: Use Shared Preferences
        Intent userIntent = new Intent(jobQuery.this, userprofile.class);
        userIntent.putExtra("UserGuid", "dc1eb417-dede-44ac-b8de-9d72c9f1f01a");
        startActivity(userIntent);
    }

    @Override
    public void onBackPressed() {
        //prefs.getBoolean("Islogin", signOut);
        //SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        //editor = getSharedPreferences(mypreference, Context.MODE_PRIVATE).edit();
        signOut = prefs.getBoolean("LogIn", false);
        if(signOut) {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            //WelcomeActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setTitle("Not Logged in")
                    .setMessage("Please Login").create().show();;
        }
    }

}



