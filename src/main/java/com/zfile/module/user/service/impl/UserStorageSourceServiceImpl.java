package com.zfile.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfile.module.user.entity.UserStorageSource;
import com.zfile.module.user.mapper.UserStorageSourceMapper;
import com.zfile.module.user.request.UserStorageSourcePageRequest;
import com.zfile.module.user.service.UserStorageSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author zhang
* @description 针对表【user_storage_source(用户存储源表)】的数据库操作Service实现
* @createDate 2025-11-11 00:17:03
*/
@Service
public class UserStorageSourceServiceImpl extends ServiceImpl<UserStorageSourceMapper, UserStorageSource>
    implements UserStorageSourceService{
    
    private static final Logger log = LoggerFactory.getLogger(UserStorageSourceServiceImpl.class);

    @Override
    public List<UserStorageSource> listUserStorageSourcesByUserId(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                log.warn("查询用户存储源时用户ID参数无效: {}", userId);
                return List.of();
            }
            
            QueryWrapper<UserStorageSource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            List<UserStorageSource> userStorageSources = this.list(queryWrapper);
            
            log.debug("根据用户ID {} 查询到 {} 个存储源", userId, userStorageSources.size());
            return userStorageSources;
        } catch (Exception e) {
            log.error("根据用户ID查询用户存储源时发生异常，用户ID: {}", userId, e);
            throw new RuntimeException("查询用户存储源失败", e);
        }
    }

    @Override
    public List<UserStorageSource> listUserStorageSourcesByEnableStatus(Boolean enable) {
        try {
            if (enable == null) {
                log.warn("查询用户存储源时启用状态参数为空");
                return List.of();
            }
            
            QueryWrapper<UserStorageSource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("enable", enable);
            List<UserStorageSource> userStorageSources = this.list(queryWrapper);
            
            log.debug("根据启用状态 {} 查询到 {} 个存储源", enable, userStorageSources.size());
            return userStorageSources;
        } catch (Exception e) {
            log.error("根据启用状态查询用户存储源时发生异常，状态: {}", enable, e);
            throw new RuntimeException("查询用户存储源失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUserStorageSourcesByIds(List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                log.warn("批量删除用户存储源时ID列表为空");
                return false;
            }
            
            boolean result = this.removeBatchByIds(ids);
            
            if (result) {
                log.info("成功删除 {} 个用户存储源，IDs: {}", ids.size(), ids);
            } else {
                log.warn("删除用户存储源失败，IDs: {}", ids);
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量删除用户存储源时发生异常，IDs: {}", ids, e);
            throw new RuntimeException("删除用户存储源失败", e);
        }
    }

    @Override
    public IPage<UserStorageSource> pageUserStorageSources(UserStorageSourcePageRequest userStorageSourcePageRequest) {
        try {
            if (userStorageSourcePageRequest == null) {
                log.warn("分页查询用户存储源时请求参数为空");
                return new Page<>();
            }
            
            // 创建分页对象
            IPage<UserStorageSource> page = new Page<>(userStorageSourcePageRequest.getCurrentPage(), userStorageSourcePageRequest.getPageSize());
            
            // 构建查询条件
            QueryWrapper<UserStorageSource> queryWrapper = new QueryWrapper<>();
            
            // 用户ID查询
            if (userStorageSourcePageRequest.getUserId() != null) {
                queryWrapper.eq("user_id", userStorageSourcePageRequest.getUserId());
            }
            
            // 存储源ID查询
            if (userStorageSourcePageRequest.getStorageSourceId() != null) {
                queryWrapper.eq("storage_source_id", userStorageSourcePageRequest.getStorageSourceId());
            }
            
            // 启用状态查询
            if (userStorageSourcePageRequest.getEnable() != null) {
                queryWrapper.eq("enable", userStorageSourcePageRequest.getEnable());
            }

            // 按ID降序排列
            queryWrapper.orderByDesc("id");
            
            IPage<UserStorageSource> result = this.page(page, queryWrapper);
            log.debug("分页查询用户存储源完成，当前页: {}, 每页大小: {}, 总记录数: {}", 
                     userStorageSourcePageRequest.getCurrentPage(), userStorageSourcePageRequest.getPageSize(), result.getTotal());
            
            return result;
        } catch (Exception e) {
            log.error("分页查询用户存储源时发生异常，请求参数: {}", userStorageSourcePageRequest, e);
            throw new RuntimeException("分页查询用户存储源失败", e);
        }
    }
}