package com.example.weatherapp.Adapter.HourlyWeather;

import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.widget.LinearLayout;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.example.weatherapp.HomeweatherActivity;
import android.net.Uri;
import android.view.LayoutInflater;
import com.example.weatherapp.R;
import android.content.ContentUris;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.MyViewHolder> {
    public final HomeweatherActivity mainAct;
    public final List<HourlyWeather> hourlyWeathers;


    public HourlyAdapter(List<HourlyWeather> hourlyWeathers, HomeweatherActivity ma) {
        this.hourlyWeathers = hourlyWeathers;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HourlyWeather hourlyWeather = hourlyWeathers.get(position);
        holder.imgWeather.setImageResource(mainAct.getResources().getIdentifier(hourlyWeather.icon, "drawable", "com.example.weatherapp"));
        holder.txtTemperature.setText(hourlyWeather.temp);
        holder.txtTime.setText(hourlyWeather.time);
        holder.txtDay.setText(hourlyWeather.day);
        holder.txtDescription.setText(hourlyWeather.description);



        holder.linearLayout.setOnClickListener(v -> {
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath("time");
            ContentUris.appendId(builder, System.currentTimeMillis());
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(builder.build());
            mainAct.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return hourlyWeathers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWeather;
        TextView txtTemperature,txtDay,txtDescription,txtTime;

        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            txtTime = view.findViewById(R.id.timeText);
            txtDay = view.findViewById(R.id.dayText);
            txtDescription = view.findViewById(R.id.descriptionOfText);
            linearLayout = view.findViewById(R.id.linearLayout);
            txtTemperature = view.findViewById(R.id.temperatureText);
            imgWeather = view.findViewById(R.id.weatherImage1);
        }

    }
}