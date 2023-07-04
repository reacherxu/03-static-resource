package com.richard.demo.utils.powermock;

import java.text.MessageFormat;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SAP Inc. Copyright (c) 1972-2021 All Rights Reserved.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SRServiceRegistryService {

    private final SRRestTemplateService SRRestTemplateService;
    static String REGISTRY_SERVICE_ID =  "api/svc/services/{0}/metadatas/{1}";

    public ServiceDetailDto getServiceById(String id, String metadataId, String token) {
        log.info("[getServiceById] Get service by id {} and metadataId {}", id, metadataId);
        HttpHeaders headers = SRRestTemplateService.buildHttpHeaders(null, token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(MessageFormat.format(REGISTRY_SERVICE_ID, id, metadataId));
        try {
            return SRRestTemplateService.request(builder.toUriString(), HttpMethod.GET, entity, ServiceDetailDto.class);
        } catch (Exception e) {
            log.error("An error occurred while getting Service by id {} metadataId, {}", id, metadataId, e);
            throw new RuntimeException( "An error occurred while getting Service", e);
        }
    }
}
