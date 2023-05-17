package com.object.haru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Activity.DetailActivity;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecruitAdapter extends RecyclerView.Adapter<RecruitAdapter.CustomViewHolder>{

    private ArrayList<RecruitDTO> items;
    private List<RecruitDTO> arrayList;
    private RecruitDTO recruitDTO;
    private Context context;

    private LocalDate rTime;

    private String token;

    private Long kakaoId;

    public RecruitAdapter(List<RecruitDTO> arrayList, Context context, String token, Long kakaoid) {
        this.arrayList = arrayList;
        this.context = context;
        this.token = token;
        this.kakaoId = kakaoid;
    }

    @NonNull
    @Override
    public RecruitAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitAdapter.CustomViewHolder holder, int position) {
        // Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);

        String timeAgo;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String rTime = arrayList.get(position).getRtime();
            Log.d("rtime", rTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            LocalDateTime rDateTime = LocalDateTime.parse(rTime, formatter);


            Duration duration = Duration.between(rDateTime, LocalDateTime.now());
            long hoursDiff = duration.toHours();

            if (hoursDiff < 1) {
                timeAgo = "방금 전";
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

        String stTime = arrayList.get(position).getStTime();
        String endTime = arrayList.get(position).getEndTime();
        String formattedStTime = stTime.substring(0, stTime.length() - 3); // ":00" 제거
        String formattedEndTime = endTime.substring(0, endTime.length() - 3); // ":00" 제거

        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getSubject());
        holder.time.setText(formattedStTime + " ~ " + formattedEndTime);
        holder.money.setText(arrayList.get(position).getPay().toString() + "원");
        holder.location.setText(arrayList.get(position).getAddr());

        holder.rTime.setText(timeAgo);
    }



    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView title, category, time, money, location, rTime;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
//          this.profile = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.text_title);
            this.category = itemView.findViewById(R.id.text_category);
            this.time = itemView.findViewById(R.id.text_time);
            this.money = itemView.findViewById(R.id.text_money);
            this.location = itemView.findViewById(R.id.text_location);
            this.rTime = itemView.findViewById(R.id.rTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();

                    recruitDTO = arrayList.get(position);

                    Intent intent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        intent = new Intent(context, DetailActivity.class);
                    }
                    intent.putExtra("rId", recruitDTO.getRid());
                    intent.putExtra("token", token);
                    intent.putExtra("kakaoId", kakaoId);

                    context.startActivity(intent);
                }
            });

        }
    }

}
