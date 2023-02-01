package com.object.haru.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Adapter.CustomAdapter;
import com.object.haru.MainData;
import com.object.haru.R;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private View view;

    private ArrayList<MainData> arrayList;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_whole_list, container, false);
        recyclerView = view.findViewById(R.id.Fragment1_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        customAdapter = new CustomAdapter(arrayList);
        recyclerView.setAdapter(customAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        MainData mainData = new MainData(R.mipmap.ic_launcher,"오늘부터 4시간 서빙 대타하실분",
                "#서빙", "#3시간", "#최저시급", "#매장위치");
        for (int i = 0; i < 14; i++) {
            arrayList.add(mainData);
        }

        customAdapter.notifyDataSetChanged();

        return view;
    }
}
