package io.github.shirohoo.flatbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class FlatBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlatBatchApplication.class, args);
    }

}
