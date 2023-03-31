package com.object.haru.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// import com.kakao.auth.ISessionCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.object.haru.DTO.FCMDTO;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.TestDTO;
import com.object.haru.Fcm.MyFirebaseMessagingService;
import com.object.haru.Fragment.HomeFragment_Slide;
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
    private Long kakaoId;
    private Button loginbtn;
    private String FcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("KeyHash", getKeyHash());



        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    FcmToken = task.getResult();
                    Log.d("FCM Token", FcmToken);
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

        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick에 저장된 FCM Token", FcmToken);
                Call<TestDTO> testDTOCall = RetrofitClientInstance.getApiService().Test_Login();
                testDTOCall.enqueue(new Callback<TestDTO>() {
                    @Override
                    public void onResponse(Call<TestDTO> call, Response<TestDTO> response) {
                        TestDTO testDTO = response.body();
                        String Test_token = testDTO.getAcccesstoken();

                        Log.d("로그인 클릭시 api에서 FCM Token", FcmToken);
                        Log.d("로그인 클릭시 AccessToken", Test_token);

                        FCMDTO fcmdto = new FCMDTO(FcmToken,9999999999L);
                        Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(Test_token,fcmdto);
                        fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                            @Override
                            public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                Log.d("[FCM-설정]","======성공=======");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("token", Test_token);
                                intent.putExtra("kakaoId",9999999999L);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<FCMDTO> call, Throwable t) {
                                Log.d("FCM 실패","====================");
                                t.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<TestDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

    }
    public void login(){
        Log.d("로그인()에 저장된 FCM Token", FcmToken);
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);

            } else if (oAuthToken != null) {

                String code = oAuthToken.getAccessToken();
                call =  RetrofitClientInstance.getApiService().kakaoLogin("", code,FcmToken);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                        if (response.isSuccessful()) {
                            KakaoDTO kakao = response.body();
                            Log.d("[로그인 성공]","===============");

                            FCMDTO fcmdto = new FCMDTO(FcmToken,kakaoId);
                            Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(),fcmdto);
                            fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                @Override
                                public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                    Log.d("[FCM-설정]","======성공=======");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("kakaoId", kakaoId);
                                    intent.putExtra("token", kakao.getacccesstoken());

                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<FCMDTO> call, Throwable t) {
                                    Log.d("FCM 실패","====================");
                                    t.printStackTrace();
                                }
                            });
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

                Log.d("fcm 확인",FcmToken);
                call = RetrofitClientInstance.getApiService().kakaoLogin("", oAuthToken.getAccessToken(),FcmToken);
                call.enqueue(new Callback<KakaoDTO>() {
                                 @Override
                                 public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                                     if (response.isSuccessful()){
                                         KakaoDTO kakao = response.body();
                                         Log.d("[로그인 성공]", kakao.getacccesstoken());

                                         FCMDTO fcmdto = new FCMDTO(FcmToken,kakaoId);
                                         Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(),fcmdto);
                                         fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                             @Override
                                             public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                                 Log.d("[FCM-설정]","======성공=======");
                                                 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                 intent.putExtra("kakaoId", kakaoId);
                                                 intent.putExtra("token", kakao.getacccesstoken());

                                                 startActivity(intent);
                                             }

                                             @Override
                                             public void onFailure(Call<FCMDTO> call, Throwable t) {
                                                Log.d("FCM 실패","====================");
                                                t.printStackTrace();
                                             }
                                         });

                                     } else {
                                         Log.d("[로그인 실패]","===================");
                                         response.errorBody();
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
                    kakaoId = user.getId();
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