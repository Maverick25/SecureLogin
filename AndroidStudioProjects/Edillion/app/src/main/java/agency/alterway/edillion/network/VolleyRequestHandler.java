package agency.alterway.edillion.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import agency.alterway.edillion.EdillionApplication;

/**
 * This code serves to set up the Volley singleton for fetching queues for the upcoming requests.
 *
 * Created by marekrigan on 27/03/15.
 */
public class VolleyRequestHandler
{
    private static VolleyRequestHandler instance = null;
    private RequestQueue queue;
    private OkHttpStack stack;

    private VolleyRequestHandler(boolean isNewStack)
    {
        if (stack == null || isNewStack)
        {
            stack = new OkHttpStack(true);
        }
        queue = Volley.newRequestQueue(EdillionApplication.getAppContext(),stack);
    }

    public static VolleyRequestHandler getInstance(boolean isNewStack)
    {
        if (instance == null || isNewStack)
        {
            instance = new VolleyRequestHandler(isNewStack);
        }
        return instance;
    }

    public RequestQueue getQueue()
    {
        return queue;
    }

}