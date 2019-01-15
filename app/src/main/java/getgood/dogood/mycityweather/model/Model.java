package getgood.dogood.mycityweather.model;


import getgood.dogood.mycityweather.pojo.WeatherDetaills;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by sourav soni on 13/1/2019.
 */

public interface Model {

    interface PresenterLisntner {

        void sendTheListOfWeatherForecast(WeatherDetaills weatherDetaills);

        void responseError(String error);

    }

    void getWhetherForecastDetails(String apiKey, String cityName, String noOfDays);

    void initialzeListner(Model.PresenterLisntner presenterLisntner);

    interface Api {

        String BASE_URL = "https://api.apixu.com/v1/";

        @GET("forecast.json")
        Call<WeatherDetaills> getWeatherDetails(
                @Query("key") String apiKey,
                @Query("q") String cityName,
                @Query("days") String numberOfDays
        );
    }

}
