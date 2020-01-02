/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = RelatedEntitiesHistoricalFindRequest.class, name = "RelatedEntitiesHistoricalFindRequest")
        })
public class RelatedEntitiesFindRequest extends OMRSAPIPagedFindRequest
{
    private static final long    serialVersionUID = 1L;

    private List<String>              entityTypeGUIDs = null;
    private List<String>              limitResultsByClassification = null;


    /**
     * Default constructor
     */
    public RelatedEntitiesFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RelatedEntitiesFindRequest(RelatedEntitiesFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.entityTypeGUIDs = getEntityTypeGUIDs();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedEntitiesFindRequest{" +
                "entityTypeGUIDs=" + entityTypeGUIDs +
                ", limitResultsByClassification=" + limitResultsByClassification +
                ", sequencingProperty='" + getSequencingProperty() + '\'' +
                ", sequencingOrder=" + getSequencingOrder() +
                ", offset=" + getOffset() +
                ", pageSize=" + getPageSize() +
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
        if (!(objectToCompare instanceof RelatedEntitiesFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelatedEntitiesFindRequest
                that = (RelatedEntitiesFindRequest) objectToCompare;
        return Objects.equals(getEntityTypeGUIDs(), that.getEntityTypeGUIDs()) &&
                Objects.equals(getLimitResultsByClassification(), that.getLimitResultsByClassification());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getEntityTypeGUIDs(), getLimitResultsByClassification());
    }
}
