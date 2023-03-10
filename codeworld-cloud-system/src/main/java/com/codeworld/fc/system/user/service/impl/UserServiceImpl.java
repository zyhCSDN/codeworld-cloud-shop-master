package com.codeworld.fc.system.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.common.domain.LoginInfoData;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.enums.StatusEnum;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.CodecUtils;
import com.codeworld.fc.common.utils.IDGeneratorUtil;

import com.codeworld.fc.system.interceptor.AuthInterceptor;
import com.codeworld.fc.system.role.mapper.RoleMapper;
import com.codeworld.fc.system.user.dto.UserDeptResponse;
import com.codeworld.fc.system.user.dto.UserResponse;
import com.codeworld.fc.system.user.entity.User;
import com.codeworld.fc.system.user.entity.UserDept;
import com.codeworld.fc.system.user.entity.UserRole;
import com.codeworld.fc.system.user.mapper.UserDeptMapper;
import com.codeworld.fc.system.user.mapper.UserMapper;
import com.codeworld.fc.system.user.request.UserRegisterRequest;
import com.codeworld.fc.system.user.request.UserSearchRequest;
import com.codeworld.fc.system.user.request.UserUpdateRequest;
import com.codeworld.fc.system.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName UserServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/11/26
 * Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private UserDeptMapper userDeptMapper;
    @Autowired(required = false)
    private RoleMapper roleMapper;

    /**
     * ???????????????????????????
     *
     * @param username
     * @return
     */
    public FCResponse<User> getUserByName(String username) {
        User user;
        try {
//            int a=1;
//            int ab= a/0;
            if (StringUtils.isBlank(username)) {
                return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.user.USER_NAME_ERROR.getMsg());
            }
             user = this.userMapper.getUserByName(username);
            if (ObjectUtils.isEmpty(user)) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.user.USER_NO_EXIST.getMsg());
            }
        }catch (Exception e){
            throw new FCException(e.getMessage());
        }

        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_GET_SUCCESS.getMsg(), user);
    }

    /**
     * ????????????????????????
     *
     * @param userSearchRequest
     * @return
     */
    public FCResponse<DataResponse<List<UserResponse>>> page(UserSearchRequest userSearchRequest) {
        PageHelper.startPage(userSearchRequest.getPage(), userSearchRequest.getLimit());
        List<UserResponse> users = this.userMapper.page(userSearchRequest);
        if (CollectionUtils.isEmpty(users)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "????????????", DataResponse.dataResponse(users, 0L));
        }
        PageInfo<UserResponse> pageInfo = new PageInfo<>(users);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "????????????", DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ??????????????????
     *
     * @param userId
     * @param userStatus
     * @return
     */
    @Override
    public FCResponse<Void> updateUserStatus(Long userId, Integer userStatus) {
        if (ObjectUtils.isEmpty(userId) || userId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), "????????????");
        }
        try {
            User user = new User();
            user.setUserId(userId);
            user.setUserStatus(userStatus);
            this.userMapper.updateUserStatus(user);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_STATUS_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????
     *
     * @param userId
     * @return
     */
    @Override
    public FCResponse<Void> deleteUser(Long userId) {
        if (ObjectUtils.isEmpty(userId) || userId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.user.USER_ID_ERROR.getMsg());
        }
        try {
            // ??????????????????????????????
            this.roleMapper.deleteUserRoleByUserId(userId);
            // ??????????????????????????????
            this.userDeptMapper.deleteUserDeptByUserId(userId);
            // ????????????
            this.userMapper.deleteUser(userId);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_DELETE_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????
     *
     * @param userRegisterRequest
     * @return
     */
    @Override
    public FCResponse<Void> addUser(UserRegisterRequest userRegisterRequest) {
        try {

            // ???????????????????????????
            User existUser = this.userMapper.getUserByName(userRegisterRequest.getUserName());
            if (existUser != null) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(), HttpMsg.user.USER_EXIST.getMsg());
            }
            // ?????????????????????????????????
            if (this.userMapper.existsUserPhone(userRegisterRequest.getUserPhone()) > 0) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(), HttpMsg.user.USER_PHONE_EXIST.getMsg());
            }
            User user = new User();
            BeanUtil.copyProperties(userRegisterRequest, user);
            user.setUserId(IDGeneratorUtil.getNextId());
            user.setCreateTime(new Date());
            user.setUpdateTime(user.getCreateTime());
            // ???????????????123456
            String salt = CodecUtils.generateSalt();
            // ?????????????????????
            String password = CodecUtils.md5Hex("123456", salt);
            user.setPassword(password);
            user.setPasswordSalt(salt);
            this.userMapper.addUser(user);

            // ??????????????????
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(IDGeneratorUtil.getNextId());
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(userRegisterRequest.getRoleId());
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(userRole.getCreateTime());
            // ??????????????????
            this.roleMapper.addUserRole(userRole);

            // ??????????????????
            UserDept userDept = new UserDept();
            userDept.setUserDeptId(IDGeneratorUtil.getNextId());
            userDept.setUserId(user.getUserId());
            userDept.setDeptId(userRegisterRequest.getDeptIds());
            userDept.setCreateTime(new Date());
            userDept.setUpdateTime(userDept.getCreateTime());
            this.userDeptMapper.addUserDept(userDept);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_ADD_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????
     *
     * @param userUpdateRequest
     * @return
     */
    @Override
    public FCResponse<Void> updateUser(UserUpdateRequest userUpdateRequest) {
        try {
            User user = new User();
            user.setUserId(userUpdateRequest.getUserId());
            user.setUserName(userUpdateRequest.getUserName());
            user.setUserEmail(userUpdateRequest.getUserEmail());
            user.setUserPhone(userUpdateRequest.getUserPhone());
            user.setUpdateTime(new Date());
            user.setUserStatus(userUpdateRequest.getUserStatus());
            user.setAreaId(userUpdateRequest.getAreaId());
            user.setAreaName(userUpdateRequest.getAreaName());
            user.setRemark(userUpdateRequest.getRemark());
            this.userMapper.updateUser(user);
            // ??????????????????
            // ???????????????id
            this.roleMapper.deleteUserRoleByUserId(user.getUserId());
            // ??????????????????
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(IDGeneratorUtil.getNextId());
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(userUpdateRequest.getRoleId());
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(userRole.getCreateTime());
            this.roleMapper.addUserRole(userRole);
            // ????????????
            // ?????????????????????
            this.userDeptMapper.deleteUserDeptByUserId(user.getUserId());
            // ????????????????????????
            UserDept userDept = new UserDept();
            userDept.setUserDeptId(IDGeneratorUtil.getNextId());
            userDept.setUserId(user.getUserId());
            userDept.setDeptId(userUpdateRequest.getDeptIds());
            userDept.setCreateTime(new Date());
            userDept.setUpdateTime(userDept.getCreateTime());
            this.userDeptMapper.addUserDept(userDept);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_UPDATE_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????Id??????????????????
     *
     * @param deptId
     * @return
     */
    @Override
    public FCResponse<List<UserDeptResponse>> getUserByDeptId(Long deptId) {
        if (ObjectUtils.isEmpty(deptId) || deptId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.dept.DEPT_PARAM_ERROR.getMsg(), null);
        }
        List<UserDeptResponse> userDepts = this.userMapper.getUserByDeptId(deptId);
        if (CollectionUtils.isEmpty(userDepts)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USE_DATA_EMPTY.getMsg(), userDepts);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_GET_SUCCESS.getMsg(), userDepts);
    }

    /**
     * ??????????????????????????????
     *
     * @param userName
     * @return
     */
    @Override
    public FCResponse<List<User>> getUserRoleToMerchant(String userName) {
        List<User> users = this.userMapper.getUserRoleToMerchant(userName);
        if (CollectionUtils.isEmpty(users)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.user.USE_DATA_EMPTY.getMsg(), users);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_GET_SUCCESS.getMsg(), users);
    }

    /**
     * ????????????id??????????????????
     *
     * @param userId
     * @return
     */
    @Override
    public FCResponse<User> getUserById(Long userId) {
        if (ObjectUtils.isEmpty(userId) || userId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.user.USER_ID_ERROR.getMsg());
        }
        User user = this.userMapper.getUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.user.USER_NO_EXIST.getMsg());
        }
        if (user.getUserStatus().equals(StatusEnum.USER_DISABLE)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.user.USER_DISABLE.getMsg());
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_GET_SUCCESS.getMsg(), user);
    }

    /**
     * ???????????????????????????
     *
     * @param userName
     * @return
     */
    @Override
    public FCResponse<List<User>> getAreaMerchantUser(String userName) {
        LoginInfoData loginInfoData = AuthInterceptor.getLoginInfo();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.user.USER_AUTH_ERROR.getMsg());
        }
        // ????????????????????????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("userId", loginInfoData.getId());
        map.put("userName", userName);
        List<User> users = this.userMapper.getAreaMerchantUser(map);
        if (CollectionUtils.isEmpty(users)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.user.USE_DATA_EMPTY.getMsg());
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_GET_SUCCESS.getMsg(), users);
    }
}
