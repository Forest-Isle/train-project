package com.senvu.train.business.admin.controller.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainSeatQueryDTO;
import com.senvu.train.business.admin.service.daily.DailyTrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatController {

    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    @GetMapping("/query-list")
    public PageResult list(DailyTrainSeatQueryDTO dailyTrainSeatQueryDTO) {
        return dailyTrainSeatService.list(dailyTrainSeatQueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return dailyTrainSeatService.getById(id);
    }

    @PostMapping
    public Result save(@RequestBody DailyTrainSeatDTO dailyTrainSeatDTO) {
         return dailyTrainSeatService.save(dailyTrainSeatDTO);
    }

    @PutMapping
    public Result update(@RequestBody DailyTrainSeatDTO dailyTrainSeatDTO) {
        return dailyTrainSeatService.update(dailyTrainSeatDTO);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return  dailyTrainSeatService.delete(id);
    }

    @GetMapping("gen-seat/{date}")
    public Result genSeat(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date){
        dailyTrainSeatService.genSeat(date);
        return new Result(Result.SUCCESS_CODE);
    }
}
