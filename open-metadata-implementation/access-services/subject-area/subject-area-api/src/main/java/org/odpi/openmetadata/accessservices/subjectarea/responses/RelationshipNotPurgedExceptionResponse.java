/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.GuidOrientatedException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipNotPurgedExceptionResponse is the response that wraps a RelationshipNotPurgedException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipNotPurgedExceptionResponse extends SubjectAreaOMASAPIResponse
{

    protected String    guid = null;
    public RelationshipNotPurgedExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.RelationshipNotPurgedException);
        guid = ((GuidOrientatedException)e).getGuid();
    }

    @Override
    public String toString()
    {
        return "RelationshipNotPurgedExceptionResponse{" +
                super.toString() + ", guid = " + guid + '}';
    }
    /**
     * Return the guid - the unique identifier of the entity that was not deleted
     *
     * @return String message id
     */
    public String getGuid() {
        return guid;
    }
    /**
     * Set up the guid - the unique identifier of the entity that was not deleted
     *
     * @param guid String guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }
}
