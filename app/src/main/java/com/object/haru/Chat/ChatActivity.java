package com.object.haru.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.object.haru.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // views from xml
    private RecyclerView recyclerView;
    private Button back_btn, send_btn;
    private EditText message_edit;
    private TextView name_text;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<ModelChat> chatList;
    private AdapterChat adapterChat;
    private String hisUid, myUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);


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
        hisUid = intent.getStringExtra("userId");


        // firebase auth
        //Firebase Authentication 인스턴스를 가져옵니다. 이 인스턴스를 사용하여 현재 로그인된 사용자의 정보를 가져옴
        firebaseAuth = FirebaseAuth.getInstance();

        //Firebase 데이터베이스에서 데이터를 읽고 쓸 수 있는 실시간 데이터베이스 인스턴스를 가져옴
        firebaseDatabase = FirebaseDatabase.getInstance();

        //"trip" 데이터베이스의 "UserAccount" 노드에 대한 참조를 가져옵니다.
        databaseReference = firebaseDatabase.getReference("trip").child("UserAccount");

        //"idToken" 필드가 현재 사용자의 UID와 같은 데이터를 가져오기 위한 쿼리를 생성
        Query userQuery = databaseReference.orderByChild("idToken").equalTo(hisUid);
        // get user name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 쿼리가 실행되면 현재 자식 노드에서 "name" 필드 값을 가져와서 저장하는 로직
                for (DataSnapshot ds: snapshot.getChildren()){
                    // get data
                    String name = ds.child("name").getValue().toString();


                    // set data
                    name_text.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                    // text not empty
                    sendMessage(message);
                    recyclerView.scrollToPosition(chatList.size()-1);
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
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    if (modelChat.getReceiver().equals(myUid) && modelChat.getSender().equals(hisUid) ||
                            modelChat.getReceiver().equals(hisUid) && modelChat.getSender().equals(myUid)) {
                        chatList.add(modelChat);

                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList);
                    adapterChat.notifyDataSetChanged();

                    //set adapter to recyclerview
                    recyclerView.setAdapter(adapterChat);
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

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null) {
            myUid = user.getUid();  // currently signed in user's uid

        } else {
           // startActivity(new Intent(this,  LobbyActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart() {
        checkUserStatus();;
        super.onStart();
    }
}
