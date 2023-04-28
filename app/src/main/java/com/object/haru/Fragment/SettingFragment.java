package com.object.haru.Fragment;

import android.content.Intent;
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

    private View notice, user_modify, user_logout, user_exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);

        notice = view.findViewById(R.id.notice);
        user_modify = view.findViewById(R.id.user_modify);
        user_logout = view.findViewById(R.id.user_logout);
        user_exit = view.findViewById(R.id.user_exit);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(this, NewActivity.class);
//                startActivity(intent);
            }
        });

        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(this, NewActivity.class);
//                startActivity(intent);
            }
        });

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(this, NewActivity.class);
//                startActivity(intent);
            }
        });

        user_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(this, NewActivity.class);
//                startActivity(intent);
            }
        });

        return view;
    }
}