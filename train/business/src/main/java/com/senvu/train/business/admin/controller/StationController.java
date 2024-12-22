package com.senvu.train.business.admin.controller;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.pojo.dto.StationDTO;
import com.senvu.train.business.admin.pojo.dto.query.StationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Station;
import com.senvu.train.business.admin.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/query-list")
    public PageResult list(StationQueryDTO stationQueryDTO) {
        return stationService.list(stationQueryDTO);
    }

    @GetMapping("/query-all")
    public Result all() {
        return stationService.all();
    }

    @GetMapping("/{id}")
    public Station getById(@PathVariable Long id) {
        return stationService.getById(id);
    }

    @PostMapping("save")
    public Result save(@RequestBody StationDTO stationDTO) {
        return stationService.save(stationDTO);
    }

    @PutMapping
    public void update(StationDTO stationDTO) {
        stationService.update(stationDTO);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        stationService.delete(id);
    }
}
