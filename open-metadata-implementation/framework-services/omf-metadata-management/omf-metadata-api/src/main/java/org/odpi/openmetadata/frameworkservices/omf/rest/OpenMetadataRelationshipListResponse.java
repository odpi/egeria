/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRelationshipListResponse is a response object for passing back a list of omf OpenMetadataRelationship
 * or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationshipListResponse extends OMAGOMFAPIResponse
{
    private OpenMetadataRelationshipList relationshipList = null;


    /**
     * Default constructor
     */
    public OpenMetadataRelationshipListResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRelationshipListResponse(OpenMetadataRelationshipListResponse template)
    {
        super(template);

        if (template != null)
        {
            relationshipList = template.getRelationshipList();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public OpenMetadataRelationshipList getRelationshipList()
    {
        return relationshipList;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param relationshipList result object
     */
    public void setRelationshipList(OpenMetadataRelationshipList relationshipList)
    {
        this.relationshipList = relationshipList;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationshipListResponse{" +
                "relationshipList=" + relationshipList +
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
        OpenMetadataRelationshipListResponse that = (OpenMetadataRelationshipListResponse) objectToCompare;
        return Objects.equals(relationshipList, that.relationshipList);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationshipList);
    }
}
