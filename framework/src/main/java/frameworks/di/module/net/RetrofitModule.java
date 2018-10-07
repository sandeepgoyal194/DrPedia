package frameworks.di.module.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import frameworks.di.scope.ApplicationScope;
import frameworks.network.interceptor.ErrorResponseInterceptor;
import frameworks.network.model.BaseResponseError;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    private static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @ApplicationScope
    @Provides
    public Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
  //.addConverterFactory(GsonConverterFactory.create(gson))

   /* @ApplicationScope
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }*/




}