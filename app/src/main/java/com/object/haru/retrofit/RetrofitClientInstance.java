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

/*
* 서버 주소 연결 
* */
public class RetrofitClientInstance {
    //private static final String BASE_URL = "http://10.0.2.2:8081/";
   //   private static final String BASE_URL = "http://192.168.0.6:8080/"; //상훈집
     // private static final String BASE_URL = "http://10.200.72.128:8080/"; //7호관 와이파이


    private static final String BASE_URL = "http://114.71.137.141:8080/"; // 로컬에서 개인 폰 돌릴 때 개인 ip4v 주소써야됌
    // 10.0.2.2


    public static RetrofitService getApiService(){return getInstance().create(RetrofitService.class);}

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new NullOnEmptuConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}
