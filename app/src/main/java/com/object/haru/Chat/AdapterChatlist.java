package com.object.haru.Chat;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private List<UserAccountDTO> userList; // 채팅 목록에 표시할 유저 정보를 저장하는 리스트
    private HashMap<String, String> messageMap; //채팅 목록에서 각 유저의 마지막 메시지를 저장하는 맵
    private HashMap<String, String> timeMap; //채팅 목록에서 각 유저의 마지막 메시지 시간을 저장하는 맵
    // constructor


    //생성자 context와 userList를 받아 초기화한다.
/*    public AdapterChatlist(Context context, List<UserAccountDTO> userList) {
        this.context = context;
        this.userList = userList;
        messageMap = new HashMap<>();
        timeMap = new HashMap<>();
    }*/

    public AdapterChatlist(Context context, List<UserAccountDTO> userList, String token, Long kakaoid) {
        this.context = context;
        this.userList = userList;
        messageMap = new HashMap<>();
        timeMap = new HashMap<>();
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

    @Override // ViewHolder 객체를 RecyclerView의 각 position에 바인딩해주는 메소드. position에 해당하는 유저의 정보를 userList에서 가져온다.
    // 이후 해당 유저의 마지막 메시지와 시간 정보를 messageMap과 timeMap에서 가져온다. 그리고 해당 뷰 홀더에 정보를 뿌려준다.
    public void onBindViewHolder(@NonNull holder holder, int position) {
        // get data
        String hisUid = userList.get(position).getIdToken();
        String userName = userList.get(position).getName();
        String message = messageMap.get(hisUid);
        String timestamp = timeMap.get(hisUid);


        holder.name_text.setText(userName);

        if (message == null || message.equals("default")) {
            holder.message_text.setVisibility(View.GONE);
            holder.time_text.setVisibility(View.GONE);
        } else {
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
              //  Log.d("kakaoid : ", kakaoid.toString());
                Log.d("token : ", token);
                // start chat activity with that user
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("idToken", hisUid);
                intent.putExtra("kakaoid", kakaoid); //상대방 kakaoid
                intent.putExtra("token", token); // 전달받은 token 값 전달
                context.startActivity(intent);
            }
        });
    }

    //messageMap에 userId와 message를 저장하는 메소드
    public void setLastMessageMap(String userId, String message) {
        messageMap.put(userId, message);
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
