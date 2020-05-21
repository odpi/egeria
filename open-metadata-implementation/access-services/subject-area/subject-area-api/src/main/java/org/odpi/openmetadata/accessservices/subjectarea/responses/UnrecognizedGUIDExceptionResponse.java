/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.GuidOrientatedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UnrecognizedGUIDExceptionResponse is the response that wraps a UnrecognizedGUIDException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UnrecognizedGUIDExceptionResponse extends SubjectAreaOMASAPIResponse {
    protected String    guid = null;
    public UnrecognizedGUIDExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.UnrecognizedGUIDException);
        guid = ((GuidOrientatedException)e).getGuid();
    }

    @Override
    public String toString()
    {
        return "UnrecognizedGUIDExceptionResponse{" +
                super.toString() + ", guid = " + guid + '}';
    }
    /**
     * Return the guid - the unrecognised unique identifier of the entity
     *
     * @return String message id
     */
    public String getGuid() {
        return guid;
    }
    /**
     * Set up the guid - the unrecognised unique identifier of the entity
     *
     * @param guid String guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }
}