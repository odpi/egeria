/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * EntityNeighborhoodFindRequest adds the ability to restrict a neighborhood find request to only
 * traversing specific relationship types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = EntityNeighborhoodHistoricalFindRequest.class, name = "EntityNeighborhoodHistoricalFindRequest")
        })
public class EntityNeighborhoodFindRequest extends OMRSAPIFindRequest
{
    private static final long    serialVersionUID = 1L;

    private List<String>    entityTypeGUIDs = null;
    private List<String>    limitResultsByClassification = null;
    private List<String>    relationshipTypeGUIDs = null;


    /**
     * Default constructor
     */
    public EntityNeighborhoodFindRequest()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public EntityNeighborhoodFindRequest(EntityNeighborhoodFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.relationshipTypeGUIDs = template.getRelationshipTypeGUIDs();
        }
    }


    /**
     * Return the list of entity types that should restrict the find request.
     *
     * @return list of guids
     */
    public List<String> getEntityTypeGUIDs()
    {
        if (entityTypeGUIDs == null)
        {
            return null;
        }
        else if (entityTypeGUIDs.isEmpty())
        {
            return null;
        }
        else
        {
            return entityTypeGUIDs;
        }
    }


    /**
     * Set up the list of entity types that should restrict the find request.
     *
     * @param entityTypeGUIDs list of guids
     */
    public void setEntityTypeGUIDs(List<String> entityTypeGUIDs)
    {
        this.entityTypeGUIDs = entityTypeGUIDs;
    }


    /**
     * Return the list of entity classifications that should restrict the find request.
     *
     * @return list of classification names
     */
    public List<String> getLimitResultsByClassification()
    {
        if (limitResultsByClassification == null)
        {
            return null;
        }
        else if (limitResultsByClassification.isEmpty())
        {
            return null;
        }
        else
        {
            return limitResultsByClassification;
        }
    }


    /**
     * Set up the list of entity classifications that should restrict the find request.
     *
     * @param limitResultsByClassification list of classification names
     */
    public void setLimitResultsByClassification(List<String> limitResultsByClassification)
    {
        this.limitResultsByClassification = limitResultsByClassification;
    }


    /**
     * Return the list of relationship type guids that limit the find request.
     *
     * @return list of guids
     */
    public List<String> getRelationshipTypeGUIDs()
    {
        if (relationshipTypeGUIDs == null)
        {
            return null;
        }
        else if (relationshipTypeGUIDs.isEmpty())
        {
            return null;
        }
        else
        {
            return relationshipTypeGUIDs;
        }
    }


    /**
     * Set up the list of relationship type guids that limit the find request.
     *
     * @param relationshipTypeGUIDs list of guids
     */
    public void setRelationshipTypeGUIDs(List<String> relationshipTypeGUIDs)
    {
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EntityNeighborhoodFindRequest{" +
                "entityTypeGUIDs=" + entityTypeGUIDs +
                ", limitResultsByClassification=" + limitResultsByClassification +
                ", relationshipTypeGUIDs=" + relationshipTypeGUIDs +
                ", limitResultsByStatus=" + getLimitResultsByStatus() +
                '}';
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
        if (!(objectToCompare instanceof EntityNeighborhoodFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EntityNeighborhoodFindRequest
                that = (EntityNeighborhoodFindRequest) objectToCompare;
        return Objects.equals(getEntityTypeGUIDs(), that.getEntityTypeGUIDs()) &&
                Objects.equals(getLimitResultsByClassification(), that.getLimitResultsByClassification()) &&
                Objects.equals(getRelationshipTypeGUIDs(), that.getRelationshipTypeGUIDs());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(),
                            getEntityTypeGUIDs(),
                            getLimitResultsByClassification(),
                            getRelationshipTypeGUIDs());
    }
}
