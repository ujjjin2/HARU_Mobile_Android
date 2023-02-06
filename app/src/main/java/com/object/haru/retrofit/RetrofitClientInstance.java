package com.object.haru.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class RetrofitClientInstance {
//    private static Retrofit retrofit;
//    private static final String url = "http://114.71.137.141:8080/";
//
//    public static Retrofit getRetrofitInstance(){
//        Gson gson = new GsonBuilder().setLenient().create();
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(url)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build();
//        }
//
//        return retrofit;
//    }
//
//}

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
* 서버 주소 연결 
* */
public class RetrofitClientInstance {
    private static final String BASE_URL = "http://114.71.137.141:8080";


    public static RetroService getApiService(){return getInstance().create(RetroService.class);}

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
