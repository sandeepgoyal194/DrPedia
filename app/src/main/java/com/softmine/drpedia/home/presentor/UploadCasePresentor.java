package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;


import com.softmine.drpedia.home.CaseUploadView;
import com.softmine.drpedia.home.domain.usecases.UploadCaseDetailUseCase;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import rx.Subscriber;

public class UploadCasePresentor implements ICaseUploadPresentor {

    CaseUploadView caseUploadView;
    private UploadCaseDetailUseCase uploadCaseDetailUseCase;

    @Inject
    public UploadCasePresentor(UploadCaseDetailUseCase uploadCaseDetailUseCase)
    {
        this.uploadCaseDetailUseCase = uploadCaseDetailUseCase;
    }

    public void setView(@NonNull CaseUploadView view) {
        this.caseUploadView = view;
    }

    @Override
    public void selectCaseImage() {
        Log.d("uploadimage","clicked selectCaseImage presentor");
        this.caseUploadView.selectImageFromStorage();
    }

    @Override
    public void selectCaseVideo() {
        Log.d("uploadimage","clicked selectCasevideo presentor");
        this.caseUploadView.selectVideoFromStorage();
    }

    @Override
    public void uploadCaseDetails() {

        this.caseUploadView.showProgressBar();

        String caseType;
        String caseDesc;
        ArrayList<String> imageBytes;
        String imageType;

        caseType = this.caseUploadView.getCaseType();
        caseDesc = this.caseUploadView.getCaseDesc();
        imageBytes = (ArrayList<String>) this.caseUploadView.getDataUri();
        imageType = this.caseUploadView.getImageType();

        Log.d("uploadlogs","case type===="+caseType);
        Log.d("uploadlogs","caseid==="+imageType);
        Log.d("uploadlogs","case desc===="+caseDesc);
        Log.d("uploadlogs","case uri size===="+imageBytes.size());

      /*  Log.d("uploadlogs","case type===="+caseType);
        Log.d("uploadlogs","caseid==="+imageType);
        Log.d("uploadlogs","case desc===="+caseDesc);
        Log.d("uploadlogs","case uri size===="+imageBytes.size());

        RequestBody requestCaseType = RequestBody.create(MediaType.parse("text/plain"), caseType);
        RequestBody requestCaseDesc = RequestBody.create(MediaType.parse("text/plain"), caseDesc);
        RequestBody requestCasetypeID = RequestBody.create(MediaType.parse("text/plain"), imageType);
        RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", "caseimage.jpg", requestImageFile);*/

        RequestParams requestParams =   UploadCaseDetailUseCase.createRequestParams(imageType,caseType,caseDesc,imageBytes);

        this.uploadCaseDetailUseCase.execute(requestParams,new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("uploadlogs","upload onCompleted");
                UploadCasePresentor.this.caseUploadView.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                String errorMsg="this is an actual network Failure";
                Log.d("uploadlogs","onError in upload");
                UploadCasePresentor.this.caseUploadView.hideProgressBar();
                if (e instanceof IOException) {
                    UploadCasePresentor.this.caseUploadView.showSnackBar(errorMsg);
                }
                e.printStackTrace();
            }

            @Override
            public void onNext(String commentData) {
                Log.d("uploadlogs","upload onNext=-==="+commentData);

                UploadCasePresentor.this.caseUploadView.setUploadResult(commentData);

            }
        });




    }


}
