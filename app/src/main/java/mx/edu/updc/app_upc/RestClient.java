package mx.edu.updc.app_upc;

/**
 * Created by root on 29/11/16.
 */

import com.loopj.android.http.*;
import android.util.Log;

public class RestClient {

    private static  final String BASE_URL = "";

    private static  AsyncHttpClient client = new AsyncHttpClient();

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }



}
