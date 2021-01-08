/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.api.model.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityOfficerOMASAPIResponse provides a common header for Security Officer OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes
        ({
                @JsonSubTypes.Type(value = SecurityOfficerSchemaElementResponse.class, name = "SecurityOfficerSchemaElementResponse"),
                @JsonSubTypes.Type(value = SecurityOfficerSecurityTagResponse.class, name = "SecurityOfficerSecurityTagResponse")
        })
public abstract class SecurityOfficerOMASAPIResponse extends FFDCResponseBase {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public SecurityOfficerOMASAPIResponse() { }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityOfficerOMASAPIResponse(SecurityOfficerOMASAPIResponse template) { super(template); }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "SecurityOfficerOMASAPIResponse{" +
                       "exceptionClassName='" + getExceptionClassName() + '\'' +
                       ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                       ", actionDescription='" + getActionDescription() + '\'' +
                       ", relatedHTTPCode=" + getRelatedHTTPCode() +
                       ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                       ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                       ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                       ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                       ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                       ", exceptionProperties=" + getExceptionProperties() +
                       '}';
    }
}