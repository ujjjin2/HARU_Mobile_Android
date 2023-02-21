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

    private String rId,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        rId = intent.getStringExtra("rId");
        Log.d("[rid확인]",rId.toString());

//        Intent intent2 = getIntent();
//        token = intent2.getStringExtra("token");
//        Log.d("[token 확인]",token);

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
             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
             startActivity(intent);
            }
        });

        ImageButton heart_btn = findViewById(R.id.imageButton_heart);
        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == true){
                    heart_btn.setImageResource(R.drawable.full_heart);

                    zzimRequestDTO zzim = new zzimRequestDTO(Integer.parseInt(rId),13);

                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimSave("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjU3ODYxMDY5IiwiaWF0IjoxNjc2NzM5NTMxLCJleHAiOjE2NzkzMzE1MzF9.1KlV8AJcOVb62n_am2dHQuB63ic_PGERRNoRVPNuuJ4"
                                                                                                    ,zzim);
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
                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimDelete("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjU3ODYxMDY5IiwiaWF0IjoxNjc2NzM5NTMxLCJleHAiOjE2NzkzMzE1MzF9.1KlV8AJcOVb62n_am2dHQuB63ic_PGERRNoRVPNuuJ4"
                                                                                                    ,13,Integer.parseInt(rId));
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
                            Call<RecruitDTO> call = RetrofitClientInstance.getApiService().Deleterecruit("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjU3ODYxMDY5IiwiaWF0IjoxNjc2NzM5NTMxLCJleHAiOjE2NzkzMzE1MzF9.1KlV8AJcOVb62n_am2dHQuB63ic_PGERRNoRVPNuuJ4",
                                    Integer.parseInt(rId));
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
                startActivity(intent);
            }
        });


    }

    private void fetch() {
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNjU3ODYxMDY5IiwiaWF0IjoxNjc2NzM5NTMxLCJleHAiOjE2NzkzMzE1MzF9.1KlV8AJcOVb62n_am2dHQuB63ic_PGERRNoRVPNuuJ4",
                Integer.parseInt(rId));
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

                            String sttime = recruit.getStTime().substring(12,16);
                            String endtime = recruit.getEndTime().substring(12,16);

                            String sttime2 = recruit.getStTime().substring(12,13);
                            String endtime2 = recruit.getEndTime().substring(12,13);
                            int st = Integer.parseInt(sttime2);
                            int ed = Integer.parseInt(endtime2);
                            int result = ed-st;
                            detail_three_time2.setText(result+"시간"); Detail_tv_time2.setText(sttime+"~"+endtime);//근무시간


                            Detail_tv_pay2.setText(recruit.getPay().toString()+"(총 "+(recruit.getPay()*result)+"원)"); // 최저시급(총)

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