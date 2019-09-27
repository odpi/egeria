/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {MalformedInputException.class})
    protected ResponseEntity<Object> handleMalformedInput(MalformedInputException ex, WebRequest request) {
        String bodyOfResponse = "Received response does not have the expected format.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {InvalidParameterException.class, PropertyServerException.class,
            InvalidParameterException.class})
    protected ResponseEntity<Object> handleAssetCatalogException(AssetCatalogException ex, WebRequest request) {
        String bodyOfResponse = "Invalid request to asset catalog.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {RestClientException.class})
    protected ResponseEntity<Object> handleResourceException(RestClientException ex, WebRequest request) {
        String bodyOfResponse = "{\"timestamp\":\"2019-09-26T17:57:49.107+0000\",\"status\":503," +
                "\"error\":\"Forbidden\",\"message\":\"Unable to access resource\",\"path\":\"/api/users/current\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("status", String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value = {UserNotAuthorizedException.class})
    protected ResponseEntity<Object> handleUnauthorizedException(UserNotAuthorizedException ex, WebRequest request) {
        String bodyOfResponse = "Unable to authorize user.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}
