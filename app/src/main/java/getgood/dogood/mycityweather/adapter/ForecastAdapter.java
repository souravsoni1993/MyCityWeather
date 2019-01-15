package getgood.dogood.mycityweather.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import getgood.dogood.mycityweather.MainActivity;
import getgood.dogood.mycityweather.R;
import getgood.dogood.mycityweather.pojo.Forecast;
import getgood.dogood.mycityweather.pojo.Forecastday;
import getgood.dogood.mycityweather.utils.DateConverter;

/**
 * Created by sourav soni on 13/1/2019.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    private List<Forecastday> forecastList;
    private MainActivity mainActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView day_textview;
        TextView tempreture_textview;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.day_textview = (TextView) itemView.findViewById(R.id.day_textview);
            this.tempreture_textview = (TextView) itemView.findViewById(R.id.tempreture_textview);


        }
    }

    public ForecastAdapter(List<Forecastday> forecastList, MainActivity mainActivity) {
        this.forecastList = forecastList;
        this.mainActivity = mainActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView day_textview = holder.day_textview;
        TextView tempreture_textview = holder.tempreture_textview;

        String nameOfTheDay = DateConverter.getDay(forecastList.get(listPosition).getDate());

        day_textview.setText(forecastList.get(listPosition).getDate());

        day_textview.setText(nameOfTheDay);

        tempreture_textview.setText("" + forecastList.get(listPosition).getDay().getAvgtempC() + " C");
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }
}