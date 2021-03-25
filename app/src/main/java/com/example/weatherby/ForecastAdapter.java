package com.example.weatherby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private String[] forecast;

    public ForecastAdapter(){

    }

    public void setWeatherData(String[] parsedResponse){
        this.forecast = parsedResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ForecastHolder(inflater.inflate(R.layout.forecast_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        holder.mForecastTextView.setText(forecast[position]);
    }

    @Override
    public int getItemCount() {
        if(forecast != null){
            return forecast.length;
        } else{
            return 0;
        }
    }

    public class ForecastHolder extends RecyclerView.ViewHolder{
        private TextView mForecastTextView;

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);
            mForecastTextView = itemView.findViewById(R.id.tv_list_item_weather);
        }
    }
}
