package com.softmine.drpedia.getToken.di;


import com.google.gson.Gson;
import com.softmine.drpedia.getToken.data.interceptor.GetTokenInterceptor;
import com.softmine.drpedia.getToken.data.net.LoginAPI;
import com.softmine.drpedia.getToken.data.net.LoginAPIURL;
import com.softmine.drpedia.getToken.data.repository.GetTokenDataRepository;
import com.softmine.drpedia.getToken.data.repository.datasource.GetTokenCloudDataSource;
import com.softmine.drpedia.getToken.data.repository.datasource.GetTokenDataFactory;
import com.softmine.drpedia.getToken.domain.IGetTokenRepository;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import frameworks.appsession.AppSessionManager;
import frameworks.network.interceptor.ErrorResponseInterceptor;
import frameworks.network.model.BaseResponseError;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sandeep on 5/5/18.
 */
@Module
public class GetTokenModule {
    @Provides
    LoginAPI provideLoginApi(Retrofit retrofit) {
        return retrofit.create(LoginAPI.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder ,Gson gson) {
        return retrofitBuilder
                .baseUrl(LoginAPIURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .readTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new GetTokenInterceptor())
                .addInterceptor(new ErrorResponseInterceptor(BaseResponseError.class))
                .build();
    }

    @Provides
    IGetTokenRepository getTokenRepository(GetTokenDataFactory getTokenDataFactory) {
        return new GetTokenDataRepository(getTokenDataFactory);
    }

    @Provides
    GetTokenDataFactory getTokenDataFactory(GetTokenCloudDataSource getTokenCloudDataSource) {
        return new GetTokenDataFactory(getTokenCloudDataSource);
    }

    @Provides
    GetTokenCloudDataSource getTokenCloudDataSource(LoginAPI loginAPI,AppSessionManager appSessionManager) {
        return new GetTokenCloudDataSource(loginAPI,appSessionManager);
    }





}
