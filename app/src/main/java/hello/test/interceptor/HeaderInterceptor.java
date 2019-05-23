package hello.test.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截请求头
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

       Request originalRequest =  chain.request();//所有Http工作发生的地方，生成和请求对应的响应。

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .method(originalRequest.method(), originalRequest.body());
        return chain.proceed(requestBuilder.build());
    }
}
