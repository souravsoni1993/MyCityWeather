package getgood.dogood.mycityweather.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import getgood.dogood.mycityweather.networkConnection.ConnectivityReceiver;

import static android.support.design.widget.Snackbar.make;


public class connectivityMessage {

    public static boolean checkConnection(View view) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected,view);
        return isConnected;
    }

    // Showing the status in Snackbar
    private static void showSnack(boolean isConnected,View view) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = make(view, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
            view.setVisibility(View.GONE);


        }
    }

}
