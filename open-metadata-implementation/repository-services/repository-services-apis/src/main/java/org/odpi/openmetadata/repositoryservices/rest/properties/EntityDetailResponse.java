/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EntityDetailResponse describes the response structure for an OMRS REST API that returns an EntityDetail object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityDetailResponse extends OMRSRESTAPIResponse
{
    private EntityDetail entity = null;

    /**
     * Default constructor
     */
    public EntityDetailResponse()
    {
    }


    /**
     * Return the resulting entity object.
     *
     * @return entity object
     */
    public EntityDetail getEntity()
    {
        return entity;
    }


    /**
     * Set up the resulting entity object.
     *
     * @param entity - entity object
     */
    public void setEntity(EntityDetail entity)
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
