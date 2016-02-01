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

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import java.io.PrintWriter;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 * Base class with helper methods for fragments in ParseLoginUI.
 */
public class ParseLoginFragmentBase extends Fragment {
  protected ParseOnLoadingListener onLoadingListener;

  protected String getLogTag() {
    return null;
  }

  protected void showToast(int id) {
    showToast(getString(id));
  }

  protected void showToast(CharSequence text) {
    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
  }

  protected void loadingStart() {
    loadingStart(true);
  }

  protected void loadingStart(boolean showSpinner) {
    if (onLoadingListener != null) {
      onLoadingListener.onLoadingStart(showSpinner);
        //String urlParameter = "grant_type=password&username="+email+"&password="+passWord;
        //String urlParameter = "grant_type=password&username="+"siddimore@live.com"+"&password="+"Saanika123";
        //new HttpPostAsyncTask().execute(urlParameter);
    }
  }

  protected void loadingFinish() {
    if (onLoadingListener != null) {
      onLoadingListener.onLoadingFinish();
    }
  }

  protected void debugLog(int id) {
    debugLog(getString(id));
  }

  protected void debugLog(String text) {
    if (Parse.getLogLevel() <= Parse.LOG_LEVEL_DEBUG &&
        Log.isLoggable(getLogTag(), Log.WARN)) {
      Log.w(getLogTag(), text);
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  protected boolean isActivityDestroyed() {
    FragmentActivity activity = getActivity();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return activity == null || activity.isDestroyed();
    } else {
      return activity == null || ((ParseLoginActivity) activity).isDestroyed();
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
                    //HttpContent entity = connection.gegetContent();
                    System.out.println("Login Pass");
                    InputStream instream = connection.getInputStream();

                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    System.out.println(sb.toString());
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
        protected  void onPreExecute()
        {
            //Toast.makeText(getApplicationContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            //System.out.println("DataSent!!!!!!");
            //loadingStart(true);
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


}
