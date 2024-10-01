package co.allconnected.fussiontech.businessesservice.exceptions;

import lombok.Getter;

@Getter
public class OperationException extends RuntimeException {
    private final int code;

    public OperationException(int code, String message) {
        super(message);
        this.code = code;
    }
}
