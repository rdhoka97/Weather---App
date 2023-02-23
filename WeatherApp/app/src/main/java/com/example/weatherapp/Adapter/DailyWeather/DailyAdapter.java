package com.example.weatherapp.Adapter.DailyWeather;
import java.util.List;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.annotation.SuppressLint;
import com.example.weatherapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.example.weatherapp.DailyForecastScreen;




public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder> {
    public final DailyForecastScreen mainAct;


    public final List<DailyWeather> dailyWeather;


    public DailyAdapter(List<DailyWeather> dailyWeathers, DailyForecastScreen ma)


    {
        this.dailyWeather = dailyWeathers;
        mainAct = ma;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.weather_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DailyWeather daily = dailyWeather.get(position);
        holder.txtDescription.setText(daily.description);
        holder.txtDate.setText(daily.date);

        holder.txtUvindex.setText("UV Index : "+daily.uvindex);
        holder.txtHighLow.setText(daily.temp);
        holder.txtMorning.setText(String.format("%.0f° " + (daily.farenheit ? "F" : "C"), Double.parseDouble(daily.getMorning())));
        holder.txtProbabilityofPrecipitation.setText("( "+daily.precipitation+" )");

        holder.txtNight.setText(String.format("%.0f° " + (daily.farenheit ? "F" : "C"), Double.parseDouble(daily.getNight())));
        holder.txtAfternoon.setText(String.format("%.0f° " + (daily.farenheit ? "F" : "C"), Double.parseDouble(daily.getDay())));
        holder.txtEvening.setText(String.format("%.0f° " + (daily.farenheit ? "F" : "C"), Double.parseDouble(daily.getEvening())));


        holder.txtDate.setText(daily.date);
        holder.imgWeatherIcon.setImageResource(mainAct.getResources().getIdentifier(daily.iconcode, "drawable", "com.example.weatherapp"));
    }

    @Override
    public int getItemCount()
    {

        return dailyWeather.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtHighLow,txtNight,txtDescription,txtProbabilityofPrecipitation,txtDate,txtMorning,txtAfternoon,txtUvindex,txtEvening;
        ImageView imgWeatherIcon;

        MyViewHolder(View view) {
            super(view);
            txtAfternoon = view.findViewById(R.id.afternoonText);
            txtHighLow = view.findViewById(R.id.highLowTemp);
            txtDescription = view.findViewById(R.id.descriptionOfText);
            txtMorning = view.findViewById(R.id.morningText);
            txtEvening = view.findViewById(R.id.eveningText);
            txtProbabilityofPrecipitation = view.findViewById(R.id.precipitationProbability);
            txtNight = view.findViewById(R.id.nightText);
            txtUvindex = view.findViewById(R.id.indexOfUV);
            imgWeatherIcon = view.findViewById(R.id.weatherImage);
            txtDate = view.findViewById(R.id.dateText);
        }

    }
}