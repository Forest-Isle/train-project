package com.senvu.train.business.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.pojo.dto.ConfirmOrderDTO;
import com.senvu.train.business.pojo.dto.query.ConfirmOrderQueryDTO;

public interface ConfirmOrderService {
    PageResult list(ConfirmOrderQueryDTO confirmOrderQueryDTO);
    Result getById(Long id);
    Result save(ConfirmOrderDTO confirmOrderDTO);
    Result update(ConfirmOrderDTO confirmOrderDTO);
    Result delete(Long id);

    Result doConfirm(ConfirmOrderDTO confirmOrderDTO);

    Result queryCount(Long confirmOrderId);
}
