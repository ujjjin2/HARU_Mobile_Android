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
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import com.object.haru.DTO.ApplyDTO;
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

// GIF add library +
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
// GIF add library -


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private View loginbutton;
    Call<KakaoDTO> call;
    private Long kakaoId, kakaoId2;
    private int checkChat;
    private Button Testloginbtn;
    private String FcmToken, token2;
    private String email;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView loading = (ImageView) findViewById(R.id.login_ing);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loading);
        Glide.with(this).load(R.drawable.load_img).into(gifImage);
        // GIF add code
        
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    FcmToken = task.getResult(); // 받은 FCM 토큰을 전역변수에 저장
                    Log.d("FCM Token", FcmToken);

                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                    kakaoId2 = auto.getLong("kakaoId", 0);
                    token2 = auto.getString("token", null);
                    SharedPreferences check = getSharedPreferences("checkChat", Activity.MODE_PRIVATE);
                    checkChat = check.getInt("chatCount", 0);

                    Log.d("[시작 에서 checkChat]", String.valueOf(checkChat));

                    // ======================================= 알림 클릭으로 시작한 경우 ========================================
                    Intent intent;
                    // 채팅 알림
                    if (getIntent().getStringExtra("chat") != null) {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", token2);
                        intent.putExtra("chat", "chat");
                        intent.putExtra("kakaoId", kakaoId2);
                        intent.putExtra("checkChat", checkChat);
                        startActivity(intent);
                        finish();
                    }else // 새로운 지원서 알림
                        if(getIntent().getStringExtra("newApply")!=null){
                        String idString = getIntent().getStringExtra("id");
                        long id = Long.parseLong(idString);
                        Call<ApplyDTO> applyDetail = RetrofitClientInstance.getApiService().getApplyDetail(token2, id);
                        applyDetail.enqueue(new Callback<ApplyDTO>() {
                            @Override
                            public void onResponse(Call<ApplyDTO> call, Response<ApplyDTO> response) {
                                Intent   intent = new Intent(LoginActivity.this, ApplyDetailActivity.class);
                                intent.putExtra("token", token2);
                                intent.putExtra("sex", response.body().getAsex());
                                intent.putExtra("self", response.body().getMyself());
                                intent.putExtra("name", response.body().getName());
                                intent.putExtra("career", response.body().getAcareer());
                                intent.putExtra("age", response.body().getAage());
                                intent.putExtra("rating", response.body().getAvgRating());
                                intent.putExtra("rId", response.body().getRid());
                                intent.putExtra("Fridkakaoid", response.body().getKakaoid()); //long 타입
                                intent.putExtra("kakaoid", kakaoId2.toString());
                                intent.putExtra("checkChat", checkChat);
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onFailure(Call<ApplyDTO> call, Throwable t) {

                            }
                        });
                        // 구인 확정 알림
                    }else if(getIntent().getStringExtra("comfirmation") != null){

                        long id = Long.parseLong(getIntent().getStringExtra("id"));
                        Intent intent2 =null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            intent2 = new Intent(LoginActivity.this, DetailActivity.class);
                        }
                        intent2.putExtra("rId", id);
                        intent2.putExtra("token", token2);
                        intent2.putExtra("kakaoId", kakaoId2);
                        intent2.putExtra("checkChat", checkChat);

                        startActivity(intent2);
                        finish();
                    }

                    // ======================================= 알림 클릭 끝========================================

                    // 자동 로그인 부분 2개의 ==를 !=로 똑같이 맞춰주면 자동 로그인 구현
                    if (kakaoId2 != 0 && token2 != null) {
                        kakaoId = kakaoId2;
                        getFirebase(new FirebaseCallback() {
                            @Override
                            public void onFirebaseCompleted() {
                                Log.d("[자동시작 에서 FcmToken]", FcmToken);
                                Log.d("[자동시작 에서 token]", token2);
                                FCMDTO fcmdto = new FCMDTO(FcmToken, kakaoId2);
                                Call<FCMDTO> fcmdtoCall = RetrofitClientInstance.getApiService().fcm_save(token2, fcmdto);
                                fcmdtoCall.enqueue(new Callback<FCMDTO>() {
                                    @Override
                                    public void onResponse(Call<FCMDTO> call, Response<FCMDTO> response) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("token", token2);
                                        intent.putExtra("FcmToken", FcmToken);
                                        intent.putExtra("kakaoId", kakaoId2);
                                        intent.putExtra("checkChat", checkChat);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<FCMDTO> call, Throwable t) {
                                        Log.d("FCM 실패", "====================");
                                        t.printStackTrace();
                                    }
                                });
                            }
                        });
                    } else {
       // ===============================   자동 로그인이 아닌 경우 ============================== //
                        loading.setVisibility(View.INVISIBLE);
                        loginbutton = findViewById(R.id.login);
                        loginbutton.setVisibility(View.VISIBLE);
                        loginbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 로그인 메소드
                                performLogin();
                                loginbutton.setVisibility(View.INVISIBLE);
                                Testloginbtn.setVisibility(View.INVISIBLE);
                                loading.setVisibility(View.VISIBLE);
                            }

                        });

// ================================== 테스트 로그인 ==============================
                        Testloginbtn = findViewById(R.id.loginbtn);
                        Testloginbtn.setVisibility(View.VISIBLE);
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
                                                intent.putExtra("checkChat", checkChat);
                                                startActivity(intent);
                                                finish();
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
 // ================================== 테스트 로그인 끝 =============================

                    }
                } else {
                    Log.w("FCM Token", "Fetching FCM registration token failed", task.getException());
                }
            }
        });
    }
    // ================================== onCreate 끝  =============================



