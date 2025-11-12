package com.zfile.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zfile.module.user.entity.User;
import com.zfile.module.user.request.UserAddRequest;
import com.zfile.module.user.request.UserPageRequest;
import com.zfile.module.user.service.UserService;
import com.zfile.module.user.mapper.UserMapper;
import com.zfile.module.user.util.PasswordUtil;
import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.userenum.UserPermissionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author zhang
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-11-07 17:55:31
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            if (!StringUtils.hasText(username)) {
                log.warn("查询用户时用户名为空");
                return null;
            }
            
            User user = userMapper.getUserByUsername(username);
            
            if (user == null) {
                log.info("未找到用户名为 {} 的用户", username);
            } else {
                log.debug("成功查询到用户名为 {} 的用户", username);
            }
            
            return user;
        } catch (Exception e) {
            log.error("根据用户名查询用户时发生异常，用户名: {}", username, e);
            throw new RuntimeException("查询用户失败", e);
        }
    }
    
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        try {
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                log.warn("用户名或密码为空，用户名: {}, 密码是否为空: {}", username, !StringUtils.hasText(password));
                return null;
            }
            
            User user = userMapper.getUserByUsername(username);
            
            if (user != null) {
                // 验证密码 (使用PasswordUtil中的matches方法)
                if (PasswordUtil.matches(password, user.getPassword(), user.getSalt())) {
                    log.debug("用户 {} 登录成功", username);
                    return user;
                } else {
                    log.warn("用户 {} 密码验证失败", username);
                }
            } else {
                log.warn("未找到用户名为 {} 的用户", username);
            }
            
            return null;
        } catch (Exception e) {
            log.error("用户登录验证时发生异常，用户名: {}", username, e);
            throw new RuntimeException("用户登录验证失败", e);
        }
    }
    
    @Override
    public User getUserById(Integer id) {
        try {
            if (id == null || id <= 0) {
                log.warn("查询用户时ID参数无效: {}", id);
                return null;
            }
            
            User user = this.getById(id);
            
            if (user == null) {
                log.info("未找到ID为 {} 的用户", id);
            } else {
                log.debug("成功查询到ID为 {} 的用户", id);
            }
            
            return user;
        } catch (Exception e) {
            log.error("根据ID查询用户时发生异常，ID: {}", id, e);
            throw new RuntimeException("查询用户失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(UserAddRequest userAddRequest) {
        try {
            if (userAddRequest == null) {
                log.warn("添加用户时请求参数为空");
                return null;
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(userAddRequest.getUsername());
            newUser.setNickname(userAddRequest.getNickname());
            
            // 生成盐值和加密密码
            String salt = PasswordUtil.generateSalt();
            String encodedPassword = PasswordUtil.encodePassword(userAddRequest.getPassword(), salt);
            
            newUser.setPassword(encodedPassword);
            newUser.setSalt(salt);
            newUser.setEnable(userAddRequest.getEnable());
            newUser.setDefaultPermissions(userAddRequest.getDefaultPermissions());
            
            // 保存用户，触发MyBatis-Plus自动填充
            boolean saved = this.save(newUser);
            
            if (saved) {
                log.info("成功添加用户，用户名: {}, 用户ID: {}", newUser.getUsername(), newUser.getId());
                return newUser;
            } else {
                log.warn("添加用户失败，用户名: {}", userAddRequest.getUsername());
                return null;
            }
        } catch (Exception e) {
            log.error("添加用户时发生异常，用户名: {}", userAddRequest.getUsername(), e);
            throw new RuntimeException("添加用户失败", e);
        }
    }
    
    @Override
    public List<User> listUsersByEnableStatus(UserStatusEnum enable) {
        try {
            if (enable == null) {
                log.warn("查询用户列表时启用状态参数为空");
                return List.of();
            }
            
            List<User> users = userMapper.listUsersByEnableStatus(enable.getCode());
            
            log.debug("根据启用状态 {} 查询到 {} 个用户", enable, users.size());
            return users;
        } catch (Exception e) {
            log.error("根据启用状态查询用户列表时发生异常，状态: {}", enable, e);
            throw new RuntimeException("查询用户列表失败", e);
        }
    }
    
    @Override
    public List<User> listUsersByPermissions(UserPermissionEnum permissions) {
        try {
            if (permissions == null) {
                log.warn("查询用户列表时权限参数为空");
                return List.of();
            }
            
            List<User> users = userMapper.listUsersByPermissions(permissions.getCode());
            
            log.debug("根据权限 {} 查询到 {} 个用户", permissions, users.size());
            return users;
        } catch (Exception e) {
            log.error("根据权限查询用户列表时发生异常，权限: {}", permissions, e);
            throw new RuntimeException("查询用户列表失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUsersByIds(List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                log.warn("批量删除用户时ID列表为空");
                return false;
            }
            
            boolean result = this.removeBatchByIds(ids);
            
            if (result) {
                log.info("成功删除 {} 个用户，IDs: {}", ids.size(), ids);
            } else {
                log.warn("删除用户失败，IDs: {}", ids);
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量删除用户时发生异常，IDs: {}", ids, e);
            throw new RuntimeException("删除用户失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeUserByUsername(String username) {
        try {
            if (!StringUtils.hasText(username)) {
                log.warn("根据用户名删除用户时用户名为空");
                return false;
            }
            
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            boolean result = this.remove(queryWrapper);
            
            if (result) {
                log.info("成功删除用户名为 {} 的用户", username);
            } else {
                log.warn("删除用户名为 {} 的用户失败，用户可能不存在", username);
            }
            
            return result;
        } catch (Exception e) {
            log.error("根据用户名删除用户时发生异常，用户名: {}", username, e);
            throw new RuntimeException("删除用户失败", e);
        }
    }

    @Override
    public IPage<User> pageUsers(UserPageRequest userPageRequest) {
        try {
            if (userPageRequest == null) {
                log.warn("分页查询用户时请求参数为空");
                return new Page<>();
            }
            
            // 创建分页对象
            IPage<User> page = new Page<>(userPageRequest.getCurrentPage(), userPageRequest.getPageSize());
            
            // 构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            
            // 用户名模糊查询
            if (StringUtils.hasText(userPageRequest.getUsername())) {
                queryWrapper.like("username", userPageRequest.getUsername());
            }
            
            // 昵称模糊查询
            if (StringUtils.hasText(userPageRequest.getNickname())) {
                queryWrapper.like("nickname", userPageRequest.getNickname());
            }
            
            // 启用状态查询
            if (userPageRequest.getEnable() != null) {
                queryWrapper.eq("enable", userPageRequest.getEnable());
            }
            
            // 权限状态查询
            if (userPageRequest.getDefaultPermissions() != null) {
                queryWrapper.eq("default_permissions", userPageRequest.getDefaultPermissions());
            }

            // 按ID降序排列
            queryWrapper.orderByDesc("id");
            
            IPage<User> result = this.page(page, queryWrapper);
            log.debug("分页查询用户完成，当前页: {}, 每页大小: {}, 总记录数: {}", 
                     userPageRequest.getCurrentPage(), userPageRequest.getPageSize(), result.getTotal());
            
            return result;
        } catch (Exception e) {
            log.error("分页查询用户时发生异常，请求参数: {}", userPageRequest, e);
            throw new RuntimeException("分页查询用户失败", e);
        }
    }
}