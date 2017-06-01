package showcode.matt.tributton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.output_text_field) protected TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.json_button)
    protected void onJSONClick() {
        outputText.setText("JSON Clicked");
    }

    @OnClick(R.id.gps_button)
    protected void onGPSClick() {
        outputText.setText("GPS Clicked");
    }

    @OnClick(R.id.apps_button)
    protected void onAppsClick() {
        outputText.setText("Apps Clicked");
    }

}
