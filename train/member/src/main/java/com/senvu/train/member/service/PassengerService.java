package com.senvu.train.member.service;

import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.member.pojo.dto.PassengerQueryDTO;
import com.senvu.train.member.pojo.dto.PassengerSaveDTO;
import jakarta.validation.Valid;

public interface PassengerService {

    Result save(@Valid PassengerSaveDTO passengerSaveDTO);

    PageResult list(@Valid PassengerQueryDTO passengerQueryDTO);

    Result delete(Long id);

    Result queryMine();

}
