/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntitySummaryResponse describes the response structure for an OMRS REST API that returns an EntitySummary object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntitySummaryResponse extends OMRSRESTAPIResponse
{
    private EntitySummary entity = null;

    /**
     * Default constructor
     */
    public EntitySummaryResponse()
    {
    }


    /**
     * Return the resulting entity object.
     *
     * @return entity object
     */
    public EntitySummary getEntity()
    {
        return entity;
    }


    /**
     * Set up the resulting entity object.
     *
     * @param entity entity object
     */
    public void setEntity(EntitySummary entity)
    {
        this.entity = entity;
    }


    @Override
    public String toString()
    {
        return "EntityDetailResponse{" +
                "entity=" + entity +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
