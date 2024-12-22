package com.senvu.train.business.admin.controller;

import com.senvu.result.PageResult;
import com.senvu.train.business.admin.pojo.dto.TrainSeatDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainSeatQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainSeat;
import com.senvu.train.business.admin.service.TrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatController {

    @Autowired
    private TrainSeatService trainSeatService;

    @GetMapping("/query-list")
    public PageResult list(TrainSeatQueryDTO trainSeatQueryDTO) {
        return trainSeatService.list(trainSeatQueryDTO);
    }

    @GetMapping("/{id}")
    public TrainSeat getById(@PathVariable Long id) {
        return trainSeatService.getById(id);
    }

    @PostMapping("save")
    public void save(@RequestBody TrainSeatDTO trainSeatDTO) {
        trainSeatService.save(trainSeatDTO);
    }

    @PutMapping
    public void update(@RequestBody TrainSeatDTO trainSeatDTO) {
        trainSeatService.update(trainSeatDTO);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        trainSeatService.delete(id);
    }
}
