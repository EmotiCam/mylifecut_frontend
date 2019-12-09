package com.example.lifecut.JsonPlacement;

import com.example.lifecut.DjangoData;
import com.example.lifecut.DjangoGet;
import com.example.lifecut.Information;
import com.example.lifecut.Information2;
import com.example.lifecut.ObjectFiles.Chat;
import com.example.lifecut.ObjectFiles.User;
import com.example.lifecut.ObjectFiles.UserSign;
import com.example.lifecut.UserPass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/v1/emotions/")
    Call<List<DjangoGet>> getPosts(@Header("Authorization") String authToken);

    @GET("api/v1/statements/neutral/")
    Call<List<Chat>> getNeutral();
    @GET("api/v1/statements/happiness/")
    Call<List<Chat>> getHappiness();
    @GET("api/v1/statements/anger/")
    Call<List<Chat>> getAnger();
    @GET("api/v1/statements/sadness/")
    Call<List<Chat>> getSadness();
    @GET("api/v1/statements/contempt/")
    Call<List<Chat>> getContempt();
    @GET("api/v1/statements/fear/")
    Call<List<Chat>> getFear();
    @GET("api/v1/statements/disgust/")
    Call<List<Chat>> getDisgust();
    @GET("api/v1/statements/surprise/")
    Call<List<Chat>> getSurprise();

    @POST("api/v1/emotions/")
    Call<DjangoData>createPost(@Body DjangoData post, @Header("Authorization") String authToken);

    @POST("api/v1/profiles/")
    Call<Information>createProfile(@Body Information post, @Header("Authorization") String authToken);

    @PATCH("api/v1/profiles/{id}/")
    Call<Information>changeProfile(@Path ("id") int id, @Body Information post, @Header("Authorization") String authToken);

    @GET("api/v1/profiles/")
    Call<List<Information>> getNick(@Header("Authorization") String authToken);

    @GET("api/v1/profiles/")
    Call<List<Information2>> getId(@Header("Authorization") String authToken);

    @POST("api/v1/auth/login/")
    Call<User> login(@Body UserPass login);

    @POST("api/v1/auth/registration/")
    Call<User> registration(@Body UserSign registration);
}
