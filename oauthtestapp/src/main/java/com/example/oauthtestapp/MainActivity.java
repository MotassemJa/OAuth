package com.example.oauthtestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moja.oauth2.Authenticator;
import com.example.moja.oauth2.OAuthConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private Button mBtnLogin;
    private Button mBtnShow;
    private TextView mTvMsg;

    private static Authenticator mAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            OAuthConfig confing = new OAuthConfig(new ArrayList<String>(), new URI("http://brentertainment.com/oauth2/lockdin/token")
                    , null, "demoapp", "demopass");
            mAuthenticator = new Authenticator(confing);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        grabAttributes();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthenticator.retrieveAccessToken(new Authenticator.AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(final String s, final Exception e) {
                        if (e != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTvMsg.setText(e.getMessage());
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTvMsg.setText(s);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();

        mAuthenticator.authenticateWithUsername(username, password, new Authenticator.AuthenticationCallback() {
            @Override
            public void onAuthenticationCompleted(boolean flag, final Exception e) {
                if (flag) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void grabAttributes() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnShow = (Button) findViewById(R.id.btn_show);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);
    }
}
