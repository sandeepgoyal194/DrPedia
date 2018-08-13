package com.softmine.drpedia.home.domain.usecases;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import frameworks.network.usecases.RequestParams;
import frameworks.network.usecases.UseCase;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public class UploadCaseDetailUseCase extends UseCase<String> {


    public static final String caseTypeTxt = "casetype";
    public static final String caseTypeIDTxt = "casetypeid";
    public static final String caseDescTxt= "casedesc";
    public static final String caseUriListTxt= "caseurilist";
    private final ICaseStudyRepository getCaseRepo;

   static RequestBody requestCaseType;
   static RequestBody requestCaseDesc;
   static MultipartBody.Part imageBody;
   static RequestBody requestCaseTypeID;

   HashMap<String, RequestBody> dataMap = new HashMap<>();
   List<MultipartBody.Part> parts = new ArrayList<>();


    @Inject
    UploadCaseDetailUseCase(ICaseStudyRepository getCaseRepo)
    {
        this.getCaseRepo = getCaseRepo;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {

        String caseTypeId = requestParams.getString(caseTypeIDTxt,null);
        requestCaseTypeID = createPartFromString(caseTypeId);

        String caseType = requestParams.getString(caseTypeTxt,null);
        requestCaseType = createPartFromString(caseType);

        String caseDesc = requestParams.getString(caseDescTxt,null);
        requestCaseDesc = createPartFromString(caseDesc);

        dataMap.put("postType_id", requestCaseTypeID);
        dataMap.put("short_description", requestCaseType);
        dataMap.put("long_description", requestCaseDesc);

        Log.d("uploadlogs","case type ID in createObservable=="+caseTypeId);
        Log.d("uploadlogs","case title createObservable=="+caseType);
        Log.d("uploadlogs","case desc createObservable=="+caseDesc);

        ArrayList<String> uriList = (ArrayList<String>) requestParams.getObject(caseUriListTxt);
        Log.d("uploadlogs","case uri list size in createObservable=="+uriList.size());

        for(String uri : uriList)
        {
            Log.d("uploadlogs","called uri item in createObservable==");
            parts.add(prepareFilePart("image",Uri.fromFile(new File(uri))));
        }

        Log.d("uploadlogs","data map size==="+dataMap.size());
        Log.d("uploadlogs","list size==="+parts.size());

        return this.getCaseRepo.uploadCaseDetail(dataMap,parts);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());
        Log.d("uploadlogs","file path=="+fileUri.toString());
        Log.d("uploadlogs","file path=="+fileUri.getPath());

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        Log.d("uploadlogs","file type=="+mimeType);

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        Log.d("uploadlogs","request filed");

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    @NonNull
    private RequestBody createPartFromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }


    public static RequestParams createRequestParams(String caseTypeID , String caseType , String caseDesc , ArrayList<String> uriList) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(caseTypeIDTxt,caseTypeID);
        requestParams.putString(caseTypeTxt,caseType);
        requestParams.putString(caseDescTxt,caseDesc);
        requestParams.putObject(caseUriListTxt,uriList);
        return requestParams;
    }


}
