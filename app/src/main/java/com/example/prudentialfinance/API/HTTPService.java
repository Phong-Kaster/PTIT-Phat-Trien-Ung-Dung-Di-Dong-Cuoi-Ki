package com.example.prudentialfinance.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPService {

    public static final String APP_PATH = "https://timeswriter.xyz/";
    public static final String UPLOADS_URL = APP_PATH + "api/assets/uploads";

    private static Retrofit retrofit;

    public static Retrofit getInstance()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(APP_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
