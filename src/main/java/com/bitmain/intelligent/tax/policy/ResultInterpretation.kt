package com.bitmain.intelligent.tax.policy

import com.bitmain.intelligent.tax.Bean.TaxResult

fun interpretation(result: Int): String {

    return when (result) {
        in 0..500 -> "你扣税大概少吃了10顿肯德基"
        in 500..2000 -> "你大概了少买了一套only套装"
        in 2000..5000 -> "不得了，你大概少了个ipad"
        in 5000..10000 -> "一个iphonx飞走了"
        in 10000..20000 -> "单反不见了吧？"
        in 20000..50000 -> "你少了一次欧洲5日游"
        else -> "你扣税太多了，我都算不过来了～"
    }


}