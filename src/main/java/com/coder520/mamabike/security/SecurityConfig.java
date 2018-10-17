package com.coder520.mamabike.security;

import com.coder520.mamabike.cache.CommonCacheUtil;
import com.coder520.mamabike.common.constants.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created by 黄俊聪 on 2018/6/6.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Parameters parameters;

    @Autowired
    private CommonCacheUtil commonCacheUtil;

    private RestPreAuthenticatedProcessingFilter getRestPreAuthenticatedProcessingFilter() throws Exception {
        RestPreAuthenticatedProcessingFilter filter = new RestPreAuthenticatedProcessingFilter(parameters.getNoneSecurityPath(),commonCacheUtil);
        filter.setAuthenticationManager(this.authenticationManagerBean());
        return filter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new RestAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(parameters.getNoneSecurityPath().toArray(new String[parameters.getNoneSecurityPath().size()])).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and().addFilter(getRestPreAuthenticatedProcessingFilter())
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")//忽略 OPTIONS 方法的请求
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
        //放过swagger
    }

}
