package com.object.haru.Fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.object.haru.Adapter.ContentsPagerAdapter;
import com.object.haru.R;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private HomeFragment homeFragment;

    private Fragment1 fragment1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        tabLayout = view.findViewById(R.id.Home_TabLayout);
        fragment1 = new Fragment1();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment1).commit();
        tabLayout.addTab(tabLayout.newTab().setText("전체"));
        tabLayout.addTab(tabLayout.newTab().setText("주방"));
        tabLayout.addTab(tabLayout.newTab().setText("서빙"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("ListActivity", "선택된 탭 : " + position);
                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                } else if (position == 1) {
                    selected = fragment1;
                } else if (position == 2) {
                    selected = fragment1;
                }

          getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }


}
