package com.example.questionandanswer.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.questionandanswer.R;
import com.example.questionandanswer.databinding.ActivityLoginBinding;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.LoginUser;
import com.example.questionandanswer.viewModel.LoginViewModel;


import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);

        loginViewModel.getUser().observe(this, new Observer<LoginUser>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(LoginUser loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrEmailAddress())) {
                    binding.txtEmailAddress.setError("Enter an E-Mail Address");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (!loginUser.isEmailValid()) {
                    binding.txtEmailAddress.setError("Enter a Valid E-mail Address");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())) {
                    binding.txtPassword.setError("Enter a Password");
                    binding.txtPassword.requestFocus();
                }
                else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    binding.txtPassword.setError("Enter at least 6 Digit password");
                    binding.txtPassword.requestFocus();
                }
                else {
                    ApiService apiService = new ApiService(getApplication());
                    apiService.loginUser(loginUser.getStrEmailAddress(), loginUser.getStrPassword(), new ApiService.OnLoginResponse() {
                        @Override
                        public void onResponse(String token) {
                            if(!token.equals(null)){
                                Toast.makeText(LoginActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "No", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

    }
}