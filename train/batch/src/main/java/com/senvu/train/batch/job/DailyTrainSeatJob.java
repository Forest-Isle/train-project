package com.senvu.train.batch.job;

import com.senvu.client.BusinessFeignClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DailyTrainSeatJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainSeatJob.class);

    private final BusinessFeignClient businessFeignClient;

    public DailyTrainSeatJob(BusinessFeignClient businessFeignClient) {
        this.businessFeignClient = businessFeignClient;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.info("生成每日座位开始");
            businessFeignClient.genSeat(LocalDate.now().plusDays(15));
            log.info("生成每日座位结束");
    }
}