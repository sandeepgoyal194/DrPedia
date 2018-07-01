package frameworks.network.interceptor;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AppBaseInterceptor implements Interceptor {
    private static final String TAG = AppBaseInterceptor.class.getSimpleName();
    public static final int SERVER_ERROR_500 = 500;
    public static final int SERVER_ERROR_599 = 599;
    protected int maxRetryAttempt = 3;

    @Inject
    public AppBaseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return getResponse(chain, chain.request());
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            Response response = chain.proceed(request);
            int count = 0;
            while (response.code() >= SERVER_ERROR_500 && response.code() <= SERVER_ERROR_599
                    && count < maxRetryAttempt) {
                count++;
                response = chain.proceed(request);
            }
            return response;
        } catch (Error e) {
            throw new UnknownHostException();
        }
    }

    public int getMaxRetryAttempt() {
        return maxRetryAttempt;
    }

    public AppBaseInterceptor setMaxRetryAttempt(int maxRetryAttempt) {
        this.maxRetryAttempt = maxRetryAttempt;
        return this;
    }
}
