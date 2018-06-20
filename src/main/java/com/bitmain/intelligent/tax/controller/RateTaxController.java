package com.bitmain.intelligent.tax.controller;

import com.bitmain.intelligent.tax.Bean.ResponseBean;
import com.bitmain.intelligent.tax.Bean.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *  rest api:
 *
 *  /calculate 计算税率
 *
 *
 */


@RestController
public class RateTaxController {

    @PostMapping(path = "/calculate")
    public ResponseBean calculate(@RequestParam("pretax") int preTax ) {

        return ResponseBean.newSuccess(new Result());
    }
}
