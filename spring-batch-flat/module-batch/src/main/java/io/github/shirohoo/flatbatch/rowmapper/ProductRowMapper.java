package io.github.shirohoo.flatbatch.rowmapper;

import io.github.shirohoo.flatbatch.domain.ProductDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ProductRowMapper implements RowMapper<ProductDTO> {

    @Override
    public ProductDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductDTO.builder()
            .id(rs.getLong("ID"))
            .name(rs.getString("NAME"))
            .price(rs.getInt("PRICE"))
            .type(rs.getString("TYPE"))
            .build();
    }

}
