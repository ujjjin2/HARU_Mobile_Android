package com.object.haru;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;


public class kakaoApplication extends Application {

//    @Override
//    public void OnCreate(){
//        super.onCreate();
//        KakaoSDK.init(this,"45f224d0e6f48c29c62dd5e9b685de3b");
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Kakao SDK 초기화
        //local.properties랑 BuildConfig 파일 안에 네이티브 키 있음
        KakaoSdk.init(this,BuildConfig.KAKAO_NATIVE_APP_KEY);
    }
}
