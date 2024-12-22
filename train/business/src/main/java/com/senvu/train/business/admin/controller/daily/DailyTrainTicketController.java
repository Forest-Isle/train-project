package com.senvu.train.business.admin.controller.daily;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.daily.DailyTrainTicketDTO;
import com.senvu.train.business.admin.pojo.dto.query.daily.DailyTrainTicketQueryDTO;
import com.senvu.train.business.admin.service.daily.DailyTrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketController {

    @Autowired
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public PageResult list(DailyTrainTicketQueryDTO dailyTrainTicketQueryDTO) {
        return dailyTrainTicketService.list(dailyTrainTicketQueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return dailyTrainTicketService.getById(id);
    }

    @PostMapping
    public Result save(@RequestBody DailyTrainTicketDTO dailyTrainTicketDTO) {
         return dailyTrainTicketService.save(dailyTrainTicketDTO);
    }

    @PutMapping
    public Result update(@RequestBody DailyTrainTicketDTO dailyTrainTicketDTO) {
        return dailyTrainTicketService.update(dailyTrainTicketDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return  dailyTrainTicketService.delete(id);
    }
}
