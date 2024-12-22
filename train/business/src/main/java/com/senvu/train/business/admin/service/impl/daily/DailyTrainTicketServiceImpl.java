package com.senvu.train.business.admin.service.impl.daily;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.result.Result;
import com.senvu.result.PageResult;
import com.senvu.train.business.admin.mapper.daily.DailyTrainTicketMapper;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainTicketDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainTicketQueryDTO;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainTicket;
import com.senvu.train.business.admin.service.daily.DailyTrainTicketService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyTrainTicketServiceImpl implements DailyTrainTicketService {

    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Override
    public PageResult list(DailyTrainTicketQueryDTO dailyTrainTicketQueryDTO) {
        Integer page = dailyTrainTicketQueryDTO.getPage();
        Integer size = dailyTrainTicketQueryDTO.getSize();
        Page<DailyTrainTicket> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrainTicket> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DailyTrainTicket::getTrainCode);
        // 1234 123 124 134 234 12 13 14 23 24 34 1 2 3 4
        if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getEnd() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        } else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getEnd() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd());
        } else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        } else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getEnd() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        }else if (dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getEnd() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        } else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getStart() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart());
        }else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getEnd() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd());
        }else if (dailyTrainTicketQueryDTO.getTrainCode() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        }else if (dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getEnd() != null) {
            wrapper.eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd());
        }else if (dailyTrainTicketQueryDTO.getStart() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        }else if (dailyTrainTicketQueryDTO.getEnd() != null && dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd()).eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        } else if (dailyTrainTicketQueryDTO.getTrainCode() != null) {
            wrapper.eq(DailyTrainTicket::getTrainCode,dailyTrainTicketQueryDTO.getTrainCode());
        }else if (dailyTrainTicketQueryDTO.getEnd() != null) {
            wrapper.eq(DailyTrainTicket::getEnd,dailyTrainTicketQueryDTO.getEnd());
        }else if (dailyTrainTicketQueryDTO.getStart() != null) {
            wrapper.eq(DailyTrainTicket::getStart,dailyTrainTicketQueryDTO.getStart());
        } else if (dailyTrainTicketQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainTicket::getDate, dailyTrainTicketQueryDTO.getDate());
        }
        dailyTrainTicketMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(dailyTrainTicketMapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(DailyTrainTicketDTO dailyTrainTicketDTO) {
          DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
          BeanUtils.copyProperties(dailyTrainTicketDTO,dailyTrainTicket);
     //   dailyTrainTicketMapper.insert();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result update(DailyTrainTicketDTO dailyTrainTicketDTO) {
        DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
        BeanUtils.copyProperties(dailyTrainTicketDTO,dailyTrainTicket);
     //   dailyTrainTicketMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        dailyTrainTicketMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }
}
