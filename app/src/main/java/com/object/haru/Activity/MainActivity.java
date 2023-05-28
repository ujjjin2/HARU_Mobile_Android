package com.object.haru.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.object.haru.Chat.ChatListFragment;


import com.object.haru.DTO.FCMDTO;
import com.object.haru.Fragment.HomeFragment_Slide;

import com.object.haru.Fragment.MyPageFragment_Slide;
import com.object.haru.Fragment.SettingFragment;
import com.object.haru.R;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    public static MainActivity INSTANCE;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private HomeFragment_Slide home;
    private ChatListFragment chatListFragment;
    private MyPageFragment_Slide myPage;
    private SettingFragment settingPage;
    public String accessToken;
    private Long kakaoId;
    private int chatCount, checkChat;
    private double longitude, latitude, altitude;
    private SharedPreferences auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        INSTANCE = this;


         auto = getSharedPreferences("checkChat", Activity.MODE_PRIVATE);
         chatCount = auto.getInt("checkChat", 0);


        Log.d("메인에서 chatCount", String.valueOf(chatCount));


        Intent intent = getIntent();
        checkChat = intent.getIntExtra("checkChat", 0);
        accessToken = intent.getStringExtra("token");
//        kakaoId = intent.getLongExtra("kakaoId",0);
        kakaoId = intent.getExtras().getLong("kakaoId");
        Log.d("[메인에서 카카오ID 확인]", String.valueOf(kakaoId));

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                String provider = location.getProvider();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                altitude = location.getAltitude();
            }

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, gpsLocationListener);
        }

        bottomNavigationView = findViewById(R.id.Main_bottomNavi);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.chatList:
                        SharedPreferences.Editor autoLoginEdit = auto.edit();
                        autoLoginEdit.putInt("chatCount", 0);
                        autoLoginEdit.apply();
                        checkChat=0;
                        item.setIcon(R.drawable.chat);

                        setFrag(1);
                        break;
                    case R.id.mypage:
                        setFrag(2);
                        break;
                    case R.id.setting:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });



        home = new HomeFragment_Slide();
        chatListFragment = new ChatListFragment();
        myPage = new MyPageFragment_Slide();
        settingPage = new SettingFragment();
        bottomNavigationView.setItemIconTintList(null);

        setFrag(0);

// 채팅 알림받은 경우 끝

    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        setIcon();


        switch (n) {
            case 0:
                ft.replace(R.id.Main_frame, home);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Main_frame, chatListFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_frame, myPage);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.Main_frame, settingPage);
                ft.commit();
                break;
        }



    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            // 위치 리스너는 위치 정보를 전달할 때 호출 되므로 onLocationChanged() 메소드 안에 위치정보를 처리를 작업을 구현해야 합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };

    public void setIcon(){
        Menu menu = bottomNavigationView.getMenu();
        if (menu != null) {
            MenuItem chatListMenuItem = menu.findItem(R.id.chatList); // chatListMenuItem 변수 선언
            if (chatCount >= 1 || checkChat >=1) {
                chatListMenuItem.setIcon(R.drawable.chat3);
                Log.d("아이콘 변경", "아이콘 변경 실행됨");
                Log.d("아이콘 변경 chatCount", String.valueOf(chatCount));
                Log.d("아이콘 변경 checkChat", String.valueOf(checkChat));
            } else{
                Log.d("아이콘 미변경 chatCount", String.valueOf(chatCount));
                Log.d("아이콘 미변경 checkChat", String.valueOf(checkChat));
                chatListMenuItem.setIcon(R.drawable.chat);
            }
        }
    }


    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

}