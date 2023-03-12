package com.object.haru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.object.haru.Activity.ProfileActivity;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.R;

import java.util.ArrayList;

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.CustomViewHolder>{


    private ArrayList<ApplyDTO> arrayList;

    public ApplyAdapter(ArrayList<ApplyDTO> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicant_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        //각 아이템 매칭
//        Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);
        //서버로 부터 이미지를 받아와 bindview 될때 삽입시켜줌
        holder.applicant_username.setText(arrayList.get(position).getUserName());
        holder.applicant_rating.setText("평점 : "+arrayList.get(position).getAvgRating());

        ApplyDTO applyDTO = arrayList.get(position);

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
        TextView applicant_username,applicant_rating;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.applicant_userImg = itemView.findViewById(R.id.applicant_userImg);
            this.applicant_username = itemView.findViewById(R.id.applicant_username);
            this.applicant_rating = itemView.findViewById(R.id.applicant_rating);


        }

    }
}