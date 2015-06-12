package agency.alterway.edillion.network;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Singleton Object that contains OkHttpClient with the proper CookieHandler
 * for accepting and working with all the cookies
 *
 * Created by marekrigan on 28/03/15.
 */
public class OkHttpStack extends agency.alterway.edillion.network.HurlStack
{
    private final OkUrlFactory mFactory;
    private OkHttpClient client;

    public OkHttpStack(boolean trustAllSsl)
    {
        super(trustAllSsl);
        if (client == null)
        {
            client = new OkHttpClient();
        }
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        mFactory = new OkUrlFactory(client);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException
    {
        return mFactory.open(url);
    }
}