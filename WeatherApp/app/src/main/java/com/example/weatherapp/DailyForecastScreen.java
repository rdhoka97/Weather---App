package com.example.weatherapp;

import com.example.weatherapp.Adapter.DailyWeather.DailyAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import com.example.weatherapp.Adapter.DailyWeather.DailyWeather;
import org.json.JSONObject;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import android.util.Log;

public class DailyForecastScreen extends AppCompatActivity {

    String jsonData;
    RecyclerView dataView;
    boolean farenheit;
    TextView txtLocation;

    ArrayList<DailyWeather> dailyWeathers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast_screen);
        viewInitlization();
        getData();
    }

    public void viewInitlization(){

        txtLocation = findViewById(R.id.locationText);
        dataView = findViewById(R.id.viewData);
    }

    @SuppressLint("SetTextI18n")
    public void getData(){
        jsonData = getIntent().getStringExtra("dailyData");

        txtLocation.setText(""+getIntent().getStringExtra("location"));
        farenheit = getIntent().getBooleanExtra("farenheit",true);

        try {
            JSONObject jObjMain = new JSONObject(jsonData);

            JSONArray daily = jObjMain.getJSONArray("daily");
            for(int i = 0 ; i<daily.length();i++){
                JSONObject dateObject = daily.getJSONObject(i);

                String iconCode = "_" + (dateObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                String description = (dateObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                String precipitation = dateObject.getString("pop") + "% precip.";
                String uvi = dateObject.getString("uvi");
                String morning = dateObject.getJSONObject("temp").getString("morn");
                String afternoon = dateObject.getJSONObject("temp").getString("day");
                String evening = dateObject.getJSONObject("temp").getString("eve");
                String night = dateObject.getJSONObject("temp").getString("night");

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(Long.parseLong(dateObject.getString("dt")) * 1000);
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("EEEE, MM/dd").format(cal.getTime());

                String highLowTemperature = String.format("%.0f° " + (farenheit ? "F" : "C"), Double.parseDouble(dateObject.getJSONObject("temp").getString("max"))) +" / " +String.format("%.0f° " + (farenheit ? "F" : "C"), Double.parseDouble(dateObject.getJSONObject("temp").getString("min")));


                dailyWeathers.add(new DailyWeather(
                        date,
                        highLowTemperature,
                        description,
                        precipitation,
                        uvi,
                        morning,
                        afternoon,
                        evening,
                        night,
                        iconCode,
                        farenheit
                ));

            }

            DailyAdapter dailyAdapter = new DailyAdapter(dailyWeathers,DailyForecastScreen.this);
            dataView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            dataView.setAdapter(dailyAdapter);

        } catch (Exception e) {
            Log.d("Error",""+e.getMessage());
        }
    }
}