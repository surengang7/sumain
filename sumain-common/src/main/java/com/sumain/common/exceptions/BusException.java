package com.sumain.common.exceptions;


import com.sumain.common.enums.ResponseEnum;

import java.io.Serial;


public class BusException extends GeneralException {


    @Serial
    private static final long serialVersionUID = -3261719680563314307L;

    public BusException(){
        super(ResponseEnum.BUSINESS_ERROR.getCode(), ResponseEnum.BUSINESS_ERROR.getMsg());
    }

    public BusException(String message) {
        super(ResponseEnum.BUSINESS_ERROR.getCode(), ResponseEnum.BUSINESS_ERROR.getMsg()+":"+message);
    }

    public BusException(String message, Object data) {
        super(ResponseEnum.BUSINESS_ERROR.getCode(), ResponseEnum.BUSINESS_ERROR.getMsg()+":"+message,data);
    }

}