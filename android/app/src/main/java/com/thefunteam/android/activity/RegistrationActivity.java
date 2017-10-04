package com.thefunteam.android.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.thefunteam.android.R;
import com.thefunteam.android.presenter.RegistrationPresenter;


/**
 * A login screen that offers login via email/password.
 */
public class RegistrationActivity extends ObservingActivity {

    private RegistrationPresenter registrationPresenter = new RegistrationPresenter(this);

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mReenterPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public RegistrationActivity() {
        super();
        presenter = registrationPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mReenterPasswordView = (EditText) findViewById(R.id.register_reenter_password);

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPasswordView.getText().toString().equals(mReenterPasswordView.getText().toString())) {
                    registrationPresenter.register(mEmailView.getText().toString(), mPasswordView.getText().toString());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT);
                    toast.show();
                    mPasswordView.setText("");
                    mReenterPasswordView.setText("");
                }
            }
        });
    }
}

