package com.example.weatherapp.Adapter.HourlyWeather;

public class HourlyWeather {
    public String time;
    public String description;
    public String temp;
    public String icon;
    public String timestamp;
    public String day;



    public HourlyWeather(String day, String time, String icon, String temp, String description,String timestamp) {

        this.description = description;
        this.icon = icon;
        this.day = day;
        this.temp = temp;
        this.timestamp = timestamp;
        this.time = time;
    }

}

