package com.parse.ui;

/**
 * Created by Sid on 5/13/2015.
 */


public class callbackClassCheck {

    interface MyCallbackClass{
        void callbackReturn();
    }

    MyCallbackClass myCallbackClass;


    void doSomething(MyCallbackClass callbackClass){
        //do something here
        myCallbackClass = callbackClass;
        //call callback method
        myCallbackClass.callbackReturn();
    }

}