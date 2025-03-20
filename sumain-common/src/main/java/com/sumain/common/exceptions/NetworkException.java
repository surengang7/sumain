package com.sumain.common.exceptions;

import com.sumain.common.enums.ResponseEnum;

import java.io.Serial;

public class NetworkException extends GeneralException{
    @Serial
    private static final long serialVersionUID = -5868037685524295556L;

    public NetworkException() {
        super(ResponseEnum.PARAM_ERROR.getCode(), ResponseEnum.PARAM_ERROR.getMsg());
    }
}
