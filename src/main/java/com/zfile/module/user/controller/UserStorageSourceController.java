package com.zfile.module.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.zfile.common.result.PageResult;
import com.zfile.common.result.Result;
import com.zfile.module.user.entity.UserStorageSource;
import com.zfile.module.user.request.UserStorageSourceAddRequest;
import com.zfile.module.user.request.UserStorageSourcePageRequest;
import com.zfile.module.user.request.UserStorageSourceUpdateRequest;
import com.zfile.module.user.response.UserStorageSourceResponse;
import com.zfile.module.user.convert.UserStorageSourceConvert;
import com.zfile.module.user.service.UserStorageSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Tag(name = "用户存储源接口")
@Slf4j
@ApiSort(2)
@RequestMapping("/user/storage-source")
@RestController
@Validated
public class UserStorageSourceController {
    //构造器中注入
    private final UserStorageSourceService userStorageSourceService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserStorageSourceController.class);

    @Autowired
    public UserStorageSourceController(UserStorageSourceService userStorageSourceService) {
        this.userStorageSourceService = userStorageSourceService;
    }

    /**
     * 添加用户存储源
     */
    @Operation(summary = "添加用户存储源")
    @PostMapping("/add")
    public Result<?> addUserStorageSource(@RequestBody @Validated UserStorageSourceAddRequest userStorageSourceAddRequest) {
        try {
            logger.info("开始添加用户存储源，请求参数: {}", userStorageSourceAddRequest);
            
            if (userStorageSourceAddRequest == null) {
                logger.warn("添加用户存储源时请求参数为空");
                return Result.fail("请求参数不能为空");
            }
            
            UserStorageSource userStorageSource = UserStorageSourceConvert.INSTANCE.convertToUserStorageSource(userStorageSourceAddRequest);
            boolean save = userStorageSourceService.save(userStorageSource);
            if (save) {
                logger.info("用户存储源添加成功");
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户存储源添加失败");
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            logger.error("添加用户存储源时发生异常，请求参数: {}", userStorageSourceAddRequest, e);
            return Result.fail("添加失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID删除用户存储源
     */
    @Operation(summary = "根据ID删除用户存储源")
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteUserStorageSource(@PathVariable("id") Integer id) {
        try {
            logger.info("开始删除用户存储源，ID: {}", id);
            
            if (id == null) {
                logger.warn("删除用户存储源时ID为空");
                return Result.fail("用户存储源ID不能为空");
            }
            
            boolean remove = userStorageSourceService.removeById(id);
            if (remove) {
                logger.info("用户存储源删除成功，ID: {}", id);
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户存储源删除失败，ID: {}", id);
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户存储源时发生异常，ID: {}", id, e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户存储源信息
     */
    @Operation(summary = "更新用户存储源信息")
    @PutMapping("/update")
    public Result<?> updateUserStorageSource(@RequestBody @Validated UserStorageSourceUpdateRequest userStorageSourceUpdateRequest) {
        try {
            logger.info("开始更新用户存储源，请求参数: {}", userStorageSourceUpdateRequest);
            
            if (userStorageSourceUpdateRequest == null) {
                logger.warn("更新用户存储源时请求参数为空");
                return Result.fail("请求参数不能为空");
            }
            
            if (userStorageSourceUpdateRequest.getId() == null) {
                logger.warn("更新用户存储源时ID为空");
                return Result.fail("用户存储源ID不能为空");
            }
            
            UserStorageSource userStorageSource = UserStorageSourceConvert.INSTANCE.convertToUserStorageSource(userStorageSourceUpdateRequest);
            boolean update = userStorageSourceService.updateById(userStorageSource);
            if (update) {
                logger.info("用户存储源更新成功，ID: {}", userStorageSourceUpdateRequest.getId());
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("用户存储源更新失败，ID: {}", userStorageSourceUpdateRequest.getId());
                return Result.fail("更新失败");
            }
        } catch (Exception e) {
            logger.error("更新用户存储源时发生异常，请求参数: {}", userStorageSourceUpdateRequest, e);
            return Result.fail("更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户存储源
     */
    @Operation(summary = "根据ID查询用户存储源")
    @GetMapping("/get/{id}")
    public Result<?> getUserStorageSourceById(@PathVariable("id") Integer id) {
        try {
            logger.info("开始根据ID查询用户存储源，ID: {}", id);
            
            if (id == null) {
                logger.warn("根据ID查询用户存储源时ID为空");
                return Result.fail("用户存储源ID不能为空");
            }
            
            UserStorageSource userStorageSource = userStorageSourceService.getById(id);
            if (userStorageSource != null) {
                logger.debug("从数据库查询到的用户存储源信息: {}", userStorageSource);
                UserStorageSourceResponse userStorageSourceResponse = UserStorageSourceConvert.INSTANCE.convertToUserStorageSourceResponse(userStorageSource);
                logger.debug("转换后的用户存储源响应信息: {}", userStorageSourceResponse);
                logger.info("根据ID查询用户存储源成功，ID: {}", id);
                return Result.success(userStorageSourceResponse); // 返回单条数据
            } else {
                logger.warn("根据ID查询用户存储源失败，未找到用户存储源，ID: {}", id);
                return Result.fail("用户存储源不存在");
            }
        } catch (Exception e) {
            logger.error("根据ID查询用户存储源时发生异常，ID: {}", id, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有用户存储源
     */
    @Operation(summary = "查询所有用户存储源")
    @GetMapping("/list")
    public Result<?> listUserStorageSources() {
        try {
            logger.info("开始查询所有用户存储源");
            
            List<UserStorageSource> userStorageSourceList = userStorageSourceService.list();
            logger.debug("从数据库查询到的用户存储源列表: {}", userStorageSourceList);
            List<UserStorageSourceResponse> userStorageSourceResponseList = UserStorageSourceConvert.INSTANCE.convertToUserStorageSourceResponseList(userStorageSourceList);
            logger.debug("转换后的用户存储源响应列表: {}", userStorageSourceResponseList);
            
            logger.info("查询所有用户存储源成功，共查询到 {} 条记录", userStorageSourceList.size());
            // 根据返回数据条数，分配使用哪个result
            if (userStorageSourceList.isEmpty()) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(userStorageSourceResponseList, (long) userStorageSourceList.size()); // 返回多条数据及数量
            }
        } catch (Exception e) {
            logger.error("查询所有用户存储源时发生异常", e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询用户存储源
     */
    @Operation(summary = "根据用户ID查询用户存储源")
    @GetMapping("/list/user/{userId}")
    public Result<?> listUserStorageSourcesByUserId(@PathVariable("userId") Integer userId) {
        try {
            logger.info("开始根据用户ID查询用户存储源，用户ID: {}", userId);
            
            if (userId == null) {
                logger.warn("根据用户ID查询用户存储源时用户ID为空");
                return Result.fail("用户ID不能为空");
            }
            
            List<UserStorageSource> userStorageSourceList = userStorageSourceService.listUserStorageSourcesByUserId(userId);
            logger.debug("从数据库查询到的用户存储源列表: {}", userStorageSourceList);
            List<UserStorageSourceResponse> userStorageSourceResponseList = UserStorageSourceConvert.INSTANCE.convertToUserStorageSourceResponseList(userStorageSourceList);
            logger.debug("转换后的用户存储源响应列表: {}", userStorageSourceResponseList);
            
            logger.info("根据用户ID查询用户存储源成功，用户ID: {}，共查询到 {} 条记录", userId, userStorageSourceList.size());
            // 根据返回数据条数，分配使用哪个result
            if (userStorageSourceList.isEmpty()) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(userStorageSourceResponseList, (long) userStorageSourceList.size()); // 返回多条数据及数量
            }
        } catch (Exception e) {
            logger.error("根据用户ID查询用户存储源时发生异常，用户ID: {}", userId, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据启用状态查询用户存储源
     */
    @Operation(summary = "根据启用状态查询用户存储源")
    @GetMapping("/list/enable/{enable}")
    public Result<?> listUserStorageSourcesByEnableStatus(@PathVariable("enable") Boolean enable) {
        try {
            logger.info("开始根据启用状态查询用户存储源，启用状态: {}", enable);
            
            List<UserStorageSource> userStorageSourceList = userStorageSourceService.listUserStorageSourcesByEnableStatus(enable);
            logger.debug("从数据库查询到的用户存储源列表: {}", userStorageSourceList);
            List<UserStorageSourceResponse> userStorageSourceResponseList = UserStorageSourceConvert.INSTANCE.convertToUserStorageSourceResponseList(userStorageSourceList);
            logger.debug("转换后的用户存储源响应列表: {}", userStorageSourceResponseList);
            
            logger.info("根据启用状态查询用户存储源成功，启用状态: {}，共查询到 {} 条记录", enable, userStorageSourceList.size());
            // 根据返回数据条数，分配使用哪个result
            if (userStorageSourceList.isEmpty()) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(userStorageSourceResponseList, (long) userStorageSourceList.size()); // 返回多条数据及数量
            }
        } catch (Exception e) {
            logger.error("根据启用状态查询用户存储源时发生异常，启用状态: {}", enable, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户存储源
     */
    @Operation(summary = "分页查询用户存储源")
    @GetMapping("/page")
    public Result<?> pageUserStorageSources(UserStorageSourcePageRequest userStorageSourcePageRequest) {
        try {
            logger.info("开始分页查询用户存储源，请求参数: {}", userStorageSourcePageRequest);
            
            if (userStorageSourcePageRequest == null) {
                userStorageSourcePageRequest = new UserStorageSourcePageRequest(); // 使用默认参数
                logger.debug("分页查询参数为空，使用默认参数");
            }
            
            IPage<UserStorageSource> userStorageSourceIPage = userStorageSourceService.pageUserStorageSources(userStorageSourcePageRequest);
            // 构造分页结果
            PageResult<UserStorageSourceResponse> pageResult = UserStorageSourceConvert.INSTANCE.convertToPageResult(userStorageSourceIPage);
            
            logger.info("分页查询用户存储源成功，总记录数: {}", userStorageSourceIPage.getTotal());
            // 根据返回数据条数，分配使用哪个result
            if (userStorageSourceIPage.getTotal() <= 0) {
                return Result.success(); // 没有数据也返回成功
            } else {
                return Result.success(pageResult, userStorageSourceIPage.getTotal()); // 返回分页数据及总数
            }
        } catch (Exception e) {
            logger.error("分页查询用户存储源时发生异常，请求参数: {}", userStorageSourcePageRequest, e);
            return Result.fail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户存储源
     */
    @Operation(summary = "批量删除用户存储源")
    @DeleteMapping("/delete/batch")
    public Result<?> removeUserStorageSourcesByIds(@RequestParam("ids") List<Integer> ids) {
        try {
            logger.info("开始批量删除用户存储源，IDs数量: {}", ids != null ? ids.size() : 0);
            
            if (ids == null || ids.isEmpty()) {
                logger.warn("批量删除用户存储源时ID列表为空");
                return Result.fail("请选择要删除的用户存储源");
            }
            
            boolean remove = userStorageSourceService.removeUserStorageSourcesByIds(ids);
            if (remove) {
                logger.info("批量删除用户存储源成功，删除数量: {}", ids.size());
                return Result.success(); // 不需要返回数据的成功操作
            } else {
                logger.warn("批量删除用户存储源失败");
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            logger.error("批量删除用户存储源时发生异常，IDs: {}", ids, e);
            return Result.fail("删除失败: " + e.getMessage());
        }
    }
}