package com.bitmain.intelligent.tax.policy;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * 按照现行税率计算
 *
 */

@Component
public class DefaultPolicyImpl implements Policy {



    @PostConstruct
    public void init(){

    }

    @Override
    public int deductionTax(Chain calculateChain,int salary, int threshold) {
        return calculateChain.stageProcess(salary-threshold);
    }


}
