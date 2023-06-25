package com.object.haru.retrofit;

import com.kakao.sdk.user.model.User;
import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.FCMDTO;
import com.object.haru.DTO.FcmSendDTO;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.NoticeDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.TestDTO;
import com.object.haru.DTO.UserDTO;
import com.object.haru.DTO.zzimRequestDTO;
import com.object.haru.alarm.AlarmDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    //일정한 위치 근처의 구인글 다 띄움
    @GET("/api/recruit/select/loaction")
    Call<List<RecruitDTO>> getAll(@Header("X-Auth-Token") String token, @Query("kakaoid") Long kakaoid, @Query("latitude") double latitude,
                                  @Query("longtitude") double longtitude, @Query("page") int i);

    //DetailActivity 에 깂 넣어서 띄우기, 지원서 지원조건 띄우기
    @GET("/api/recruit/select/{id}")
    Call<RecruitDTO> getDetailRecruit(@Header("X-Auth-Token")String token,@Path("id") Long rid);

    //카카오
    @GET("/kakao/oauth")
    Call<KakaoDTO> kakaoLogin(@Header("X-Auth-Token") String token, @Query("acccesstoken")String acccesstoken, @Query("fcmtoken")String fcmtoken);

    // 검색
    @GET("/api/recruit/select/location/search3")
    Call<List<RecruitDTO>> getSearchRecruit(@Header("X-Auth-Token")String token,@Query("search")String search, @Query("kakaoid")Long kakaoid);

    //상세 페이지-삭제
    @PUT("/api/recruit/remove/{rid}")
    Call<RecruitDTO> Deleterecruit(@Header("X-Auth-Token")String token,@Path("rid") Long rid);

    //찜 누르기
    @POST("/zzim/v1/save")
    Call<zzimRequestDTO> zzimSave(@Header("X-Auth-Token")String token, @Body zzimRequestDTO zzim);

    //찜 삭제 하기
    @DELETE("/zzim/v1/delete/{kakaoid}/{rid}")
    Call<zzimRequestDTO> zzimDelete(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid, @Query("rid")Long rid);

    // 찜 확인 하기
    @GET("/zzim/v1/count/{rid}/{kakaoid}")
    Call<Boolean> zzimCheck(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid, @Query("rid")Long rid);

    // 찜한 리스트 불러오기
    @GET("/zzim/v1/select/kakaoid")
    Call<List<RecruitDTO>> getZzimList(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakoid);

    //지원서 작성 하기
    @POST("/apply/v1/save")
    Call<ApplyDTO> applyWrite(@Header("X-Auth-Token")String token, @Body ApplyDTO applyDTO);

    //구인 등록 하기
    @POST("/api/recruit/post")
    Call<RecruitDTO> register(@Header("X-Auth-Token")String token,@Body RecruitDTO recruitDTO);

    //지원자 리스트
    @GET("/apply/v1/select/rid")
    Call<List<ApplyDTO>> apply_list(@Header("X-Auth-Token")String token,@Query("rid")Long rid);

    //작성글 리스트
    @GET("/api/recruit/select/user/{kakaoid}")
    Call<List<RecruitDTO>> writed_list(@Header("X-Auth-Token")String token,@Path("kakaoid")Long kakaoid);

    @PUT("/api/recruit/choose/{rid}/{kakaoid}")
    Call<Void> confirmUser(@Header("X-Auth-Token")String token, @Path("kakaoid")Long kakaoid, @Path("rid")Long rid);

    //지원 리스트
    @GET("/apply/v1/select/kakaoid")
    Call<List<RecruitDTO>> getApplyList(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid);

    //지원 상세 내역
    @GET("/apply/v1/select/detail/aid") // 0514 새로 추가
    Call<ApplyDTO> getApplyDetail(@Header("X-Auth-Token")String token, @Query("aid")Long aid);

    //사용자 평균 평점 조회 0514 추가
    @GET("/rreview/rrating/{rreceiverkakaoid}") // 0514 새로 추가
    Call<Long> getRating(@Header("X-Auth-Token")String token, @Query("rreceiverkakaoid")Long kakaoid);

    // 해당 구인글에 지원 여부
    @GET("/apply/v1/count/{rid}/{uid}")
    Call<Long> isApply(@Header("X-Auth-Token")String token, @Query("rid")Long rid, @Query("kakaoid")Long kakaoid);

    // 지원취소하기
    @DELETE("/apply/v1/delete/aid")
    Call<Void> deleteApply(@Header("X-Auth-Token")String token, @Query("aid")Long aid);

    //마이 리스트 - 내가 작성한 글
    @GET("/api/recruit/select/user/one/{kakaoid}")
    Call<RecruitDTO> MypageSHOW(@Header("X-Auth-Token")String token,@Path("kakaoid")Long kakaoid);

    //마이 리스트 - 내가 지원한 알바
    @GET("/apply/v1/recentApply")
    Call<ApplyDTO> MapageSHOW_Apply(@Header("X-Auth-Token")String token,@Query("kakaoid")Long kakaoid);

    // Test 로그인
    @GET("/test/login")
    Call<TestDTO> Test_Login();

    // 마이 페이지 유저 보여주기
    @GET("/kakao/select/user")
    Call<UserDTO> Show_user_info(@Header("X-Auth-Token")String token,@Query("kakaoid")Long kakaoid);

    //fcm save
    @POST("/fcm/save")
    Call<FCMDTO> fcm_save(@Header("X-Auth-Token")String token,@Body FCMDTO fcmdto);

    //fcm 알림 send
    @POST("/fcm/send")
    Call<FcmSendDTO> fcm_send(@Header("X-Auth-Token")String token, @Body FcmSendDTO fcmSendDTO);

    // 회원정보 수정
    @PUT("/kakao/change")
    Call<UserDTO> updateUser(@Header("X-Auth-Token")String token, @Query("age")String age, @Query("career")String career,
                             @Query("kakaoid")Long kakaoid, @Query("name")String name, @Query("photo")String photo, @Query("sex")String sex);

    //구인 수정
    @PUT("/api/recruit/change")
    Call<RecruitDTO> updateRecruit(@Header("X-Auth-Token")String token,@Query("addr")String addr,@Query("endTime")String endTime,
                                   @Query("kakaoid")Long kakaoid, @Query("lat")double lat, @Query("lon")double lon,@Query("pay")int pay,@Query("rage")String rage,
                                   @Query("rid")Long rid, @Query("rsex")String rsex,@Query("stTime")String stTime,@Query("subject") String subject,
                                   @Query("title")String title);

    //공지 받아오기
    @GET("/admin/notice/select")
    Call<List<NoticeDTO>> selectNotice(@Header("X-Auth-Token")String token);

    @GET("/kakao/delete")
    Call<Void> deleteUser(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid);


    @GET("/alarm/select/kakaoid")
    Call<List<AlarmDTO>> getAlarmList(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid);

    @PUT("/alarm/update/{alarmId}")
    Call<Void> updateCheckAlarm(@Header("X-Auth-Token")String token, @Path("alarmId")Long alarmId);




}
