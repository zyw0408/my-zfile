package com.zfile.module.user.request;

import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.userenum.UserPermissionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.io.Serial;
import java.io.Serializable;

/**
 * 更新用户请求类
 */
@Data
@Schema(description = "更新用户请求类")
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Integer id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private UserStatusEnum enable;

    /**
     * 默认权限
     */
    @Schema(description = "默认权限")
    private UserPermissionEnum defaultPermissions;
}