package hello.test.http.execption;

import hello.test.http.config.HttpCode;
import hello.test.http.execption.base.BaseException;

public class ResultInvalidException extends BaseException {

    public ResultInvalidException() {
        super(HttpCode.CODE_RESULT_INVALID, "无效请求");
    }
}
