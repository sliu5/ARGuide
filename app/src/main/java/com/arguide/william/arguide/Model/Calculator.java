package com.arguide.william.arguide.Model;

import android.util.Log;
/**
 * Created by william on 15-4-9.
 */
public class Calculator{

    double EARTH_RADIUS = 6378.137;

    private double Rad(double d){
        return Math.PI * d /180.0;
    }

    /*
     * Get the distance between 2 point
     */
    public double GetDistance(double lat1,double lon1,double lat2,double lon2){
        double radLat1 = Rad(lat1);
        double radLat2 = Rad(lat2);
        double a = radLat1 - radLat2;
        double b = Rad(lon1) - Rad(lon2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return s;
    }

    public double GetAngle(double lat1,double lon1,double lat2,double lon2,double ang){
        lon1 = -lon1;
        lon2 = -lon2;
        ang = -ang;
        double angle = 0.0;
        if(lon1 > lon2){
            angle = Math.atan( Math.sin((lat1 - lat2)*Math.PI/180)/Math.sin((lon1 - lon2)*Math.PI/180) )*180/Math.PI+90-ang;
        }
        if(lon1 < lon2){
            angle = -Math.atan( Math.sin((lat1 - lat2)*Math.PI/180)/Math.sin((lon2 - lon1)*Math.PI/180) )*180/Math.PI-90-ang;
        }
        return angle;

    }
}
