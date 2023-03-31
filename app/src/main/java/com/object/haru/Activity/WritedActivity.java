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
import com.object.haru.Adapter.WritedAdapter;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.InMemoryDexClassLoader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RecruitDTO> arrayList;
    private WritedAdapter writedAdapter;

    private ImageView backButton;

    private String token;
    private Long kakaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writed);

        //마이페이지 슬라이드에서 받아옴
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);

        recyclerView = findViewById(R.id.recyclerView_writed);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetch();

        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Information 객체를 담을 ArraryList

    }

    private void fetch() {

        Call<List<RecruitDTO>> call = RetrofitClientInstance.getApiService().writed_list(token,kakaoId);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    writedAdapter = new WritedAdapter(arrayList, token, kakaoId);
                    recyclerView.setAdapter(writedAdapter);
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