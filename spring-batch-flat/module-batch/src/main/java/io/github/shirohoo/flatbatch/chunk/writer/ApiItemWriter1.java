package io.github.shirohoo.flatbatch.chunk.writer;

import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ApiResponseDTO;
import io.github.shirohoo.flatbatch.service.AbstractApiService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class ApiItemWriter1 implements ItemWriter<ApiRequestDTO> {

    private final AbstractApiService apiService;

    public ApiItemWriter1(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestDTO> items) throws Exception {
        ApiResponseDTO response = apiService.service(items);
        log.info("response={}", response);
    }

}
