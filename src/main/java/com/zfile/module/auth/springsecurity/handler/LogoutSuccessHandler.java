package com.zfile.module.auth.springsecurity.handler;

import com.alibaba.fastjson2.JSON;
import com.zfile.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登出成功处理器
 * 用于处理用户登出成功的逻辑
 */
@Slf4j
@Component
public class LogoutSuccessHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            log.info("用户登出成功，用户名: {}", username);
        } else {
            log.info("匿名用户登出");
        }
    }
}