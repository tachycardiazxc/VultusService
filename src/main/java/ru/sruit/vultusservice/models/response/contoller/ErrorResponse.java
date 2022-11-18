package ru.sruit.vultusservice.models.response.contoller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse <T> extends Response<T> {

    private final String message;

    public ErrorResponse(int code, String message) {
        this.message = message;
        this.code = code;
    }

}
