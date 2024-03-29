package com.object.haru.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.zzimRequestDTO;
import com.object.haru.ModifyActivity;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private NestedScrollView nestedScrollView;
    private MotionLayout motionLayout;

    private View emptyView, lineView3, lineView2;

    private TextView deposit_tv2, deposit_tv3, writer, writeTime_tv, title_tv, writer_tv, topMoney_tv, firstDayCount_tv,
                    date_tv, secondDayCount_tv, workTime_tv, time_tv, status_tv, gender_tv, age_tv, career_tv, bottomMoney_tv,
                    location_tv, category_tv;

    private ImageView starButton, backButton, optionButton;
    private Long check;

    private Long rId, kakaoId;
    private String token;
    private RecruitDTO recruit;

    private Button applyButton;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;

    private double lat, lon;
    private boolean zzimButtonCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        rId = intent.getLongExtra("rId", 0);
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        Log.d("[Test rid확인]", String.valueOf(rId));
        Log.d("[Test token]", token);
        Log.d("[Test kakaoId]", String.valueOf(kakaoId));

        backButton = findViewById(R.id.back_btn);
        tabLayout = findViewById(R.id.tablayout);
        nestedScrollView = findViewById(R.id.nested_scrollview);
//        motionLayout = findViewById(R.id.motionlayout);
        lineView2 = findViewById(R.id.line_view2);
        lineView3 = findViewById(R.id.line_view3);
        emptyView = findViewById(R.id.emptyView);
//        deposit_tv2 = findViewById(R.id.deposit_tv2);
        writer = findViewById(R.id.writer_text);
        writeTime_tv = findViewById(R.id.time_text);
        title_tv = findViewById(R.id.title_text);
        writer_tv = findViewById(R.id.writer_text);
        topMoney_tv = findViewById(R.id.money_edit);
        firstDayCount_tv = findViewById(R.id.dayCount);
        date_tv = findViewById(R.id.work_period_data);
        secondDayCount_tv = findViewById(R.id.day_data);
        workTime_tv = findViewById(R.id.work_time);
        time_tv = findViewById(R.id.time_data);
        status_tv = findViewById(R.id.status_data);
        gender_tv = findViewById(R.id.gender_data);
        age_tv = findViewById(R.id.age_data);
        career_tv = findViewById(R.id.career_data);
        bottomMoney_tv = findViewById(R.id.money_data);
        location_tv = findViewById(R.id.location_data);
        applyButton = findViewById(R.id.apply_btn);
        starButton = findViewById(R.id.imageView22);
        optionButton = findViewById(R.id.imageButton_option);
        category_tv = findViewById(R.id.category_data);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        coordinatorLayout = findViewById(R.id.coodinatorlayout);
        appBarLayout = findViewById(R.id.appBarLayout);

        appBarLayout.setExpanded(true);


