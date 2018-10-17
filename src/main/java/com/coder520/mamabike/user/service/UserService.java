package com.coder520.mamabike.user.service;

import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 黄俊聪 on 2017/12/14.
 */
public interface UserService {

    String login(String data, String key) throws MaMaBikeException;

    void modifyNickName(User user) throws MaMaBikeException;

    void sendVercode(String mobile,String ip)throws MaMaBikeException;

    String uploadHeadImg(MultipartFile file, long userId) throws MaMaBikeException;
}
