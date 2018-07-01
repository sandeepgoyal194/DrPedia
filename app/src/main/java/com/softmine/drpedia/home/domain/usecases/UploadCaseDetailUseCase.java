package com.softmine.drpedia.home.domain.usecases;

import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class UploadCaseDetailUseCase extends UseCase<String> {


    public static final String like_Status = "status";
    public static final String postID = "post_id";
    private final ICaseStudyRepository getCaseRepo;

   static RequestBody requestCaseDesc3;
   static RequestBody requestCaseDesc4;
   static MultipartBody.Part imageBody;
   static RequestBody requestCaseTypeID;

    @Inject
    UploadCaseDetailUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return this.getCaseRepo.uploadCaseDetail(requestCaseTypeID,requestCaseDesc3,requestCaseDesc4,imageBody);
    }

    public static void setCaseUploadParams( RequestBody requestCasetypeID1,RequestBody requestCaseDesc2,RequestBody requestCaseDesc1, MultipartBody.Part imageBody1)
    {
        requestCaseTypeID = requestCasetypeID1;
        requestCaseDesc3 = requestCaseDesc2;
        requestCaseDesc4 = requestCaseDesc1;
        imageBody = imageBody1;
    }

}
