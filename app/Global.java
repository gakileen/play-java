/*
 * play.Application is the Java class that we need.
 * Be sure not to import play.api.Application which is Scala class.
 */

import common.utils.Log;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {
    private static final String TAG = Global.class.getSimpleName();

    @Override
    public void onStart(Application app) {
        Log.i(TAG, "onStart ", "Application " + app.toString() + " has started");


        QuartzScheduler.start();
    }

    @Override
    public void onStop(Application app) {

		Log.i(TAG, "onStop ");

        QuartzScheduler.shutdown();

    }
}
