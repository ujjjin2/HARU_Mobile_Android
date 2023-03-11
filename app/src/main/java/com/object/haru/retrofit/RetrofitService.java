package com.object.haru.retrofit;

import com.object.haru.DTO.ApplyDTO;
import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.RecruitDTO;
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

    //상세페이지-삭제
    @PUT("/api/recruit/remove/{rid}")
    Call<RecruitDTO> Deleterecruit(@Header("X-Auth-Token")String token,@Path("rid") int rid);

    //찜 누르기
    @POST("/zzim/v1/save")
    Call<zzimRequestDTO> zzimSave(@Header("X-Auth-Token")String token, @Body zzimRequestDTO zzim);

    //찜 삭제하기
    @DELETE("/zzim/v1/delete/{uid}/{rid}")
    Call<zzimRequestDTO> zzimDelete(@Header("X-Auth-Token")String token, @Query("uid")int uid, @Query("rid")int rid);

    //지원서 작성하기
    @POST("/apply/v1/save")
    Call<ApplyDTO> applyWrite(@Header("X-Auth-Token")String token, @Body ApplyDTO applyDTO);

    @POST("/api/recruit/post")
    Call<RecruitDTO> register(@Header("X-Auth-Token")String token,@Body RecruitDTO recruitDTO);

}
