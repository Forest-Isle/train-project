package com.senvu.client;

import com.senvu.pojo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient("business")
public interface BusinessFeignClient {

    @GetMapping("/business/admin/daily-train/gen-train/{date}")
    Result genTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/business/admin/daily-train-station/gen-train-station/{date}")
    Result genDailyTrainStation(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/business/admin/daily-train-carriage/gen-carriage/{date}")
    Result genCarriage(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/business/admin/daily-train-seat/gen-seat/{date}")
    Result genSeat(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date);
}
