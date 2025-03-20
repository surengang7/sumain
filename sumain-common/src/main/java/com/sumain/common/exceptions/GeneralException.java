package com.sumain.common.exceptions;

import com.sumain.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class GeneralException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -8617839656774864869L;

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
