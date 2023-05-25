package com.object.haru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Activity.DetailActivity;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.zzimRequestDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitAdapter extends RecyclerView.Adapter<RecruitAdapter.CustomViewHolder>{

    private ArrayList<RecruitDTO> items;
    private List<RecruitDTO> arrayList;
    private RecruitDTO recruitDTO;
    private Context context;

    private LocalDate rTime;

    private String token;

    private Map<Long, Boolean> zzimMap = new HashMap<>();

    private Long kakaoId;
    private RecruitDTO recruitDTO1;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecruitAdapter.CustomViewHolder holder, int position) {
        // Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);

        recruitDTO1 = arrayList.get(position);
        String timeAgo = getTimeAgo(recruitDTO1.getRtime());
        boolean isZzim = recruitDTO1.isZzim();

        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getSubject());
        holder.time.setText(formatTime(recruitDTO1.getStTime(), recruitDTO1.getEndTime()));
        holder.money.setText(arrayList.get(position).getPay().toString() + "원");
        holder.location.setText(arrayList.get(position).getAddr());
        holder.rTime.setText(timeAgo);

        hideZzimButtonIfOwnedByUser(holder.zzim, recruitDTO1.getKakaoid());
        setZzimState(holder.zzim, isZzim);
        setZzimButtonClickListener(holder.zzim, recruitDTO1);
    }

    private void setZzimButtonClickListener(ImageView zzimButton, RecruitDTO recruitDTO) {
        zzimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleZzimButtonClick(zzimButton, recruitDTO);
            }
        });
    }

    private void handleZzimButtonClick(ImageView zzimButton, RecruitDTO recruitDTO) {
        boolean isZzim = recruitDTO.isZzim();

        if (isZzim) {
            Log.d("찜버튼 클릭해제", "클릭해제");
            deleteZzim(recruitDTO.getRid(), new Callback<zzimRequestDTO>() {
                @Override
                public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                    zzimRequestDTO zzimRequestDTO = response.body();
                    setZzimState(zzimButton, false);
                    recruitDTO.setZzim(false);
                }

                @Override
                public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Log.d("찜버튼 클릭", "클릭");
            saveZzim(recruitDTO.getRid(), new Callback<zzimRequestDTO>() {
                @Override
                public void onResponse(Call<zzimRequestDTO> call, Response<zzimRequestDTO> response) {
                    zzimRequestDTO zzimRequestDTO = response.body();
                    setZzimState(zzimButton, true);
                    recruitDTO.setZzim(true);
                }

                @Override
                public void onFailure(Call<zzimRequestDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void deleteZzim(Long rid, Callback<zzimRequestDTO> callback) {
        Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimDelete(token, kakaoId, rid);
        call.enqueue(callback);
    }

    private void saveZzim(Long rid, Callback<zzimRequestDTO> callback) {
        zzimRequestDTO zzimRequestDTO = new zzimRequestDTO(rid, kakaoId);
        Call<zzimRequestDTO> call = RetrofitClientInstance.getApiService().zzimSave(token, zzimRequestDTO);
        call.enqueue(callback);
    }

    private void setZzimState(ImageView zzim, boolean isZzimChecked) {
        if (isZzimChecked) {
            zzim.setImageResource(R.drawable.starcolor);
        } else {
            zzim.setImageResource(R.drawable.zzim);
        }
    }

    private void hideZzimButtonIfOwnedByUser(ImageView zzimButton, long ownerId) {
        if (ownerId == kakaoId) {
            zzimButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView title, category, time, money, location, rTime;
        private ImageView zzim;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
//          this.profile = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.text_title);
            this.category = itemView.findViewById(R.id.text_category);
            this.time = itemView.findViewById(R.id.text_time);
            this.money = itemView.findViewById(R.id.text_money);
            this.location = itemView.findViewById(R.id.text_location);
            this.rTime = itemView.findViewById(R.id.rTime);
            this.zzim = itemView.findViewById(R.id.starIcon);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String formatTime(String startTime, String endTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime stTime = LocalDateTime.parse(startTime, inputFormatter);
        LocalDateTime enTime = LocalDateTime.parse(endTime, inputFormatter);

        String formattedStTime = stTime.format(outputFormatter);
        String formattedEnTime = enTime.format(outputFormatter);

        return formattedStTime + " ~ " + formattedEnTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTimeAgo(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime pastTime = LocalDateTime.parse(date, formatter);
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(pastTime, currentTime);

        if (duration.getSeconds() < 0) {
            return "방금 전";
        } else if (duration.getSeconds() > 0 && duration.getSeconds() < 60) {
            return duration.getSeconds() + "초 전";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else {
            return duration.toDays() + "일 전";
        }
    }
}
