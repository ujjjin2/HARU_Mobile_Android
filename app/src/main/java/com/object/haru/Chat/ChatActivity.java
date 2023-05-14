package com.object.haru.Chat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.object.haru.DTO.FcmSendDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    // views from xml
    private RecyclerView recyclerView;
    private Button back_btn, send_btn;
    private EditText message_edit;
    private TextView name_text;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<ChatDTO> chatList;
    private AdapterChat adapterChat;
    private String hisUid, myUid, kakao, Fridname;
    private String myName, token, uid;
  //  private  Query myQuery,userQuery;

    private Long Fridkakaoid,kakaoid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("채팅액티비티", "채팅시작");
        setContentView(R.layout.chat);
        onCheck();

        // init views
        back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.recyclerView);
        send_btn = findViewById(R.id.sendBtn);
        message_edit = findViewById(R.id.message_edit);
        name_text = findViewById(R.id.user_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        Intent intent = getIntent();

        hisUid = intent.getStringExtra("idToken");
        Fridname = intent.getStringExtra("Fridname");
        name_text.setText(Fridname);
        kakaoid = Long.parseLong(intent.getStringExtra("kakaoid")); //나의 kakaoid
        Fridkakaoid = Long.parseLong(intent.getStringExtra("Fridkakaoid")); //상대방 kakaoid
        token = intent.getStringExtra("token");

        Log.d("채팅시작 kakaoid", kakaoid.toString());  //확인완료 --> chat에서 넘겨주는거랑 맞추기 0511 0239
        Log.d("채팅시작 Fridkakaoid", Fridkakaoid.toString()); //확인완료 --> chat에서 넘겨주는거 확인하기



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get text from edit text
                String message = message_edit.getText().toString().trim();
                // check if text is empty or not
                if (TextUtils.isEmpty(message)){
                    // text empty
                    Toast.makeText(ChatActivity.this,"메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    FcmSendDTO fcmSendDTO = new FcmSendDTO(Fridkakaoid, myName, message, "chat");
                    Call<FcmSendDTO> fcmSend = RetrofitClientInstance.getApiService().fcm_send(token,fcmSendDTO);
                    fcmSend.enqueue(new Callback<FcmSendDTO>() {
                        @Override
                        public void onResponse(Call<FcmSendDTO> call, Response<FcmSendDTO> response) {
                            Log.d("메세지 전송 알림 성공 : " ,"[성공]");
                            sendMessage(message);
                           // recyclerView.scrollToPosition(chatList.size()-1);
                        }

                        @Override
                        public void onFailure(Call<FcmSendDTO> call, Throwable t) {
                            Log.d("메세지 전송 알림 실패 : " ,"[실패]");
                        }
                    });
                }
            }
        });

        readMessages();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ViewTreeObserver viewTreeObserver =recyclerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                if (heightDiff > 500) {
                    // Keyboard is shown
                    if (adapterChat != null && adapterChat.getItemCount() > 0) {
                        recyclerView.smoothScrollToPosition(adapterChat.getItemCount() - 1);
                    } else {
                        recyclerView.scrollToPosition(0);
                    }
                } else {
                    // Keyboard is hidden
                    if (adapterChat != null && adapterChat.getItemCount() > 0) {
                        recyclerView.smoothScrollToPosition(adapterChat.getItemCount() - 1);
                    } else {
                        recyclerView.scrollToPosition(0);
                    }
                }
            }
        });

    }
    

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ChatDTO chatDTO = ds.getValue(ChatDTO.class);

                    if (chatDTO.getReceiver() != null && chatDTO.getSender() != null) {
                        if (chatDTO.getReceiver().equals(myUid) && chatDTO.getSender().equals(hisUid) ||
                                chatDTO.getReceiver().equals(hisUid) && chatDTO.getSender().equals(myUid)) {
                            chatList.add(chatDTO);
                        }
                    }else{
                        Log.d("chatDTO 널", " chatDTO 널");
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList);
                    adapterChat.notifyDataSetChanged();
                    //set adapter to recyclerview
                    recyclerView.setAdapter(adapterChat);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (adapterChat != null && adapterChat.getItemCount() > 0) {
                                recyclerView.smoothScrollToPosition(adapterChat.getItemCount() - 1);
                            } else {
                                recyclerView.scrollToPosition(0);
                            }

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        Log.d("센더에서 hisuid ", hisUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        databaseReference.child("Chats").push().setValue(hashMap);

        // reset edittext after sending message

        message_edit.setText("");

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid).child(hisUid);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    chatRef.child("id").setValue(hisUid);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(hisUid).child(myUid);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    public void onCheck(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();

        //Firebase 데이터베이스에서 데이터를 읽고 쓸 수 있는 실시간 데이터베이스 인스턴스를 가져옴
        firebaseDatabase = FirebaseDatabase.getInstance();

        //"trip" 데이터베이스의 "UserAccount" 노드에 대한 참조를 가져옵니다.
        databaseReference = firebaseDatabase.getReference("userAccount");
        //"idToken" 필드가 현재 사용자의 UID와 같은 데이터를 가져오기 위한 쿼리를 생성
        Query userQuery = databaseReference.orderByChild("name").equalTo(Fridname);

        Query myQuery = databaseReference.orderByChild("idToken").equalTo(uid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 쿼리가 실행되면 현재 자식 노드에서 "name" 필드 값을 가져와서 저장하는 로직
                for (DataSnapshot ds: snapshot.getChildren()){
                    // get data
                    hisUid = ds.child("idToken").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("디비에러", "데이터베이스 에러");

            }
        });

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("myQuery 이름", "시작");
                // 쿼리가 실행되면 현재 자식 노드에서 "name" 필드 값을 가져와서 저장하는 로직
                for (DataSnapshot ds: snapshot.getChildren()){
                    // get data
                    myName = ds.child("name").getValue().toString();
                    Log.d("내 이름", myName);
                    if(myName == null){
                        myName = "새로운 메세지";
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("디비에러", "데이터베이스 에러");
            }
        });

    }

}
