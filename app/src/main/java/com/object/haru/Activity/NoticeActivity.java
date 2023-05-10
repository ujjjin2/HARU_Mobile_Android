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
import com.object.haru.Adapter.SettingAdapter;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.NoticeDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity {
    //공지사항 리스트
    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<NoticeDTO> list;
    private SettingAdapter settingAdapter;
    private String token;
    private long kakaoId;
    private Long nid;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent intent = getIntent();

        token = intent.getStringExtra("token"); // 자신의 토큰
        kakaoId = intent.getLongExtra("kakaoId", 0); //자신의 kakaoId
        nid = intent.getLongExtra("nid", 0);

        recyclerView = findViewById(R.id.recyclerView_notice);
        backButton = findViewById(R.id.notice_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>(); //Information 객체를 담을 ArraryList

        fetch();
    }

    private void fetch() {

        Call<List<NoticeDTO>> call = RetrofitClientInstance.getApiService().selectNotice(token);
        call.enqueue(new Callback<List<NoticeDTO>>() {
            @Override
            public void onResponse(Call<List<NoticeDTO>> call, Response<List<NoticeDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<NoticeDTO> notice = response.body();
                    list.addAll(notice);
                    settingAdapter = new SettingAdapter(list, token);
                    recyclerView.setAdapter(settingAdapter);
                    Log.d("[입력 성공]", "=============");
                }
            }

            @Override
            public void onFailure(Call<List<NoticeDTO>> call, Throwable t) {
                Log.d("[입력 실패]", "=============");
            }
        });
    }

}