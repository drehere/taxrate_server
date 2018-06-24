package com.bitmain.intelligent.tax.policy;


import org.springframework.stereotype.Component;

@Component
public interface Policy {
    int deductionTax(Chain calculateChain,int input, int threshold);

}
