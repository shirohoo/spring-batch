package io.github.shirohoo.flatbatch.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

    private String url;
    private List<? extends ApiRequestDTO> apiRequestList;

}
