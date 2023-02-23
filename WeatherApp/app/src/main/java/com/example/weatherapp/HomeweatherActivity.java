package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weatherapp.Adapter.HourlyWeather.HourlyAdapter;
import com.example.weatherapp.Adapter.HourlyWeather.HourlyWeather;
import com.example.weatherapp.WeatherApi.Weather;
import com.example.weatherapp.WeatherApi.WeatherDownloadRunnable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeweatherActivity extends AppCompatActivity {

    TextView txtLocation,txtCurrentDateTime,txtTemperature,txtFeelsLike,txtWeatherDescription,txtWinds,txtHumidity,txtUvindex,txtVisibility,txtMorning,txtAfternoon,txtEvening,txtNight;
    TextView txtSunrise,txtSunset;
    ImageView imgWeatherIcon;
    RecyclerView dataRecyclerView;
    boolean degreeFarenheit = true;
    String locationName = "";
    double lat = 0.0,lng = 0.0;
    LinearLayout linearInternet,linearNoInternet;
    String jsonData;
    SwipeRefreshLayout swiper;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeweather);
        viewInitlization();
        onClicks();
        setData();
        checkNetwork();
    }

    public void viewInitlization(){
        txtLocation  = findViewById(R.id.locationText);
        txtCurrentDateTime  = findViewById(R.id.currentDateAndTimeText);
        txtTemperature  = findViewById(R.id.temperatureText);
        txtFeelsLike  = findViewById(R.id.feelsLikeText);
        txtWeatherDescription  = findViewById(R.id.descriptionOfWeather);
        txtWinds  = findViewById(R.id.txtWinds);
        txtHumidity  = findViewById(R.id.humidityText);
        txtUvindex  = findViewById(R.id.indexOfUV);
        txtVisibility  = findViewById(R.id.visibilityText);
        txtMorning  = findViewById(R.id.morningText);
        txtAfternoon  = findViewById(R.id.afternoonText);
        txtEvening  = findViewById(R.id.eveningText);
        txtNight  = findViewById(R.id.nightText);
        txtSunrise  = findViewById(R.id.txtSunrise);
        txtSunset  = findViewById(R.id.txtSunset);
        dataRecyclerView  = findViewById(R.id.dataRecyclerView);
        linearInternet  = findViewById(R.id.linearInternet);
        linearNoInternet  = findViewById(R.id.noInternetText);
        imgWeatherIcon  = findViewById(R.id.weatherImage);
        swiper = findViewById(R.id.swiper);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void onClicks(){
        swiper.setOnRefreshListener(() -> {
            if(hasNetworkConnection()) {
                WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(HomeweatherActivity.this, lat, lng, degreeFarenheit);
                new Thread(loaderTaskRunnable).start();
                swiper.setRefreshing(false);
                linearInternet.setVisibility(View.VISIBLE);
                linearNoInternet.setVisibility(View.GONE);
            }
            else{
                linearInternet.setVisibility(View.GONE);
                linearNoInternet.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Internet connection is required",Toast.LENGTH_LONG).show();
                swiper.setRefreshing(false);
            }
        });
    }

    public void setData(){
        txtLocation.setText(prefs.getString("locationname","Chicago , Illinois"));
        lat = Double.parseDouble(prefs.getString("lat","41.8675766"));
        lng = Double.parseDouble(prefs.getString("long","-87.616232"));
        degreeFarenheit = prefs.getBoolean("degreeselected",true);

        WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(HomeweatherActivity.this, lat,lng, degreeFarenheit);
        new Thread(loaderTaskRunnable).start();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(Weather weather, String jsonData){
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        this.jsonData = jsonData;

        txtTemperature.setText(String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getTemp())));
        txtHumidity.setText(String.format(Locale.getDefault(), "Humidity: %.0f%%", Double.parseDouble(weather.getHumidity())));
        imgWeatherIcon.setImageResource(getResources().getIdentifier(
                weather.iconName, "drawable", "com.example.weatherapp"));
        txtWinds.setText(String.format("Winds: "+getDirection(Double.parseDouble(weather.getWinddegree()))+" at %.0f " + (degreeFarenheit ? "mph" : "mps"), Double.parseDouble(weather.getWindspeed())));
        txtWeatherDescription.setText(String.format("%s", weather.getDescription()));
        txtAfternoon.setText(String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getDayTemp())));
        txtMorning.setText(String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getMorningTemp())));
        txtEvening.setText(String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getEveningTemp())));
        txtNight.setText(String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getNightTemp())));



        txtSunrise.setText("Sunrise : " + weather.sunrise);
        txtSunset.setText("Sunset : "+ weather.sunset);
        txtUvindex.setText("UV Index : "+String.format("%s", weather.getUvi()));
        txtFeelsLike.setText("Feels Like "+String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(weather.getFeelslike())));
        txtVisibility.setText("Visibility : "+String.format("%s", (Double.parseDouble(weather.getVisibility())/1000) +" miles"));


        txtCurrentDateTime.setText(weather.timeZone);

        dataRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        ArrayList<HourlyWeather> hourlyWeathers = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(jsonData);
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject dateObject = hourly.getJSONObject(i);

                String iconCode = "_" + (dateObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                String description = (dateObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                String temp = String.format("%.0f° " + (degreeFarenheit ? "F" : "C"), Double.parseDouble(dateObject.getString("temp")));

                String timeZoneOffset = jObjMain.getString("timezone_offset");

                LocalDateTime ldt =
                        LocalDateTime.ofEpochSecond(Long.parseLong(dateObject.getString("dt")) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC);
                DateTimeFormatter dtf =
                        DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
                String day = ldt.format(dtf);

                LocalDateTime ldt1 =
                        LocalDateTime.ofEpochSecond(Long.parseLong(jObjMain.getJSONObject("current").getString("dt")) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC);

                DateTimeFormatter dtf2 =
                        DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
                String today = ldt1.format(dtf2); // Thu Sep 30 10:06 PM, 2021


                DateTimeFormatter dtf1 =
                        DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
                String time = ldt.format(dtf1); // Thu Sep 30 10:06 PM, 2021

                if(day.equalsIgnoreCase(today)){
                    day = "Today";
                }



                hourlyWeathers.add(new HourlyWeather(
                        day,
                        time,
                        iconCode,
                        temp,
                        description,
                        dateObject.getString("dt")
                ));

            }

            HourlyAdapter hourlyAdapter = new HourlyAdapter(hourlyWeathers, HomeweatherActivity.this);
            dataRecyclerView.setAdapter(hourlyAdapter);

        }
        catch (Exception e){
            Log.e("Error",e.getMessage());
        }



    }

    public void checkNetwork(){
        if(hasNetworkConnection()){
            linearInternet.setVisibility(View.VISIBLE);
            linearNoInternet.setVisibility(View.GONE);
        }
        else{
            linearInternet.setVisibility(View.GONE);
            linearNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {

                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1,p2;
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            locationName = p1 + ", " + p2;
            return locationName;
        }
        catch (IOException e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {

                return;
            }
            lat = address.get(0).getLatitude();
            lng = address.get(0).getLongitude();

        } catch (Exception e) {

            Log.e("Error",e.getMessage());
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = getSystemService(ConnectivityManager.class);
        }
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icons, menu);
        MenuItem item = menu.getItem(0);
        if(degreeFarenheit){
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
        }
        else{
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkNetwork();
        if (item.getItemId() == R.id.dailyMenu) {
            if(hasNetworkConnection()) {
                Intent intent = new Intent(this, DailyForecastScreen.class);
                intent.putExtra("dailyData", jsonData);
                intent.putExtra("farenheit", degreeFarenheit);
                intent.putExtra("location", txtLocation.getText().toString());
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"This function requires devices to be connected to the internet",Toast.LENGTH_LONG).show();
            }
            return true;

        }
        else if (item.getItemId() == R.id.unitTempMenu) {
            if(hasNetworkConnection()) {
                if (degreeFarenheit) {
                    degreeFarenheit = false;
                    prefs.edit().putBoolean("degreeselected", degreeFarenheit).apply();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
                    WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(HomeweatherActivity.this, lat,lng, degreeFarenheit);
                    new Thread(loaderTaskRunnable).start();
                } else {
                    degreeFarenheit = true;
                    prefs.edit().putBoolean("degreeselected", degreeFarenheit).apply();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
                    WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(HomeweatherActivity.this, lat,lng, degreeFarenheit);
                    new Thread(loaderTaskRunnable).start();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"This function requires devices to be connected to the internet",Toast.LENGTH_LONG).show();
            }
            return true;

        }
        else if (item.getItemId() == R.id.locationMenu) {

            if(hasNetworkConnection()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);

                builder.setTitle("Enter Location");
                builder.setMessage("For US Location, enter as 'City', or 'City, State'\n For international  locations enter as 'City,Country'");



                builder.setPositiveButton("OK", (dialog, id) -> {
                    txtLocation.setText(getLocationName(et.getText().toString().trim()));
                    getLatLon(et.getText().toString().trim());
                    prefs.edit().putString("locationname", txtLocation.getText().toString()).apply();
                    prefs.edit().putString("lat", ""+lat).apply();
                    prefs.edit().putString("long", ""+lng).apply();
                    prefs.edit().putBoolean("degreeselected", degreeFarenheit).apply();
                    WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(HomeweatherActivity.this, lat,lng, degreeFarenheit);
                    new Thread(loaderTaskRunnable).start();
                });


                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(),"This function requires devices to be connected to the internet",Toast.LENGTH_LONG).show();
            }

            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}