package frameworks.di.module.net;

import android.content.Context;


import dagger.Module;
import dagger.Provides;
import frameworks.GlobalConfig;
import frameworks.appsession.SessionValue;
import frameworks.di.qualifier.ApplicationContext;
import frameworks.di.scope.ApplicationScope;
import frameworks.network.interceptor.AppAuthInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ApplicationScope
    @Provides
    AppAuthInterceptor provideAuthInterceptor(@ApplicationContext Context context,
                                                  SessionValue sessionValue){
        return new AppAuthInterceptor(context, sessionValue);
    }


}
