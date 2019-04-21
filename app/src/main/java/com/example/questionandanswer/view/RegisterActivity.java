package com.example.questionandanswer.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.questionandanswer.R;
import com.example.questionandanswer.databinding.ActivityLoginBinding;
import com.example.questionandanswer.databinding.ActivityRegisterBinding;
import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.LoginUser;
import com.example.questionandanswer.model.RegisterUser;
import com.example.questionandanswer.model.SharedPrefManager;
import com.example.questionandanswer.viewModel.LoginViewModel;
import com.example.questionandanswer.viewModel.RegisterViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel loginViewModel;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);

        binding.setLifecycleOwner(this);

        binding.setRegisterViewModel(loginViewModel);

        loginViewModel.sendUser().observe(this,
                new Observer<RegisterUser>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(RegisterUser registerUser) {
                if (TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrName())) {
                    binding.txtName.setError("Enter an name");
                    binding.txtName.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrName())) {
                    binding.txtEmailAddress.setError("Enter an Email");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (!registerUser.isEmailValid()) {
                    binding.txtEmailAddress.setError("Enter a Valid E-mail Address");
                    binding.txtEmailAddress.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrPassword())) {
                    binding.txtPassword.setError("Enter a Password");
                    binding.txtPassword.requestFocus();
                }
                else if (!registerUser.isPasswordLengthGreaterThan5()) {
                    binding.txtPassword.setError("Enter at least 6 Digit password");
                    binding.txtPassword.requestFocus();
                }
                else {
                    ApiService apiService = new ApiService(getApplication());
                    apiService.RegisterUser(registerUser.getStrName(),registerUser.getStrEmailAddress(), registerUser.getStrPassword(), new ApiService.OnRegisterResponse() {
                        @Override
                        public void onResponse(String token) {
                            if(token.length()>10){
                                Toast.makeText(RegisterActivity.this, "Ok ,you was registering", Toast.LENGTH_SHORT).show();
                                SharedPrefManager sharedPrefManager = new SharedPrefManager(RegisterActivity.this);
                                sharedPrefManager.write(token);
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "sorry ,a problem has been creating", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }
}
