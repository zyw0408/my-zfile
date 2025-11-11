package com.zfile.module.auth.springsecurity.service;

import com.zfile.common.result.Result;
import com.zfile.module.auth.springsecurity.request.RegisterRequest;
import com.zfile.module.user.entity.User;
import com.zfile.module.user.request.UserAddRequest;
import com.zfile.module.user.service.UserService;
import com.zfile.module.user.userenum.UserPermissionEnum;
import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Spring Security用户服务类
 * 处理用户注册等与安全相关的业务逻辑
 */
@Slf4j
@Service
public class UserSSCService {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求参数
     * @return 注册结果
     */
    public Result<?> register(RegisterRequest registerRequest) {
        try {
            // 检查参数
            if (registerRequest == null) {
                log.warn("注册请求参数为空");
                return Result.fail("请求参数不能为空");
            }

            // 检查用户名是否已存在
            User existingUser = userService.getUserByUsername(registerRequest.getUsername());
            if (existingUser != null) {
                log.warn("注册失败，用户名已存在: {}", registerRequest.getUsername());
                return Result.fail("用户名已存在");
            }

            // 创建新用户请求
            UserAddRequest userAddRequest = new UserAddRequest();
            userAddRequest.setUsername(registerRequest.getUsername());
            userAddRequest.setNickname(registerRequest.getNickname());
            userAddRequest.setPassword(registerRequest.getPassword());
            userAddRequest.setEnable(UserStatusEnum.ENABLE); // 默认启用
            userAddRequest.setDefaultPermissions(UserPermissionEnum.USER); // 默认普通用户权限

            // 通过UserService创建用户，确保自动填充机制生效
            User newUser = userService.addUser(userAddRequest);
            if (newUser == null) {
                log.error("用户注册失败，无法保存用户信息，用户名: {}", registerRequest.getUsername());
                return Result.fail("注册失败");
            }

            log.info("用户注册成功，用户名: {}, 用户ID: {}", newUser.getUsername(), newUser.getId());
            return Result.success("注册成功");
        } catch (Exception e) {
            log.error("用户注册时发生异常，用户名: {}", registerRequest.getUsername(), e);
            return Result.fail("注册失败: " + e.getMessage());
        }
    }
}