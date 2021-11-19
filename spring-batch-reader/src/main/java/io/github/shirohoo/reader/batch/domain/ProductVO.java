package io.github.shirohoo.reader.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {

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
