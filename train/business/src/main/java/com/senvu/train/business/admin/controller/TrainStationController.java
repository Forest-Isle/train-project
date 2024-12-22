package com.senvu.train.business.admin.controller;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainStationDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainStationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainStation;
import com.senvu.train.business.admin.service.TrainStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationController {

    @Autowired
    private TrainStationService trainStationService;

    @GetMapping("/query-list")
    public PageResult list(TrainStationQueryDTO trainStationQueryDTO) {
        return trainStationService.list(trainStationQueryDTO);
    }

    @GetMapping("/{id}")
    public TrainStation getById(@PathVariable Long id) {
        return trainStationService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody TrainStationDTO trainStationDTO) {
        return trainStationService.save(trainStationDTO);
    }

    @PutMapping
    public void update(@RequestBody TrainStationDTO trainStationDTO) {
        trainStationService.update(trainStationDTO);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        trainStationService.delete(id);
    }

    @GetMapping("query-all")
    public Result all(){
        return trainStationService.all();
    }
}
