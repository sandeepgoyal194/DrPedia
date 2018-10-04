package com.softmine.drpedia.home.di;

import com.softmine.drpedia.home.service.UploadService;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    UploadService mService;

    public ServiceModule(UploadService service) {
        mService = service;
    }

    @Provides
    UploadService provideMyService() {
        return mService;
    }
}