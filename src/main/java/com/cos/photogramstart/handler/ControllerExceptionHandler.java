package com.cos.photogramstart.handler;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public String validationException(CustomValidationException e){

        /*
        CMRespDto, Script 비교
        1. 클라이언트에게 응답할때는 Script가 좋음.
        2. Ajax통신 - CMRespDto
        3. Android통신 - CMRespDto
        * */
    if(e.getErrorMap() == null){
        return Script.back(e.getMessage());
    }else{
        return Script.back(e.getErrorMap().toString());
        }
    }

    // CMRespDto 오브젝트 + HttpStatus 상태코드를 응답하는 핸들러
    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
        return new ResponseEntity<>(
                new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        return new ResponseEntity<>(
                new CMRespDto<>(-1, e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public String exception(CustomException e){
        return Script.back(e.getMessage());
    }
}
