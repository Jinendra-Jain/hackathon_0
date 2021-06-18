package com.example.hackathon_0.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hackathon_0.model.AppRepositoryLOP;

import org.jetbrains.annotations.NotNull;

public class LoginOTPPageViewModel extends AndroidViewModel {
    private AppRepositoryLOP vAppRepositoryLOP;
    public LoginOTPPageViewModel(@NonNull @NotNull Application application) {
        super(application);


        vAppRepositoryLOP = new AppRepositoryLOP(application);

    }
}

