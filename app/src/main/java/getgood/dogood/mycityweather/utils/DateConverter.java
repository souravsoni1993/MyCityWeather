package getgood.dogood.mycityweather.utils;

import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by sourav soni on 13/1/2019.
 */

public class DateConverter {

    public static String getDay(String value) {

        String day = "";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.get(Calendar.DAY_OF_WEEK);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                day = "Monday";
                break;

            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Monday";
                break;
            case 7:
                day = "Monday";
                break;
            default:


        }

        return day;

    }
}
