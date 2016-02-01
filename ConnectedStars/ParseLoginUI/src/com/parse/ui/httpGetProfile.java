//package com.parse.ui;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.conn.ssl.X509HostnameVerifier;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.SingleClientConnManager;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//
///**
// * Created by Sid on 7/12/2015.
// */
//public class httpGetProfile {
//}
//

package com.parse.ui;

import android.app.ProgressDialog;
import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.Toast;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sid on 4/16/2015.
 */

//Call Job API to get Jobs
public class httpGetProfile extends jobQuery {
    private Context thisContext;
    private String resultString;
    private JSONObject internalJO;
    private ProgressDialog pd;
    private callAble thisCallback;

    public httpGetProfile(callAble callBack, JSONObject jO, String URL, Context passedContext) {

        internalJO = jO;
        thisContext = passedContext;
        thisCallback = callBack;
        this.doWork();

    }

    //Block to pOST Job Search Keyword
    public String POST() {
        // {
        //Default client with SSL
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        DefaultHttpClient client = new DefaultHttpClient();
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

        // Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        //Get Job API
        //TO-DO: Remove Hardcoded value and use SharedPreference instead
        String userguid = "dc1eb417-dede-44ac-b8de-9d72c9f1f01a";
        String modifiedUserGuid = "\'" + userguid + "\'";
        //new HttpPostAsyncTask().execute("https://connectedstars.com/service/odata/Profiles(guid" + modifiedUserGuid + ")?$expand=ContactInfo,ProfileEducations,ProfileSkills,ProfileDomains,ProfileCompanies,ProfileCourses,ProfileInterests,ProfileSocialMedia,ProfileExpertise");
        String targetURL = "https://connectedstars.com/service/odata/Profiles(guid'dc1eb417-dede-44ac-b8de-9d72c9f1f01a')?$expand=ContactInfo,ProfileEducations,ProfileSkills,ProfileDomains,ProfileCompanies,ProfileCourses,ProfileInterests,ProfileSocialMedia,ProfileExpertise";
        //String targetURL = "https://connectedstars.com/service/odata/Profiles(guid" + modifiedUserGuid + ")?$expand=ContactInfo,ProfileEducations,ProfileSkills,ProfileDomains,ProfileCompanies,ProfileCourses,ProfileInterests,ProfileSocialMedia,ProfileExpertise";
        URL url = null;

        try {
            url = new URL(targetURL);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        if (null != url) {
            HttpGet httpPost = new HttpGet(targetURL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            //To-Do get the actual bearer returned by the Login method
            httpPost.setHeader("Authorization", "Bearer WEb-L5mPDrTNao1c6eHqzDKjTvpxVBjWd9wO8sRDjo_HGS_xZlaUYOuSD0wr2GrOK5PnR78KeZw4HJv1PwdORqknYAY6VB4iFXTGErC7dskSp9JgwbYQM8avke46-mzqlyJTUh2mEBcZvHEjJTSoqN3AS0nyoP6PMmEM2T-HfgNSpeZXpntYkBlbHAZdkOiG8VioFoCAubrUdabN_SYxV-NEMc2wuSPiNdUWodxTkAQN9W6xAjZbTCAVb1ICJRgeRr4ceMQKsaCzhjefa6HJ7UbOhNvSx0uF3Jp0ApQn9qee6_uvHpy9dGfWcUIAtnS5JQ5p7oUFs4FT0SQxFdO_AA");
            try {
                HttpResponse response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        {
                            resultString = EntityUtils.toString(response.getEntity());
                        }
                    }
                } else {
                    System.out.println("Get Profile Failed");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    //Async Task Post Sign
    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(thisContext);
            if (pd != null) {
                pd.setMessage("Loading...");
                pd.show();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST();
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd != null) {
                pd.dismiss();
                showToast("Finished Search successfully!!!");
                thisCallback.callBackMethod(resultString);
            }

        }
    }

    public void showToast(final String toast) {

        Toast.makeText(thisContext, toast, Toast.LENGTH_LONG).show();
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

        }
        return total.toString();
    }

    private void doWork() {
        new HttpPostAsyncTask().execute();
    }
}
