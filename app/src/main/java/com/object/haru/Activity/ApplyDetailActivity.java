package com.object.haru.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.object.haru.Chat.ChatActivity;
import com.object.haru.Chat.UserAccountDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyDetailActivity extends AppCompatActivity {
    private String token, sex, self, name, career, age,kakaoid;
    private TextView tv_name, tv_rating, tv_age, tv_career, tv_sex, tv_self;
    private Double rating;
    private ImageButton backButton;
    private Button confirm, chatButton;
    private Long rId, Fridkakaoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        Log.d("[[ ApplyDetailAct ]]", "Start");

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sex = intent.getStringExtra("sex");
        self = intent.getStringExtra("self");
        name = intent.getStringExtra("name");
        career = intent.getStringExtra("career");
        age = intent.getStringExtra("age");
        rating = intent.getDoubleExtra("rating", 0);
        rId = intent.getLongExtra("rId", 0);
        Fridkakaoid = intent.getLongExtra("Fridkakaoid", 0);
        kakaoid = intent.getStringExtra("kakaoid");



        tv_name = findViewById(R.id.applyDetail_Name);
        tv_rating = findViewById(R.id.applyDetail_rating_tv);
        tv_age = findViewById(R.id.applyDetail_age2);
        tv_career = findViewById(R.id.applyDetail_career2);
        tv_sex = findViewById(R.id.applyDetail_sex2);
        tv_self = findViewById(R.id.applyDetail_myself2);
        backButton = findViewById(R.id.imageButton_left);
        chatButton = findViewById(R.id.applyDetail_btn_chating);
        confirm = findViewById(R.id.applyDetail_btn_check);

        //   Log.d("확인", getIdTokenFromKakaoId(String.valueOf(kakaoid)));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (rating == 0) {
            tv_rating.setText("0.0");
        } else {
            tv_rating.setText(rating.toString());
        }

        // 여기서부터
        tv_name.setText(name);
        tv_age.setText(age+"세");
        tv_career.setText(career);
        tv_self.setText(self);
        tv_sex.setText(sex);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm.setEnabled(false);
                confirmUser();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButton.setEnabled(false);
                System.out.println("상대방 kakaoid : "+ Fridkakaoid);
                chatStart(Fridkakaoid);
            }
        });


    }

    private void confirmUser() {
        Call<Void> call = RetrofitClientInstance.getApiService().confirmUser(token, Fridkakaoid, rId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        confirm.setEnabled(true);
    }

    private void chatStart(Long Fridkakaoid) {
        Log.d("charStart에서 Fridkakaoid", Fridkakaoid.toString());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userAccount");
        reference.child(String.valueOf(Fridkakaoid)).child("idToken").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String idToken = snapshot.getValue(String.class);

                    Intent intent = new Intent(ApplyDetailActivity.this, ChatActivity.class);
                    intent.putExtra("idToken", idToken);  //idToken = 파베  전용 uid
                    intent.putExtra("Fridkakaoid", Fridkakaoid.toString()); //상대방 kakaoid
                    intent.putExtra("kakaoid", kakaoid); //내 자신 kakaoid
                    intent.putExtra("Fridname", name); //내 자신 kakaoid
                    intent.putExtra("token", token); // 내 token
                    startActivity(intent);
                    // idToken 값을 가져와서 처리

                } else {
                    Log.d("TAG", "No matching data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Database Error", error.toException());
            }
        });
        chatButton.setEnabled(true);
    }






}