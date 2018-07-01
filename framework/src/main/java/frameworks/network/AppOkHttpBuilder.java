package frameworks.network;


import java.util.concurrent.TimeUnit;

import frameworks.network.interceptor.AppBaseInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public class AppOkHttpBuilder {

    private OkHttpClient.Builder builder;

    public AppOkHttpBuilder(OkHttpClient.Builder builder) {
        this.builder = builder;
    }

    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

    public AppOkHttpBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public AppOkHttpBuilder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public AppOkHttpBuilder addDebugInterceptor() {
//        if (GlobalConfig.isAllowDebuggingTools()) {
//            LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
//            Boolean allowLogOnNotification = cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
//            this.addInterceptor(new ChuckInterceptor(MainApplication.getAppContext())
//                    .showNotification(allowLogOnNotification));
//            this.addInterceptor(new DebugInterceptor());
//        }

        return this;
    }

    public AppOkHttpBuilder setOkHttpRetryPolicy(OkHttpRetryPolicy retryPolicy) {
        builder.readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS);
        builder.connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
        for (Interceptor interceptor : builder.interceptors()) {
            if (interceptor instanceof AppBaseInterceptor) {
                ((AppBaseInterceptor) interceptor).setMaxRetryAttempt(retryPolicy.maxRetryAttempt);
            }
        }

        return this;
    }

    public OkHttpClient build() {
        return builder.build();
    }
}
