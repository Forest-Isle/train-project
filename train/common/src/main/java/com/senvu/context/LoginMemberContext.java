package com.senvu.context;

import com.senvu.common.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginMemberContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    private static ThreadLocal<LoginVo> member = new ThreadLocal<>();

    public static LoginVo getMember() {
        return member.get();
    }

    public static void setMember(LoginVo member) {
        LoginMemberContext.member.set(member);
    }

    public static Long getMemberId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }

}
