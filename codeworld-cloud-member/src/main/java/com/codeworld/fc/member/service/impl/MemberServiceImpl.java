package com.codeworld.fc.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.common.utils.StringUtil;
import com.codeworld.fc.member.domain.MemberInfo;
import com.codeworld.fc.member.entity.Member;
import com.codeworld.fc.member.entity.MemberDetail;
import com.codeworld.fc.member.entity.ReceiverAddress;
import com.codeworld.fc.member.mapper.MemberDetailMapper;
import com.codeworld.fc.member.mapper.MemberMapper;
import com.codeworld.fc.member.mapper.ReceiverAddressMapper;
import com.codeworld.fc.member.request.MemberRegisterRequest;
import com.codeworld.fc.member.response.MemberReceiverAddressInfo;
import com.codeworld.fc.member.response.MemberResponse;
import com.codeworld.fc.member.service.MemberService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ClassName MemberServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/12/18
 * Version 1.0
 **/
@Service
public class MemberServiceImpl implements MemberService {

    private final String PHONE_CODE = "PHONE_CODE:";
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @Autowired(required = false)
    private MemberMapper memberMapper;

    @Autowired(required = false)
    private ReceiverAddressMapper receiverAddressMapper;

    @Autowired(required = false)
    private MemberDetailMapper memberDetailMapper;


    /**
     * ????????????
     *
     * @param memberRegisterRequest
     * @return
     */
    @Transactional
    public FCResponse<Void> registerMember(MemberRegisterRequest memberRegisterRequest) {
        // ???????????????????????????????????????????????????
        Integer count = this.memberMapper.checkMemberByPhone(memberRegisterRequest.getPhone());
        if (count != 0){
            // ??????redis?????????????????????
            this.stringRedisTemplate.delete(PHONE_CODE + memberRegisterRequest.getPhone());
            return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(),HttpMsg.member.MEMBER_PHONE_REGISTER.getMsg());
        }
        // ???Redis??????????????????
        // ???????????????????????????
        if (!this.stringRedisTemplate.hasKey(PHONE_CODE + memberRegisterRequest.getPhone())) {
            LOGGER.info("?????????????????????" + memberRegisterRequest.getPhone());
            return FCResponse.dataResponse(HttpFcStatus.VALIDATEFAILCODE.getCode(), HttpMsg.sms.SMS_CODE_EXPIRE.getMsg());
        }
        String code = this.stringRedisTemplate.opsForValue().get(PHONE_CODE + memberRegisterRequest.getPhone());
        // ???????????????
        if (!StringUtils.equals(memberRegisterRequest.getVerifyCode(), code)) {
            LOGGER.info("???????????????");
            return FCResponse.dataResponse(HttpFcStatus.VALIDATEFAILCODE.getCode(), HttpMsg.sms.SMS_CODE_ERROR.getMsg());
        }
        // ????????????,????????????
        Member member = new Member();
        member.setId(IDGeneratorUtil.getNextId());
        member.setPhone(memberRegisterRequest.getPhone());
        member.setPassword(memberRegisterRequest.getPassword());
        member.setCreateTime(new Date());
        member.setUpdateTime(member.getCreateTime());
        this.memberMapper.registerMember(member);
        // ??????redis????????????
        this.stringRedisTemplate.delete(PHONE_CODE + memberRegisterRequest.getPhone());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.member.MEMBER_REGISTER_SUCCESS.getMsg());
    }

    /**
     * ???????????????????????????
     *
     * @param phone
     * @return
     */
    public FCResponse<Integer> checkMemberByPhone(String phone) {
        // ???????????????
        if (!StringUtil.checkPhone(phone)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.sms.SMS_PHONE_ERROR.getMsg(), 0);
        }
        Integer count = this.memberMapper.checkMemberByPhone(phone);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.member.MEMBER_DATA_SUCCESS.getMsg(), count);
    }

    /**
     * ???????????????????????????
     *
     * @param phone
     * @return
     */
    public FCResponse<MemberResponse> getMemberByPhone(String phone) {
        // ???????????????
        if (!StringUtil.checkPhone(phone)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.sms.SMS_PHONE_ERROR.getMsg(), null);
        }
        MemberResponse memberResponse = this.memberMapper.getMemberByPhone(phone);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.member.MEMBER_DATA_SUCCESS.getMsg(), memberResponse);
    }

    /**
     * ????????????id???????????????????????????????????????
     *
     * @param memberId
     * @return
     */
    public FCResponse<MemberReceiverAddressInfo> getMemberReceiverAddressByMemberId(Long memberId) {
        if (ObjectUtils.isEmpty(memberId) || memberId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.member.MEMBER_ID_ERROR.getMsg(), null);
        }
        // ????????????id??????????????????
        Member member = this.memberMapper.getMemberById(memberId);
        // ????????????Id??????????????????
        List<ReceiverAddress> receiverAddresses = this.receiverAddressMapper.getReceiverAddressByMemberId(memberId);
        MemberReceiverAddressInfo memberReceiverAddressInfo = new MemberReceiverAddressInfo();
        memberReceiverAddressInfo.setId(member.getId());
        memberReceiverAddressInfo.setName(member.getName());
        memberReceiverAddressInfo.setPhone(member.getPhone());
        memberReceiverAddressInfo.setAddresses(receiverAddresses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(),HttpMsg.member.MEMBER_DATA_SUCCESS.getMsg(),memberReceiverAddressInfo);
    }

    /**
     * ????????????id??????????????????
     *
     * @param memberId
     * @return
     */
    public FCResponse<MemberInfo> getMemberById(Long memberId) {
        if (ObjectUtils.isEmpty(memberId) || memberId <= 0){
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(),HttpMsg.member.MEMBER_ID_ERROR.getMsg());
        }
        Member member = this.memberMapper.getMemberById(memberId);
        if (ObjectUtils.isEmpty(member)){
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(),HttpMsg.member.MEMBER_DATA_EMPTY.getMsg());
        }
        // ????????????id????????????????????????
        MemberDetail memberDetail = this.memberDetailMapper.getMemberDetailByMemberId(memberId);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(memberId);
        memberInfo.setMemberName(member.getName());
        memberInfo.setMemberPhone(member.getPhone());
        memberInfo.setMemberAvatar(memberDetail.getAvatar());
        memberInfo.setMemberSex(memberDetail.getSex());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(),HttpMsg.member.MEMBER_DATA_SUCCESS.getMsg(),memberInfo);
    }
}
