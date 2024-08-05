/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRelationshipRequestBody provides a structure for passing the properties for the find relationships request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindRelationshipRequestBody
{
    private String                relationshipTypeName        = null;
    private SearchProperties      searchProperties            = null;
    private List<ElementStatus>   limitResultsByStatus        = null;
    private String                sequencingProperty          = null;
    private SequencingOrder       sequencingOrder             = null;
    private Date                  asOfTime                    = null;


    /**
     * Default constructor
     */
    public FindRelationshipRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRelationshipRequestBody(FindRelationshipRequestBody template)
    {
        if (template != null)
        {
            relationshipTypeName = template.getRelationshipTypeName();
            searchProperties     = template.getSearchProperties();
            limitResultsByStatus = template.getLimitResultsByStatus();
            asOfTime             = template.getAsOfTime();
            sequencingProperty   = template.getSequencingProperty();
            sequencingOrder      = template.getSequencingOrder();
        }
    }


    /**
     * Return the type of relationship that the caller is searching for.
     *
     * @return open metadata type name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Set up the type of relationship that the caller is searching for.
     *
     * @param relationshipTypeName open metadata type name
     */
    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
    }


    /**
     * Return the details of the property values that must be true for the returned metadata elements.
     *
     * @return property specification
     */
    public SearchProperties getSearchProperties()
    {
        return searchProperties;
    }


    /**
     * Set up the details of the property values that must be true for the returned metadata elements.
     *
     * @param searchProperties property specification
     */
    public void setSearchProperties(SearchProperties searchProperties)
    {
        this.searchProperties = searchProperties;
    }


    /**
     * Return the status values that the resulting metadata elements must match.
     * By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @return status values
     */
    public List<ElementStatus> getLimitResultsByStatus()
    {
        return limitResultsByStatus;
    }


    /**
     * Set up the status values that the resulting metadata elements must match.
     *
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     */
    public void setLimitResultsByStatus(List<ElementStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Return the time used for a historical query - null mean current repository contents.
     *
     * @return date/time object
     */
    public Date getAsOfTime()
    {
        return asOfTime;
    }


    /**
     * Set up the time used for a historical query - null mean current repository contents.
     *
     * @param asOfTime date/time object
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }


    /**
     * Return the name of the property whose value will be used to sequence the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the name of the property whose value will be used to sequence the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the order that the results should be returned in.
     *
     * @return enum for the sequencing order
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the order that the results should be returned in.
     *
     * @param sequencingOrder enum for the sequencing order
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "FindRelationshipRequestBody{" +
                       "relationshipTypeName='" + relationshipTypeName + '\'' +
                       ", searchProperties=" + searchProperties +
                       ", limitResultsByStatus=" + limitResultsByStatus +
                       ", sequencingProperty='" + sequencingProperty + '\'' +
                       ", sequencingOrder=" + sequencingOrder +
                       ", asOfTime=" + asOfTime +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        FindRelationshipRequestBody that = (FindRelationshipRequestBody) objectToCompare;
        return Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(searchProperties, that.searchProperties) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                Objects.equals(sequencingProperty, that.sequencingProperty) &&
                sequencingOrder == that.sequencingOrder &&
                Objects.equals(asOfTime, that.asOfTime);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipTypeName, searchProperties, limitResultsByStatus,
                            asOfTime, sequencingProperty, sequencingOrder);
    }
}
