/*
package com.object.haru.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;

// NotificationListAdapter.java
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private List<AlarmDTO> notificationList;

    public NotificationListAdapter(List<AlarmDTO> notificationList) {
        this.notificationList = notificationList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView messageTextView;
        private TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notification_title);
            messageTextView = itemView.findViewById(R.id.notification_message);
            timeTextView = itemView.findViewById(R.id.notification_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmDTO notification = notificationList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.timeTextView.setText(notification.getTime());

        // 클릭 시 글씨 색상 변경
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.titleTextView.setTextColor(Color.parseColor("#AAAAAA"));
                holder.messageTextView.setTextColor(Color.parseColor("#AAAAAA"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

*/
