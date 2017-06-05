package showcode.matt.tributton;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module.  App is small enough that 1 module covers the functions.
 */

@Module
public class DaggerModule {
    Application application;

    public DaggerModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    RegulationsManager provideRegulationsManager() {
        return new RegulationsManager(application);
    }

    @Provides
    @Singleton
    GoogleAPIManager provideGoogleAPIManager() {
        return new GoogleAPIManager(application);
    }

    @Provides
    @Singleton
    ApplicationListingManager provideApplicationListingManager() {
        return new ApplicationListingManager(application);
    }
}
