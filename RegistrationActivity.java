package com.temp.greenbill.presentation.mvp.registration.view.impl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.temp.greenbill.Application;
import com.temp.greenbill.R;
import com.temp.greenbill.data.viewmodel.registration.FirstStepRegistrationViewModel;
import com.temp.greenbill.data.viewmodel.registration.SecondStepRegistrationViewModel;
import com.temp.greenbill.injection.component.app.RegistrationComponent;
import com.temp.greenbill.presentation.base.BaseActivity;
import com.temp.greenbill.presentation.mvp.registration.presenter.contract.IRegistrationPresenter;
import com.temp.greenbill.presentation.mvp.registration.steps.first.view.impl.FirstRegistrationStepFragment;
import com.temp.greenbill.presentation.mvp.registration.steps.second.view.impl.SecondRegistrationStepFragment;
import com.temp.greenbill.presentation.mvp.registration.view.contract.IRegistrationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends BaseActivity implements IRegistrationView {

    @Inject
    IRegistrationPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RegistrationComponent registrationComponent;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        registrationComponent = Application.getRegistrationComponent();
        registrationComponent.inject(this);
        presenter.attachView(this);
        initViews(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void initViews(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        if (savedInstanceState == null) {
            replaceFragment(R.id.container, FirstRegistrationStepFragment.newInstance(), false, false);
        }
    }

    @Override
    public void toFirstStep() {
        presenter.openFirstStep();
    }

    @Override
    public void toSecondStep() {
        presenter.openSecondStep();
    }

    @Override
    public void showFirstStep(FirstStepRegistrationViewModel data) {
        hideKeyboard();
        replaceFragment(R.id.container, FirstRegistrationStepFragment.newInstance(data));
    }

    @Override
    public void showSecondStep(SecondStepRegistrationViewModel data) {
        hideKeyboard();
        replaceFragment(R.id.container, SecondRegistrationStepFragment.newInstance(data));
    }

    @Override
    public void endRegistration() {
        presenter.endRegistration();
    }

    public RegistrationComponent getRegistrationComponent() {
        return registrationComponent;
    }
}
