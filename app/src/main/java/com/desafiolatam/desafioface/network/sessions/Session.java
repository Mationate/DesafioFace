package com.desafiolatam.desafioface.network.sessions;

import com.desafiolatam.desafioface.models.CurrentUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Session {

    @FormUrlEncoded
    @POST("mobile_sessions")
    Call<CurrentUser> login (@Field("session[email]") String email, @Field("session[password]")String password);
}
