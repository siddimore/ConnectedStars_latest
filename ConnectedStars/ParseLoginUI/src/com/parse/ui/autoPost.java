package com.parse.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;


/**
 * Created by Sid on 5/25/2015.
 */
public class autoPost extends ActionBarActivity{
    // Declaring the String Array with the Text Data for the Spinners
    String[] frequency = { "Weekly", "Twice a Week", "Monthly"};

    private Spinner spinner1, spinner2, spinner3;
    private ArrayList list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autopost);

        Drawable d = getResources().getDrawable(R.drawable.modlogo);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        list = new ArrayList();

        for(int i=0; i < frequency.length; i++)
        {
            list.add(frequency[i]);
        }
        addItemsOnSpinner1();
        addItemsOnSpinner2();
        addItemsOnSpinner3();

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

    // add items into spinner dynamically
    public void addItemsOnSpinner1() {

        spinner1= (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner3() {

        spinner3= (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter);
    }
}
