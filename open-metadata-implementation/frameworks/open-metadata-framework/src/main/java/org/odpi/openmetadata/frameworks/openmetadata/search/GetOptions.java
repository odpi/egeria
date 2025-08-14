/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GetOptions carries the date/time for a query along with other common search parameters.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GetOptions extends BasicOptions
{
    private Date         asOfTime                      = null;
    private String       metadataElementTypeName       = null;
    private List<String> metadataElementSubtypeNames   = null;
    private List<String> skipRelationships             = null;
    private List<String> includeOnlyRelationships      = null;
    private List<String> skipClassifiedElements        = null;
    private List<String> includeOnlyClassifiedElements = null;
    private int          graphQueryDepth               = 5;

    /**
     * Default constructor
     */
    public GetOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GetOptions(GetOptions template)
    {
        super(template);

        if (template != null)
        {
            asOfTime                      = template.getAsOfTime();
            metadataElementTypeName       = template.getMetadataElementTypeName();
            metadataElementSubtypeNames   = template.getMetadataElementSubtypeNames();
            skipRelationships             = template.getSkipRelationships();
            includeOnlyRelationships      = template.getIncludeOnlyRelationships();
            skipClassifiedElements        = template.getSkipClassifiedElements();
            includeOnlyClassifiedElements = template.getIncludeOnlyClassifiedElements();
            graphQueryDepth               = template.getGraphQueryDepth();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GetOptions(BasicOptions template)
    {
        super(template);
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
     * Return the list of relationships that the query should skip (not return).
     * Typically, egeria queries return all related element when an element is retrieved.
     * Some elements have a large number of linked elements.  This option allows you to prune specific
     * relationships from the response.
     *
     * @return list of relationship type names or null
     */
    public List<String> getSkipRelationships()
    {
        return skipRelationships;
    }


    /**
     * Set up the list of relationships that the query should skip (not return). Default is null.
     *
     * @param skipRelationships  list of relationship type names or null
     */
    public void setSkipRelationships(List<String> skipRelationships)
    {
        this.skipRelationships = skipRelationships;
    }


    /**
     * Return the list of relationship type names that restrict which types of relationships
     * can be returned from the query.
     *
     * @return list of relationship type names or null
     */
    public List<String> getIncludeOnlyRelationships()
    {
        return includeOnlyRelationships;
    }


    /**
     * Set up the list of relationship type names that restrict which types of relationships
     * can be returned from the query.
     *
     * @param includeOnlyRelationships list of relationship type names or null
     */
    public void setIncludeOnlyRelationships(List<String> includeOnlyRelationships)
    {
        this.includeOnlyRelationships = includeOnlyRelationships;
    }


    /**
     * Return the list of classification names that should not be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @return list of classification names or null.
     */
    public List<String> getSkipClassifiedElements()
    {
        return skipClassifiedElements;
    }


    /**
     * Set up the list of classification names that should not be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @param skipClassifiedElements list of classification names or null.
     */
    public void setSkipClassifiedElements(List<String> skipClassifiedElements)
    {
        this.skipClassifiedElements = skipClassifiedElements;
    }


    /**
     * Return the list of classification names that must be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @return list of classification names or null.
     */
    public List<String> getIncludeOnlyClassifiedElements()
    {
        return includeOnlyClassifiedElements;
    }


    /**
     * Set up the list of classification names that must be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @param includeOnlyClassifiedElements list of classification names or null.
     */
    public void setIncludeOnlyClassifiedElements(List<String> includeOnlyClassifiedElements)
    {
        this.includeOnlyClassifiedElements = includeOnlyClassifiedElements;
    }


    /**
     * Return the maximum number of relationships away from the starting element that a graph query can traverse.
     *
     * @return int (default is 5)
     */
    public int getGraphQueryDepth()
    {
        return graphQueryDepth;
    }


    /**
     * Set up the maximum number of relationships away from the starting element that a graph query can traverse.
     *
     * @param graphQueryDepth int (default is 5)
     */
    public void setGraphQueryDepth(int graphQueryDepth)
    {
        this.graphQueryDepth = graphQueryDepth;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GetOptions{" +
                "asOfTime=" + asOfTime +
                ", metadataElementTypeName='" + metadataElementTypeName + '\'' +
                ", skipRelationships=" + skipRelationships +
                ", includeOnlyRelationships=" + includeOnlyRelationships +
                ", skipClassifiedElements=" + skipClassifiedElements +
                ", includeOnlyClassifiedElements=" + includeOnlyClassifiedElements +
                ", graphQueryDepth=" + graphQueryDepth +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GetOptions that = (GetOptions) objectToCompare;
        return graphQueryDepth == that.graphQueryDepth &&
                Objects.equals(asOfTime, that.asOfTime) &&
                Objects.equals(metadataElementTypeName, that.metadataElementTypeName) &&
                Objects.equals(metadataElementSubtypeNames, that.metadataElementSubtypeNames) &&
                Objects.equals(skipRelationships, that.skipRelationships) &&
                Objects.equals(includeOnlyRelationships, that.includeOnlyRelationships) &&
                Objects.equals(skipClassifiedElements, that.skipClassifiedElements) &&
                Objects.equals(includeOnlyClassifiedElements, that.includeOnlyClassifiedElements);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), asOfTime, metadataElementTypeName, metadataElementSubtypeNames, skipRelationships, includeOnlyRelationships, skipClassifiedElements, includeOnlyClassifiedElements, graphQueryDepth);
    }
}
