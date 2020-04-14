/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceFindRequest adds match properties to a find request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = EntityFindRequest.class, name = "EntityFindRequest")
        })
public class InstanceFindRequest extends SubtypeLimitedFindRequest
{
    private static final long    serialVersionUID = 1L;

    private SearchProperties      matchProperties      = null;

    /**
     * Default constructor
     */
    public InstanceFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public InstanceFindRequest(InstanceFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.matchProperties = template.getMatchProperties();
        }
    }


    /**
     * Return the collection of match properties for this find request.
     *
     * @return SearchProperties
     */
    public SearchProperties getMatchProperties()
    {
        return matchProperties;
    }


    /**
     * Set up the collection of match properties for this find request.
     *
     * @param matchProperties to set as search criteria
     */
    public void setMatchProperties(SearchProperties matchProperties)
    {
        this.matchProperties = matchProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InstanceFindRequest{" +
                "matchProperties=" + matchProperties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        InstanceFindRequest that = (InstanceFindRequest) objectToCompare;
        return Objects.equals(getMatchProperties(), that.getMatchProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMatchProperties());
    }
}
