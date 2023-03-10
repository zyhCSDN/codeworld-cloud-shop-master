package com.codeworld.fc.marketing.carouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.codeworld.fc.common.domain.LoginInfoData;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.common.utils.JsonUtils;
import com.codeworld.fc.marketing.carouse.entity.Carouse;
import com.codeworld.fc.marketing.carouse.mapper.CarouseMapper;
import com.codeworld.fc.marketing.carouse.request.CarouseAddRequest;
import com.codeworld.fc.marketing.carouse.request.CarouseSearchRequest;
import com.codeworld.fc.marketing.carouse.request.ReviewCarouseRequest;
import com.codeworld.fc.marketing.carouse.service.CarouseService;
import com.codeworld.fc.marketing.client.MerchantClient;
import com.codeworld.fc.marketing.domain.MerchantResponse;
import com.codeworld.fc.marketing.interceptor.AuthInterceptor;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

//import com.codeworld.fc.marketing.interceptor.AuthInterceptor;

/**
 * ClassName CarouseServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2021/1/13
 * Version 1.0
 **/
@Service
public class CarouseServiceImpl implements CarouseService {

    @Autowired(required = false)
    private CarouseMapper carouseMapper;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;
    @Autowired(required = false)
    private AmqpTemplate amqpTemplate;

    private static final String CAROUSE_ENABLE = "carouse_enable";

    @Autowired(required = false)
    private MerchantClient merchantClient;

