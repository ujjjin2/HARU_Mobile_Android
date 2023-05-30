package com.object.haru.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.object.haru.Activity.ApplyActivity;
import com.object.haru.Activity.DetailActivity;
import com.object.haru.Activity.ProfileActivity;
import com.object.haru.Activity.WritedActivity;
import com.object.haru.Activity.ZzimListActivity;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.UserDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment_Slide extends Fragment  {

    private View view;
    private View profile;
    private View zzim;
    private TextView profile_title1,profile_title2,myPageFragment_name,myPageFragment_applyText,myPageFragment_writeText;
    private  String token;
    private Long kakaoId;

    private CardView writeCardView, registerCardView;

    private RecruitDTO recruitDTO;
    String[] descriptionData = {"모집중", "선발중", "모집 완료"};

    ImageView myPageFragment_profile;
    private StateProgressBar stateProgressBar;
    private StateProgressBar stateProgressBar2;
    private ApplyDTO applyDTO;
    private UserDTO userDTO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mypage, container, false);

        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        Log.d("[카카오ID 확인]", String.valueOf(kakaoId));

        Context context = requireContext();
        // SharedPreferences 가져오기
        SharedPreferences auto = context.getSharedPreferences("chatCount", Context.MODE_PRIVATE);
        int chatCount = auto.getInt("chatCount", 0);
        Log.d("마이페이지 프레그먼트에서 chatCount", String.valueOf(chatCount));

        profile = view.findViewById(R.id.view1);
        zzim = view.findViewById(R.id.view2);
        writeCardView = view.findViewById(R.id.myPageFragment_cardView1);
        registerCardView = view.findViewById(R.id.myPageFragment_cardView2);
        stateProgressBar = (StateProgressBar) view.findViewById(R.id.progress_bar_1);
        stateProgressBar2 = (StateProgressBar) view.findViewById(R.id.progress_bar_2);

        profile_title2 = view.findViewById(R.id.profile_title2);
        profile_title1 = view.findViewById(R.id.profile_title1);

        myPageFragment_profile = view.findViewById(R.id.myPageFragment_profile);
        myPageFragment_name = view.findViewById(R.id.myPageFragment_name);
        myPageFragment_applyText = view.findViewById(R.id.myPageFragment_applyText);
        myPageFragment_writeText = view.findViewById(R.id.myPageFragment_writeText);

        fetchRecentWritedData();
        fetchRecentApplyData();
        fetchUserInformation();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("kakaoId", kakaoId);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZzimListActivity.class);
                intent.putExtra("kakaoId", kakaoId);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        //내가 작성한 글 > 을 클릭 하면 이동
        myPageFragment_writeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WritedActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("kakaoId", kakaoId);
                startActivity(intent);
            }
        });

        //내가 지원한 알바 글 > 을 클릭 하면 이동
        myPageFragment_applyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApplyActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("kakaoId", kakaoId);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchUserInformation() {
        //유저 정보
        Call<UserDTO> userDTOCall = RetrofitClientInstance.getApiService().Show_user_info(token,kakaoId);
        userDTOCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                userDTO = response.body();
                updateUserInformation();
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("이름 오류","============");
                t.printStackTrace();
            }
        });
    }

    private void updateUserInformation() {
        myPageFragment_name.setText(userDTO.getName());
        String photoURL = userDTO.getPhoto();
        Glide.with(getActivity()).load(photoURL).into(myPageFragment_profile);
    }

    private void fetchRecentApplyData() {
        //내가 지원한 알바
        Call<ApplyDTO> applycall = RetrofitClientInstance.getApiService().MapageSHOW_Apply(token,kakaoId);
        applycall.enqueue(new Callback<ApplyDTO>() {
            @Override
            public void onResponse(Call<ApplyDTO> call, Response<ApplyDTO> response) {
                applyDTO = response.body();
                updateRecentApplyDataStateProgressBar();
            }

            @Override
            public void onFailure(Call<ApplyDTO> call, Throwable t) {
                Log.w("실패[내가 지원한 알바]","====================");
            }
        });
    }

    private void updateRecentApplyDataStateProgressBar() {
        if (applyDTO.getRid() == null){
            profile_title2.setText("지원한 글이 없음");
            stateProgressBar2.setVisibility(View.INVISIBLE);
            registerCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            registerCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intent = new Intent(getActivity(), DetailActivity.class);
                    }
                    intent.putExtra("kakaoId", kakaoId);
                    intent.putExtra("token", token);
                    intent.putExtra("rId", applyDTO.getRid());
                    startActivity(intent);
                }
            });
            profile_title2.setText(applyDTO.getTitle());
            String step = applyDTO.getStep();
            if (step.equals("모집중")){
                stateProgressBar2.setVisibility(View.VISIBLE);
                stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            } else if (step.equals("선발중")){
                stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            } else if (step.equals("모집완료")){
                stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                stateProgressBar2.setAllStatesCompleted(true);
            }
            stateProgressBar2.setStateDescriptionData(descriptionData);
        }
    }

    private void fetchRecentWritedData() {
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().MypageSHOW(token, kakaoId);
        call.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                recruitDTO = response.body();
                updateRecentWritedDataStateProgressBar();
            }

            @Override
            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                Log.w("실패[내가 작성한 알바]","====================");
            }
        });
    }

    private void updateRecentWritedDataStateProgressBar() {
        if (recruitDTO == null){
            profile_title1.setText("작성한 글이 없음");
            stateProgressBar.setVisibility(View.INVISIBLE);
            writeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            writeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intent = new Intent(getActivity(), DetailActivity.class);
                    }
                    intent.putExtra("kakaoId", kakaoId);
                    intent.putExtra("token", token);
                    intent.putExtra("rId", recruitDTO.getRid());
                    startActivity(intent);
                }
            });

            profile_title1.setText(recruitDTO.getTitle());

            String step = recruitDTO.getStep();
            if (step.equals("모집중")){
                stateProgressBar.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            }else if (step.equals("선발중")){
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            }else if (step.equals("모집완료")){
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                stateProgressBar.setAllStatesCompleted(true);
            }
            stateProgressBar.setStateDescriptionData(descriptionData);
        }
    }


}
