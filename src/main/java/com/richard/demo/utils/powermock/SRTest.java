package com.richard.demo.utils.powermock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/7/9 4:34 PM richard.xu Exp $
 */
@RunWith(MockitoJUnitRunner.class)
public class SRTest {

    @InjectMocks
    private SRServiceRegistryService serviceRegistryService;

    @Mock
    private SRRestTemplateService SRRestTemplateService;

    @Mock
    UriComponentsBuilder mockBuilder;

    MockedStatic<UriComponentsBuilder> builder;

    @Before
    public void setup() {
        builder = mockStatic(UriComponentsBuilder.class);
        builder.when(() -> UriComponentsBuilder.fromHttpUrl(anyString())).thenReturn(mockBuilder);
    }

    @After
    public void after() {
        this.builder.close();
    }

    @Test
    public void testMock() {

        Mockito.when(mockBuilder.toUriString()).thenReturn("https://localhost:8080");
        ServiceDetailDto s = new ServiceDetailDto();
        HttpHeaders headers = new HttpHeaders();
        Mockito.when(SRRestTemplateService.buildHttpHeaders(null, "token")).thenReturn(headers);
        Mockito.when(SRRestTemplateService.request(Mockito.anyString(), Mockito.isA(HttpMethod.class), Mockito.isA(HttpEntity.class),
                Mockito.eq(ServiceDetailDto.class))).thenReturn(s);
        serviceRegistryService.getServiceById("srvId", "meatadataId", "token");

    }
}
