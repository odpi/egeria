/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataClassElement contains the properties and header for a data class entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassElement extends AttributedMetadataElement
{
    private DataClassProperties                 properties             = null;
    private List<RelatedMetadataElementSummary> nestedDataClasses      = null;
    private List<RelatedMetadataElementSummary> specializedDataClasses = null;

    /**
     * Default constructor
     */
    public DataClassElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataClassElement(DataClassElement template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
            nestedDataClasses = template.getNestedDataClasses();
            specializedDataClasses = template.getSpecializedDataClasses();
        }
    }


    /**
     * Return details of the data class
     *
     * @return data class properties
     */
    public DataClassProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up data class properties
     *
     * @param properties data class properties
     */
    public void setProperties(DataClassProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of data classes that support nested data fields.
     *
     * @return list of related data classes
     */
    public List<RelatedMetadataElementSummary> getNestedDataClasses()
    {
        return nestedDataClasses;
    }


    /**
     * Set up the list of data classes that support nested data fields.
     *
     * @param nestedDataClasses list of related data classes
     */
    public void setNestedDataClasses(List<RelatedMetadataElementSummary> nestedDataClasses)
    {
        this.nestedDataClasses = nestedDataClasses;
    }


    /**
     * Return the list of data classes that contain more specialized specifications.
     *
     * @return list of related data classes
     */
    public List<RelatedMetadataElementSummary> getSpecializedDataClasses()
    {
        return specializedDataClasses;
    }


    /**
     * Set up the list of data classes that contain more specialized specifications.
     *
     * @param specializedDataClasses list of related data classes
     */
    public void setSpecializedDataClasses(List<RelatedMetadataElementSummary> specializedDataClasses)
    {
        this.specializedDataClasses = specializedDataClasses;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataClassElement{" +
                "properties=" + properties +
                ", nestedDataClasses=" + nestedDataClasses +
                ", specializedDataClasses=" + specializedDataClasses +
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
        DataClassElement that = (DataClassElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(nestedDataClasses, that.nestedDataClasses) &&
                Objects.equals(specializedDataClasses, that.specializedDataClasses);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, nestedDataClasses, specializedDataClasses);
    }
}
