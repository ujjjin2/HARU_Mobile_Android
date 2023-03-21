package com.object.haru.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.object.haru.Adapter.AppliedAdapter;
import com.object.haru.Adapter.ApplyAdapter;
import com.object.haru.Adapter.WritedAdapter;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyActivity extends AppCompatActivity {
//지원 했던 글 목록

    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    private ArrayList<RecruitDTO> arrayList;
    private AppliedAdapter appliedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        recyclerView = findViewById(R.id.recyclerView_apply);
        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Information 객체를 담을 ArraryList

        fetch();

    }

    private void fetch() {

        Call<List<RecruitDTO>> call = RetrofitClientInstance.getApiService().writed_list("",null);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    appliedAdapter = new AppliedAdapter(arrayList);
                    recyclerView.setAdapter(appliedAdapter);
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