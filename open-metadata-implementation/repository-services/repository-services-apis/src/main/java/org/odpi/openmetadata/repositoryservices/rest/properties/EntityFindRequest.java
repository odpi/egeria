/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityFindRequest restricts a find request to entities with specific classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityFindRequest extends InstanceFindRequest
{
    private static final long    serialVersionUID = 1L;

    private SearchClassifications matchClassifications = null;


    /**
     * Default constructor
     */
    public EntityFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public EntityFindRequest(EntityFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.matchClassifications = new SearchClassifications(template.getMatchClassifications());
        }
    }


    /**
     * Return the list of classification search criteria.
     *
     * @return SearchClassifications
     */
    public SearchClassifications getMatchClassifications()
    {
        return matchClassifications;
    }


    /**
     * Set the list of classification search criteria.
     *
     * @param matchClassifications to set as search criteria
     */
    public void setMatchClassifications(SearchClassifications matchClassifications)
    {
        this.matchClassifications = matchClassifications;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EntityFindRequest{" +
                "matchClassifications=" + matchClassifications +
                ", matchProperties=" + getMatchProperties() +
                ", typeGUID='" + getTypeGUID() + '\'' +
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
        if (!(objectToCompare instanceof EntityFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EntityFindRequest that = (EntityFindRequest) objectToCompare;
        return Objects.equals(getMatchClassifications(), that.getMatchClassifications());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMatchClassifications());
    }

}
