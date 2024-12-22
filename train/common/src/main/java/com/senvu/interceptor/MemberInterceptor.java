package com.senvu.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.senvu.common.LoginVo;
import com.senvu.context.LoginMemberContext;
import com.senvu.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    Logger log = LoggerFactory.getLogger(MemberInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("MemberInterceptor开始");
        //获取header的token参数
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)){
            log.info("获取会员登录token：{}",token);
            JSONObject loginMember = JwtUtil.getJSONObject(token);
            log.info("当前登录会员：{}", loginMember);
            LoginVo member = JSONUtil.toBean(loginMember, LoginVo.class);
            LoginMemberContext.setMember(member);
            LoginVo member1 = LoginMemberContext.getMember();
            System.out.println(member1);
        }
        log.info("MemberInterceptor结束");
        return true;
    }
}
