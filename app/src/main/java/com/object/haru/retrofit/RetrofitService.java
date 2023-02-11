package com.object.haru.retrofit;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.TestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    //일정한 위치 근처의 구인글 다 띄움
    @GET("/api/recruit/select/loaction")
    Call<List<RecruitDTO>> getAll(@Header("X-Auth-Token")String token, @Query("distance")double distance, @Query("latitude")double latitude, @Query("longtitude")double longtitude);

    //DetailActivity 에 깂 넣어서 띄우기
    @GET("/api/recruit/select/{id}")
    Call<RecruitDTO> getDetailRecruit(@Path("id")int rid);

}
