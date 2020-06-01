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
import org.odpi.openmetadata.accessservices.subjectarea.utils.RestCaller;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides term term authoring interface for subject area experts.
 */
public class SubjectAreaBaseImpl
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaBaseImpl.class);

    private static final String className = SubjectAreaBaseImpl.class.getName();

    /*
     * The URL of the server where OMAS is active
     */
    protected String                    omasServerURL = null;
    /*
     * serverName is a name that picks out a specific named running instance on the server.
     */
    protected String serverName;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL unique id for the connector instance
     */
    public SubjectAreaBaseImpl(String omasServerURL, String serverName)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
        this.serverName = serverName;
    }

    protected void encodeQueryProperty(String propertyName, String propertyValue, String methodName, StringBuffer queryStringSB) throws InvalidParameterException {
        try {
            QueryUtils.encodeQueryParam(propertyName,propertyValue, queryStringSB);
        } catch (UnsupportedEncodingException e) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.ERROR_ENCODING_QUERY_PARAMETER;

            throw new InvalidParameterException(
                    errorCode.getMessageDefinition(),
                    className,
                    methodName,
                    propertyName,
                    propertyValue);
        }
    }
    protected List<Line> getRelationships(String base_url, String userId, String guid, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws InvalidParameterException, MetadataServerUncontactableException, UserNotAuthorizedException, FunctionNotSupportedException, UnexpectedResponseException {
        final String methodName = "getRelationships";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
        final String urlTemplate = this.omasServerURL +base_url + "/%s/relationships";
        String url = String.format(urlTemplate, serverName, userId, guid);
        if (sequencingOrder==null) {
            sequencingOrder = SequencingOrder.ANY;
        }
        StringBuffer queryStringSB = new StringBuffer();
        QueryUtils. addCharacterToQuery(queryStringSB);
        queryStringSB.append("sequencingOrder="+ sequencingOrder);
        if (asOfTime != null) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("asOfTime="+ asOfTime);
        }
        if (offset != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("offset="+ offset);
        }
        if (pageSize != 0) {
            QueryUtils.addCharacterToQuery(queryStringSB);
            queryStringSB.append("pageSize="+ pageSize);
        }

        if (sequencingProperty !=null) {
            // encode the string
            encodeQueryProperty("sequencingProperty",sequencingProperty, methodName, queryStringSB);
        }
        if (queryStringSB.length() >0) {
            url = url + queryStringSB.toString();
        }
        SubjectAreaOMASAPIResponse restResponse = RestCaller.issueGet(className,methodName,url);
        DetectUtils.detectAndThrowUserNotAuthorizedException(restResponse);
        DetectUtils.detectAndThrowInvalidParameterException(restResponse);
        DetectUtils.detectAndThrowFunctionNotSupportedException(restResponse);
        List<Line> relationships = DetectUtils.detectAndReturnLines(className, methodName, restResponse);
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId );
        }
        return relationships;
    }
}
