package com.senvu.train.batch.job;

import com.senvu.client.BusinessFeignClient;
import com.senvu.pojo.result.Result;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DailyTrainStationJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainStationJob.class);

    private final BusinessFeignClient businessFeignClient;

    public DailyTrainStationJob(BusinessFeignClient businessFeignClient){
        this.businessFeignClient = businessFeignClient;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("生成每日车站数据开始");
        Result result = businessFeignClient.genDailyTrainStation(LocalDate.now().plusDays(15));
        log.info("生成每日车站数据结束，结果为:{}", result.getCode());
    }
}
