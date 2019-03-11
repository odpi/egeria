/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

public class RestCaller {

    /**
     *
     * @return headers for Atlas
     */
    private static HttpHeaders getHttpHeaders() {
        String base64Creds = getBase64CredsString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    /**
     *
     * @return credentials strig
     */
    private static String getBase64CredsString() {
        byte[] plainCredsBytes = "admin:atlas@INGisC0ol".getBytes();
        //byte[] plainCredsBytes = "admin:admin".getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        return new String(base64CredsBytes);
    }

    public static <T> T getForObject(String url, Class<T> clazz, Object... params){
        return restCall(url, HttpMethod.GET, null, clazz, params);
    }

    public static <T> T postForObject(String url, Object body , Class<T> clazz, Object... params){
        return restCall(url, HttpMethod.POST, body, clazz, params);
    }

    private static <T> T restCall(String url, HttpMethod method, Object body,  Class<T> clazz, Object... params){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request;
        if(body!=null){
            request = new HttpEntity(body, getHttpHeaders());
        }else{
            request = new HttpEntity<>(getHttpHeaders());
        }

        ResponseEntity<T> result =
                restTemplate.exchange(url, method, request, clazz, params);
        return result.getBody();
    }
}
