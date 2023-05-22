package com.object.haru.Chat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.IOException;
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
    private  Intent intent;
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


        intent = getIntent();

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
                    sendMessage(message);

                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                            .child(hisUid).child(myUid);
                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {

                            } else {
                                int confirmValue = snapshot.child("confirm").getValue(Integer.class);
                                if (confirmValue == 0) {
                                    // confirm 값이 0인 경우 - 화면 보고 있는 상대
                                } else {
                                    // confirm 값이 1인 경우 - 화면 보고있지 않은 상대
                                    new FcmSendTask().execute(message); // 알림 전송
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error if needed
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
    // onCreate END ============================================================================================



    // readMessages start ============================================================================================
    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatDTO chatDTO = ds.getValue(ChatDTO.class);

                    if (chatDTO.getReceiver() != null && chatDTO.getSender() != null) {
                        if (chatDTO.getReceiver().equals(myUid) && chatDTO.getSender().equals(hisUid) ||
                                chatDTO.getReceiver().equals(hisUid) && chatDTO.getSender().equals(myUid)) {
                            chatList.add(chatDTO);

                            // 확인 처리
                            if (chatDTO.getConfirm().equals("미확인") && chatDTO.getReceiver().equals(myUid)) {
                                Log.d("미확인 처리", "성공");
                                ds.getRef().child("confirm").setValue("확인");
                            }
                        }
                    } else {
                        Log.d("chatDTO 널", " 실패 ");
                    }
                }

                adapterChat = new AdapterChat(ChatActivity.this, chatList);
                recyclerView.setAdapter(adapterChat);
                adapterChat.notifyDataSetChanged();

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 처리 중단 시 동작
            }
        });
    }



// send start ============================================================================================
    private void sendMessage(String message) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("confirm", "미확인");
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
                    chatRef.child("confirm").setValue(1);

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
                    chatRef2.child("confirm").setValue(1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    // onStart start ============================================================================================
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();

        intent = getIntent();
        hisUid = intent.getStringExtra("idToken");


// 채팅화면 접속중인 액티비티 동기화 =================================
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid).child(hisUid);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("confirm").setValue(1);
                } else {
                    chatRef.child("confirm").setValue(0); //현재 화면을 보고있단 뜻
                    Log.d("채팅 보는중", "성공");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });

// 채팅화면 접속중인 액티비티 동기화 끝 =================================
    }

    // onCheck start ============================================================================================

    public void onCheck(){
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

    @Override
    public void finish() {
        // 액티비티가 종료될 때 호출됨
        super.finish();
        Log.d("뒤로가기 ", "뺵");
        closeCheck();
    }

    @Override
    public void onBackPressed() {
        // 뒤로 가기 버튼이 눌렸을 때 호출됨
        super.onBackPressed();
        closeCheck();
    }

    public void closeCheck(){
        Log.d("closeCheck ", "closeCheck");
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid).child(hisUid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRef.child("confirm").setValue(1);
                // 예: Toast 메시지 출력, UI 업데이트 등
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }


    private class FcmSendTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String message = params[0];
            // 이후 코드에서 message 변수 사용
            FcmSendDTO fcmSendDTO = new FcmSendDTO(Fridkakaoid, myName, message, "chat", 1L);
            Call<FcmSendDTO> fcmSend = RetrofitClientInstance.getApiService().fcm_send(token, fcmSendDTO);
            try {
                Response<FcmSendDTO> response = fcmSend.execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d("메세지 전송 알림 성공: ", "[성공]");
                // recyclerView.scrollToPosition(chatList.size()-1);
            } else {
                Log.d("메세지 전송 알림 실패: ", "[실패]");
            }
        }
    }




}
