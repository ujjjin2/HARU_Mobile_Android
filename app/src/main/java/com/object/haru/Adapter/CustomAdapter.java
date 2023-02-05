package com.object.haru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.MainData;
import com.object.haru.R;


import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private List<RecruitDTO> arrayList;
    private Context context;

    public CustomAdapter(Context context, List<RecruitDTO> arrayList) {
        this.arrayList = arrayList; this.context=context;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
//      Glide.with(holder.itemView).load(arrayList.get(position).getImgURl()).into(holder.iv_img);

        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getTitle());
        holder.time.setText(arrayList.get(position).getTitle());
        holder.money.setText(arrayList.get(position).getTitle());
        holder.location.setText(arrayList.get(position).getTitle());

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

        }
    }
}
