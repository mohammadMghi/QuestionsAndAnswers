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

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();


    private MutableLiveData<LoginUser> currentNameLogin;


    public MutableLiveData<LoginUser> getUserLogin() {
        if (currentNameLogin == null) {
            currentNameLogin = new MutableLiveData<LoginUser>();
        }
        return currentNameLogin;
    }



    public void onClick(View view) {

        LoginUser loginUser = new LoginUser(EmailAddress.getValue(), Password.getValue());

        currentNameLogin.postValue(loginUser);

    }

}