package com.object.haru.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.object.haru.DTO.NoticeDTO;
import com.object.haru.R;


import java.util.ArrayList;
import java.util.List;


public class MyNoticeRecyclerViewAdapter extends RecyclerView.Adapter<MyNoticeRecyclerViewAdapter.CustomViewHolder> {

    private List<NoticeDTO> notices;

    public MyNoticeRecyclerViewAdapter(ArrayList<NoticeDTO> notices) {
        this.notices = notices;
    }


    @NonNull
    @Override
    public MyNoticeRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notice_list
                ,parent, false);
        MyNoticeRecyclerViewAdapter.CustomViewHolder holder = new MyNoticeRecyclerViewAdapter.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( MyNoticeRecyclerViewAdapter.CustomViewHolder holder, int position) {
        holder.nid.setText(notices.get(position).getNid().toString());
        holder.ncontents.setText(notices.get(position).getNcontents());
    }

    @Override
    public int getItemCount() {
        return (null != notices ? notices.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView nid, ncontents;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nid = itemView.findViewById(R.id.nid);
            this.ncontents = itemView.findViewById(R.id.ncontents);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAbsoluteAdapterPosition();
//
//                    recruitDTO = arrayList.get(position);
//
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra("rId", recruitDTO.getRid());
//                    intent.putExtra("token", token);
//                    intent.putExtra("kakaoId", kakaoId);
//
//                    context.startActivity(intent);
//                }
//            });

        }
    }

}