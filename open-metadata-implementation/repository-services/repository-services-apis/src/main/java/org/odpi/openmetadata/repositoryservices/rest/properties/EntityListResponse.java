/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityListResponse support an OMRS REST API response that returns a list of EntityDetail objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityListResponse extends OMRSRESTAPIPagedResponse
{
    private List<EntityDetail> entities = null;


    /**
     * Default constructor
     */
    public EntityListResponse()
    {
    }


    /**
     * Return the list of entities.
     *
     * @return entity list
     */
    public List<EntityDetail> getEntities()
    {
        return entities;
    }


    /**
     * Set up the list of entities.
     *
     * @param entities entity list
     */
    public void setEntities(List<EntityDetail> entities)
    {
        this.entities = new ArrayList<>(entities);
    }


    @Override
    public String toString()
    {
        return "EntityListResponse{" +
                "entities=" + entities +
                ", nextPageURL='" + nextPageURL + '\'' +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
