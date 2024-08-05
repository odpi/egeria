/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SearchParameters provides a structure to make the assets' search results more precise.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParameters
{
    /**
     * The maximum number of elements that can be returned on a request.
     */
    private Integer pageSize = 0;

    /**
     * The start from of the result.
     */
    private Integer from = 0;

    /**
     * The number of the relationships out from the starting entity.
     */
    private Integer level = 1;

    /**
     * The name of the property that is to be used to sequence the results
     */
    private String sequencingProperty;

    /**
     * The enum defining how the results should be ordered
     */
    private SequencingOrder sequencingOrder;

    /**
     * The list of classifications that must be present on all returned entities.
     */
    private List<String> limitResultsByClassification;

    /**
     * The list of entity types to search for
     */
    private List<String> entityTypes;

    /**
     * The list of relationship types to include in the query results.
     */
    private List<String> relationshipTypeGUIDs;

    /**
     * The case sensitivity for the search criteria.
     */
    private boolean caseInsensitive = Boolean.TRUE;

    /**
     * The exact match for the search criteria.
     */
    private boolean exactMatch = Boolean.FALSE;


    /**
     * Default constructor;
     */
    public SearchParameters()
    {
    }


    /**
     * Return the maximum page pageSize supported by this server.
     *
     * @return max number of elements that can be returned on a request.
     */
    public Integer getPageSize()
    {
        return pageSize;
    }


    /**
     * Set up the pageSize the result set to only include the specified number of entries.
     *
     * @param pageSize max number of elements that can be returned on a request.
     */
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    /**
     * Return the start from of the result set.
     *
     * @return the start from of result
     */
    public Integer getFrom()
    {
        return from;
    }


    /**
     * Set up the start from of the result set for pagination.
     *
     * @param from start from of the result set
     */
    public void setFrom(Integer from)
    {
        this.from = from;
    }


    /**
     * Return the number of the relationships out from the starting entity that the query will traverse to gather results.
     *
     * @return number of the relationship
     */
    public Integer getLevel()
    {
        return level;
    }


    /**
     * Set up the number of the relationships out from the starting entity that the query will traverse to gather results.
     *
     * @param level the number of the relationships out from the starting entity
     */
    public void setLevel(Integer level)
    {
        this.level = level;
    }


    /**
     * Return the name of the property that is to be used to sequence the results.
     *
     * @return the name of the property that is to be used to sequence the results
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the name of the property that is to be used to sequence the results.
     *
     * @param sequencingProperty the name of the property that is to be used to sequence the results
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the enum defining how the results should be ordered.
     *
     * @return the enum defining how the results should be ordered
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the enum defining how the results should be ordered.
     *
     * @param sequencingOrder the enum defining how the results should be ordered
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Returns the list of classifications that must be present on all returned entities.
     *
     * @return list of classifications that must be present on all returned entities.
     */
    public List<String> getLimitResultsByClassification()
    {
        return limitResultsByClassification;
    }


    /**
     * Set up the list of classifications that must be present on all returned entities.
     *
     * @param limitResultsByClassification list of classifications that must be present on all returned entities.
     */
    public void setLimitResultsByClassification(List<String> limitResultsByClassification)
    {
        this.limitResultsByClassification = limitResultsByClassification;
    }


    /**
     * Return the list of entity types name to search for. Null means any types.
     *
     * @return the list of entity types to search for
     */
    public List<String> getEntityTypes()
    {
        return entityTypes;
    }


    /**
     * Set up the list of entity types name to search for. Null means any types.
     *
     * @param entityTypes the list of entity types to search for
     */
    public void setEntityTypes(List<String> entityTypes)
    {
        this.entityTypes = entityTypes;
    }


    /**
     * Return the list of relationship types to include in the query results.
     * Null means include all relationships found, irrespective of their type.
     *
     * @return list of relationship types to include in the query results
     */
    public List<String> getRelationshipTypeGUIDs()
    {
        return relationshipTypeGUIDs;
    }


    /**
     * Set up the list of relationship types to include in the query results.
     * Null means include all relationships found, irrespective of their type.
     *
     * @param relationshipTypeGUIDs List of relationship types to include in the query results
     */
    public void setRelationshipTypeGUIDs(List<String> relationshipTypeGUIDs)
    {
        this.relationshipTypeGUIDs = relationshipTypeGUIDs;
    }


    /**
     * Returns whether the search should be performed as a case-insensitive regular expression (true)
     * or as a case-sensitive regular expression (false).
     *
     * @return false if it is performed a case-insensitive search and true otherwise
     */
    public boolean isCaseInsensitive()
    {
        return caseInsensitive;
    }


    /**
     * Indicates whether the search should be performed as a case-insensitive regular expression (true)
     * or as a case-sensitive regular expression (false).
     *
     * @param caseInsensitive boolean to set the case sensitivity for the search criteria
     */
    public void setCaseInsensitive(boolean caseInsensitive)
    {
        this.caseInsensitive = caseInsensitive;
    }


    /**
     * Returns whether the search should be performed taking into account and exact match criteria (true).
     *
     * @return false if it is performed an inexact match search and true otherwise
     */
    public boolean isExactMatch()
    {
        return exactMatch;
    }


    /**
     * Indicates whether the search should be performed  taking into account and exact match criteria (true).
     *
     * @param exactMatch boolean to set the exact match parameter for the search criteria
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }


    @Override
    public String toString()
    {
        return "SearchParameters{" +
                       "pageSize=" + pageSize +
                       ", from=" + from +
                       ", level=" + level +
                       ", sequencingProperty='" + sequencingProperty + '\'' +
                       ", sequencingOrder=" + sequencingOrder +
                       ", limitResultsByClassification=" + limitResultsByClassification +
                       ", entityTypes=" + entityTypes +
                       ", relationshipTypeGUIDs=" + relationshipTypeGUIDs +
                       ", caseInsensitive=" + caseInsensitive +
                       ", exactMatch=" + exactMatch +
                       '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SearchParameters that))
        {
            return false;
        }
        return caseInsensitive == that.caseInsensitive &&
                       exactMatch == that.exactMatch &&
                       Objects.equals(pageSize, that.pageSize) &&
                       Objects.equals(from, that.from) &&
                       Objects.equals(level, that.level) &&
                       Objects.equals(sequencingProperty, that.sequencingProperty) &&
                       sequencingOrder == that.sequencingOrder &&
                       Objects.equals(limitResultsByClassification, that.limitResultsByClassification) &&
                       Objects.equals(entityTypes, that.entityTypes) &&
                       Objects.equals(relationshipTypeGUIDs, that.relationshipTypeGUIDs);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(pageSize, from, level, sequencingProperty, sequencingOrder, limitResultsByClassification, entityTypes,
                            relationshipTypeGUIDs, caseInsensitive, exactMatch);
    }
}
