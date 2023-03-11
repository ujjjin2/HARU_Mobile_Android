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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.object.haru.Activity.MainActivity;
import com.object.haru.Adapter.RecruitAdapter;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 * MainFragment_rc.java
 * 메인화면 리사이클러뷰
 * */

public class MainFragment_rc extends Fragment {

    private View view;

    private List<RecruitDTO> arrayList = new ArrayList<>();
    private RecruitAdapter recruitAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Call<List<RecruitDTO>> call;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private String token;
    private Double longitude, latitude, altitude;
    private SwipeRefreshLayout swipeRefreshLayout;


    int page = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_whole_list, container, false);
        recyclerView = view.findViewById(R.id.Fragment1_recyclerview);
        nestedScrollView = view.findViewById(R.id.scrollView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

//        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);//리사이클뷰 기존성능 강화


        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra("token");

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                altitude = location.getAltitude();
                fetch(page);
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, gpsLocationListener);
        }
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recruitAdapter);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    fetch(page);
                }
            }
        });


        return view;
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

    private void fetch(int page) {
        //2023-02-07 허유진 Retrofit 전체보이게 하기

        call = RetrofitClientInstance.getApiService().getAll(token,
                30,latitude,longitude, page);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                
                if (response.isSuccessful() && response.body() != null) {
//                    progressBar.setVisibility(View.GONE);

                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getContext(), token);
                    recyclerView.setAdapter(recruitAdapter);
                    Log.d("[입력 성공]", "=============");
                } else {
//                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<RecruitDTO>> call, Throwable t) {
                Log.d("[입력 실패]", "=============");
            }
        });
    }

    private void update(int page) {
        call = RetrofitClientInstance.getApiService().getAll(token, 30, latitude, longitude, page);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayList.clear();
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getContext(), token);
                    recyclerView.setAdapter(recruitAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<RecruitDTO>> call, Throwable t) {
                Log.d("[업데이트 실패]", "=============");
            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(0);
                page = 0;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    fetch(page);
                }
            }
        });
    }
}
