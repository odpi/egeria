/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.BadRequestException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.CategoryNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.GlossaryNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.LineageNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.OpenLineageServiceException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.TermNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.BadPropertyException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.BadTypeException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.EntityNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.RexInvalidParameterException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.RexSearchException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.RexSubGraphNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.UnsupportedFunctionException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex.UsernameNotAuthorizedException;
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
    protected ResponseEntity<Object> handleAssetCatalogException(OCFCheckedExceptionBase ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        UserInterfaceErrorCodes errorCode = UserInterfaceErrorCodes.INVALID_REQUEST_FOR_ASSET_CATALOG;
        if (ex.getReportedHTTPCode() == HttpStatus.NOT_FOUND.value()) {
            errorCode = UserInterfaceErrorCodes.ENTITY_NOT_FOUND;
        }
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, errorCode);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), errorCode.getHttpErrorCode(), request);
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

    @ExceptionHandler(value = {LineageNotFoundException.class})
    protected ResponseEntity<Object> handleLineageNotFoundException(LineageNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.LINEAGE_NOT_FOUND);
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), UserInterfaceErrorCodes.LINEAGE_NOT_FOUND.getHttpErrorCode(), request);
    }


    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, ex.getErrorCode());
        return handleExceptionInternal(ex, errorAttributes,
                new HttpHeaders(), ex.getErrorCode().getHttpErrorCode(), request);
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

    @ExceptionHandler(value = {GlossaryNotFoundException.class})
    protected ResponseEntity<Object> handleGlossaryViewNotFoundRequestException(GlossaryNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.GLOSSARY_NOT_FOUND);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.GLOSSARY_NOT_FOUND.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {TermNotFoundException.class})
    protected ResponseEntity<Object> handleGlossaryViewNotFoundRequestException(TermNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.TERM_NOT_FOUND);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.TERM_NOT_FOUND.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {CategoryNotFoundException.class})
    protected ResponseEntity<Object> handleGlossaryViewNotFoundRequestException(CategoryNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.CATEGORY_NOT_FOUND);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.CATEGORY_NOT_FOUND.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {UsernameNotAuthorizedException.class})
    protected ResponseEntity<Object> handleUsernameNotAuthorizedException(UsernameNotAuthorizedException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {RexInvalidParameterException.class})
    protected ResponseEntity<Object> handleRexInvalidParameterException(RexInvalidParameterException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.REX_INVALID_PARAMETER_REQUEST);
        errorAttributes.put("message", ex.getMessage());
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.REX_INVALID_PARAMETER_REQUEST.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.ENTITY_NOT_FOUND);

        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.ENTITY_NOT_FOUND.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {UnsupportedFunctionException.class})
    protected ResponseEntity<Object> handleUnsupportedFunctionException(UnsupportedFunctionException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.OPERATION_NOT_SUPPORTED);

        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.OPERATION_NOT_SUPPORTED.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {BadTypeException.class})
    protected ResponseEntity<Object> handleBadTypeException(BadTypeException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.BAD_TYPE_INFORMATION);

        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.BAD_TYPE_INFORMATION.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {BadPropertyException.class})
    protected ResponseEntity<Object> handleBadPropertyException(BadPropertyException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.BAD_PROPERTY_INFORMATION);

        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.BAD_PROPERTY_INFORMATION.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {RexSubGraphNotFoundException.class})
    protected ResponseEntity<Object> handleRexSubGraphNotFoundException(RexSubGraphNotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request, UserInterfaceErrorCodes.REX_SUBGRAPH_NOT_FOUND);

        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.REX_SUBGRAPH_NOT_FOUND.getHttpErrorCode(), request);
    }

    @ExceptionHandler(value = {RexSearchException.class})
    protected ResponseEntity<Object> handleRexSearchException(RexSearchException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        Map<String, Object> errorAttributes = this.errorAttributes.getRexSearchErrorAttributes(request, UserInterfaceErrorCodes.INVALID_SEARCH_REQUEST, ex);
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(),
                UserInterfaceErrorCodes.INVALID_SEARCH_REQUEST.getHttpErrorCode(), request);
    }
}
