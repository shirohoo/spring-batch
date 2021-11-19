package io.github.shirohoo.flatbatch.service;

import io.github.shirohoo.flatbatch.domain.ApiInfo;
import io.github.shirohoo.flatbatch.domain.ApiRequestDTO;
import io.github.shirohoo.flatbatch.domain.ApiResponseDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractApiService {

    public ApiResponseDTO service(List<? extends ApiRequestDTO> apiRequest) {
        RestTemplate restTemplate = buildRestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ApiInfo apiInfo = ApiInfo.builder().apiRequestList(apiRequest).build();

        return doService(restTemplate, apiInfo);
    }

    protected abstract ApiResponseDTO doService(RestTemplate restTemplate, ApiInfo apiInfo);

    private RestTemplate buildRestTemplate() {
        return new RestTemplateBuilder()
            .errorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse response) throws IOException {

                }
            })
            .build();
    }

}
