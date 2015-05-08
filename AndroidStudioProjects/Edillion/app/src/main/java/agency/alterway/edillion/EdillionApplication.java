package agency.alterway.edillion;

import android.app.Application;
import android.content.Context;

/**
 * This class serves with the Application Context for the non-UI part of the application.
 * Created by marekrigan on 05/05/15.
 */
public class EdillionApplication extends Application
{
    private static EdillionApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext()
    {
        return instance.getApplicationContext();
    }

}
