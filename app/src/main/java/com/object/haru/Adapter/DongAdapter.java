package com.object.haru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;
import com.object.haru.UpdateListener;

import java.util.ArrayList;

public class DongAdapter extends RecyclerView.Adapter<DongAdapter.CustomViewHolder> {

    ArrayList<String> list;
    public DongAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DongAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dong_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DongAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_dong);
        }
    }
}
