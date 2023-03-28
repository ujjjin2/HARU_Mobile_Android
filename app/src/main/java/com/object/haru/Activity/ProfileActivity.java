package com.object.haru.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.object.haru.DTO.UserDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView receiveReview, writeReview, nameTextView;
    private Button editButton;

    private String token;
    private Long kakaoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);


        toolbar = findViewById(R.id.profileActivity_Toolbar);
        receiveReview = findViewById(R.id.profile_receive_review);
        writeReview = findViewById(R.id.profile_write_review);
        editButton = findViewById(R.id.button);
        nameTextView = findViewById(R.id.myPageFragment_name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("프로필");

        editButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                  intent.putExtra("token", token);
                  intent.putExtra("kakaoId", kakaoId);
                  startActivity(intent);
              }
        });

        getUserInfo();

        receiveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ReceiveReviewActivity.class);
                startActivity(intent);
            }
        });

        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, WriteReviewActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getUserInfo() {
        Call<UserDTO> userDTOCall = RetrofitClientInstance.getApiService().Show_user_info(token,kakaoId);
        userDTOCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {

                UserDTO userDTO = response.body();
                nameTextView.setText(userDTO.getName());
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("이름 오류","============");
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
