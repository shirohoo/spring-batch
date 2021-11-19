package io.github.shirohoo.flatbatch.job.api;

import io.github.shirohoo.flatbatch.chunk.processor.ApiItemProcessor1;
import io.github.shirohoo.flatbatch.chunk.processor.ApiItemProcessor2;
import io.github.shirohoo.flatbatch.chunk.processor.ApiItemProcessor3;
import io.github.shirohoo.flatbatch.chunk.writer.ApiItemWriter1;
import io.github.shirohoo.flatbatch.chunk.writer.ApiItemWriter2;
import io.github.shirohoo.flatbatch.chunk.writer.ApiItemWriter3;
import io.github.shirohoo.flatbatch.classifier.ProcessorClassifier;
import io.github.shirohoo.flatbatch.classifier.WriterClassifier;
import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ProductDTO;
import io.github.shirohoo.flatbatch.partition.ProductPartitioner;
import io.github.shirohoo.flatbatch.service.ApiService1;
import io.github.shirohoo.flatbatch.service.ApiService2;
import io.github.shirohoo.flatbatch.service.ApiService3;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    private final int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
            .partitioner(apiSlaveStep().getName(), productPartitioner())
            .step(apiSlaveStep())
            .gridSize(3)
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public ProductPartitioner productPartitioner() {
        return new ProductPartitioner(dataSource);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(6);
        executor.setThreadNamePrefix("api-thread");
        return executor;
    }

    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
            .<ProductDTO, ProductDTO>chunk(chunkSize)
            .reader(itemReader(null))
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<ProductDTO> itemReader(@Value("#{stepExecutionContext['product']}") ProductDTO productDTO) throws Exception {
        JdbcPagingItemReader<ProductDTO> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductDTO.class));

        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();
        queryProvider.setSelectClause("ID, NAME, PRICE, TYPE");
        queryProvider.setFromClause("FROM PRODUCT");
        queryProvider.setWhereClause("WHERE TYPE = :TYPE");
        queryProvider.setSortKeys(Map.of("ID", Order.DESCENDING));

        reader.setParameterValues(QueryGenerator.getParameterForQuery("TYPE", productDTO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductDTO, ApiRequestDTO> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProductDTO, ItemProcessor<?, ? extends ApiRequestDTO>> classifier = new ProcessorClassifier();
        classifier.setProcessorMap(Map.of(
            "1", new ApiItemProcessor1(),
            "2", new ApiItemProcessor2(),
            "3", new ApiItemProcessor3()
        ));
        processor.setClassifier(classifier);
        return processor;
    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestDTO> writer = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestDTO, ItemWriter<? super ApiRequestDTO>> classifier = new WriterClassifier();
        classifier.setWriterMap(Map.of(
            "1", new ApiItemWriter1(apiService1),
            "2", new ApiItemWriter2(apiService2),
            "3", new ApiItemWriter3(apiService3)
        ));
        writer.setClassifier(classifier);
        return writer;
    }

}
