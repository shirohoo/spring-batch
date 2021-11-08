package io.github.shirohoo.batch.job.fulltext;

import java.io.File;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileReadJobConfiguration {

    public static final String TEST_FILE_PATH = "src/test/resources/TestSpecFile";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TestSpecRepository testSpecRepository;

    public FileReadJobConfiguration(final JobBuilderFactory jobBuilderFactory, final StepBuilderFactory stepBuilderFactory, final TestSpecRepository testSpecRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.testSpecRepository = testSpecRepository;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("testSpecFileJob")
            .start(step())
            .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("testSpecFileStep")
            .tasklet((contribution, chunkContext) -> {
                TestSpec testSpec = TestSpecFileParser.parse(new File(TEST_FILE_PATH));
                testSpecRepository.save(testSpec);
                return RepeatStatus.FINISHED;
            })
            .build();
    }

}
