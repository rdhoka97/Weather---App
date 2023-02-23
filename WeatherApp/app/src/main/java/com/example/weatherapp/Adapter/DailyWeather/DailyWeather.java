package com.example.weatherapp.Adapter.DailyWeather;

public class DailyWeather {
    public String night;
    public String temp;
    public String precipitation;
    public String morning;
    public String day;
    public String uvindex;
    public String evening;
    public String description;
    public boolean farenheit;
    public String date;
    public String iconcode;


    public DailyWeather(String date, String temp, String description, String precipitation, String uvindex, String morning, String day, String evening, String night, String iconcode,boolean farenheit) {
        this.temp = temp;
        this.night = night;
        this.precipitation = precipitation;
        this.morning = morning;
        this.day = day;
        this.evening = evening;
        this.uvindex = uvindex;
        this.iconcode = iconcode;
        this.description = description;
        this.farenheit = farenheit;
        this.date = date;
    }

    public String getTemp()
    {
        return temp;
    }
    public String getEvening() {
        return evening;
    }
    public String getPrecipitation()
    {
        return precipitation;
    }
    public String getDay() {
        return day;
    }

    public String getUvindex() {
        return uvindex;
    }
    public String getNight() {
        return night;
    }
    public String getDescription()
    {
        return description;
    }
    public String getIconcode() {
        return iconcode;
    }
    public String getDate()
    {
        return date;
    }
    public String getMorning() {
        return morning;
    }
}
