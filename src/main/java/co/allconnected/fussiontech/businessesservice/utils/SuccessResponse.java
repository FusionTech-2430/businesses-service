package co.allconnected.fussiontech.businessesservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse {
    private int code;
    private String message;
}