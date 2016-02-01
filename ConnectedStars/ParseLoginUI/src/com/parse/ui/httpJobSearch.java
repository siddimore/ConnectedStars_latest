package com.parse.ui;


import android.app.ProgressDialog;
import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
public class httpJobSearch extends jobQuery {
    private Context thisContext;
    private String resultString;
    private JSONObject internalJO;
    private ProgressDialog pd;
    private callAble thisCallback;

    public httpJobSearch(callAble callBack, JSONObject jO, String URL, Context passedContext) {

        internalJO = jO;
        thisContext = passedContext;
        thisCallback = callBack;
        this.doWork();

    }

    //Block to pOST Job Search Keyword
    public String POST() {
        {
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
            HttpURLConnection connection = null;
            String targetURL = "https://connectedstars.com/service/api/JobsCustomController/GetJob";
            URL url = null;
            try {
                url = new URL(targetURL);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            try {
                if (null != url) {
                    HttpPost httpPost = new HttpPost(targetURL);
                    httpPost.setEntity(new StringEntity(internalJO.toString(), "UTF-8"));
                    httpPost.setHeader("Content-Type", "application/json");
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
                            System.out.println("Job Search Post Failed");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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
            // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
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
            //Log.e("log_tag", "Error converting result " + e.toString());
        }
        return total.toString();
    }

    private void doWork() {
        new HttpPostAsyncTask().execute();
    }
}
