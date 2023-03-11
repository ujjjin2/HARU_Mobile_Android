package com.object.haru.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;
import com.object.haru.UpdateDongListener;

import java.util.ArrayList;

public class SiAdapter extends RecyclerView.Adapter<SiAdapter.CustomViewHolder> {


    private int selectedItemPosition = -1;

    ArrayList<String> list;

    ArrayList<String> dongArrayList = new ArrayList<>();
    UpdateDongListener listener;


    public SiAdapter(ArrayList<String> list, UpdateDongListener listener) {
        this.listener = listener;
        this.list = list;
    }


    @NonNull
    @Override
    public SiAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.si_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiAdapter.CustomViewHolder holder, final int position) {

        final int item = position;

        holder.textView.setText(list.get(position));

        if (position == selectedItemPosition) {
            holder.textView.setTextColor(Color.BLUE);
            holder.layout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        } else {
            holder.textView.setTextColor(Color.BLACK);
            holder.layout.setBackgroundColor(Color.WHITE);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String si = holder.textView.getText().toString();
                notifyDataSetChanged();
                callItems(si);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout layout;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_si);
            layout = itemView.findViewById(R.id.layout_si);
        }
    }

    public void callItems(String position) {
        if (dongArrayList.size() > 0) {
            dongArrayList.clear();
        }

        if (position.equals("강남구")) {
            dongArrayList.add("전체");
            dongArrayList.add("개포1동");
            dongArrayList.add("개포2동");
            dongArrayList.add("개포4동");
            dongArrayList.add("개포동");
            dongArrayList.add("논현1동");
            dongArrayList.add("논현2동");
            dongArrayList.add("논현동");
            dongArrayList.add("대치1동");
            dongArrayList.add("대치2동");
            dongArrayList.add("대치4동");
            dongArrayList.add("대치동");
            dongArrayList.add("도곡1동");
            dongArrayList.add("도곡2동");
            dongArrayList.add("도곡동");
            dongArrayList.add("삼성1동");
            dongArrayList.add("삼성2동");
            dongArrayList.add("삼성동");
            dongArrayList.add("세곡동");
            dongArrayList.add("수서동");
            dongArrayList.add("신사동");
            dongArrayList.add("압구정동");
            dongArrayList.add("역삼1동");
            dongArrayList.add("역삼2동");
            dongArrayList.add("역삼동");
            dongArrayList.add("율현동");
            dongArrayList.add("일원1동");
            dongArrayList.add("일원2동");
            dongArrayList.add("일원동");
            dongArrayList.add("일원본동");
            dongArrayList.add("자곡동");
            dongArrayList.add("청담동");
        }

        if (listener != null) {
            listener.onUpdate(dongArrayList);
        }

    }
}
