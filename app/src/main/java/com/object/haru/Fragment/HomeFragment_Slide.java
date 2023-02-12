package com.object.haru.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.object.haru.Activity.RegisterActivity;
import com.object.haru.Activity.SearchActivity;
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
    private FloatingActionButton floatingActionButton;
    private FrameLayout frameLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private MenuItem menuItem;
    private TextView textView;

    private MainFragment_rc fragment1;
    private EditText editText_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        tabLayout = view.findViewById(R.id.Home_TabLayout);
        viewPager2 = view.findViewById(R.id.Home_ViewPager);
        editText_search = view.findViewById(R.id.HomeFragment_edittext_search);

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


        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);

        floatingActionButton = view.findViewById(R.id.HomeFragment_FAB);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        frameLayout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.notification_badge, null);
        toolbar = (Toolbar) view.findViewById(R.id.HomeFragment_Toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle("");

        setHasOptionsMenu(true);

        editText_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

        @Override
        public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);

            MenuInflater inflater1 = ((AppCompatActivity)getActivity()).getMenuInflater();
            inflater1.inflate(R.menu.noti_button, menu);

            menuItem = menu.findItem(R.id.noti);

            menuItem.setActionView(frameLayout);
            View view = menuItem.getActionView();
            textView = view.findViewById(R.id.badge_counter);

            textView.setText("3");

            setHasOptionsMenu(true);
        }
    }
