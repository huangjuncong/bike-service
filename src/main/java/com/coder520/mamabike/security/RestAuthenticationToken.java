package com.coder520.mamabike.security;

import com.coder520.mamabike.user.entity.UserElement;
import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by 黄俊聪 on 2018/6/6.
 */
@Data
public class RestAuthenticationToken extends AbstractAuthenticationToken {

    private UserElement user;

    public RestAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
