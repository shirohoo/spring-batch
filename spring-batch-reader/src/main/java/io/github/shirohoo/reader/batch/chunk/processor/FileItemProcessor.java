package io.github.shirohoo.reader.batch.chunk.processor;

import io.github.shirohoo.reader.batch.domain.Product;
import io.github.shirohoo.reader.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO item) throws Exception {
        return item.entity();
    }

}
