package hello.test.http.basic;

import hello.test.http.execption.base.BaseException;

public interface RequestMultiplyCallback<T> extends RequestCallBack<T> {

    void onFail(BaseException e);
}
