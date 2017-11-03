package com.temp.greenbill.data.repository;


import com.temp.greenbill.data.api.Api;
import com.temp.greenbill.data.api.model.auth.registration.UserRegistrationModel;
import com.temp.greenbill.injection.scope.RegistrationScope;

import javax.inject.Inject;

import io.reactivex.Completable;

@RegistrationScope
public class RegistrationRepository {

    private Api api;

    @Inject
    public RegistrationRepository(Api api) {
        this.api = api;
    }

    public Completable registerUser(UserRegistrationModel userRegistrationModel) {
        return api.registerUser(userRegistrationModel);
    }
}
