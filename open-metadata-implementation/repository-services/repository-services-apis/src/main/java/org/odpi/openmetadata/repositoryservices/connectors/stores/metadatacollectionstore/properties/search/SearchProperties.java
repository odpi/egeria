/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The SearchProperties class provides support for searching against one or more properties using a variety of
 * comparison mechanisms, including searching against the same property using different comparison mechanisms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchProperties implements Serializable
{

    private static final long    serialVersionUID = 1L;

    private List<PropertyCondition> conditions;
    private MatchCriteria matchCriteria;

    /**
     * Typical constructor
     */
    public SearchProperties()
    {
        super();
        // Setup defaults
        matchCriteria = MatchCriteria.ALL;
    }

    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public SearchProperties(SearchProperties templateProperties)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.matchCriteria = templateProperties.getMatchCriteria();
            this.conditions = new ArrayList<>();
            this.conditions.addAll(templateProperties.getConditions());
        }
    }

    /**
     * Retrieve the conditions against which to match.
     *
     * @return {@code List<SearchCondition>}
     */
    public List<PropertyCondition> getConditions()
    {
        return conditions;
    }

    /**
     * Set the conditions against which to match.
     *
     * @param conditions against which to match
     */
    public void setConditions(List<PropertyCondition> conditions)
    {
        this.conditions = conditions;
    }

    /**
     * Retrieve the criteria against which to match the conditions.
     *
     * @return MatchCriteria
     */
    public MatchCriteria getMatchCriteria()
    {
        return matchCriteria;
    }

    /**
     * Set the criteria against which to match the conditions.
     *
     * @param matchCriteria against which to match the conditions
     */
    public void setMatchCriteria(MatchCriteria matchCriteria)
    {
        this.matchCriteria = matchCriteria;
    }

    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "SearchProperties{" +
                "matchCriteria=" + matchCriteria +
                ", conditions=" + conditions +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof SearchProperties))
        {
            return false;
        }
        SearchProperties that = (SearchProperties) objectToCompare;
        return getMatchCriteria() == that.getMatchCriteria() &&
                Objects.equals(getConditions(), that.getConditions());
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getMatchCriteria(), getConditions());
    }

}
