package com.zfile.module.auth.springsecurity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 注册请求类 - Spring Security版本
 */
@Data
@Schema(description = "注册请求类 - Spring Security版本")
public class RegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;

}