/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EntityDetailResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs EntityDetail
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityDetailResponse extends SubjectAreaOMASAPIResponse
{
    private EntityDetail entityDetail = null;

    /**
     * Default constructor
     */
    public EntityDetailResponse() {
        this.setResponseCategory(ResponseCategory.OmrsEntityDetail);
    }
    public EntityDetailResponse(EntityDetail entityDetail)
    {
        this();
        this.entityDetail = entityDetail;
    }


    /**
     * Return the EntityDetail object.
     *
     * @return entityDetail
     */
    public EntityDetail getEntityDetail()
    {
        return entityDetail;
    }

    /**
     * Set up the EntityDetail object.
     *
     * @param entityDetail - entityDetail object
     */
    public void setEntityDetail(EntityDetail entityDetail)
    {
        this.entityDetail = entityDetail;
    }


    @Override
    public String toString()
    {
        return "EntityDetailResponse{" +
                "entityDetail=" + entityDetail +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
