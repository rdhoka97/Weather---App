package com.example.weatherapp.WeatherApi;

import android.os.Build;
import java.time.LocalDateTime;
import com.example.weatherapp.HomeweatherActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import org.json.JSONArray;
import java.time.format.DateTimeFormatter;
import androidx.annotation.RequiresApi;
import java.util.Locale;
import android.util.Log;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

public class  WeatherDownloadRunnable implements Runnable {

    private static final String TAG = "WeatherDownloadRunnable";

    private final HomeweatherActivity homeweatherActivity;
    private final boolean fahrenheit;
    private final double lat,lng;


    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall";


    public WeatherDownloadRunnable(HomeweatherActivity homeweatherActivity, double lat,double lng, boolean fahrenheit) {

        this.lat = lat;
        this.fahrenheit = fahrenheit;
        this.homeweatherActivity = homeweatherActivity;
        this.lng = lng;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(weatherURL+"?lat="+lat+"&lon="+lng+"&units="+ (fahrenheit ? "imperial" : "metric")+"&appid=8a694147e5254c9e4d0b6ca2fc4cc30c");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handleResults(final String jsonString) {

        final Weather w = parseJSON(jsonString);
        homeweatherActivity.runOnUiThread(() -> homeweatherActivity.updateData(w,jsonString));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Weather parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);


            String timeZoneOffset = jObjMain.getString("timezone_offset");
            JSONObject jMain = jObjMain.getJSONObject("current");
            JSONArray weather = jMain.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            String icon = "_" + jWeather.getString("icon");
            String description = jWeather.getString("description");
            String dt = jMain.getString("dt");
            String humidity = jMain.getString("humidity");
            String feelsLike = jMain.getString("feels_like");
            String visibility = jMain.getString("visibility");
            String uvi = jMain.getString("uvi");
            String sunrise = jMain.getString("sunrise");
            String sunset = jMain.getString("sunset");
            String windspeed = jMain.getString("wind_speed");
            String winddegree = jMain.getString("wind_deg");
            String windgust = "";
            if(jMain.has("wind_gust"))
                windgust = jMain.getString("wind_gust");

            String temp = jMain.getString("temp");

            LocalDateTime ldt =
                    LocalDateTime.ofEpochSecond(Long.parseLong(dt) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC);
            DateTimeFormatter dtf =
                    DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
            String formattedTimeString = ldt.format(dtf); // Thu Sep 30 10:06 PM, 2021


            LocalDateTime ldt1 =
                    LocalDateTime.ofEpochSecond(Long.parseLong(sunrise) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC);
            DateTimeFormatter dtf1 =
                    DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
            sunrise = ldt1.format(dtf1);


            LocalDateTime ldt2 =
                    LocalDateTime.ofEpochSecond(Long.parseLong(sunset) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC);
            DateTimeFormatter dtf2 =
                    DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
            sunset = ldt2.format(dtf2); //



            JSONArray daily = jObjMain.getJSONArray("daily");
            JSONObject jDaily = (JSONObject) daily.get(0);
            JSONObject jDaily1 = jDaily.getJSONObject("temp");
            String afternoon = jDaily1.getString("day");
            String evening = jDaily1.getString("eve");
            String day = jDaily1.getString("morn");
            String night = jDaily1.getString("night");


            return new Weather("",
                    "",
                    description,
                    temp,
                    humidity,
                    windspeed,
                    winddegree,
                    windgust,
                    "",
                    visibility,
                    feelsLike,
                    uvi,
                    day,
                    afternoon,
                    sunrise,
                    sunset,
                    evening,
                    night,
                    formattedTimeString,
                    icon);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error: ", e);
        }

        return null;
    }
}