//        tabLayout.addOnTabSelectedListener(this);
//        nestedScrollView.setOnScrollChangeListener(this);

        TabLayout.Tab firstTab = tabLayout.newTab().setText("지원조건");
        TabLayout.Tab secondTab = tabLayout.newTab().setText("근무지역");
        TabLayout.Tab thirdTab = tabLayout.newTab().setText("근무조건");

        tabLayout.addTab(firstTab);
        tabLayout.addTab(secondTab);
        tabLayout.addTab(thirdTab);

        moveUserProfile();
        actionBackButton();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);
                switch (tab.getPosition()) {
                    case 0:
                        nestedScrollView.smoothScrollTo(0, emptyView.getTop(), 1000);
                        appBarLayout.setExpanded(false);
                        break;
                    case 1:
                        nestedScrollView.smoothScrollTo(0, lineView2.getTop(), 1000);
                        appBarLayout.setExpanded(false);
                        break;
                    case 2:
                        nestedScrollView.smoothScrollTo(0, lineView3.getTop(), 1000);
                        appBarLayout.setExpanded(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 탭 선택 이전에는 AppBarLayout을 확장된 상태로 유지
                appBarLayout.setExpanded(true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    tabLayout.setScrollPosition(0, 0f, true);
                } else if (scrollY >= computeDistanceToView(nestedScrollView, lineView2) && (scrollY < computeDistanceToView(nestedScrollView, lineView3))) {
                    tabLayout.setScrollPosition(1, 0f, true);
                } else if (scrollY >= computeDistanceToView(nestedScrollView, lineView3) && (scrollY < computeDistanceToView(nestedScrollView, deposit_tv2))) {
                    tabLayout.setScrollPosition(2, 0f, true);
                } else {
                    tabLayout.setScrollPosition(3, 0f, true);
                }
            }
        });

    }

    private void checkZzim() {
        Call<Boolean> call = RetrofitClientInstance.getApiService().zzimCheck(token, kakaoId, rId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean check = response.body();
                if (check == true) {
                    starButton.setImageResource(R.drawable.starcolor);
                    zzimButtonCheck = false;
                } else {
                    starButton.setImageResource(R.drawable.star);
                    zzimButtonCheck = true;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    private void moveUserProfile() {
        writer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actionBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRecruitData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<RecruitDTO> call = RetrofitClientInstance.getApiService().getDetailRecruit(token, rId);
                call.enqueue(new Callback<RecruitDTO>() {
                    @Override
                    public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                        if (response.isSuccessful()) {
                            recruit = response.body();
                            if (recruit.getKakaoid() == kakaoId) {
                                changeWriterStatus();
                            } else {
                                changeReaderStatus();
                            }

                            lat = recruit.getLat();
                            lon = recruit.getLon();

                            String writeDate = recruit.getRtime().substring(0, 10);
                            String writeTime = recruit.getRtime().substring(11, 19);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    writeTime_tv.setText(writeDate + " " + writeTime);
                                    title_tv.setText(recruit.getTitle());
                                    writer_tv.setText(recruit.getName());
                                    DecimalFormat decimalFormat = new DecimalFormat("###,###");
                                    String formattedNumber = decimalFormat.format(recruit.getPay());
                                    topMoney_tv.setText(formattedNumber + "원");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                                    String startDateString = recruit.getStTime();
//                                    Toast.makeText(getApplicationContext(), startDateString, Toast.LENGTH_SHORT).show();
                                    String endDateString = recruit.getEndTime();

                                    try {
                                        Date startDate = format.parse(startDateString);
                                        Date endDate = format.parse(endDateString);
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        String start = outputFormat.format(startDate);
                                        String end = outputFormat.format(endDate);

                                        long diff = endDate.getTime() - startDate.getTime();
                                        long diffDays = diff / (24 * 60 * 60 * 1000);

                                        Log.d("Days", "일 수: " + diffDays); // 결과 출력
                                        firstDayCount_tv.setText(Long.toString(diffDays + 1) + "일");
                                        date_tv.setText(start + " ~ " + end);
                                        secondDayCount_tv.setText(Long.toString(diffDays + 1) + "일");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        Date d1 = format1.parse(startDateString);
                                        Date d2 = format1.parse(endDateString);

                                        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                                        String start = outputFormat.format(d1);
                                        String end = outputFormat.format(d2);

                                        long diff = d2.getTime() - d1.getTime();

                                        long diffMinutes = diff / (60 * 1000) % 60;
                                        long diffHours = diff / (60 * 60 * 1000) % 24;

                                        if (diffHours > 0) {
                                            if (diffMinutes > 0) {
                                                workTime_tv.setText(Long.toString(diffHours) + "시간 " + Long.toString(diffMinutes) + "분");
                                            } else {
                                                workTime_tv.setText(Long.toString(diffHours) + "시간");
                                            }
                                        } else {
                                            workTime_tv.setText(Long.toString(diffMinutes) + "분");
                                        }
                                        time_tv.setText(start + " ~ " + end);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    status_tv.setText(recruit.getStep());
                                    gender_tv.setText(recruit.getRsex());
                                    age_tv.setText(recruit.getRage());
                                    career_tv.setText(recruit.getRcareer());
                                    bottomMoney_tv.setText(recruit.getPay().toString() + "원");
                                    location_tv.setText(recruit.getAddr());
                                    category_tv.setText(recruit.getSubject());
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<RecruitDTO> call, Throwable t) {
                        Toast.makeText(DetailActivity.this, "레트로핏 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void changeWriterStatus() {
        applyButton.setText("지원자 확인하기");
        applyButton.setBackgroundResource(R.drawable.register_btn2);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ApplicantActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("kakaoId", kakaoId);
                intent.putExtra("rid", rId);
                startActivity(intent);
            }
        });
        starButton.setVisibility(View.INVISIBLE);
        optionButton.setVisibility(View.VISIBLE);

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionButton.setEnabled(false);
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_item1){
                            Intent intent = new Intent(DetailActivity.this, ModifyActivity.class);
                            intent.putExtra("rId", rId);
                            intent.putExtra("token", token);
                            intent.putExtra("kakaoId", kakaoId);
                            startActivity(intent);

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

                                            optionButton.setEnabled(true);
                                        }

                                        @Override
                                        public void onFailure(Call<RecruitDTO> call, Throwable t) {
                                            Toast.makeText(DetailActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                            t.printStackTrace();

                                            optionButton.setEnabled(true);
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

    private void changeReaderStatus() {
        checkZzim();
        isApply();

        starButton.setVisibility(View.VISIBLE);
        optionButton.setVisibility(View.GONE);

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starButton.setEnabled(false);
                if (zzimButtonCheck == true) {
                    starButton.setImageResource(R.drawable.starcolor);
                    zzimRequestDTO zzim = new zzimRequestDTO(rId, kakaoId);

                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimSave(token, zzim);
                    call.enqueue(new Callback<zzimRequestDTO>() {
                        @Override
                        public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                            zzimRequestDTO zzimRequestDTO = response.body();
                            Toast.makeText(DetailActivity.this, "좋아요 저장", Toast.LENGTH_SHORT).show();
                            zzimButtonCheck = false;

                            starButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                            Toast.makeText(DetailActivity.this, "좋아요 저장 실패", Toast.LENGTH_SHORT).show();
                            zzimButtonCheck = false;
                            t.printStackTrace();

                            starButton.setEnabled(true);
                        }
                    });
                } else {
                    starButton.setImageResource(R.drawable.star);
                    Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimDelete(token, kakaoId, rId);
                    call.enqueue(new Callback<zzimRequestDTO>() {
                        @Override
                        public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                            zzimRequestDTO zzimRequestDTO = response.body();
                            Toast.makeText(DetailActivity.this, "좋아요 삭제", Toast.LENGTH_SHORT).show();
                            zzimButtonCheck = true;

                            starButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                            Toast.makeText(DetailActivity.this, "좋아요 삭제 실패", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                            zzimButtonCheck = true;

                            starButton.setEnabled(true);
                        }
                    });
                }
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
                    if (recruit.getPerson() == null) {
                        applyButton.setBackgroundResource(R.drawable.register_cancel_btn);
                        applyButton.setText("지원 취소");
                        applyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                applyButton.setEnabled(false);
                                deleteCheckDialog();
                            }
                        });
                    } else if (recruit.getPerson() != null) {
                        applyButton.setBackgroundResource(R.drawable.register_closed_btn);
                        applyButton.setText("지원이 마감되었습니다.");
                        applyButton.setEnabled(true);
                    } else {
                        Log.i("예외 발생", "예외 발생");
                    }
                } else {
                    applyButton.setBackgroundResource(R.drawable.register_btn2);
                    applyButton.setText("지원서 작성하기");
                    applyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            applyButton.setEnabled(false);
                            Intent intent = new Intent(DetailActivity.this, ApplyWriteActivity.class);
                            intent.putExtra("rId", rId);
                            intent.putExtra("token", token);
                            intent.putExtra("kakaoId", kakaoId);
                            startActivity(intent);
                            applyButton.setEnabled(true);
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
        applyButton.setEnabled(true);
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

    public static int computeDistanceToView(NestedScrollView scrollView, View view) {
        int top = calculateRectOnScreen(scrollView).top;
        int scrollY = scrollView.getScrollY();
        int viewTop = calculateRectOnScreen(view).top;
        return Math.abs(top - (scrollY + viewTop));
    }

    public static Rect calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Rect(
                location[0],
                location[1],
                location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecruitData();
    }
}
