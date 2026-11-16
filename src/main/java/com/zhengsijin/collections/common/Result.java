package com.zhengsijin.collections.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public final class Result<T> {

    private final int code;
    private final String message;
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(HttpStatus.OK.value(), "成功", data);
    }

    public static <T> Result<T> created(T data) {
        return new Result<>(HttpStatus.CREATED.value(), "创建成功", data);
    }

    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
