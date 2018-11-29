package com.softmine.drpedia.home.domain.usecases;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;


import com.softmine.drpedia.home.domain.repositry.ICaseStudyRepository;
import com.softmine.drpedia.home.model.UploadCaseDetail;
import com.softmine.drpedia.utils.GsonFactory;

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
    public static final String caseUploadedIDsListTxt= "caseUploadedIDsListTxt";
    public static final String caseImageUriTxt= "caseimageuritxt";
    public static final String caseVideoUriTxt= "casevideouritxt";
    public static final String caseThumbnailUriTxt= "casethumbnailuritxt";
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

        UploadCaseDetail uploadCaseDetail = new UploadCaseDetail();

        String caseTypeId = requestParams.getString(caseTypeIDTxt,null);
        requestCaseTypeID = createPartFromString(caseTypeId);

        String caseType = requestParams.getString(caseTypeTxt,null);
        requestCaseType = createPartFromString(caseType);

        String caseDesc = requestParams.getString(caseDescTxt,null);
        requestCaseDesc = createPartFromString(caseDesc);

        ArrayList<Integer> uriList = (ArrayList<Integer>) requestParams.getObject(caseUploadedIDsListTxt);

        uploadCaseDetail.setSubtype_id(caseTypeId);
        uploadCaseDetail.setShort_description(caseType);
        uploadCaseDetail.setLong_description(caseDesc);
        uploadCaseDetail.setUpload_item_id(uriList);

        String uploadData = GsonFactory.getGson().toJson(uploadCaseDetail);

        return this.getCaseRepo.uploadCaseDetail(uploadData);
    }

    public static RequestParams createRequestParams(String caseTypeID , String caseType , String caseDesc , ArrayList<Integer> uploaded_item_id) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(caseTypeIDTxt,caseTypeID);
        requestParams.putString(caseTypeTxt,caseType);
        requestParams.putString(caseDescTxt,caseDesc);
        requestParams.putObject(caseUploadedIDsListTxt,uploaded_item_id);
        return requestParams;
    }

    public Observable<Integer> createImageUploadObservable(RequestParams requestParams) {

        parts.clear();

        String uriTxt = (String) requestParams.getObject(caseImageUriTxt);
        parts.add(prepareFilePart("image",Uri.fromFile(new File(uriTxt))));
        return this.getCaseRepo.uploadCaseImage(parts);
    }

    public static RequestParams createImageUploadRequestParams(String uri)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(caseImageUriTxt,uri);
        return requestParams;
    }

    public Observable<Integer> createVideoUploadObservable(RequestParams requestParams) {

        parts.clear();

        String videoUriTxt = (String) requestParams.getObject(caseVideoUriTxt);
        String thumbnailUriTxt = (String) requestParams.getObject(caseThumbnailUriTxt);

        parts.add(prepareFilePart("video",Uri.fromFile(new File(videoUriTxt))));
        parts.add(prepareFilePart("thumbnail",Uri.fromFile(new File(thumbnailUriTxt))));

        return this.getCaseRepo.uploadCaseVideo(parts);
    }

    public static RequestParams createVideoUploadRequestParams(String videoUri , String thumbnailUri)
    {
        Log.d("uploadimagelogs" , "creating video params ");
        Log.d("uploadimagelogs" , "video uri  "+videoUri);
        Log.d("uploadimagelogs" , "thumbnail uri  "+thumbnailUri);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(caseVideoUriTxt,videoUri);
        requestParams.putString(caseThumbnailUriTxt,thumbnailUri);
        return requestParams;
    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = new File(fileUri.getPath());
        Log.d("uploadimagelogs","file path=="+fileUri.toString());
        Log.d("uploadimagelogs","file path=="+fileUri.getPath());

        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        Log.d("uploadimagelogs","file type=="+mimeType);

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        Log.d("uploadimagelogs","request filed");

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }




}
