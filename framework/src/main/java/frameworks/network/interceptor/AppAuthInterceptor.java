package frameworks.network.interceptor;

import android.content.Context;
import android.support.v4.util.ArrayMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import frameworks.appsession.SessionValue;
import frameworks.di.qualifier.ApplicationContext;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author Angga.Prasetiyo on 27/11/2015.
 */
public class AppAuthInterceptor extends AppBaseInterceptor {
    private static final int ERROR_FORBIDDEN_REQUEST = 403;
    private static final String ACTION_TIMEZONE_ERROR = "com.tokopedia.tkpd.TIMEZONE_ERROR";

    private static final String REQUEST_METHOD_GET = "GET";
    private static final String REQUEST_METHOD_POST = "POST";
    private static final String REQUEST_METHOD_PATCH = "PATCH";
    private static final String REQUEST_METHOD_DELETE = "DELETE";

    private static final String RESPONSE_STATUS_OK = "OK";
    private static final String RESPONSE_STATUS_FORBIDDEN = "FORBIDDEN";
    private static final String RESPONSE_STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    private static final String RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String RESPONSE_STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String RESPONSE_STATUS_INVALID_GRANT = "INVALID_GRANT";
    private static final String HEADER_PARAM_AUTHORIZATION = "authorization";
    private static final String HEADER_PARAM_BEARER = "Bearer";
    private static final String RESPONSE_PARAM_MAKE_LOGIN = "make_login";
    private static final String RESPONSE_PARAM_STATUS = "status";
    private static final String RESPONSE_PARAM_MESSAGE_ERROR = "message_error";

    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "GIS_TOKEN";
    private static final String TOKEN = "token";
    private static final String HEADER_ACCOUNTS_AUTHORIZATION = "accounts-authorization";
    private static final String HEADER_PARAM_IS_BETA = "is_beta";
    private static final String PARAM_DEFAULT_BETA = "0";
    private static final String PARAM_BETA_TRUE = "1";
    private static final String RESPONSE_PARAM_TOKEN = "token";
    private static final String REQUEST_PARAM_REFRESH_TOKEN = "refresh_token";
    private Context context;
    protected SessionValue userSession;
    protected String authKey;

    public AppAuthInterceptor(@ApplicationContext Context context,
                              SessionValue userSession) {
        this.context = context;
        this.userSession = userSession;
        this.authKey = userSession.getApi_key();
    }


    private Lock lock = new ReentrantLock();

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        checkForceLogout(chain, response, finalRequest);

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    protected void checkResponse(String string, Response response) {
        String bodyResponse = string;
         if(isForbiddenRequest(bodyResponse, response.code())) {
        }
    }



    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        /* this can override for throw error */
    }


    protected boolean isForbiddenRequest(String bodyResponse, int code) {

        JSONObject json;
        try {
            json = new JSONObject(bodyResponse);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_FORBIDDEN) && code == ERROR_FORBIDDEN_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, originRequest);
        generateHeader(authHeaders, originRequest, newRequest);
    }

    Map<String, String> prepareHeader(Map<String, String> authHeaders, Request originRequest) {

        String contentTypeHeader = null;
        if (!REQUEST_METHOD_GET.equals(originRequest.method())
                && originRequest.body() != null
                && originRequest.body().contentType() != null) {
            contentTypeHeader = originRequest.body().contentType().toString();
        }
        if (REQUEST_METHOD_GET.equalsIgnoreCase(originRequest.method())) {
            contentTypeHeader = "";
        }
        switch (originRequest.method()) {
            case REQUEST_METHOD_PATCH:
            case REQUEST_METHOD_DELETE:
            case REQUEST_METHOD_POST:
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateParamBodyString(originRequest),
                        originRequest.method(),
                        authKey,
                        contentTypeHeader
                );
                break;
            case REQUEST_METHOD_GET:
                authHeaders = getHeaderMap(
                        originRequest.url().uri().getPath(),
                        generateQueryString(originRequest),
                        originRequest.method(),
                        authKey,
                        contentTypeHeader
                );
                break;
        }
        return authHeaders;
    }

    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(AUTHORIZATION,authKey);
        return headerMap;
    }

    private void generateHeader(Map<String, String> authHeaders, Request originRequest, Request.Builder newRequest) {
        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            newRequest.addHeader(entry.getKey(), entry.getValue());
        }
        newRequest.method(originRequest.method(), originRequest.body());
    }

    private String generateParamBodyString(final Request request) {
        String paramBody = "";
        try {
            if (request.body() != null) {
                final Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                paramBody = buffer.readUtf8();
            }
        } catch (IOException e) {
            paramBody = "";
        }
        return paramBody;
    }

    private String generateQueryString(final Request request) {
        String query = request.url().query();
        return query != null ? query : "";
    }

    protected boolean isMaintenance(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_UNDER_MAINTENANCE);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isHasErrorMessage(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            JSONArray errorMessage = json.optJSONArray(RESPONSE_PARAM_MESSAGE_ERROR);
            return errorMessage.length() > 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isRequestDenied(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            return status.equals(RESPONSE_STATUS_REQUEST_DENIED);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unused")
    private boolean isInvalidRequest(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            String status = json.optString(RESPONSE_PARAM_STATUS, RESPONSE_STATUS_OK);
            if (status.equals(RESPONSE_STATUS_INVALID_REQUEST)) return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    protected boolean isServerError(int code) {
        return code >= 500;
    }

    protected Response createNewResponse(Response oldResponse, String oldBodyResponse) {
        ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), oldBodyResponse);

        Response.Builder builder = new Response.Builder();
        builder.body(body)
                .headers(oldResponse.headers())
                .message(oldResponse.message())
                .handshake(oldResponse.handshake())
                .protocol(oldResponse.protocol())
                .cacheResponse(oldResponse.cacheResponse())
                .priorResponse(oldResponse.priorResponse())
                .code(isServerError(oldResponse.code())
                        && isHasErrorMessage(oldBodyResponse) ? 200 : oldResponse.code())
                .request(oldResponse.request())
                .networkResponse(oldResponse.networkResponse());

        return builder.build();
    }




    protected Boolean isNeedGcmUpdate(Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains(RESPONSE_STATUS_REQUEST_DENIED) &&
                    !response.request().url().encodedPath().contains(RESPONSE_PARAM_MAKE_LOGIN);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean isUnauthorized(Request request, Response response) {
        try {
            //using peekBody instead of body in order to avoid consume response object, peekBody will automatically return new reponse
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains(RESPONSE_STATUS_INVALID_REQUEST)
                    && request.header(HEADER_PARAM_AUTHORIZATION).contains(HEADER_PARAM_BEARER);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isInvalidGrantWhenRefreshToken(Request request, Response response) {
        try {
            String responseString = response.peekBody(512).string();
            return responseString.toUpperCase().contains(RESPONSE_STATUS_INVALID_GRANT)
                    && response.request().url().encodedPath().contains(RESPONSE_PARAM_TOKEN)
                    && request.toString().contains(REQUEST_PARAM_REFRESH_TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    protected Response checkForceLogout(Interceptor.Chain chain, Response response, Request finalRequest) throws
            IOException {
        return response;
    }




    private Response checkShowForceLogout(Interceptor.Chain chain, Request newestRequest) throws IOException {
        Response response = chain.proceed(newestRequest);
        return response;
    }
}
