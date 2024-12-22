package com.senvu.train.business.controller;

import com.senvu.result.PageResult;
import com.senvu.train.business.pojo.dto.ConfirmOrderDTO;
import com.senvu.train.business.pojo.dto.query.ConfirmOrderQueryDTO;
import com.senvu.train.business.service.ConfirmOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.senvu.result.Result;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Autowired
    private ConfirmOrderService confirmOrderService;

    @GetMapping("/query")
    public PageResult list(ConfirmOrderQueryDTO confirmOrderQueryDTO) {
        return confirmOrderService.list(confirmOrderQueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return confirmOrderService.getById(id);
    }

    @PostMapping
    public Result save(@RequestBody ConfirmOrderDTO confirmOrderDTO) {
         return confirmOrderService.save(confirmOrderDTO);
    }

    @PutMapping
    public Result update(@RequestBody ConfirmOrderDTO confirmOrderDTO) {
        return confirmOrderService.update(confirmOrderDTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return  confirmOrderService.delete(id);
    }

    @PostMapping("do")
    @Transactional
    public Result doConfirm(@RequestBody ConfirmOrderDTO confirmOrderDTO) {
        return confirmOrderService.doConfirm(confirmOrderDTO);
    }

    @GetMapping("query-line-count/{confirmOrderId}")
    public Result queryCount(@PathVariable String confirmOrderId) {
        return confirmOrderService.queryCount(Long.valueOf(confirmOrderId));
    }
}
