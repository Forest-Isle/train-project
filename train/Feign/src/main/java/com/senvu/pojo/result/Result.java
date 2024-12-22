package com.senvu.pojo.result;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer FAILD_CODE = 400;

    private String message;
    private Integer code;
    private T data;

    public Result(Integer code, String message){
        this.setCode(code);
        this.setMessage(message);
    }

    public Result(Integer code){
        this.setCode(code);
    }
}
