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


import java.util.ArrayList;
import java.util.List;

public class RecruitAdapter extends RecyclerView.Adapter<RecruitAdapter.CustomViewHolder>{

    private ArrayList<RecruitDTO> items;
    private List<RecruitDTO> arrayList;
    private RecruitDTO recruitDTO;
    private Context context;

    private String token;

    public RecruitAdapter(List<RecruitDTO> arrayList, Context context,String token) {
        this.arrayList = arrayList;
        this.context = context;
        this.token = token;
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
//      Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);

        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getSubject());
        holder.time.setText(arrayList.get(position).getStTime() + "~" + arrayList.get(position).getEndTime());
        holder.money.setText(arrayList.get(position).getPay().toString() + "원");
        holder.location.setText(arrayList.get(position).getAddr());

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView title, category, time, money, location;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
//          this.profile = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.text_title);
            this.category = itemView.findViewById(R.id.text_category);
            this.time = itemView.findViewById(R.id.text_time);
            this.money = itemView.findViewById(R.id.text_money);
            this.location = itemView.findViewById(R.id.text_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();

                    recruitDTO = arrayList.get(position);

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("rId", recruitDTO.getRid().toString());
                    intent.putExtra("token", token);

                    context.startActivity(intent);
                }
            });

        }
    }

}
