package com.bitmain.intelligent.tax.service;

import com.bitmain.intelligent.tax.database.WXGroupMapper;
import com.bitmain.intelligent.tax.database.entity.WXGroup;
import com.bitmain.intelligent.tax.wx.GroupData;
import com.bitmain.intelligent.tax.wx.RegisterBody;
import com.bitmain.intelligent.tax.database.UserMapper;
import com.bitmain.intelligent.tax.database.entity.WXUser;
import com.bitmain.intelligent.tax.http.response.WxLoginResponse;
import com.bitmain.intelligent.tax.util.TextUtil;
import com.bitmain.intelligent.tax.util.WXCore;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class WXService {


    private static Logger logger = LoggerFactory.getLogger(WXService.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private Gson gson;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WXGroupMapper wxGroupMapper;


    @Value("${project.appid}")
    private String appID;
    @Value("${project.secret}")
    private String secret;

    private static final String wxXCXTokenUrl="https://api.weixin.qq.com/sns/jscode2session?";

    @Transactional
    public WXUser loginWXFromXCX(String code){
        WXUser wxUser = null;
        try{
            Response response=okHttpClient.newCall(get(getOpenIDurl(code))).execute();
            String result=response.body().string();
            WxLoginResponse wxLoginResponse=gson.fromJson(result,WxLoginResponse.class);
            if(wxLoginResponse!=null&&TextUtil.isNotEmpty(wxLoginResponse.getOpenid())){
                wxUser=userMapper.getOne(wxLoginResponse.getOpenid());
                if(wxUser==null) {
                    wxUser = new WXUser();
                    wxUser.setOpenID(wxLoginResponse.getOpenid());
                    wxUser.setSessionKey(wxLoginResponse.getSessionKey());
                    userMapper.insert(wxUser);
                }else{
                    wxUser.setSessionKey(wxLoginResponse.getSessionKey());
                    userMapper.updateSessionKey(wxUser);
                }
            }
        }catch(Exception e){
            logger.error("loginWXFromXCX error ",e);
        }
        return wxUser;
    }

    @Transactional
    public WXUser registerWXUserInfo(String openID,String encryptedData,String iv){
        WXUser wxUser=userMapper.getOne(openID);
        if(wxUser==null||TextUtil.isEmpty(wxUser.getSessionKey())){
            return null;
        }
        String result=WXCore.decrypt(appID,encryptedData,wxUser.getSessionKey(),iv);
        if(TextUtil.isEmpty(result)){
            return null;
        }
        RegisterBody registerBody=gson.fromJson(result,RegisterBody.class);
        wxUser.setCity(registerBody.getCity());
        wxUser.setAvatarUrl(registerBody.getAvatarUrl());
        wxUser.setGender(registerBody.getGender());
        wxUser.setNickName(registerBody.getNickName());
        wxUser.setUnionID(registerBody.getUnionId());
        userMapper.updateUserInfo(wxUser);
        return wxUser;
    }

    @Transactional
    public WXUser getWxuser(String openID){
        return userMapper.getOne(openID);
    }

    @Transactional
    public WXGroup analyseGroupInfo(String openID, String encryptedData, String iv){
        WXUser wxUser=userMapper.getOne(openID);
        if(wxUser==null||TextUtil.isEmpty(wxUser.getSessionKey())){
            logger.info("analyseGroupInfo error no users with openid "+openID);
            return null;
        }
        String result=WXCore.decrypt(appID,encryptedData,wxUser.getSessionKey(),iv);
        if(TextUtil.isEmpty(result)){
            logger.info("analyseGroupInfo error data fail");
            return null;
        }
        logger.info("[WXService analyseGroupInfo]"+result);
        GroupData groupData=gson.fromJson(result,GroupData.class);
        if(groupData==null){
            return null;
        }
        WXGroup wxGroup=wxGroupMapper.getOne(groupData.getOpenGId());
        if(wxGroup==null){
            wxGroup=new WXGroup();
            wxGroup.setGroupID(groupData.getOpenGId());
            wxGroupMapper.insert(wxGroup);
            logger.info("[WXService analyseGroupInfo ] insert group "+wxGroup.getId());
        }
        wxGroupMapper.insertGroupUserIfNotExist(wxGroup.getId(),wxUser.getId());
        return wxGroup;
    }


    public List<WXUser> loadUsersByGroupID(String groupID,String openID){
        WXGroup wxGroup = wxGroupMapper.getOne(groupID);
        if(wxGroup==null){
            logger.info("[WXService loadUsersByGroupID ] groupID  "+groupID +" 不存在");
            return null;
        }
        List<WXUser> wxUsers= wxGroupMapper.findUsersByGroupID(Collections.singletonMap("groupID",groupID));
        return wxUsers;
    }


//    @Transactional
//    @Deprecated
//    public WXUser loginWX(String code) {
//        WXUser wxUser = null;
//        try {
//            Request.Builder builder = new Request.Builder();
//            Request request = builder.get().url(getAccessTokenUrl(code))
//                    .build();
//            Response response = okHttpClient.newCall(request).execute();
//            String result = response.body().string();
//            logger.info("login : " + response);
//            WxLoginResponse wxLoginResponse = gson.fromJson(result, WxLoginResponse.class);
//            if(TextUtil.isEmpty(wxLoginResponse.getOpenid())){
//                return null;
//            }
//            wxUser = userMapper.getOne(wxLoginResponse.getOpenid());
//            if (wxUser == null) {
//                response = okHttpClient.newCall(get(getWXUserInfo(wxLoginResponse.getAccessToken(), wxLoginResponse.getOpenid())))
//                        .execute();
//                logger.info("userinfo : " + response);
//                WxUserResponse wxUserResponse=gson.fromJson(response.body().string(),WxUserResponse.class);
//                wxUser=new WXUser();
//                wxUser.setAccesstoken(wxLoginResponse.getAccessToken());
//                wxUser.setRefreshtoken(wxLoginResponse.getRefreshToken());
//                wxUser.setCity(wxUserResponse.getCity());
//                wxUser.setHeadimgurl(wxUserResponse.getHeadimgurl());
//                wxUser.setSex(wxUserResponse.getSex());
//                wxUser.setUnionid(wxUserResponse.getUnionid());
//                userMapper.insert(wxUser);
//            }else{
//                wxUser.setAccesstoken(wxLoginResponse.getAccessToken());
//                wxUser.setRefreshtoken(wxLoginResponse.getRefreshToken());
//                userMapper.update(wxUser);
//            }
//
//        } catch (Exception e) {
//            return null;
//        }
//        return wxUser;
//    }



    private String getOpenIDurl(String code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(wxXCXTokenUrl)
                .append("appid=").append(appID).append("&secret=")
                .append(secret).append("&js_code=").append(code)
                .append("&grant_type=authorization_code");
        return stringBuilder.toString();
    }

//    private String getAccessTokenUrl(String code) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(accessToken)
//                .append("?appid=").append(appID).append("&secret=")
//                .append(secret).append("&code=").append(code)
//                .append("&grant_type=authorization_code");
//        return stringBuilder.toString();
//    }
//
//    private String getWXUserInfo(String accessToken, String openid) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(userinfo)
//                .append("?accessToken=").append(accessToken).append("&openid=")
//                .append(openid);
//        return stringBuilder.toString();
//    }

    private static Request get(String url) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(url);
        return builder.build();
    }

}
