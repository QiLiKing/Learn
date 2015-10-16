package com.learn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qlk.library.notice.QlkDialog;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    private static final int REQUEST_LOGIN = 0;
    static final String RESULT_ACCOUNT = "account";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mAccountView;
    private EditText mPasswordView;
    private View mLoginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mLoginLayout = findViewById(R.id.al_lyt_login);
        mAccountView = (EditText) findViewById(R.id.al_edt_account_number);
        mPasswordView = (EditText) findViewById(R.id.al_edt_password);
        findViewById(R.id.al_btn_login).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        findViewById(R.id.al_txt_register).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REQUEST_LOGIN);
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancelLogin = false;
        View focusView = null;

        if (TextUtils.isEmpty(account))
        {
            mAccountView.setError(getString(R.string.error_null_account));
            focusView = mAccountView;
            cancelLogin = true;
        }

        if (!cancelLogin && TextUtils.isEmpty(password))
        {
            mPasswordView.setError(getString(R.string.error_null_password));
            focusView = mPasswordView;
            cancelLogin = true;
        }

        if (cancelLogin)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(account, password);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;
        private Dialog dialog;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = QlkDialog.showProgressDialog(LoginActivity.this, "正在登录，请稍后...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service.

            try
            {
                // Simulate network access.
                Thread.sleep(4000);
            } catch (InterruptedException e)
            {
                return false;
            }


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            dialog.dismiss();

            if (success)
            {
                finish();
            }
            else
            {
                mPasswordView.setError("error");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            dialog.dismiss();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN)
        {
            if (resultCode == RESULT_OK)
            {
                finish();
            }
            else
            {
                if (data != null)
                {
                    String account = data.getStringExtra(RESULT_ACCOUNT);
                    if (!TextUtils.isEmpty(account))
                    {
                        mAccountView.setText(account);
                        mAccountView.requestFocus();
                    }
                }
            }
        }
    }
}

