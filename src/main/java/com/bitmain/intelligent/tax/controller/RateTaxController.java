package com.bitmain.intelligent.tax.controller;

import com.bitmain.intelligent.tax.Bean.ResponseBean;
import com.bitmain.intelligent.tax.Bean.TaxResult;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import com.bitmain.intelligent.tax.service.TaxService;
import com.bitmain.intelligent.tax.service.WXService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        int result=taxService.deductionTax(grossPay-fee,threshold);
        TaxResult taxResult=new TaxResult();
        taxResult.setResult(result);
        taxResult.setFee(fee);
        taxResult.setGrossPay(grossPay);
        taxResult.setThreshold(threshold);
        taxResult.setRealSalary(grossPay-fee-result);
        taxResult.setResultDesc("你总共减少了10顿肯德基");
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
}
