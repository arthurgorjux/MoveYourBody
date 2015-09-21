package com.example.agorjux.myapplication.ClientRest;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by arthur on 18/06/15.
 */
public class ClientRest extends Application {
    public static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public static void deconnexion() {
        client = new AsyncHttpClient();
    }

    public static String baseUrl(String method){
        return "http://192.168.1.50/MoveYourBodyWS/codeigniter/index.php/" + method;
        //return "http://192.168.1.12/MoveYourBodyWS/codeigniter/index.php/" + method;
    }
}
