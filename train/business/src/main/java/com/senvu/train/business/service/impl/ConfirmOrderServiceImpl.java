package com.senvu.train.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senvu.Enum.ConfirmOrderStatusEnum;
import com.senvu.Enum.SeatTypeEnum;
import com.senvu.client.MemberFeignClient;
import com.senvu.context.LoginMemberContext;
import com.senvu.pojo.dto.TicketSaveDto;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.daily.DailyTrainCarriageMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainSeatMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainStationMapper;
import com.senvu.train.business.admin.mapper.daily.DailyTrainTicketMapper;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainCarriage;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainSeat;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainStation;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrainTicket;
import com.senvu.train.business.mapper.ConfirmOrderMapper;
import com.senvu.train.business.pojo.dto.ConfirmOrderDTO;
import com.senvu.train.business.pojo.dto.TicketDTO;
import com.senvu.train.business.pojo.dto.query.ConfirmOrderQueryDTO;
import com.senvu.train.business.pojo.entity.ConfirmOrder;
import com.senvu.train.business.pojo.entity.SellStorage;
import com.senvu.train.business.service.ConfirmOrderService;
import com.senvu.util.SnowUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {

    @Autowired
    private ConfirmOrderMapper confirmOrderMapper;
    @Autowired
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Autowired
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;
    @Autowired
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Autowired
    private DailyTrainStationMapper dailyTrainStationMapper;
    @Autowired
    private MemberFeignClient memberFeignClient;

    @Override
    public PageResult list(ConfirmOrderQueryDTO confirmOrderQueryDTO) {
        Integer page = confirmOrderQueryDTO.getPage();
        Integer size = confirmOrderQueryDTO.getSize();
        Page<ConfirmOrder> pages = new Page<>(page,size);
        LambdaQueryWrapper<ConfirmOrder> wrapper = new LambdaQueryWrapper<>();
        confirmOrderMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(confirmOrderMapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(ConfirmOrderDTO confirmOrderDTO) {
          ConfirmOrder confirmOrder = new ConfirmOrder();
          BeanUtils.copyProperties(confirmOrderDTO,confirmOrder);
     //   confirmOrderMapper.insert();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result update(ConfirmOrderDTO confirmOrderDTO) {
        ConfirmOrder confirmOrder = new ConfirmOrder();
        BeanUtils.copyProperties(confirmOrderDTO,confirmOrder);
     //   confirmOrderMapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        confirmOrderMapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result doConfirm(ConfirmOrderDTO confirmOrderDTO) {
        // 1. 生成订单
        ConfirmOrder confirmOrder = new ConfirmOrder();
        BeanUtils.copyProperties(confirmOrderDTO,confirmOrder);
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setMemberId(LoginMemberContext.getMemberId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(confirmOrder.getTickets());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LocalDateTime now = LocalDateTime.now();
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrderMapper.insertConfirmOrder(confirmOrder,json);

        // 进来先睡5秒钟
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 更新订单状态为处理中
        ConfirmOrder confirmOrder1 = new ConfirmOrder();
        confirmOrder1.setId(confirmOrder.getId());
        confirmOrder1.setStatus(ConfirmOrderStatusEnum.PENDING.getCode());
        confirmOrder1.setUpdateTime(LocalDateTime.now());
        confirmOrderMapper.updateConfirmOrderById(confirmOrder1);

        int lineNumber = confirmOrderDTO.getLineNumber();
        while (lineNumber > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lineNumber--;
                ConfirmOrder confirmOrder2 = new ConfirmOrder();
                confirmOrder2.setId(confirmOrder.getId());
                confirmOrder2.setLineNumber(lineNumber);
                confirmOrder2.setUpdateTime(LocalDateTime.now());
                confirmOrderMapper.updateConfirmOrderById(confirmOrder2);
            }
        }
        String trainCode = confirmOrder.getTrainCode();
        LocalDate date = confirmOrder.getDate();

        LambdaQueryWrapper<DailyTrainStation> dailyTrainStationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dailyTrainStationLambdaQueryWrapper.eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode);
        List<DailyTrainStation> dailyTrainStations3 = dailyTrainStationMapper.selectList(dailyTrainStationLambdaQueryWrapper);
        dailyTrainStations3.sort(Comparator.comparingInt(DailyTrainStation::getIndex));
        Integer finalStationIndex = dailyTrainStations3.get(dailyTrainStations3.size() - 1).getIndex();
        Integer beginStationIndex = dailyTrainStations3.get(0).getIndex();

        String start = confirmOrder.getStart();
        LambdaQueryWrapper<DailyTrainStation> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getName,start);
        List<DailyTrainStation> dailyTrainStations = dailyTrainStationMapper.selectList(wrapper3);
        DailyTrainStation startStation = dailyTrainStations.get(0);
        Integer startStationIndex = startStation.getIndex();

        String end = confirmOrder.getEnd();
        LambdaQueryWrapper<DailyTrainStation> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getName,end);
        List<DailyTrainStation> dailyTrainStations1 = dailyTrainStationMapper.selectList(wrapper4);
        DailyTrainStation endStation = dailyTrainStations1.get(0);
        Integer endStationIndex = endStation.getIndex();

        Long dailyTrainTicketId = confirmOrder.getDailyTrainTicketId();
        List<TicketDTO> tickets = confirmOrder.getTickets();
        TicketDTO ticket0 = tickets.get(0);

        // 全部是一等座或全部是二等座才支持选座
        // 当余票少于20时不支持选座
        // 要选座需要所有票都选座

            // 获得当前订单选择的座位的类型
            // 判断是否有选座

        // 2. 检查余票是否充足
        LambdaQueryWrapper<DailyTrainTicket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyTrainTicket::getId,dailyTrainTicketId);
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketMapper.selectList(wrapper);
        DailyTrainTicket dailyTrainTicket = dailyTrainTickets.get(0); // 这次车的票的详细信息

        Integer ydz = dailyTrainTicket.getYdz();
        Integer edz = dailyTrainTicket.getEdz();
        Integer rw = dailyTrainTicket.getRw();
        Integer yw = dailyTrainTicket.getYw();

        for (TicketDTO ticket : tickets) {
            String seatTypeCode = ticket.getSeatTypeCode();
            if (seatTypeCode.equals(SeatTypeEnum.YDZ.getCode())){
                ydz--;
            } else if (seatTypeCode.equals(SeatTypeEnum.EDZ.getCode())) {
                edz--;
            } else if (seatTypeCode.equals(SeatTypeEnum.RW.getCode())) {
                rw--;
            } else if (seatTypeCode.equals(SeatTypeEnum.YW.getCode())) {
                yw--;
            }
        }

        if (ydz < 0 || edz < 0 || rw < 0 || yw < 0) {
            ConfirmOrder confirmOrder2 = new ConfirmOrder();
            confirmOrder2.setId(confirmOrder.getId());
            confirmOrder2.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
            confirmOrder2.setUpdateTime(LocalDateTime.now());
            confirmOrderMapper.updateConfirmOrderById(confirmOrder);
            Result result = new Result();
            result.setCode(Result.FAILD_CODE);
            result.setMessage(ConfirmOrderStatusEnum.EMPTY.getDesc());
            result.setData(String.valueOf(confirmOrder.getId()));
            return result;
        }

        // 获得对应的车票信息

        if (ticket0.getSeat() != null) {

            // 支持选座
            // 1 A B C D F
            // 2 A B C D F

            String seatTypeCode = ticket0.getSeatTypeCode();
            Map<String, Integer> map = new HashMap<>();
            if (seatTypeCode.equals(SeatTypeEnum.YDZ.getCode())) {
                map.put("A1",0);
                map.put("C1",1);
                map.put("D1",2);
                map.put("F1",3);
                map.put("A2",4);
                map.put("C2",5);
                map.put("D2",6);
                map.put("F2",7);
            } else {
                map.put("A1",0);
                map.put("B1",1);
                map.put("C1",2);
                map.put("D1",3);
                map.put("F1",4);
                map.put("A2",5);
                map.put("B2",6);
                map.put("C2",7);
                map.put("D2",8);
                map.put("F2",9);
            }
            List<String> seatList = new ArrayList<>();
            for (TicketDTO ticket : tickets) {
                String seat = ticket.getSeat();
                seatList.add(seat);
            }
            seatList.sort(Comparator.naturalOrder());
            String baseSeat = seatList.get(0);// 最小的位置的坐标 如A1
            String col = String.valueOf(baseSeat.charAt(0));
            Integer baseIndex = map.get(baseSeat);// 最小的位置的坐标对应的索引
            Map<String,Integer> plusMap = new HashMap<>();
            for (TicketDTO ticket : tickets) {
                String passengerId = ticket.getPassengerId();
                Integer plusIndex = map.get(ticket.getSeat()) - baseIndex;
                plusMap.put(passengerId,plusIndex);
            }
            Map <String,SellStorage> storageMap = new HashMap<>();
            Integer ticketsNum = tickets.size();// 需要处理的车票的总数
            Integer nowTickets = ticketsNum;
            LambdaQueryWrapper<DailyTrainCarriage> wrapper1 = new LambdaQueryWrapper<>();
            // 根据日期 车次 座位类型找到对应的车厢
            wrapper1.eq(DailyTrainCarriage::getTrainCode,trainCode).eq(DailyTrainCarriage::getDate,date).eq(DailyTrainCarriage::getSeatType,seatTypeCode);
            List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(wrapper1);
            // 对每一个车厢进行处理
 carriage:   for (DailyTrainCarriage dailyTrainCarriage : dailyTrainCarriages) {
                // 获得车厢的编号
                Integer index = dailyTrainCarriage.getIndex();
                // 根据日期 车次 座位类型 车厢获得对应的车厢内的所有座位
                LambdaQueryWrapper<DailyTrainSeat> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(DailyTrainSeat::getCarriageIndex,index).eq(DailyTrainSeat::getDate,date).eq(DailyTrainSeat::getTrainCode,trainCode).eq(DailyTrainSeat::getSeatType,seatTypeCode);
                List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectList(wrapper2);
                // 对车厢内的所有座位进行处理
          flag:  for (DailyTrainSeat dailyTrainSeat : dailyTrainSeats) {
                    // 获取座位的列编号 用于与选票时的编号的对应
                    String col1 = dailyTrainSeat.getCol();
                    // 获取第一个位置的车厢内的座位索引
                    Integer carriageSeatIndex = dailyTrainSeat.getCarriageSeatIndex();
                    // 如果第一张票的列编号与当前座位相同 进入处理 否则跳过这个座位
                    if (col1.equals(col)) {
                        // 获取当前座位的售卖情况 如果中间站有被售出的 不可以被买 跳过这个座位
                        String sell = dailyTrainSeat.getSell();
                        for (int i = startStationIndex; i <= endStationIndex - 1; i++) {
                            if (sell.charAt(i) == '1') {
                                continue flag;
                            }
                        }
                        // 当第一个位置符合条件时 加上偏移量获取所有车票的在车厢中对应的位置索引
                        // 需要用一个新的hashmap来存储结果 避免因为中间有一个位置不符合要求需要全部回退但部分数据被更新的情况出现
                        Map<String,Integer> indexMap = new HashMap<>();
                        for (var entry : plusMap.entrySet()) {
                            Integer newIndex = entry.getValue() + carriageSeatIndex;
                            // 当座位索引超出车厢的座位总数时 直接去下一个车厢
                            if (newIndex > dailyTrainCarriage.getSeatCount()){
                                continue carriage;
                            }
                            indexMap.put(entry.getKey(), newIndex);
                        }
                        // 当全部符合要求的时候 进行下一步车票的处理
                        for (var entry : indexMap.entrySet()) {
                            // 根据日期 车次 车厢号 座位类型 座位在车厢内的索引来查找出确定的座位
                            LambdaQueryWrapper<DailyTrainSeat> wrapper5 = new LambdaQueryWrapper<>();
                            wrapper5.eq(DailyTrainSeat::getTrainCode,trainCode).eq(DailyTrainSeat::getDate,date).eq(DailyTrainSeat::getCarriageIndex,dailyTrainCarriage.getIndex()).eq(DailyTrainSeat::getSeatType,seatTypeCode).eq(DailyTrainSeat::getCarriageSeatIndex,entry.getValue());
                            List<DailyTrainSeat> dailyTrainSeats1 = dailyTrainSeatMapper.selectList(wrapper5);
                            // 确定的座位
                            DailyTrainSeat dailyTrainSeat1 = dailyTrainSeats1.get(0);
                            // 判断销售情况 如果有一张票的中间站被售卖 数据回滚 去往下一个座位
                            String sell1 = dailyTrainSeat1.getSell();
                            for (int i = startStationIndex; i <= endStationIndex - 1; i++) {
                                if (sell1.charAt(i) == '1') {
                                    nowTickets = ticketsNum;
                                    continue flag;
                                }
                            }
                            // 当该座位符合要求 进行数据存储 供后续购买
                            StringBuilder stringBuilder = new StringBuilder(sell1);
                            for (int i = startStationIndex; i <= endStationIndex - 1; i++) {
                                stringBuilder.setCharAt(i,'1');
                            }
                            // 更新座位的售卖情况
                            String newSell = stringBuilder.toString();
                            SellStorage sellStorage = new SellStorage();
                            sellStorage.setSell(newSell);
                            sellStorage.setSeatCol(dailyTrainSeat1.getCol());
                            sellStorage.setSeatRow(dailyTrainSeat1.getRow());
                            sellStorage.setDailyTrainSeatId(dailyTrainSeat1.getId());
                            sellStorage.setCarriageIndex(dailyTrainCarriage.getIndex());
                            sellStorage.setSeatType(seatTypeCode);
                            sellStorage.setDailyTrainSeatId(dailyTrainSeat.getId());
                            // 将乘客和他对应的车票数据存储
                            storageMap.put(entry.getKey(),sellStorage);
                            // 将当前待处理车票数量减1
                            nowTickets--;
                        }
                        // 每个座位处理完 判断是否处理完所有的车票
                        if (nowTickets == 0) {
                            // 全部处理完成 可以进行订单信息的更新 车票信息的生成 和 库存的扣减 座位售卖信息的更新

                            List<TicketSaveDto> list = new ArrayList<>();
                            for (TicketDTO ticket : tickets) {
                                // 获得乘客对应的车票存储信息
                                String passengerId = ticket.getPassengerId();
                                SellStorage sellStorage = storageMap.get(passengerId);
                                // 更新座位售卖信息 同一个车次内与这个区间有交集的车站的车票的库存和座位的信息都要被更新
                                LambdaQueryWrapper<DailyTrainSeat> wrapper5 = new LambdaQueryWrapper<>();
                                // 根据日期 车次 车厢 座位类型 座位排数 座位列数 查询出确定的座位信息
                                wrapper5.eq(DailyTrainSeat::getDate,date).eq(DailyTrainSeat::getTrainCode,trainCode).eq(DailyTrainSeat::getCarriageIndex,dailyTrainCarriage.getIndex()).eq(DailyTrainSeat::getSeatType,seatTypeCode).eq(DailyTrainSeat::getCol,sellStorage.getSeatCol()).eq(DailyTrainSeat::getRow,sellStorage.getSeatRow());
                                List<DailyTrainSeat> dailyTrainSeats1 = dailyTrainSeatMapper.selectList(wrapper5);
                                // 更新座位
                                DailyTrainSeat dailyTrainSeat1 = dailyTrainSeats1.get(0);
                                dailyTrainSeat1.setSell(sellStorage.getSell());
                                dailyTrainSeatMapper.updateById(dailyTrainSeat1);
                                // 乘客车票信息的生成 // todo: 可以添加付款功能
                                TicketSaveDto ticketSaveDto = new TicketSaveDto();
                                ticketSaveDto.setPassengerId(ticket.getPassengerId());
                                ticketSaveDto.setPassengerIdCard(ticket.getPassengerIdCard());
                                ticketSaveDto.setPassengerName(ticket.getPassengerName());
                                ticketSaveDto.setPassengerType(ticket.getPassengerType());
                                ticketSaveDto.setSeat(ticket.getSeat());
                                ticketSaveDto.setSeatTypeCode(seatTypeCode);
                                ticketSaveDto.setCarriageIndex(dailyTrainCarriage.getIndex());
                                ticketSaveDto.setStart(start);
                                ticketSaveDto.setEnd(end);
                                ticketSaveDto.setTrainCode(trainCode);
                                ticketSaveDto.setDate(date);
                                ticketSaveDto.setStartTime(dailyTrainStationMapper.selectList(new LambdaQueryWrapper<DailyTrainStation>().eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getIndex,startStationIndex)).get(0).getOutTime());
                                ticketSaveDto.setEndTime(dailyTrainStationMapper.selectList(new LambdaQueryWrapper<DailyTrainStation>().eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getIndex,endStationIndex)).get(0).getInTime());
                                ticketSaveDto.setCol(sellStorage.getSeatCol());
                                ticketSaveDto.setRow(sellStorage.getSeatRow());
                                ticketSaveDto.setMemberId(LoginMemberContext.getMemberId());
                                list.add(ticketSaveDto);
                            }
                            // 生成乘客车票
                            memberFeignClient.save(list);
                            // 查询出同日的同一车次的所有经过的车站
                            LambdaQueryWrapper<DailyTrainStation> wrapper5 = new LambdaQueryWrapper<>();
                            wrapper5.eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getDate,date);
                            List<DailyTrainStation> dailyTrainStations2 = dailyTrainStationMapper.selectList(wrapper5);
                            dailyTrainStations2.sort(Comparator.comparingInt(DailyTrainStation::getIndex));
                            // 减少库存 因为全部都是相同的座位 所以查出对应座位类型的余票直接相减就可以了
                            for (DailyTrainStation dailyTrainStation : dailyTrainStations2) {
                                Integer index1 = dailyTrainStation.getIndex();
                                if (index1 < startStationIndex) {
                                    for (int i = startStationIndex + 1; i <= finalStationIndex; i++) {
                                        LambdaQueryWrapper<DailyTrainTicket> wrapper6 = new LambdaQueryWrapper<>();
                                        wrapper6.eq(DailyTrainTicket::getDate,date).eq(DailyTrainTicket::getTrainCode,trainCode).eq(DailyTrainTicket::getStartIndex,index1).eq(DailyTrainTicket::getEndIndex,i);
                                        List<DailyTrainTicket> dailyTrainTickets1 = dailyTrainTicketMapper.selectList(wrapper6);
                                        DailyTrainTicket dailyTrainTicket1 = dailyTrainTickets1.get(0);
                                        if (seatTypeCode.equals(SeatTypeEnum.YDZ.getCode())) {
                                            dailyTrainTicket1.setYdz(dailyTrainTicket1.getYdz() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.EDZ.getCode())) {
                                            dailyTrainTicket1.setEdz(dailyTrainTicket1.getEdz() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.RW.getCode())) {
                                            dailyTrainTicket1.setRw(dailyTrainTicket1.getRw() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.YW.getCode())) {
                                            dailyTrainTicket1.setYw(dailyTrainTicket1.getYw() - ticketsNum);
                                        }
                                        dailyTrainTicketMapper.updateById(dailyTrainTicket1);
                                    }
                                } else if (index1 >= startStationIndex && index1 < endStationIndex) {
                                    for (int i = index1 + 1; i <= finalStationIndex; i++) {
                                        LambdaQueryWrapper<DailyTrainTicket> wrapper6 = new LambdaQueryWrapper<>();
                                        wrapper6.eq(DailyTrainTicket::getDate,date).eq(DailyTrainTicket::getTrainCode,trainCode).eq(DailyTrainTicket::getStartIndex,index1).eq(DailyTrainTicket::getEndIndex,i);
                                        List<DailyTrainTicket> dailyTrainTickets1 = dailyTrainTicketMapper.selectList(wrapper6);
                                        DailyTrainTicket dailyTrainTicket1 = dailyTrainTickets1.get(0);
                                        if (seatTypeCode.equals(SeatTypeEnum.YDZ.getCode())) {
                                            dailyTrainTicket1.setYdz(dailyTrainTicket1.getYdz() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.EDZ.getCode())) {
                                            dailyTrainTicket1.setEdz(dailyTrainTicket1.getEdz() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.RW.getCode())) {
                                            dailyTrainTicket1.setRw(dailyTrainTicket1.getRw() - ticketsNum);
                                        } else if (seatTypeCode.equals(SeatTypeEnum.YW.getCode())) {
                                            dailyTrainTicket1.setYw(dailyTrainTicket1.getYw() - ticketsNum);
                                        }
                                        dailyTrainTicketMapper.updateById(dailyTrainTicket1);
                                    }
                                }
                            }

                            // 更新订单信息
                            ConfirmOrder confirmOrder2 = new ConfirmOrder();
                            confirmOrder2.setUpdateTime(LocalDateTime.now());
                            confirmOrder2.setStatus(ConfirmOrderStatusEnum.SUCCESS.getCode());
                            confirmOrder2.setId(confirmOrder.getId());
                            confirmOrderMapper.updateConfirmOrderById(confirmOrder2);

                            Result result = new Result();
                            result.setCode(Result.SUCCESS_CODE);
                            result.setMessage(ConfirmOrderStatusEnum.SUCCESS.getDesc());
                            result.setData(String.valueOf(confirmOrder.getId()));
                            return result;
                        } else {
                            nowTickets = ticketsNum;
                        }
                    }
                }
            }
            // 所有车厢处理完 如果还没有处理完 失败
            if (nowTickets != 0) {
                ConfirmOrder confirmOrder2 = new ConfirmOrder();
                confirmOrder2.setId(confirmOrder.getId());
                confirmOrder2.setUpdateTime(LocalDateTime.now());
                confirmOrder2.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                confirmOrderMapper.updateConfirmOrderById(confirmOrder);
                Result result = new Result();
                result.setCode(Result.FAILD_CODE);
                result.setMessage(ConfirmOrderStatusEnum.EMPTY.getDesc());
                result.setData(String.valueOf(confirmOrder.getId()));
                return result;
            }
        } else {
            // 不支持选座
            // todo:可以进行算法优化
            int ticketsNum = tickets.size();
            int nowTickets = ticketsNum;
            Map<String,SellStorage> map = new HashMap<>();
            // 3. 查找车厢对应的符合的座位
            // 对每张车票进行处理
            Set<Long> seatIds = new HashSet<>();
           ticket: for (TicketDTO ticket : tickets) {
                // 获得该张车票的座位类型
                String seatTypeCode = ticket.getSeatTypeCode();
                // 根据日期 车次 座位类型 来查找所有符合的车厢
                LambdaQueryWrapper<DailyTrainCarriage> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DailyTrainCarriage::getTrainCode,trainCode).eq(DailyTrainCarriage::getDate,date).eq(DailyTrainCarriage::getSeatType,seatTypeCode);
                List<DailyTrainCarriage> dailyTrainCarriages = dailyTrainCarriageMapper.selectList(wrapper1);
                // 对每一个车厢进行寻找 看有没有符合要求的座位
                for (DailyTrainCarriage dailyTrainCarriage : dailyTrainCarriages) {
                    // 根据车次 日期 车厢号 座位类型 查找所有座位
                    LambdaQueryWrapper<DailyTrainSeat> wrapper2 = new LambdaQueryWrapper<>();
                    wrapper2.eq(DailyTrainSeat::getCarriageIndex,dailyTrainCarriage.getIndex()).eq(DailyTrainSeat::getTrainCode,trainCode).eq(DailyTrainSeat::getDate,date).eq(DailyTrainSeat::getSeatType,seatTypeCode);
                    List<DailyTrainSeat> dailyTrainSeats = dailyTrainSeatMapper.selectList(wrapper2);// 获得对应座位类型的每一个车厢的所有座位

                    // 在每一个车厢的所有座位中找到空闲的座位并进行选择
                    // 1 -> 2 -> 3 -> 4 -> 5
                    // 0    1    2    3    4
                    //   0    0    0    0
                    // 两站之间 如果有一站的座位是被买了的 那么这个座位不可以被买

                   flag: for (DailyTrainSeat dailyTrainSeat : dailyTrainSeats) {
                        String sell = dailyTrainSeat.getSell();
                        // 判断售卖情况 如果中间站被买 那么这个座位不可以被买 去找下一个座位
                        for (int i = startStationIndex; i <= endStationIndex - 1; i++) {
                            if (sell.charAt(i) == '1' || seatIds.contains(dailyTrainSeat.getId())) {
                                continue flag;
                            }
                        }
                        // 生成新的座位售卖信息 标记这个座位已经被预订
                       seatIds.add(dailyTrainSeat.getId());
                       StringBuilder stringBuilder = new StringBuilder(dailyTrainSeat.getSell());
                       for (int i = startStationIndex; i <= endStationIndex - 1; i++) {
                           stringBuilder.setCharAt(i,'1');
                       }
                       String newSell = stringBuilder.toString();
                       // 进行存储
                       SellStorage sellStorage = new SellStorage();
                       sellStorage.setSell(newSell);
                       Long id = dailyTrainSeat.getId();
                       sellStorage.setDailyTrainSeatId(id);
                       sellStorage.setCarriageIndex(dailyTrainCarriage.getIndex());
                       String seatType = ticket.getSeatTypeCode();
                       sellStorage.setSeatType(seatType);
                       String row = dailyTrainSeat.getRow();
                       String col = dailyTrainSeat.getCol();
                       sellStorage.setSeatCol(col);
                       sellStorage.setSeatRow(row);
                       map.put(ticket.getPassengerId(),sellStorage);
                       ticketsNum--;

                       if (ticketsNum == 0) {
                           // 剩余处理票数为0 全部有票 需要减少对应库存 订单状态更新
                           // 5. 扣减座位库存
                           List<TicketDTO> tickets1 = confirmOrder.getTickets();
                           List<TicketSaveDto> ticketSaveDtos = new ArrayList<>();
                           for (TicketDTO ticket1 : tickets1) {

                               String passengerId = ticket1.getPassengerId();
                               SellStorage sellStorage1 = map.get(passengerId);
                               // 设置车票其他属性
                               TicketSaveDto ticketSaveDto = new TicketSaveDto();
                               BeanUtils.copyProperties(ticket1,ticketSaveDto);
                               String seatType1 = sellStorage1.getSeatType();
                               ticketSaveDto.setDate(date);
                               ticketSaveDto.setCarriageIndex(sellStorage1.getCarriageIndex());
                               ticketSaveDto.setStart(start);
                               ticketSaveDto.setEnd(end);
                               ticketSaveDto.setTrainCode(trainCode);
                               ticketSaveDto.setCol(sellStorage1.getSeatCol());
                               ticketSaveDto.setRow(sellStorage1.getSeatRow());
                               ticketSaveDto.setStartTime(dailyTrainStationMapper.selectList(new LambdaQueryWrapper<DailyTrainStation>().eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getIndex,startStationIndex)).get(0).getOutTime());
                               ticketSaveDto.setEndTime(dailyTrainStationMapper.selectList(new LambdaQueryWrapper<DailyTrainStation>().eq(DailyTrainStation::getDate,date).eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getIndex,endStationIndex)).get(0).getInTime());
                               ticketSaveDto.setMemberId(LoginMemberContext.getMemberId());
                               ticketSaveDtos.add(ticketSaveDto);
                               // 更新座位售卖状态
                               DailyTrainSeat dailyTrainSeat1 = dailyTrainSeatMapper.selectById(sellStorage1.getDailyTrainSeatId());
                               dailyTrainSeat1.setSell(sellStorage1.getSell());
                               dailyTrainSeatMapper.updateById(dailyTrainSeat1);
                               // 更新票的库存
                               // 查询出同日的同一车次的所有经过的车站
                               LambdaQueryWrapper<DailyTrainStation> wrapper5 = new LambdaQueryWrapper<>();
                               wrapper5.eq(DailyTrainStation::getTrainCode,trainCode).eq(DailyTrainStation::getDate,date);
                               List<DailyTrainStation> dailyTrainStations2 = dailyTrainStationMapper.selectList(wrapper5);
                               dailyTrainStations2.sort(Comparator.comparingInt(DailyTrainStation::getIndex));
                               // 0 1 2 3
                               // 0 -> 2
                               // 0 -> 1 0 ->
                               for (DailyTrainStation dailyTrainStation : dailyTrainStations2) {
                                   Integer index1 = dailyTrainStation.getIndex();
                                   if (index1 < startStationIndex) {
                                       for (int i = startStationIndex + 1; i <= finalStationIndex; i++) {
                                           LambdaQueryWrapper<DailyTrainTicket> wrapper6 = new LambdaQueryWrapper<>();
                                           wrapper6.eq(DailyTrainTicket::getDate,date).eq(DailyTrainTicket::getTrainCode,trainCode).eq(DailyTrainTicket::getStartIndex,index1).eq(DailyTrainTicket::getEndIndex,i);
                                           List<DailyTrainTicket> dailyTrainTickets1 = dailyTrainTicketMapper.selectList(wrapper6);
                                           DailyTrainTicket dailyTrainTicket1 = dailyTrainTickets1.get(0);
                                           if (seatType1.equals(SeatTypeEnum.YDZ.getCode())) {
                                               dailyTrainTicket1.setYdz(dailyTrainTicket1.getYdz() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.EDZ.getCode())) {
                                               dailyTrainTicket1.setEdz(dailyTrainTicket1.getEdz() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.RW.getCode())) {
                                               dailyTrainTicket1.setRw(dailyTrainTicket1.getRw() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.YW.getCode())) {
                                               dailyTrainTicket1.setYw(dailyTrainTicket1.getYw() - 1);
                                           }
                                           dailyTrainTicketMapper.updateById(dailyTrainTicket1);
                                       }
                                   } else if (index1 >= startStationIndex && index1 < endStationIndex) {
                                       for (int i = index1 + 1; i <= finalStationIndex; i++) {
                                           LambdaQueryWrapper<DailyTrainTicket> wrapper6 = new LambdaQueryWrapper<>();
                                           wrapper6.eq(DailyTrainTicket::getDate,date).eq(DailyTrainTicket::getTrainCode,trainCode).eq(DailyTrainTicket::getStartIndex,index1).eq(DailyTrainTicket::getEndIndex,i);
                                           List<DailyTrainTicket> dailyTrainTickets1 = dailyTrainTicketMapper.selectList(wrapper6);
                                           DailyTrainTicket dailyTrainTicket1 = dailyTrainTickets1.get(0);
                                           if (seatType1.equals(SeatTypeEnum.YDZ.getCode())) {
                                               dailyTrainTicket1.setYdz(dailyTrainTicket1.getYdz() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.EDZ.getCode())) {
                                               dailyTrainTicket1.setEdz(dailyTrainTicket1.getEdz() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.RW.getCode())) {
                                               dailyTrainTicket1.setRw(dailyTrainTicket1.getRw() - 1);
                                           } else if (seatType1.equals(SeatTypeEnum.YW.getCode())) {
                                               dailyTrainTicket1.setYw(dailyTrainTicket1.getYw() - 1);
                                           }
                                           dailyTrainTicketMapper.updateById(dailyTrainTicket1);
                                       }
                                   }
                               }
                           }
                           // 更新订单信息
                           ConfirmOrder confirmOrder2 = new ConfirmOrder();
                           LocalDateTime time = LocalDateTime.now();
                           confirmOrder2.setId(confirmOrder.getId());
                           confirmOrder2.setUpdateTime(time);
                           confirmOrder2.setStatus(ConfirmOrderStatusEnum.SUCCESS.getCode());
                           confirmOrderMapper.updateConfirmOrderById(confirmOrder2);
                           Result result = new Result();
                           result.setCode(Result.SUCCESS_CODE);
                           result.setMessage(ConfirmOrderStatusEnum.SUCCESS.getDesc());
                           result.setData(String.valueOf(confirmOrder.getId()));
                           // 6. 生成会员车票
                           memberFeignClient.save(ticketSaveDtos);
                           return result;
                       } else {
                            continue ticket;
                       }
                   }
                }
            }

            // 4. 更新订单状态

            // 剩余处理票数不为0 说明票不够 订单状态更新
            if (ticketsNum > 0) {
                ConfirmOrder confirmOrder2 = new ConfirmOrder();
                confirmOrder2.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                confirmOrder2.setUpdateTime(LocalDateTime.now());
                confirmOrder2.setId(confirmOrder.getId());
                confirmOrderMapper.updateConfirmOrderById(confirmOrder2);
                Result result = new Result();
                result.setCode(Result.FAILD_CODE);
                result.setMessage(ConfirmOrderStatusEnum.EMPTY.getDesc());
                result.setData(String.valueOf(confirmOrder.getId()));
                return result;
            }

        }

        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result queryCount(Long confirmOrderId) {
        Result result = new Result();
        result.setCode(Result.SUCCESS_CODE);
        ConfirmOrder confirmOrder = confirmOrderMapper.selectById(confirmOrderId);
        Integer lineNumber = confirmOrder.getLineNumber();
        if (lineNumber > 0) {
            result.setData(lineNumber);
        } else {
            ConfirmOrder confirmOrder1 = confirmOrderMapper.selectById(confirmOrderId);
            String status = confirmOrder1.getStatus();
            if (Objects.equals(status, "S")) {
                result.setData(-1);
            } else if (Objects.equals(status, "F")) {
                result.setData(-2);
            } else if (Objects.equals(status, "E")) {
                result.setData(-3);
            }
        }
        return result;
    }
}
