package com.object.haru.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.MainData;
import com.object.haru.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private ArrayList<MainData> arrayList;

    public CustomAdapter(ArrayList<MainData> arrayList) { this.arrayList = arrayList; }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        holder.profile.setImageResource(arrayList.get(position).getProfile());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.category.setText(arrayList.get(position).getCategory());
        holder.time.setText(arrayList.get(position).getTime());
        holder.money.setText(arrayList.get(position).getMoney());
        holder.location.setText(arrayList.get(position).getLocation());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView profile;
        protected TextView title, category, time, money, location;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.text_title);
            this.category = itemView.findViewById(R.id.text_category);
            this.time = itemView.findViewById(R.id.text_time);
            this.money = itemView.findViewById(R.id.text_money);
            this.location = itemView.findViewById(R.id.text_location);

        }
    }
}
