package com.example.loginwithretrofit.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    /********
     * URLS
     *******/
    private static final String ROOT_URL = "http://192.168.1.109/uconnect/";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiInterface getApiService() {
        return getRetrofitInstance().create(ApiInterface.class);
    }
}
