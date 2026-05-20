/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataRelationshipSummaryResponse is a response object for passing back a single 
 * relationship summary or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataRelationshipSummaryResponse extends FFDCResponseBase
{
    private MetadataRelationshipSummary relationship = null;


    /**
     * Default constructor
     */
    public MetadataRelationshipSummaryResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataRelationshipSummaryResponse(MetadataRelationshipSummaryResponse template)
    {
        super(template);

        if (template != null)
        {
            relationship = template.getRelationship();
        }
    }


    /**
     * Return the relationship.
     *
     * @return result object
     */
    public MetadataRelationshipSummary getRelationship()
    {
        return relationship;
    }


    /**
     * Set up the relationship to return.
     *
     * @param relationship result object
     */
    public void setRelationship(MetadataRelationshipSummary relationship)
    {
        this.relationship = relationship;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MetadataRelationshipSummaryResponse{" +
                "relationship=" + relationship +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MetadataRelationshipSummaryResponse that = (MetadataRelationshipSummaryResponse) objectToCompare;
        return Objects.equals(relationship, that.relationship);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationship);
    }
}
