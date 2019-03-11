package com.example.questionandanswer.viewModel;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.questionandanswer.model.ApiService;
import com.example.questionandanswer.model.LoginUser;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginUser> currentName;

    public MutableLiveData<LoginUser> getUser() {
        if (currentName == null) {
            currentName = new MutableLiveData<LoginUser>();
        }
        return currentName;
    }

    public void onClick(View view) {

        LoginUser loginUser = new LoginUser(EmailAddress.getValue(), Password.getValue());

        currentName.postValue(loginUser);

    }

}