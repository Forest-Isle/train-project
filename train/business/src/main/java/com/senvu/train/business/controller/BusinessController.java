package com.senvu.train.business.controller;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.pojo.dto.query.QueryDto;
import com.senvu.train.business.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("daily-train-ticket/query-list")
    public PageResult query(QueryDto queryDto){
        queryDto.setStart(URLDecoder.decode(String.valueOf(queryDto.getStart()), StandardCharsets.UTF_8));
        queryDto.setEnd(URLDecoder.decode(queryDto.getEnd(), StandardCharsets.UTF_8));
        return businessService.query(queryDto);
    }

    @GetMapping("station/query-all")
    public Result all(){
        return businessService.all();
    }

    @GetMapping("kaptcha/image-code/{imageCodeToken}")
    public String imageUrl(@PathVariable String imageCodeToken){
        return businessService.imageUrl(imageCodeToken);
    }
}
