package com.object.haru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.object.haru.Activity.ApplyDetailActivity;
import com.object.haru.Activity.ProfileActivity;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.R;

import java.util.ArrayList;

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.CustomViewHolder>{

//    지원자 리스트 Adapter
    private ArrayList<ApplyDTO> arrayList;
    private Context context;
    private String token;
    private Long kakaoid;

    private ApplyDTO applyDTO;
    private Long confirmedPerson;

    public ApplyAdapter(ArrayList<ApplyDTO> arrayList, String token, Long kakaoid, Long confirmedPerson) {
        this.arrayList = arrayList;
        this.token = token;
        this.kakaoid = kakaoid;
        this.confirmedPerson = confirmedPerson;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicant_item,parent,false);
        context = parent.getContext();
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        //각 아이템 매칭
//        Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);
        //서버로 부터 이미지를 받아와 bindview 될때 삽입시켜줌
        holder.applicant_username.setText(arrayList.get(position).getUserName());
        if (arrayList.get(position).getAvgRating() == null) {
            holder.applicant_rating.setText("0.0");
        } else {
            holder.applicant_rating.setText(arrayList.get(position).getAvgRating().toString());
        }

        if (String.valueOf(confirmedPerson).equals(String.valueOf(arrayList.get(position).getKakaoid()))) {
            holder.confirmedSignal.setVisibility(View.VISIBLE);
        }
        holder.applicant_self_introduce.setText(arrayList.get(position).getMyself());
        holder.applicant_sex.setText(arrayList.get(position).getAsex());

        //아이템 클릭 이벤트
        holder.applicant_userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mPostion = holder.getAdapterPosition();
                Context context = view.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
                //ProfileActivity 전송
            }
        });
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0 );
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView applicant_userImg;
        TextView applicant_username,applicant_rating, applicant_self_introduce, applicant_sex;

        Button confirmedSignal;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.applicant_userImg = itemView.findViewById(R.id.applicant_userImg);
            this.applicant_username = itemView.findViewById(R.id.applicant_username);
            this.applicant_rating = itemView.findViewById(R.id.applicant_rating);
            this.applicant_sex = itemView.findViewById(R.id.applicant_sex);
            this.applicant_self_introduce = itemView.findViewById(R.id.applicant_self_introduce);
            this.confirmedSignal = itemView.findViewById(R.id.confirm_button);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    applyDTO = arrayList.get(position);

                    Intent intent = new Intent(context, ApplyDetailActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("aid", applyDTO.getAid());
                    intent.putExtra("age", applyDTO.getAage());
                    intent.putExtra("self", applyDTO.getMyself());
                    intent.putExtra("sex", applyDTO.getAsex());
                    intent.putExtra("career", applyDTO.getAcareer());
                    intent.putExtra("name", applyDTO.getUserName());
                    intent.putExtra("rating", applyDTO.getAvgRating());
                    intent.putExtra("rId", applyDTO.getRid());
                    intent.putExtra("Fridkakaoid", applyDTO.getUid()); //상대방거
                    intent.putExtra("kakaoid", kakaoid.toString());

                    context.startActivity(intent);
                }
            });
        }

    }
}