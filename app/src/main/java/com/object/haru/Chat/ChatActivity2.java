package com.object.haru.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.object.haru.R;

public class ChatActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Write a message to the database
        //데이터베이스 선언과 할당
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // message 데이터를 참조
        DatabaseReference myRef = database.getReference("message");

        //message에 해당 내용 쓰기
        myRef.setValue("Hello, World!");
    }
}

//