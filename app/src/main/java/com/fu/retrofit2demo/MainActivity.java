package com.fu.retrofit2demo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //直接在这里初始化了，后面少些些代码
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.123/")    //要设置基础路径
                .addConverterFactory(GsonConverterFactory.create())   //添加Gson解析器,返回的数据就可以在内部就被Gson解析
                .build();
        api = retrofit.create(Api.class);

    }

    /**
     * 无参数@GET
     *
     * @param view
     */
    public void method1(View view) {
        //1.先创建Retrofit客户端,是Builder创建出来的
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.123/")    //要设置基础路径
                .addConverterFactory(GsonConverterFactory.create())   //添加Gson解析器,返回的数据就可以在内部就被Gson解析
                .build();

        //2.通过Retrofit创建出接口对象的代理对象
        Api api = retrofit.create(Api.class);

        //3.调用接口中的方法
        Call<User> call = api.getUser();
        //发起请求
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //请求成功后的回调
                User user = response.body(); //在内部自动用Gson解析成了User了
                Log.i(TAG, "onResponse: " + user.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //请求失败后的回调
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }

    /**
     * 动态URL的有参数@GET,@PATH
     *
     * @param view
     */
    public void method2(View view) {
        // 1、2步写在了一起
        Api api = new Retrofit.Builder()
                .baseUrl("http://192.168.0.123/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);

        Call<User> call = api.getUser("zhy");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.i(TAG, "onResponse: " + user.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 带有查询参数的url @GET, @Query
     *
     * @param view
     */
    public void method3(View view) {
        Api api = new Retrofit.Builder()
                .baseUrl("http://192.168.0.123/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);

        Call<User> call = api.getUserById(2);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.i(TAG, "onResponse: " + user.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 带有查询参数的url其实跟上面的@GET一样的
     *
     * @param view
     */
    public void method4(View view) {
        Call<User> call = api.getUserByName("周杰伦");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.i(TAG, "onResponse: " + user.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 向服务器传入json字符串,@POST,@Body
     * 这个服务端不会写，不知道怎么接受JSON字符串，暂时不测试
     * @param view
     */
    public void method5(View view) {
        Call<ResponseBody> call = api.postJson(new User("林俊杰", "男", 28));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "onResponse: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 以表单的方式传递键值对
     *
     * @param view
     */
    public void method6(View view) {
        Call<ResponseBody> call = api.login2("傅友财", "123");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "onResponse: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * 单文件上传,@Multipart,@POST,@Part
     * @param view
     */
    public void method7(View view) {
        File file = new File(getFilesDir(), "icon.png");
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photos", "icon.png", photoRequestBody);

        Call<ResponseBody> call = api.registerUser(photo, RequestBody.create(null, "abc"), RequestBody.create(null, "123"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i(TAG, "onResponse: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
