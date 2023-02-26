package com.object.haru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.zzimRequestDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

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

    boolean i = true;

    private int rId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        rId = Math.toIntExact(intent.getLongExtra("rId", 1));
        token = intent.getStringExtra("token");
        Log.d("[rid확인]", String.valueOf(rId));
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

        buttonaction();

        fetch();
    }

    private void buttonaction() {
        Detail_tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageButton back_btn = findViewById(R.id.imageButton_left);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });

        ImageButton heart_btn = findViewById(R.id.imageButton_heart);
        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == true){
                    heart_btn.setImageResource(R.drawable.full_heart);

                    zzimRequestDTO zzim = new zzimRequestDTO(rId,1);

                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimSave(token,zzim);
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

                }else{
                    heart_btn.setImageResource(R.drawable.detail_img);
                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimDelete(token,1,rId);
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
                        }
                    });
                }
            }
        });
        ImageButton option_btn = findViewById(R.id.imageButton_option);
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
                            Call<RecruitDTO> call = RetrofitClientInstance.getApiService().Deleterecruit(token, rId);
                            call.enqueue(new Callback<RecruitDTO>() {
                                @Override
                                public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                                    RecruitDTO recruit = response.body();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<RecruitDTO> call, Throwable t) {
                                    Toast.makeText(DetailActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                }
                            });
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        Button apply_btn = findViewById(R.id.apply_btn);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ApplyWriteActivity.class);
                intent.putExtra("rId", rId);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });


    }

    private void fetch() {
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit(token, rId);
        call.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                if (response.isSuccessful()){
                    RecruitDTO recruit = response.body();
                    Detail_tv_writeTime.setText(recruit.getRtime());
                            Detail_tv_title.setText(recruit.getTitle()); //제목
                            Detail_tv_name.setText(recruit.getName()); //작성자
                            detail_three_pay2.setText(recruit.getPay().toString()); // 최저시급

                            String stdate = recruit.getStTime().substring(0,10);
                            String enddate = recruit.getEndTime().substring(0,10);
                            detail_three_date2.setText(stdate);//상단 근무일자
                            Detail_tv_date2.setText(stdate+"~"+enddate); //근무일자

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String sttime = recruit.getStTime().substring(12,16);
                        String endtime = recruit.getEndTime().substring(12,16);
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


}