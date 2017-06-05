package showcode.matt.tributton;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.json_button) protected Button jsonButton;
    @BindView(R.id.gps_button) protected Button gpsButton;
    @BindView(R.id.apps_button) protected Button appsButton;
    @BindView(R.id.output_text_field) protected TextView outputText;

    @Inject RegulationsManager regulationsManager;
    @Inject GoogleAPIManager googleManager;
    @Inject ApplicationListingManager applicationListingManager;

    private static final int PERMISSION_READ_FINE_LOCATION = 1148;

    @Override
    protected void onStart() {
        googleManager.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleManager.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BaseApplication)getApplication()).getDaggerComponent().inject(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.json_button)
    protected void onJSONClick() {
        setButtonsEnabled(false);
        outputText.setText("JSON Processing . . .");
        Consumer<ResponseBody> process = response -> processJSON(response);
        regulationsManager.retrieveJSON(process);
    }

    private void processJSON(ResponseBody data) {
        if (data == null) {
            runOnUiThread(() -> outputText.setText("Error Processing JSON!"));
        } else {
            try {
                String newText = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(data.string()));
                runOnUiThread(() -> outputText.setText(newText));
            } catch (IOException e) {
                runOnUiThread(() -> outputText.setText("Error Processing JSON!"));
                e.printStackTrace();
            }
        }
        setButtonsEnabled(true);
    }

    @OnClick(R.id.gps_button)
    protected void onGPSClick() {
        setButtonsEnabled(false);
        outputText.setText("GPS Clicked");

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_READ_FINE_LOCATION);
        }
        else { showDistanceToSanFran(googleManager.getLocation()); }

    }

    private void showDistanceToSanFran(Location currentLocation) {
        if (currentLocation != null) {
            Location sanFranLocation = new Location("google");
            sanFranLocation.setLatitude(37.7749d);
            sanFranLocation.setLongitude(-122.4194d);
            float meterDistance = currentLocation.distanceTo(sanFranLocation);

            float milesToSanFran = meterDistance/((float)1609.344);
            String outputString = String.format("You are %.2f meters or %.2f miles from the center of San Francisco", meterDistance, milesToSanFran);
            outputText.setText(outputString);
        }
        else { outputText.setText("Error finding location, please try again later"); }
        setButtonsEnabled(true);
    }

    @OnClick(R.id.apps_button)
    protected void onAppsClick() {
        setButtonsEnabled(false);
        outputText.setText("App Info processing . . .\n(This one can take a bit)");
        Consumer<List<PackageStats>> process = list -> processAppList(list);
        applicationListingManager.getStatsList(process);
    }

    private void processAppList(List<PackageStats> list) {
        StringBuilder outputStringBuilder = new StringBuilder();
        for (PackageStats stats : list) {
            if (!stats.equals(list.get(0))) {outputStringBuilder.append(" ,");}
            outputStringBuilder.append(String.format("%s, ", applicationListingManager.getPackageLabel(stats)));
            outputStringBuilder.append(String.format("%s, ", stats.packageName));
            outputStringBuilder.append(String.format("%d, ", stats.codeSize));
            outputStringBuilder.append(String.format("%d, ", stats.dataSize));
            outputStringBuilder.append(String.format("%d", stats.cacheSize));
        }
        runOnUiThread(() -> {
            outputText.setText(outputStringBuilder.toString());
            setButtonsEnabled(true);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDistanceToSanFran(googleManager.getLocation());
                } else {
                    outputText.setText("Please enable GPS to use this feature!");
                }
                return;
            }
        }
    }

    // Used to disable / enable buttons while processing to avoid double calling / stacking requests
    private void setButtonsEnabled(boolean isEnabled) {
        jsonButton.setEnabled(isEnabled);
        gpsButton.setEnabled(isEnabled);
        appsButton.setEnabled(isEnabled);
    }
}
