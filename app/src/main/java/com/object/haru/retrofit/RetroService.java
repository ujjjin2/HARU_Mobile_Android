package com.object.haru.retrofit;

import com.object.haru.DTO.RecruitDTO;
import com.object.haru.DTO.TestDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroService {

    @GET("/api/recruit/select/{id}")
    Call<RecruitDTO> getAll(@Path("id") String id);

}
