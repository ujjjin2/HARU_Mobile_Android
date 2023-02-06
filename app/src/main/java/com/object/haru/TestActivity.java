package com.object.haru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestActivity extends AppCompatActivity {

    Call<RecruitDTO> call;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        textView = findViewById(R.id.textView1);

//        call = RetrofitClientInstance.getApiService().getAll("1");
//        call.enqueue(new Callback<RecruitDTO>() {
//            @Override
//            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
//                textView.setText("");
//                RecruitDTO user = response.body();
//                Log.d("입력이 됐나...?", "=============");
//                String str;
//                str = user.getTitle() + "\n";
//
//                textView.setText(str);
//            }
//
//            @Override
//            public void onFailure(Call<RecruitDTO> call, Throwable t) {
//                Log.d("입력실패 ㅋ...?", "=============");
//            }
//        });

        }
    }
