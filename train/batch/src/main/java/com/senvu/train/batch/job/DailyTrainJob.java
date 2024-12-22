package com.senvu.train.batch.job;

import com.senvu.client.BusinessFeignClient;
import com.senvu.pojo.result.Result;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DailyTrainJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainJob.class);


    private final BusinessFeignClient businessFeignClient;

    public DailyTrainJob (BusinessFeignClient businessFeignClient){
        this.businessFeignClient = businessFeignClient;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("生成15日之后的车次任务开始");
        LocalDate date = LocalDate.now();
        date = date.plusDays(15);
        Result result = businessFeignClient.genTrain(date);
        log.info("生成15日之后的车次任务结束,执行结果：{}",result.getCode());
    }
}
