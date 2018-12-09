package com.softmine.drpedia.home.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.domain.usecases.UploadCaseDetailUseCase;
import com.softmine.drpedia.home.notification.UploadNotificationConfig;
import com.softmine.drpedia.home.notification.UploadNotificationStatusConfig;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import frameworks.AppBaseApplication;
import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.app.NotificationManager.*;

public class UploadService extends Service {

    private CaseStudyComponent caseStudyComponent;
    public static final String PARAM_TASK_CLASS = "taskClass";
    protected UploadTaskParameters params = null;
    private NotificationManager notificationManager;
    public static final String PARAM_TASK_PARAMETERS = "taskParameters";
    protected static final int UPLOAD_NOTIFICATION_BASE_ID = 1234; // Something unique
    private static final String ACTION_UPLOAD_SUFFIX = ".uploadservice.action.upload";
    public static String NAMESPACE = "com.softmine";
    public static boolean EXECUTE_IN_FOREGROUND = true;
    private long notificationCreationTimeMillis;
    private int notificationId=12345;
    @Inject
    UploadCaseDetailUseCase uploadCaseDetailUseCase;

    @Inject
    public UploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService","service onCreate called  ");
        this.initializeInjector();
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }

    private void initializeInjector() {
        this.caseStudyComponent = DaggerCaseStudyComponent.builder()
                .baseAppComponent(((AppBaseApplication)getApplication())
                .getBaseAppComponent())
                .getCaseStudyListModule(new GetCaseStudyListModule(this))
                .build();
        this.caseStudyComponent.inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null || !getActionUpload().equals(intent.getAction())) {
            return START_REDELIVER_INTENT;
        }

        this.params = intent.getParcelableExtra(UploadService.PARAM_TASK_PARAMETERS);
        Log.d("MyService","case title "+params.caseTitle);
        Log.d("MyService","case title "+params.caseTitle);
        Log.d("MyService","case desc "+params.caseDesc);
        Log.d("MyService","case category "+params.caseCategory);

        for(String file : params.attachmentList)
        Log.d("MyService","file path "+file);

        if(uploadCaseDetailUseCase!=null)
            Log.d("MyService","object not null ");
        else
            Log.d("MyService","object null ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && params.notificationConfig != null) {
            String notificationChannelId = params.notificationConfig.getNotificationChannelId();

            if (notificationChannelId == null) {
                params.notificationConfig.setNotificationChannelId(UploadService.NAMESPACE);
                notificationChannelId = UploadService.NAMESPACE;
            }

            if (notificationManager.getNotificationChannel(notificationChannelId) == null) {
                NotificationChannel channel = new NotificationChannel(notificationChannelId, "Upload Service channel", IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
            }
        }

        createNotification();
        executeRequest(params);
        return super.onStartCommand(intent, flags, startId);
    }

    public Observable<String> uploadPostData(UploadTaskParameters params, List<Integer> attachmentList)
    {
        Log.d("uploadimagelogs" , "get3 called  ");
        Log.d("uploadimagelogs" , "params list=== ");
        Log.d("uploadimagelogs" , "case type id=== "+params.caseCategory);
        Log.d("uploadimagelogs" , "params case title=== "+params.caseTitle);
        Log.d("uploadimagelogs" , "params case description=== "+params.caseDesc);
        Log.d("uploadimagelogs" , "params uploaded ids list=== ");
        for(Integer id : attachmentList)
        {
            Log.d("uploadimagelogs" , "id ==  "+id);
        }

        RequestParams requestParams =   UploadCaseDetailUseCase.createRequestParams(params.caseCategory,params.caseTitle,params.caseDesc, (ArrayList<Integer>) attachmentList);
        return this.uploadCaseDetailUseCase.createObservable(requestParams);
    }


    public Observable<Integer> uploadAttachments(String uri)
    {
        String fileType;
        Log.d("uploadimagelogs" , "get2 called  ");
        Log.d("uploadimagelogs" , "uri===  "+uri);

        fileType = getFileType(uri);

        if(fileType.equalsIgnoreCase("image"))
        {
            Log.d("uploadimagelogs" , "uploading image");
            RequestParams requestParams = UploadCaseDetailUseCase.createImageUploadRequestParams(uri);
           return this.uploadCaseDetailUseCase.createImageUploadObservable(requestParams);
        }
        else if (fileType.equalsIgnoreCase("video"))
        {
            Log.d("uploadimagelogs" , "uploading video");
            File thumbnailPath = null;
            Bitmap bmThumbnail = createThumbnailFromPath(uri , MediaStore.Video.Thumbnails.MINI_KIND);
            try {
                thumbnailPath = saveBitmap(bmThumbnail);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestParams requestParams = UploadCaseDetailUseCase.createVideoUploadRequestParams(uri , thumbnailPath.getAbsolutePath());
            return this.uploadCaseDetailUseCase.createVideoUploadObservable(requestParams);
        }
        return  Observable.empty();
    }


    private void executeRequest(UploadTaskParameters params)
    {
        final UploadNotificationConfig notificationConfig = params.notificationConfig;

        Observable.from(params.attachmentList)
                .flatMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String s) {
                        Observable<Integer> nu = uploadAttachments(s);
                        Log.d("uploadimagelogs","id   "+nu);
                        updateNotification(params.notificationConfig.getProgress());
                        return nu;
                    }
                })
                .toList()
                .flatMap(list -> uploadPostData(params , list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("uploadimagelogs" , "onCompleted");
                        updateNotification(notificationConfig.getCompleted());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("uploadimagelogs" , "onError");
                        e.printStackTrace();
                        updateNotification(notificationConfig.getError());
                    }

                    @Override
                    public void onNext(String strings) {

                        Log.d("uploadimagelogs" , "onNext");
                        Log.d("uploadimagelogs" , ""+strings);
                    }
                });
