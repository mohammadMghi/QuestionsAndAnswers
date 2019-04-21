package com.example.questionandanswer.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questionandanswer.R;
import com.example.questionandanswer.databinding.ActivityLoginBinding;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.LoginUser;
import com.example.questionandanswer.model.SharedPrefManager;
import com.example.questionandanswer.viewModel.LoginViewModel;

import java.net.InetAddress;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isInternetAvailable()) {
            //connected
        } else {
            Toast.makeText(this, "please check internet connection...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        loginViewModel.getUserLogin().observe(this, new Observer<LoginUser>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(LoginUser loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrEmailAddress())) {
                    binding.txtEmailAddress.setError("Enter an E-Mail Address");
                    binding.txtEmailAddress.requestFocus();
                } else if (!loginUser.isEmailValid()) {
                    binding.txtEmailAddress.setError("Enter a Valid E-mail Address");
                    binding.txtEmailAddress.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())) {
                    binding.txtPassword.setError("Enter a Password");
                    binding.txtPassword.requestFocus();
                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.txtPassword.setError("Enter at least 6 Digit password");
                    binding.txtPassword.requestFocus();
                } else {
                    ApiService apiService = new ApiService(getApplication());
                    apiService.loginUser(loginUser.getStrEmailAddress(), loginUser.getStrPassword(), new ApiService.OnLoginResponse() {
                        @Override
                        public void onResponse(String token) {
                            if (token.length() > 10) {
                                Toast.makeText(LoginActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                SharedPrefManager sharedPrefManager = new SharedPrefManager(LoginActivity.this);
                                sharedPrefManager.write(token);
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent1);
                            }
                        }

                        @Override
                        public void onResponseError(int state) {
                            if (state == 1) {
                                Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                            } else if (state == 0) {
                                Toast.makeText(LoginActivity.this, "Error from server please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });


        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }
}