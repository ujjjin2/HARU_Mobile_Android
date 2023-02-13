package com.object.haru.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

/*
 * MainFragment_rc.java
 * 메인화면 리사이클러뷰
 * */

public class MainFragment_rc extends Fragment {

    private View view;

    private List<RecruitDTO> arrayList = new ArrayList<>();
    private RecruitAdapter recruitAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Call<List<RecruitDTO>> call;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_whole_list, container, false);
        recyclerView = view.findViewById(R.id.Fragment1_recyclerview);

        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        // 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(recruitAdapter);

        Log.d("[ㅇㅇㅇㅇ] : " , "111111111");
        fetch();

        return view;
    }

    private void fetch() {
        //2023-02-07 허유진 Retrofit 전체보이게 하기

        call = RetrofitClientInstance.getApiService().getAll("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjYwODU4NjU5IiwiaWF0IjoxNjc2MjUyMTc5LCJleHAiOjE2Nzg4NDQxNzl9.e7XfU8fOIR20USIgYcyKi8QA9aaQMKUBI8VEg65o-wk",
                30,37.450354677762,126.65915614333);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                
                if (response.isSuccessful()) {
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getContext());
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
