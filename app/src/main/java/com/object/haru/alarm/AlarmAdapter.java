package com.object.haru.alarm;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.object.haru.Activity.ApplyDetailActivity;
import com.object.haru.Activity.DetailActivity;
import com.object.haru.Activity.LoginActivity;

import com.object.haru.DTO.ApplyDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.CustomViewHolder> {
    private List<AlarmDTO> alarmList;
    private Context context;
    private String token;
    private Long kakaoid;

    private AlarmDTO alarmDTO;

    public AlarmAdapter(List<AlarmDTO> alarmList, Context context, String token, Long kakaoid) {
        this.alarmList = alarmList;
        this.context = context;
        this.token = token;
        this.kakaoid = kakaoid;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        AlarmDTO alarm = alarmList.get(position);

        // 알림 확인 값에 따라 배경색 설정
        if (alarm.getConfirm() == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE); // 알림 확인이 0인 경우 하얀색 배경
        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_border); // 알림 확인이 1인 경우 초록색 배경
        }

        String timeAgo;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String alTime = alarm.getAlTime();

            Log.d("alTime", alTime);
           // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            String formattedAlTime = alTime.substring(0, alTime.lastIndexOf('.'));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime rDateTime = LocalDateTime.parse(formattedAlTime, formatter);


            Duration duration = Duration.between(rDateTime, LocalDateTime.now());
            long hoursDiff = duration.toHours();

            if (hoursDiff < 1) {
                long minutesDiff = duration.toMinutes();  // 시간 차이를 분 단위로 계산
                if (minutesDiff < 1) {
                    timeAgo = "방금 전";
                } else {
                    timeAgo = minutesDiff + "분 전";
                }
            } else if (hoursDiff < 24) {
                timeAgo = hoursDiff + "시간 전";
            } else {
                long daysDiff = hoursDiff / 24;
                if (daysDiff == 1) {
                    timeAgo = "1일 전";
                } else {
                    timeAgo = daysDiff + "일 전";
                }
            }

        } else {
            timeAgo = "";     // SDK 버전이 낮은 경우 처리
        }

        holder.title.setText(alarm.getTitle());
        holder.body.setText(alarm.getBody());
        holder.time.setText(timeAgo);

    }
    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView title, body, time;
        private boolean isSelected;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            body = itemView.findViewById(R.id.body_tv);
            time = itemView.findViewById(R.id.Time_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    Log.d("클릭", String.valueOf(position));
                    alarmDTO = alarmList.get(position);

                    Log.d("읽음 상태 체크",String.valueOf(alarmDTO.getConfirm()));
                    Log.d("변경할 id ",String.valueOf(alarmDTO.getAlarmid()));

                    Call<Void> updateCheck =  RetrofitClientInstance.getApiService().updateCheckAlarm(token, alarmDTO.getAlarmid());
                    updateCheck.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("[알림체크]","성공");
                            Intent intent = null;
                            if(alarmDTO.getTitle().equals("새로운 지원서가 도착했습니다!")){
                                long id = alarmDTO.getAid();
                                Call<ApplyDTO> applyDetail = RetrofitClientInstance.getApiService().getApplyDetail(token, id);
                                applyDetail.enqueue(new Callback<ApplyDTO>() {
                                    @Override
                                    public void onResponse(Call<ApplyDTO> call, Response<ApplyDTO> response) {
                                        alarmDTO.setConfirm(0);
                                        notifyItemChanged(position);
                                        Intent   intent = new Intent(context, ApplyDetailActivity.class);
                                        intent.putExtra("newApply", "newApply");
                                        intent.putExtra("id", String.valueOf(alarmDTO.getAid()));
                                        intent.putExtra("token", token);
                                        intent.putExtra("sex", response.body().getAsex());
                                        intent.putExtra("self", response.body().getMyself());
                                        intent.putExtra("name", response.body().getName());
                                        intent.putExtra("career", response.body().getAcareer());
                                        intent.putExtra("age", response.body().getAage());
                                        intent.putExtra("rating", response.body().getAvgRating());
                                        intent.putExtra("rId", response.body().getRid());
                                        intent.putExtra("Fridkakaoid", response.body().getKakaoid()); //long 타입
                                        intent.putExtra("kakaoid", kakaoid.toString());
                                        context.startActivity(intent);
                                    }
                                    @Override
                                    public void onFailure(Call<ApplyDTO> call, Throwable t) {
                                        Log.d("[지원서 확인]","실패"+t.getStackTrace().toString());
                                    }
                                });


                            }else if(alarmDTO.getTitle().equals("지원하신 알바가 확정되었습니다!")){
                                alarmDTO.setConfirm(0);
                                notifyItemChanged(position);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    intent = new Intent(context, DetailActivity.class);

                                }
                                intent.putExtra("comfirmation", "comfirmation");
                                intent.putExtra("rId", Long.valueOf(alarmDTO.getRid()));
                                intent.putExtra("token", token);
                                intent.putExtra("kakaoId", kakaoid);

                                context.startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("[알림체크]","실패"+t.getStackTrace().toString());
                        }
                    });



                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}

