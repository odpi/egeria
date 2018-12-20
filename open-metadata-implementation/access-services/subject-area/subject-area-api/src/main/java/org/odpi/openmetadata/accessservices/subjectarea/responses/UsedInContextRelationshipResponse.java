/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.UsedInContext;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UsedInContextResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * UsedInContext object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsedInContextRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private UsedInContext usedInContext= null;

    /**
     * Default constructor
     */
    public UsedInContextRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermUsedInContextRelationship);
    }
    public UsedInContextRelationshipResponse(UsedInContext usedInContext)
    {
        this.usedInContext=usedInContext;
        this.setResponseCategory(ResponseCategory.TermUsedInContextRelationship);
    }


    /**
     * Return the UsedInContext object.
     *
     * @return usedInContextResponse
     */
    public UsedInContext getUsedInContext()
    {
        return usedInContext;
    }

    public void setUsedInContext(UsedInContext usedInContext)
    {
        this.usedInContext = usedInContext;
    }


    @Override
    public String toString()
    {
        return "UsedInContextResponse{" +
                "usedInContext=" + usedInContext +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
