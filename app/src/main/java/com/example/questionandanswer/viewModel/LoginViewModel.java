package com.example.questionandanswer.viewModel;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.LoginUser;
import com.example.questionandanswer.model.RegisterUser;

public class MyViewModel extends ViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    public MutableLiveData<String> NameRegister = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddressRegister = new MutableLiveData<>();
    public MutableLiveData<String> PasswordRegister = new MutableLiveData<>();


    private MutableLiveData<LoginUser> currentNameLogin;
    private MutableLiveData<RegisterUser> currentNameRegister;

    public MutableLiveData<LoginUser> getUserLogin() {
        if (currentNameLogin == null) {
            currentNameLogin = new MutableLiveData<LoginUser>();
        }
        return currentNameLogin;
    }


    public MutableLiveData<RegisterUser> getUserRegister() {
        if (currentNameRegister == null) {
            currentNameRegister = new MutableLiveData<RegisterUser>();
        }
        return currentNameRegister;
    }
    public void onClick(View view) {

        LoginUser loginUser = new LoginUser(EmailAddress.getValue(), Password.getValue());

        currentNameLogin.postValue(loginUser);

    }


    public void onClickRegister(View view) {

        RegisterUser registerUser = new RegisterUser(NameRegister.getValue(),EmailAddress.getValue(), Password.getValue());

        currentNameRegister.postValue(registerUser);

    }

}