package com.object.haru.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.object.haru.Adapter.MainSlideAdapter;
import com.object.haru.R;

import java.util.Arrays;
import java.util.List;
    /*
    * HomeFragment_Slide.java
    * 메인화면 슬라이드
    * */
public class HomeFragment_Slide extends Fragment {

    private View view;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private HomeFragment_Slide homeFragment;

    private MainFragment_rc fragment1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        tabLayout = view.findViewById(R.id.Home_TabLayout);
        viewPager2 = view.findViewById(R.id.Home_ViewPager);

        MainSlideAdapter contentsPagerAdapter = new MainSlideAdapter(getActivity());
        viewPager2.setAdapter(contentsPagerAdapter);

        final List<String> tabElement = Arrays.asList("전체", "서빙"); // 뷰페이저 메뉴 이름

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(getActivity());
                textView.setText(tabElement.get(position));
                tab.setCustomView(textView);
            }
        }).attach();


        return view;
    }


}
