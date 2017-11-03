package com.temp.greenbill.domain.interactor.registration;


import com.temp.greenbill.data.api.model.auth.registration.FirstStepRegistrationModel;
import com.temp.greenbill.data.api.model.auth.registration.SecondStepRegistrationModel;
import com.temp.greenbill.data.api.model.auth.registration.UserRegistrationModel;
import com.temp.greenbill.data.repository.RegistrationRepository;
import com.temp.greenbill.domain.validator.features.registration.RegistrationValidator;
import com.temp.greenbill.injection.scope.RegistrationScope;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@RegistrationScope
public class RegistrationInteractor {

    private UserRegistrationModel.Builder builder;
    private RegistrationRepository registrationRepository;
    private RegistrationValidator registrationValidator;

    @Inject
    public RegistrationInteractor(RegistrationRepository registrationRepository,
                                  RegistrationValidator registrationValidator) {
        this.registrationRepository = registrationRepository;
        this.registrationValidator = registrationValidator;
        builder = new UserRegistrationModel.Builder();
    }

    public Completable executeFirstStepRegistration(String name, String lastName, String email) {
        return registrationValidator.validateFirstStepRegistration(new FirstStepRegistrationModel(name, lastName, email))
                .doOnSuccess(builder::setFirstStepModel)
                .toCompletable()
                .subscribeOn(Schedulers.io());
    }

    public Completable executeSecondStepRegistration(String userName, String password, String passwordConfirmation) {
        return registrationValidator.validateSecondStepRegistration(new SecondStepRegistrationModel(userName, password, passwordConfirmation))
                .doOnSuccess(builder::setSecondStepModel)
                .toCompletable()
                .subscribeOn(Schedulers.io());
    }

    public Completable registerUser() {
        return registrationRepository.registerUser(builder.build())
                .subscribeOn(Schedulers.io());
    }

    public Single<FirstStepRegistrationModel> getFirstStepRegistrationModel() {
        return Single.just(builder.getFirstStepModel() != null ? builder.getFirstStepModel() : new FirstStepRegistrationModel());
    }

    public Single<SecondStepRegistrationModel> getSecondStepRegistrationModel() {
        return Single.just(builder.getSecondStepModel() != null ? builder.getSecondStepModel() : new SecondStepRegistrationModel());
    }
}
