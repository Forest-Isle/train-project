package com.senvu.train.business.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.Enum.BusinessExceptionEnum;
import com.senvu.exception.BusinessException;
import com.senvu.result.PageResult;
import com.senvu.result.Result;
import com.senvu.train.business.admin.mapper.StationMapper;
import com.senvu.train.business.admin.pojo.dto.StationDTO;
import com.senvu.train.business.admin.pojo.dto.query.StationQueryDTO;
import com.senvu.train.business.admin.pojo.entity.Station;
import com.senvu.train.business.admin.pojo.vo.StationVO;
import com.senvu.train.business.admin.service.StationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;

    public StationServiceImpl(StationMapper stationMapper) {
        this.stationMapper = stationMapper;
    }

    @Override
    public PageResult list(StationQueryDTO stationQueryDTO) {
        Integer page = stationQueryDTO.getPage();
        Integer size = stationQueryDTO.getSize();
        Page<Station> pages = new Page<>(page,size);
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Station::getName);
        stationMapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Station getById(Long id) {
        return stationMapper.selectById(id);
    }

    @Override
    public Result save(StationDTO stationDTO) {
        Station station = new Station();
        BeanUtils.copyProperties(stationDTO,station);
        if (stationDTO.getId() != null){
            stationMapper.updateById(station);
        } else{
            if (isExisted(stationDTO)){
                throw new BusinessException(BusinessExceptionEnum.STATION_EXITED_ERROR);
            }
            stationMapper.insert(station);
        }
        Result result = new Result();
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    private Boolean isExisted(StationDTO stationDTO) {
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Station::getName, stationDTO.getName());
        List<Station> stations = stationMapper.selectList(wrapper);
        return !stations.isEmpty();
    }

    @Override
    public void update(StationDTO stationDTO) {
//        stationMapper.updateById();
    }

    @Override
    public void delete(Long id) {
        stationMapper.deleteById(id);
    }

    @Override
    public Result all() {
        List<Station> stations = stationMapper.selectList(null);
        List<StationVO> list = stations.stream().map(station -> {
            StationVO stationVO = new StationVO();
            BeanUtils.copyProperties(station, stationVO);
            return stationVO;
        }).collect(Collectors.toList());
        Result result = new Result();
        result.setData(list);
        return result;
    }
}
