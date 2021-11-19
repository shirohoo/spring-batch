package io.github.shirohoo.flatbatch.service;

import io.github.shirohoo.flatbatch.domain.ApiInfo;
import io.github.shirohoo.flatbatch.domain.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService3 extends AbstractApiService {

    @Override
    protected ApiResponseDTO doService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8081/api/products/3", apiInfo, String.class);
        return ApiResponseDTO.builder()
            .status(responseEntity.getStatusCodeValue())
            .message(responseEntity.getBody())
            .build();
    }

}
