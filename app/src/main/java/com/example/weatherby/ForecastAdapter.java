package com.example.weatherby;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherby.Data.WeatherContract;
import com.example.weatherby.Utilities.WeatherUnitUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private Cursor mForecastCursor;

    private final Context mContext;

    public ForecastAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void swapCursor(Cursor cursor){
        this.mForecastCursor = cursor;
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
        mForecastCursor.moveToPosition(position);
        long epochDate = mForecastCursor.getLong(mForecastCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
        String formattedDate = WeatherUnitUtils.convertEpochToDate(epochDate);

        double minTemp = mForecastCursor.getDouble(mForecastCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        double maxTemp = mForecastCursor.getDouble(mForecastCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));

        String formattedWeather = formattedDate + WeatherUnitUtils.factorMinMaxTemp(mContext, minTemp, maxTemp);

        holder.mForecastTextView.setText(formattedWeather);
    }

    @Override
    public int getItemCount() {
        if(mForecastCursor != null){
            return mForecastCursor.getCount();
        } else{
            return 0;
        }
    }

    public class ForecastHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mForecastTextView;

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);
            mForecastTextView = itemView.findViewById(R.id.tv_list_item_weather);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String weather = mForecastTextView.getText().toString();
            Intent intent = new Intent(v.getContext(), WeatherDetailsActivity.class);
            intent.setData(WeatherContract.WeatherEntry.buildWeatherUriWithDate(mForecastCursor.getLong(mForecastCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE))));
            v.getContext().startActivity(intent);
        }
    }
}
