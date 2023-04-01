package com.object.haru.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyDetailActivity extends AppCompatActivity {
    private String token, sex, self, name, career, age;
    private TextView tv_name, tv_rating, tv_age, tv_career, tv_sex, tv_self;
    private Integer rating;
    private ImageButton backButton;
    private Button confirm;
    private Long rId, kakaoid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sex = intent.getStringExtra("sex");
        self = intent.getStringExtra("self");
        name = intent.getStringExtra("name");
        career = intent.getStringExtra("career");
        age = intent.getStringExtra("age");
        rating = intent.getIntExtra("rating", 0);
        rId = intent.getLongExtra("rId", 0);
        kakaoid = intent.getLongExtra("kakaoId", 0);


        Log.d("레이팅 확인", String.valueOf(rating));

        tv_name = findViewById(R.id.applyDetail_Name);
        tv_rating = findViewById(R.id.applyDetail_rating_tv);
        tv_age = findViewById(R.id.applyDetail_age2);
        tv_career = findViewById(R.id.applyDetail_career2);
        tv_sex = findViewById(R.id.applyDetail_sex2);
        tv_self = findViewById(R.id.applyDetail_myself2);
        backButton = findViewById(R.id.imageButton_left);
        confirm = findViewById(R.id.applyDetail_btn_check);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (rating == 0) {
            tv_rating.setText("0.0");
        } else {
            tv_rating.setText(rating);
        }

        // 여기서부터
        tv_name.setText(name);
        tv_age.setText(age);
        tv_career.setText(career);
        tv_self.setText(self);
        tv_sex.setText(sex);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUser();
            }
        });

    }

    private void confirmUser() {
        Call<Void> call = RetrofitClientInstance.getApiService().confirmUser(token, kakaoid, rId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


}