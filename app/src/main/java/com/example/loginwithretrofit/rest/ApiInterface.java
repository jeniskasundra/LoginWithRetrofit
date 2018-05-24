package com.example.loginwithretrofit.rest;

import com.example.loginwithretrofit.model.User;
import com.example.loginwithretrofit.model.UserList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    /*
  Retrofit get annotation with our URL
  And our method that will return us the List of User List
  */
    @GET("getAllUser.php")
    Call<UserList> getAllUser();

    @FormUrlEncoded
    @POST("addUser.php")
    public Call<ResponseBody> registerUser(@Field("name") String name,
                                           @Field("email") String password,
                                           @Field("password") String email, @Field("image") String image);

    @GET("getUser.php")
    Call<User> getUserById(@Query("id") Integer id);


    @FormUrlEncoded
    @POST("updateUser.php")
    public Call<ResponseBody> updateUser(@Field("id") Integer id,
                                         @Field("name") String password,
                                         @Field("image") String image);

    @FormUrlEncoded
    @POST("updatePasswordUser.php")
    public Call<ResponseBody> updatePasswordUser(@Field("id") Integer id,
                                                 @Field("password") String password
                                                 );

    @GET("deleteUser.php")
    Call<ResponseBody> deleteUser(@Query("id") Integer id);

}
