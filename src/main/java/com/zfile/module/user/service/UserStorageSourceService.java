package com.zfile.module.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zfile.module.user.entity.UserStorageSource;
import com.zfile.module.user.request.UserStorageSourcePageRequest;

import java.util.List;

/**
* @author zhang
* @description 针对表【user_storage_source(用户存储源表)】的数据库操作Service
* @createDate 2025-11-11 00:17:03
*/
public interface UserStorageSourceService extends IService<UserStorageSource> {

    /**
     * 根据用户ID查询用户存储源列表
     *
     * @param userId 用户ID
     * @return 用户存储源列表
     */
    List<UserStorageSource> listUserStorageSourcesByUserId(Integer userId);

    /**
     * 根据启用状态查询用户存储源列表
     *
     * @param enable 是否启用
     * @return 用户存储源列表
     */
    List<UserStorageSource> listUserStorageSourcesByEnableStatus(Boolean enable);

    /**
     * 批量删除用户存储源
     *
     * @param ids 用户存储源ID列表
     * @return 是否删除成功
     */
    boolean removeUserStorageSourcesByIds(List<Integer> ids);

    /**
     * 分页查询用户存储源
     *
     * @param userStorageSourcePageRequest 分页查询参数
     * @return 分页结果
     */
    IPage<UserStorageSource> pageUserStorageSources(UserStorageSourcePageRequest userStorageSourcePageRequest);
}