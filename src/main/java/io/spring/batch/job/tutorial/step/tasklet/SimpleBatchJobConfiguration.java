package io.spring.batch.job.tutorial.step.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Spring-Batch
 * Job과 Step으로 나뉜다
 * 아래 작업의 도표
 * https://t1.daumcdn.net/cfile/tistory/99E8E3425B66BA2713
 * 출처 : https://jojoldu.tistory.com/
 * </pre>
 */
@Slf4j
@Configuration // Spring-Batch Job은 이 애노테이션으로 사용한다
@RequiredArgsConstructor
public class SimpleBatchJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // job과 관련된 객체
    private final StepBuilderFactory stepBuilderFactory; // step과 관련된 객체

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob") // simpleJob이라는 이름으로 job을 생성
                                .incrementer(new RunIdIncrementer()) // job id를 증가시켜 매번 다른 job instance가 생성되게 함
                                .start(this.simpleStep()) // simpleStep1을 시작
                                .build();
    }

    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("simpleStep") // simpleStep1이라는 이름으로 step을 생성
                                 .tasklet(this.tasklet())
                                 .build();
    }

    private Tasklet tasklet() {
        return (contribution, chunkContext)->{ // step안에서 수행될 기능들. tasklet은 단일로 수행될 커스텀한 기능을 선언할 때 사용
            log.info(">>>>>> This is Step1"); // batch-job이 실행되면 가장먼저 실행될 step은 log를 기록한다
            return RepeatStatus.FINISHED;
        };
    }
}
