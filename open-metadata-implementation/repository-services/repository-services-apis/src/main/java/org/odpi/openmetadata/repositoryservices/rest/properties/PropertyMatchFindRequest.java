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
 *
 * * @param exactMatchClassificationProperties optional list of entity properties that must match exactly.
 *      * @param exactMatchCriteria Enum defining how the exact match properties should be matched to the classifications in the repository.
 *      * @param fuzzyMatchClassificationProperties Optional list of entity properties to match (contains wildcards).
 *      * @param matchCriteria Enum defining how the fuzzy match properties should be matched to the classifications in the repository.
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
    private InstanceProperties exactMatchProperties = null;
    private MatchCriteria      exactMatchCriteria   = null;
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
            this.exactMatchCriteria = template.getExactMatchCriteria();
            this.exactMatchProperties = template.getExactMatchProperties();
            this.matchCriteria = template.getFuzzyMatchCriteria();
            this.matchProperties = template.getFuzzyMatchProperties();
        }
    }


    /**
     * Return the collection of exact match properties for this find request.
     *
     * @return instance properties object
     */
    public InstanceProperties getExactMatchProperties()
    {
        return exactMatchProperties;
    }


    /**
     * Set up the collection of exact match properties for this find request.
     *
     * @param matchProperties instance properties object
     */
    public void setExactMatchProperties(InstanceProperties matchProperties)
    {
        this.exactMatchProperties = matchProperties;
    }


    /**
     * Return the criteria for how the exact properties should match - ie all, any, none.
     *
     * @return match criteria enum
     */
    public MatchCriteria getExactMatchCriteria()
    {
        return exactMatchCriteria;
    }


    /**
     * Set up the criteria for how the exact properties should match - ie all, any, nome.
     *
     * @param matchCriteria match criteria enum
     */
    public void setExactMatchCriteria(MatchCriteria matchCriteria)
    {
        this.exactMatchCriteria = matchCriteria;
    }


    /**
     * Return the collection of fuzzy match properties for this find request.
     *
     * @return instance properties object
     */
    public InstanceProperties getFuzzyMatchProperties()
    {
        return matchProperties;
    }


    /**
     * Set up the collection of fuzzy match properties for this find request.
     *
     * @param matchProperties instance properties object
     */
    public void setFuzzyMatchProperties(InstanceProperties matchProperties)
    {
        this.matchProperties = matchProperties;
    }


    /**
     * Return the criteria for how the fuzzy properties should match - ie all, any, none.
     *
     * @return match criteria enum
     */
    public MatchCriteria getFuzzyMatchCriteria()
    {
        return matchCriteria;
    }


    /**
     * Set up the criteria for how the fuzzy properties should match - ie all, any, nome.
     *
     * @param matchCriteria match criteria enum
     */
    public void setFuzzyMatchCriteria(MatchCriteria matchCriteria)
    {
        this.matchCriteria = matchCriteria;
    }


    /**
     * Return the collection of fuzzy match properties for this find request.
     *
     * @return instance properties object
     */
    @Deprecated
    public InstanceProperties getMatchProperties()
    {
        return matchProperties;
    }


    /**
     * Set up the collection of fuzzy match properties for this find request.
     *
     * @param matchProperties instance properties object
     */
    @Deprecated
    public void setMatchProperties(InstanceProperties matchProperties)
    {
        this.matchProperties = matchProperties;
    }


    /**
     * Return the criteria for how the fuzzy properties should match - ie all, any, none.
     *
     * @return match criteria enum
     */
    @Deprecated
    public MatchCriteria getMatchCriteria()
    {
        return matchCriteria;
    }


    /**
     * Set up the criteria for how the fuzzy properties should match - ie all, any, nome.
     *
     * @param matchCriteria match criteria enum
     */
    @Deprecated
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
                "exactMatchProperties=" + exactMatchProperties +
                ", exactMatchCriteria=" + exactMatchCriteria +
                ", matchProperties=" + matchProperties +
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
        PropertyMatchFindRequest that = (PropertyMatchFindRequest) objectToCompare;
        return Objects.equals(getExactMatchProperties(), that.getExactMatchProperties()) &&
                getExactMatchCriteria() == that.getExactMatchCriteria() &&
                Objects.equals(getFuzzyMatchProperties(), that.getFuzzyMatchProperties()) &&
                getFuzzyMatchCriteria() == that.getFuzzyMatchCriteria();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getExactMatchProperties(), getExactMatchCriteria(),
                            getFuzzyMatchProperties(),
                            getFuzzyMatchCriteria());
    }
}
