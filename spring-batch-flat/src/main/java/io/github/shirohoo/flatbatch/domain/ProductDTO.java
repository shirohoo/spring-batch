package io.github.shirohoo.flatbatch.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private int price;
    private String type;

    public Product entity() {
        return Product.builder()
            .id(id)
            .name(name)
            .price(price)
            .type(type)
            .build();
    }

}
