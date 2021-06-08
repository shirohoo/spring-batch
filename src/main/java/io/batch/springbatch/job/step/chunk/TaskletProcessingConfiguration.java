package io.batch.springbatch.job.step.chunk;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TaskletProcessingConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    /**
     * Batch items
     */
    private List<String> getItems() {
        List<String> items = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            items.add("item" + i);
        }
        return items;
    }
    
    @Bean
    public Job taskletProcessingJob() {
        return jobBuilderFactory.get("taskletProcessing")
                                .incrementer(new RunIdIncrementer())
                                .start(this.taskletBaseStep())
                                .build();
    }
    
    @Bean
    public Step taskletBaseStep() {
        return stepBuilderFactory.get("taskBaseStep")
                                 .tasklet(this.tasklet(null))
                                 .build();
    }
    
    /**
     * <pre>
     * {@literal @}Scope <-- 어떤 시점에 Bean을 생성/소멸 시킬지 생명주기를 설정
     *
     * {@literal @}JobScope <--Job 실행 시점에 Bean 생성/소멸, Step에 설정 가능
     * {@literal @}StepScope <-- Step 실행 시점에 Bean 생성/소멸
     *     Tasklet, Chunk(itemReader, ItemProcessor, ItemWriter) 등의
     *     Step 하위 메서드에 선언
     *
     * {@literal @}Scope("job") == @JobScope
     * {@literal @}Scope("step") == @StepScope
     *
     *  실제로 애노테이션을 타고 들어가면 하기와 같이 선언돼있다.
     * {@literal @}Scope(value = "job", proxyMode = ScopedProxyMode.TARGET_CLASS)
     * {@literal @}Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
     *
     * {@literal @}Value("#{jobParameters[chunkSize]}") String chunkSize
     *  를 사용하기 위해서는 Scope 사용이 필수적
     * </pre>
     */
    @Bean
    @StepScope
    public Tasklet tasklet(@Value("#{jobParameters[chunkSize]}") String value) {
        List<String> items = this.getItems();
        return (contribution, chunkContext)->{
            StepExecution stepExecution = contribution.getStepExecution();
            
            int chunkSize = this.getChunkSize(value);
            int fromIndex = stepExecution.getReadCount();
            int toIndex = fromIndex + chunkSize;
            
            if(fromIndex >= items.size()) {
                return RepeatStatus.FINISHED;
            }
            
            List<String> subList = items.subList(fromIndex, toIndex);
            log.info("task item size: {}", subList.size());
            
            stepExecution.setReadCount(toIndex);
            return RepeatStatus.CONTINUABLE;
        };
    }
    
    /**
     * Batch-Application 실행 시 실행 옵션으롤 chunk size 지정
     * 입력 된 실행옵션이 없으면 size=10으로 지정
     * -chunkSize=20 --job.name=taskletProcessing
     */
    private int getChunkSize(String value) {
        return StringUtils.isNotEmpty(value) ? parseInt(value) : 10;
    }
}
