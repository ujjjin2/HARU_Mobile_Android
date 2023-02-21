package com.object.haru.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.object.haru.Activity.BottomSheetDialog;
import com.object.haru.Activity.LocationActivity;
import com.object.haru.Activity.RegisterActivity;
//import com.object.haru.Activity.SearchActivity;
import com.object.haru.Activity.SearchActivity;
import com.object.haru.Adapter.MainSlideAdapter;
import com.object.haru.R;

import java.io.IOException;
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
    private TextView textView, location;

    private MainFragment_rc fragment1;
    private EditText editText_search;
        private double longitude, latitude, altitude;
        private String name;

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);

        tabLayout = view.findViewById(R.id.Home_TabLayout);
        viewPager2 = view.findViewById(R.id.Home_ViewPager);
        editText_search = view.findViewById(R.id.HomeFragment_edittext_search);
        location = view.findViewById(R.id.text_location);
            final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            else {
                // 가장 최근 위치 정보 가져오기
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> gList = null;

                    try {
                        gList = geocoder.getFromLocation(latitude, longitude, 8);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "서버에서 주소변환시 에러발생");
                    }
                    if (gList != null) {
                        if (gList.size() == 0) {
                            Toast.makeText(getContext(), "현재위치에서 검색된 주소정보가 없습니다.", Toast.LENGTH_SHORT);

                        }else {
                            Address address = gList.get(0);

                            name = address.getAddressLine(0).substring(5);

                            Log.d("찾은 주소", name);
                        }
                    }
                }

                // 위치 정보를 원하는 시간, 거리마다 갱신해준다.
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
            }

            location.setText(name);

            location.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                                                bottomSheetDialog.show(getActivity().getSupportFragmentManager(),  bottomSheetDialog.getTag());
                                            }
                                        });

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


    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            // 위치 리스너는 위치 정보를 전달할 때 호출 되므로 onLocationChanged() 메소드 안에 위치정보를 처리를 작업을 구현해야 합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

        @Override
        public void onResume() {
            super.onResume();
            final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            else {
                // 가장 최근 위치 정보 가져오기
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    String provider = location.getProvider();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    double altitude = location.getAltitude();

                    Geocoder geocoder = new Geocoder(getContext());
                    List<Address> gList = null;

                    try {
                        gList = geocoder.getFromLocation(latitude, longitude, 8);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "서버에서 주소변환시 에러발생");
                    }
                    if (gList != null) {
                        if (gList.size() == 0) {
                            Toast.makeText(getContext(), "현재위치에서 검색된 주소정보가 없습니다.", Toast.LENGTH_SHORT);

                        }else {
                            Address address = gList.get(0);

                            name = address.getAddressLine(0).substring(5);

                            Log.d("찾은 주소", name);
                        }
                    }
                }

                // 위치 정보를 원하는 시간, 거리마다 갱신해준다.
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
            }

            location.setText(name);
        }
    }
