/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PropertyMatchFindRequest adds match properties and the match criteria to a find request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PropertyMatchHistoricalFindRequest.class, name = "PropertyMatchHistoricalFindRequest"),
                @JsonSubTypes.Type(value = EntityPropertyFindRequest.class, name = "EntityPropertyFindRequest")
        })
public class PropertyMatchFindRequest extends TypeLimitedFindRequest
{
    private static final long    serialVersionUID = 1L;

    private InstanceProperties matchProperties      = null;
    private MatchCriteria      matchCriteria        = null;



    /**
     * Default constructor
     */
    public PropertyMatchFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public PropertyMatchFindRequest(PropertyMatchFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.matchCriteria = template.getMatchCriteria();
            this.matchProperties = template.getMatchProperties();
        }
    }




    /**
     * Return the collection of match properties for this find request.
     *
     * @return instance properties object
     */
    public InstanceProperties getMatchProperties()
    {
        return matchProperties;
    }


    /**
     * Set up the collection of match properties for this find request.
     *
     * @param matchProperties instance properties object
     */
    public void setMatchProperties(InstanceProperties matchProperties)
    {
        this.matchProperties = matchProperties;
    }


    /**
     * Return the criteria for how the properties should match - ie all, any, none.
     *
     * @return match criteria enum
     */
    public MatchCriteria getMatchCriteria()
    {
        return matchCriteria;
    }


    /**
     * Set up the criteria for how the properties should match - ie all, any, nome.
     *
     * @param matchCriteria match criteria enum
     */
    public void setMatchCriteria(MatchCriteria matchCriteria)
    {
        this.matchCriteria = matchCriteria;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PropertyMatchFindRequest{" +
                "matchProperties=" + matchProperties +
                ", matchCriteria=" + matchCriteria +
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
        PropertyMatchFindRequest
                that = (PropertyMatchFindRequest) objectToCompare;
        return Objects.equals(getMatchProperties(), that.getMatchProperties()) &&
                getMatchCriteria() == that.getMatchCriteria();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMatchProperties(), getMatchCriteria(), getMatchProperties(), getMatchCriteria());
    }
}
