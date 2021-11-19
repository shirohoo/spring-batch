package io.github.shirohoo.flatbatch.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private Long id;
    private String name;
    private int price;
    private String type;

}
