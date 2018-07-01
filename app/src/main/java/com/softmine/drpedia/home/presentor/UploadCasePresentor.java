package com.softmine.drpedia.home.presentor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.softmine.drpedia.home.CaseUploadView;
import com.softmine.drpedia.home.domain.usecases.UploadCaseDetailUseCase;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

     this.caseUploadView.selectImageFromStorage();
    }

    @Override
    public void uploadCaseDetails() {

        String caseType;
        String caseDesc;
        byte[] imageBytes;
        String imageType;

        caseType = this.caseUploadView.getCaseType();
        caseDesc = this.caseUploadView.getCaseDesc();
        imageBytes = this.caseUploadView.getImageBytes();
        imageType = this.caseUploadView.getImageType();

        Log.d("uploadlogs","case type===="+caseType);
        Log.d("uploadlogs","case desc===="+caseDesc);
        Log.d("uploadlogs","case image===="+imageBytes);

        Log.d("uploadlogs","type==="+caseType);
        Log.d("uploadlogs","desc==="+caseDesc);
        Log.d("uploadlogs","caseid==="+imageType);

        RequestBody requestCaseType = RequestBody.create(MediaType.parse("text/plain"), caseType);
        RequestBody requestCaseDesc = RequestBody.create(MediaType.parse("text/plain"), caseDesc);
        RequestBody requestCasetypeID = RequestBody.create(MediaType.parse("text/plain"), imageType);
        RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", "caseimage.jpg", requestImageFile);

        UploadCaseDetailUseCase.setCaseUploadParams(requestCasetypeID,requestCaseDesc,requestCaseDesc,imageBody);

        this.uploadCaseDetailUseCase.execute(RequestParams.EMPTY,new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("uploadlogs","upload onCompleted");
            }

            @Override
            public void onError(Throwable e) {

                Log.d("uploadlogs","onError in upload");
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
