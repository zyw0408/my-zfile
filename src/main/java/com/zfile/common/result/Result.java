package com.zfile.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAIL = 500;

    @Schema(title = "业务状态码，200为正常，其他值均为异常，异常情况下见响应消息", example = "200")
    private final Integer code;

    @Schema(title = "响应消息", example = "ok")
    private final String msg;

    @Schema(title = "响应数据")
    private final T data;

    @Schema(title = "数据总条数，分页情况有效")
    private final Long dataCount;

    @Schema(title = "跟踪 ID")
    private final String traceId;

    @Schema(title = "响应时间")
    private final LocalDateTime timestamp;

    //返回空值
    public static Result<Void> success() {
        return new Result<Void>(CODE_SUCCESS, "ok", null, null, null, LocalDateTime.now());
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(CODE_SUCCESS, "ok", data, 1L, null, LocalDateTime.now());
    }
    
    public static <T> Result<T> success(T data, Long dataCount) {
        return new Result<T>(CODE_SUCCESS, "ok", data, dataCount, null, LocalDateTime.now());
    }
    
    public static Result<Void> fail(String msg) {
        return new Result<>(CODE_FAIL, msg, null, null, null, LocalDateTime.now());
    }
    
    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<T>(code, msg, null, null, null, LocalDateTime.now());
    }
    
    public static <T> Result<T> fail(Integer code, String msg, String traceId) {
        return new Result<T>(code, msg, null, null, traceId, LocalDateTime.now());
    }
}