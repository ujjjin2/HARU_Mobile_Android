package com.object.haru.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.object.haru.Activity.ApplyActivity;
import com.object.haru.Activity.ProfileActivity;
import com.object.haru.Activity.WritedActivity;
import com.object.haru.Activity.ZzimListActivity;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.UserDTO;
import com.object.haru.R;
import com.object.haru.retrofit.RetrofitClientInstance;

import java.net.URL;

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
    String[] descriptionData = {"모집중", "선발중", "모집 완료"};

    ImageView myPageFragment_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mypage, container, false);

        Intent intent = getActivity().getIntent();
        token = intent.getStringExtra("token");
        kakaoId = intent.getLongExtra("kakaoId", 0);
        Log.d("[카카오ID 확인]", String.valueOf(kakaoId));

        profile = view.findViewById(R.id.view1);
        zzim = view.findViewById(R.id.view2);

        profile_title2 = view.findViewById(R.id.profile_title2);
        profile_title1 = view.findViewById(R.id.profile_title1);

        //내가 작성한 알바
        Call<RecruitDTO> call = RetrofitClientInstance.getApiService().MypageSHOW(token,kakaoId);
        call.enqueue(new Callback<RecruitDTO>() {
            @Override
            public void onResponse(Call<RecruitDTO> call, Response<RecruitDTO> response) {
                RecruitDTO recruitDTO = response.body();

                String title = recruitDTO.getTitle();
                if (title.equals(null) ){
                    profile_title1.setText("작성한 글이 없음");
                }else {
                    profile_title1.setText(recruitDTO.getTitle());

                    String step = recruitDTO.getStep();
                    StateProgressBar stateProgressBar = (StateProgressBar) view.findViewById(R.id.progress_bar_1);
                    if (step.equals("모집중")){
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

            @Override
            public void onFailure(Call<RecruitDTO> call, Throwable t) {
                Log.w("실패[내가 작성한 알바]","====================");
            }
        });

//        //내가 지원한 알바
//        Call<ApplyDTO> applycall = RetrofitClientInstance.getApiService().MapageSHOW_Apply(token,kakaoId);
//        applycall.enqueue(new Callback<ApplyDTO>() {
//            @Override
//            public void onResponse(Call<ApplyDTO> call, Response<ApplyDTO> response) {
//                ApplyDTO applyDTO = response.body();
//
//                profile_title2.setText(applyDTO.getTitle());
//
//                String step = applyDTO.getStep();
//                StateProgressBar stateProgressBar2 = (StateProgressBar) view.findViewById(R.id.progress_bar_2);
//                if (step.equals("모집중")){
//                    stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("one"));
//                }else if (step.equals("선발중")){
//                    stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("two"));
//                }else if (step.equals("모집 완료")){
//                    stateProgressBar2.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("three"));
//                    stateProgressBar2.setAllStatesCompleted(true);
//                }
//                stateProgressBar2.setStateDescriptionData(descriptionData);
//
//            }
//
//            @Override
//            public void onFailure(Call<ApplyDTO> call, Throwable t) {
//                Log.w("실패[내가 지원한 알바]","====================");
//            }
//        });

        myPageFragment_profile = view.findViewById(R.id.myPageFragment_profile);
        myPageFragment_name = view.findViewById(R.id.myPageFragment_name);

        //유저 정보
        Call<UserDTO> userDTOCall = RetrofitClientInstance.getApiService().Show_user_info(token,kakaoId);
        userDTOCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {

                UserDTO userDTO = response.body();
                myPageFragment_name.setText(userDTO.getName());
                String photoURL = userDTO.getPhoto();
                Glide.with(getActivity()).load(photoURL).into(myPageFragment_profile);

            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("이름 오류","============");
                t.printStackTrace();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZzimListActivity.class);
                startActivity(intent);
            }
        });

        //내가 작성한 글 > 을 클릭 하면 이동
        myPageFragment_writeText = view.findViewById(R.id.myPageFragment_writeText);
        myPageFragment_writeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WritedActivity.class);
                startActivity(intent);
            }
        });

        //내가 작성한 글 > 을 클릭 하면 이동
        myPageFragment_applyText = view.findViewById(R.id.myPageFragment_applyText);
        myPageFragment_applyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApplyActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
