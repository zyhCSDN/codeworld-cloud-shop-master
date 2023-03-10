package com.codeworld.fc.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.common.domain.LoginInfoData;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.CodecUtils;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.common.utils.StringUtil;
import com.codeworld.fc.client.RoleClient;
import com.codeworld.fc.merchant.domain.UserRole;
import com.codeworld.fc.merchant.entity.MerChantDetail;
import com.codeworld.fc.merchant.entity.Merchant;
import com.codeworld.fc.interceptor.AuthInterceptor;
import com.codeworld.fc.merchant.entity.MerchantWxInfo;
import com.codeworld.fc.merchant.mapper.MerChantDetailMapper;
import com.codeworld.fc.merchant.mapper.MerchantMapper;
import com.codeworld.fc.merchant.request.*;
import com.codeworld.fc.merchant.response.MerchantResponse;
import com.codeworld.fc.merchant.service.MerchantService;
import com.codeworld.fc.store.mapper.StoreMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * ClassName MerchantServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/12/30
 * Version 1.0
 **/
@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    private static final String PHONE_CODE = "PHONE_CODE:";
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;
    @Autowired(required = false)
    private MerchantMapper merchantMapper;
    @Autowired(required = false)
    private MerChantDetailMapper merChantDetailMapper;
    @Autowired(required = false)
    private StoreMapper storeMapper;

    @Autowired(required = false)
    private RoleClient roleClient;

    /**
     * ????????????
     *
     * @param merchantAddRequest
     * @return
     */
    public FCResponse<Void> merchantSettled(MerchantAddRequest merchantAddRequest) {
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        // ????????????Id???????????????
        String number = this.merchantMapper.getMerchantByMerchantId(loginInfoData.getId());
        // ????????????????????????
        Integer status = this.merChantDetailMapper.getMerchantStatusByMerchantNumber(number);
        if (status != 3) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(), HttpMsg.merchant.MERCHANT_EXIST.getMsg());
        }
        MerChantDetail merChantDetail = new MerChantDetail();
        BeanUtil.copyProperties(merchantAddRequest, merChantDetail);
        merChantDetail.setMerchantNumber(number);
        // ??????????????????
        merChantDetail.setStatus(2);
        merChantDetail.setCreateTime(new Date());
        this.merChantDetailMapper.merchantSettled(merChantDetail);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_SETTLED_ADD_SUCCESS.getMsg());
    }

    /**
     * ????????????
     *
     * @param merchantRegisterRequest
     * @return
     */
    public FCResponse<String> registerMerchantWeb(MerchantRegisterRequest merchantRegisterRequest) {
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        // ???????????????????????????????????????????????????
        Integer count = this.merchantMapper.checkMerchantByPhone(merchantRegisterRequest.getPhone());
        if (count != 0) {
            // ??????redis?????????????????????
            this.stringRedisTemplate.delete(PHONE_CODE + merchantRegisterRequest.getPhone());
            return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(), HttpMsg.merchant.MERCHANT_PHONE_REGISTER.getMsg());
        }
        // ???Redis??????????????????
        // ???????????????????????????
        if (!this.stringRedisTemplate.hasKey(PHONE_CODE + merchantRegisterRequest.getPhone())) {
            log.info("?????????????????????" + merchantRegisterRequest.getPhone());
            return FCResponse.dataResponse(HttpFcStatus.VALIDATEFAILCODE.getCode(), HttpMsg.sms.SMS_CODE_EXPIRE.getMsg());
        }
        String code = this.stringRedisTemplate.opsForValue().get(PHONE_CODE + merchantRegisterRequest.getPhone());
        // ???????????????
        if (!StringUtils.equals(merchantRegisterRequest.getCode(), code)) {
            log.info("???????????????");
            return FCResponse.dataResponse(HttpFcStatus.VALIDATEFAILCODE.getCode(), HttpMsg.sms.SMS_CODE_ERROR.getMsg());
        }
        // ????????????????????????
        Merchant merchant = new Merchant();
        merchant.setId(IDGeneratorUtil.getMerchantId());
        merchant.setNumber(String.valueOf(IDGeneratorUtil.getMerchantNumber()));
        merchant.setNickName(merchantRegisterRequest.getNickName());
        merchant.setPhone(merchantRegisterRequest.getPhone());
        merchant.setMerchantFollowUser(loginInfoData.getId());
        // ????????????
        String salt = CodecUtils.generateSalt();
        // ?????????????????????
        String password = CodecUtils.md5Hex(merchantRegisterRequest.getPassword(), salt);
        // ????????????
        merchant.setPasswordSalt(salt);
        // ????????????
        merchant.setPassword(password);
        merchant.setCreateTime(new Date());
        this.merchantMapper.registerMerchant(merchant);
        // ??????????????????????????????
        MerChantDetail merChantDetail = new MerChantDetail();
        merChantDetail.setMerchantNumber(merchant.getNumber());
        merChantDetail.setStatus(3);
        this.merChantDetailMapper.addMerchantDetail(merChantDetail);
        // ?????????????????????
        // ???????????? ???????????? ---982301
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(IDGeneratorUtil.getNextId());
        userRole.setRoleId(982301L);
        userRole.setUserId(merchant.getId());
        userRole.setCreateTime(new Date());
        userRole.setUpdateTime(userRole.getCreateTime());
        this.roleClient.addUserRole(userRole);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_ADD_SUCCESS.getMsg(), merchant.getNumber());
    }

    /**
     * ????????????????????????????????????
     *
     * @param phone
     * @return
     */
    public FCResponse<Integer> checkMerchantByPhone(String phone) {
        // ???????????????
        if (!StringUtil.checkPhone(phone)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.sms.SMS_PHONE_ERROR.getMsg(), 0);
        }
        Integer count = this.merchantMapper.checkMerchantByPhone(phone);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), count);
    }

    /**
     * ?????????????????????????????????
     *
     * @param phone
     * @return
     */
    public FCResponse<MerchantResponse> getMerchantByPhone(String phone) {
        // ???????????????
        if (!StringUtil.checkPhone(phone)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.sms.SMS_PHONE_ERROR.getMsg(), null);
        }
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantByPhone(phone);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), merchantResponse);
    }

    /**
     * ??????????????????
     *
     * @param merchantSearchRequest
     * @return
     */
    @Override
    public FCResponse<DataResponse<List<MerchantResponse>>> getPageMerchant(MerchantSearchRequest merchantSearchRequest) {
        // ????????????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.user.USER_AUTH_ERROR.getMsg());
        }
        merchantSearchRequest.setMerchantFollowUser(loginInfoData.getId());
        PageHelper.startPage(merchantSearchRequest.getPage(), merchantSearchRequest.getLimit());
        List<MerchantResponse> merchantResponses = this.merchantMapper.getPageMerchant(merchantSearchRequest);
        if (CollectionUtils.isEmpty(merchantResponses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_EMPTY.getMsg(), DataResponse.dataResponse(merchantResponses, 0L));
        }
        PageInfo<MerchantResponse> pageInfo = new PageInfo<>(merchantResponses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param merchantNumber
     * @return
     */
    @Override
    public FCResponse<Integer> checkMerchantByMerchantNumber(String merchantNumber) {
        if (ObjectUtils.isEmpty(merchantNumber)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.merchant.MERCHANT_ID_ERROR.getMsg());
        }
        Integer count = this.merchantMapper.checkMerchantByMerchantNumber(merchantNumber);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), count);
    }

    /**
     * ???????????????????????????
     *
     * @param merchantNumber
     * @return
     */
    @Override
    public FCResponse<MerchantResponse> getMerchantByMerchantNumber(String merchantNumber) {
        if (ObjectUtils.isEmpty(merchantNumber)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.merchant.MERCHANT_ID_ERROR.getMsg());
        }
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantByMerchantNumber(merchantNumber);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), merchantResponse);
    }

    /**
     * ????????????
     *
     * @param examineMerchantRequest
     * @return
     */
    @Override
    public FCResponse<Void> examineMerchant(ExamineMerchantRequest examineMerchantRequest) {
        MerChantDetail merChantDetail = new MerChantDetail();
        merChantDetail.setMerchantNumber(examineMerchantRequest.getNumber());
        merChantDetail.setStatus(examineMerchantRequest.getStatus());
        // ??????
        this.merChantDetailMapper.examineMerchant(merChantDetail);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_EXAMINE_SUCCESS.getMsg());
    }

    /**
     * ????????????id??????????????????
     *
     * @param merchantId
     * @return
     */
    @Override
    public FCResponse<MerchantResponse> getMerchantNumberAndNameById(Long merchantId) {

        if (ObjectUtils.isEmpty(merchantId) || merchantId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.merchant.MERCHANT_ID_ERROR.getMsg());
        }
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantNumberAndNameById(merchantId);
        if (ObjectUtils.isEmpty(merchantResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.merchant.MERCHANT_DATA_EMPTY.getMsg());
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), merchantResponse);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public FCResponse<Integer> judgmentMerchantSet() {
        // ????????????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_EXPIRE.getMsg());
        }
        // ????????????id??????????????????
        Integer status = this.merchantMapper.judgmentMerchantSet(loginInfoData.getId());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), status);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @Override
    public FCResponse<MerchantResponse> getMerchantInfo() {
        // ????????????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_EXPIRE.getMsg());
        }
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantInfoById(loginInfoData.getId());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), merchantResponse);
    }

    /**
     * ????????????????????????
     *
     * @param editMerchantInfo
     * @return
     */
    @Override
    public FCResponse<Void> updateMerchantInfo(EditMerchantInfo editMerchantInfo) {

        Merchant merchant = new Merchant();
        merchant.setNumber(editMerchantInfo.getNumber());
        merchant.setAvatar(editMerchantInfo.getAvatar());
        merchant.setMerchantFollowUser(editMerchantInfo.getMerchantFollower());
        merchant.setNickName(editMerchantInfo.getNickName());
        this.merchantMapper.updateMerchantInfo(merchant);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_UPDATE_SUCCESS.getMsg());
    }

    /**
     * ????????????Web???
     *
     * @param transferMerchantRequest
     * @return
     */
    @Override
    public FCResponse<Void> transferMerchantWeb(TransferMerchantRequest transferMerchantRequest) {
        Merchant merchant = new Merchant();
        merchant.setMerchantFollowUser(transferMerchantRequest.getUserId());
        merchant.setNumber(transferMerchantRequest.getMerchantNumber());
        try {
            this.merchantMapper.transferMerchantWeb(merchant);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_TRANSFER_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * Web???????????????
     *
     * @param editPasswordRequest
     * @return
     */
    @Override
    public FCResponse<Void> resetPassword(EditPasswordRequest editPasswordRequest) {
        // ???????????????????????????
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantByPhone(editPasswordRequest.getSetPhone());
        if (ObjectUtils.isEmpty(merchantResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.merchant.MERCHANT_SET_PHONE_ERROR.getMsg());
        }
        // ???????????????????????????
        if (!merchantResponse.getPassword().equals(editPasswordRequest.getOldPassword())) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.merchant.MERCHANT_OLD_PASSWORD_ERROR.getMsg());
        }
        // ????????????
        Merchant merchant = new Merchant();
        merchant.setNumber(merchantResponse.getNumber());
        merchant.setPassword(editPasswordRequest.getNewPassword());
        try {
            this.merchantMapper.resetPassword(merchant);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_UPDATE_PASSWORD_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @Override
    public FCResponse<Boolean> checkMerchantHasStore() {
        // ????????????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMerchant();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_EXPIRE.getMsg());
        }
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantInfoById(loginInfoData.getId());
        if (ObjectUtils.isEmpty(merchantResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_EXPIRE.getMsg());
        }
        Integer count = this.storeMapper.getStoreByMerchantNumber(merchantResponse.getNumber());
        if (count <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.store.STORE_DATA_EMPTY.getMsg(), false);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.store.STORE_DATA_SUCCESS.getMsg(), true);
    }

    /**
     * ??????????????????openId??????????????????
     *
     * @param openId
     * @return
     */
    @Override
    public FCResponse<MerchantResponse> getMerchantByOpenId(String openId) {
        if (ObjectUtils.isEmpty(openId)) {
            log.error("?????????openid??????");
            return FCResponse.validateErrorResponse("openid??????");
        }
        MerchantWxInfo merchantWxInfo = this.merchantMapper.getMerchantByOpenId(openId);
        if (ObjectUtils.isEmpty(merchantWxInfo)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "??????????????????", null);
        }
        // ???????????????????????????
        MerchantResponse merchantResponse = this.merchantMapper.getMerchantByMerchantNumber(merchantWxInfo.getMerchantNumber());
        if (ObjectUtils.isEmpty(merchantResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "??????????????????", null);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "??????????????????", merchantResponse);
    }
}
