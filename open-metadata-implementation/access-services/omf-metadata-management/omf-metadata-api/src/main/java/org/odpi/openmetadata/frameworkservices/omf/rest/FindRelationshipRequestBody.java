/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.search.EndMatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;

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
public class FindRelationshipRequestBody extends ResultsRequestBody
{
    private String           relationshipTypeName     = null;
    private List<String>     relationshipSubtypeNames = null;
    private List<String>     end1EntityGUIDs          = null;
    private String           end1EntityTypeName       = null;
    private List<String>     end2EntityGUIDs          = null;
    private String           end2EntityTypeName       = null;
    private EndMatchCriteria endMatchCriteria         = null;
    private SearchProperties searchProperties         = null;


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
        super(template);

        if (template != null)
        {
            this.relationshipTypeName     = template.getRelationshipTypeName();
            this.relationshipSubtypeNames = template.getRelationshipSubtypeNames();
            this.searchProperties         = template.getSearchProperties();
            this.end1EntityGUIDs          = template.getEnd1EntityGUIDs();
            this.end1EntityTypeName       = template.getEnd1EntityTypeName();
            this.end2EntityGUIDs          = template.getEnd2EntityGUIDs();
            this.end2EntityTypeName       = template.getEnd2EntityTypeName();
            this.endMatchCriteria         = template.getEndMatchCriteria();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FindRelationshipRequestBody(QueryOptions template)
    {
        super(template);
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
     * Return the subtype names to limit the results of the find request.
     *
     * @return {@code List<String>} open metadata type names
     */
    public List<String> getRelationshipSubtypeNames()
    {
        return relationshipSubtypeNames;
    }


    /**
     * Set up the subtype names to limit the results of the find request.
     *
     * @param relationshipSubtypeNames {@code List<String>} open metadata type names
     */
    public void setRelationshipSubtypeNames(List<String> relationshipSubtypeNames)
    {
        this.relationshipSubtypeNames = relationshipSubtypeNames;
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
     * Return the list of entity guids used to match end 1 of the relationships.
     *
     * @return list of guids
     */
    public List<String> getEnd1EntityGUIDs()
    {
        return end1EntityGUIDs;
    }


    /**
     * Set up the list of entity guids used to match end 1 of the relationships.
     *
     * @param end1EntityGUIDs list of guids
     */
    public void setEnd1EntityGUIDs(List<String> end1EntityGUIDs)
    {
        this.end1EntityGUIDs = end1EntityGUIDs;
    }


    /**
     * Return the name of the type that the entity at end 1 of the relationship must belong to.  Subtypes of
     * the named type match too.  This is optional, and independent of the end 1 guids: naming the type on its
     * own, with no guids, asks for the relationships that start at any entity of that type.
     *
     * @return type name
     */
    public String getEnd1EntityTypeName()
    {
        return end1EntityTypeName;
    }


    /**
     * Set up the name of the type that the entity at end 1 of the relationship must belong to.
     *
     * @param end1EntityTypeName type name
     */
    public void setEnd1EntityTypeName(String end1EntityTypeName)
    {
        this.end1EntityTypeName = end1EntityTypeName;
    }


    /**
     * Return the list of entity guids used to match end 2 of the relationships.
     *
     * @return list of guids
     */
    public List<String> getEnd2EntityGUIDs()
    {
        return end2EntityGUIDs;
    }


    /**
     * Set up the list of entity guids used to match end 2 of the relationships.
     *
     * @param end2EntityGUIDs list of guids
     */
    public void setEnd2EntityGUIDs(List<String> end2EntityGUIDs)
    {
        this.end2EntityGUIDs = end2EntityGUIDs;
    }


    /**
     * Return the name of the type that the entity at end 2 of the relationship must belong to.  Subtypes of
     * the named type match too.  This is optional, and independent of the end 2 guids: naming the type on its
     * own, with no guids, asks for the relationships that end at any entity of that type.
     *
     * @return type name
     */
    public String getEnd2EntityTypeName()
    {
        return end2EntityTypeName;
    }


    /**
     * Set up the name of the type that the entity at end 2 of the relationship must belong to.
     *
     * @param end2EntityTypeName type name
     */
    public void setEnd2EntityTypeName(String end2EntityTypeName)
    {
        this.end2EntityTypeName = end2EntityTypeName;
    }


    /**
     * Return the end matching search criteria.
     *
     * @return SearchClassifications
     */
    public EndMatchCriteria getEndMatchCriteria()
    {
        return endMatchCriteria;
    }


    /**
     * Set the end matching search criteria.
     *
     * @param endMatchCriteria to set as search criteria
     */
    public void setEndMatchCriteria(EndMatchCriteria endMatchCriteria)
    {
        this.endMatchCriteria = endMatchCriteria;
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
                ", relationshipSubtypeNames=" + relationshipSubtypeNames +
                ", end1EntityGUIDs=" + end1EntityGUIDs +
                ", end1EntityTypeName='" + end1EntityTypeName + '\'' +
                ", end2EntityGUIDs=" + end2EntityGUIDs +
                ", end2EntityTypeName='" + end2EntityTypeName + '\'' +
                ", endMatchCriteria=" + endMatchCriteria +
                ", searchProperties=" + searchProperties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindRelationshipRequestBody that = (FindRelationshipRequestBody) objectToCompare;
        return Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(relationshipSubtypeNames, that.relationshipSubtypeNames) &&
                Objects.equals(end1EntityGUIDs, that.end1EntityGUIDs) &&
                Objects.equals(end1EntityTypeName, that.end1EntityTypeName) &&
                Objects.equals(end2EntityGUIDs, that.end2EntityGUIDs) &&
                Objects.equals(end2EntityTypeName, that.end2EntityTypeName) &&
                endMatchCriteria == that.endMatchCriteria &&
                Objects.equals(searchProperties, that.searchProperties);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipTypeName, relationshipSubtypeNames,
                            end1EntityGUIDs, end1EntityTypeName, end2EntityGUIDs, end2EntityTypeName,
                            endMatchCriteria, searchProperties);
    }
}
