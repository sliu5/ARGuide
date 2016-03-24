package com.arguide.william.arguide.Model;

/**
 * Created by william on 15-4-9.
 */
public class InterestingPoint {
    public String name,description;
    public Double latitude,longitude,distance,angle;

    public InterestingPoint(String Name,String Description,Double Lat,Double Lon){
        name = Name;
        description = Description;
        latitude = Lat;
        longitude = Lon;
    }
}
