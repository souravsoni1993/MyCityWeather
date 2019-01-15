package getgood.dogood.mycityweather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import getgood.dogood.mycityweather.adapter.ForecastAdapter;
import getgood.dogood.mycityweather.pojo.Forecastday;
import getgood.dogood.mycityweather.pojo.WeatherDetaills;
import getgood.dogood.mycityweather.presenter.WeatherActivityPresenter;
import getgood.dogood.mycityweather.utils.connectivityMessage;


/**
 * Created by sourav soni on 13/1/2019.
 */

public class MainActivity extends AppCompatActivity implements WeatherActivityPresenter.View, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<Forecastday> forecastsList;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private WeatherActivityPresenter presenter;
    private TextView currentCityTextview, currentTempretureTextview;
    private String cityName;

    private ProgressBar progressBar;


    //location

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_details);


        mainLayout = findViewById(R.id.mainLayout);
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        currentCityTextview = findViewById(R.id.current_city_textView);
        currentTempretureTextview = findViewById(R.id.current_temperature_textView);

        presenter = new WeatherActivityPresenter(this);

    }

    @Override
    public void updateWeatherDetailsListView(WeatherDetaills weatherDetaills) {

        removedItems = new ArrayList<Integer>();

        forecastsList = weatherDetaills.getForecast().getForecastday();

        currentCityTextview.setText(weatherDetaills.getLocation().getName());
        currentTempretureTextview.setText("" + weatherDetaills.getCurrent().getTempC() + (char) 0x00B0);
        adapter = new ForecastAdapter(forecastsList, MainActivity.this);
        recyclerView.setAdapter(adapter);

        //slide bottom up animation in recyclerView
        int resId = R.anim.layout_animation_up_to_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(MainActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);


    }

    @Override
    public void showProgressBar() {


        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                150);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        mainLayout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            progressBar.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
        }


    }

    @Override
    public void hideProgressBar() {

        progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public void showErrorMessage(String error) {

        Toast.makeText(MainActivity.this, "Error :" + error, Toast.LENGTH_LONG).show();
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            currentCityTextview.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            currentCityTextview.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());


            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cityName = addresses.get(0).getAddressLine(0);

            if (cityName != null)

                if (connectivityMessage.checkConnection(currentCityTextview))
                    presenter.getWeatherDetailsFromServer(getResources().getString(R.string.apiKey), cityName, getResources().getString(R.string.noOfDays));

                else {
                    progressBar.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {


                        @Override
                        public void run() {
                            // close this activity
                            finish();
                        }
                    }, 3000);


                }


        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentCityTextview.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

}
