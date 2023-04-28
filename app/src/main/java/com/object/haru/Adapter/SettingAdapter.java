package com.object.haru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.DTO.TestDTO;
import com.object.haru.R;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {


    private ArrayList<String> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.setting_item_title);
        }
    }

    public SettingAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

