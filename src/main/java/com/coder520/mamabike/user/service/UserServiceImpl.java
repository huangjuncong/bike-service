package com.coder520.mamabike.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coder520.mamabike.cache.CommonCacheUtil;
import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.common.utils.QiniuFileUploadUtil;
import com.coder520.mamabike.common.utils.RandomNumberCode;
import com.coder520.mamabike.jms.SmsProcessor;
import com.coder520.mamabike.security.AESUtil;
import com.coder520.mamabike.security.Base64Util;
import com.coder520.mamabike.security.MD5Util;
import com.coder520.mamabike.security.RSAUtil;
import com.coder520.mamabike.user.dao.UserMapper;
import com.coder520.mamabike.user.entity.User;
import com.coder520.mamabike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄俊聪 on 2017/12/14.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String VERIFYCODE_PREFIX = "verify.code.";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonCacheUtil cacheUtil;

    @Autowired
    private SmsProcessor smsProcessor;

    private static final String SMS_QUEUE = "sms.queue";

    @Override
    public String login(String data, String key) throws MaMaBikeException {

        String token = null;
        String decryptData = null;

        try{
            byte[] aesKey = RSAUtil.decryptByPrivateKey(Base64Util.decode(key));
            decryptData = AESUtil.decrypt(data, new String(aesKey, "UTF-8"));
            if (decryptData == null){
                throw new Exception();
            }
            JSONObject jsonObject = JSON.parseObject(decryptData);
            String mobile = jsonObject.getString("mobile");
            String code = jsonObject.getString("code");
            String platform = jsonObject.getString("platform");//机器类型
            String channelId = jsonObject.getString("channelId");//推送频道编码 单个设备唯一
            if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code) || StringUtils.isBlank(platform)||StringUtils.isBlank(channelId)){
                throw new Exception();
            }
            //去redis取验证码比较手机号码和验证码是否匹配 若匹配 说明是本人手机
            String verCode = cacheUtil.getCacheValue(mobile);
            User user = null;
            if (code.equals(verCode)){
                //检查用户是否存在
                user = userMapper.selectByMobile(mobile);
                if (user == null){
                    user = new User();
                    user.setMobile(mobile);
                    user.setNickname(mobile);
                    userMapper.insertSelective(user);
                }
            }else {
                throw new MaMaBikeException("手机号码与验证码不匹配");
            }
            //生成token
            try {
                token = generateToken(user);
            } catch (Exception e) {
                throw new MaMaBikeException("生成token失败");
            }

            UserElement ue = new UserElement();
            ue.setMobile(mobile);
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);
            ue.setPushChannelId(channelId);
            cacheUtil.putTokenWhenLogin(ue);

        }catch (Exception e){
            log.error("Fail to decrypt data",e);
            throw new MaMaBikeException("数据解析错误");
        }

        return token;
    }

    @Override
    public void modifyNickName(User user) throws MaMaBikeException {
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     *@Author 黄俊聪
     *@Date 2017/12/17 13:56
     *@Description 生成token
     */
    private String generateToken(User user) {

        String source = user.getId() + ":" + user.getMobile() + ":" + System.currentTimeMillis();
        return MD5Util.getMD5(source);
    }

    public void sendVercode(String mobile, String ip) throws MaMaBikeException {

        String verCode = RandomNumberCode.verCode();
        //先存redis  reids缓存检查是否恶意请求 决定是否真的发送验证码
        int result = cacheUtil.cacheForVerificationCode(VERIFYCODE_PREFIX+mobile,verCode,"reg",60,ip);
        if (result == 1) {
            log.info("当前验证码未过期，请稍后重试");
            throw new MaMaBikeException("当前验证码未过期，请稍后重试");
        } else if (result == 2) {
            log.info("超过当日验证码次数上线");
            throw new MaMaBikeException("超过当日验证码次数上限");
        } else if (result == 3) {
            log.info("超过当日验证码次数上限 {}", ip);
            throw new MaMaBikeException(ip + "超过当日验证码次数上限");
        }
        log.info("Sending verify code {} for phone {}", verCode, mobile);
        //验证码推送到队列
        Destination destination = new ActiveMQQueue(SMS_QUEUE);
        Map<String,String> smsParam = new HashMap<>();
        smsParam.put("mobile",mobile);
        smsParam.put("tplId",Constants.MDSMS_VERCODE_TPLID);
        smsParam.put("vercode",verCode);
        String message = JSON.toJSONString(smsParam);
        smsProcessor.sendSmsToQueue(destination,message);
    }

    @Override
    public String uploadHeadImg(MultipartFile file, long userId) throws MaMaBikeException {
        try {
            //获取user 得到原来的头像地址
            User user = userMapper.selectByPrimaryKey(userId);
            // 调用七牛
            String imgUrlName = QiniuFileUploadUtil.uploadHeadImg(file);
            user.setHeadImg(imgUrlName);
            //更新用户头像URL
            userMapper.updateByPrimaryKeySelective(user);
            return Constants.QINIU_HEAD_IMG_BUCKET_URL+"/"+Constants.QINIU_HEAD_IMG_BUCKET_NAME+"/"+imgUrlName;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new MaMaBikeException("头像上传失败");
        }
    }
}
