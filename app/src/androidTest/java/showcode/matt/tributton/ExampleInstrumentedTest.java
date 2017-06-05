package showcode.matt.tributton;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import okhttp3.ResponseBody;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("showcode.matt.tributton", appContext.getPackageName());
    }

    @Test
    public void testRegulations() throws Exception {
        RegulationsManager testManager = new RegulationsManager(InstrumentationRegistry.getTargetContext());
        Consumer<ResponseBody> process = response -> assertNotNull(response);
        testManager.retrieveJSON(process);
    }

    @Test
    public void testGPS() throws Exception {
        GoogleAPIManager testManager = new GoogleAPIManager(InstrumentationRegistry.getTargetContext());
        testManager.connect();
        //Wait 2 seconds so the connection actually applies
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                int permissionCheck = ContextCompat.checkSelfPermission(InstrumentationRegistry.getTargetContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    assertNotNull(testManager.getLocation());
                }
            }
        }, 2000);
    }

    @Test
    public void testApps() throws Exception {
        ApplicationListingManager testManager = new ApplicationListingManager(InstrumentationRegistry.getTargetContext());
        assertNotNull(testManager.getAppList());
    }
}
