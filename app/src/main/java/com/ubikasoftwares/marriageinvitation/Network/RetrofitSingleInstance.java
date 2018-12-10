package com.ubikasoftwares.marriageinvitation.Network;

import com.ubikasoftwares.marriageinvitation.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleInstance{

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://www.ubikasoftwares.com/";



    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            if(BuildConfig.DEBUG){
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(loggingInterceptor);
            }
            OkHttpClient shortHttpClient = okHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            retrofit = new Retrofit.Builder().
                    baseUrl(BASE_URL)
                    .client(shortHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
