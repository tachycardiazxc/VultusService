package ru.sruit.vultusservice.util.validation;

import ru.sruit.vultusservice.models.exception.ApiValidationException;

public class ApiValidationUtils {

    public static void expectedNotNull(Object object, int code, String message) {
        if (object == null) {
            throw new ApiValidationException(code, message);
        }
    }

    public static void expectedNull(Object object, int code, String message) {
        if (object != null) {
            throw new ApiValidationException(code, message);
        }
    }

    public static void expectedTrue(boolean val, int code, String message) {
        if (!val) {
            throw new ApiValidationException(code, message);
        }
    }

    public static void expectedFalse(boolean val, int code, String message) {
        if (val) {
            throw new ApiValidationException(code, message);
        }
    }

    public static <T> void expectedEqual(T a, T b, int code, String message) {
        if (!a.equals(b)) {
            throw new ApiValidationException(code, message);
        }
    }

}
