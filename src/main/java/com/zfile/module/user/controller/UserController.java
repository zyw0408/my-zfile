package com.zfile.module.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zfile.common.result.PageResult;
import com.zfile.common.result.Result;
import com.zfile.module.user.entity.User;
import com.zfile.module.user.request.UserAddRequest;
import com.zfile.module.user.request.UserPageRequest;
import com.zfile.module.user.request.UserUpdateRequest;
import com.zfile.module.user.response.UserResponse;
import com.zfile.module.user.convert.UserConvert;
import com.zfile.module.user.service.UserService;
import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.userenum.UserPermissionEnum;
import com.zfile.module.user.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Tag(name = "用户接口")
@Slf4j
@ApiSort(1)
@RequestMapping("/user")
@RestController
@Validated
public class UserController {
    //构造器中注入
    private final UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 添加用户
     */
    @Operation(summary = "添加用户")
    @PostMapping("/add")
    public Result<?> addUser(@RequestBody @Validated UserAddRequest userAddRequest) {
        try {
            logger.info("开始添加用户，请求参数: {}", userAddRequest);
            
            if (userAddRequest == null) {
                logger.warn("添加用户时请求参数为空");
                return Result.fail("请求参数不能为空");
            }
            
            User user = UserConvert.INSTANCE.convertToUser(userAddRequest);
            // 处理密码加密和加盐
            if (userAddRequest.getPassword() != null && !userAddRequest.getPassword().isEmpty()) {
                String salt = PasswordUtil.generateSalt();
                String encodedPassword = PasswordUtil.encodePassword(userAddRequest.getPassword(), salt);
                user.setPassword(encodedPassword);
                user.setSalt(salt);
            }
            
            // 设置默认权限（如果未提供）
            if (userAddRequest.getDefaultPermissions() == null) {
                user.setDefaultPermissions(UserPermissionEnum.GUEST); // 默认访客权限
            }
            
            // 设置默认启用状态（如果未提供）
            if (userAddRequest.getEnable() == null) {
                user.setEnable(UserStatusEnum.ENABLE); // 默认启用
            }
            
            boolean save = userService.save(user);
            if (save) {
                logger.info("用户添加成功");
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户添加失败");
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            logger.error("添加用户时发生异常，请求参数: {}", userAddRequest, e);
            return Result.fail("添加失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID删除用户
     */
    @Operation(summary = "根据ID删除用户")
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUser(@PathVariable("id") Integer id) {
        try {
            logger.info("开始删除用户，ID: {}", id);
            
            if (id == null) {
                logger.warn("删除用户时ID为空");
                return Result.fail("用户ID不能为空");
            }
            
            boolean remove = userService.removeById(id);
            if (remove) {
                logger.info("用户删除成功，ID: {}", id);
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户删除失败，ID: {}", id);
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户时发生异常，ID: {}", id, e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户名删除用户
     */
    @Operation(summary = "根据用户名删除用户")
    @DeleteMapping("/delete/username/{username}")
    public Result<?> deleteUserByUsername(@PathVariable("username") String username) {
        try {
            logger.info("开始根据用户名删除用户，用户名: {}", username);
            
            if (username == null || username.trim().isEmpty()) {
                logger.warn("根据用户名删除用户时用户名为空");
                return Result.fail("用户名不能为空");
            }
            
            boolean remove = userService.removeUserByUsername(username);
            if (remove) {
                logger.info("根据用户名删除用户成功，用户名: {}", username);
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("根据用户名删除用户失败，用户名: {}", username);
                return Result.fail("删除失败，用户可能不存在");
            }
        } catch (Exception e) {
            logger.error("根据用户名删除用户时发生异常，用户名: {}", username, e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody @Validated UserUpdateRequest userUpdateRequest) {
        try {
            logger.info("开始更新用户，请求参数: {}", userUpdateRequest);
            
            if (userUpdateRequest == null) {
                logger.warn("更新用户时请求参数为空");
                return Result.fail("请求参数不能为空");
            }
            
            if (userUpdateRequest.getId() == null) {
                logger.warn("更新用户时ID为空");
                return Result.fail("用户ID不能为空");
            }
            
            User user = UserConvert.INSTANCE.convertToUser(userUpdateRequest);
            // 如果密码为空，则不更新密码
            if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
                // 处理密码加密和加盐
                String salt = PasswordUtil.generateSalt();
                String encodedPassword = PasswordUtil.encodePassword(userUpdateRequest.getPassword(), salt);
                user.setPassword(encodedPassword);
                user.setSalt(salt);
            }
            
            boolean update = userService.updateById(user);
            if (update) {
                logger.info("用户更新成功，ID: {}", userUpdateRequest.getId());
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户更新失败，ID: {}", userUpdateRequest.getId());
                return Result.fail("更新失败");
            }
        } catch (Exception e) {
            logger.error("更新用户时发生异常，请求参数: {}", userUpdateRequest, e);
            return Result.fail("更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     */
    @Operation(summary = "根据ID查询用户")
    @GetMapping("/get/{id}")
    public Result<?> getUserById(@PathVariable("id") Integer id) {
        try {
            logger.info("开始根据ID查询用户，ID: {}", id);
            
            if (id == null) {
                logger.warn("根据ID查询用户时ID为空");
                return Result.fail("用户ID不能为空");
            }
            
            User user = userService.getUserById(id);
            if (user != null) {
                logger.debug("从数据库查询到的用户信息: {}", user);
                UserResponse userResponse = UserConvert.INSTANCE.convertToUserResponse(user);
                logger.debug("转换后的用户响应信息: {}", userResponse);
                logger.info("根据ID查询用户成功，ID: {}", id);
                return Result.success(userResponse); // 返回单条数据
            } else {
                logger.warn("根据ID查询用户失败，未找到用户，ID: {}", id);
                return Result.fail("用户不存在");
            }
        } catch (Exception e) {
            logger.error("根据ID查询用户时发生异常，ID: {}", id, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有用户
     */
    @Operation(summary = "查询所有用户")
    @GetMapping("/list")
    public Result<?> listUsers() {
        try {
            logger.info("开始查询所有用户");
            
            List<User> userList = userService.list();
            logger.debug("从数据库查询到的用户列表: {}", userList);
            List<UserResponse> userResponseList = UserConvert.INSTANCE.convertToUserResponseList(userList);
            logger.debug("转换后的用户响应列表: {}", userResponseList);
            
            logger.info("查询所有用户成功，共查询到 {} 条记录", userList.size());
            // 根据返回数据条数，分配使用哪个result
            if (userList.isEmpty()) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(userResponseList, (long) userList.size()); // 返回多条数据及数量
            }
        } catch (Exception e) {
            logger.error("查询所有用户时发生异常", e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户
     */
    @Operation(summary = "根据用户名查询用户")
    @GetMapping("/get/username/{username}")
    public Result<?> getUserByUsername(@PathVariable("username") String username) {
        try {
            logger.info("开始根据用户名查询用户，用户名: {}", username);
            
            if (username == null || username.trim().isEmpty()) {
                logger.warn("根据用户名查询用户时用户名为空");
                return Result.fail("用户名不能为空");
            }
            
            User user = userService.getUserByUsername(username);
            if (user != null) {
                logger.debug("从数据库查询到的用户信息: {}", user);
                UserResponse userResponse = UserConvert.INSTANCE.convertToUserResponse(user);
                logger.debug("转换后的用户响应信息: {}", userResponse);
                logger.info("根据用户名查询用户成功，用户名: {}", username);
                return Result.success(userResponse); // 返回单条数据
            } else {
                logger.warn("根据用户名查询用户失败，未找到用户，用户名: {}", username);
                return Result.fail("用户不存在");
            }
        } catch (Exception e) {
            logger.error("根据用户名查询用户时发生异常，用户名: {}", username, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据启用状态查询用户
     */
    @Operation(summary = "根据启用状态查询用户")
    @GetMapping("/list/enable/{enable}")
    public Result<?> listUsersByEnableStatus(@PathVariable("enable") Boolean enable) {
        try {
            logger.info("开始根据启用状态查询用户，启用状态: {}", enable);
            
            UserStatusEnum status = enable ? UserStatusEnum.ENABLE : UserStatusEnum.DISABLE;
            List<User> userList = userService.listUsersByEnableStatus(status);
            logger.debug("从数据库查询到的用户列表: {}", userList);
            List<UserResponse> userResponseList = UserConvert.INSTANCE.convertToUserResponseList(userList);
            logger.debug("转换后的用户响应列表: {}", userResponseList);
            
            logger.info("根据启用状态查询用户成功，启用状态: {}，共查询到 {} 条记录", enable, userList.size());
            // 根据返回数据条数，分配使用哪个result
            if (userList.isEmpty()) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(userResponseList, (long) userList.size()); // 返回多条数据及数量
            }
        } catch (Exception e) {
            logger.error("根据启用状态查询用户时发生异常，启用状态: {}", enable, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据权限状态查询用户
     */
    @Operation(summary = "根据权限状态查询用户")
    @GetMapping("/list/permissions/{permissions}")
    public Result<?> listUsersByPermissions(@PathVariable("permissions") String permissions) {
        try {
            logger.info("开始根据权限状态查询用户，权限状态: {}", permissions);
            
            UserPermissionEnum permission = UserPermissionEnum.fromCode(permissions);
            if (permission != null) {
                List<User> userList = userService.listUsersByPermissions(permission);
                logger.debug("从数据库查询到的用户列表: {}", userList);
                List<UserResponse> userResponseList = UserConvert.INSTANCE.convertToUserResponseList(userList);
                logger.debug("转换后的用户响应列表: {}", userResponseList);
                
                logger.info("根据权限状态查询用户成功，权限状态: {}，共查询到 {} 条记录", permissions, userList.size());
                // 根据返回数据条数，分配使用哪个result
                if (userList.isEmpty()) {
                    return Result.success(); // 没有数据也返回成功
                } else {
                    return Result.success(userResponseList, (long) userList.size()); // 返回多条数据及数量
                }
            } else {
                logger.warn("根据权限状态查询用户失败，无效的权限参数: {}", permissions);
                return Result.fail("无效的权限参数");
            }
        } catch (Exception e) {
            logger.error("根据权限状态查询用户时发生异常，权限状态: {}", permissions, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询用户
     */
    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public Result<?> pageUsers(UserPageRequest userPageRequest) {
        try {
            logger.info("开始分页查询用户，请求参数: {}", userPageRequest);
            
            if (userPageRequest == null) {
                userPageRequest = new UserPageRequest(); // 使用默认参数
                logger.debug("分页查询参数为空，使用默认参数");
            }
            
            IPage<User> userIPage = userService.pageUsers(userPageRequest);
            // 构造分页结果
            PageResult<UserResponse> pageResult = UserConvert.INSTANCE.convertToPageResult(userIPage);
            
            logger.info("分页查询用户成功，总记录数: {}", userIPage.getTotal());
            // 根据返回数据条数，分配使用哪个result
            if (userIPage.getTotal() <= 0) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(pageResult, userIPage.getTotal()); // 返回分页数据及总数
            }
        } catch (Exception e) {
            logger.error("分页查询用户时发生异常，请求参数: {}", userPageRequest, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户")
    @DeleteMapping("/delete/batch")
    public Result<?> removeUsersByIds(@RequestParam("ids") List<Integer> ids) {
        try {
            logger.info("开始批量删除用户，IDs数量: {}", ids != null ? ids.size() : 0);
            
            if (ids == null || ids.isEmpty()) {
                logger.warn("批量删除用户时ID列表为空");
                return Result.fail("请选择要删除的用户");
            }
            
            boolean remove = userService.removeUsersByIds(ids);
            if (remove) {
                logger.info("批量删除用户成功，删除数量: {}", ids.size());
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("批量删除用户失败");
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            logger.error("批量删除用户时发生异常，IDs: {}", ids, e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }
}