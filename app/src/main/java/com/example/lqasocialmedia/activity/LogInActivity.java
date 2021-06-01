package com.example.lqasocialmedia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lqasocialmedia.R;
import com.example.lqasocialmedia.Session;
import com.example.lqasocialmedia.dto.LogInDTO;
import com.example.lqasocialmedia.network.NetworkProvider;
import com.example.lqasocialmedia.network.SocialMediaService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LogInActivity";
    private Session session;

    private EditText txtEmail, txtPassword;
    private TextView lblLogInError;
    private Button btnLogIn, btnSignUp;

    private SocialMediaService socialMediaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        session = new Session(this);
        socialMediaService = NetworkProvider.getInstance().getRetrofit().create(SocialMediaService.class);

        initView();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogIn();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        lblLogInError = findViewById(R.id.lblLogInError);

        lblLogInError.setText("");
    }

    private void tryLogIn() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        LogInDTO logInDTO = new LogInDTO(email, password);
        Log.i(LOG_TAG, "start request");
        socialMediaService.logIn(logInDTO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(LOG_TAG, "done request");
                if (response.isSuccessful()) {
                    session.setToken(email);
                    session.setAccountId(Integer.parseInt(response.body()));
                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    StringBuilder error = new StringBuilder();
                    try {
                        BufferedReader bufferedReader = null;
                        if (response.errorBody() != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(
                                    response.errorBody().byteStream()));

                            String eLine = null;
                            while ((eLine = bufferedReader.readLine()) != null) {
                                error.append(eLine);
                            }
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                        error.append(e.getMessage());
                    }
                    lblLogInError.setText(error);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(LOG_TAG, t.getMessage());
            }
        });
    }
}