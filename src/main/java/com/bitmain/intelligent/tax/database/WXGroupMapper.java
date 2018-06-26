package com.bitmain.intelligent.tax.database;

import com.bitmain.intelligent.tax.database.entity.WXGroup;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WXGroupMapper {
    @Select("SELECT * FROM wxgroup WHERE groupid = #{groupID)}")
    WXGroup getOne(String groupID);

    @Insert("INSERT INTO wxgroup(groupID) VALUES(#{groupID})")
    @Options(useGeneratedKeys = true)
    long insert(WXGroup wxGroup);


//    @SelectProvider(type = GroupUsersDaoProvider.class, method = "findUsersByGroupID")
//    List<WXUser> findUsersByGroupID(Map<String,Object> params);


    @SelectProvider(type = GroupUsersDaoProvider.class, method = "findUsersByGroupID")
    List<WXUser> findUsersByGroupID(Map<String,Object> params);


    @Insert("INSERT INTO wxgroup_user(group_id,user_id) select #{arg0},#{arg1} from dual where not exists (select * from wxgroup_user where  group_id=#{arg0} and  user_id=#{arg1})")
    long insertGroupUserIfNotExist(long groupID, long userID);



    class GroupUsersDaoProvider {
        public String findUsersByGroupID(Map<String,Object> params) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT a.,b.*  FROM wxuser a left join taxdata b on (b.userID = a.id) WHERE  a.id IN (select user_id from wxgroup_user where group_id=(select id from wxgroup where groupID=#{groupID}))");
            return stringBuilder.toString();
        }
    }

}
