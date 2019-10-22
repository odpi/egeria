/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {MalformedInputException.class})
    protected ResponseEntity<Object> handleMalformedInput(MalformedInputException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        String bodyOfResponse = "Received response does not have the expected format.";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse, httpStatus);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), httpStatus, request);
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
        LOG.error(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String bodyOfResponse = "Invalid request to asset catalog.";
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse, httpStatus);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), httpStatus, request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {RestClientException.class})
    protected ResponseEntity<Object> handleResourceException(RestClientException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        String bodyOfResponse = "Unable to access resource.";
        HttpStatus httpStatus =  HttpStatus.SERVICE_UNAVAILABLE;
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, bodyOfResponse, httpStatus);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), httpStatus, request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {UserNotAuthorizedException.class})
    protected ResponseEntity<Object> handleUnauthorizedException(UserNotAuthorizedException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, "Unauthorized user.", httpStatus);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), httpStatus, request);
    }

}
