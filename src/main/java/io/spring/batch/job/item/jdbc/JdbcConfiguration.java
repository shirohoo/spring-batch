package io.spring.batch.job.item.jdbc;

import io.spring.batch.job.model.Member;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static java.lang.Integer.parseInt;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcConfiguration {
    private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "jdbcJob";
    private static final String STEP_ONE = "jdbcStep";

    private static final String JDBC_CURSOR_ITEM_READER = "jdbcCursorItemReader";
    private static final String SELECT_QUERY = "select id, name, age, address from member";
    private static final String INSERT_QUERY = "insert into member(name, age, address) values(:name, :age, :address)";

    @Bean
    public Job jdbcJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(jdbcStep(null))
                                .build();
    }

    @Bean
    @JobScope
    public Step jdbcStep(@Value("#{jobParameters[chunkSize]}") String value) throws Exception {
        return stepBuilderFactory.get(STEP_ONE)
                                 .<Member, Member>chunk(getChunkSize(value))
                                 .reader(this.jdbcCursorItemReader())
                                 .writer(this.jdbcBatchItemWriter())
                                 .build();
    }

    /**
     * Batch-Application 실행 시 실행 옵션으롤 chunk size 지정
     * 입력 된 실행옵션이 없으면 size=10으로 지정
     * -chunkSize=20 --job.name=taskletProcessing
     */
    private int getChunkSize(String value) {
        return StringUtils.isNotEmpty(value) ? parseInt(value) : 10;
    }

    private JdbcCursorItemReader<Member> jdbcCursorItemReader() throws Exception {
        JdbcCursorItemReader<Member> itemReader = new JdbcCursorItemReaderBuilder<Member>()
                .name(JDBC_CURSOR_ITEM_READER)
                .dataSource(dataSource)
                .sql(SELECT_QUERY)
                .rowMapper((rs, rowNum)->new Member(rs.getInt(1),
                                                    rs.getString(2),
                                                    rs.getInt(3),
                                                    rs.getString(4))).build();

        itemReader.afterPropertiesSet();
        return itemReader;
    }

    private ItemWriter<Member> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Member> itemWriter = new JdbcBatchItemWriterBuilder<Member>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_QUERY)
                .build();

        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
