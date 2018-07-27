package com.youtubevideos;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youtubevideos.api.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Context context;
    TextInputLayout inputEmail, inputPassword;
    TextInputEditText etEmail, etPassword;
    TextView tvLogin, tvSignup;
    private static final String REGEX_EMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context             = this;
        inputEmail          = findViewById(R.id.inputEmail);
        inputPassword       = findViewById(R.id.inputPassword);
        etEmail             = findViewById(R.id.etEmail);
        etPassword          = findViewById(R.id.etPassword);
        tvLogin             = findViewById(R.id.tvLogin);
        tvSignup            = findViewById(R.id.tvSignup);


        tvLogin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLogin:
                if (checkValidation()){
                    Singleton.setPrefBoolean(Constants.USER_SESSION,true,context);
                    startActivity(new Intent(context,MainActivity.class));
                }
                break;

            case R.id.tvSignup:
                startActivity(new Intent(context,SignupActivity.class));
                break;
        }
    }

    public Boolean checkValidation(){
        if (etEmail.getText().toString().trim().length() == 0){
            inputEmail.setError("Enter email");
            return false;
        }
        if (!etEmail.getText().toString().trim().matches(REGEX_EMAIL)){
            inputEmail.setError("Enter valid email");
            return false;
        }
        if (etPassword.getText().toString().trim().length() == 0){
            inputPassword.setError("Enter email");
            return false;
        }
        if (etEmail.getText().toString().equals(Singleton.getPref(Constants.USER_EMAIL,context))) {
            if (!etPassword.getText().toString().equals(Singleton.getPref(Constants.USER_PASSWORD, context))) {
                inputPassword.setError("Enter correct password");
                return false;
            }
        }
        else {
            Toast.makeText(context,"Not registered yet! Signup again",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
