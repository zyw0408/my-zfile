package com.zfile.module.user.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zfile.common.result.PageResult;
import com.zfile.module.user.entity.UserStorageSource;
import com.zfile.module.user.request.UserStorageSourceAddRequest;
import com.zfile.module.user.request.UserStorageSourceUpdateRequest;
import com.zfile.module.user.response.UserStorageSourceResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T15:59:01+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
public class UserStorageSourceConvertImpl implements UserStorageSourceConvert {

    @Override
    public UserStorageSource convertToUserStorageSource(UserStorageSourceAddRequest userStorageSourceAddRequest) {
        if ( userStorageSourceAddRequest == null ) {
            return null;
        }

        UserStorageSource userStorageSource = new UserStorageSource();

        userStorageSource.setUserId( userStorageSourceAddRequest.getUserId() );
        userStorageSource.setStorageSourceId( userStorageSourceAddRequest.getStorageSourceId() );
        userStorageSource.setRootPath( userStorageSourceAddRequest.getRootPath() );
        userStorageSource.setEnable( userStorageSourceAddRequest.getEnable() );
        userStorageSource.setPermissions( userStorageSourceAddRequest.getPermissions() );

        return userStorageSource;
    }

    @Override
    public UserStorageSource convertToUserStorageSource(UserStorageSourceUpdateRequest userStorageSourceUpdateRequest) {
        if ( userStorageSourceUpdateRequest == null ) {
            return null;
        }

        UserStorageSource userStorageSource = new UserStorageSource();

        userStorageSource.setId( userStorageSourceUpdateRequest.getId() );
        userStorageSource.setUserId( userStorageSourceUpdateRequest.getUserId() );
        userStorageSource.setStorageSourceId( userStorageSourceUpdateRequest.getStorageSourceId() );
        userStorageSource.setRootPath( userStorageSourceUpdateRequest.getRootPath() );
        userStorageSource.setEnable( userStorageSourceUpdateRequest.getEnable() );
        userStorageSource.setPermissions( userStorageSourceUpdateRequest.getPermissions() );

        return userStorageSource;
    }

    @Override
    public UserStorageSourceResponse convertToUserStorageSourceResponse(UserStorageSource userStorageSource) {
        if ( userStorageSource == null ) {
            return null;
        }

        UserStorageSourceResponse userStorageSourceResponse = new UserStorageSourceResponse();

        userStorageSourceResponse.setId( userStorageSource.getId() );
        userStorageSourceResponse.setUserId( userStorageSource.getUserId() );
        userStorageSourceResponse.setStorageSourceId( userStorageSource.getStorageSourceId() );
        userStorageSourceResponse.setRootPath( userStorageSource.getRootPath() );
        userStorageSourceResponse.setEnable( userStorageSource.getEnable() );
        userStorageSourceResponse.setPermissions( userStorageSource.getPermissions() );
        userStorageSourceResponse.setCreateTime( userStorageSource.getCreateTime() );
        userStorageSourceResponse.setUpdateTime( userStorageSource.getUpdateTime() );

        return userStorageSourceResponse;
    }

    @Override
    public List<UserStorageSourceResponse> convertToUserStorageSourceResponseList(List<UserStorageSource> userStorageSourceList) {
        if ( userStorageSourceList == null ) {
            return null;
        }

        List<UserStorageSourceResponse> list = new ArrayList<UserStorageSourceResponse>( userStorageSourceList.size() );
        for ( UserStorageSource userStorageSource : userStorageSourceList ) {
            list.add( convertToUserStorageSourceResponse( userStorageSource ) );
        }

        return list;
    }

    @Override
    public PageResult<UserStorageSourceResponse> convertToPageResult(IPage<UserStorageSource> userStorageSourceIPage) {
        if ( userStorageSourceIPage == null ) {
            return null;
        }

        PageResult<UserStorageSourceResponse> pageResult = new PageResult<UserStorageSourceResponse>();

        pageResult.setTotal( userStorageSourceIPage.getTotal() );
        pageResult.setCurrentPage( (int) userStorageSourceIPage.getCurrent() );
        pageResult.setPageSize( (int) userStorageSourceIPage.getSize() );
        pageResult.setTotalPages( (int) userStorageSourceIPage.getPages() );
        pageResult.setList( convertToUserStorageSourceResponseList( userStorageSourceIPage.getRecords() ) );

        return pageResult;
    }
}
