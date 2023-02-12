package com.object.haru.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.object.haru.Activity.MainActivity;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    TextView Detail_tv_writeTime, //작성시간
            Detail_tv_title, //제목
            Detail_tv_name, //작성자
            detail_three_pay2,Detail_tv_pay2, // 최저시급, 최저시급(총)
            detail_three_date2,Detail_tv_date2, //근무일자
            detail_three_time2, Detail_tv_time2,//근무시간
            Detail_tv_category2, //분야
            Detail_tv_storeinfo2, //매장정보
            Detail_tv_age2, //우대나이
            Detail_tv_career2, //우대경력
            Detail_tv_sex2; //우대 성별

    boolean i = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Detail_tv_writeTime = findViewById(R.id.Detail_tv_writeTime);
        Detail_tv_name = findViewById(R.id.Detail_tv_name);
        Detail_tv_title = findViewById(R.id.Detail_tv_title);
        detail_three_pay2 = findViewById(R.id.detail_three_pay2); Detail_tv_pay2 = findViewById(R.id.Detail_tv_pay2);
        detail_three_date2 = findViewById(R.id.detail_three_date2); Detail_tv_date2 = findViewById(R.id.Detail_tv_date2);
        detail_three_time2 = findViewById(R.id.detail_three_time2); Detail_tv_time2 = findViewById(R.id.Detail_tv_time2);
        Detail_tv_category2 = findViewById(R.id.Detail_tv_category2);
        Detail_tv_storeinfo2 = findViewById(R.id.Detail_tv_storeinfo2);
        Detail_tv_age2 = findViewById(R.id.Detail_tv_age2);
        Detail_tv_career2 = findViewById(R.id.Detail_tv_career2);
        Detail_tv_sex2 = findViewById(R.id.Detail_tv_sex2);


        //툴바 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//튀로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle("");

        fetch();
    }

    private void fetch() {
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjYwODU4NjU5IiwiaWF0IjoxNjc2MTAxMjQ4LCJleHAiOjE2Nzg2OTMyNDh9.c2NFrbOsSRBgK5RtTO0dcg_FCoeZWN-x89WMEVLskHg",
                                                                                        1);
        call.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                if (response.isSuccessful()){
                    RecruitDTO recruit = response.body();
                    Detail_tv_writeTime.setText(recruit.getRtime());
                            Detail_tv_title.setText(recruit.getTitle()); //제목
                            Detail_tv_name.setText(recruit.getName()); //작성자
                            detail_three_pay2.setText(recruit.getPay()); Detail_tv_pay2.setText(recruit.getPay()); // 최저시급, 최저시급(총)
                            detail_three_date2.setText(recruit.getStTime()+"~"+recruit.getEndTime());Detail_tv_date2.setText(recruit.getStTime()+"~"+recruit.getEndTime()); //근무일자
                            detail_three_time2.setText(recruit.getStTime()+"~"+recruit.getEndTime()); Detail_tv_time2.setText(recruit.getStTime()+"~"+recruit.getEndTime());//근무시간
                            Detail_tv_category2.setText(recruit.getSubject()); //분야
                            Detail_tv_storeinfo2.setText(recruit.getAddr()); //매장정보
                            Detail_tv_age2.setText(recruit.getRage()); //우대나이
                            Detail_tv_career2.setText(recruit.getRcareer()); //우대경력
                            Detail_tv_sex2.setText(recruit.getRsex());;
//2022-02-13/10:00
                }
            }

            @Override
            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Retrofit 받아오는거 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.action_item1: {
//                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
//                startActivity(intent);
//                break;
            }

            case R.id.action_item2: {
//                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
//                startActivity(intent);
//                break;
            }
            case R.id.heart:{
                if (i ==true){

                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}