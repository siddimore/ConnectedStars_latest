package com.parse.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Dictionary;


/**
 * Created by Sid on 4/15/2015.
 */

    public class jobListAdapter extends BaseAdapter {
        int resource;
    private Context testContext;
    private Dictionary<Integer, jobPair> myDictionary;

public jobListAdapter(Context _cont, int _resource, Dictionary<Integer, jobPair> _items) {

    myDictionary = _items;
    resource = _resource;
    testContext = _cont;

}
    @Override
    public int getCount() {
        if(myDictionary != null)
        return myDictionary.size();
        else
            return 0;
    }

    @Override
    public jobPair getItem(int position) {
        return myDictionary.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout rl;
            final jobPair prod = getItem(position);
            if (convertView == null) {

                rl = new RelativeLayout(parent.getContext());
                LayoutInflater vi = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                vi.inflate(resource, rl, true);

            } else {

                rl = (RelativeLayout)convertView;

            }
            //SET THE NAME

            Button referButton = (Button) rl.findViewById(R.id.referbtn);
            referButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Toast.makeText(testContext, "ButtonClicked",Toast.LENGTH_LONG).show();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("jobPair", prod);
                    Intent intent = new Intent(testContext, jobDetail.class);
                    intent.putExtras(mBundle);
                    testContext.startActivity(intent);
                }
                });
            TextView jobTitletv = (TextView)rl.findViewById(R.id.jobTitle);
            jobTitletv.setText(prod.mjobTitle);

            return rl;
        }
    }

