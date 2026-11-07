package com.example.showpointrma.api;

import android.content.Context;

import com.example.showpointrma.util.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String BASE_URL = "http://10.0.2.2:8080/ShowPoint/"; //ZA EMULATOR
    private static final String BASE_URL = "http://10.168.198.252:8080/ShowPoint/"; // ZA FIZ. UREDJAJ

    private static Retrofit retrofit;
    private static ApiService api;

    public static synchronized Retrofit get(Context context) {
        if (retrofit == null) {
            final TokenManager tokenManager = new TokenManager(context);

            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BODY); // samo za razvoj

            OkHttpClient client = new OkHttpClient.Builder()
                    // 1: dodajemo JWT na svaki zahtev ako je korisnik ulogvan
                    .addInterceptor(chain -> {
                        Request.Builder builder = chain.request().newBuilder();
                        String token = tokenManager.getToken();
                        if (token != null)
                            builder.header("Authorization", "Bearer " + token);

                        return chain.proceed(builder.build());
                    })
                    // 2: logovanje ide nakon, pa u LOgcatu vidimo i authorization header
                    .addInterceptor(log)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    // precica: RetrofitClient.getApi(this).getSerije()...
    public static synchronized ApiService getApi(Context context) {
        if (api == null) {
            api = get(context).create(ApiService.class);
        }
        return api;
    }

}
