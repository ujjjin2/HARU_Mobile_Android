package com.object.haru.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.object.haru.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.holder>{

    private Context context;
    private String token;
    private Long kakaoid;
    private int  chatCount = 0; // chatCount -> 읽지 않은 채팅 갯수
    private List<UserAccountDTO> userList; // 채팅 목록에 표시할 유저 정보를 저장하는 리스트
    private HashMap<String, String> messageMap; //채팅 목록에서 각 유저의 마지막 메시지를 저장하는 맵
    private HashMap<String, String> confirmMap; //채팅 목록에서 각 유저의 마지막 읽음체크를 저장하는 맵
    private HashMap<String, String> senderMap; //채팅 목록에서 각 유저의 마지막 읽음체크를 저장하는 맵
    private HashMap<String, String> timeMap; //채팅 목록에서 각 유저의 마지막 메시지 시간을 저장하는 맵
    // constructor



    public AdapterChatlist(Context context, List<UserAccountDTO> userList, String token, Long kakaoid) {
        this.context = context;
        this.userList = userList;
        messageMap = new HashMap<>();
        timeMap = new HashMap<>();
        senderMap = new HashMap<>();
        confirmMap = new HashMap<>();
        this.token = token;
        this.kakaoid = kakaoid;
    }

    @NonNull
    @Override// ViewHolder 객체 생성 시 호출되는 메소드. 채팅 목록에 사용될 레이아웃 파일을 inflate하고,
    // 해당 레이아웃 파일에서 정의한 뷰들을 저장하는 ViewHolder 객체를 생성하여 리턴한다.
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        holder h = new holder(view);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        // get data
        String hisUid = userList.get(holder.getAbsoluteAdapterPosition()).getIdToken();
        String userName = userList.get(holder.getAbsoluteAdapterPosition()).getName();
        String message = messageMap.get(hisUid);
        String timestamp = timeMap.get(hisUid);
        String confirm = confirmMap.get(hisUid);
        String sender = senderMap.get(hisUid);

        SharedPreferences auto = context.getSharedPreferences("chatCount", Activity.MODE_PRIVATE);
        SharedPreferences.Editor chat = auto.edit();


        holder.name_text.setText(userName);

        if (message == null || message.equals("default")) {
            holder.message_text.setVisibility(View.GONE);
            holder.time_text.setVisibility(View.GONE);
        } else {
            if (confirm != null && confirm.equals("미확인") && sender != null && sender.equals(hisUid)) {
                chatCount++; //읽지 않은 채팅의 수를 +1 해줌
                holder.itemView.setBackgroundResource(R.drawable.item_border); // 알림 확인이 1인 경우 초록색 배경
            } else {
                // 기본 배경색

                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            // if문 안에 넣어줘야 실행 됨...아니면 numberformatException...
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(timestamp));
            // String time = (String) DateFormat.format("MM/dd hh:mm", calendar);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd a h:mm ", Locale.getDefault());
            String time = dateFormat.format(calendar.getTime());

            holder.message_text.setVisibility(View.VISIBLE);
            holder.message_text.setText(message);
            holder.time_text.setVisibility(View.VISIBLE);
            holder.time_text.setText(time);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start chat activity with that user
                int adapterPosition = holder.getAbsoluteAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    chatCount--;
                    chat.putInt("chatCount", chatCount);
                    chat.commit();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("idToken", hisUid);
                    intent.putExtra("kakaoid", kakaoid.toString()); //상대방 kakaoid
                    intent.putExtra("Fridkakaoid", userList.get(adapterPosition).getKakaoid().toString()); //상대방 kakaoid
                    intent.putExtra("Fridname", userList.get(adapterPosition).getName()); //상대방 kakaoid
                    intent.putExtra("token", token); // 전달받은 token 값 전달
                    context.startActivity(intent);
                }
            }
        });


        chat.putInt("chatCount", chatCount);
        chat.commit();

    }



    //messageMap에 userId와 message를 저장하는 메소드
    public void setLastMessageMap(String userId, String message) {
        messageMap.put(userId, message);
    }

    public void setLastConfirmMap(String userId, String confirm) {
        confirmMap.put(userId, confirm);
    }
    public void setLastsenderMap(String userId, String sender) {
        senderMap.put(userId, sender);
    }


    // timeMap에 userId와 time을 저장하는 메소드
    public void setLastTimeMap(String userId, String time){
        timeMap.put(userId, time);
    }

//RecyclerView에 표시할 아이템의 개수를 리턴하는 메소드
    @Override
    public int getItemCount() {
        return userList.size(); // size of the list
    }

    //RecyclerView에서 사용될 뷰 홀더 클래스. 채팅 목록에 표시할 뷰들을 초기화한다.
    class holder extends RecyclerView.ViewHolder {
        // views of row chatlist.xml
        TextView name_text, message_text, time_text;


        public holder(@NonNull View itemView) {
            super(itemView);
            this.name_text = itemView.findViewById(R.id.name_text);
            this.message_text = itemView.findViewById(R.id.message_text);
            this.time_text = itemView.findViewById(R.id.time_text);


        }

    }

}
