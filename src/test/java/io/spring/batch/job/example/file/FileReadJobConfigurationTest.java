package io.spring.batch.job.example.file;

import static org.assertj.core.api.Assertions.assertThat;
import io.spring.batch.config.TestBatchConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
    TestBatchConfiguration.class,
    FileReadJobConfiguration.class,
})
class FileReadJobConfigurationTest {

    private final JobLauncherTestUtils jobLauncherTestUtils;
    private final JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    public FileReadJobConfigurationTest(final JobLauncherTestUtils jobLauncherTestUtils, final JobRepositoryTestUtils jobRepositoryTestUtils) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.jobRepositoryTestUtils = jobRepositoryTestUtils;
    }

    @BeforeEach
    public void clearJobExecutions() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void job() throws Exception {
        // given
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

}
