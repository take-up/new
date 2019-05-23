package hello.test.http.execption.base;

import hello.test.http.config.HttpCode;

public class BaseException extends RuntimeException{

     private  int errorCode = HttpCode.CODE_UNKNOWN;

    public BaseException() {

    }

    public BaseException(int errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
