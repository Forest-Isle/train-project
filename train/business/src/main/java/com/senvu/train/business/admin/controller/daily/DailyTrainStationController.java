package com.senvu.train.business.admin.controller.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainStationQueryDTO;
import com.senvu.train.business.admin.service.daily.DailyTrainStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationController {

    @Autowired
    private DailyTrainStationService dailyTrainStationService;

    @GetMapping("/query-list")
    public PageResult list(DailyTrainStationQueryDTO dailyTrainStationQueryDTO) {
        return dailyTrainStationService.list(dailyTrainStationQueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return dailyTrainStationService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody DailyTrainStationDTO dailyTrainStationDTO) {
         return dailyTrainStationService.save(dailyTrainStationDTO);
    }

    @PutMapping
    public Result update(@RequestBody DailyTrainStationDTO dailyTrainStationDTO) {
        return dailyTrainStationService.update(dailyTrainStationDTO);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return  dailyTrainStationService.delete(id);
    }

    @GetMapping("gen-train-station/{date}")
    public Result genDailyTrainStation(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return dailyTrainStationService.gen(date);
    }
}
