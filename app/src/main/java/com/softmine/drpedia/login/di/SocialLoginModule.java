package com.softmine.drpedia.login.di;


import com.softmine.drpedia.getToken.domain.GetTokenUseCase;
import com.softmine.drpedia.getToken.domain.IGetTokenRepository;
import com.softmine.drpedia.login.di.LoginScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SocialLoginModule {

    @LoginScope
    @Provides
    GetTokenUseCase getTokenUseCase(IGetTokenRepository getTokenRepository) {
        return new GetTokenUseCase(getTokenRepository);
    }
}
