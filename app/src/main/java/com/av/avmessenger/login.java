package com.av.avmessenger;

// LoginActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.av.avmessenger.Class.ApiService;
import com.av.avmessenger.Class.LoginRequest;
import com.av.avmessenger.Class.LoginResponse;
import com.av.avmessenger.Class.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class login extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ApiService apiService;
    String ip="http://192.168.1.114:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTexLogEmail);
        passwordEditText = findViewById(R.id.editTextLogPassword);
        loginButton = findViewById(R.id.logbutton);

        Retrofit retrofit = RetrofitClient.getClient(ip+"/Stage/test/");
        apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        int userId = loginResponse.getUserId();
                        int role = loginResponse.getRole();
                        if (role == 1){
                            Intent intent = new Intent(login.this,MainActivity.class);
                            intent.putExtra("idUser",userId);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(login.this,Employee.class);
                            intent.putExtra("idUser",userId);
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        Toast.makeText(login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(login.this, "Response error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(login.this, "Request failed", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Request failed", t);
            }
        });
    }
}
