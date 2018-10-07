package com.softmine.drpedia.home.di;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softmine.drpedia.home.domain.datasource.GetCaseStudyDataFactory;
import com.softmine.drpedia.home.domain.datasource.GetCaseStudyDataSource;
import com.softmine.drpedia.home.domain.interceptor.RequestInterceptor;
import com.softmine.drpedia.home.domain.repositry.GetCaseStudyDataRepository;
import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.CategoryMainItem;
import com.softmine.drpedia.home.net.CaseStudyAPI;
import com.softmine.drpedia.home.net.CaseStudyAPIURL;
import com.softmine.drpedia.utils.CategoryListInstanceCreator;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import frameworks.appsession.AppSessionManager;
import frameworks.di.scope.ApplicationScope;
import frameworks.imageloader.GlideImageLoaderImpl;
import frameworks.imageloader.ImageLoader;
import frameworks.network.interceptor.ErrorResponseInterceptor;
import frameworks.network.model.BaseResponseError;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class GetCaseStudyListModule {

    private final Context context;
    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public GetCaseStudyListModule(Context context)
    {
        this.context = context;
    }

    @Provides
    Context provideContext()
    {
        return context;
    }

    @Provides
    CaseStudyAPI provideCaseStudyApi(Retrofit retrofit) {
        return retrofit.create(CaseStudyAPI.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder,Gson gson) {

       // retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));

        return   retrofitBuilder
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(CaseStudyAPIURL.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(CategoryMainItem.class , new CategoryListInstanceCreator())
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, AppSessionManager appSessionManager) {
        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new RequestInterceptor(appSessionManager))
                .addInterceptor(new ErrorResponseInterceptor(BaseResponseError.class))
                .build();
    }

    @Provides
    ICaseStudyRepository getTokenRepository(GetCaseStudyDataFactory getCaseStudyDataFactory) {
        return new GetCaseStudyDataRepository(getCaseStudyDataFactory);
    }

    @Provides
    GetCaseStudyDataFactory getTokenDataFactory(GetCaseStudyDataSource getCaseStudyDataSource) {
        return new GetCaseStudyDataFactory(getCaseStudyDataSource);
    }

    @Provides
    GetCaseStudyDataSource getTokenCloudDataSource(CaseStudyAPI caseStudyAPI) {
        return new GetCaseStudyDataSource(caseStudyAPI);
    }

    @Provides
    ImageLoader getImageLoader(Context context)
    {
        return new GlideImageLoaderImpl(context);
    }


}
