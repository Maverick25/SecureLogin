package agency.alterway.edillion.controllers.injections;

import agency.alterway.edillion.models.Customer;
import agency.alterway.edillion.models.User;

/**
 * Created by marekrigan on 26/05/15.
 */
public interface LoginInjection
{
    String LOGIN_TAG = "LOGIN_TAG";

    void onSuccessfulLogin(User user, Customer customer);
    void onErrorLogin(String message);
}
