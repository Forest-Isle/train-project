package com.senvu.train.business.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.pojo.dto.query.QueryDto;

public interface BusinessService {
    PageResult query(QueryDto queryDto);

    Result all();

    String imageUrl(String imageCodeToken);
}
