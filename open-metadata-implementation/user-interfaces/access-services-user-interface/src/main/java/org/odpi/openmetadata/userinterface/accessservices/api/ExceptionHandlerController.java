/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Autowired
    private CustomErrorAttributes errorAttributes;

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {MalformedInputException.class})
    protected ResponseEntity<Object> handleMalformedInput(MalformedInputException ex, WebRequest request) {
        String bodyOfResponse = "Received response does not have the expected format.";
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {InvalidParameterException.class, PropertyServerException.class,
            InvalidParameterException.class})
    protected ResponseEntity<Object> handleAssetCatalogException(AssetCatalogException ex, WebRequest request) {
        String bodyOfResponse = "Invalid request to asset catalog.";
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {RestClientException.class})
    protected ResponseEntity<Object> handleResourceException(RestClientException ex, WebRequest request) {
        String bodyOfResponse = "Unable to access resource.";
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {UserNotAuthorizedException.class})
    protected ResponseEntity<Object> handleUnauthorizedException(UserNotAuthorizedException ex, WebRequest request) {
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, "Unauthorized user.");
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}
