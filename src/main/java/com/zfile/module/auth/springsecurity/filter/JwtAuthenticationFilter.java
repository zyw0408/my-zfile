package com.zfile.module.auth.springsecurity.filter;

import com.zfile.module.auth.springsecurity.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于拦截请求并验证JWT令牌
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token的格式为 "Bearer token"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            if (StringUtils.hasText(jwtToken)) {
                try {
                    username = jwtUtil.getUsernameFromToken(jwtToken);
                    // 检查解析出的用户名是否有效
                    if (username == null || !StringUtils.hasText(username)) {
                        log.warn("从JWT Token中解析出的用户名为空");
                    }
                } catch (Exception e) {
                    log.warn("无法获取JWT中的用户名", e);
                }
            } else {
                log.warn("JWT Token为空");
            }
        } else {
            if (requestTokenHeader != null) {
                log.warn("JWT Token不是以Bearer字符串开头");
            } else {
                log.debug("请求中未提供Authorization头");
            }
        }

        // 只有在用户名有效且SecurityContext中没有认证信息时才进行认证
        if (username != null && StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 如果token有效，配置Spring Security
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 将认证信息设置到SecurityContext中
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    log.info("JWT Token验证通过，用户名: {}", username);
                } else {
                    log.warn("JWT Token验证失败，用户名: {}", username);
                }
            } catch (Exception e) {
                log.warn("加载用户信息时发生异常，用户名: {}", username, e);
            }
        } else if (username != null && !StringUtils.hasText(username)) {
            log.warn("用户名为空，跳过JWT认证");
        }
        
        chain.doFilter(request, response);
    }
}