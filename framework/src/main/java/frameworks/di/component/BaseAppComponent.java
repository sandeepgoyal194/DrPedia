package frameworks.di.component;

import android.content.Context;

import com.google.gson.Gson;

import dagger.Component;
import frameworks.appsession.AppSessionManager;
import frameworks.appsession.SessionValue;
import frameworks.di.module.AppModule;
import frameworks.di.qualifier.ApplicationContext;
import frameworks.di.scope.ApplicationScope;
import frameworks.network.interceptor.AppAuthInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = {
        AppModule.class
})
public interface BaseAppComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder retrofitBuilder();

//    Gson gson();

    SessionValue userSession();


    AppAuthInterceptor tkpdAuthInterceptor();


    AppSessionManager getSessionManager();


    HttpLoggingInterceptor httpLoggingInterceptor();


}
