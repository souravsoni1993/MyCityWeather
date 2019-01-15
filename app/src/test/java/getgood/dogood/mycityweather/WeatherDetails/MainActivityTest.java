package getgood.dogood.mycityweather.WeatherDetails;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import getgood.dogood.mycityweather.MainActivity;
import getgood.dogood.mycityweather.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {

        mainActivity = activityActivityTestRule.getActivity();


    }


    @Test
    public void checkActivity() {
        View view = mainActivity.findViewById(R.id.current_city_textView);

        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {

        mainActivity = null;

    }
}