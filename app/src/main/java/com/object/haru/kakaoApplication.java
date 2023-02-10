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
        KakaoSdk.init(this,"45f224d0e6f48c29c62dd5e9b685de3b");
    }
}
