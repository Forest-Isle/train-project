package com.senvu.train.member.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.context.LoginMemberContext;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.PassengerQueryDTO;
import com.senvu.train.member.pojo.dto.PassengerSaveDTO;
import com.senvu.train.member.pojo.entity.Passenger;
import com.senvu.train.member.mapper.PassengerMapper;
import com.senvu.train.member.service.PassengerService;
import com.senvu.util.SnowUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    @Override
    public Result save(PassengerSaveDTO passengerSaveDTO) {
        Date date = new Date();
        Passenger passenger1 = passengerMapper.selectById(passengerSaveDTO.getId());
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(passengerSaveDTO,passenger);
        Result result = new Result<>();
        result.setCode(Result.SUCCESS_CODE);
        if (passenger.getId() == null){
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setMemberId(LoginMemberContext.getMemberId());
            int insert = passengerMapper.insert(passenger);
            result.setData(passenger);
            result.setMessage("乘客添加成功");
        }else{
            passengerMapper.updateById(passenger);
            result.setMessage("乘客更新成功");
        }

        return result;
    }

    @Override
    public PageResult list(PassengerQueryDTO passengerQueryDTO) {
        LambdaQueryWrapper<Passenger> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(passengerQueryDTO.getMemberId())){
            wrapper.eq(Passenger::getMemberId,passengerQueryDTO.getMemberId());
        }
        Page<Passenger> page = new Page<>(passengerQueryDTO.getPage(),passengerQueryDTO.getSize());
        passengerMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setList(page.getRecords());
        return pageResult;
    }

    @Override
    public Result delete(Long id) {
        passengerMapper.deleteById(id);
        Result res = new Result();
        res.setCode(Result.SUCCESS_CODE);
        res.setMessage("删除成功");
        return res;
    }

    @Override
    public Result queryMine() {
        LambdaQueryWrapper<Passenger> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Passenger::getMemberId,LoginMemberContext.getMemberId());
        List<Passenger> passengers = passengerMapper.selectList(wrapper);
        Result result = new Result();
        result.setCode(Result.SUCCESS_CODE);
        result.setData(passengers);
        return result;
    }
}
