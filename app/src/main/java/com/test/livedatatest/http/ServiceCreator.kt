package com.test.livedatatest.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 不带参数，静态laze单例模式
 */
class ServiceCreator private constructor(){

    private val WRITE_TIME : Long = 60_000;

    private val READ_TIME : Long = 60_000;

    private val CONN_TIME : Long = 60_000;

    private val serviceMap : Map<String,Object> = ConcurrentHashMap<String,Object>();

     private object RetrofitHolder {
         val INSTANCE = ServiceCreator()
     }

     companion object {
         val instance : ServiceCreator by lazy { RetrofitHolder.INSTANCE}

     }

     //java 写法
//      private class RetrofitHolder {
//          private static final ServiceCreator serviceCreator = new ServiceCreator();
//      }
//
//      public static ServiceCreator getInstance() {
//        return RetrofitHolder.serviceCreator;
//    }

    //第二种写法
//    companion object {
//
//        //① 双重锁校验单例模式 第一种写法
//        @Volatile //原子性
//        private var INSTANCE : ServiceCreator? = null
//
//         val instance : ServiceCreator
//         get() {
//             if (INSTANCE == null) {
//                 synchronized(ServiceCreator::class.java) {
//                     if (INSTANCE == null) {
//                         INSTANCE = ServiceCreator()
//                     }
//                 }
//             }
//             return INSTANCE!!
//         }
//        //② 双重锁校验单例模式 第二种写法
//          val instance : ServiceCreator by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ServiceCreator() }
//    }

     private fun createRetrofit(url : String) : Retrofit? {

         var okHttpClient : OkHttpClient? = null

          var builder : OkHttpClient.Builder = OkHttpClient.Builder()
                  .readTimeout(READ_TIME,TimeUnit.MILLISECONDS)
                  .writeTimeout(WRITE_TIME,TimeUnit.MILLISECONDS)
                  .connectTimeout(CONN_TIME,TimeUnit.MILLISECONDS)
                  .retryOnConnectionFailure(true)
         okHttpClient = builder.build();

         return Retrofit.Builder().baseUrl(url)
                 .client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create())
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .build()
     }




}