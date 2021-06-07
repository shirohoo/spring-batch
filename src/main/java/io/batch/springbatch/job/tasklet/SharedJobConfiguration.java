package io.batch.springbatch.job.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SharedJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    /**
     * <pre>
     * 각 Step끼리는 데이터 공유가 되지 않으므로
     * emptyStepKey이 출력되는게 정상
     * StepExecution은 Step하나에 종속적이며,
     * JobExecution은 Job전체에서 공유할 수 있다
     *
     * JobExecution = 전역
     * StepExecution = 지역
     * </pre>
     */
    @Bean
    public Job shareJob() {
        return jobBuilderFactory.get("shareJob")
                                .incrementer(new RunIdIncrementer())
                                .start(this.shareStep1())
                                .next(this.shareStep2())
                                .build();
    }
    
    @Bean
    public Step shareStep1() {
        return stepBuilderFactory.get("shareStep1")
                                 .tasklet((contribution, chunkContext)->{
                                     StepExecution stepExecution = contribution.getStepExecution();
            
                                     ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
                                     stepExecutionContext.putString("stepKey", "step execution context");
            
                                     JobExecution jobExecution = stepExecution.getJobExecution();
                                     ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
                                     jobExecutionContext.putString("jobKey", "job execution context");
            
                                     JobParameters jobParameters = jobExecution.getJobParameters();
                                     JobInstance jobInstance = jobExecution.getJobInstance();
            
                                     log.info(">>>>>>>>>> shareStep1\njobName: {}\nstepName: {}\nparameter:{}",
                                              jobInstance.getJobName(),
                                              stepExecution.getStepName(),
                                              jobParameters.getLong("run.id")
                                             );
            
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
    
    @Bean
    public Step shareStep2() {
        return stepBuilderFactory.get("shareStep2")
                                 .tasklet((contribution, chunkContext)->{
                                     StepExecution stepExecution = contribution.getStepExecution();
            
                                     ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
            
                                     JobExecution jobExecution = stepExecution.getJobExecution();
                                     ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
            
                                     log.info(">>>>>>>>>> shareStep2\njobKey: {}\nstepKey: {}",
                                              jobExecutionContext.getString("jobKey", "emptyJobKey"),
                                              stepExecutionContext.getString("stepKey", "emptyStepKey")
                                             );
            
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }
}
