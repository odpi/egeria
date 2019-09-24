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
import org.springframework.web.client.ResourceAccessException;
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

    @ExceptionHandler(value = {ResourceAccessException.class})
    protected ResponseEntity<Object> handleResourceException(ResourceAccessException ex, WebRequest request) {
        String bodyOfResponse = "Unable to access resource.";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
