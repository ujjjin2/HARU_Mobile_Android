package com.object.haru.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public List<RecruitDTO> arrayList = new ArrayList<>();
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
    private Long kakaoId;
    private LocationManager lm;

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
        kakaoId = intent.getExtras().getLong("kakaoId");
        Log.d("[카카오ID 확인]", String.valueOf(kakaoId));

        Context context = requireContext();
        // SharedPreferences 가져오기
        SharedPreferences auto = context.getSharedPreferences("chatCount", Context.MODE_PRIVATE);
        int chatCount = auto.getInt("chatCount", 0);
        Log.d("메인 프레그먼트에서 chatCount", String.valueOf(chatCount));

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                altitude = location.getAltitude();
                if (recruitAdapter == null) {
                    fetch(page);
                }
            }
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    private void fetch(int page) {
        //2023-02-07 허유진 Retrofit 전체보이게 하기

        recruitAdapter = new RecruitAdapter(arrayList, getActivity(),token, kakaoId);
        call = RetrofitClientInstance.getApiService().getAll(token, kakaoId, latitude,longitude, page);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                
                if (response.isSuccessful() && response.body() != null) {
//                    progressBar.setVisibility(View.GONE);
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getContext(), token, kakaoId);
                    recyclerView.setAdapter(recruitAdapter);
                    recruitAdapter.notifyDataSetChanged();
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
        recyclerView.setAdapter(recruitAdapter);
    }

    private void update(int page) {
        call = RetrofitClientInstance.getApiService().getAll(token, kakaoId, latitude, longitude, page);
        call.enqueue(new Callback<List<RecruitDTO>>() {
            @Override
            public void onResponse(Call<List<RecruitDTO>> call, Response<List<RecruitDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayList.clear();
                    List<RecruitDTO> recruit = response.body();
                    arrayList.addAll(recruit);
                    recruitAdapter = new RecruitAdapter(arrayList, getContext(), token, kakaoId);
                    recyclerView.setAdapter(recruitAdapter);
                    recruitAdapter.notifyDataSetChanged();
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 권한이 있을 경우 위치 업데이트
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener2);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, gpsLocationListener2);
        } else {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // 무한 로딩 해서 주석해뒀음 0621 한승완
//        if (latitude != null && longitude != null) {
//            update(0);
//        }

        if (recruitAdapter != null) {
            recruitAdapter.notifyDataSetChanged();
        }
    }

    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private final LocationListener gpsLocationListener2 = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 위치 정보가 업데이트될 때마다 호출됩니다.
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();
            if (latitude != null && longitude != null) {
                // 위치 정보가 업데이트되면 데이터를 로드합니다.
                // 무한 로딩 해서 주석해뒀음 0621 한승완
                //update(0);
                if (recruitAdapter != null) {
                    recruitAdapter.notifyDataSetChanged();
                }
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

}
