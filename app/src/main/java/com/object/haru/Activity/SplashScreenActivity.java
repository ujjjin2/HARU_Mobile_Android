package com.object.haru.Activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.sdk.user.UserApiClient;
import com.object.haru.DTO.FCMDTO;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private long kakaoId2;
    private String token2;
    private LoginActivity loginActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        kakaoId2 = auto.getLong("kakaoId", 0);
        token2 = auto.getString("token", null);

        if (kakaoId2 != 0 && token2 != null) {
            Log.d("메인으로감", "메인으로감");
            startMainActivity();
        } else {
            Log.d("로그인으로감", "로그인으로감");
            startLoginActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
