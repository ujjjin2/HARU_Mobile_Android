package com.object.haru.retrofit;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.TestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroService {

    @GET("/api/recruit/select/loaction")
    Call<List<RecruitDTO>> getAll(@Query("distance")double distance, @Query("latitude")double latitude, @Query("longtitude")double longtitude);

}
