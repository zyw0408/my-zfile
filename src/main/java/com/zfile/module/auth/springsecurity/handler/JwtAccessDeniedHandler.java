package com.zfile.module.auth.springsecurity.handler;

import com.alibaba.fastjson2.JSON;
import com.zfile.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT访问拒绝处理器
 * 用于处理已认证但权限不足的用户访问受保护资源的情况
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("用户权限不足，无法访问受保护的资源: {}", request.getRequestURI());
        
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        // 返回权限不足的响应
        Result<?> result = Result.fail("权限不足，无法访问该资源");
        response.getWriter().println(JSON.toJSONString(result));
    }
}