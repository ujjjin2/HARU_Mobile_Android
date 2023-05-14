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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import com.object.haru.Chat.ChatActivity;
import com.object.haru.Chat.ChatListFragment;
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
    private Long kakaoId, kakaoId2;
    private Button Testloginbtn;
    private String FcmToken, token2;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    FcmToken = task.getResult(); // 받은 FCM 토큰을 전역변수에 저장
                    Log.d("FCM Token", FcmToken);

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    token2 = auto.getString("token", null);
                    kakaoId2 = auto.getLong("kakaoId", 0);

                    Intent intent;
                    if (getIntent().getStringExtra("chat") != null) {
                        Log.d("[메세지 알림 실행] ", getIntent().getStringExtra("chat"));
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", token2);
                        intent.putExtra("chat", "chat");
                        intent.putExtra("kakaoId", kakaoId2);
                        startActivity(intent);
                    }


                    if (kakaoId2 != 0 && token2 == null) {  // [필독★]- 테스트 할 때 auto 로그인 풀려고 해논거! (정상 가동 -> ==를 !=로 수정)
                        kakaoId = kakaoId2;
                        getFirebase();
                        Log.d("[자동시작 에서 FcmToken ]", FcmToken);
                        Log.d("[자동시작 에서 token ]", token2);
                        FCMDTO fcmdto = new FCMDTO(FcmToken, kakaoId2);
                        Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(token2, fcmdto);
                        fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                            @Override
                            public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("token", token2);
                                intent.putExtra("FcmToken", FcmToken);
                                intent.putExtra("kakaoId", kakaoId2);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<FCMDTO> call, Throwable t) {
                                Log.d("FCM 실패", "====================");
                                t.printStackTrace();
                            }
                        });
                    } else {  // ================   자동 로그인이 아닌 경우
                        loginbutton = findViewById(R.id.login);
                        loginbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //loginInfo가 비어있을때 로그인 창으로 넘어감
                                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                                    login();
                                } else {
                                    accountLogin();
                                }
                            }

                        });

                        // ================ 테스트 로그인
                        Testloginbtn = findViewById(R.id.loginbtn);
                        Testloginbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("테스트onClick FCM Token", FcmToken);
                                Call<TestDTO> testDTOCall = RetrofitClientInstance.getApiService().Test_Login();
                                testDTOCall.enqueue(new Callback<TestDTO>() {
                                    @Override
                                    public void onResponse(Call<TestDTO> call, Response<TestDTO> response) {
                                        TestDTO testDTO = response.body();
                                        String Test_token = testDTO.getAcccesstoken();

                                        Log.d("테스트에서 FCM Token", FcmToken);
                                        Log.d("테스트 AccessToken", Test_token);

                                        Long testid = 123456789L;
                                        String email = "TestID@test.com";     //임시용 이메일 나중에 이메일 받아와야됌


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
                                                                        String uid = user.getUid();
                                                                        // UserAccountDTO 객체 생성 및 초기화
                                                                        UserAccountDTO userAccountDTO = new UserAccountDTO(uid, email, "TestID",123456789L);
                                                                        // 데이터베이스에 저장
                                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
                                                                        databaseReference.child(testid.toString()).setValue(userAccountDTO);
                                                                    } else {
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

                                        FCMDTO fcmdto = new FCMDTO(FcmToken, 123456789L);
                                        Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(Test_token, fcmdto);
                                        fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                            @Override
                                            public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                                Log.d("[FCM-설정]", "======성공=======");
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("token", Test_token);
                                                intent.putExtra("kakaoId", 123456789L);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(Call<FCMDTO> call, Throwable t) {
                                                Log.d("FCM 실패", "====================");
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
                } else {
                    Log.w("FCM Token", "Fetching FCM registration token failed", task.getException());
                }
            }
        });
    }


