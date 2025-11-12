package com.zfile.module.auth.satoken.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应类
 */
@Data
@AllArgsConstructor
@Schema(description = "登录响应类")
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否登录成功
     */
    @Schema(description = "是否登录成功")
    private Boolean success;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;

    /**
     * Token值
     */
    @Schema(description = "Token值")
    private String token;

    /**
     * Token过期时间
     */
    @Schema(description = "Token过期时间，单位：秒")
    private Long tokenTimeout;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Integer userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
}