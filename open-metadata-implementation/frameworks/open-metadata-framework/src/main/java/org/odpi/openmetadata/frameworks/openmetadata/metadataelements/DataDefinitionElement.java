/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFieldElement contains the properties and header for a data field entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataDefinitionElement extends OpenMetadataRootElement
{
    private List<RelatedMetadataElementSummary> assignedMeanings    = null;


    /**
     * Default constructor
     */
    public DataDefinitionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataDefinitionElement(DataDefinitionElement template)
    {
        super(template);

        if (template != null)
        {
            assignedMeanings    = template.getAssignedMeanings();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataDefinitionElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Return the assigned glossary terms that describes the meaning of this data field.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getAssignedMeanings()
    {
        return assignedMeanings;
    }


    /**
     * Set up the assigned glossary terms that describes the meaning of this data field.
     *
     * @param assignedMeanings related elements
     */
    public void setAssignedMeanings(List<RelatedMetadataElementSummary> assignedMeanings)
    {
        this.assignedMeanings = assignedMeanings;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataDefinitionElement{" +
                "assignedMeanings=" + assignedMeanings +
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
        DataDefinitionElement that = (DataDefinitionElement) objectToCompare;
        return Objects.equals(assignedMeanings, that.assignedMeanings);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assignedMeanings);
    }
}
