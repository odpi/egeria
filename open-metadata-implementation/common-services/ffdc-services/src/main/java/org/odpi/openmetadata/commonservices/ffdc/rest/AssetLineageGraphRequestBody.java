/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageGraphRequestBody provides the request body payload for an asset lineage graph.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetLineageGraphRequestBody extends EffectiveTimeQueryRequestBody
{
    private Date         asOfTime                  = null;
    private List<String> relationshipTypes         = null;
    private String       limitToISCQualifiedName   = null;
    private String       highlightISCQualifiedName = null;


    /**
     * Default constructor
     */
    public AssetLineageGraphRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetLineageGraphRequestBody(AssetLineageGraphRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.asOfTime                  = template.getAsOfTime();
            this.relationshipTypes         = template.getRelationshipTypes();
            this.limitToISCQualifiedName   = template.getLimitToISCQualifiedName();
            this.highlightISCQualifiedName = template.getHighlightISCQualifiedName();
        }
    }


    /**
     * Return the repository time that should be used when retrieving metadata.
     *
     * @return date/time (default null which means now)
     */
    public Date getAsOfTime()
    {
        return asOfTime;
    }


    /**
     * Set up the repository time that should be used when retrieving metadata.
     *
     * @param asOfTime date/time (default null which means now)
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }


    /**
     * Return the types of relationships that should be queried.
     *
     * @return list
     */
    public List<String> getRelationshipTypes()
    {
        return relationshipTypes;
    }


    /**
     * Set up he types of relationships that should be queried.
     *
     * @param relationshipTypes list
     */
    public void setRelationshipTypes(List<String> relationshipTypes)
    {
        this.relationshipTypes = relationshipTypes;
    }


    /**
     * Return whether the returned results should just show a particular information supply chain.
     * This supply chain has to be connected to the starting asset to show.
     *
     * @return qualified name
     */
    public String getLimitToISCQualifiedName()
    {
        return limitToISCQualifiedName;
    }


    /**
     * Set up whether the returned results should just show a particular information supply chain.
     * This supply chain has to be connected to the starting asset to show.
     *
     * @param limitToISCQualifiedName qualified name
     */
    public void setLimitToISCQualifiedName(String limitToISCQualifiedName)
    {
        this.limitToISCQualifiedName = limitToISCQualifiedName;
    }


    /**
     * Return whether a particular information supply chain should be highlighted.
     *
     * @return qualified name
     */
    public String getHighlightISCQualifiedName()
    {
        return highlightISCQualifiedName;
    }


    /**
     * Set up whether a particular information supply chain should be highlighted.
     *
     * @param highlightISCQualifiedName qualified name
     */
    public void setHighlightISCQualifiedName(String highlightISCQualifiedName)
    {
        this.highlightISCQualifiedName = highlightISCQualifiedName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AssetLineageGraphRequestBody{" +
                "relationshipTypes=" + relationshipTypes +
                ", asOfTime=" + asOfTime +
                ", limitToISCQualifiedName='" + limitToISCQualifiedName + '\'' +
                ", highlightISCQualifiedName='" + highlightISCQualifiedName + '\'' +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        AssetLineageGraphRequestBody that = (AssetLineageGraphRequestBody) objectToCompare;
        return  Objects.equals(asOfTime, that.asOfTime) &&
                Objects.equals(relationshipTypes, that.relationshipTypes) &&
                Objects.equals(limitToISCQualifiedName, that.limitToISCQualifiedName) &&
                Objects.equals(highlightISCQualifiedName, that.highlightISCQualifiedName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), asOfTime, relationshipTypes, limitToISCQualifiedName, highlightISCQualifiedName);
    }
}
