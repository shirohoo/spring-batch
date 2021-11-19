package io.github.shirohoo.flatbatch.job.api;

import io.github.shirohoo.flatbatch.domain.ProductDTO;
import io.github.shirohoo.flatbatch.rowmapper.ProductRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryGenerator {

    public static ProductDTO[] getProductList(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("select type from product group by type", new ProductRowMapper() {
                @Override
                public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return ProductDTO.builder()
                        .type(rs.getString("type"))
                        .build();
                }
            })
            .toArray(new ProductDTO[]{});
    }

    public static Map<String, Object> getParameterForQuery(String parameter, String value) {
        return Map.of(parameter, value);
    }

}
