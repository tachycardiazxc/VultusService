package ru.sruit.vultusservice.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response <T> {

    protected int code;
    protected T data;

    public static <T> Response<T> ok() {
        return new SuccessResponse<>();
    }

    public static <T> Response<T> ok(T data) {
        return new SuccessResponse<>(data);
    }

    public static <T> Response<T> error(int code, String message) {
        return new ErrorResponse<>(code, message);
    }

}
