package com.senvu.train.business.admin.controller.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.daily.DailyTrain;
import com.senvu.train.business.admin.service.daily.DailyTrainCarriageService;
import com.senvu.train.business.admin.service.daily.DailyTrainSeatService;
import com.senvu.train.business.admin.service.daily.DailyTrainService;
import com.senvu.train.business.admin.service.daily.DailyTrainStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainController {

    @Autowired
    private DailyTrainService dailyTrainService;
    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;
    @Autowired
    private DailyTrainStationService dailyTrainStationService;

    @GetMapping("/query-list")
    public PageResult list(DailyTrainQueryDTO dailyTrainQueryDTO) {
        return dailyTrainService.list(dailyTrainQueryDTO);
    }

    @GetMapping("/{id}")
    public DailyTrain getById(@PathVariable Long id) {
        return dailyTrainService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody DailyTrainDTO dailyTrainDTO) {
        return dailyTrainService.save(dailyTrainDTO);
    }

    @PutMapping
    public Result update(@RequestBody DailyTrainDTO dailyTrainDTO) {
        return dailyTrainService.update(dailyTrainDTO);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return dailyTrainService.delete(id);
    }

    @GetMapping("gen-train/{date}")
    public Result genTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        dailyTrainService.gen(date);
        return new Result(Result.SUCCESS_CODE);
    }

    @GetMapping("gen-daily/{date}")
    public Result genAll(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        dailyTrainService.gen(date);
        dailyTrainCarriageService.genCarriage(date);
        dailyTrainSeatService.genSeat(date);
        dailyTrainStationService.gen(date);
        return new Result<>(Result.SUCCESS_CODE);
    }
}
