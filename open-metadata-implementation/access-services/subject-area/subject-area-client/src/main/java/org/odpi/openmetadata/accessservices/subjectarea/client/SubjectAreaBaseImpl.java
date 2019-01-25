/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
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
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName,propertyName+"=");
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
        }
    }

}
