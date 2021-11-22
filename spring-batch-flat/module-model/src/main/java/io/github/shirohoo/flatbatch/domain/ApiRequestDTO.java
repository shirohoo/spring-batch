package io.github.shirohoo.flatbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestDTO {

    private long id;
    private ProductDTO productDTO;

}
