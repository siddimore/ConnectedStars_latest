package com.parse.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Sid on 5/24/2015.
 */
public class jobDetail extends ActionBarActivity implements postInterface {
    private jobPair job = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobdetail);

        Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //TO-DO:Add bottom row of post,profile and briefcase buttons

        View jobapply = findViewById(R.id.referbtn);
        jobapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobApplyClickListener();
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

        Intent intent = getIntent();
        job = (jobPair) intent.getSerializableExtra("jobPair");
        System.out.println("jobDescription:" + job.mJobDescription);

        TextView titleText = (TextView) findViewById(R.id.jobTitleText);
        titleText.setText(job.mjobTitle);
        TextView descriptionText = (TextView) findViewById(R.id.DescriptionText);
        String jobDescriptionText = job.mJobDescription;
        //Remove all HTML tags from JobDescription response
        jobDescriptionText = android.text.Html.fromHtml(jobDescriptionText).toString();
        //jobDescriptionText = jobDescriptionText.replaceAll("<br>", "\n");
        descriptionText.setText(jobDescriptionText);

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
    public void postClickListener() {
        Intent postIntent = new Intent(jobDetail.this, autoPost.class);
        startActivity(postIntent);
    }

    public void userProfileLaunch() {
        Intent userIntent = new Intent(jobDetail.this, userprofile.class);
        startActivity(userIntent);
    }

    public void jobApplyClickListener() {
        Intent userIntent = new Intent(jobDetail.this, jobapply.class);
        startActivity(userIntent);

    }

}
