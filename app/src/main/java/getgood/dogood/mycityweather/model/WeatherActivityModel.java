package getgood.dogood.mycityweather.model;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import getgood.dogood.mycityweather.pojo.WeatherDetaills;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by sourav soni on 13/1/2019.
 */


public class WeatherActivityModel implements Model {


    private PresenterLisntner presenterLisntner;

    @Override
    public void getWhetherForecastDetails(String apiKey, String cityName, String noOfDays) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();


        Api request = retrofit.create(Api.class);
        Call<WeatherDetaills> call = request.getWeatherDetails(apiKey, cityName, noOfDays);
        call.enqueue(new Callback<WeatherDetaills>() {
            @Override
            public void onResponse(Call<WeatherDetaills> call, Response<WeatherDetaills> response) {
                if (response.body() != null) {

                    WeatherDetaills weatherDetaills = new WeatherDetaills();
                    weatherDetaills = response.body();

                    presenterLisntner.sendTheListOfWeatherForecast(weatherDetaills);
                } else {
                    presenterLisntner.responseError("No response from the server.");
                }
            }


            @Override
            public void onFailure(Call<WeatherDetaills> call, Throwable t) {
                presenterLisntner.responseError(t.getMessage());
            }
        });

    }

    @Override
    public void initialzeListner(PresenterLisntner presenterLisntner) {

        this.presenterLisntner = presenterLisntner;
    }
}