package frameworks;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import frameworks.di.component.BaseAppComponent;
import frameworks.di.component.DaggerBaseAppComponent;
import frameworks.di.module.AppModule;

/**
 * Created by Sandeep on 21/01/2017.
 */

public class AppBaseApplication extends Application {
    private static AppBaseApplication mApplication;


    public static String TAG = AppBaseApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        printHashKey();
        this.initializeInjector();
        JodaTimeAndroid.init(this);
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
    }

    private BaseAppComponent baseAppComponent;

    private void initializeInjector() {
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
    }

    public BaseAppComponent getBaseAppComponent(){

        return baseAppComponent;
    }
    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sachin.doctorsguide",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("sachin:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static AppBaseApplication getApplication() {
        return mApplication;
    }


}
