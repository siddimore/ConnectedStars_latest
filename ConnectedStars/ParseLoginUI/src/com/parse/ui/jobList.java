package com.parse.ui;

/**
 * Created by Sid on 4/15/2015.
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView.OnItemClickListener;

import java.util.Hashtable;

public class jobList extends ActionBarActivity implements callAble, postInterface, OnItemClickListener {

    private Hashtable<Integer, jobPair> myMap = null;
    private ListView myList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joblist_activity);
        Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        Intent intent = getIntent();
        String resultQuery = intent.getStringExtra("b");
        try {
            JSONObject jsonObjRecv = new JSONObject(resultQuery);
            new httpJobSearch(jobList.this, jsonObjRecv, "https://dit.connectedstars.com/service/api/account/register", jobList.this);
        } catch (JSONException ex) {
            System.out.println("Exception in converting to JSONObject" + ex.toString());
        }
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
            JSONObject jsonObjRecv = new JSONObject(resultString);
            JSONArray data = jsonObjRecv.getJSONArray("jobDisplayList"); // get data object
            int count = data.length(); // get totalCount of all jsonObjects
            myMap = new Hashtable<>(
                    count
            );

            for (int i = 0; i < count; i++) {   // iterate through jsonArray to get desired data
                JSONObject jsonObject = data.getJSONObject(i);  // get jsonObject @ i position
                String companyname = jsonObject.getString("companyName");
                System.out.println("job_title" + jsonObject.getString("companyName"));
                JSONObject jobObject = new JSONObject(jsonObject.toString());
                JSONObject jobtitleObject = new JSONObject(jobObject.getJSONObject("job").toString());

                System.out.println("job_title" + jobtitleObject.getString("job_title"));
                String jobTitle = jobtitleObject.getString("job_title");
                System.out.println("jobDescription:" + jobtitleObject.getString("job_description"));
                String jobDescription = jobtitleObject.getString("job_description");

                jobPair newJob = new jobPair(companyname, jobTitle, jobDescription, null);
                myMap.put(i, newJob);
            }
            LinearLayout lvContainer = (LinearLayout) this.findViewById(R.id.containerListView);
            ListView listview = (ListView) lvContainer.findViewById(R.id.listview);

            jobListAdapter jobAdapter = new jobListAdapter(this, R.layout.activity_listview, myMap);
            listview.setAdapter(jobAdapter);
            listview.setClickable(true);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(jobList.this, jobDetail.class);
                    intent.putExtra("hashMap", myMap);
                    startActivity(intent);

                }

            });

        } catch (JSONException ex) {
            System.out.println("Exception in converting to JSONObject" + ex.toString());

        }

    }

    @Override
    public void postClickListener() {
        Intent postIntent = new Intent(jobList.this, autoPost.class);
        startActivity(postIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Intent intent = new Intent(jobList.this, jobDetail.class);
        startActivity(intent);

    }

    public void userProfileLaunch() {
        Intent userIntent = new Intent(jobList.this, userprofile.class);
        startActivity(userIntent);
    }

}

