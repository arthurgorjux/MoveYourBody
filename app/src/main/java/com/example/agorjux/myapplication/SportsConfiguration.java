package com.example.agorjux.myapplication;

import android.graphics.Color;

/**
 * Created by agorjux on 24/09/15.
 */
public class SportsConfiguration {

    public static int getColorBySport(String sport){
        int color = Color.BLACK;
        switch (sport){
            case "Tennis":
                color = Color.YELLOW;
                break;
            case "Football":
                color = Color.GREEN;
                break;
            case "Running":
                color = Color.RED;
                break;
            default:
                break;
        }
        return color;
    }
}
