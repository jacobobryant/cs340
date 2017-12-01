package com.thefunteam.android.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.thefunteam.android.Poller;
import com.thefunteam.android.model.userInfo;
import com.thefunteam.android.presenter.LoginPresenter;
import com.thefunteam.android.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ObservingActivity {

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public LoginActivity() {
        super();

        presenter = loginPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.username = mUsernameView.getText().toString();
                userInfo.password = mPasswordView.getText().toString();
                loginPresenter.login(mUsernameView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.go_to_register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Poller.getInstance().startPolling();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Poller.getInstance().stopPolling();
    }

    public void presentAvailableGames() {
        startActivity(new Intent(this, AvailableGamesActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginPresenter.update();
    }
}

