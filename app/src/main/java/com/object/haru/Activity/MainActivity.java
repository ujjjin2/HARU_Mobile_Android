package com.object.haru.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.object.haru.Fragment.HomeFragment_Slide;
import com.object.haru.Fragment.MyPageFragment_Slide;
import com.object.haru.R;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private HomeFragment_Slide home;
    private MyPageFragment_Slide myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        bottomNavigationView = findViewById(R.id.Main_bottomNavi);

         bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.mypage:
                        setFrag(2);
                        break;
                }

                 return true;
             }
         });

         home = new HomeFragment_Slide();
         myPage = new MyPageFragment_Slide();
        setFrag(0);
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n) {
            case 0:
                ft.replace(R.id.Main_frame, home);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_frame, myPage);
                ft.commit();
                break;
        }
    }



}