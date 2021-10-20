package com.prathamesh.quotes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mySingleton;
    private RequestQueue myRequestQueue;
    private static Context myContext;

    private MySingleton(Context context){
        myContext = context;
        myRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {

        if(myRequestQueue == null){
            myRequestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        }

        return myRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }


    public static synchronized MySingleton getInstance(Context context){

        if(mySingleton == null){
            mySingleton = new MySingleton(context);
        }

        return mySingleton;
    }

}
