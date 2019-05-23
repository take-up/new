package hello.test.http;

import android.util.TimeUtils;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import hello.test.interceptor.HeaderInterceptor;
import hello.test.interceptor.HttpInterceptor;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import www.baidutest.com.BuildConfig;
import www.baidutest.com.MyAppcation;

/**
 * 使用单例模式
 */
public class RetrofitManagement {

    //定义网络连接，读取，写入时间
    private static final long READ_TIMEOUT = 60;

    private static final long WRITE_TIMEOUT = 60;

    private static final long CONNECT_TIMEOUT = 60;

    private RetrofitManagement() {
    }

    public static RetrofitManagement getInstance() {
        return RetrofitHolder.retrofitManagement;
    }

    private static class RetrofitHolder {
        private static final RetrofitManagement retrofitManagement = new RetrofitManagement();
    }

    private Retrofit createRetrofit(String url) {
        //实例化OkHttp,添加拦截器
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new HttpInterceptor());
        if (BuildConfig.IS_DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
            builder.addInterceptor(new ChuckInterceptor(MyAppcation.getAppContext()));
        }

        OkHttpClient okHttpClient = builder.build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



}
