package com.parse.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sid on 6/8/2015.
 */
//Gets User Profile ID
public class userprofile extends ActionBarActivity implements callAble {

    private TextView educationText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        educationText = (TextView) findViewById(R.id.usereducationText);

        Intent intent = getIntent();
        String userguid = intent.getStringExtra("UserGuid");
        new httpGetProfile(userprofile.this, null, "https://dit.connectedstars.com/service/api/account/register", userprofile.this);
        //TO-DO: Remove Hardcoded value and use SharedPreference instead
        //userguid = "dc1eb417-dede-44ac-b8de-9d72c9f1f01a";
        //String modifiedUserGuid = "\'"+userguid+"\'";
        //new HttpPostAsyncTask().execute("https://connectedstars.com/service/odata/Profiles(guid" + modifiedUserGuid + ")?$expand=ContactInfo,ProfileEducations,ProfileSkills,ProfileDomains,ProfileCompanies,ProfileCourses,ProfileInterests,ProfileSocialMedia,ProfileExpertise");
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

    @Override
    public void callBackMethod(String resultString) {

        try {
            System.out.println("userprofiledata" + resultString);
            JSONObject jsonObject = new JSONObject(resultString);
            System.out.println("ProfileExpertise" + jsonObject.getString("ProfileExpertise"));
            String eduText = jsonObject.getString("ProfileEducations");
            educationText.setText(eduText);
            //System.out.println("job_title" + jsonObject.getString("companyName"));
        }
        catch(JSONException ex)
        {
            System.out.println("JsonException" + ex.toString());
        }
        //JSONObject json =  (JSONObject) new JSONParser().parse(Data);//JSON(resultString);
    }

//    public String POST(String urlParameters) {
//        {
//            //Default client with SSL
//            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//            DefaultHttpClient client = new DefaultHttpClient();
//
//            HttpParams params = new BasicHttpParams();
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(params, "utf-8");
//            HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
//            HttpProtocolParams.setUseExpectContinue(params, true);
//            SchemeRegistry registry = new SchemeRegistry();
//            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//            registry.register(new Scheme("https", socketFactory, 443));
//            SingleClientConnManager mgr = new SingleClientConnManager(params, registry);
//            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, params);
//            // Set verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//            HttpURLConnection connection = null;
//            String targetURL = urlParameters;
//
//            URL url = null;
//            try {
//                url = new URL(targetURL);
//            } catch (MalformedURLException ex) {
//                ex.printStackTrace();
//            }
//
//            try {
//                if (null != url) {
//
//                    HttpGet httpGet = new HttpGet(targetURL);
//                    httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
//                    httpGet.setHeader("Content-Type",
//                            "application/x-www-form-urlencoded");
//                    httpGet.setHeader("Accept-Language", "en-US");
//                    httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//                    httpGet.setHeader("Accept", "application/json, text/plain");
//                    try {
//                        HttpResponse response = httpClient.execute(httpGet);
//                        if (response.getStatusLine().getStatusCode() == 200) {
//                            HttpEntity entity = response.getEntity();
//                            if (entity != null) {
//                                {
//                                    String resultString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//                                    System.out.println("Profile id string: " + resultString);
//                                    InputStream is = entity.getContent();
//                                    String responseString = convertStreamToString(is);
//                                    System.out.println("Profile id string: " + responseString);
//                                }
//                            }
//                        }
//                    } catch (Exception ex) {
//                        System.out.println("Exception:" + ex.toString());
//
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return "null";
//    }

    //Async Task Post Login
//    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            return POST(urls[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//        }
//    }
//
//    //Helper to Convert InputStream to JSON String
//    private static String convertStreamToString(InputStream is) {
//        String line = "";
//        StringBuilder total = new StringBuilder();
//        try {
//
//            try {
//                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//                while ((line = rd.readLine()) != null) {
//                    total.append(line);
//                }
//            } catch (Exception e) {
//                System.out.println("Stream Exception");
//            }
//
//        } catch (Exception e) {
//            Log.e("log_tag", "Error converting result " + e.toString());
//
//        }
//        return total.toString();
//    }

}
