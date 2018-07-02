package com.bitmain.intelligent.tax.database;

import com.bitmain.intelligent.tax.database.entity.TaxData;
import com.bitmain.intelligent.tax.database.entity.WXGroup;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WXGroupMapper {
    @Select("SELECT * FROM wxgroup WHERE groupid = #{groupID}")
    WXGroup getOne(@Param(value="groupID") String groupID);

    @Insert("INSERT INTO wxgroup(groupID) VALUES(#{groupID})")
    @Options(useGeneratedKeys = true)
    long insert(WXGroup wxGroup);





    @SelectProvider(type = GroupUsersDaoProvider.class, method = "findUsersByGroupID")
    @Results(value={
            @Result(property ="nickName",column = "nickName"),
            @Result(property ="id",column = "id",id = true),
            @Result(property ="gender",column = "gender"),
            @Result(property ="avatarUrl",column = "avatarUrl"),
            @Result(property ="city",column = "city"),
            @Result(property ="nickName",column = "nickName"),
            @Result(one=@One(select = "com.bitmain.intelligent.tax.database.TaxMapper.findTaxDataIfExist"),property = "taxData",column = "id",javaType = TaxData.class)
    })
    List<WXUser> findUsersByGroupID(Map<String, Object> params);


    @Insert("INSERT INTO wxgroup_user(group_id,user_id) select #{groupID},#{userID} from dual where not exists (select * from wxgroup_user where  group_id=#{groupID} and  user_id=#{userID})")
    long insertGroupUserIfNotExist(@Param(value="groupID") long groupID,@Param(value="userID") long userID);

//    做外连接查询使用
//    @SelectProvider(type = GroupUsersDaoProvider.class, method = "findUsersByGroupID")
//    List<WXUser> findUsersByGroupID(Map<String,Object> params);


    class GroupUsersDaoProvider {
        /**
         * 直接左外连接全部数据全都查出来。
         * @param params
         * @return
         */
//        public String findUsersByGroupID(Map<String, Object> params) {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("SELECT a.*,b.*  FROM wxuser a left join taxdata b on (b.userID = a.id) WHERE  a.id IN (select user_id from wxgroup_user where group_id=(select id from wxgroup where groupID=#{groupID}))");
//            return stringBuilder.toString();
//        }

        /**
         * 通过mybaits的关联查询
         * @param params
         * @return
         */
        public String findUsersByGroupID(Map<String, Object> params) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT a.nickName as nickName,a.gender as gender,a.avatarUrl as avatarUrl, " +
                    "a.city as city,a.id as id  FROM wxuser a WHERE  a.id IN (select user_id from wxgroup_user where group_id=(select id from wxgroup where groupID=#{groupID}))");
            return stringBuilder.toString();
        }
    }

}
