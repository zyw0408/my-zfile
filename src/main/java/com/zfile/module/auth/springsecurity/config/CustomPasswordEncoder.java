package com.zfile.module.auth.springsecurity.config;

import com.zfile.module.user.entity.User;
import com.zfile.module.user.service.UserService;
import com.zfile.module.user.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义密码编码器
 * 适配系统现有的PasswordUtil工具类
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {
    
    @Autowired
    private UserService userService;

    /**
     * 由于我们不直接使用此方法进行编码，这里简单返回null
     * 实际的密码编码在UserService中完成
     *
     * @param rawPassword 原始密码
     * @return null
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    /**
     * 使用系统现有的密码验证逻辑进行密码匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码（实际上是用户名）
     * @return 是否匹配
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 由于Spring Security的UserDetailsService实现中，我们将用户的加密密码设置为用户名
        // 所以encodedPassword实际上是用户名
        String username = encodedPassword;
        
        // 根据用户名查询用户信息
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        // 使用系统现有的密码验证逻辑
        return PasswordUtil.matches(rawPassword.toString(), user.getPassword(), user.getSalt());
    }
}