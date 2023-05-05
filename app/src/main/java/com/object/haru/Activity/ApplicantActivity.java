package com.object.haru.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.object.haru.Adapter.ApplyAdapter;
import com.object.haru.Adapter.RecruitAdapter;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantActivity extends AppCompatActivity {
//지원자 리스트
    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<ApplyDTO> arrayList;
    private ApplyAdapter applyAdapter;
    private String token;
    private long kakaoId;
    private Long rid;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant);

        Intent intent = getIntent();
        token = intent.getStringExtra("token"); // 자신의 토큰
        kakaoId = intent.getLongExtra("kakaoId", 0); //자신의 kakaoId
        rid = intent.getLongExtra("rid", 0);
        recyclerView = findViewById(R.id.recyclerView_applicant);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

                recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Information 객체를 담을 ArraryList

        fetch();
    }

    private void fetch() {

        Call<List<ApplyDTO>> call = RetrofitClientInstance.getApiService().apply_list(token, rid);
        call.enqueue(new Callback<List<ApplyDTO>>() {
            @Override
            public void onResponse(Call<List<ApplyDTO>> call, Response<List<ApplyDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<ApplyDTO> apply = response.body();
                    arrayList.addAll(apply);
                    applyAdapter = new ApplyAdapter(arrayList, token);
                    recyclerView.setAdapter(applyAdapter);
                    Log.d("[입력 성공]", "=============");
                }
            }

            @Override
            public void onFailure(Call<List<ApplyDTO>> call, Throwable t) {
                Log.d("[입력 실패]", "=============");
            }
        });
    }
}