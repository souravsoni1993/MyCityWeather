package getgood.dogood.mycityweather.presenter;


import getgood.dogood.mycityweather.pojo.WeatherDetaills;

/**
 * Created by sourav soni on 13/1/2019.
 */

public interface Presenter {

    void setupview();

    void getWeatherDetailsFromServer(String apiKey, String city, String days);

    interface View {

        void updateWeatherDetailsListView(WeatherDetaills weatherDetaills);

        void showProgressBar();

        void hideProgressBar();

        void showErrorMessage(String error);
    }


}
