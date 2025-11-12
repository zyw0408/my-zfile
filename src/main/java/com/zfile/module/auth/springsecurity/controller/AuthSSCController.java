package com.zfile.module.auth.springsecurity.controller;

import com.zfile.common.result.Result;
import com.zfile.module.auth.springsecurity.request.LoginRequest;
import com.zfile.module.auth.springsecurity.request.RegisterRequest;
import com.zfile.module.auth.springsecurity.service.UserSSCService;
import com.zfile.module.user.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Security认证接口控制器
 */
@Tag(name = "Spring Security认证接口")
@Slf4j
@RequestMapping("/spring-security/auth")
@RestController
public class AuthSSCController {

    @Autowired
    private UserSSCService userSSCService;

    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            log.info("开始处理用户注册请求，用户名: {}", registerRequest.getUsername());
            Result<?> result = userSSCService.register(registerRequest);
            if (result.getCode() == 200) {
                log.info("用户注册成功，用户名: {}", registerRequest.getUsername());
            } else {
                log.warn("用户注册失败，用户名: {}，原因: {}", registerRequest.getUsername(), result.getMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("用户注册时发生异常，用户名: {}", registerRequest.getUsername(), e);
            return Result.fail("注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录接口
     * 注意：实际的登录逻辑由Spring Security处理，这里只是提供API文档说明
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest loginRequest) {
        // 实际的登录逻辑由Spring Security的UsernamePasswordAuthenticationFilter处理
        // 这里只是为了Swagger API文档展示
        return Result.success();
    }

    /**
     * 用户登出接口
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        try {
            log.info("开始处理用户登出请求");
            
            // 清除SecurityContext中的认证信息
            SecurityContextHolder.clearContext();
            
            log.info("用户登出成功");
            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("用户登出时发生异常", e);
            return Result.fail("登出失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/userinfo")
    @PreAuthorize("isAuthenticated()")
    public Result<?> getCurrentUserInfo() {
        try {
            log.info("开始获取当前登录用户信息");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                
                // 构造用户信息响应
                UserResponse userResponse = new UserResponse();
                userResponse.setUsername(username);
                
                log.info("获取当前登录用户信息成功，用户名: {}", username);
                return Result.success(userResponse);
            } else {
                log.warn("获取当前登录用户信息失败");
                return Result.fail("获取用户信息失败");
            }
        } catch (Exception e) {
            log.error("获取当前登录用户信息时发生异常", e);
            return Result.fail("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 测试管理员权限接口
     */
    @Operation(summary = "测试管理员权限")
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> testAdminPermission() {
        return Result.success("管理员权限验证通过");
    }

    /**
     * 测试普通用户权限接口
     */
    @Operation(summary = "测试普通用户权限")
    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public Result<?> testUserPermission() {
        return Result.success("普通用户权限验证通过");
    }
}