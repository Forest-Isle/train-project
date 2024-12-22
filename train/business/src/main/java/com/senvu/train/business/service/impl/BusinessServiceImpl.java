package com.senvu.train.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainStationMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainTicketMapper;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainTicket;
import com.senvu.train.business.pojo.dto.query.QueryDto;
import com.senvu.train.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Autowired
    private TrainStationMapper trainStationMapper;
//    @Autowired
//    private RedisTemplate<String,String> redisTemplate;
//    @Autowired
//    private DefaultKaptcha defaultKaptcha;

    @Override
    public PageResult query(QueryDto queryDto) {
        Integer page = queryDto.getPage();
        Integer size = queryDto.getSize();
        System.out.println("start:" + queryDto.getStart());
        System.out.println("end:" + queryDto.getEnd());
        Page<DailyTrainTicket> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrainTicket> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DailyTrainTicket::getTrainCode);
        // 1234 123 124 134 234 12 13 14 23 24 34 1 2 3 4
        if (queryDto.getDate() != null && queryDto.getStart() != null && queryDto.getEnd() != null) {
            wrapper.eq(DailyTrainTicket::getDate,queryDto.getDate()).eq(DailyTrainTicket::getStart,queryDto.getStart()).eq(DailyTrainTicket::getEnd,queryDto.getEnd());
        }
        dailyTrainTicketMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Result all() {
        List<TrainStation> trainStations = trainStationMapper.selectList(null);
        Result result = new Result();
        result.setData(trainStations);
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public String imageUrl(String imageCodeToken) {
//        String text = defaultKaptcha.createText();
//        redisTemplate.opsForValue().set("kaptcha:" + imageCodeToken,text,5, TimeUnit.MINUTES);
//        return text;
        return "'https://wuqisen.oss-cn-hangzhou.aliyuncs.com/alipay.png'";
    }
}
