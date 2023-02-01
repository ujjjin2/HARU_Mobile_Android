package com.object.haru.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.object.haru.Fragment.Fragment1;

import java.util.ArrayList;
import java.util.List;

public class ContentsPagerAdapter extends FragmentStateAdapter {

    private int mPageCount = 0;

    private final List<Fragment> mFragmentList = new ArrayList<>();



    public ContentsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment1 fragment1 = new Fragment1();
                return fragment1;
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
