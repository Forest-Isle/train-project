package com.senvu.train.business.admin.controller;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainCarriageDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainCarriageQueryDTO;
import com.senvu.train.business.admin.pojo.entity.TrainCarriage;
import com.senvu.train.business.admin.pojo.vo.TrainCarriageVO;
import com.senvu.train.business.admin.service.TrainCarriageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageController {

    @Autowired
    private TrainCarriageService trainCarriageService;

    @GetMapping("/query-list")
    public PageResult list(TrainCarriageQueryDTO trainCarriageQueryDTO) {
        return trainCarriageService.list(trainCarriageQueryDTO);
    }

    @GetMapping("/{id}")
    public TrainCarriage getById(@PathVariable Long id) {
        return trainCarriageService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody TrainCarriageDTO trainCarriageDTO) {
        return trainCarriageService.save(trainCarriageDTO);
    }

    @PutMapping
    public void update(@RequestBody TrainCarriageDTO trainCarriageDTO) {
        trainCarriageService.update(trainCarriageDTO);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
    }

    @GetMapping("query-all")
    public List<TrainCarriageVO> all() {
        return trainCarriageService.all();
    }
}
