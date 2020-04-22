/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.OpenLineageServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.MALFORMED_INPUT_EXCEPTION);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), UserInterfaceErrorCodes.MALFORMED_INPUT_EXCEPTION.getHttpErrorCode(), request);
    }

    /**
     * 
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {InvalidParameterException.class, PropertyServerException.class})
    protected ResponseEntity<Object> handleAssetCatalogException(AssetCatalogException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.INVALID_REQUEST_FOR_ASSET_CATALOG);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), UserInterfaceErrorCodes.INVALID_REQUEST_FOR_ASSET_CATALOG.getHttpErrorCode(), request);
    }
    /**
     *
     * @param ex - raised exception to be handled
     * @param request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {OpenLineageServiceException.class})
    protected ResponseEntity<Object> handleOpenLineageClientException(OpenLineageServiceException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.INVALID_REQUEST_FOR_OPEN_LINEAGE);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), UserInterfaceErrorCodes.INVALID_REQUEST_FOR_OPEN_LINEAGE.getHttpErrorCode(), request);
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
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.RESOURCE_NOT_FOUND);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), UserInterfaceErrorCodes.RESOURCE_NOT_FOUND.getHttpErrorCode(), request);
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
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.USER_NOT_AUTHORIZED);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), UserInterfaceErrorCodes.USER_NOT_AUTHORIZED.getHttpErrorCode(), request);
    }


    @ExceptionHandler(value = {GlossaryViewOmasException.class})
    protected ResponseEntity<Object> handleGlossaryViewOmasException(GlossaryViewOmasException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.INVALID_REQUEST_FOR_GLOSSARY_VIEW);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.INVALID_REQUEST_FOR_GLOSSARY_VIEW.getHttpErrorCode(), request);
    }

}
