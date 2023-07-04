package com.richard.demo.utils.powermock;

import javax.print.attribute.standard.Destination;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * SAP Inc. Copyright (c) 1972-2021 All Rights Reserved.
 */
@Service
@Slf4j
public class SRRestTemplateService {

    private RestTemplate restTemplate;

    private RestTemplate restTemplateForProxy;


    public <T> T request(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        try {
            return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
        } catch (RestClientException e) {
            log.error("request definition failed", e);
            throw e;
        }
    }

    public HttpHeaders buildHttpHeaders(Destination destination, String userToken) {
        HttpHeaders headers = new HttpHeaders();

        return headers;
    }

}
