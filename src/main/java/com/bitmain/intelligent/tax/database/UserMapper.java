package com.bitmain.intelligent.tax.database;

import com.bitmain.intelligent.tax.database.entity.WXUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Component
public interface UserMapper {

    @Select("SELECT * FROM wxuser WHERE openid = #{openid)}")
    WXUser getOne(String openid);

    @Insert("INSERT INTO wxuser(openID,sessionKey,unionID,phone,nickName,city,gender,avatarUrl) " +
            "VALUES(#{openID},#{sessionKey},#{unionID}, #{phone}, #{nickName},#{city},#{gender},#{avatarUrl})")
    long insert(WXUser user);

    @Update("UPDATE wxuser SET sessionKey=#{sessionKey} WHERE id =#{id} ")
    void updateSeesionKey(WXUser user);

    @Update("UPDATE wxuser SET nickName=#{nickName},city=#{city},gender=#{gender},avatarUrl=#{avatarUrl},unionID=#{unionID} " +
            "where id=#{id}")
    void updateUserInfo(WXUser wxUser);

}