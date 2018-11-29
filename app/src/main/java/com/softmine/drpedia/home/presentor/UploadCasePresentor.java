package com.softmine.drpedia.home.presentor;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.softmine.drpedia.R;
import com.softmine.drpedia.exception.DefaultErrorBundle;
import com.softmine.drpedia.exception.ErrorBundle;
import com.softmine.drpedia.exception.ErrorMessageFactory;
import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.CaseUploadView;
import com.softmine.drpedia.home.domain.usecases.UploadCaseDetailUseCase;
import com.softmine.drpedia.home.service.UploadService;
import com.softmine.drpedia.home.service.UploadTaskParameters;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class UploadCasePresentor implements ICaseUploadPresentor {

    CaseUploadView caseUploadView;
    private UploadCaseDetailUseCase uploadCaseDetailUseCase;
    protected UploadTaskParameters params = new UploadTaskParameters();
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

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(caseUploadView.getContext(),
                errorBundle.getException());
        caseUploadView.showSnackBar(errorMessage);
    }

    @Override
    public void uploadCaseDetails() {


        String caseTitle;
        String caseDesc;
        ArrayList<String> attachmentList;
        String interestType;

        caseTitle = this.caseUploadView.getCaseTitle();
        caseDesc = this.caseUploadView.getCaseDesc();
        attachmentList = (ArrayList<String>) this.caseUploadView.getDataUri();
        interestType = this.caseUploadView.getInterestType();

        params.caseTitle = caseTitle;
        params.caseDesc = caseDesc;
        params.caseCategory = interestType;
        params.attachmentList = attachmentList;

        params.notificationConfig = this.caseUploadView.getNotificationConfig(R.string.multipart_upload);

        if( params.notificationConfig!=null)
        {
            Log.d("MyService","notificationConfig not null");
        }
        else
        {
            Log.d("MyService","notificationConfig null");
        }

        final Intent intent = new Intent(this.caseUploadView.getContext(), UploadService.class);
        intent.setAction(UploadService.getActionUpload());
        intent.putExtra(UploadService.PARAM_TASK_PARAMETERS, params);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if (params.notificationConfig == null)
            {
              throw new IllegalArgumentException("Android Oreo requires a notification configuration for the service to run. https://developer.android.com/reference/android/content/Context.html#startForegroundService(android.content.Intent)");
            }
             this.caseUploadView.getContext().startForegroundService(intent);
        } else {
            this.caseUploadView.getContext().startService(intent);
        }

        //this.caseUploadView.getContext().startService(intent);
      /*  if (TextUtils.isEmpty(caseType)) {
            this.caseUploadView.onCaseTypeEmpty();
        } else if (TextUtils.isEmpty(caseDesc)) {
            this.caseUploadView.onCaseDescEmpty();
        }
        else if(attachmentList.size()==0)
        {
            this.caseUploadView.onUriListEmpty();
        }
        else
        {
            Log.d("uploadlogs","case type===="+caseType);
            Log.d("uploadlogs","caseid==="+imageType);
            Log.d("uploadlogs","case desc===="+caseDesc);
            Log.d("uploadlogs","case uri size===="+attachmentList.size());
            this.caseUploadView.showProgressBar();
            RequestParams requestParams =   UploadCaseDetailUseCase.createRequestParams(imageType,caseType,caseDesc,attachmentList);

            this.uploadCaseDetailUseCase.execute(requestParams,new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    Log.d("uploadlogs","upload onCompleted");
                    UploadCasePresentor.this.caseUploadView.hideProgressBar();
                }

                @Override
                public void onError(Throwable e) {
                    UploadCasePresentor.this.caseUploadView.hideProgressBar();
                    e.printStackTrace();
                    if(e instanceof IOException)
                    {
                        if(e instanceof HttpException)
                        {
                            Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                        else if(e instanceof ResponseException)
                        {
                            Log.d("bookmarkresponse","ResponseException code  "+((ResponseException)e).getMessage());
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                        else if(e instanceof NetworkConnectionException)
                        {
                            Log.d("loginresponse","other issues");
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                        }
                        else
                        {
                            Log.d("loginresponse", "other issue");
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                    }
                    else
                    {
                        if(e instanceof JSONException) {
                            Log.d("loginresponse", "Json Parsing exception");
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                        else if(e instanceof HttpException)
                        {
                            Log.d("loginresponse", "Http exception issue");
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                        else
                        {
                            Log.d("loginresponse", "other issue");
                            UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                        }
                    }
                }

                @Override
                public void onNext(String commentData) {
                    Log.d("uploadlogs","upload onNext=-==="+commentData);

                    UploadCasePresentor.this.caseUploadView.setUploadResult(commentData);

                }
            });
        }
*/
    }


}
