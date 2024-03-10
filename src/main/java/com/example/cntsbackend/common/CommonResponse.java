package com.example.cntsbackend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)//序列化去掉空值
public class CommonResponse<T> {
    private final int code;
    private final String message;

    private T data;

    private CommonResponse(int code, String message){
        this.code=code;
        this.message=message;
    }

    private CommonResponse(int code, String message, T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public static <T> CommonResponse<T> createForSuccess(String message){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(),message);
    }
    public static <T> CommonResponse<T> createForSuccess(T data){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDesc(), data);
    }

    public static <T> CommonResponse<T> createForSuccess(String message,T data){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(),message, data);
    }
    public static <T> CommonResponse<T> createForSuccess(int code, String message){
        return new CommonResponse<>(code,message);
    }

    public static <T> CommonResponse<T> createForError(){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> CommonResponse<T> createForError(String message){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(),message);
    }
    public static <T> CommonResponse<T> createForError(int code, String message){
        return new CommonResponse<>(code,message);
    }
    public static <T> CommonResponse<T> createForError(String message,T data){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(),message, data);
    }

}
