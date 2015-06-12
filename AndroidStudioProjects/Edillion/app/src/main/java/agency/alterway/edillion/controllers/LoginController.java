package agency.alterway.edillion.controllers;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import agency.alterway.edillion.EdillionApplication;
import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.injections.LoginInjection;
import agency.alterway.edillion.models.Customer;
import agency.alterway.edillion.models.LoginObject;
import agency.alterway.edillion.models.User;
import agency.alterway.edillion.network.VolleyRequestHandler;

/**
 * Created by marekrigan on 26/05/15.
 */
public class LoginController
{
    private static LoginController instance;
    private static LoginInjection  injection;
    private RequestQueue queue;

    private LoginController(LoginInjection injection)
    {
        LoginController.injection = injection;
    }

    public static LoginController getInstance(LoginInjection injection)
    {
        if (instance == null)
        {
            instance = new LoginController(injection);
        }
        else
        {
            LoginController.injection = injection;
        }
        return instance;
    }

    /**
     * Cancel all pending requests within the activity.
     * @param tag = String tag of the RequestQueue
     */
    public void closeQueue(String tag)
    {
        if(queue != null) {
            queue.cancelAll(tag);
        }
    }


    /**
     * This method finishes the authorisation with sending all 3 parameters to the server
     * to compare the server, username and hashed password with the database values.
     * @param email = String value in the Email field
     * @param password = String value of the hashed Password
     */
    public void doAuthorization(final String email, final String password)
    {
        queue = VolleyRequestHandler.getInstance(true).getQueue();

        StringRequest finalRequest = new StringRequest(Request.Method.POST, makeAuthRequest(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Customer customer;
                        User user;

                        try
                        {
                            Gson gson = new Gson();
                            LoginObject loginObject = gson.fromJson(response,LoginObject.class);

                            customer = loginObject.getCustomer();
                            user = loginObject.getUser();

                            injection.onSuccessfulLogin(user,customer);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            injection.onErrorLogin(EdillionApplication.getAppContext().getString(R.string.error_wrong_credentials));
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                try {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        injection.onErrorLogin(EdillionApplication.getAppContext().getString(R.string.error_no_connection));
                    } else if (error instanceof AuthFailureError) {
                        injection.onErrorLogin(EdillionApplication.getAppContext().getString(R.string.error_auth_failure));
                        error.printStackTrace();
                    } else if (error instanceof ServerError) {
                        NetworkResponse response = error.networkResponse;

                        JSONObject responseStatus = new JSONObject(new String(response.data)).getJSONObject("ResponseStatus");

                        String message = responseStatus.getString("Message");
                        message = message.substring(message.lastIndexOf("$")+2);
                        injection.onErrorLogin(message);
                    } else if (error instanceof NetworkError) {
                        injection.onErrorLogin(EdillionApplication.getAppContext().getString(R.string.error_network));
                        error.printStackTrace();
                    } else if (error instanceof ParseError) {
                        injection.onErrorLogin(EdillionApplication.getAppContext().getString(R.string.error_parse));
                        error.printStackTrace();
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    injection.onErrorLogin("Internal Error: " + e.getMessage());
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> loginParams = new HashMap<>();

                loginParams.put("email",email);
                loginParams.put("password",password);

                return loginParams;
            }
        };

        finalRequest.setTag(LoginInjection.LOGIN_TAG);

        finalRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(30),//time out in 10second
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//DEFAULT_MAX_RETRIES = 1;
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(finalRequest);
    }

    /**
     * Making of URL for the final authorisation.
     * @return URL
     */
    private String makeAuthRequest()
    {
        String PATH_BASE = "/AppGateway?login";
        String PATH_PREFIX = "https://";
        String server = "test.edillion.dk";

        return PATH_PREFIX + server + PATH_BASE;
    }
}
