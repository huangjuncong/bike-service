package com.coder520.mamabike.user.controller;

import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.common.resp.ApiResult;
import com.coder520.mamabike.common.rest.BaseController;
import com.coder520.mamabike.user.entity.LoginInfo;
import com.coder520.mamabike.user.entity.User;
import com.coder520.mamabike.user.entity.UserElement;
import com.coder520.mamabike.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 黄俊聪 on 2017/12/13.
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    /**
     *@Author 黄俊聪
     *@Date 2017/12/26 21:21
     *@Description 用户登陆注册
     */
    @ApiOperation(value="用户登录",notes = "用户登录",httpMethod = "POST")
    @ApiImplicitParam(name = "loginInfo",value = "加密数据",required = true,dataType = "LoginInfo")
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@RequestBody LoginInfo loginInfo){
        ApiResult<String> resp = new ApiResult<>();

        try{
            String data = loginInfo.getData();
            String key = loginInfo.getKey();
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)){
                throw new MaMaBikeException("参数校验失败");
            }
            String token = userService.login(data,key);
            resp.setData(token);
        }catch (MaMaBikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e){
            log.error("Fail to login ",e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }


    /**
     *@Author 黄俊聪
     *@Date 2017/12/17 20:01
     *@Description 修改用户昵称
     */
    @ApiOperation(value="修改昵称",notes = "用户修改昵称",httpMethod = "POST")
    @ApiImplicitParam(name = "user",value = "用户信息 包含昵称",required = true,dataType = "User")
    @RequestMapping("/modifyNickName")
    public ApiResult modifyNickName(@RequestBody User user){
        ApiResult<String> resp = new ApiResult<>();
        try{
            UserElement ue = getCurrentUser();
            user.setId(ue.getUserId());
            userService.modifyNickName(user);
        }catch (MaMaBikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }catch (Exception e) {
            log.error("Fail to update user info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }
    
    /**
     *@Author 黄俊聪
     *@Date 2018/6/7 21:24
     *@Description 发送验证码
     */

    @ApiOperation(value="短信验证码",notes = "根据用户手机号码发送验证码",httpMethod = "POST")
    @ApiImplicitParam(name = "user",value = "用户信息 包含手机号码",required = true,dataType = "User")
    @RequestMapping("/sendVercode")
    public ApiResult sendVercode(@RequestBody User user, HttpServletRequest request){
        ApiResult<String> resp = new ApiResult<>();
        try {
            if(StringUtils.isEmpty(user.getMobile())){
                throw new MaMaBikeException("手机号码不能为空");
            }
            userService.sendVercode(user.getMobile(),getIpFromRequest(request));
            resp.setMessage("发送成功");
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to update user info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }

    /**
     *@Author 黄俊聪
     *@Date 2018/6/10 20:52
     *@Description 修改头像
     */
    @ApiOperation(value="上传头像",notes = "用户上传头像 file" ,httpMethod = "POST")
    @RequestMapping(value = "/uploadHeadImg",method = RequestMethod.POST)
    public ApiResult<String> uploadHeadImg(HttpServletRequest req, @RequestParam(required=false ) MultipartFile file) {

        ApiResult<String> resp = new ApiResult<>();
        try {
            UserElement ue = getCurrentUser();
            String uploadHeadImgUrl = userService.uploadHeadImg(file, ue.getUserId());
            resp.setMessage("上传成功");
            resp.setData(uploadHeadImgUrl);
        } catch (MaMaBikeException e) {
            resp.setCode(e.getStatusCode());
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to update user info", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }

}
