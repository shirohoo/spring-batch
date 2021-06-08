package io.batch.springbatch.job.custom;

import io.batch.springbatch.job.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ItemReaderConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job itemReaderJob() {
        return jobBuilderFactory.get("itemReaderJob")
                                .incrementer(new RunIdIncrementer())
                                .start(customItemReaderStep())
                                .build();
    }
    
    @Bean
    public Step customItemReaderStep() {
        return stepBuilderFactory.get("customItemReaderStep")
                                 .<Member, Member>chunk(10)
                                 .reader(new CustomItemReader<>(getItems()))
                                 .writer(itemWriter())
                                 .build();
    }
    
    private List<Member> getItems() {
        List<Member> members = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            members.add(new Member(i + 1, "name" + i, 29, "address"));
        }
        return members;
    }
    
    /**
     * <pre>
     *     [예상 출력 패턴]
     *
     *     name0, name1, name2, name3, name4, name5, ...
     *     name10, name11, name12, name13, name14, name15, ...
     *     name20, name21, name22, name23, name24, name25, ...
     *     name30, name31, name32, name33, name34, name35, ...
     *     name40, name41, name42, name43, name44, name45, ...
     *     name50, name51, name52, name53, name54, name55, ...
     *     name60, name61, name62, name63, name64, name65, ...
     *     name70, name71, name72, name73, name74, name75, ...
     *     name80, name81, name82, name83, name84, name85, ...
     *     name90, name91, name92, name93, name94, name95, ...
     * </pre>
     */
    private ItemWriter<Member> itemWriter() {
        return items->log.info(items.stream()
                                    .map(Member::getName)
                                    .collect(joining(", ")));
    }
}
