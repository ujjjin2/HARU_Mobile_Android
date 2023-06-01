package com.object.haru.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private Call<List<AlarmDTO>> call;
    private List<AlarmDTO> arrayList;
    private Long kakaoId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        kakaoId = intent.getLongExtra("kakaoId", 0);
        token = intent.getStringExtra("token");

        Log.i("알림에서 kakaod",kakaoId.toString());
        Log.i("알림에서 token",token.toString());

        arrayList = new ArrayList<>(); // 초기화
        Button back_btn = findViewById(R.id.back_btn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(arrayList, this, token, kakaoId);
        recyclerView.setAdapter(alarmAdapter);
        Log.d("getAlarmlist","시작");
        getAlarmlist();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    @Override
    public void onBackPressed() {
        // 뒤로 가기 버튼이 눌렸을 때 호출됨
        super.onBackPressed();
    }


    private void getAlarmlist() {
        call = RetrofitClientInstance.getApiService().getAlarmList(token, kakaoId);
        call.enqueue(new Callback<List<AlarmDTO>>() {
            @Override
            public void onResponse(Call<List<AlarmDTO>> call, Response<List<AlarmDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Call","성공");
                    List<AlarmDTO> alarm = response.body();

                    arrayList.clear();
                    arrayList.addAll(alarm);
                    alarmAdapter = new AlarmAdapter(arrayList, AlarmActivity.this, token, kakaoId);
                    recyclerView.setAdapter(alarmAdapter);
                    alarmAdapter.notifyDataSetChanged();

                    //arrayList를 초기화

                }
            }

            @Override
            public void onFailure(Call<List<AlarmDTO>> call, Throwable t) {
                Log.e("Call<List<AlarmDTO>>", "실패: " + t.getMessage());


            }


        });

    }
}
