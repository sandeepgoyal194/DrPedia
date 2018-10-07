package com.softmine.drpedia.home.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.softmine.drpedia.exception.NetworkConnectionException;
import com.softmine.drpedia.home.di.CaseStudyComponent;
import com.softmine.drpedia.home.di.DaggerCaseStudyComponent;
import com.softmine.drpedia.home.di.GetCaseStudyListModule;
import com.softmine.drpedia.home.domain.usecases.UploadCaseDetailUseCase;
import com.softmine.drpedia.home.notification.UploadNotificationConfig;
import com.softmine.drpedia.home.notification.UploadNotificationStatusConfig;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import frameworks.AppBaseApplication;
import frameworks.network.model.ResponseException;
import frameworks.network.usecases.RequestParams;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

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
                NotificationChannel channel = new NotificationChannel(notificationChannelId, "Upload Service channel", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
            }
        }

        createNotification();
        executeRequest(params);
        return super.onStartCommand(intent, flags, startId);
    }

    private void executeRequest(UploadTaskParameters params)
    {
        final UploadNotificationConfig notificationConfig = params.notificationConfig;
        RequestParams requestParams =   UploadCaseDetailUseCase.createRequestParams(params.caseCategory,params.caseTitle,params.caseDesc,params.attachmentList);
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
        });
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
                .setOngoing(false);
        setRingtone(notification);

        notificationManager.notify(notificationId + 1, notification.build());
        if(isExecuteInForeground())
            stopForeground(true);

        stopSelf();
    }


}
