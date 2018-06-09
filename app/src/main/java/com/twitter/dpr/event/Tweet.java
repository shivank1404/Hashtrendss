package com.twitter.dpr.event;

/**
 * Created by root on 7/2/17.
 */

public class Tweet {

    public Tweet(String location , String date,String name)
    {
        this.name = name;
        this.date= date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String name;
    private String date;
    private String location;


}
