package com.temp.greenbill.injection.module.global.api;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.temp.greenbill.data.api.Api;
import com.temp.greenbill.data.exception.UnauthorizedException;
import com.temp.greenbill.data.prefs.PreferencesManager;
import com.temp.greenbill.injection.qualifier.HeaderInterceptor;
import com.temp.greenbill.injection.qualifier.UnauthorizedInterceptor;
import com.temp.greenbill.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
@Module
public class ApiModule {

    private final String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(@HeaderInterceptor Interceptor headerInterceptor) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        return okHttpClient;
    }

    @HeaderInterceptor
    @Singleton
    @Provides
    public Interceptor provideHeaderInterceptor(PreferencesManager preferencesManager) {
        return chain -> {
            Request request = chain.request();
            final Headers.Builder builder = request.headers()
                    .newBuilder();
            builder.add("Accept", "application/json");
            if (preferencesManager.isSessionExist()) {
                builder.add(Constants.AUTHORIZATION_HEADER, "Bearer " + preferencesManager.getSession());
            }
            final Headers headers = builder.build();
            request = request.newBuilder().headers(headers).build();
            return chain.proceed(request);
        };
    }

    @UnauthorizedInterceptor
    @Singleton
    @Provides
    public Interceptor provideUnauthorizeInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            if (response.code() == 401) {
                throw new UnauthorizedException();
            }
            return response;
        };
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        final GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Singleton
    @Provides
    public Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}
