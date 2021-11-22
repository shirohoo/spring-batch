package io.github.shirohoo.flatbatch.classifier;


import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

public class WriterClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemWriter<ApiRequestDTO>> writerMap = new HashMap<>();

    public void setWriterMap(Map<String, ItemWriter<ApiRequestDTO>> writerMap) {
        this.writerMap = writerMap;
    }

    @Override
    public T classify(C classifiable) {
        return (T) writerMap.get(((ApiRequestDTO) classifiable).getProductDTO().getType());
    }

}
