package io.github.shirohoo.flatbatch.chunk.processor;

import io.github.shirohoo.flatbatch.domain.Product;
import io.github.shirohoo.flatbatch.domain.ProductDTO;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductDTO, Product> {

    @Override
    public Product process(ProductDTO item) throws Exception {
        return item.entity();
    }

}
