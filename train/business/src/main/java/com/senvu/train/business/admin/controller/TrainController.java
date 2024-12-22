package com.senvu.train.business.admin.controller;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.TrainDTO;
import com.senvu.train.business.admin.pojo.dto.query.TrainQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Train;
import com.senvu.train.business.admin.pojo.vo.TrainVO;
import com.senvu.train.business.admin.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping("/query-list")
    public PageResult list(TrainQueryDTO trainQueryDTO) {
        return trainService.list(trainQueryDTO);
    }

    @GetMapping("/{id}")
    public Train getById(@PathVariable Long id) {
        return trainService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody TrainDTO trainDTO) {
        return trainService.save(trainDTO);
    }

    @PutMapping
    public void update(@RequestBody TrainDTO trainDTO) {
        trainService.update(trainDTO);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        trainService.delete(id);
    }

    @GetMapping("query-all")
    public List<TrainVO> all(){
        return trainService.all();
    }

    @GetMapping("gen-seat/{code}")
    public void genSeat(@PathVariable String code){
         trainService.genSeat(code);
    }
}
