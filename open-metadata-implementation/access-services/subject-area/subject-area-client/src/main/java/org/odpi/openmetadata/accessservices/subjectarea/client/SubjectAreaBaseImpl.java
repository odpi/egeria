/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utils.DetectUtils;
import org.odpi.openmetadata.accessservices.subjectarea.utils.QueryUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides a glossary authoring interface for subject area experts.
 */
public class SubjectAreaBaseImpl extends FFDCRESTClient {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaBaseImpl.class);

    private static final String className = SubjectAreaBaseImpl.class.getName();

    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    protected SubjectAreaBaseImpl(String serverName, String serverPlatformURLRoot) throws
                                                                                   org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId                user id for the HTTP request
     * @param password              password for the HTTP request
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException there is a problem creating the client-side components to issue any
     *                                                                                    REST API calls.
     */
    protected SubjectAreaBaseImpl(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    protected void encodeQueryProperty(String propertyName, String propertyValue, String methodName, StringBuffer queryStringSB) throws InvalidParameterException {
        try {
            QueryUtils.encodeQueryParam(propertyName, propertyValue, queryStringSB);
        } catch (UnsupportedEncodingException e) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.ERROR_ENCODING_QUERY_PARAMETER;
            ExceptionMessageDefinition messageDefinition = errorCode.getMessageDefinition();
            messageDefinition.setMessageParameters(propertyName,propertyValue);
            throw new InvalidParameterException(
                    messageDefinition,
                    className,
                    methodName,
                    propertyName,
                    propertyValue);
        }
    }

    protected List<Line> getRelationships(String base_url,
                                          String userId,
                                          String guid,
                                          Date asOfTime,
                                          int offset,
                                          int pageSize,
                                          SequencingOrder sequencingOrder,
                                          String sequencingProperty) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            FunctionNotSupportedException,
                                                                            UnexpectedResponseException,
                                                                            PropertyServerException {
        final String methodName = "getRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils.addCharacterToQuery(queryStringSB);
        queryStringSB.append("sequencingOrder=" + sequencingOrder);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime=" + asOfTime);
        }
        if (offset != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("offset=" + offset);
        }
        if (pageSize != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("pageSize=" + pageSize);
        }

        if (sequencingProperty != null) {
            // encode the string
            encodeQueryProperty("sequencingProperty", sequencingProperty, methodName, queryStringSB);
        }
        String urlTemplate = base_url + "/%s/relationships";

        if (queryStringSB.length() > 0) {
            urlTemplate = urlTemplate + queryStringSB.toString();
        }

        SubjectAreaOMASAPIResponse response = getByIdRESTCall(userId, guid, methodName, urlTemplate);
        List<Line> relationships = DetectUtils.detectAndReturnLines(className, methodName, response);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId);
        }
        return relationships;
    }

    protected SubjectAreaOMASAPIResponse postRESTCall(String userId, String methodName, String urlTemplate, Object requestBody) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId);
            response = callPostRESTCall(methodName,
                                        SubjectAreaOMASAPIResponse.class,
                                        expandedURL,
                                        requestBody);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction()
            );
            throw new PropertyServerException(messageDefinition, className, methodName);

        }
        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }

    protected SubjectAreaOMASAPIResponse putRESTCall(String userId, String guid, boolean isReplace, String methodName, String urlTemplate, Object requestBody) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid, isReplace);
            response = callPutRESTCall(methodName,
                                       SubjectAreaOMASAPIResponse.class,
                                       expandedURL,
                                       requestBody);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction()
            );
            throw new PropertyServerException(messageDefinition, className, methodName);

        }
        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }


    protected SubjectAreaOMASAPIResponse getByIdRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
            response = callGetRESTCall(methodName,
                                       SubjectAreaOMASAPIResponse.class,
                                       expandedURL);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction()
            );
            throw new PropertyServerException(messageDefinition, className, methodName);

        }
        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }

    protected SubjectAreaOMASAPIResponse findRESTCall(String userId,
                                                      String methodName,
                                                      String urlTemplate,
                                                      String searchCriteria,
                                                      Date asOfTime,
                                                      int offset,
                                                      int pageSize,
                                                      org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                      String sequencingProperty) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        if (sequencingOrder == null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils.addCharacterToQuery(queryStringSB);
        queryStringSB.append("sequencingOrder=" + sequencingOrder);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime=" + asOfTime);
        }
        if (searchCriteria != null) {
            // encode the string

            encodeQueryProperty("searchCriteria", searchCriteria, methodName, queryStringSB);
        }
        if (offset != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("offset=" + offset);
        }
        if (pageSize != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("pageSize=" + pageSize);
        }
        if (sequencingProperty != null) {
            // encode the string
            encodeQueryProperty("sequencingProperty", sequencingProperty, methodName, queryStringSB);
        }
        if (queryStringSB.length() > 0) {
            urlTemplate = urlTemplate + queryStringSB.toString();
        }
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId);
            response = callGetRESTCall(methodName,
                                       SubjectAreaOMASAPIResponse.class,
                                       expandedURL);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction()
            );
            throw new PropertyServerException(messageDefinition, className, methodName);

        }
        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }

    protected SubjectAreaOMASAPIResponse deleteEntityRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, EntityNotDeletedException {
        SubjectAreaOMASAPIResponse response = deleteRESTCall(userId, guid, methodName, urlTemplate);
        DetectUtils.detectAndThrowEntityNotDeletedException(response);
        return response;
    }



    protected SubjectAreaOMASAPIResponse purgeEntityRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, EntityNotPurgedException {
        SubjectAreaOMASAPIResponse response = deleteRESTCall(userId, guid, methodName, urlTemplate);
        DetectUtils.detectAndThrowEntityNotPurgedException(response);
        return response;
    }

    private SubjectAreaOMASAPIResponse deleteRESTCall(String userId, String guid, String methodName, String urlTemplate) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
            response = callDeleteRESTCall(methodName,
                                          SubjectAreaOMASAPIResponse.class,
                                          expandedURL);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction());
            throw new PropertyServerException(messageDefinition, className, methodName);
        }

        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }

    protected SubjectAreaOMASAPIResponse deleteRelationshipRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, RelationshipNotDeletedException {

        SubjectAreaOMASAPIResponse response = deleteRESTCall(userId, guid, methodName, urlTemplate);
        DetectUtils.detectAndThrowRelationshipNotDeletedException(response);
        return response;
    }



    protected SubjectAreaOMASAPIResponse purgeRelationshipRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException, RelationshipNotPurgedException {

        SubjectAreaOMASAPIResponse response = deleteRESTCall(userId, guid, methodName, urlTemplate);
        DetectUtils.detectAndThrowRelationshipNotPurgedException(response);
        return response;
    }

    protected SubjectAreaOMASAPIResponse restoreRESTCall(String userId, String guid, String methodName, String urlTemplate) throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException, FunctionNotSupportedException {
        SubjectAreaOMASAPIResponse response;
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        try {
            String expandedURL = String.format(serverPlatformURLRoot + urlTemplate, serverName, userId, guid);
            response = callPostRESTCall(methodName,
                                        SubjectAreaOMASAPIResponse.class,
                                        expandedURL,
                                       null);

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(e.getReportedHTTPCode(),
                                                                                          e.getReportedErrorMessageId(),
                                                                                          e.getMessage(),
                                                                                          e.getReportedSystemAction(),
                                                                                          e.getReportedUserAction());
            throw new PropertyServerException(messageDefinition, className, methodName);
        }

        DetectUtils.detectAndThrowUserNotAuthorizedException(response);
        DetectUtils.detectAndThrowInvalidParameterException(response);
        DetectUtils.detectAndThrowFunctionNotSupportedException(response);
        return response;
    }


}
