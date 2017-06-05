package showcode.matt.tributton;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger Component.  Simple Dagger set up for a small app.
 */

@Singleton
@Component(modules = { DaggerModule.class })
public interface DaggerComponent {
    // allow to inject into our Main class
    void inject(MainActivity main);
}
