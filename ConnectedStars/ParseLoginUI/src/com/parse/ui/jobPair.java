package com.parse.ui;

import java.io.Serializable;

/**
 * Created by Sid on 5/21/2015.
 */
//object for Title, CompanyName, Description and Location if not null
public class jobPair implements Serializable
{
    public String mjobTitle = null;
    private String mjobLocation = null;
    public String mCompanyName = null;
    public String mJobDescription = null;

    public jobPair(String companyName, String title, String description, String location)
    {
        mCompanyName = companyName;
        mjobTitle = title;
        mjobLocation = location;
        mJobDescription = description;
    }
}
