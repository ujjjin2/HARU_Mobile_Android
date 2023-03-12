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

import com.object.haru.Activity.ProfileActivity;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.R;

import java.util.ArrayList;


public class WritedAdapter extends RecyclerView.Adapter<WritedAdapter.CustomViewHolder>{


    private ArrayList<RecruitDTO> arrayList;

    public WritedAdapter(ArrayList<RecruitDTO> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apply_writed_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        //각 아이템 매칭
//        Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);
        //서버로 부터 이미지를 받아와 bindview 될때 삽입시켜줌
        holder.apply_writed_title.setText(arrayList.get(position).getTitle());

        RecruitDTO recruitDTO = arrayList.get(position);

    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0 );
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView apply_writed_title;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.apply_writed_title = itemView.findViewById(R.id.apply_writed_title);


        }

    }
}