    /**
     * ?????????????????????
     *
     * @param carouseSearchRequest
     * @return
     */
    public FCResponse<DataResponse<List<Carouse>>> getPageCarouse(CarouseSearchRequest carouseSearchRequest) {
        // ??????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginInfo();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            throw new FCException("??????????????????????????????");
        }
        // ??????id???????????????
        FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantNumberAndNameById(loginInfoData.getId());
        if (!merchantFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            throw new FCException(merchantFcResponse.getMsg());
        }
        MerchantResponse merchantResponse = merchantFcResponse.getData();
        carouseSearchRequest.setMerchantNumber(merchantResponse.getNumber());
        PageHelper.startPage(carouseSearchRequest.getPage(), carouseSearchRequest.getLimit());
        List<Carouse> carouses = this.carouseMapper.getPageCarouse(carouseSearchRequest);
        if (CollectionUtils.isEmpty(carouses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.carouse.CAROUSE_DATA_EMPTY.getMsg(), DataResponse.dataResponse(carouses, 0L));
        }
        PageInfo<Carouse> pageInfo = new PageInfo<>(carouses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_DATA_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ???????????????
     *
     * @param carouseAddRequest
     * @return
     */
    @Override
    public FCResponse<Void> addCarouse(CarouseAddRequest carouseAddRequest) {
        // ??????????????????
        LoginInfoData loginInfoData = AuthInterceptor.getLoginInfo();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            throw new FCException("??????????????????????????????");
        }
        // ??????id???????????????
        FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantNumberAndNameById(loginInfoData.getId());
        if (!merchantFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            throw new FCException(merchantFcResponse.getMsg());
        }
        MerchantResponse merchantResponse = merchantFcResponse.getData();
        // ??????????????????????????????????????????
        if (DateUtil.compare(carouseAddRequest.getStartTime(), new Date()) < 0
                || DateUtil.compare(carouseAddRequest.getEndTime(), new Date()) < 0) {
            // ????????????????????????????????????????????????
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.carouse.CAROUSE_DATE_ERROR.getMsg());
        }
        // ?????????????????????????????????
        if (DateUtil.compare(carouseAddRequest.getStartTime(), carouseAddRequest.getEndTime()) > 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.carouse.CAROUSE_END_DATE_ERROR.getMsg());
        }
        Carouse carouse = new Carouse();
        BeanUtil.copyProperties(carouseAddRequest, carouse);
        carouse.setId(IDGeneratorUtil.getNextId());
        carouse.setCreateTime(new Date());
        carouse.setMerchantNumber(merchantResponse.getNumber());
        // ????????????????????????
        carouse.setReviewStatus(0);
        // ???????????????app??????
        carouse.setPosition(carouseAddRequest.getPosition());
        this.carouseMapper.addCarouse(carouse);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_ADD_SUCCESS.getMsg());
    }

    /**
     * ?????????????????????
     *
     * @param carouse
     */
    @Override
    public void updateCarouseStatus(Carouse carouse) {
        this.carouseMapper.updateCarouseStatus(carouse);
        // ??????redis?????????????????????
        this.stringRedisTemplate.delete(CAROUSE_ENABLE);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public FCResponse<List<Carouse>> getCarouseEnable() {
        // ??????redis??????????????????????????????
        if (this.stringRedisTemplate.hasKey(CAROUSE_ENABLE)) {
            String json = this.stringRedisTemplate.opsForValue().get(CAROUSE_ENABLE);
            if (ObjectUtils.isNotEmpty(json)) {
                List<Carouse> carouses = JsonUtils.parseList(json, Carouse.class);
                return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_DATA_SUCCESS.getMsg(), carouses);
            }
        }
        // ???????????????????????????
        List<Carouse> carouses = this.carouseMapper.getCarouseEnable();
        if (CollectionUtils.isEmpty(carouses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.carouse.CAROUSE_DATA_EMPTY.getMsg());
        }
        // ?????????redis???
        String json = JsonUtils.serialize(carouses);
        assert json != null;
        this.stringRedisTemplate.opsForValue().set(CAROUSE_ENABLE, json);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_DATA_SUCCESS.getMsg(), carouses);
    }

    /**
     * ?????????????????????????????????????????????1????????????
     *
     * @param carouseIds
     */
    @Override
    public void deleteCarouseTime(List<Long> carouseIds) {
        this.carouseMapper.deleteCarouseTime(carouseIds);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return
     */
    @Override
    public List<Long> getCarouseEndTimeGtNow() {
        return this.carouseMapper.getCarouseEndTimeGtNow();
    }

    /**
     * ????????????????????????????????????
     *
     * @param carouseSearchRequest
     * @return
     */
    @Override
    public FCResponse<DataResponse<List<Carouse>>> getPageCarouseMarketingSystem(CarouseSearchRequest carouseSearchRequest) {
        PageHelper.startPage(carouseSearchRequest.getPage(), carouseSearchRequest.getLimit());
        List<Carouse> carouses = this.carouseMapper.getPageCarouseMarketingSystem(carouseSearchRequest);
        if (CollectionUtils.isEmpty(carouses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.carouse.CAROUSE_DATA_EMPTY.getMsg(), DataResponse.dataResponse(carouses, 0L));
        }
        PageInfo<Carouse> pageInfo = new PageInfo<>(carouses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_DATA_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ???????????????
     *
     * @param reviewCarouseRequest
     * @return
     */
    @Override
    public FCResponse<Void> reviewCarouse(ReviewCarouseRequest reviewCarouseRequest) {
        // ??????????????????????????????????????????
        if (DateUtil.compare(reviewCarouseRequest.getStartTime(), new Date()) < 0
                || DateUtil.compare(reviewCarouseRequest.getEndTime(), new Date()) < 0) {
            // ????????????????????????????????????????????????
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.carouse.CAROUSE_DATE_ERROR.getMsg());
        }
        // ?????????????????????????????????
        if (DateUtil.compare(reviewCarouseRequest.getStartTime(), reviewCarouseRequest.getEndTime()) > 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.carouse.CAROUSE_END_DATE_ERROR.getMsg());
        }
        Carouse carouse = new Carouse();
        carouse.setId(reviewCarouseRequest.getId());
        carouse.setReviewStatus(reviewCarouseRequest.getReviewStatus());
        carouse.setStartTime(reviewCarouseRequest.getStartTime());
        carouse.setEndTime(reviewCarouseRequest.getEndTime());
        try {
            this.carouseMapper.updateCarouseStatus(carouse);
            // ???????????????????????????
            if (reviewCarouseRequest.getReviewStatus() == 1) {
                // ??????
                // ??????????????????
                //??????carouse?????????
                String json = JsonUtils.serialize(carouse);
                this.amqpTemplate.convertAndSend("carouse.update.status.enable", json);
                this.amqpTemplate.convertAndSend("carouse.update.status.disable", json);
            }
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.carouse.CAROUSE_REVIEW_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ???????????????
     *
     * @param id
     * @return
     */
    @Override
    public FCResponse<Void> deleteCarouse(Long id) {
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(),HttpMsg.carouse.CAROUSE_ID_ERROR.getMsg());
        }
        // ???????????????????????????
        Carouse carouse = this.carouseMapper.getCarouseById(id);
        if (ObjectUtils.isEmpty(carouse)){
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(),HttpMsg.carouse.CAROUSE_DATA_EMPTY.getMsg());
        }
        // ????????????????????????????????????
        if (carouse.getStatus() != 0){
            return FCResponse.dataResponse(HttpFcStatus.RUNTIMECODE.getCode(),HttpMsg.carouse.CAROUSE_STATUS_ERROR.getMsg());
        }
        // ????????????????????????????????????
        carouse.setStatus(-1);
        try {
            this.carouseMapper.updateCarouseStatus(carouse);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(),HttpMsg.carouse.CAROUSE_DELETE_SUCCESS.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }
}
