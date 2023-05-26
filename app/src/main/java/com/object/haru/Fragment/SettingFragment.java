package com.object.haru.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.object.haru.Activity.NoticeActivity;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {

    private View notice, user_modify, user_logout, user_exit;

    private Call<Void> exit_call;
    private String token;
    private Long kakaoId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        //Context 객체 가져오기
        Context context = requireContext();
        // SharedPreferences 가져오기
        SharedPreferences auto = context.getSharedPreferences("chatCount", Context.MODE_PRIVATE);
        int chatCount = auto.getInt("chatCount", 0);
        Log.d("세팅 프레그먼트에서 chatCount", String.valueOf(chatCount));

        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getExtras().getLong("kakaoId");

        notice = view.findViewById(R.id.notice);
        user_modify = view.findViewById(R.id.user_modify);
        user_logout = view.findViewById(R.id.user_logout);
        user_exit = view.findViewById(R.id.user_exit);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(this, NewActivity.class);
//                startActivity(intent);
            }
        });

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("알림");
                builder.setMessage("로그아웃 하시겠습니까?");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 자동로그인 추가시 테스트 후 추가
                        // autoLoginEdit.clear(); // SharedPreferences에 저장된 모든 데이터 제거
                        // autoLoginEdit.apply(); // 변경 사항 저장
                        getActivity().finish(); //시작 액티비티로 이동
                        showToast("로그아웃 완료");
                    }

                    private void showToast(String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                });

                builder.setNegativeButton("아니요", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        user_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("알림");
                builder.setMessage("정말로 회원 탈퇴를 하시겠습니까? \n 개인 정보를 제외한 다른 내용은 \n 삭제 되지 않습니다.");

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Void> exit_call = RetrofitClientInstance.getApiService().deleteUser(token, kakaoId);
                        exit_call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    getActivity().finish(); //시작 액티비티로 이동
                                    // 자동 로그인, 로직 변경시 해당 칸에 기입
                                    showToast("회원 탈퇴 완료");
                                } else {
                                    // 요청은 성공했으나 서버에서 오류 응답을 받은 경우
                                    showToast("회원 탈퇴 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // 요청이 실패한 경우
                                showToast("회원 탈퇴 실패: " + t.getMessage());
                            }

                            private void showToast(String message) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("아니요", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;

    }

}

