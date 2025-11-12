package com.zfile.module.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "更新用户存储源请求")
public class UserStorageSourceUpdateRequest {

    @Schema(description = "用户存储源ID")
    @NotNull(message = "用户存储源ID不能为空")
    private Integer id;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @Schema(description = "存储源ID")
    @NotNull(message = "存储源ID不能为空")
    private Integer storageSourceId;

    @Schema(description = "根路径")
    @NotBlank(message = "根路径不能为空")
    @Length(max = 255, message = "根路径长度不能超过255个字符")
    private String rootPath;

    @Schema(description = "是否启用")
    @NotNull(message = "启用状态不能为空")
    private Boolean enable;

    @Schema(description = "权限列表")
    @Length(max = 255, message = "权限列表长度不能超过255个字符")
    private String permissions;
}