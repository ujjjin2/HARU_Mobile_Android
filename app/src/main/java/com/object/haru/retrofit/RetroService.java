package com.object.haru.retrofit;

import com.object.haru.DTO.RecruitDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetroService {
    @GET("/api/recruit/select/{id}")
    Call<List<RecruitDTO>> getAll(@Path("id") int rid);

}
