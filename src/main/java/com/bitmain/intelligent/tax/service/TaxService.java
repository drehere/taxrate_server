package com.bitmain.intelligent.tax.service;

import com.bitmain.intelligent.tax.Bean.TaxResult;
import com.bitmain.intelligent.tax.database.TaxMapper;
import com.bitmain.intelligent.tax.database.UserMapper;
import com.bitmain.intelligent.tax.database.entity.TaxData;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import com.bitmain.intelligent.tax.policy.Policy;
import com.bitmain.intelligent.tax.policy.ResultInterpretationKt;
import com.bitmain.intelligent.tax.policy.SpeedCalculateChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaxMapper taxMapper;

    @Autowired
    private Policy policy;

    public TaxResult deductionTax(int grossPay, int fee, int threshold) {
        TaxResult taxResult = new TaxResult();
        taxResult.setFee(fee);
        taxResult.setGrossPay(grossPay);
        taxResult.setThreshold(threshold);
        final int salary = grossPay - fee;
        if (threshold == 5000) {
            int tax = policy.deductionTax(SpeedCalculateChain.latestChain(), salary, threshold);
            taxResult.setTax(tax);
            int oldTax=policy.deductionTax(SpeedCalculateChain.standardChain(),salary,3500);
            taxResult.setResultDesc(ResultInterpretationKt.interpretationDiff(tax,oldTax));
        } else {

            int tax = policy.deductionTax(SpeedCalculateChain.standardChain(), salary, threshold);
            taxResult.setTax(tax);
            taxResult.setResultDesc(ResultInterpretationKt.interpretation(tax));
        }
        taxResult.setRealSalary(grossPay-fee-taxResult.getTax());
        return taxResult;

    }

    public boolean storeCalculateData(WXUser wxUser, TaxResult taxResult) {
        TaxData taxData = taxMapper.findTaxDataIfExist(wxUser.getId());
        if (taxData != null) {
            taxData.setFee(taxResult.getFee());
            taxData.setGrossPay(taxResult.getGrossPay());
            taxData.setRealSalary(taxResult.getRealSalary());
            taxData.setTax(taxResult.getTax());
            taxData.setResultDesc(taxResult.getResultDesc());
            taxData.setThreshold(taxResult.getThreshold());
            taxMapper.updateTaxData(taxData);
        } else {
            taxData = new TaxData();
            taxData.setFee(taxResult.getFee());
            taxData.setGrossPay(taxResult.getGrossPay());
            taxData.setRealSalary(taxResult.getRealSalary());
            taxData.setTax(taxResult.getTax());
            taxData.setResultDesc(taxResult.getResultDesc());
            taxData.setThreshold(taxResult.getThreshold());
            taxData.setUserID(wxUser.getId());
            taxMapper.addTaxData(taxData);
        }
        return true;
    }
}
