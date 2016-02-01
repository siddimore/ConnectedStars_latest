package com.parse.ui;

/**
 * Created by Sid on 7/19/2015.
 */

import android.content.Intent;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Sid on 5/24/2015.
 */
public class jobapply extends ActionBarActivity implements postInterface{
    private jobPair job  = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobapply);

        Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //TO-DO:Add bottom row of post,profile and briefcase buttons
        View fileBlayout = findViewById(R.id.FileBrowserLayout);
        fileBlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filebrowserClickListener();
            }
        });
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

//        Intent intent = getIntent();
//        job = (jobPair)intent.getSerializableExtra("jobPair");
//        System.out.println("jobDescription:" + job.mJobDescription);
//
//        TextView titleText = (TextView)findViewById(R.id.jobTitleText);
//        titleText.setText(job.mjobTitle);
//        TextView descriptionText = (TextView)findViewById(R.id.DescriptionText);
//        String jobDescriptionText = job.mJobDescription;
//        //Remove all HTML tags from JobDescription response
//        jobDescriptionText = android.text.Html.fromHtml(jobDescriptionText).toString();
//        descriptionText.setText(jobDescriptionText);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String FilePath = data.getData().getPath();
                File f = new File(FilePath);
                try {
                    byte[] fileByteArray = fileToByteArray(f);
                }catch (IOException ex)
                {
                    System.out.println("Error converting file to byte array :(");
                }

            }
        }
    }
    @Override
    public void postClickListener() {
        Intent postIntent = new Intent(jobapply.this, autoPost.class);
        startActivity(postIntent);
    }

    //fileBrowser to select resume from Disk
    public void filebrowserClickListener()
    {
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,2);

        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent,2);
        }

    }

    public void userProfileLaunch()
    {
        Intent userIntent = new Intent(jobapply.this, userprofile.class);
        startActivity(userIntent);
    }

    public void jobApplyClickListener()
    {

    }

    public byte[] fileToByteArray(File file) throws IOException {

        byte []buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if ( ios.read(buffer) == -1 ) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if ( ios != null )
                    ios.close();
            } catch ( IOException e) {
            }
        }

        return buffer;
    }

}
