package com.object.haru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.Activity.ApplyDetailActivity;
import com.object.haru.Activity.ProfileActivity;
import com.object.haru.DTO.NoticeDTO;
import com.object.haru.DTO.TestDTO;
import com.object.haru.R;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.CustomViewHolder> {
//설정의 공지사항 어댑터

    private ArrayList<NoticeDTO> list;
    private Context context;
    private String token;

    private NoticeDTO noticeDTO;

    public SettingAdapter(ArrayList<NoticeDTO> list, String token){
        this.list = list;
        this.token = token;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView applicant_nid,applicant_ncontents;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.applicant_nid = itemView.findViewById(R.id.nid);
            this.applicant_ncontents = itemView.findViewById(R.id.ncontents);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    noticeDTO = list.get(position);

                    Intent intent = new Intent(context, ApplyDetailActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("nid", noticeDTO.getNid());
                    intent.putExtra("ncontents", noticeDTO.getNcontents());
                    Log.d("[입력 성공]", "=============");

                    context.startActivity(intent);
                }
            });
        }

    }


    @NonNull
    @Override
    public SettingAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notice, parent, false);
        context = parent.getContext();
        SettingAdapter.CustomViewHolder holder = new SettingAdapter.CustomViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.CustomViewHolder holder, int position) {
        //서버로 부터 받아와 bindview 될때 삽입시켜줌
        holder.applicant_nid.setText(list.get(position).getNid().toString());
        holder.applicant_ncontents.setText(list.get(position).getNcontents());
        //아이템 클릭 이벤트 일단 비워둠
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0 );
    }





}

