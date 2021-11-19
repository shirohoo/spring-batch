package io.github.shirohoo.flatbatch.api;

import io.github.shirohoo.flatbatch.domain.ApiInfo;
import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ProductDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController {

    @PostMapping("/api/products/1")
    public String product1(@RequestBody ApiInfo apiInfo) {
        List<ProductDTO> productList = apiInfo.getApiRequestList().stream()
            .map(ApiRequestDTO::getProductDTO)
            .collect(Collectors.toUnmodifiableList());
        log.info("server1={}", productList);
        return "/api/products/1 was successfully processed";
    }

    @PostMapping("/api/products/2")
    public String product2(@RequestBody ApiInfo apiInfo) {
        List<ProductDTO> productList = apiInfo.getApiRequestList().stream()
            .map(ApiRequestDTO::getProductDTO)
            .collect(Collectors.toUnmodifiableList());
        log.info("server2={}", productList);
        return "/api/products/2 was successfully processed";
    }

    @PostMapping("/api/products/3")
    public String product3(@RequestBody ApiInfo apiInfo) {
        List<ProductDTO> productList = apiInfo.getApiRequestList().stream()
            .map(ApiRequestDTO::getProductDTO)
            .collect(Collectors.toUnmodifiableList());
        log.info("server3={}", productList);
        return "/api/products/3 was successfully processed";
    }

}
