package com.senvu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.LoginDTO;
import com.senvu.train.member.pojo.dto.RegisterDTO;
import com.senvu.train.member.pojo.dto.SendCodeDTO;
import com.senvu.train.member.pojo.entity.Member;
import com.senvu.train.member.pojo.vo.LoginVo;
import com.senvu.train.member.mapper.MemberMapper;
import com.senvu.train.member.service.MemberService;
import com.senvu.util.JwtUtil;
import com.senvu.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    Logger LOG = LoggerFactory.getLogger(MemberServiceImpl.class);

//    @Override
//    public Integer getCount() {
//        return Math.toIntExact(memberMapper.countByExample(null));
////        return 1;
//    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        List<Member> members = members(registerDTO.getMobile());
        Result result = new Result();
        if (CollUtil.isNotEmpty(members)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
//            throw new RuntimeException(String.valueOf(BusinessExceptionEnum.MEMBER_MOBILE_EXIST));
        }
        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(registerDTO.getMobile());
        int insert = memberMapper.insert(member);
        if (insert != 0){
            result.setMessage("注册成功");
            result.setCode(Result.SUCCESS_CODE);
            result.setData(member.getId());
        }else {
            result.setMessage("注册失败");
            result.setCode(Result.FAILD_CODE);
        }
        return result;
    }

    @Override
    public Result sendCode(SendCodeDTO sendCodeDTO) {
        List<Member> members = members(sendCodeDTO.getMobile());
        Member member = new Member();
        if (CollUtil.isNotEmpty(members)){
            LOG.info("手机号存在 直接生成验证码");
            member = members.get(0);
        }else{
            LOG.info("手机号不存在 插入一条记录");
            member.setMobile(sendCodeDTO.getMobile());
            member.setId(SnowUtil.getSnowflakeNextId());
            memberMapper.insert(member);
        }

//        String code = RandomUtil.randomString(4);
        String code = "8888";
        LOG.info("生成验证码:{}",code);
        Result result = new Result(Result.SUCCESS_CODE, "发送验证码成功");
        result.setData(code);
        return result;
    }

    @Override
    public Result login(LoginDTO loginDTO) {
        List<Member> members = members(loginDTO.getMobile());
        Result result = new Result();
        if (CollUtil.isEmpty(members)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }else if (!"8888".equals(loginDTO.getCode())){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_CODE_ERROR);
        }
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(members.get(0),loginVo);
        Map<String, Object> map = BeanUtil.beanToMap(loginVo);
        String token = JwtUtil.createToken(loginVo.getId(), loginVo.getMobile());
        loginVo.setToken(token);
        result.setData(loginVo);
        result.setCode(Result.SUCCESS_CODE);
        result.setMessage("登录成功");
        return result;
    }

    private List<Member> members(String mobile) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getMobile,mobile);
        List<Member> members = memberMapper.selectList(wrapper);
        return members;
    }
}
