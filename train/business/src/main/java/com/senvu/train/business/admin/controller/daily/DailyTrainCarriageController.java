package com.senvu.train.business.admin.controller.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainCarriageQueryDTO;
import com.senvu.train.business.admin.service.daily.DailyTrainCarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageController {

    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;

    @GetMapping("/query-list")
    public PageResult list(DailyTrainCarriageQueryDTO dailyTrainCarriageQueryDTO) {
        return dailyTrainCarriageService.list(dailyTrainCarriageQueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return dailyTrainCarriageService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody DailyTrainCarriageDTO dailyTrainCarriageDTO) {
         return dailyTrainCarriageService.save(dailyTrainCarriageDTO);
    }

    @PutMapping
    public Result update(@RequestBody DailyTrainCarriageDTO dailyTrainCarriageDTO) {
        return dailyTrainCarriageService.update(dailyTrainCarriageDTO);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return  dailyTrainCarriageService.delete(id);
    }

    @GetMapping("gen-carriage/{date}")
    public Result genCarriage(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        dailyTrainCarriageService.genCarriage(date);
        return new Result(Result.SUCCESS_CODE);
    }
}
