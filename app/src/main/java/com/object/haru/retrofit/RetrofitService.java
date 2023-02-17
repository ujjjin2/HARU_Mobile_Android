package com.object.haru.retrofit;

import com.object.haru.DTO.KakaoDTO;
import com.object.haru.DTO.RecruitDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    //일정한 위치 근처의 구인글 다 띄움
    @GET("/api/recruit/select/loaction")
    Call<List<RecruitDTO>> getAll(@Header("X-Auth-Token") String token, @Query("distance") double distance,
                                  @Query("latitude") double latitude, @Query("longtitude") double longtitude, @Query("pageSize") int i);

    //DetailActivity 에 깂 넣어서 띄우기
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

    @PUT("/api/recruit/remove")
    Call<RecruitDTO> Deleterecruit(@Header("X-Auth-Token")String token,@Path("id") int rid);
}
