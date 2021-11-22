package io.github.shirohoo.flatbatch.classifier;


import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ProductDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemProcessor<ProductDTO, ApiRequestDTO>> processorMap = new HashMap<>();

    public void setProcessorMap(Map<String, ItemProcessor<ProductDTO, ApiRequestDTO>> processorMap) {
        this.processorMap = processorMap;
    }

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get(((ProductDTO) classifiable).getType());
    }

}
