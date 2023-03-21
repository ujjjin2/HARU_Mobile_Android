package com.object.haru.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

// import com.kakao.auth.ISessionCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.Fcm.MyFirebaseMessagingService;
import com.object.haru.Fragment.MainFragment_rc;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private View loginbutton;
    Call<KakaoDTO> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Log.d("KeyHash", getKeyHash());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    Log.d("FCM Token", token);
                } else {
                    Log.w("FCM Token", "Fetching FCM registration token failed", task.getException());
                }
            }
        });



        loginbutton = findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    login();
                }
                else{
                    accountLogin();
                }
            }
        });

    }
    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);

            } else if (oAuthToken != null) {

                String code = oAuthToken.getAccessToken();
                call =  RetrofitClientInstance.getApiService().kakaoLogin("", code);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                        if (response.isSuccessful()) {
                            KakaoDTO kakao = response.body();
                            Log.d("[로그인 성공]","===============");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("token", kakao.getacccesstoken());

                            Intent intent1 = new Intent(LoginActivity.this, MainFragment_rc.class);
                            intent1.putExtra("token", kakao.getacccesstoken());

                            Intent intent2 = new Intent(LoginActivity.this, SearchResultActivity.class);
                            intent.putExtra("token", kakao.getacccesstoken());

                            startActivity(intent);
                        } else {
                            Log.d("[로그인 실패]","===================");
                        }

                    }

                    @Override
                    public void onFailure(Call<KakaoDTO> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
                getUserInfo();
            }
            return null;
        });
    }

    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                System.out.println("토큰"+oAuthToken.getAccessToken());

                call = RetrofitClientInstance.getApiService().kakaoLogin("", oAuthToken.getAccessToken());
                call.enqueue(new Callback<KakaoDTO>() {
                                 @Override
                                 public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                                     if (response.isSuccessful()){
                                         KakaoDTO kakao = response.body();
                                         Log.d("[로그인 성공]", kakao.getacccesstoken());
                                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                         intent.putExtra("token", kakao.getacccesstoken());

                                         Intent intent2 = new Intent(LoginActivity.this, DetailActivity.class);
                                         intent.putExtra("token", kakao.getacccesstoken());

                                         startActivity(intent);
                                     } else {
                                         Log.d("[로그인 실패]","===================");
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<KakaoDTO> call, Throwable t) {
                                     Log.d("[로그인 실패]","===================");
                                     t.printStackTrace();
                                 }
                             });

                        getUserInfo();
            }
            return null;
        });
    }

    public void getUserInfo(){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: "+user.getId() +
                            "\n이메일: "+user.getKakaoAccount().getEmail());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);
            }
            return null;
        });
    }

    // 키해시 얻기
    public String getKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if(packageInfo == null) return null;
            for(Signature signature: packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                }catch (NoSuchAlgorithmException e){
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }




}