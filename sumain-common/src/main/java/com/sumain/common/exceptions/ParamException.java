package com.sumain.common.exceptions;


import com.sumain.common.enums.ResponseEnum;

import java.io.Serial;


public class ParamException extends GeneralException {


    @Serial
    private static final long serialVersionUID = 837707218120368947L;

    public ParamException(){
        super(ResponseEnum.PARAM_ERROR.getCode(), ResponseEnum.PARAM_ERROR.getMsg());
    }

    public ParamException(String message) {
        super(ResponseEnum.PARAM_ERROR.getCode(), ResponseEnum.PARAM_ERROR.getMsg()+":"+message);
    }

    public ParamException(String message,Object data) {
        super(ResponseEnum.PARAM_ERROR.getCode(), ResponseEnum.PARAM_ERROR.getMsg()+":"+message,data);
    }
}