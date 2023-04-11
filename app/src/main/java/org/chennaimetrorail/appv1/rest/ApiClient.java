package org.chennaimetrorail.appv1.rest;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {

    // public static final String BASE_URL = "https://api.chennaimetrorail.org/";//not use
    // public static final String BASE_URL = "https://tapi.chennaimetrorail.org/"; //liVE1
    // public static final String BASE_URL = "https://apidev.chennaimetrorail.org/"; //dev
    // public static final String BASE_URL = "http://itechdemo.in/cmrlservice/"; //Demo
    // public static final String BASE_URL = "https://apidev2.chennaimetrorail.org/v2/"; //Demo
    // public static final String BASE_URL = " https://apiprod.chennaimetrorail.org/v3/" ;//Live old
    // public static final String BASE_URL = "https://apin.chennaimetrorail.org/v4/ ";//Test server

    // public static final String BASE_URL = " https://apiprod.chennaimetrorail.org/v4/" ;//Live

    public static final String BASE_URL = "https://apidev.chennaimetrorail.org/v4/";//Api dev server

    private static Retrofit retrofit = null;
    public static Retrofit getResponse() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
 }
