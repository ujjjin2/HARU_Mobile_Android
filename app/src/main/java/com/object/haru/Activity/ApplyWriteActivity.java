package com.object.haru.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyWriteActivity extends AppCompatActivity {

    TextView applywrite_age2,applywrite_career2,applywrite_sex2;
    Button applywrite_btn;
    EditText applywrite_age_et,applywrite_career_et,applywrite_sex_et,applywrite_myself;
    private Long rId;
    private String token;
    private Long kakaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_write);

        Intent intent = getIntent();
        rId = intent.getLongExtra("rId",0);
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);

        Log.d("[rid확인]", String.valueOf(rId));
        Log.d("[token확인]", token);
        Log.d("[kakaoId확인]", String.valueOf(kakaoId));

        applywrite_age2 = findViewById(R.id.applywrite_age2);
        applywrite_career2 = findViewById(R.id.applywrite_career2);
        applywrite_sex2 = findViewById(R.id.applywrite_sex2);

        applywrite_age_et=findViewById(R.id.applywrite_age_et);
        applywrite_career_et=findViewById(R.id.applywrite_career_et);
        applywrite_sex_et=findViewById(R.id.applywrite_sex_et);
        applywrite_myself=findViewById(R.id.applywrite_myself);

        //툴바 생성
//        Toolbar toolbar = findViewById(R.id.ApplyWrite_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//튀로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //TextView 에 정보 가져오기
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit(token, rId) ;
        call.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                RecruitDTO recruitDTO = response.body();
                String age = recruitDTO.getRage();
                applywrite_age2.setText(age);
                String career = recruitDTO.getRcareer();
                applywrite_career2.setText(career);
                String sex = recruitDTO.getRsex();
                applywrite_sex2.setText(sex);
            }

            @Override
            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                Toast.makeText(ApplyWriteActivity.this, "지원서 - 요건들 받아오지 못함", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        applywrite_btn = findViewById(R.id.applywrite_btn);
        applywrite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applywrite_btn.setEnabled(false);

                String ageWrite = applywrite_age_et.getText().toString();
                String careerWrite = applywrite_career_et.getText().toString();
                String sexWrite = applywrite_sex_et.getText().toString();
                String myselfWrite = applywrite_myself.getText().toString();

                ApplyDTO applyDTO = new ApplyDTO(ageWrite, careerWrite, sexWrite, myselfWrite, new Long(rId), kakaoId);

                Call<ApplyDTO> call = RetrofitClientInstance.getApiService().applyWrite(token, applyDTO);
                call.enqueue(new Callback<ApplyDTO>() {
                    @Override
                    public void onResponse(Call<ApplyDTO> call, Response<ApplyDTO> response) {
                        ApplyDTO apply = response.body();
                        Log.w("[지원하기-값 넣기 성공]","===========================");
                        finish();

                        // API 호출 완료 후에 버튼 활성화
                        applywrite_btn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<ApplyDTO> call, Throwable t) {
                        Log.w("[지원하기-값 넣기 실패]","===========================");
                        t.printStackTrace();

                        // API 호출 실패 시에도 버튼 활성화
                        applywrite_btn.setEnabled(true);
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}