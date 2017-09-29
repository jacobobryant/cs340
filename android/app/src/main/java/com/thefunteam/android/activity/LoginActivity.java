package com.thefunteam.android.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.thefunteam.android.presenter.LoginPresenter;
import com.thefunteam.android.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ObservingActivity {

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    LoginActivity() {
        super();
        presenter = loginPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.login(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.go_to_register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this, RegistrationActivity.class), 0);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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

