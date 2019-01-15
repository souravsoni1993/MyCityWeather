package getgood.dogood.mycityweather.presenter;


import getgood.dogood.mycityweather.model.Model;
import getgood.dogood.mycityweather.model.WeatherActivityModel;
import getgood.dogood.mycityweather.pojo.WeatherDetaills;


/**
 * Created by sourav soni on 13/1/2019.
 */

public class WeatherActivityPresenter implements Model.PresenterLisntner, Presenter {
    private View mainView;
    private WeatherActivityModel mainActivityModel;

    public WeatherActivityPresenter(View view) {
        this.mainView = view;
        this.mainActivityModel = new WeatherActivityModel();
        setupview();
    }

    @Override
    public void sendTheListOfWeatherForecast(WeatherDetaills weatherDetaills) {

        mainView.updateWeatherDetailsListView(weatherDetaills);
        mainView.hideProgressBar();

    }

    @Override
    public void responseError(String error) {
        mainView.showErrorMessage(error);
        mainView.hideProgressBar();

    }


    @Override
    public void setupview() {
        mainActivityModel.initialzeListner(this);
        mainView.showProgressBar();
    }

    @Override
    public void getWeatherDetailsFromServer(String apiKey,String cityName,String noOfDays) {

        mainActivityModel.getWhetherForecastDetails(apiKey,cityName,noOfDays);

    }

}
