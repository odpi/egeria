/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;

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
public class DataFieldElement extends AttributedMetadataElement
{
    private DataFieldProperties                 properties          = null;
    private List<MemberDataField>               nestedDataFields    = null;
    private List<RelatedMetadataElementSummary> assignedDataClasses = null;
    private List<RelatedMetadataElementSummary> assignedMeanings    = null;


    /**
     * Default constructor
     */
    public DataFieldElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataFieldElement(DataFieldElement template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
            nestedDataFields    = template.getNestedDataFields();
            assignedDataClasses = template.getAssignedDataClasses();
            assignedMeanings    = template.getAssignedMeanings();
        }
    }


    /**
     * Return details of the data field
     *
     * @return data field properties
     */
    public DataFieldProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up data field properties
     *
     * @param properties data field properties
     */
    public void setProperties(DataFieldProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of nested data fields.
     *
     * @return list
     */
    public List<MemberDataField> getNestedDataFields()
    {
        return nestedDataFields;
    }


    /**
     * Set up the list of nested data fields.
     *
     * @param nestedDataFields list
     */
    public void setNestedDataFields(List<MemberDataField> nestedDataFields)
    {
        this.nestedDataFields = nestedDataFields;
    }


    /**
     * Return the assigned data classes that describes the content in this data field.
     *
     * @return related elements
     */
    public List<RelatedMetadataElementSummary> getAssignedDataClasses()
    {
        return assignedDataClasses;
    }


    /**
     * Set up the assigned data classes that describes the content in this data field.
     *
     * @param assignedDataClasses related elements
     */
    public void setAssignedDataClasses(List<RelatedMetadataElementSummary> assignedDataClasses)
    {
        this.assignedDataClasses = assignedDataClasses;
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
        return "DataFieldElement{" +
                "properties=" + properties +
                ", nestedDataFields=" + nestedDataFields +
                ", assignedDataClasses=" + assignedDataClasses +
                ", assignedMeanings=" + assignedMeanings +
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
        DataFieldElement that = (DataFieldElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(nestedDataFields, that.nestedDataFields) &&
                Objects.equals(assignedDataClasses, that.assignedDataClasses) &&
                Objects.equals(assignedMeanings, that.assignedMeanings);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, nestedDataFields, assignedDataClasses, assignedMeanings);
    }
}
