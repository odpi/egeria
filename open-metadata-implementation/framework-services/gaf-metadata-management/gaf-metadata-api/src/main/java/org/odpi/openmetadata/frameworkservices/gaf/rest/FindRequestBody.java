/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRequestBody provides a structure for passing the properties for the find request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FindRequestBody
{
    private String                metadataElementTypeName     = null;
    private List<String>          metadataElementSubtypeNames = null;
    private SearchProperties      searchProperties            = null;
    private List<ElementStatus>   limitResultsByStatus        = null;
    private SearchClassifications matchClassifications        = null;
    private String                sequencingProperty          = null;
    private SequencingOrder       sequencingOrder             = null;
    private Date                  asOfTime                    = null;


    /**
     * Default constructor
     */
    public FindRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRequestBody(FindRequestBody template)
    {
        if (template != null)
        {
            metadataElementTypeName     = template.getMetadataElementTypeName();
            metadataElementSubtypeNames = template.getMetadataElementSubtypeNames();
            searchProperties            = template.getSearchProperties();
            limitResultsByStatus = template.getLimitResultsByStatus();
            matchClassifications = template.getMatchClassifications();
            asOfTime = template.getAsOfTime();
            sequencingProperty = template.getSequencingProperty();
            sequencingOrder = template.getSequencingOrder();
        }
    }


    /**
     * Return the type of metadata element that the caller is searching for.
     *
     * @return open metadata type name
     */
    public String getMetadataElementTypeName()
    {
        return metadataElementTypeName;
    }


    /**
     * Set up the type of metadata element that the caller is searching for.
     *
     * @param metadataElementTypeName open metadata type name
     */
    public void setMetadataElementTypeName(String metadataElementTypeName)
    {
        this.metadataElementTypeName = metadataElementTypeName;
    }


    /**
     * Return the list of valid subtypes that the returned metadata elements must belong to.
     *
     * @return open metadata type names
     */
    public List<String> getMetadataElementSubtypeNames()
    {
        return metadataElementSubtypeNames;
    }


    /**
     * Set up the list of valid subtypes that the returned metadata elements must belong to.
     *
     * @param metadataElementSubtypeNames open metadata type names
     */
    public void setMetadataElementSubtypeNames(List<String> metadataElementSubtypeNames)
    {
        this.metadataElementSubtypeNames = metadataElementSubtypeNames;
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
     *  status values except DELETED.
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
     * Return details about the classifications that need to be present/absent from the search request.
     *
     * @return classification specification
     */
    public SearchClassifications getMatchClassifications()
    {
        return matchClassifications;
    }


    /**
     * Set up details about the classifications that need to be present/absent from the search request.
     *
     * @param matchClassifications classification specification
     */
    public void setMatchClassifications(SearchClassifications matchClassifications)
    {
        this.matchClassifications = matchClassifications;
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
        return "FindRequestBody{" +
                       "metadataElementTypeName='" + metadataElementTypeName + '\'' +
                       ", metadataElementSubtypeName=" + metadataElementSubtypeNames +
                       ", searchProperties=" + searchProperties +
                       ", limitResultsByStatus=" + limitResultsByStatus +
                       ", matchClassifications=" + matchClassifications +
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
        FindRequestBody that = (FindRequestBody) objectToCompare;
        return Objects.equals(metadataElementTypeName, that.metadataElementTypeName) &&
                Objects.equals(metadataElementSubtypeNames, that.metadataElementSubtypeNames) &&
                Objects.equals(searchProperties, that.searchProperties) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                Objects.equals(matchClassifications, that.matchClassifications) &&
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
        return Objects.hash(metadataElementTypeName, metadataElementSubtypeNames, searchProperties, limitResultsByStatus, matchClassifications,
                            asOfTime, sequencingProperty, sequencingOrder);
    }
}
