package com.learn;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qlk.library.notice.QlkDialog;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity
{

    private EditText mPhoneNumView;
    private EditText mSecurityView;
    private EditText mPasswordView;
    private EditText mNickView;
    private Button mGetSecurityButton;

    private SecurityCodeTask mSecurityTask;
    private UserRegisterTask mRegisterTask;
    private CountDownTimer mSecurityCountTimer;

    private String mSecurityCodeFromNet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPhoneNumView = (EditText) findViewById(R.id.ar_edt_phone_number);
        mSecurityView = (EditText) findViewById(R.id.ar_edt_security_code);
        mPasswordView = (EditText) findViewById(R.id.ar_edt_password);
        mNickView = (EditText) findViewById(R.id.ar_edt_nick);
        mGetSecurityButton = (Button) findViewById(R.id.ar_btn_get_security_code);
        mGetSecurityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSecurityCode();
            }
        });
        findViewById(R.id.ar_btn_register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToRegister();
            }
        });


    }

    private void goToRegister()
    {
        if (mSecurityTask != null || mRegisterTask != null)
        {
            return;
        }

        mPhoneNumView.setError(null);
        mSecurityView.setError(null);
        mPasswordView.setError(null);
        mNickView.setError(null);

        final String phoneNum = mPhoneNumView.getText().toString().trim();
        final String securityCode = mSecurityView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        final String nick = mNickView.getText().toString().trim();

        boolean cancelRegister = false;
        View focusView = null;

        if (TextUtils.isEmpty(phoneNum))
        {
            mPhoneNumView.setError(getString(R.string.error_null_phone_number));
            focusView = mPhoneNumView;
            cancelRegister = true;
        }
        if (!cancelRegister && !isPhoneNumberValid(phoneNum))
        {
            mPhoneNumView.setError(getString(R.string.error_invalid_phone_number));
            focusView = mPhoneNumView;
            cancelRegister = true;
        }

        if (!cancelRegister && TextUtils.isEmpty(securityCode))
        {
            mSecurityView.setError(getString(R.string.error_null_security_code));
            focusView = mSecurityView;
            cancelRegister = true;
        }

        if (!cancelRegister && TextUtils.isEmpty(mSecurityCodeFromNet))
        {
            mSecurityView.setError(getString(R.string.error_get_security_code));
            focusView = mSecurityView;
            cancelRegister = true;
        }

        if (!cancelRegister && !securityCode.equals(mSecurityCodeFromNet))
        {
            mSecurityView.setError(getString(R.string.error_error_security_code));
            focusView = mSecurityView;
            cancelRegister = true;
        }

        if (!cancelRegister && TextUtils.isEmpty(password))
        {
            mPasswordView.setError(getString(R.string.error_null_password));
            focusView = mPasswordView;
            cancelRegister = true;
        }

        if (!cancelRegister && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancelRegister = true;
        }

        if (cancelRegister)
        {
            focusView.clearFocus(); //默认全部选中
            focusView.requestFocus();
        }
        else
        {
            mRegisterTask = new UserRegisterTask(phoneNum, password, nick);
            mRegisterTask.execute((Void) null);
        }

    }

    private boolean isPasswordValid(String password)
    {
        return password.length() >= 4 && password.length() <= 20;
    }

    private void getSecurityCode()
    {
        if (mSecurityTask != null)
        {
            return;
        }

        mPhoneNumView.setError(null);

        final String phoneNum = mPhoneNumView.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum))
        {
            mPhoneNumView.setError(getString(R.string.error_null_phone_number));
            mPhoneNumView.requestFocus();
        }
        else if (!isPhoneNumberValid(phoneNum))
        {
            mPhoneNumView.setError(getString(R.string.error_invalid_phone_number));
            mPhoneNumView.clearFocus(); //默认全部选中，以便于更改
            mPhoneNumView.requestFocus();
        }
        else
        {
            mSecurityTask = new SecurityCodeTask(phoneNum);
            mSecurityTask.execute((Void) null);
        }
    }

    private boolean isPhoneNumberValid(String phoneNum)
    {
        return phoneNum.matches("1\\d{10}");
    }

    public class SecurityCodeTask extends AsyncTask<Void, Void, String>
    {
        private final String mPhoneNum;
        private Dialog dialog;

        public SecurityCodeTask(String phoneNum)
        {
            mPhoneNum = phoneNum;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = QlkDialog.showProgressDialog(RegisterActivity.this, "获取验证码中...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            dialog.dismiss();
            mSecurityTask = null;

            if (result != null)
            {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(LoginActivity.RESULT_ACCOUNT, mPhoneNum);
                setResult(RESULT_CANCELED, resultIntent);
            }
            else
            {
//                Toast.makeText(getApplicationContext(), "验证码已发出", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "测试验证码：123456", Toast.LENGTH_SHORT).show();
                startSecurityCounter();
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            dialog.dismiss();
            mSecurityTask = null;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            mSecurityCodeFromNet = "123456";

            return null;
        }
    }

    private void startSecurityCounter()
    {
        mGetSecurityButton.setClickable(false);
        mGetSecurityButton.setBackgroundResource(R.drawable.xml_radius_gray);
        mGetSecurityButton.setTextColor(getResources().getColor(R.color.black));
        mSecurityCountTimer = new CountDownTimer(30000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                mGetSecurityButton.setText(String.format(Locale.CHINA, "%s（%d秒）", getString(R.string.reget_security_code), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish()
            {
                mGetSecurityButton.setText(getString(R.string.reget_security_code));
                mGetSecurityButton.setClickable(true);
                mGetSecurityButton.setBackgroundResource(R.drawable.xml_radius_blue);
                mGetSecurityButton.setTextColor(getResources().getColor(R.color.white));
            }
        };
        mSecurityCountTimer.start();
    }


    public class UserRegisterTask extends AsyncTask<Void, Void, String>
    {
        private final String mPhoneNum;
        private final String mPassword;
        private final String mNick;
        private Dialog dialog;

        public UserRegisterTask(String phoneNum, String password, String nick)
        {
            mPhoneNum = phoneNum;
            mPassword = password;
            mNick = nick;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = QlkDialog.showProgressDialog(RegisterActivity.this, "注册帐号中...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            mRegisterTask = null;
            dialog.dismiss();

            if (result != null)
            {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(LoginActivity.RESULT_ACCOUNT, mPhoneNum);
                setResult(RESULT_CANCELED, resultIntent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();

            mRegisterTask = null;
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            boolean suc = false;
            try
            {
                Thread.sleep(3000);
                suc = true;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (suc)
            {
            }

            return null;
        }


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mSecurityCountTimer != null)
        {
            mSecurityCountTimer.cancel();
        }
    }
}
