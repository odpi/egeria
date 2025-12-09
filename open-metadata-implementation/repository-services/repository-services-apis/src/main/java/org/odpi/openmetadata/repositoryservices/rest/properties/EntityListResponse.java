/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityListResponse support an OMRS REST API response that returns a list of EntityDetail objects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityListResponse extends OMRSAPIPagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<EntityDetail> entities = null;


    /**
     * Default constructor
     */
    public EntityListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EntityListResponse(EntityListResponse template)
    {
        super(template);

        if (template != null)
        {
            entities = template.getEntities();
        }
    }


    /**
     * Return the list of entities.
     *
     * @return entity list
     */
    public List<EntityDetail> getEntities()
    {
        if (entities == null)
        {
            return null;
        }
        else if (entities.isEmpty())
        {
            return null;
        }
        else
        {
            List<EntityDetail>  clonedEntities = new ArrayList<>();

            for (EntityDetail  entity : entities)
            {
                clonedEntities.add(new EntityDetail(entity));
            }

            return clonedEntities;
        }
    }


    /**
     * Set up the list of entities.
     *
     * @param entities entity list
     */
    public void setEntities(List<EntityDetail> entities)
    {
        this.entities = entities;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EntityListResponse{" +
                "entities=" + entities +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof EntityListResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EntityListResponse
                that = (EntityListResponse) objectToCompare;
        return Objects.equals(getEntities(), that.getEntities());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getEntities());
    }
}
