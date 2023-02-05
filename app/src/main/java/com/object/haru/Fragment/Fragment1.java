package com.object.haru.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Adapter.CustomAdapter;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetroService;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment1 extends Fragment {

    private View view;

    private List<RecruitDTO> arrayList = new ArrayList<>();
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_whole_list, container, false);
        recyclerView = view.findViewById(R.id.Fragment1_recyclerview);

        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        customAdapter = new CustomAdapter(getContext(), arrayList);
        recyclerView.setAdapter(customAdapter);

        Log.d("[ㅇㅇㅇㅇ] : " , "111111111");
//        fetch();

        return view;
    }

//    private void fetch() {
//        RetroService service = RetrofitClientInstance.getRetrofitInstance().create(RetroService.class);
//        service.getAll(1).enqueue(new Callback<List<RecruitDTO>>() {
//            @Override
//            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
//                if (response.isSuccessful() && response.body() !=null){
//                    Log.d("[ㅇㅇㅇㅇ] : " ,  "222222222222222222");
//                    arrayList = response.body();
//                    Log.d("[성공] : " , "성공했습니다.");
//                    customAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<RecruitDTO>> call, Throwable t) {
//                Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
//                Log.d("[실패] : " , "onFailure");
//            }
//        });
//    }


}
