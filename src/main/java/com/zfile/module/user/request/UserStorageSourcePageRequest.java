package com.zfile.module.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户存储源分页请求")
public class UserStorageSourcePageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    @Schema(description = "当前页", example = "1")
    private Integer currentPage = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "存储源ID")
    private Integer storageSourceId;

    @Schema(description = "是否启用")
    private Boolean enable;
}