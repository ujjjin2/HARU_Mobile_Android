package com.object.haru.Chat;

import android.os.Bundle;
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

public class Chat extends Fragment {

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

    public Chat() {

    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chatlist, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);

        modelChatList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelChatList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    modelChatList.add(chatlist);
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
        databaseReference = FirebaseDatabase.getInstance().getReference("trip").child("UserAccount");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    UserAccountDTO userAccountDTO = ds.getValue(UserAccountDTO.class);

                    for (ModelChatlist chatlist: modelChatList) {
                        if(userAccountDTO.getIdToken() != null && userAccountDTO.getIdToken().equals(chatlist.getId())) {
                            userList.add(userAccountDTO);
                            break;
                        }
                    }
                    // adapter
                    adapterChatlist = new AdapterChatlist(getContext(), userList);
                    // setadapter
                    recyclerView.setAdapter(adapterChatlist);

                    for(int i=0; i<userList.size(); i++) {
                        lastMessage(userList.get(i).getIdToken());
                    }
                }
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
                    ModelChat chat = ds.getValue(ModelChat.class);
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