// =============  카카오 로그인 메소드 부분


    //-------------------------------------------로그인 기능 ---------------------------------------------------//
    public void login() {
        Log.d("login() FCM Token", FcmToken);
        getFirebase();
        String TAG = "login()";
        getFirebase();
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                String code = oAuthToken.getAccessToken();
                call = RetrofitClientInstance.getApiService().kakaoLogin("", code, FcmToken);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                        if (response.isSuccessful()) {
                            KakaoDTO kakao = response.body();
                            Log.d("[로그인 성공]", "===============");
                            FCMDTO fcmdto = new FCMDTO(FcmToken, kakaoId);
                            Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(), fcmdto);
                            fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                @Override
                                public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                    Log.d("[FCM-설정]", "======성공=======");
                                    //파이어베이스 인증 및 로그인
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
                                    Log.d("FCM 실패", "====================");
                                    t.printStackTrace();
                                }
                            });
                        } else {
                            Log.d("[로그인 실패]", "===================");
                        }
                    }

                    @Override
                    public void onFailure(Call<KakaoDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                //  getUserInfo();
            }
            return null;
        });
    }

    //-------------------------------------------account 로그인 기능 ---------------------------------------------------//
    public void accountLogin() {
        Log.d("accountLogin FCMToken", FcmToken);
        //파이어베이스 인증 및 로그인
        getFirebase();
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                System.out.println("토큰" + oAuthToken.getAccessToken());
                Log.d("fcm 확인", FcmToken);
                call = RetrofitClientInstance.getApiService().kakaoLogin("", oAuthToken.getAccessToken(), FcmToken);
                call.enqueue(new Callback<KakaoDTO>() {
                    @Override
                    public void onResponse(Call<KakaoDTO> call, Response<KakaoDTO> response) {
                        if (response.isSuccessful()) {
                            KakaoDTO kakao = response.body();
                            Log.d("[로그인 성공]", kakao.getacccesstoken());

                            FCMDTO fcmdto = new FCMDTO(FcmToken, kakaoId);
                            Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(kakao.getacccesstoken(), fcmdto);
                            fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                @Override
                                public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                    Log.d("[FCM-설정]", "======성공=======");
                                    Log.d("[accountlogin 에서]", kakaoId.toString());


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
                                    Log.d("FCM 실패", "====================");
                                    t.printStackTrace();
                                }
                            });

                        } else {
                            Log.d("[로그인 실패]", "===================");
                            response.errorBody();
                        }
                    }

                    @Override
                    public void onFailure(Call<KakaoDTO> call, Throwable t) {
                        Log.d("[로그인 실패]", "===================");
                        t.printStackTrace();
                    }
                });
                //  getUserInfo();
            }
            return null;
        });
    }


    //-------------------------------------------키 해시 얻기 기능 ---------------------------------------------------//
    public String getKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null) return null;
            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature=" + signature, e);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }

    //-------------------------------------------kakaoid 가져오기, 파베 인증 및 로그인 기능 ---------------------------------------------------//

    public void getFirebase() {
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: " + user.getId() +
                            "\n이메일: " + user.getKakaoAccount().getEmail());
                    kakaoId = user.getId();
                    email = user.getKakaoAccount().getEmail();
                    name = user.getKakaoAccount().getProfile().getNickname();
                    Log.d("getUserInfo name", "getUserInf 실행 ");
                    Log.d(" getUserInfo name : ", name);
                    Log.d(" getUserInfo email : ", email);
                    Log.d(" getUserInfo kakaoId : ", kakaoId.toString());

                }
                if (email == null) {
                    Log.d("fire email null", "=======");
                    email = "" + kakaoId.toString() + "@test.com";
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
                                            FirebaseUser fuser = mAuth.getCurrentUser();
                                            //리얼타임에 값이 없는 경우
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userAccount");
                                            ref.child(fuser.getUid()).child(kakaoId.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Log.d("[realTime 체크 완료]","[성공]");
                                                        // kakaoId 변수에 해당 데이터의 값을 가져와 할당합니다.
                                                    } else {
                                                        // "kakaoId"가 존재하지 않는 경우
                                                        String email = fuser.getEmail();
                                                        String idToken = fuser.getUid();
                                                        UserAccountDTO userAccountDTO = new UserAccountDTO(idToken, email, name,kakaoId);
                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
                                                        databaseReference.child(kakaoId.toString()).setValue(userAccountDTO)
                                                                .addOnCompleteListener(databaseTask -> {
                                                                    if (databaseTask.isSuccessful()) { //비동기 처리
                                                                        Log.d("[realTime]", "Data successfully written to database.");
                                                                    } else {
                                                                        Log.w("[realTime]", "Error writing data to database.", databaseTask.getException());
                                                                    }
                                                                });
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // 데이터베이스 에러 발생 시 처리하는 코드
                                                }
                                            });
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
                                            FirebaseUser fuser = mAuth.getCurrentUser();
                                            if (fuser != null) {
                                                String email = fuser.getEmail();
                                                String idToken = fuser.getUid();
                                                UserAccountDTO userAccountDTO = new UserAccountDTO(idToken, email, name,kakaoId);
                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
                                                databaseReference.child(kakaoId.toString()).setValue(userAccountDTO)
                                                        .addOnCompleteListener(databaseTask -> {
                                                            if (databaseTask.isSuccessful()) { //비동기 처리
                                                                Log.d(TAG, "Data successfully written to database.");
                                                            } else {
                                                                Log.w(TAG, "Error writing data to database.", databaseTask.getException());
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.w(TAG, "createUserWithEmailAndPassword:failure", createTask.getException());
                                        }
                                    });
                        }
                    }
                });
            }
            return null;
        });
    }
}





