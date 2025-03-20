package com.sumain.common.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sumain.common.enums.ResponseEnum;
import com.sumain.common.exceptions.GeneralException;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Setter
@Getter
@ToString
@Data
public class ResponseEntity<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = -7477940840031734693L;

    private static final Integer SUCCESS_CODE = ResponseEnum.SUCCESS.getCode();

	private Integer code;
	private String msg;
	private T data;

	public static <T> ResponseEntity<T> ok() {
		return newResponseEntity(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),null);
	}

	public static <T> ResponseEntity<T> ok(T data) {
		return newResponseEntity(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getMsg(),data);
	}

	public static <T> ResponseEntity<T> fail(GeneralException exception) {
		return newResponseEntity(exception.getCode(),exception.getMessage(),null);
	}

	private static <T> ResponseEntity<T> newResponseEntity(Integer code, String msg, T data) {
		ResponseEntity<T> responseEntity = new ResponseEntity<>();
		responseEntity.setCode(code);
		responseEntity.setData(data);
		responseEntity.setMsg(msg);
		return responseEntity;
	}

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
