package hello.test.http.execption;

import hello.test.http.config.HttpCode;
import hello.test.http.execption.base.BaseException;

public class ConnectionException extends BaseException {

   public ConnectionException() {
       super(HttpCode.CODE_CONNECTION_FAILED, "网络请求失败");
   }
}
