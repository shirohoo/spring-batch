package io.github.shirohoo.flatbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // do nothing
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();
        long elapsedTime = endTime - startTime;
        log.info("elapsed time={}", elapsedTime);
    }

}
