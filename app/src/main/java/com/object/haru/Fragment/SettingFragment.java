package com.object.haru.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.object.haru.Adapter.SettingAdapter;
import com.object.haru.R;

import java.util.ArrayList;

public class SettingFragment extends Fragment {
    ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        list = new ArrayList<>();
        list.add("공지사항");
        list.add("정보");
        list.add("개발자들");

        SettingAdapter settingAdapter = new SettingAdapter(list);
        RecyclerView recyclerView = view.findViewById(R.id.setting_recycler);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(settingAdapter);
        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화

        return view;
    }
}