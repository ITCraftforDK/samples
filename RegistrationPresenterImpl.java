package com.temp.greenbill.presentation.mvp.registration.presenter.impl;


import com.temp.greenbill.data.mapper.repository.registration.StepDataMapper;
import com.temp.greenbill.domain.interactor.registration.RegistrationInteractor;
import com.temp.greenbill.injection.scope.RegistrationScope;
import com.temp.greenbill.presentation.base.BasePresenter;
import com.temp.greenbill.presentation.mvp.registration.presenter.contract.IRegistrationPresenter;
import com.temp.greenbill.presentation.mvp.registration.view.contract.IRegistrationView;
import com.temp.greenbill.utils.errorhandler.ErrorHandler;
import com.temp.greenbill.utils.errorhandler.ErrorHandlerConfig;
import com.temp.greenbill.utils.errorhandler.ErrorHandlerModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

@RegistrationScope
public class RegistrationPresenterImpl extends BasePresenter<IRegistrationView> implements IRegistrationPresenter {

    private RegistrationInteractor registrationInteractor;
    private StepDataMapper stepDataMapper;
    private ErrorHandler errorHandler;

    @Inject
    public RegistrationPresenterImpl(RegistrationInteractor registrationInteractor, StepDataMapper stepDataMapper, ErrorHandler errorHandler) {
        this.registrationInteractor = registrationInteractor;
        this.stepDataMapper = stepDataMapper;
        this.errorHandler = errorHandler;
        this.errorHandler
                .addModule(new ErrorHandlerModule("email").withAction(s -> getView().showErrorDialog(s)))
                .addModule(new ErrorHandlerModule("username").withAction(s -> getView().showErrorDialog(s)))
                .addModule(new ErrorHandlerModule("first_name").withAction(s -> getView().showErrorDialog(s)))
                .addModule(new ErrorHandlerModule("last_name").withAction(s -> getView().showErrorDialog(s)))
                .addConfig(new ErrorHandlerConfig(ErrorHandlerConfig.ResolutionStrategy.PASS_FIRST));
    }

    @Override
    public void openFirstStep() {
        registrationInteractor.getFirstStepRegistrationModel()
                .map(stepDataMapper::mapFirstStepData)
                .doOnSubscribe(this::bindToLifecycle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::showFirstStep);
    }

    @Override
    public void openSecondStep() {
        registrationInteractor.getSecondStepRegistrationModel()
                .map(stepDataMapper::mapSecondStepData)
                .doOnSubscribe(this::bindToLifecycle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getView()::showSecondStep);
    }

    @Override
    public void endRegistration() {
        registrationInteractor.registerUser()
                .doOnSubscribe(this::bindToLifecycle)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscribe -> getView().showProgress())
                .doFinally(getView()::hideProgress)
                .subscribe(getView()::finish, this::handleError);
    }

    @Override
    protected void handleHttpException(HttpException httpException) {
        errorHandler.handle(httpException);
    }
}
