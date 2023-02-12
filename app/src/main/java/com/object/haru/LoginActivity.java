package com.object.haru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.retrofit.RetrofitClientInstance;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private View loginbutton;
    Call<KakaoDTO> call;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

//        Log.d("KeyHash", getKeyHash());

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

                String code = "https://kauth.kakao.com/oauth/authorize?client_id=a91d97f1d08c184bf4385f5553d57b6a&redirect_uri=http://localhost:8080/kakao/oauth&response_type=code";
                call =  RetrofitClientInstance.getApiService().kakaoLogin(code);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {

                            KakaoDTO kakao = response.body();
                            Log.d("[로그인 성공]","야호~~~");

                    }

                    @Override
                    public void onFailure(Call<KakaoDTO> call, Throwable t) {

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

                String code ="https://kauth.kakao.com/oauth/authorize?client_id=a91d97f1d08c184bf4385f5553d57b6a&redirect_uri=http://localhost:8080/kakao/oauth&response_type=code";
                call =  RetrofitClientInstance.getApiService().kakaoLogin(code);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {

                            KakaoDTO kakao = response.body();

                            Log.d("[로그인 성공]","gg"+kakao);

                    }

                    @Override
                    public void onFailure(Call<KakaoDTO> call, Throwable t) {
                        Log.d("[로그인 실패]","ㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜㅜ");

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