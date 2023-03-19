package com.object.haru.retrofit;

import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.TestDTO;
import com.object.haru.DTO.UserDTO;
import com.object.haru.DTO.zzimRequestDTO;

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
    Call<List<RecruitDTO>> getAll(@Header("X-Auth-Token") String token, @Query("distance") double distance,
                                  @Query("latitude") double latitude, @Query("longtitude") double longtitude, @Query("page") int i);

    //DetailActivity 에 깂 넣어서 띄우기, 지원서 지원조건 띄우기
    @GET("/api/recruit/select/{id}")
    Call<RecruitDTO> getDetailRecruit(@Header("X-Auth-Token")String token,@Path("id") int rid);

    //카카오
    @GET("/kakao/oauth")
    Call<KakaoDTO> kakaoLogin(@Header("X-Auth-Token") String token, @Query("acccesstoken")String acccesstoken);

    // 검색
    @GET("api/recruit/select/search")
    Call<List<RecruitDTO>> getSearchRecruit(@Header("X-Auth-Token")String token, @Query("distance")double distance,
                                      @Query("latitude")double latitude, @Query("longtitude")double longtitude,
                                      @Query("search")String search);

    //상세 페이지-삭제
    @PUT("/api/recruit/remove/{rid}")
    Call<RecruitDTO> Deleterecruit(@Header("X-Auth-Token")String token,@Path("rid") int rid);

    //찜 누르기
    @POST("/zzim/v1/save")
    Call<zzimRequestDTO> zzimSave(@Header("X-Auth-Token")String token, @Body zzimRequestDTO zzim);

    //찜 삭제 하기
    @DELETE("/zzim/v1/delete/{uid}/{rid}")
    Call<zzimRequestDTO> zzimDelete(@Header("X-Auth-Token")String token, @Query("kakaoid")int kakaoid, @Query("rid")int rid);

    // 찜 확인 하기
    @GET("/zzim/v1/count/{rid}/{kakaoid}")
    Call<Boolean> zzimCheck(@Header("X-Auth-Token")String token, @Query("kakaoid")Long kakaoid, @Query("rid")int rid);

    //지원서 작성 하기
    @POST("/apply/v1/save")
    Call<ApplyDTO> applyWrite(@Header("X-Auth-Token")String token, @Body ApplyDTO applyDTO);

    //구인 등록 하기
    @POST("/api/recruit/post")
    Call<RecruitDTO> register(@Header("X-Auth-Token")String token,@Body RecruitDTO recruitDTO);

    //지원자 리스트
    @GET("/apply/v1/select/rid")
    Call<List<ApplyDTO>> apply_list(@Header("X-Auth-Token")String token,@Query("rid")int rid);

    //작성글 리스트
    @GET("/api/recruit/select/user/{kakaoid}")
    Call<List<RecruitDTO>> writed_list(@Header("X-Auth-Token")String token,@Path("kakaoid")Long kakaoid);

    //마이 리스트 - 내가 작성한 글
    @GET("/api/recruit/select/user/one/{kakaoid}")
    Call<RecruitDTO> MypageSHOW(@Header("X-Auth-Token")String token,@Path("kakaoid")Long kakaoid);

    //마이 리스트 - 내가 지원한 알바
    @GET("/apply/v1/recentApply")
    Call<ApplyDTO> MapageSHOW_Apply(@Header("X-Auth-Token")String token,@Query("kakaoid")Long kakaoid);

    @GET("/test/login")
    Call<TestDTO> Test_Login();

    @GET("/kakao/select/user")
    Call<UserDTO> Show_user_info(@Header("X-Auth-Token")String token,@Query("kakaoid")Long kakaoid);

}
