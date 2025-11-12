package com.zfile.module.auth.springsecurity;

import com.zfile.module.user.entity.User;
import com.zfile.module.user.service.UserService;
import com.zfile.module.user.userenum.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户详情服务实现类
 * 用于Spring Security认证过程中加载用户信息
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始加载用户信息，用户名: {}", username);

        // 检查用户名是否为空
        if (!StringUtils.hasText(username)) {
            log.warn("用户名为空");
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 根据用户名查询用户信息 (使用UserService中的方法)
        User user = userService.getUserByUsername(username);
        if (user == null) {
            log.warn("用户不存在，用户名: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        // 判断用户状态是否启用
        if (user.getEnable() != UserStatusEnum.ENABLE) {
            log.warn("用户已被禁用，用户名: {}", username);
            throw new UsernameNotFoundException("用户已被禁用");
        }

        // 获取用户权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getDefaultPermissions().getCode().toUpperCase()));

        log.info("用户信息加载成功，用户名: {}", username);
        // 返回Spring Security的UserDetails对象
        // 注意：这里我们将用户名作为"密码"传递给Spring Security，
        // 实际的密码验证将在CustomPasswordEncoder中通过用户名查询用户并验证密码
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getUsername()) // 关键修改：传递用户名而非密码
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getEnable() != UserStatusEnum.ENABLE)
                .build();
    }
}