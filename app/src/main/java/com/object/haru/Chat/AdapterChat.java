package com.object.haru.Chat;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.object.haru.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.holder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<ChatDTO> chatDTO;
    FirebaseUser firebaseUser; //현재 로그인 사용자

    public AdapterChat(Context context, List<ChatDTO> chatDTO) {
        this.context = context;
        this.chatDTO = chatDTO;
    }
//1. 어댑터 가져와서 적용시키기
    @NonNull
    @Override //메시지 뷰를 만들고, 뷰 홀더를 생성하여 반환합니다. + 어떤 layout을 쓸건지 정의
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new holder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new holder(view);
        }
    }

    @Override  //뷰 홀더의 데이터와 UI를 연결합니다. 즉 데이터 세팅
    public void onBindViewHolder(@NonNull holder holder, int position) {
        // get data
        String message = chatDTO.get(position).getMessage();
        String timestamp = chatDTO.get(position).getTimestamp();

        // convert time stamp to dd/mm/yyyy hh:mm am/pm

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String time = (String) DateFormat.format("MM/dd hh:mm", calendar);

        // set data

        holder.message_text.setText(message);
        holder.time_text.setText(time);

    }

    @Override //채팅 내용 리스트의 크기를 반환
    public int getItemCount() {
        return chatDTO.size();
    }

    @Override // 채팅 메시지를 왼쪽/오른쪽으로 정렬할지 결정합니다. 메시지의 송신자가 현재 로그인한 사용자인 경우 오른쪽 정렬하고, 그렇지 않으면 왼쪽 정렬합니다.
    public int getItemViewType(int position) {
        // get currently signed in user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatDTO.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    // view holder class

    class holder extends RecyclerView.ViewHolder{

        // view
        TextView message_text, time_text;
        public holder(@NonNull View itemView) {
            super(itemView);

            // init views
            message_text = itemView.findViewById(R.id.myMessage);
            time_text = itemView.findViewById(R.id.time);
        }
    }
}
