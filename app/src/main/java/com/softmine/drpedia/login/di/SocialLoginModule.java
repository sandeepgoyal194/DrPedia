package com.softmine.drpedia.login.di;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softmine.drpedia.getToken.domain.GetTokenUseCase;
import com.softmine.drpedia.getToken.domain.IGetTokenRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class SocialLoginModule {

    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @LoginScope
    @Provides
    GetTokenUseCase getTokenUseCase(IGetTokenRepository getTokenRepository) {
        return new GetTokenUseCase(getTokenRepository);
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}
