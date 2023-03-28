package com.object.haru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Adapter.RecruitAdapter;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZzimListActivity extends AppCompatActivity {

    private ImageView backButton;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<RecruitDTO> arrayList = new ArrayList<>();
    private RecruitAdapter recruitAdapter;
    private Call<List<RecruitDTO>> call;

    private Long kakaoId;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zzim_list);

        Intent intent = getIntent();
        kakaoId = intent.getLongExtra("kakaoId", 0);
        token = intent.getStringExtra("token");

        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.zzim_recyclerview);

        getZzimlist();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recruitAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZzimListActivity.super.onBackPressed();
            }
        });
    }

    private void getZzimlist() {
        call = RetrofitClientInstance.getApiService().getZzimList(token, kakaoId);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, ZzimListActivity.this, token, kakaoId);
                    recyclerView.setAdapter(recruitAdapter);
                    recruitAdapter.notifyDataSetChanged();
                    Log.d("[입력 성공]", "=============");
                }
            }

            @Override
            public void onFailure(Call<List<RecruitDTO>> call, Throwable t) {
                Log.d("[입력 실패]", "=============");
            }
        });

    }
}
