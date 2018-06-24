package com.bitmain.intelligent.tax.service;

import com.bitmain.intelligent.tax.database.TaxMapper;
import com.bitmain.intelligent.tax.database.UserMapper;
import com.bitmain.intelligent.tax.policy.Policy;
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

    public int deductionTax(int salary,int threshold){
        return policy.deductionTax(SpeedCalculateChain.standarChain(),salary,threshold);
    }
}