/*
        this.uploadCaseDetailUseCase.execute(requestParams,new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("uploadlogs","upload onCompleted");
                //UploadCasePresentor.this.caseUploadView.hideProgressBar();.
                updateNotification(notificationConfig.getCompleted());
            }

            @Override
            public void onError(Throwable e) {
              //  UploadCasePresentor.this.caseUploadView.hideProgressBar();
                e.printStackTrace();
                if(e instanceof IOException)
                {
                    if(e instanceof HttpException)
                    {
                        Log.d("bookmarkresponse","exception code  "+((HttpException)e).code());
                   //     UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof ResponseException)
                    {
                        Log.d("bookmarkresponse","ResponseException code  "+((ResponseException)e).getMessage());
                      //  UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof NetworkConnectionException)
                    {
                        Log.d("loginresponse","other issues");
                       // UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle(new NetworkConnectionException()));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                        //UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    updateNotification(notificationConfig.getError());
                }
                else
                {
                    if(e instanceof JSONException) {
                        Log.d("loginresponse", "Json Parsing exception");
                      //  UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else if(e instanceof HttpException)
                    {
                        Log.d("loginresponse", "Http exception issue");
                      //  UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    else
                    {
                        Log.d("loginresponse", "other issue");
                      //  UploadCasePresentor.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
                    }
                    updateNotification(notificationConfig.getError());
                }
            }

            @Override
            public void onNext(String commentData) {
                Log.d("uploadlogs","upload onNext=-==="+commentData);

               // UploadCasePresentor.this.caseUploadView.setUploadResult(commentData);

            }
        });*/
    }


    private boolean isExecuteInForeground() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O || EXECUTE_IN_FOREGROUND;
    }




    private void createNotification()
    {
        if (params.notificationConfig == null || params.notificationConfig.getProgress().message == null) return;

        UploadNotificationStatusConfig statusConfig = params.notificationConfig.getProgress();
        notificationCreationTimeMillis = System.currentTimeMillis();
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, params.notificationConfig.getNotificationChannelId())
                .setWhen(notificationCreationTimeMillis)
                .setContentTitle(statusConfig.title)
                .setContentText(statusConfig.message)
                .setContentIntent(statusConfig.getClickIntent(this))
                .setSmallIcon(statusConfig.iconResourceID)
                .setLargeIcon(statusConfig.largeIcon)
                .setColor(statusConfig.iconColorResourceID)
                .setGroup(UploadService.NAMESPACE)
                .setProgress(100, 0, true)
                .setOngoing(true);
        Notification builtNotification = notification.build();
        if (!isExecuteInForeground())
        {
            notificationManager.notify(notificationId, builtNotification);
        }
        else
        {
            startForeground(UPLOAD_NOTIFICATION_BASE_ID, builtNotification);
            notificationManager.cancel(notificationId);
        }
    }

    private void setRingtone(NotificationCompat.Builder notification) {

        if (params.notificationConfig.isRingToneEnabled() && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Uri sound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
            notification.setSound(sound);
        }

    }




    private void updateNotification(UploadNotificationStatusConfig statusConfig)
    {
        if (params.notificationConfig == null) return;
        notificationManager.cancel(notificationId);
       // UploadNotificationStatusConfig statusConfig = params.notificationConfig.getCompleted();
        if (statusConfig.message == null) return;
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, params.notificationConfig.getNotificationChannelId())
                .setWhen(notificationCreationTimeMillis)
                .setContentTitle(statusConfig.title)
                .setContentText(statusConfig.message)
                .setAutoCancel(statusConfig.clearOnAction)
                .setSmallIcon(statusConfig.iconResourceID)
                .setLargeIcon(statusConfig.largeIcon)
                .setColor(statusConfig.iconColorResourceID)
                .setGroup(UploadService.NAMESPACE)
                .setProgress(100, 0, true)
                .setOngoing(false);
        setRingtone(notification);

        notificationManager.notify(notificationId + 1, notification.build());
        if(isExecuteInForeground())
            stopForeground(true);

        stopSelf();
    }


    public String getFileType(String fileUri)
    {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());

        Log.d("uploadimagelogs" , "getFileType called  ");
        Log.d("uploadimagelogs" , "full type ===  "+mimeType);
        Log.d("uploadimagelogs" , "exact file type ===  "+mimeType.substring(0,mimeType.indexOf("/")));
        String fileType = mimeType.substring(0,mimeType.indexOf("/"));
        return fileType;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type){
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    public static File saveBitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        Log.d("uploadimagelogs","file AbsolutePath"+f.getAbsolutePath());
        Log.d("uploadimagelogs","file name "+f.getName());
        Log.d("uploadimagelogs","file path "+f.getPath());
        return f;
    }


}
