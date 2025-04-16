/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The response object for passing back a list of GAF OpenMetadataRelationship
 * or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataElementListResponse extends OMAGGAFAPIResponse
{
     private RelatedMetadataElementList relatedElementList = null;


    /**
     * Default constructor
     */
    public RelatedMetadataElementListResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataElementListResponse(RelatedMetadataElementListResponse template)
    {
        super(template);

        if (template != null)
        {
            relatedElementList = template.getRelatedElementList();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public RelatedMetadataElementList getRelatedElementList()
    {
        return relatedElementList;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param relatedElementList result object
     */
    public void setRelatedElementList(RelatedMetadataElementList relatedElementList)
    {
        this.relatedElementList = relatedElementList;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedMetadataElementListResponse{" +
                "relatedElementList=" + relatedElementList +
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
        RelatedMetadataElementListResponse that = (RelatedMetadataElementListResponse) objectToCompare;
        return Objects.equals(relatedElementList, that.relatedElementList);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedElementList);
    }
}
