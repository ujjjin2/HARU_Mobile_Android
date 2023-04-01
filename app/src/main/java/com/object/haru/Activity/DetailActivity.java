package com.object.haru.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.zzimRequestDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    TextView Detail_tv_writeTime, //작성시간
            Detail_tv_title, //제목
            Detail_tv_name, //작성자
            detail_three_pay2,Detail_tv_pay2, // 최저시급, 최저시급(총)
            detail_three_date2,Detail_tv_date2, //근무일자
            detail_three_time2, Detail_tv_time2,//근무시간
            Detail_tv_category2, //분야
            Detail_tv_storeinfo2, //매장정보
            Detail_tv_age2, //우대나이
            Detail_tv_career2, //우대경력
            Detail_tv_sex2; //우대 성별

    private int min = 9620;

    boolean i;
    private Long rId;
    private String token;

    private ImageButton heart_btn;
    private Long kakaoId;

    private double lat,lon;

    private Button apply_btn;
    private ImageButton back_btn, option_btn;
    private Long check;
    private ViewGroup mapViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        rId = intent.getLongExtra("rId", 0);
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        Log.d("[rid확인]", String.valueOf(rId));
        Log.d("[카카오ID 확인]", String.valueOf(kakaoId));
//        Log.d("[token확인]", token);

        Detail_tv_writeTime = findViewById(R.id.Detail_tv_writeTime);
        Detail_tv_name = findViewById(R.id.Detail_tv_name);
        Detail_tv_title = findViewById(R.id.Detail_tv_title);
        detail_three_pay2 = findViewById(R.id.detail_three_pay2); Detail_tv_pay2 = findViewById(R.id.Detail_tv_pay2);
        detail_three_date2 = findViewById(R.id.detail_three_date2); Detail_tv_date2 = findViewById(R.id.Detail_tv_date2);
        detail_three_time2 = findViewById(R.id.detail_three_time2); Detail_tv_time2 = findViewById(R.id.Detail_tv_time2);
        Detail_tv_category2 = findViewById(R.id.Detail_tv_category2);
        Detail_tv_storeinfo2 = findViewById(R.id.Detail_tv_storeinfo2);
        Detail_tv_age2 = findViewById(R.id.Detail_tv_age2);
        Detail_tv_career2 = findViewById(R.id.Detail_tv_career2);
        Detail_tv_sex2 = findViewById(R.id.Detail_tv_sex2);
        apply_btn = findViewById(R.id.apply_btn);
        heart_btn = findViewById(R.id.imageButton_heart);
        back_btn = findViewById(R.id.imageButton_left);
        option_btn = findViewById(R.id.imageButton_option);

        buttonaction();
    }

    private void mapView() {
        MapView mapView = new MapView(this);
        mapViewContainer = (ViewGroup)findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 중심점 변경 - 인하공전
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        /*마커 추가*/
        //마커 찍기 (인하공전)
        MapPoint MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(lat, lon);

        // 마커 아이콘 추가하는 함수
        MapPOIItem customMarker = new MapPOIItem();
        // 클릭 했을 때 나오는 호출 값
        customMarker.setItemName("매장 위치");

        customMarker.setTag(1);

        // 좌표를 입력받아 현 위치로 출력
        customMarker.setMapPoint(MARKER_POINT1);

        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.marker); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.


        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(customMarker);
    }

    private void checkZzim() {
        Call<Boolean> call = RetrofitClientInstance.getApiService().zzimCheck(token, kakaoId, rId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean check = response.body();
                if (check == true) {
                    heart_btn.setImageResource(R.drawable.full_heart);
                    i = false;
                } else {
                    heart_btn.setImageResource(R.drawable.detail_img);
                    i = true;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void buttonaction() {
        Detail_tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void fetch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit(token, rId);
                call.enqueue(new Callback<RecruitDTO>() {
                    @Override
                    public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                        if (response.isSuccessful()){
                            RecruitDTO recruit = response.body();

                            // 자신이 작성한 게시물인지 확인
                            if (recruit.getKakaoid() == kakaoId) {
                                changeWriterStatus();
                            } else {
                                changeReaderStatus();
                            }

                            lat = recruit.getLat();
                            lon = recruit.getLon();
                            Log.d("[위도, 경도]","["+lat+","+lon+"]");

                            //지도 띄우기
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mapView();
                                }
                            });

                            String writetime_date = recruit.getRtime().substring(0,10);
                            String writetime_time = recruit.getRtime().substring(11,19);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Detail_tv_writeTime.setText(writetime_date+"/"+writetime_time);

                                    Detail_tv_title.setText(recruit.getTitle()); //제목
                                    Detail_tv_name.setText(recruit.getName()); //작성자
                                    detail_three_pay2.setText(recruit.getPay().toString()); // 최저시급

                                    String stdate = recruit.getStTime().substring(0,10);
                                    String enddate = recruit.getEndTime().substring(0,10);

                                    try {
                                        Date format1 = new SimpleDateFormat("yyyy-MM-dd").parse(stdate);
                                        Date format2 = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);

                                        long diffSec = (format1.getTime() - format2.getTime()) / 1000; //초 차이
                                        long diffDays = diffSec / (24*60*60); //일자수 차이

                                        detail_three_date2.setText((Math.toIntExact(diffDays)+1)+"일");//상단 근무일자

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Detail_tv_date2.setText(stdate+"~"+enddate); //근무일자

                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                        String sttime = recruit.getStTime().substring(11,16);
                                        String endtime = recruit.getEndTime().substring(11,16);
                                        Date date1 = sdf.parse(sttime);
                                        Date date2 = sdf.parse(endtime);

                                        //문자열 -> Date
                                        long timeMil1 = date1.getTime();
                                        long timeMil2 = date2.getTime();

                                        //비교
                                        long diff = timeMil2-timeMil1;

                                        long diffHour = diff/(1000*60*24);
                                        long diffMin = diff/(1000*60);
                                        long hour = diffMin/60;
                                        long Min = diffMin%60;

                                        if (Min == 0){
                                            detail_three_time2.setText(hour+"시간"); Detail_tv_time2.setText(sttime+"~"+endtime);//근무시간
                                            Detail_tv_pay2.setText(recruit.getPay().toString()+"(총 "+(recruit.getPay()*hour)+"원)"); // 최저시급(총)
                                        }else{
                                            detail_three_time2.setText(hour+"시간"+Min+"분"); Detail_tv_time2.setText(sttime+"~"+endtime);//근무시간
                                            Detail_tv_pay2.setText(recruit.getPay().toString()+"(총 "+(recruit.getPay()*hour+recruit.getPay()/60*Min)+"원)"); // 최저시급(총)
                                        }

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            Detail_tv_category2.setText(recruit.getSubject()); //분야
                            Detail_tv_storeinfo2.setText(recruit.getAddr()); //매장정보
                            Detail_tv_age2.setText(recruit.getRage()); //우대나이
                            Detail_tv_career2.setText(recruit.getRcareer()); //우대경력
                            Detail_tv_sex2.setText(recruit.getRsex());;
//2022-02-13/10:00
                        }
                    }
                    @Override
                    public void onFailure(Call<RecruitDTO> call, Throwable t) {
                        Toast.makeText(DetailActivity.this, "Retrofit 받아오는거 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }

    private void changeWriterStatus() {
        apply_btn.setText("지원자 확인하기");
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ApplicantActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("kakaoId", kakaoId);
                intent.putExtra("rid", rId);
                startActivity(intent);
            }
        });
        heart_btn.setVisibility(View.INVISIBLE);
        option_btn.setVisibility(View.INVISIBLE);
    }

    private void changeReaderStatus() {
        checkZzim();
        isApply();
        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == true){
                    heart_btn.setImageResource(R.drawable.full_heart);

                    zzimRequestDTO zzim = new zzimRequestDTO(rId, kakaoId);

                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimSave(token, zzim);
                    call.enqueue(new Callback<zzimRequestDTO>() {
                        @Override
                        public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                            zzimRequestDTO zzimRequestDTO = response.body();
                            Toast.makeText(DetailActivity.this, "좋아요 저장", Toast.LENGTH_SHORT).show();
                            i = false;
                        }

                        @Override
                        public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                            Toast.makeText(DetailActivity.this, "좋아요 저장 실패", Toast.LENGTH_SHORT).show();
                            i = false;
                            t.printStackTrace();
                        }
                    });

                }else {
                    heart_btn.setImageResource(R.drawable.detail_img);
                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimDelete(token,kakaoId,rId);
                    call.enqueue(new Callback<zzimRequestDTO>() {
                        @Override
                        public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                            zzimRequestDTO zzimRequestDTO = response.body();
                            Toast.makeText(DetailActivity.this, "좋아요 삭제", Toast.LENGTH_SHORT).show();
                            i = true;
                        }

                        @Override
                        public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                            Toast.makeText(DetailActivity.this, "좋아요 삭제 실패", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                            i = true;
                        }
                    });
                }
            }
        });

        option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_item1){
                            Toast.makeText(DetailActivity.this, "수정 클릭", Toast.LENGTH_SHORT).show();
                        }else if (menuItem.getItemId() == R.id.action_item2){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Call<RecruitDTO> call = RetrofitClientInstance.getApiService().Deleterecruit(token, rId);
                                    call.enqueue(new Callback<RecruitDTO>() {
                                        @Override
                                        public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                                            RecruitDTO recruit = response.body();
                                            finish();
                                            Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<RecruitDTO> call, Throwable t) {
                                            Toast.makeText(DetailActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            }).start();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void isApply() {
        Call<Long> call = RetrofitClientInstance.getApiService().isApply(token, rId, kakaoId);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                check = response.body();
                if (check != 0) {
                    apply_btn.setBackgroundColor(Color.rgb(246, 100, 80));
                    apply_btn.setText("지원 취소");
                    apply_btn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             deleteCheckDialog();
                         }
                    });
                } else {
                    apply_btn.setBackgroundColor(Color.rgb(41, 148, 96));
                    apply_btn.setText("지원하기");
                    apply_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailActivity.this, ApplyWriteActivity.class);
                            intent.putExtra("rId", rId);
                            intent.putExtra("token", token);
                            intent.putExtra("kakaoId", kakaoId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {

            }
        });
    }

    private void deleteCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle("지원취소 확인");
        builder.setMessage("지원을 취소하시겠습니까?");
        builder.setCancelable(false);
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteApply();
                dialog.dismiss();

            }
        });
        builder.show();
    }

    private void deleteApply() {
        Call<Void> call = RetrofitClientInstance.getApiService().deleteApply(token, check);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isApply();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeAllViews();
    }
}