package com.sumain.compare.model;

import lombok.Data;
import lombok.EqualsAndHashCode;



@EqualsAndHashCode(callSuper = true)
@Data
public class GeneralException extends RuntimeException{

    private static final long serialVersionUID = 980355576254893561L;
    private final int code;
    private final String message;
    private Object data = null;

    public GeneralException(String message) {
        super(message);
        this.code = ResponseEnum.FAIL.getCode();
        this.message = message;
    }


    public GeneralException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public GeneralException(int code, String message, Object data){
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
