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
    private String token, sex, self, name, career, age, idToken;
    private TextView tv_name, tv_rating, tv_age, tv_career, tv_sex, tv_self;
    private Integer rating;
    private ImageButton backButton;
    private Button confirm, chatButton;
    private Long rId, kakaoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sex = intent.getStringExtra("sex");
        self = intent.getStringExtra("self");
        name = intent.getStringExtra("name");
        career = intent.getStringExtra("career");
        age = intent.getStringExtra("age");
        rating = intent.getIntExtra("rating", 0);
        rId = intent.getLongExtra("rId", 0);
        kakaoid = intent.getLongExtra("kakaoId", 0);


        Log.d("레이팅 확인", String.valueOf(rating));

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
           Log.d("kakaoid 확인", kakaoid.toString());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (rating == 0) {
            tv_rating.setText("0.0");
        } else {
            tv_rating.setText(rating);
        }

        // 여기서부터
        tv_name.setText(name);
        tv_age.setText(age);
        tv_career.setText(career);
        tv_self.setText(self);
        tv_sex.setText(sex);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUser();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("상대방 kakaoid : "+ kakaoid);
                chatStart(kakaoid);
            }
        });


    }

    private void confirmUser() {
        Call<Void> call = RetrofitClientInstance.getApiService().confirmUser(token, kakaoid, rId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void chatStart(Long kakaoid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userAccount");
        Query query = databaseReference.orderByChild("kakaoid").equalTo(String.valueOf(kakaoid));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserAccountDTO userAccountDTO = snapshot.getValue(UserAccountDTO.class);
                        String idToken = userAccountDTO.getIdToken();

                        System.out.println("메소드안에서 idtoken "+ idToken);

                        Intent intent = new Intent(ApplyDetailActivity.this, ChatActivity.class);
                        intent.putExtra("token", idToken);
                        intent.putExtra("kakaoid", kakaoid);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "No matching data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database Error", databaseError.toException());
            }
        });
    }





}