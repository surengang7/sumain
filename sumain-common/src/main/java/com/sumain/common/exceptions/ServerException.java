package com.sumain.common.exceptions;


import com.sumain.common.enums.ResponseEnum;

import java.io.Serial;


public class ServerException extends GeneralException {


    @Serial
    private static final long serialVersionUID = -5153363148912084629L;

    public ServerException(){
        super(ResponseEnum.SERVICE_ERROR.getCode(), ResponseEnum.SERVICE_ERROR.getMsg());
    }

    public ServerException(String message) {
        super(ResponseEnum.SERVICE_ERROR.getCode(), ResponseEnum.SERVICE_ERROR.getMsg()+":"+message);
    }

    public ServerException(String message,Object data) {
        super(ResponseEnum.SERVICE_ERROR.getCode(), ResponseEnum.SERVICE_ERROR.getMsg()+":"+message,data);
    }
}