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
 *RelationshipNotDeletedExceptionResponse is the response that wraps a RelationshipNotDeletedException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipNotDeletedExceptionResponse extends SubjectAreaOMASAPIResponse
{

    protected String    guid = null;
    public RelationshipNotDeletedExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.RelationshipNotDeletedException);
        guid = ((GuidOrientatedException)e).getGuid();
    }

    @Override
    public String toString()
    {
        return "RelationshipNotDeletedExceptionResponse{" +
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
