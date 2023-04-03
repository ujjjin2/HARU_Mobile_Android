package com.object.haru;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.object.haru.Activity.RegisterActivity;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.Fragment.MainFragment_rc;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView date_period,startTime_set,endTime_set;
    private EditText register_pt_age,register_pt_career,register_pt_pay, addr,register_pt_storeinfo2,Register_pt_title;
    private String token,strsex,data, sthour,stmin,endhour, endmin,stTime,endTime,firstDate,lastDate,stdate,enddate,endtime,sttime;
    private Long kakaoId,rid;
    Button category_btn,Register_btn_age,register_btn_pay,Register_btn_career,Register_btn_register;
    RadioButton radioBtn,radioBtn2,radioBtn3;
    int minpay = 9620;

    View date_view,startTime_view,EndTime_view;

    ConstraintLayout EndTime_layout,starttime_layout;


    private Spinner end_spinner_min,end_spinner_hour,start_spinner_min,start_spinner_hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        // DetailActivity 에서 받아온 Intent
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        rid = intent.getLongExtra("rId", 0);

        Log.d("[rId]", String.valueOf(rid));
        Log.d("[카카오Id 확인]", String.valueOf(kakaoId));

        toolbar(); // 툴바 부분 기능*
        calendar_view(); // 달력 관련 기능*
        timezone();// 시간 관련된 기능
        addressinfo(); //주소 관련 기능 *
        extra(); // 제목,최저 시급 및 지원 요건 기능들 *
        category(); // 카테고리 관련 기능*
        viewClick(); //뷰 관련 처리 ( 시작시간, 종료시간, 달력)*
        showData();
        retrofit(); //수정 하는 기능

    }

    private void showData() {
        Call<RecruitDTO> recruitDTOCall = RetrofitClientInstance.getApiService().getDetailRecruit(token,rid);
        recruitDTOCall.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                RecruitDTO recruitDTO = response.body();
                Register_pt_title.setText(recruitDTO.getTitle());
                category_btn.setText(recruitDTO.getSubject());

                sttime = recruitDTO.getStTime().substring(11,16);
                stdate = recruitDTO.getStTime().substring(0,10);

                endTime = recruitDTO.getEndTime().substring(11,16);
                enddate = recruitDTO.getEndTime().substring(0,10);

                date_period.setText(stdate+"~"+enddate);
                startTime_set.setText(sttime);
                endTime_set.setText(endTime);

                register_pt_pay.setText(String.valueOf(recruitDTO.getPay()));

                register_pt_age.setText(recruitDTO.getRage());
                register_pt_career.setText(recruitDTO.getRcareer());



            }

            @Override
            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                    Log.e("에러","===값 받아오기 실패===");
                    t.printStackTrace();
            }
        });
    }

    private void viewClick() {
        date_view = findViewById(R.id.date_view);
        startTime_view = findViewById(R.id.startTime_view);
        EndTime_view = findViewById(R.id.EndTime_view);
        EndTime_layout = findViewById(R.id.EndTime_layout);
        starttime_layout = findViewById(R.id.starttime_layout);

        //시간 쪽 클릭 -> 달력 생기거나 들어가거나
        calendarView.setVisibility(View.GONE);
        date_view.setOnClickListener(new View.OnClickListener() {
            boolean isCalendarVisible = false;
            @Override
            public void onClick(View view) {
                if (isCalendarVisible) {
                    calendarView.setVisibility(View.GONE);
                    isCalendarVisible = false;
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    isCalendarVisible = true;
                }

            }
        });

        starttime_layout.setVisibility(View.GONE);
        startTime_view.setOnClickListener(new View.OnClickListener() {
            boolean isStartTime = false;
            @Override
            public void onClick(View view) {

                if (isStartTime){
                    starttime_layout.setVisibility(View.GONE);
                    isStartTime = false;
                }else {
                    starttime_layout.setVisibility(View.VISIBLE);
                    isStartTime = true;
                }

            }
        });

        EndTime_layout.setVisibility(View.GONE);
        EndTime_view.setOnClickListener(new View.OnClickListener() {
            boolean isEndTime = false;
            @Override
            public void onClick(View view) {

                if (isEndTime){
                    EndTime_layout.setVisibility(View.GONE);
                    isEndTime = false;
                }else {
                    EndTime_layout.setVisibility(View.VISIBLE);
                    isEndTime = true;
                }

            }
        });

    }

    private void retrofit() {

        register_pt_storeinfo2 = findViewById(R.id.register_pt_storeinfo2);
        Register_pt_title = findViewById(R.id.Register_pt_title);

        final Geocoder geocoder = new Geocoder(this);
        Register_btn_register = findViewById(R.id.Register_btn_register);
        Register_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Address> list = null;

                try {
                    list = geocoder.getFromLocationName(data+register_pt_storeinfo2.getText().toString(),10);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (list!=null){
                    if (list.size()==0){
                        Toast.makeText(ModifyActivity.this, "해당 주소정보의 위도 경도가 주어지지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Address address = list.get(0);
                        double lat = address.getLatitude();
                        double lon = address.getLongitude();
                        Log.d("위치 출력",lat+"//////"+lon);

                        Call<RecruitDTO> callupdate = RetrofitClientInstance.getApiService().updateRecruit(token,
                                data+register_pt_storeinfo2.getText().toString(),enddate+"/"+endTime,kakaoId,lat,lon,Integer.parseInt(String.valueOf(register_pt_pay.getText())),
                                register_pt_age.getText().toString(),rid,strsex,stdate+"/"+stTime,category_btn.getText().toString(),
                                Register_pt_title.getText().toString());

                        callupdate.enqueue(new Callback<RecruitDTO>() {
                            @Override
                            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                                RecruitDTO recruitDTO = response.body();
                                Log.d("업데이트 성공","=================");
                                finish();
                            }

                            @Override
                            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                                Log.e("업데이트 실패","=================");
                                t.printStackTrace();
                            }
                        });

                    }
                }
            }
        });
    }

    private void category() {
        category_btn = findViewById(R.id.register_sp_category);
        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] kind = getResources().getStringArray(R.array.kind);
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
                builder.setTitle("분야를 선택해주세요");

                //다이얼로그에 리스트 담기
                builder.setItems(kind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (kind[i].equals("식당")){
                            String[] kind1 = getResources().getStringArray(R.array.kind1);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ModifyActivity.this);
                            builder1.setTitle("해당되는 일을 선택해주세요");
                            builder1.setItems(kind1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    category_btn.setText("식당/"+kind1[i]);
                                }
                            });
                            AlertDialog alertDialog1 = builder1.create();
                            alertDialog1.show();
                        }else if (kind[i].equals("카페")){
                            String[] kind2 = getResources().getStringArray(R.array.kind2);
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(ModifyActivity.this);
                            builder2.setTitle("해당되는 일을 선택해주세요");
                            builder2.setItems(kind2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    category_btn.setText("카페/"+kind2[i]);
                                }
                            });
                            AlertDialog alertDialog2 = builder2.create();
                            alertDialog2.show();
                        }else if (kind[i].equals("편의점")){
                            String[] kind3 = getResources().getStringArray(R.array.kind3);
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(ModifyActivity.this);
                            builder3.setTitle("해당되는 일을 선택해주세요");
                            builder3.setItems(kind3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    category_btn.setText("편의점/"+kind3[i]);
                                }
                            });
                            AlertDialog alertDialog3 = builder3.create();
                            alertDialog3.show();
                        }else if (kind[i].equals("기타")){
                            final EditText et = new EditText(ModifyActivity.this);
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(ModifyActivity.this);
                            builder4.setTitle("해당되는 일을 선택해주세요").setMessage("일을 적어주세요");
                            builder4.setView(et);
                            builder4.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String value = et.getText().toString();
                                    category_btn.setText("기타/"+value);
                                }
                            });

                            AlertDialog alertDialog4 = builder4.create();
                            alertDialog4.show();
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    private void extra() {

        register_btn_pay = findViewById(R.id.register_btn_pay);
        register_pt_pay = (EditText) findViewById(R.id.register_pt_pay);
        register_btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_pay.setText(String.valueOf(minpay));
            }
        });

        Register_btn_age = findViewById(R.id.Register_btn_age);
        Register_btn_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_age = findViewById(R.id.register_pt_age);
                register_pt_age.setText("연령무관");
            }
        });

        Register_btn_career = findViewById(R.id.Register_btn_career);
        Register_btn_career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_pt_career = findViewById(R.id.register_pt_career);
                register_pt_career.setText("경력무관");
            }
        });

        radioBtn = findViewById(R.id.radioButton);
        radioBtn2 = findViewById(R.id.radioButton2);
        radioBtn3 = findViewById(R.id.radioButton3);

        RadioGroup radioGroup= findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton:
                        strsex = "성별 무관";
                        break;
                    case R.id.radioButton2:
                        strsex = "남성 우대";
                        break;
                    case R.id.radioButton3:
                        strsex = "여성 우대";
                        break;
                }
            }
        });

    }

    private void addressinfo() {
        addr = findViewById(R.id.register_pt_storeinfo);
        addr.setFocusable(false);
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Log.i("주소설정페이지", "주소입력창 클릭");
                Intent i = new Intent(getApplicationContext(), Search_register.class);
                // 화면전환 애니메이션 없애기
                overridePendingTransition(0, 0);
                // 주소결과
                getSearchResult.launch(i);
            }
        });
    }

    private void timezone() {
        start_spinner_hour = findViewById(R.id.start_spinner_hour);
        start_spinner_min = findViewById(R.id.start_spinner_min);

        ArrayAdapter starthourAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.time_hour, android.R.layout.simple_spinner_item);
        ArrayAdapter startminAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.time_minute, android.R.layout.simple_spinner_item);

        starthourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_spinner_hour.setAdapter(starthourAdapter);
        startminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_spinner_min.setAdapter(startminAdapter);


        end_spinner_hour = findViewById(R.id.end_spinner_hour);
        end_spinner_min = findViewById(R.id.end_spinner_min);

        ArrayAdapter endthourAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.time_hour, android.R.layout.simple_spinner_item);
        ArrayAdapter endtminAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.time_minute, android.R.layout.simple_spinner_item);

        endthourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        end_spinner_hour.setAdapter(endthourAdapter);
        endtminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        end_spinner_min.setAdapter(endtminAdapter);

        startTime_set = findViewById(R.id.startTime_set);
        startTime_set.setVisibility(View.GONE);
        start_spinner_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!start_spinner_hour.getItemAtPosition(i).equals("0시")) {
                    sthour = (String) start_spinner_hour.getSelectedItem();
                } else {
                    sthour = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        start_spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!start_spinner_min.getItemAtPosition(i).equals("0분")) {
                    stmin = (String) start_spinner_min.getSelectedItem();
                    stTime = sthour + ":" + stmin;
                    startTime_set.setText(stTime);
                    startTime_set.setVisibility(View.VISIBLE);
                }else {
                    stmin = "";
                    startTime_set.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        endTime_set = findViewById(R.id.endTime_set);
        endTime_set.setVisibility(View.GONE);

        end_spinner_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!end_spinner_hour.getItemAtPosition(i).equals("0시")){
                    endhour = (String) end_spinner_hour.getSelectedItem();
                } else {
                    endhour = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        end_spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!end_spinner_min.getItemAtPosition(i).equals("0분")){
                    endmin = (String) end_spinner_min.getSelectedItem();
                    endTime = endhour+":"+endmin;
                    endTime_set.setText(endTime);
                    endTime_set.setVisibility(View.VISIBLE);
                } else {
                    endmin = "";
                    endTime_set.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void toolbar() {
        ImageButton back_btn = findViewById(R.id.imageButton_left_register);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //Search_register로 부터의 결과 값을 받아 이곳으로 전달
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        data = result.getData().getStringExtra("data");
                        addr.setText(data);
                    }
                }
            }
    );

    private void calendar_view() {
        calendarView = findViewById(R.id.calendar_view);
        date_period = findViewById(R.id.date_period);

        calendarView.setShowDaysOfWeekTitle(false);
        calendarView.setSelectionManager(new RangeSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                List<Calendar> selectedDates = calendarView.getSelectedDates();
                if (selectedDates.size() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    firstDate = sdf.format(selectedDates.get(0).getTime());
                    lastDate = sdf.format(selectedDates.get(selectedDates.size() - 1).getTime());
                    String message = firstDate + " ~ " + lastDate;
                    date_period.setText(message);

                }
            }
        }));



    }
}