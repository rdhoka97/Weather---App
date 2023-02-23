package com.example.weatherapp.WeatherApi;

public class Weather {



    public final String description;
    public final String nightTemp;
    public final String humidity;
    public final String winddegree;
    public final String eveningTemp;
    public final String clouds;
    public final String feelslike;
    public final String morningTemp;
    public final String dayTemp;
    public final String sunrise;
    public final String sunset;
    public final String uvi;
    public final String visibility;
    public final String timeZone;
    public final String windgust;
    public final String city;
    public final String windspeed;
    public final String temp;
    public final String iconName;
    public final String country;



    public String getTemp()
    {
        return temp;
    }
    public String getDayTemp()
    {
        return dayTemp;
    }
    public String getHumidity()
    {
        return humidity;
    }
    public String getVisibility()
    {
        return visibility;
    }
    public String getWindspeed()
    {
        return windspeed;
    }
    public String getFeelslike()
    {
        return feelslike;
    }
    public String getUvi()
    {
        return uvi;
    }
    public String getMorningTemp()
    {
        return morningTemp;
    }
    public String getWinddegree()
    {
        return winddegree;
    }
    public String getEveningTemp()
    {
        return eveningTemp;
    }
    public String getDescription()
    {
        return description;
    }
    public String getNightTemp()
    {
        return nightTemp;
    }


    public Weather(String city, String country, String description, String temp, String humidity, String windspeed, String winddegree, String windgust, String clouds, String visibility, String feelslike, String uvi, String morningTemp, String dayTemp, String sunrise, String sunset, String eveningTemp, String nightTemp, String timeZone, String iconName) {
        this.country = country;
        this.temp = temp;
        this.dayTemp = dayTemp;
        this.windspeed = windspeed;
        this.windgust = windgust;
        this.visibility = visibility;
        this.feelslike = feelslike;
        this.morningTemp = morningTemp;
        this.sunrise = sunrise;
        this.clouds = clouds;
        this.sunset = sunset;
        this.winddegree = winddegree;
        this.eveningTemp = eveningTemp;
        this.humidity = humidity;
        this.nightTemp = nightTemp;
        this.description = description;
        this.timeZone = timeZone;
        this.city = city;
        this.iconName = iconName;
        this.uvi = uvi;
    }
}
