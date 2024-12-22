package com.senvu.train.business.admin.service.impl.daily;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.Enum.SeatTypeEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.TrainStationMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainSeatMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainStationMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainTicketMapper;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainStationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrain;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainSeat;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainStation;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainTicket;
import com.senvu.train.business.admin.service.daily.DailyTrainStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class DailyTrainStationServiceImpl implements DailyTrainStationService {

    @Autowired
    private DailyTrainStationMapper dailyTrainStationMapper;
    @Autowired
    private DailyTrainMapper dailyTrainMapper;
    @Autowired
    private TrainStationMapper trainStationMapper;
    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Autowired
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Override
    public PageResult list(DailyTrainStationQueryDTO dailyTrainStationQueryDTO) {
        Integer page = dailyTrainStationQueryDTO.getPage();
        Integer size = dailyTrainStationQueryDTO.getSize();
        Page<DailyTrainStation> pages = new Page<>(page,size);
        LambdaQueryWrapper<DailyTrainStation> wrapper = new LambdaQueryWrapper<>();
        deal(dailyTrainStationQueryDTO,wrapper);
        dailyTrainStationMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    private static void deal(DailyTrainStationQueryDTO dailyTrainStationQueryDTO, LambdaQueryWrapper<DailyTrainStation> wrapper) {
        wrapper.orderByDesc(DailyTrainStation::getDate).orderByAsc(DailyTrainStation::getTrainCode);
        if (dailyTrainStationQueryDTO.getDate() != null && dailyTrainStationQueryDTO.getTrainCode() != null){
            wrapper.eq(DailyTrainStation::getDate, dailyTrainStationQueryDTO.getDate()).eq(DailyTrainStation::getTrainCode, dailyTrainStationQueryDTO.getTrainCode());
        } else if (dailyTrainStationQueryDTO.getDate() != null) {
            wrapper.eq(DailyTrainStation::getDate, dailyTrainStationQueryDTO.getDate());
        } else if (dailyTrainStationQueryDTO.getTrainCode() != null) {
            wrapper.eq(DailyTrainStation::getTrainCode, dailyTrainStationQueryDTO.getTrainCode());
        }
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(dailyTrainStationMapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(DailyTrainStationDTO dailyTrainStationDTO) {
          DailyTrainStation dailyTrainStation = new DailyTrainStation();
          BeanUtils.copyProperties(dailyTrainStationDTO,dailyTrainStation);
          if (dailyTrainStationDTO.getId() != null) {
              dailyTrainStationMapper.updateById(dailyTrainStation);
          } else {
              if (existed(dailyTrainStation)) {
                  throw new BusinessException(BusinessExceptionEnum.DAILY_TRAIN_STATION_EXITED_ERROR);
              }
              dailyTrainStationMapper.insert(dailyTrainStation);
          }
        return new Result(Result.SUCCESS_CODE);
    }

    private boolean existed(DailyTrainStation dailyTrainStation) {
        LambdaQueryWrapper<DailyTrainStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrainStation::getTrainCode, dailyTrainStation.getTrainCode()).eq(DailyTrainStation::getIndex, dailyTrainStation.getIndex()).eq(DailyTrainStation::getDate,dailyTrainStation.getDate());
        LambdaQueryWrapper<DailyTrainStation> wrapper1 = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrainStation::getTrainCode, dailyTrainStation.getTrainCode()).eq(DailyTrainStation::getName, dailyTrainStation.getName()).eq(DailyTrainStation::getDate,dailyTrainStation.getDate());
        return !dailyTrainStationMapper.selectList(wrapper).isEmpty() || !dailyTrainStationMapper.selectList(wrapper1).isEmpty();
    }
    
    @Override
    public Result update(DailyTrainStationDTO dailyTrainStationDTO) {
        DailyTrainStation dailyTrainStation = new DailyTrainStation();
        BeanUtils.copyProperties(dailyTrainStationDTO,dailyTrainStation);
     //   dailyTrainStationMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        dailyTrainStationMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result gen(LocalDate date) {
        // 1. 在每日车次中查询出所有的车次
        LambdaQueryWrapper<DailyTrain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrain::getDate,date);
        List<DailyTrain> dailyTrains = dailyTrainMapper.selectList(wrapper);

        // 2. 对于每个车次在对应的车站表查出经过的所有车站
        for (DailyTrain dailyTrain : dailyTrains) {
            // 先删除原有的车站信息
            LambdaQueryWrapper<DailyTrainStation> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(DailyTrainStation::getTrainCode, dailyTrain.getCode());
            dailyTrainStationMapper.delete(wrapper2);

            LambdaQueryWrapper<TrainStation> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(TrainStation::getTrainCode, dailyTrain.getCode());
            // 获得对应车次的所有车站
            List<TrainStation> trainStations = trainStationMapper.selectList(wrapper1);
            // 3. 生成每日车站数据
            for (TrainStation trainStation : trainStations) {
                DailyTrainStation dailyTrainStation = new DailyTrainStation();
                BeanUtils.copyProperties(trainStation, dailyTrainStation);
                dailyTrainStation.setDate(date);
                dailyTrainStationMapper.insert(dailyTrainStation);
            }

            // 4. 生成对应的车票信息
            for (int i = 0; i < trainStations.size(); i++) {
                TrainStation start = trainStations.get(i);

                for (int j = i + 1; j < trainStations.size(); j++) {
                    TrainStation end = trainStations.get(j);

                    DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                    dailyTrainTicket.setDate(date);
                    dailyTrainTicket.setStartPinyin(start.getNamePinyin());
                    dailyTrainTicket.setStart(start.getName());
                    dailyTrainTicket.setTrainCode(dailyTrain.getCode());
                    dailyTrainTicket.setEndPinyin(end.getNamePinyin());
                    dailyTrainTicket.setStartIndex(start.getIndex());
                    dailyTrainTicket.setEndIndex(end.getIndex());
                    dailyTrainTicket.setStartTime(start.getOutTime());
                    dailyTrainTicket.setEnd(end.getName());
                    dailyTrainTicket.setEndTime(end.getInTime());
                    LambdaQueryWrapper<DailyTrainSeat> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(DailyTrainSeat::getTrainCode, dailyTrain.getCode());
                    // 查出车次对应的所有座位
                    List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectList(wrapper3);

                    // 先删除所有的余票信息
                    LambdaQueryWrapper<DailyTrainTicket> wrapper4 = new LambdaQueryWrapper<>();
                    wrapper4.eq(DailyTrainTicket::getTrainCode, dailyTrain.getCode()).eq(DailyTrainTicket::getStart, start.getName()).eq(DailyTrainTicket::getEnd, end.getName());
                    dailyTrainTicketMapper.delete(wrapper4);

                    Integer ydzNum = 0;
                    Integer edzNum = 0;
                    Integer rwNum = 0;
                    Integer ywNum = 0;

                    BigDecimal distance = end.getKm();
                    dailyTrainTicket.setYdzPrice(distance.multiply(SeatTypeEnum.YDZ.getPrice()));
                    dailyTrainTicket.setEdzPrice(distance.multiply(SeatTypeEnum.EDZ.getPrice()));
                    dailyTrainTicket.setRwPrice(distance.multiply(SeatTypeEnum.RW.getPrice()));
                    dailyTrainTicket.setYwPrice(distance.multiply(SeatTypeEnum.YW.getPrice()));

                    for (DailyTrainSeat dailyTrainSeat : dailyTrainSeats) {
                        if (Objects.equals(dailyTrainSeat.getSeatType(), SeatTypeEnum.YDZ.getCode())) {
                            ydzNum++;

                        } else if (Objects.equals(dailyTrainSeat.getSeatType(), SeatTypeEnum.EDZ.getCode())) {
                            ++edzNum;

                        } else if (Objects.equals(dailyTrainSeat.getSeatType(), SeatTypeEnum.RW.getCode())) {
                            ++rwNum;

                        } else if (Objects.equals(dailyTrainSeat.getSeatType(), SeatTypeEnum.YW.getCode())) {
                            ++ywNum;

                        }
                    }

                    dailyTrainTicket.setYdz(ydzNum);
                    dailyTrainTicket.setEdz(edzNum);
                    dailyTrainTicket.setRw(rwNum);
                    dailyTrainTicket.setYw(ywNum);

                    dailyTrainTicketMapper.insert(dailyTrainTicket);

                }
            }

        }

        return new Result(Result.SUCCESS_CODE);
    }
}
