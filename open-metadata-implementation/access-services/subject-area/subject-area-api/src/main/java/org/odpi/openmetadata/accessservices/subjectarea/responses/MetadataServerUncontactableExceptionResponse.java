/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.SubjectAreaCheckedException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataServerUncontactableExceptionResponseResponse is the response that wraps a MetadataServerUncontactableException
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataServerUncontactableExceptionResponse extends SubjectAreaOMASAPIResponse
{


    /**
     * Default constructor
     * @param e previously caught exception
     */
    public MetadataServerUncontactableExceptionResponse(SubjectAreaCheckedException e)
    {
        super(e);
        this.setResponseCategory(ResponseCategory.MetadataServerUncontactableException);
    }

    @Override
    public String toString()
    {
        return "MetadataServerUncontactableExceptionResponse{" +
                super.toString() + '}';
    }
}