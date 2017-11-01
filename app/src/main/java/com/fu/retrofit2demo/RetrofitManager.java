package com.fu.retrofit2demo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fu.
 * QQ:908323236
 * 2017/11/1 16:49
 * 对retrofit做一些简单的封装
 */

public class RetrofitManager {
    private static final String BASE_URL = "http://192.168.0.123/";  //基础地址

    private volatile static RetrofitManager instance;
    private Retrofit mRetrofit;

    //单例子模式，私有化构造器
    private RetrofitManager() {
        //创建okhttpp客户端,并在这里进行配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)  //设置超时时间
                .build();

        //创建retrofit客户端，并在这里进行配置
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)          //把okhtpp客户端配置进去
                .addConverterFactory(GsonConverterFactory.create())   //添加转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //添加适配器
                .build();
    }

    //双重检查锁
    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    //创建请求接口
    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }
}
