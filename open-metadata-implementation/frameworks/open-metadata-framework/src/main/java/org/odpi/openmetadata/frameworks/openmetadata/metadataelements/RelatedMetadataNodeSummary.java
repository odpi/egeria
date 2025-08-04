/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedBy contains the properties and header for a relationship retrieved from the metadata repository along with the stub
 * of the related element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataNodeSummary extends RelatedMetadataElementSummary
{
    private String  startingElementGUID = null;


    /**
     * Default constructor
     */
    public RelatedMetadataNodeSummary()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataNodeSummary(RelatedMetadataNodeSummary template)
    {
        super(template);

        if (template != null)
        {
            startingElementGUID = template.getStartingElementGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template superclass object to copy
     * @param startingElementGUID starting element unique identifier
     */
    public RelatedMetadataNodeSummary(RelatedMetadataElementSummary template,
                                      String                        startingElementGUID)
    {
        super(template);

        if (template != null)
        {
            this.startingElementGUID = startingElementGUID;
        }
    }


    /**
     * Return whether the element is at end 1 of the relationship.
     *
     * @return guid
     */
    public String getStartingElementGUID()
    {
        return startingElementGUID;
    }


    /**
     * Set up whether the element is at end 1 of the relationship.
     *
     * @param startingElementGUID guid
     */
    public void setStartingElementGUID(String startingElementGUID)
    {
        this.startingElementGUID = startingElementGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedMetadataNodeSummary{" +
                "startingElementGUID='" + startingElementGUID + '\'' +
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
        RelatedMetadataNodeSummary that = (RelatedMetadataNodeSummary) objectToCompare;
        return Objects.equals(startingElementGUID, that.startingElementGUID);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), startingElementGUID);
    }
}
