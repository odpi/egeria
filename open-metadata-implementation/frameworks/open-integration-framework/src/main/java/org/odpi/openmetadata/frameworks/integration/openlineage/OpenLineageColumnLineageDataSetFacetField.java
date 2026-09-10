/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the input fields (and transformation) that produce a single output field in the columnLineage dataset facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageColumnLineageDataSetFacetField
{
    private List<OpenLineageColumnLineageDataSetFacetInputField> inputFields = null;
    private String                                               transformationDescription = null;
    private String                                               transformationType = null;


    /**
     * Default constructor
     */
    public OpenLineageColumnLineageDataSetFacetField()
    {
    }


    /**
     * Return the input fields used to evaluate the output field.
     *
     * @return list
     */
    public List<OpenLineageColumnLineageDataSetFacetInputField> getInputFields()
    {
        return inputFields;
    }


    /**
     * Set up the input fields used to evaluate the output field.
     *
     * @param inputFields list
     */
    public void setInputFields(List<OpenLineageColumnLineageDataSetFacetInputField> inputFields)
    {
        this.inputFields = inputFields;
    }


    /**
     * Return the string representation of the transformation applied (deprecated in favour of per-input transformations).
     *
     * @return string
     */
    public String getTransformationDescription()
    {
        return transformationDescription;
    }


    /**
     * Set up the string representation of the transformation applied (deprecated in favour of per-input transformations).
     *
     * @param transformationDescription string
     */
    public void setTransformationDescription(String transformationDescription)
    {
        this.transformationDescription = transformationDescription;
    }


    /**
     * Return the type of the transformation: IDENTITY or MASKED (deprecated in favour of per-input transformations).
     *
     * @return string
     */
    public String getTransformationType()
    {
        return transformationType;
    }


    /**
     * Set up the type of the transformation: IDENTITY or MASKED (deprecated in favour of per-input transformations).
     *
     * @param transformationType string
     */
    public void setTransformationType(String transformationType)
    {
        this.transformationType = transformationType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageColumnLineageDataSetFacetField{" +
                       "inputFields=" + inputFields +
                       ", transformationDescription='" + transformationDescription + '\'' +
                       ", transformationType='" + transformationType + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        OpenLineageColumnLineageDataSetFacetField that = (OpenLineageColumnLineageDataSetFacetField) objectToCompare;
        return Objects.equals(inputFields, that.inputFields) &&
                       Objects.equals(transformationDescription, that.transformationDescription) &&
                       Objects.equals(transformationType, that.transformationType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(inputFields, transformationDescription, transformationType);
    }
}
