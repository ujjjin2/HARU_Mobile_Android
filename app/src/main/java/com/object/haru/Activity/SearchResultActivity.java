package com.object.haru.Activity;

import static java.sql.DriverManager.println;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView back_btn;
    private List<RecruitDTO> arrayList = new ArrayList<>();
    private RecruitAdapter recruitAdapter;
    private LinearLayoutManager layoutManager;
    private Call<List<RecruitDTO>> call;
    private String token;
    private String searchWord;
    private Long kakaoId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        Log.d("검색 결과 액티비티 시작", "시작되었음");
        Intent intent = getIntent();
        searchWord = intent.getStringExtra("searchWord");

        Intent intent2 = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        Log.d("[카카오ID 확인]", String.valueOf(kakaoId));
        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //hideKeyBoard();
                                            finish();
                                        }
                                    });


        recyclerView = findViewById(R.id.searchResult_recyclerView);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        // 구분선
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(recruitAdapter);


        editText = findViewById(R.id.searchResult_edittext);
        editText.setText(searchWord);
        editText.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });

        fetch();
    }

    private void fetch() {
        call = RetrofitClientInstance.getApiService().getSearchRecruit(token,30,37.450354677762,126.65915614333, searchWord);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                if (response.isSuccessful()) {
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getBaseContext(),token, kakaoId);
                    recyclerView.setAdapter(recruitAdapter);
                    recruitAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<RecruitDTO>> call, Throwable t) {
                Log.d("[검색 실패]", "=============");
            }
        });


    }

    // 액티비티 종료시 애니메이션 삭제
//    @Override
//    protected void onPause() {
//        super.onPause();
//        overridePendingTransition(0,0);
//    }
}
