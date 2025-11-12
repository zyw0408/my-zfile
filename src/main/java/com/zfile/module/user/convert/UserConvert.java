package com.zfile.module.user.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zfile.common.result.PageResult;
import com.zfile.module.user.entity.User;
import com.zfile.module.user.request.UserAddRequest;
import com.zfile.module.user.request.UserUpdateRequest;
import com.zfile.module.user.response.UserResponse;
import com.zfile.module.user.util.PasswordUtil;
import com.zfile.module.user.userenum.UserStatusEnum;
import com.zfile.module.user.userenum.UserPermissionEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户对象转换类
 */
@Mapper(componentModel = "default")
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * 将 UserAddRequest 转换为 User
     *
     * @param userAddRequest 用户添加请求
     * @return User 实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User convertToUser(UserAddRequest userAddRequest);

    /**
     * 将 UserUpdateRequest 转换为 User
     *
     * @param userUpdateRequest 用户更新请求
     * @return User 实体
     */
    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User convertToUser(UserUpdateRequest userUpdateRequest);

    /**
     * 将 User 转换为 UserResponse
     *
     * @param user 用户实体
     * @return UserResponse 响应对象
     */
    UserResponse convertToUserResponse(User user);

    /**
     * 将 User 列表转换为 UserResponse 列表
     *
     * @param userList 用户实体列表
     * @return UserResponse 响应对象列表
     */
    List<UserResponse> convertToUserResponseList(List<User> userList);

    /**
     * 将 MyBatis Plus 分页结果转换为 PageResult
     *
     * @param userIPage MyBatis Plus 分页结果
     * @return PageResult 分页结果
     */
    @Mapping(target = "total", source = "total")
    @Mapping(target = "currentPage", source = "current")
    @Mapping(target = "pageSize", source = "size")
    @Mapping(target = "totalPages", source = "pages")
    @Mapping(target = "list", source = "records")
    PageResult<UserResponse> convertToPageResult(IPage<User> userIPage);
}