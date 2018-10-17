package com.coder520.mamabike.common.utils;

import com.coder520.mamabike.common.constants.Constants;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by 黄俊聪 on 2018/6/10.
 */
public class QiniuFileUploadUtil {

    public static String uploadHeadImg(MultipartFile file) throws IOException {

        //Zone.zone2()是七牛的华南地区，//构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...根据配置类创建上传管理类
        UploadManager uploadManager = new UploadManager(cfg);
        //...创建上传凭证，然后准备上传
        Auth auth = Auth.create(Constants.QINIU_ACCESS_KEY, Constants.QINIU_SECRET_KEY);
        //获取上传的token
        String upToken = auth.uploadToken(Constants.QINIU_HEAD_IMG_BUCKET_NAME);
        //得到上传后的返回结果
        Response response = uploadManager.put(file.getBytes(),null, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }

}
