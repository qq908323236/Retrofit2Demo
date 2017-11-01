package com.fu.retrofit2demo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Fu.
 * QQ:908323236
 * 2017/10/31 16:49
 */

public interface Api {
    /**
     * 1.最简单接口,注解中的参数与BaseUrl拼接组成完成路径
     * 如: http://192.168.0.123/user
     *
     * @return Call对象, 用来发起请求的
     */
    @GET("user")
    Call<User> getUser();

    /**
     * 2.动态的URL访问，注解中{ }相当于占位符
     * 如 username = fyc  :  http://192.168.0.123/user/fyc
     * 如 username = zhy  :  http://192.168.0.123/user/zhy
     * 参数中需要声明占位符，用@Path("...")
     * 这里可以重载
     *
     * @param username
     * @return
     */
    @GET("user/{username}")
    Call<User> getUser(@Path("username") String username);

    /**
     * 3.GET请求带有查询参数的URL，@Query("id")定义URL中的参数
     * 如: http://192.168.0.123/users?id=2
     * 参数中需要用@Query来声明
     *
     * @param id
     * @return
     */
    @GET("users")
    Call<User> getUserById(@Query("id") int id);

    /**
     * 4.POST请求带有查询参数的URL，@Query("name")定义URL中的参数
     * post请求的参数就不会拼接到url里了，在请求头里的
     * 参数中需要用@Query()来声明
     *
     * @param name
     * @return
     */
    @POST("users2")
    Call<User> getUserByName(@Query("name") String name);

    /**
     * 5.以Post的请求方式，向服务器传入json字符串
     * 参数中需要用@Body来声明是传入的是对象
     *
     * @param user
     * @return
     */
    @POST("postjson")
    Call<ResponseBody> postJson(@Body User user);

    /**
     * 6.以表单的方式传递键值对
     *
     * @param name
     * @param password
     * @return
     */
    @POST("login2")
    @FormUrlEncoded
    Call<ResponseBody> login2(@Field("username") String name, @Field("password") String password);

    /**
     * 7.单文件上传
     *
     * @param phot
     * @param username
     * @param password
     * @return
     */
    @Multipart
    @POST("registerUser")
    Call<ResponseBody> registerUser(@Part MultipartBody.Part phot, @Part("username") RequestBody username,
                                    @Part("username") RequestBody password);

    /**
     * 8.与RxJava配合使用,返回的就是Observable(被观察者)
     * @param username
     * @return
     */
    @GET("user/{username}")
    Observable<User> getUser2(@Path("username") String username);
}
