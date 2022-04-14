package com.fun.common.utils.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 返回类
 * @author hardy.ma
 */
@Data
public class Result<T> {

    private Integer code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
