package agency.alterway.edillion.activities;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.LoginController;
import agency.alterway.edillion.controllers.injections.LoginInjection;
import agency.alterway.edillion.models.Customer;
import agency.alterway.edillion.models.User;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,LoginInjection
{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthorizationTask authorizationTask = null;

    private SharedPreferences preferences;
    private String email;
    private String password;

    private static final int LOGIN_FORM = 0;
    private static final int SIGNUP_FORM = 1;
    private static final int PROGRESS_FORM = 2;

    // UI Injections
    @InjectView(R.id.flipper_login)
    ViewFlipper loginFlipper;
    @InjectView(R.id.actView_email)
    AutoCompleteTextView emailView;
    @InjectView(R.id.field_password)
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // if already logged in and preselected RememberMe switch, this leads straight to login
        // with the persistent credentials.
        if (preferences.getBoolean(getString(R.string.tag_save_login), false))
        {
            String email = preferences.getString(getString(R.string.tag_email), "");
            String password = preferences.getString(getString(R.string.tag_password), "");
            doLogin(email,password);
        }
        else
        {
            loginFlipper.setDisplayedChild(LOGIN_FORM);
        }


        // if just logged out, fill text fields with the information of previously logged in user
        if (preferences.getBoolean(getString(R.string.tag_logout), false))
        {
            emailView.setText(preferences.getString(getString(R.string.tag_email), ""));
            passwordField.setText(preferences.getString(getString(R.string.tag_password), ""));
            preferences.edit().clear().apply();
        }

//        testLogin();

        populateAutoComplete();

        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

    }

    @OnClick(R.id.button_signIn)
    void submitSignIn()
    {
        attemptLogin();
    }

    @OnClick(R.id.textView_registerHere)
    void goToRegister()
    {
        animateForward(SIGNUP_FORM);
    }

    @OnClick(R.id.button_signUp)
    void submitSignUp()
    {
        Intent goToMain = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(goToMain);
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    /**
     * Setting of animations for animating forward within the Flipper.
     * @param nextView = registration view
     */
    private void animateForward(int nextView)
    {
        loginFlipper.setInAnimation(this, R.anim.slide_in_left);
        loginFlipper.setOutAnimation(this, R.anim.slide_out_left);
        loginFlipper.setDisplayedChild(nextView);
    }

    /**
     * Setting of animations for animating forward within the Flipper.
     * @param previousView = login view
     */
    private void animateBackwards(int previousView)
    {
        loginFlipper.setInAnimation(this,R.anim.slide_in_right);
        loginFlipper.setOutAnimation(this,R.anim.slide_out_right);
        loginFlipper.setDisplayedChild(previousView);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (authorizationTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            doLogin(email,password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailView.setAdapter(adapter);
    }

    private void doLogin(String email, String password)
    {
        loginFlipper.setDisplayedChild(PROGRESS_FORM);
        authorizationTask = new AuthorizationTask();
        authorizationTask.execute(email, password);
    }

    /**
     * was used to test login
     */
    private void testLogin()
    {
        String email = "rigan.marek@gmail.com";
        String password = "w84mynewPW";

        LoginController.getInstance(this).doAuthorization(email,password);
    }

    /**
     * Timeout Warning after 30 seconds
     */
    private void showWarningDialog()
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .positiveText(getString(R.string.dialog_continue))
                .negativeText(getString(R.string.dialog_cancel))
                .callback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onNegative(MaterialDialog dialog)
                    {
                        if (authorizationTask != null && authorizationTask.getStatus() != AsyncTask.Status.FINISHED)
                        {
                            authorizationTask.cancel(true);
                        }
                    }
                })
                .title(getString(R.string.title_still_processing))
                .content(getString(R.string.dialog_still_processing_text))
                .build();

        dialog.show();
    }

    /**
     * Method that creates and shows an Error Dialog
     * after an error occurrence.
     */
    private void showErrorDialog(String message)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .neutralText(getString(R.string.dialog_dismiss))
                .title(getString(R.string.error))
                .content(message)
                .build();

        dialog.show();
    }

    @Override
    public void onSuccessfulLogin(User user, Customer customer)
    {
        if(user != null && customer != null)
        {
            Log.v("Authorization Test"," = SUCCESS = "+user.toString()+" and "+customer.toString());
        }
        else
        {
            Log.e("Authorization Test"," = FAILURE = user or customer is NULL");
        }

        // if rememberSwitch is checked, save all the credentials into persistent memory
        if (!preferences.getBoolean(getString(R.string.tag_save_login), false))
        {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean(getString(R.string.tag_save_login), true);
            edit.putString(getString(R.string.tag_email), email);
            edit.putString(getString(R.string.tag_password), password);
            edit.apply();
        }

        Intent goToBase = new Intent(LoginActivity.this, MainActivity.class);
        goToBase.putExtra(getString(R.string.tag_current_user), user);
        startActivity(goToBase);
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onErrorLogin(String message)
    {
        if (message != null)
        {
            Log.e("Authorization Test"," = FAILURE = message: "+message);
        }
        else
        {
            Log.e("Authorization Test"," = FAILURE = no message");
        }

        if (message.startsWith(getString(R.string.text_wrong)))
        {
            emailView.setError(getString(R.string.error_wrong_credentials_short));
            passwordField.setError(getString(R.string.error_wrong_credentials_short));
            emailView.requestFocus();
        }

        showErrorDialog(message);

        authorizationTask = null;
        loginFlipper.setDisplayedChild(LOGIN_FORM);
    }

    /**
     * Task for deploying the authorization process implementing the proper controller and its injection.
     */
    private class AuthorizationTask extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected void onPreExecute()
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (authorizationTask.getStatus() == AsyncTask.Status.RUNNING)
                        {
                            showWarningDialog();
                        }
                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 30000);
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                email = params[0];
                password = params[1];

                LoginController.getInstance(LoginActivity.this).doAuthorization(email, password);
                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

    }

}

