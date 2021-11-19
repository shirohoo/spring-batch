package io.github.shirohoo.flatbatch.chunk.processor;

import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ProductDTO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor1 implements ItemProcessor<ProductDTO, ApiRequestDTO> {

    @Override
    public ApiRequestDTO process(ProductDTO item) throws Exception {
        return ApiRequestDTO.builder()
            .id(item.getId())
            .productDTO(item)
            .build();
    }

}
