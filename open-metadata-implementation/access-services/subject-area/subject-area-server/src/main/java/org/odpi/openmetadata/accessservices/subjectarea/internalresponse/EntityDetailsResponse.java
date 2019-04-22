/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EntityDetailsResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs EntityDetail
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityDetailsResponse extends SubjectAreaOMASAPIResponse
{
    private List<EntityDetail> entityDetails = null;

    /**
     * Default constructor
     */
    public EntityDetailsResponse() {
        this.setResponseCategory(ResponseCategory.OmrsEntityDetails);
    }
    public EntityDetailsResponse(List<EntityDetail> entityDetails)
    {
        this();
        this.entityDetails = entityDetails;
    }


    /**
     * Return the EntityDetail object.
     *
     * @return entityDetails
     */
    public List<EntityDetail> getEntityDetails()
    {
        return entityDetails;
    }

    /**
     * Set up the EntityDetail object.
     *
     * @param entityDetails - entityDetails object
     */
    public void setEntityDetails(List<EntityDetail> entityDetails)
    {
        this.entityDetails = entityDetails;
    }


    @Override
    public String toString()
    {
        return "EntityDetailResponse{" +
                "entityDetails=" + entityDetails +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
