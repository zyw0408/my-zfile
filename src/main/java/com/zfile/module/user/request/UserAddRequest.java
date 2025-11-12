package com.zfile.module.user.request;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.userenum.UserPermissionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.io.Serial;
import java.io.Serializable;

/**
 * 添加用户请求类
 */
@Data
@Schema(description = "添加用户请求类")
public class UserAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    @EnumValue
    private UserStatusEnum enable;

    /**
     * 默认权限
     */
    @Schema(description = "默认权限")
    @EnumValue
    private UserPermissionEnum defaultPermissions;

}