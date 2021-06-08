package io.batch.springbatch.job.item.jpa;

import io.batch.springbatch.job.custom.CustomItemReader;
import io.batch.springbatch.job.model.Person;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaConfiguration {
    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    private static final String JOB_NAME = "jpaJob";
    private static final String STEP_ONE = "jpaStep";
    
    private static final String JPA_CURSOR_ITEM_READER = "jpaCursorItemReader";
    private static final String SELECT_QUERY = "select p from Person p";
    
    @Bean
    public Job jpaJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
                                .incrementer(new RunIdIncrementer())
                                .start(jpaStep(null))
                                .build();
    }
    
    @Bean
    @JobScope
    public Step jpaStep(@Value("#{jobParameters[chunkSize]}") String value) throws Exception {
        return stepBuilderFactory.get(STEP_ONE)
                                 .<Person, Person>chunk(getChunkSize(value))
                                 //                                 .reader(this.jpaCursorItemReader())
                                 .reader(this.customItemReader(getItems()))
                                 .processor(this.itemProcessor())
                                 .writer(this.jpaItemWriter())
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
    
    private List<Person> getItems() {
        List<Person> list = new ArrayList<>();
        for(int i = 1; i <= 100; i++) {
            list.add(new Person(null, "name" + i, i, "address" + i));
        }
        return list;
    }
    
    /**
     * 비영속 상태의 ItemReader
     */
    private ItemReader<Person> customItemReader(List<Person> list) {
        return new CustomItemReader(list);
    }
    
    /**
     * 영속, 준영속 상태의 ItemReader
     */
    private JpaCursorItemReader<Person> jpaCursorItemReader() throws Exception {
        JpaCursorItemReader<Person> itemReader = new JpaCursorItemReaderBuilder<Person>()
                .name(JPA_CURSOR_ITEM_READER)
                .entityManagerFactory(entityManagerFactory)
                .queryString(SELECT_QUERY)
                .build();
        
        itemReader.afterPropertiesSet();
        return itemReader;
    }
    
    private ItemProcessor<? super Person, ? extends Person> itemProcessor() {
        return item->item.getAge() % 2 == 0 ? item : null;
    }
    
    private ItemWriter<Person> jpaItemWriter() throws Exception {
        JpaItemWriter<Person> itemWriter = new JpaItemWriterBuilder<Person>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true) // EntityManager가 merge가 아닌 persist를 사용하게 강제함.
                .build();
        
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }
}