// ==========================  메소드 부분 ========================== //

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void performLogin() {  // 메인 메소드
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
            UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
                if (error != null) {
                    Log.e("login() FCM Token", "로그인 실패", error);
                } else if (oAuthToken != null) {
                    handleLoginSuccess(oAuthToken.getAccessToken());
                }
                return null;
            });
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
                if (error != null) {
                    Log.e("accountLogin FCMToken", "로그인 실패", error);
                } else if (oAuthToken != null) {
                    handleLoginSuccess(oAuthToken.getAccessToken());
                }
                return null;
            });
        }
    }


    public void handleLoginSuccess(String accessToken) {
        getFirebase(new FirebaseCallback() {

            @Override
            public void onFirebaseCompleted() {
                // getFirebase() 메소드가 완료된 후 실행되어야 할 코드들
                Log.d("fcm 확인", FcmToken);

                call = RetrofitClientInstance.getApiService().kakaoLogin("", accessToken, FcmToken);
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
                                    Log.d("[handleLoginSuccess]", kakaoId.toString());

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("kakaoId", kakaoId);
                                    intent.putExtra("token", kakao.getacccesstoken());
                                    intent.putExtra("checkChat", checkChat);
                                    SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLoginEdit = auto.edit();
                                    autoLoginEdit.putLong("kakaoId", kakaoId);
                                    autoLoginEdit.putString("token", kakao.getacccesstoken());
                                    autoLoginEdit.commit();

                                    startActivity(intent);
                                    finish();
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
            }
        });
    }


    //-------------------------------------------키 해시 얻기 기능 ---------------------------------------------------//
    public String getKeyHash() {  // -> 이 메소드 카카오api에선 필요없나 봄. 구글 로그인엔 필요할지도? 확장을 위해 냅둘까?
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

    // 카카오 로그인 데이터 처리, 리얼타임 로직 처리
    public void getFirebase(FirebaseCallback callback) {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
                return null;
            }

            kakaoId = user.getId();
            email = user.getKakaoAccount().getEmail();
            name = user.getKakaoAccount().getProfile().getNickname();

            // 파이어베이스 인증 및 로그인
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<String> signInMethods = task.getResult().getSignInMethods();
                    if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                        // 기존 유저 - 로그인 시도
                        signInExistingUser(kakaoId, email, callback, mAuth);
                    } else {
                        // 신규 유저 - 회원가입 후 로그인
                        signUpAndSignInNewUser(kakaoId, email, name, callback, mAuth);
                    }
                }
            });
            return null;
        });
    }

    // [리얼타임 로직(1)] : 기존 파베 인증자 로그인
    private void signInExistingUser(Long kakaoId, String email, FirebaseCallback callback, FirebaseAuth mAuth ) {
        mAuth.signInWithEmailAndPassword(email, kakaoId.toString())
                .addOnCompleteListener(LoginActivity.this, signInTask -> {
                    if (signInTask.isSuccessful()) {
                        // 로그인 성공
                        FirebaseUser fuser = mAuth.getCurrentUser();
                        checkRealtimeDatabase(fuser.getUid(), kakaoId, callback, fuser); //기존 유저가 리얼타임에 유저가 있는지 확인
                    } else {
                        // 로그인 실패
                        Log.w(TAG, "signInWithEmail:failure", signInTask.getException());
                    }
                });
    }

    // [리얼타임 로직(2)] : 신규 파베 인증자 로그인
    private void signUpAndSignInNewUser(Long kakaoId, String email, String name, FirebaseCallback callback, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, kakaoId.toString())
                .addOnCompleteListener(LoginActivity.this, createTask -> {
                    if (createTask.isSuccessful()) {
                        // 회원가입 성공
                        FirebaseUser fuser = mAuth.getCurrentUser();
                        if (fuser != null) {
                            addToRealtimeDatabase(fuser.getUid(), email, name, kakaoId, callback); // 신규 유저 리얼타임에 추가
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmailAndPassword:failure", createTask.getException());
                    }
                });
    }

    // [리얼타임 로직(3)] : 유저 데이터 체크
    private void checkRealtimeDatabase(String userId, Long kakaoId, FirebaseCallback callback, FirebaseUser fuser ) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userAccount");
        ref.child(userId).child(kakaoId.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "기존 유저 - 리얼타임 데이터 존재");
                    // 해당 유저 정보가 리얼타임 데이터베이스에 존재하는 경우 처리
                } else {
                    Log.d(TAG, "기존 유저 - 리얼타임 데이터 미존재");
                    // 해당 유저 정보가 리얼타임 데이터베이스에 없는 경우 처리
                    addToRealtimeDatabase(userId, fuser.getEmail(), name, kakaoId, callback); // 기존 유저가 리얼타임에 값이 없으면 추가
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스 에러 발생 시 처리하는 코드
            }
        });
    }

    // [리얼타임 로직(4)] : 유저 데이터 추가
    private void addToRealtimeDatabase(String userId, String email, String name, Long kakaoId, FirebaseCallback callback) {
        UserAccountDTO userAccountDTO = new UserAccountDTO(userId, email, name, kakaoId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
        databaseReference.child(kakaoId.toString()).setValue(userAccountDTO)
                .addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        Log.d(TAG, "리얼타임 데이터베이스에 데이터 추가 완료");
                        callback.onFirebaseCompleted();
                    } else {
                        Log.w(TAG, "리얼타임 데이터베이스에 데이터 추가 실패", databaseTask.getException());
                    }
                });
    }


    // Firebase 작업이 완료되었을 때 호출되는 콜백 인터페이스
    private interface FirebaseCallback {
        void onFirebaseCompleted();
    }

    //-------------------------------------------kakaoid 가져오기, 파베 인증 및 로그인 기능 끝 ---------------------------------------------------//

}





