package com.senvu.exception;

import com.senvu.Enum.BusinessExceptionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum exceptionEnum;

    public BusinessException(BusinessExceptionEnum e){
        this.exceptionEnum = e;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
