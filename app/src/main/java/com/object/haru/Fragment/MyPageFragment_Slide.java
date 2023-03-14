package com.object.haru.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.object.haru.Activity.ProfileActivity;
import com.object.haru.Activity.ZzimListActivity;
import com.object.haru.R;

public class MyPageFragment_Slide extends Fragment  {

    private View view;
    private View profile;
    private View zzim;

    String[] descriptionData = {"모집중", "선발중", "모집 완료"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mypage, container, false);

        profile = view.findViewById(R.id.view1);
        zzim = view.findViewById(R.id.view2);

        StateProgressBar stateProgressBar = (StateProgressBar) view.findViewById(R.id.progress_bar_1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        StateProgressBar stateProgressBar2 = (StateProgressBar) view.findViewById(R.id.progress_bar_2);
        stateProgressBar.setStateDescriptionData(descriptionData);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZzimListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
