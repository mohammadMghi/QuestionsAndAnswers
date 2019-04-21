package com.example.questionandanswer.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.example.questionandanswer.model.RegisterUser;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();


    private MutableLiveData<RegisterUser> currentName;

    public MutableLiveData<RegisterUser> sendUser() {
        if (currentName == null) {
            currentName = new MutableLiveData<RegisterUser>();
        }
        return currentName;
    }

    public void onClick(View view) {

        RegisterUser registerUser = new RegisterUser(name.getValue(),EmailAddress.getValue(), Password.getValue());

        currentName.postValue(registerUser);

    }

}
