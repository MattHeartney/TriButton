package showcode.matt.tributton;

import android.app.Application;

/**
 * Quick extension to use Dagger.
 */

public class BaseApplication extends Application {

    private DaggerComponent dComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger
        dComponent = DaggerDaggerComponent.builder()
                // list of modules that are part of this component need to be created here too
                .daggerModule(new DaggerModule(this))
                .build();
    }

    public DaggerComponent getDaggerComponent() {
        return dComponent;
    }
}
