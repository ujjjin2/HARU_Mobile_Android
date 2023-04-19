package com.object.haru.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.object.haru.Chat.UserAccountDTO;
import com.object.haru.DTO.FCMDTO;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.TestDTO;
import com.object.haru.DTO.UserDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private View loginbutton;
    Call<KakaoDTO> call;
    private Long kakaoId,kakaoId2;
    private Button loginbtn;
    private String FcmToken,token2;
    private String email;

    private String name;




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

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        token2 = auto.getString("token", null);
        kakaoId2 = auto.getLong("kakaoId", 0);

// 카카오 로그인
        loginbutton = findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (kakaoId2 != 0 && token2 !=null){
                    kakaoId = kakaoId2;
                    getFirebase();
                    Log.d("[onCreate 에서]",kakaoId.toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("token", token2);
                    intent.putExtra("kakaoId",kakaoId2);
                    startActivity(intent);

                }else{//loginInfo가 비어있을때 로그인 창으로 넘어감
                    if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                        login();

                    }
                    else{
                        accountLogin();
                    }
                }
            }
        });

        //테스트 로그인
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

                        Long testid = 123456789L;
                        email = "testID@test.com";     //임시용 이메일 나중에 이메일 받아와야됌


                        // 파이어베이스 인증 및 로그인
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<String> signInMethods = task.getResult().getSignInMethods();
                                if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                    // 이미 회원가입한 사용자인 경우 로그인
                                    mAuth.signInWithEmailAndPassword(email, testid.toString())
                                            .addOnCompleteListener(LoginActivity.this, signInTask -> {
                                                if (signInTask.isSuccessful()) {
                                                    // 로그인 성공
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                } else {
                                                    // 로그인 실패
                                                    Log.w(TAG, "signInWithEmail:failure", signInTask.getException());
                                                }
                                            });
                                } else {
                                    // 이메일 주소가 존재하지 않는 경우 회원가입 후 로그인
                                    mAuth.createUserWithEmailAndPassword(email, testid.toString())
                                            .addOnCompleteListener(LoginActivity.this, createTask -> {
                                                if (createTask.isSuccessful()) {
                                                    // 회원가입 및 로그인 성공
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    if (user != null) {
                                                        Log.w("테스트 user 추가", "진행");
                                                        String email = user.getEmail();
                                                        String uid = user.getUid();
                                                        // UserAccountDTO 객체 생성 및 초기화
                                                        UserAccountDTO userAccountDTO = new UserAccountDTO(uid, email, "testID");
                                                        // 데이터베이스에 저장
                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
                                                        databaseReference.child(testid.toString()).setValue(userAccountDTO);
                                                    }else{
                                                        Log.w("테스트 user이 null임", "널");
                                                    }

                                                } else {
                                                    // 회원가입 실패
                                                    Log.w(TAG, "Firebase createUser:failure", createTask.getException());
                                                }
                                            });
                                }
                            } else {
                                // 이메일 주소 확인 실패
                                Log.w(TAG, "Firebase Email:failure", task.getException());
                            }
                        });

                        FCMDTO fcmdto = new FCMDTO(FcmToken,123456789L);
                        Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(Test_token,fcmdto);
                        fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                            @Override
                            public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                Log.d("[FCM-설정]","======성공=======");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("token", Test_token);
                                intent.putExtra("kakaoId",123456789L);
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
                            //파이어베이스 인증 및 로그인



                            FCMDTO fcmdto = new FCMDTO(FcmToken,kakaoId);
                            Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(),fcmdto);
                            fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                @Override
                                public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                    Log.d("[FCM-설정]","======성공=======");
                                    Log.d("[login 에서]",kakaoId.toString());
                                    getFirebase();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("kakaoId", kakaoId.toString());
                                    intent.putExtra("token", kakao.getacccesstoken());

                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLoginEdit = auto.edit();
                                    autoLoginEdit.putLong("kakaoId", kakaoId);
                                    autoLoginEdit.putString("token", kakao.getacccesstoken());
                                    autoLoginEdit.commit();


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

                            //파이어베이스 인증 및 로그인


                            FCMDTO fcmdto = new FCMDTO(FcmToken,kakaoId);
                            Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(),fcmdto);
                            fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                @Override
                                public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                    Log.d("[FCM-설정]","======성공=======");
                                    Log.d("[accountlogin 에서]",kakaoId.toString());
                                    getFirebase();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("kakaoId", kakaoId);
                                    intent.putExtra("token", kakao.getacccesstoken());
                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLoginEdit = auto.edit();
                                    autoLoginEdit.putLong("kakaoId", kakaoId);
                                    autoLoginEdit.putString("token", kakao.getacccesstoken());
                                    autoLoginEdit.commit();

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
                    email = user.getKakaoAccount().getEmail();
                    name = user.getKakaoAccount().getProfile().getNickname();
                    Log.d("getUserInfo name", "getUserInf 실행 ");

                }
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

    public void getFirebase(){

        if(kakaoId==null){
            email = "nullPoint@test.com";  //시점에 따라서 kakaoid를 못 받는경우도 있음
            Log.d("[최종]", "if 카카오아이디 넗");
        }else{
            email = ""+kakaoId.toString()+"@test.com";     //임시용 이메일 나중에 이메일 받아와야됌
        }
        if(email == null){
            email = ""+kakaoId.toString()+"@test.com";
        }

        Log.d("[최종 email]", email);
        Log.d("[최종 kakaoId]", kakaoId.toString());

        // 파이어베이스 인증 및 로그인
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                    // 이미 회원가입한 사용자인 경우 로그인
                    mAuth.signInWithEmailAndPassword(email, kakaoId.toString())
                            .addOnCompleteListener(LoginActivity.this, signInTask -> {
                                if (signInTask.isSuccessful()) {
                                    // 로그인 성공
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    // 로그인 실패
                                    Log.w(TAG, "signInWithEmail:failure", signInTask.getException());
                                }
                            });
                } else {
                        // 이메일 주소가 존재하지 않는 경우 회원가입 후 로그인
                    mAuth.createUserWithEmailAndPassword(email, kakaoId.toString())
                            .addOnCompleteListener(LoginActivity.this, createTask -> {
                                if (createTask.isSuccessful()) {
                                    // 회원가입 및 로그인 성공
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null) {
                                        String email = user.getEmail();
                                        String idToken = user.getUid();
                                        // UserAccountDTO 객체 생성 및 초기화
                                        UserAccountDTO userAccountDTO = new UserAccountDTO(idToken, email, name);
                                        // 데이터베이스에 저장
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
                                        databaseReference.child(kakaoId.toString()).setValue(userAccountDTO);
                                    }

                                } else {
                                    // 회원가입 실패
                                    Log.w(TAG, "createUserWithEmail:failure", createTask.getException());
                                }
                            });
                }
            } else {
                // 이메일 주소 확인 실패
                Log.w(TAG, "fetchSignInMethodsForEmail:failure", task.getException());
            }
        });
    }

}