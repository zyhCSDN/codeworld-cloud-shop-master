package com.codeworld.fc.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.auth.client.*;
import com.codeworld.fc.auth.domain.*;
import com.codeworld.fc.auth.properties.JwtProperties;
import com.codeworld.fc.auth.request.*;
import com.codeworld.fc.auth.response.SystemLoginInfoResponse;
import com.codeworld.fc.auth.service.AuthService;
import com.codeworld.fc.common.domain.LoginInfoData;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.enums.StatusEnum;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.*;
import com.codeworld.fc.auth.response.MemberInfoResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * ClassName AuthServiceImpl
 * Description
 * Author Lenovo
 * Date 2020/11/26
 * Version 1.0
 **/
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired(required = false)
    private JwtProperties jwtProperties;
    @Resource
    private UserClient userClient;
    @Resource
    private RoleClient roleClient;
    @Resource
    private MenuClient menuClient;
    @Resource
    private MemberClient memberClient;
    @Resource
    private MerchantClient merchantClient;

    @Value("${wx.code2Session}")
    private String wxLoginUrl;
    @Value("${wx.appid}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;

    @Autowired(required = false)
    private RestTemplate restTemplate = new RestTemplate();



    /**
     * ??????????????????
     *
     * @param memberLoginRequest
     * @param request
     * @param response
     * @return
     */
    @Override
    public FCResponse<String> memberLogin(MemberLoginRequest memberLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        // ???????????????????????????????????????
        FCResponse<Integer> fcResponse = this.memberClient.checkMemberByPhone(memberLoginRequest.getPhone());
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        if (fcResponse.getData() == 0) {
            // ?????????
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.member.MEMBER_NOT_REGISTER.getMsg(), null);
        }
        // ?????????????????????????????????
        FCResponse<MemberResponse> memberFcResponse = this.memberClient.getMemberByPhone(memberLoginRequest.getPhone());
        if (!memberFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        // ????????????
        MemberResponse memberResponse = memberFcResponse.getData();
        if (!StringUtils.equals(memberResponse.getPassword(), memberLoginRequest.getPassword())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.member.MEMBER_MESSAGE_ERROR.getMsg(), null);
        }
        // ????????????
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(memberResponse.getId());
        memberInfo.setMemberPhone(memberResponse.getPhone());
        LoginInfoData loginInfoData = new LoginInfoData();
        loginInfoData.setId(memberInfo.getMemberId());
        loginInfoData.setPhone(memberInfo.getMemberPhone());
        // ?????????????????????
        loginInfoData.setResources("member");
        try {
            String token = JwtUtils.generateToken(loginInfoData, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire() * 24);
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge() * 60);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.member.MEMBER_LOGIN_SUCCESS.getMsg(), token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param tokenRequest
     * @return
     */
    @Override
    public FCResponse<MemberInfoResponse> getMemberInfo(TokenRequest tokenRequest) {
        try {
            LoginInfoData loginInfoData = JwtUtils.getInfoFromToken(tokenRequest.getToken(), jwtProperties.getPublicKey());
            if (ObjectUtils.isEmpty(loginInfoData)) {
                return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.member.MEMBER_DATA_EXPIRE.getMsg(), null);
            }
            // ????????????Id??????????????????
            FCResponse<MemberReceiverAddressInfo> memberResponse = this.memberClient.getMemberReceiverAddressByMemberId(loginInfoData.getId());
            if (!memberResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.member.MEMBER_DATA_EXPIRE.getMsg(), null);
            }
            MemberReceiverAddressInfo memberReceiverAddressInfo = memberResponse.getData();
            MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMemberId(loginInfoData.getId());
            memberInfo.setMemberPhone(loginInfoData.getPhone());
            memberInfo.setMemberName(memberReceiverAddressInfo.getName());
            memberInfoResponse.setMemberInfo(memberInfo);
            memberInfoResponse.setReceiverAddresses(memberReceiverAddressInfo.getAddresses());
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.member.MEMBER_DATA_SUCCESS.getMsg(), memberInfoResponse);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new FCException("??????????????????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????
     *
     * @param merchantLoginRequest
     * @return
     */
    @Override
    public FCResponse<String> merchantLogin(MerchantLoginRequest merchantLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        // ????????????????????????????????????
        FCResponse<Integer> fcResponse = this.merchantClient.checkMerchantByPhone(merchantLoginRequest.getPhone());
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        if (fcResponse.getData() == 0) {
            // ?????????
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.member.MEMBER_NOT_REGISTER.getMsg(), null);
        }
        FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantByPhone(merchantLoginRequest.getPhone());
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        // ????????????
        // ????????????
        MerchantResponse merchantResponse = merchantFcResponse.getData();
        if (!StringUtils.equals(merchantResponse.getPassword(), merchantLoginRequest.getPassword())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_MESSAGE_ERROR.getMsg(), null);
        }
        // ????????????
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setId(merchantResponse.getId());
        merchantInfo.setPhone(merchantResponse.getPhone());
        LoginInfoData loginInfoData = new LoginInfoData();
        loginInfoData.setId(merchantInfo.getId());
        loginInfoData.setPhone(merchantResponse.getPhone());
        // ?????????????????????
        loginInfoData.setResources("merchant");
        try {
            String token = JwtUtils.generateToken(loginInfoData, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge() * 60);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_SUCCESS.getMsg(), token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param systemLoginRequest
     * @param request
     * @param response
     * @return
     */
    @Override
    public FCResponse<Map<String, Object>> systemLogin(SystemLoginRequest systemLoginRequest, HttpServletRequest request, HttpServletResponse response) {

        // ???????????????????????????
        FCResponse<User> userFCResponse = this.userClient.getUserByName(systemLoginRequest.getUsername());
        if (!userFCResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), userFCResponse.getMsg(), null);
        }
        User user = userFCResponse.getData();
        if (user.getUserStatus().equals(StatusEnum.USER_DISABLE)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.user.USER_DISABLE.getMsg(), null);
        }
        // ?????????????????????
        String password = CodecUtils.md5Hex(systemLoginRequest.getPassword(), user.getPasswordSalt());
        // ????????????
        if (!StringUtils.equals(user.getPassword(), password)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.user.USER_MESSAGE_ERROR.getMsg(), null);
        }
        // ????????????
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setPhone(user.getUserPhone());
        LoginInfoData loginInfoData = new LoginInfoData();
        loginInfoData.setId(userInfo.getUserId());
        loginInfoData.setPhone(userInfo.getPhone());
        // ?????????????????????
        loginInfoData.setResources("system");
        try {
            String token = JwtUtils.generateToken(loginInfoData, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge() * 60);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_LOGIN_SUCCESS.getMsg(), map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param tokenRequest
     * @param request
     * @param response
     * @return
     */
    @Override
    public FCResponse<SystemLoginInfoResponse> getSystemLoginInfo(TokenRequest tokenRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginInfoData loginInfoData = JwtUtils.getInfoFromToken(tokenRequest.getToken(), this.jwtProperties.getPublicKey());
            if (ObjectUtils.isEmpty(loginInfoData)) {
                throw new FCException("????????????");
            }
            SystemLoginInfoResponse systemLoginInfoResponse = new SystemLoginInfoResponse();
            systemLoginInfoResponse.setId(loginInfoData.getId().toString());
            // ????????????????????????
            String resources = loginInfoData.getResources();
            // ?????????????????????
            if ("system".equals(resources)) {
                // ???????????????????????????
                FCResponse<User> fcResponse = this.userClient.getUserById(loginInfoData.getId());
                if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                    return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg());
                }
                User user = fcResponse.getData();
                systemLoginInfoResponse.setAvatar(user.getAvatar());
            }
            // ????????????
            else if ("merchant".equals(resources)) {
                // ??????????????????
                FCResponse<MerchantResponse> fcResponse = this.merchantClient.getMerchantInfoById(loginInfoData.getId());
                if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                    return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg());
                }
                MerchantResponse merchantResponse = fcResponse.getData();
                systemLoginInfoResponse.setAvatar(merchantResponse.getAvatar());
            }
            Set<String> roles = new HashSet<>();
            Set<MenuVO> menuVOS = new HashSet<>();
            Set<ButtonVO> buttonVOS = new HashSet<>();
            // ????????????id??????????????????
            FCResponse<Role> roleFcResponse = this.roleClient.getRoleByUserId(loginInfoData.getId());
            if (roleFcResponse.getCode().equals(HttpFcStatus.PARAMSERROR.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), roleFcResponse.getMsg());
            }
            roles.add(roleFcResponse.getData().getRoleCode());
            // ????????????id??????????????????
            FCResponse<List<Menu>> menuFcResponse = this.menuClient.getMenuByRoleId(roleFcResponse.getData().getRoleId());
            if (menuFcResponse.getCode().equals(HttpFcStatus.PARAMSERROR.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), menuFcResponse.getMsg());
            }
            if (ObjectUtils.isEmpty(menuFcResponse.getData())) {
                return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), menuFcResponse.getMsg());
            }
            List<Menu> menus = menuFcResponse.getData();
            // ???????????????
            if (!CollectionUtils.isEmpty(menus)) {
                menus.stream().filter(Objects::nonNull).forEach(menu -> {
                    // ????????????
                    if (StringUtils.equalsIgnoreCase("button", menu.getType())) {
                        // ?????????????????????
                        ButtonVO buttonVO = new ButtonVO();
                        BeanUtil.copyProperties(menu, buttonVO);
                        buttonVOS.add(buttonVO);
                    }
                    // ????????????
                    if (StringUtils.equalsIgnoreCase("menu", menu.getType())) {
                        // ?????????????????????
                        MenuVO menuVO = new MenuVO();
                        BeanUtil.copyProperties(menu, menuVO);
                        menuVOS.add(menuVO);
                    }
                });
            }
            systemLoginInfoResponse.getRoles().addAll(roles);
            systemLoginInfoResponse.getButtons().addAll(buttonVOS);
            systemLoginInfoResponse.getMenus().addAll(TreeBuilder.buildTree(menuVOS));
            systemLoginInfoResponse.setLoginIp(IPUtil.getIpAddr(request));
            systemLoginInfoResponse.setLoginLocation(AddressUtil.getCityInfo(IPUtil.getIpAddr(request)));
            systemLoginInfoResponse.setLoginTime(new Date());
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.user.USER_AUTH_SUCCESS.getMsg(), systemLoginInfoResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param tokenRequest
     * @return
     */
    @Override
    public FCResponse<MerchantInfo> getMerchantInfo(TokenRequest tokenRequest) {
        try {
            LoginInfoData loginInfoData = JwtUtils.getInfoFromToken(tokenRequest.getToken(), jwtProperties.getPublicKey());
            if (ObjectUtils.isEmpty(loginInfoData) || !StringUtils.equals("merchant", loginInfoData.getResources())) {
                return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.member.MEMBER_DATA_EXPIRE.getMsg(), null);
            }
            FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantInfoById(loginInfoData.getId());
            if (!merchantFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), merchantFcResponse.getMsg(), null);
            }
            MerchantResponse merchantResponse = merchantFcResponse.getData();
            MerchantInfo merchantInfo = new MerchantInfo();
            merchantInfo.setId(merchantResponse.getId());
            merchantInfo.setPhone(merchantResponse.getPhone());
            merchantInfo.setMerchantName(merchantResponse.getMerchantName());
            merchantInfo.setNumber(merchantResponse.getNumber());
            merchantInfo.setNickName(merchantResponse.getNickName());
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_DATA_SUCCESS.getMsg(), merchantInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }

    }

    /**
     * ????????????????????????
     *
     * @param systemLoginRequest
     * @param request
     * @param response
     * @return
     */
    @Override
    public FCResponse<Map<String, Object>> merchantSystemLogin(SystemLoginRequest systemLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        // ??????????????????
        FCResponse<Integer> fcResponse = this.merchantClient.checkMerchantByMerchantNumber(systemLoginRequest.getUsername());
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        // ????????????????????????
        if (fcResponse.getData() == 0) {
            // ?????????
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.merchant.MERCHANT_NO_REGISTER.getMsg(), null);
        }
        // ?????????????????????????????????
        FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantByMerchantNumber(systemLoginRequest.getUsername());
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), fcResponse.getMsg(), null);
        }
        // ????????????
        // ????????????
        MerchantResponse merchantResponse = merchantFcResponse.getData();
        // ????????????????????????????????????
        String password = CodecUtils.md5Hex(systemLoginRequest.getPassword(), merchantResponse.getPasswordSalt());
        if (!StringUtils.equals(merchantResponse.getPassword(), password)) {
            return FCResponse.dataResponse(HttpFcStatus.AUTHFAILCODE.getCode(), HttpMsg.merchant.MERCHANT_MESSAGE_ERROR.getMsg(), null);
        }
        // ????????????
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setId(merchantResponse.getId());
        merchantInfo.setPhone(merchantResponse.getPhone());
        LoginInfoData loginInfoData = new LoginInfoData();
        loginInfoData.setId(merchantInfo.getId());
        loginInfoData.setPhone(merchantResponse.getPhone());
        // ?????????????????????
        loginInfoData.setResources("merchant");
        try {
            String token = JwtUtils.generateToken(loginInfoData, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge() * 60);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_SUCCESS.getMsg(), map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ??????code??????
     *
     * @param map
     * @param request
     * @param response
     * @return
     */
    @Override
    public FCResponse<String> wxLogin(Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        log.info("??????????????????");
        // ????????????
        String code = map.get("code");
        log.info("?????????code???{}",code);
        // ???????????????opedId
        String url = wxLoginUrl + "?appid=" + appId +"&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        log.info("?????????????????????url???{}",url);
        // ????????????
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(url, String.class);
        String openId = entity.getBody();
        // ??????openid??????????????????
        FCResponse<MerchantResponse> fcResponse = this.merchantClient.getMerchantByOpenId(openId);
        if (!fcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())){
            return FCResponse.dataResponse(fcResponse.getCode(),fcResponse.getMsg());
        }
        MerchantResponse merchantResponse = fcResponse.getData();
        // ????????????
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setId(merchantResponse.getId());
        merchantInfo.setPhone(merchantResponse.getPhone());
        LoginInfoData loginInfoData = new LoginInfoData();
        loginInfoData.setId(merchantInfo.getId());
        loginInfoData.setPhone(merchantResponse.getPhone());
        // ?????????????????????
        loginInfoData.setResources("merchant");
        try {
            String token = JwtUtils.generateToken(loginInfoData, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge() * 60);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.merchant.MERCHANT_LOGIN_SUCCESS.getMsg(), token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }
}
