package com.senvu.train.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.senvu.train.member.pojo.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}