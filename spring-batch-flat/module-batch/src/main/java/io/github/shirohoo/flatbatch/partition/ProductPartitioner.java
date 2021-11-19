package io.github.shirohoo.flatbatch.partition;

import io.github.shirohoo.flatbatch.domain.ProductDTO;
import io.github.shirohoo.flatbatch.job.api.QueryGenerator;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class ProductPartitioner implements Partitioner {

    private final DataSource dataSource;

    public ProductPartitioner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        ProductDTO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> map = new HashMap<>();

        int number = 0;
        for (int i = 0; i < productList.length; i++) {
            ExecutionContext value = new ExecutionContext();
            map.put("partition" + number, value);
            value.put("product", productList[i]);
            number++;
        }

        return map;
    }

}
