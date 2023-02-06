package com.object.haru.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.object.haru.Fragment.MainFragment_rc;

import java.util.ArrayList;
import java.util.List;
/*
* HomeFragment_Slide 에서 넘어갈수 있게 해주는 기능
* */

public class MainSlideAdapter extends FragmentStateAdapter {

    private int mPageCount = 2;  // 뷰 페이저 갯수

    private final List<Fragment> mFragmentList = new ArrayList<>();



    public MainSlideAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                MainFragment_rc fragment1 = new MainFragment_rc();
                return fragment1;
            case 1:
                MainFragment_rc fragment12 = new MainFragment_rc();
                return fragment12;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mPageCount;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
