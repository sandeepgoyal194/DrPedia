package frameworks.di.module.net;


import dagger.Module;
import dagger.Provides;
import frameworks.di.scope.ApplicationScope;
import frameworks.network.OkHttpRetryPolicy;
import okhttp3.OkHttpClient;

/**
 * @author ricoharisin on 3/23/17.
 */

@Module(includes = {InterceptorModule.class})
public class OkHttpClientModule {

    @ApplicationScope
    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder) {
        return okHttpClientBuilder.build();
    }

    @ApplicationScope
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

}
