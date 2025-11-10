package com.progress.coolProject.DTO;

import com.progress.coolProject.Enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private ResponseStatus status;
    private String message;
    private Object detail;
    private Object details;
    public static ResponseDTO success(String message) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, null,null);
    }

    public static ResponseDTO success(String message, Object detail) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, detail,null);
    }

    public static ResponseDTO success(Object details, String message) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, null, details);
    }

    public static ResponseDTO error(String message) {
        return new ResponseDTO(ResponseStatus.ERROR, message, null,null);
    }

    public static ResponseDTO error(String message, Object detail) {
        return new ResponseDTO(ResponseStatus.ERROR, message, detail,null);
    }

    public static ResponseDTO error(Object details, String message) {
        return new ResponseDTO(ResponseStatus.ERROR, message, null, details);
    }

    public static ResponseDTO notFound(String message) {
        return new ResponseDTO(ResponseStatus.NOT_FOUND, message, null,null);
    }

    public static ResponseDTO badRequest(String message) {
        return new ResponseDTO(ResponseStatus.BAD_REQUEST, message, null,null);
    }

    public static ResponseDTO internalError(String message) {
        return new ResponseDTO(ResponseStatus.INTERNAL_SERVER_ERROR, message, null,null);
    }
}
