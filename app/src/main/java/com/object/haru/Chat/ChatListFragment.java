package com.object.haru.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.object.haru.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private View view;

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private List<ModelChatlist> modelChatList;
    private List<UserAccountDTO> userList;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private AdapterChatlist adapterChatlist;
    private LinearLayoutManager layoutManager;
    private String lastmessage, receiver, sender, time;

    private  String token;
    private Long kakaoid,Fridkakaoid;

    public ChatListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chatlist, container, false);

        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra("token");
        kakaoid = intent.getLongExtra("kakaoId", 0);

        Log.d("[채팅 리스트에서 카카오ID 확인]", String.valueOf(kakaoid)); //자신의 kid
        Log.d("TOKEN : ", token);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);

        modelChatList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setStackFromEnd(true);  //맨 마지막에 아이템 추가
        recyclerView.setLayoutManager(layoutManager);

     //   databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(String.valueOf(kakaoId));
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        Log.d("로그인사용자 idToken :", firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelChatList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    modelChatList.add(chatlist);
                }
                for (int i = 1; i < modelChatList.size(); i++) {
                    Log.d("List에 담긴 uid ["+ i+"]", "Chat ID: " + modelChatList.get(i).getId());
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void loadChats() {
        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserAccountDTO userAccountDTO = ds.getValue(UserAccountDTO.class);
                        for (ModelChatlist chatlist : modelChatList) {
                            if (userAccountDTO.getIdToken() != null && userAccountDTO.getIdToken().equals(chatlist.getId())) {
                                userList.add(userAccountDTO);

                                for (int i = 0; i < userList.size(); i++) {
                                    Log.d("userList", " email: " + userList.get(i).getEmail());
                                    Log.d("userList", " name: " + userList.get(i).getName());
                                    Log.d("userList", " idToken: " + userList.get(i).getIdToken());
                                    Fridkakaoid = userList.get(i).getKakaoid();
                                    // 나머지 필드들도 마찬가지로 출력 가능
                                }

                                break;
                            }
                        }
                    }

                }else if(!snapshot.exists()){
                    Log.d("loadchat 데이터없음", "No data available");
                }

                // 데이터를 어댑터에 추가하고 갱신
                adapterChatlist = new AdapterChatlist(getContext(), userList, token, kakaoid,Fridkakaoid);
                // 마지막 메시지 가져오기
                for (int i = 0; i < userList.size(); i++) {
                    lastMessage(userList.get(i).getIdToken());
                }
                recyclerView.setAdapter(adapterChatlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void lastMessage(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastmessage = "default";
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ChatDTO chat = ds.getValue(ChatDTO.class);
                    if (chat==null) {
                        continue;
                    }
                    sender = chat.getSender();
                    receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid()) &&
                            chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
                        lastmessage = chat.getMessage();
                        time = chat.getTimestamp();

                    }
                }
                Log.d("라스트 receiver", receiver);
                Log.d("라스트 sender", sender);
                Log.d("라스트 getMessage", lastmessage);
                Log.d("라스트 getTimestamp", time);
                adapterChatlist.setLastMessageMap(userId, lastmessage);

                adapterChatlist.notifyDataSetChanged();
                adapterChatlist.setLastTimeMap(userId, time);
                adapterChatlist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}