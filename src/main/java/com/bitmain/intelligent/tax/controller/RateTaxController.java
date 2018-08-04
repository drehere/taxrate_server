package com.bitmain.intelligent.tax.controller;

import com.bitmain.intelligent.tax.Bean.ResponseBean;
import com.bitmain.intelligent.tax.Bean.TaxResult;
import com.bitmain.intelligent.tax.database.entity.WXGroup;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import com.bitmain.intelligent.tax.policy.ResultInterpretationKt;
import com.bitmain.intelligent.tax.service.TaxService;
import com.bitmain.intelligent.tax.service.WXService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * rest api:
 * <p>
 * /calculate 计算税率
 */


@RestController
public class RateTaxController {

    private static Logger logger = LoggerFactory.getLogger(RateTaxController.class);

    @Autowired
    private WXService wxService;


    @Autowired
    private TaxService taxService;

    @PostMapping(path = "/calculate")
    public ResponseBean calculate(@RequestParam("grossPay") int grossPay,
                                  @RequestParam("fee") int fee,
                                  @RequestParam("threshold") int threshold,
                                  @RequestHeader("openID") String openID) {
        WXUser wxUser=wxService.getWxuser(openID);
        if(wxUser==null){
            return ResponseBean.newError(9999,"非法用户");
        }
        TaxResult taxResult=taxService.deductionTax(grossPay,fee,threshold);
        taxService.storeCalculateData(wxUser,taxResult);
        return ResponseBean.newSuccess(taxResult);
    }

    @PostMapping(path = "/login")
    public ResponseBean login(@RequestParam("code") String code) {
        WXUser wxUser = wxService.loginWXFromXCX(code);
        if (wxUser == null) {
            return ResponseBean.newError(9999, "登陆失败");
        }
        return ResponseBean.newSuccess(wxUser);
    }

    @PostMapping(path = "/registerUserInfo")
    public ResponseBean registerUserInfo(@RequestParam("encryptedData") String encryptedData,
                                         @RequestParam("iv") String iv, @RequestHeader("openID") String openID) {

        WXUser wxUser = wxService.registerWXUserInfo(openID,encryptedData,iv);
        return ResponseBean.newSuccess(wxUser);
    }
    @PostMapping(path = "/uploadShareInfo")
    public ResponseBean uploadShareInfo(@RequestParam("encryptedData") String encryptedData,
                                        @RequestParam("iv") String iv, @RequestHeader("openID") String openID) {

        WXGroup wxGroup=wxService.analyseGroupInfo(openID,encryptedData,iv);
        if(wxGroup!=null) {
            return ResponseBean.newSuccess(wxGroup);
        }else{
            return ResponseBean.newError(9999,"分析群组数据失败");
        }
    }

    @PostMapping(path="/getRankList")
    public ResponseBean loadGroupUsers(@RequestParam("groupID") String groupID,@RequestHeader("openID") String openID){
        List<WXUser> users=wxService.loadUsersByGroupID(groupID,openID);
        if(users!=null){
            return ResponseBean.newSuccess(users);
        }else{
            return ResponseBean.newError(9999,"获取排行榜数据失败");
        }
    }
}
