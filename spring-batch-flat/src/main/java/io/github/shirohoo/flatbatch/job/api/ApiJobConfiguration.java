package io.github.shirohoo.flatbatch.job.api;

import io.github.shirohoo.flatbatch.listener.JobListener;
import io.github.shirohoo.flatbatch.tasklet.ApiEndTasklet;
import io.github.shirohoo.flatbatch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step jobStep;

    @Bean
    public Job apiJob() {
        return jobBuilderFactory.get("apiJob")
            .incrementer(new RunIdIncrementer())
            .listener(new JobListener())
            .start(apiStep1())
            .next(jobStep)
            .next(apiStep2())
            .build();
    }

    @Bean
    public Step apiStep1() {
        return stepBuilderFactory.get("apiStep1")
            .tasklet(apiStartTasklet)
            .build();
    }

    @Bean
    public Step apiStep2() {
        return stepBuilderFactory.get("apiStep2")
            .tasklet(apiEndTasklet)
            .build();
    }

}
