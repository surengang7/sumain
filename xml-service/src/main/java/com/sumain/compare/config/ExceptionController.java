package com.sumain.compare.config;


import com.sumain.compare.model.GeneralException;
import com.sumain.compare.model.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    @ResponseBody
    public ResponseEntity<String> handleStatusRuntimeGeneralException(GeneralException ex) {
        return ResponseEntity.fail(ex);
    }


}
