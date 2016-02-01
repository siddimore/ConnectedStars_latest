package com.parse.ui;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Sid on 4/13/2015.
 */
//User Login Async method
public class httpParseClass {
    private httpParseResponse mainClass;
    public String url;
    public httpParseClass(httpParseResponse mClass, String urlParameter) {
        mainClass = mClass;
        this.url = urlParameter;
        this.calledFromMain();
    }

    //Post Method
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
                }
                else
                {
                    mainClass.callback();
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


    public void calledFromMain(){
        //Do somthing...
        new HttpPostAsyncTask().execute(url);
    }
}
