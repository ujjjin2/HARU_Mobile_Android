package com.object.haru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.object.haru.DTO.UserDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText nameEdit, sexEdit, ageEdit, careerEdit;

    private String token, name, career, age;
    private Long kakaoId;

    private TextView editButton, cancelButton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setEnabled(false);
              updateUserInfo();
          }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        nameEdit = findViewById(R.id.nameEdit);

        sexEdit = findViewById(R.id.sexEdit);

        ageEdit = findViewById(R.id.ageEdit);

        careerEdit = findViewById(R.id.careerEdit);



        getUserInfo();
    }

    private void updateUserInfo() {
        Call<UserDTO> userDTOCall = RetrofitClientInstance.getApiService().updateUser(token, ageEdit.getText().toString(), String.valueOf(careerEdit.getText()),
                kakaoId, String.valueOf(nameEdit.getText()), "0", String.valueOf(sexEdit.getText()));
        userDTOCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                finish();
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("회원정보 수정 오류","============");
                t.printStackTrace();
            }
        });
        editButton.setEnabled(true);
    }

    private void getUserInfo() {
        Call<UserDTO> userDTOCall = RetrofitClientInstance.getApiService().Show_user_info(token,kakaoId);
        userDTOCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {

                UserDTO userDTO = response.body();
                nameEdit.setText(userDTO.getName());
                sexEdit.setText(userDTO.getSex());
                ageEdit.setText(userDTO.getAge());
                careerEdit.setText(userDTO.getCareer());
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("이름 오류","============");
                t.printStackTrace();
            }
        });
    }
}
