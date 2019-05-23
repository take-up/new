package hello.test.http.basic;

/**
 * 成功回调接口
 * @param <T>
 */
public interface RequestCallBack<T> {

    void onSuccess( T t );
}
