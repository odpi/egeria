/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MetadataRelationshipSummariesResponse is the response structure used on the OMAS REST API calls that return a
 * list of relationships identifiers as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataRelationshipSummariesResponse extends FFDCResponseBase
{
    private List<MetadataRelationshipSummary> relationships = null;


    /**
     * Default constructor
     */
    public MetadataRelationshipSummariesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataRelationshipSummariesResponse(MetadataRelationshipSummariesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.relationships = template.getRelationships();
        }
    }


    /**
     * Return the list of element identifiers.
     *
     * @return list of objects or null
     */
    public List<MetadataRelationshipSummary> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the list of element identifiers.
     *
     * @param relationships - list of objects or null
     */
    public void setRelationships(List<MetadataRelationshipSummary> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MetadataRelationshipSummariesResponse{" +
                "relationships=" + relationships +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof MetadataRelationshipSummariesResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(this.getRelationships(), that.getRelationships());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationships, super.hashCode());
    }
}
