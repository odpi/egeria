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
 * This class represents an input field that contributes to an output field in the columnLineage dataset facet.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageColumnLineageDataSetFacetInputField
{
    private String                                                   namespace = null;
    private String                                                   name = null;
    private String                                                   field = null;
    private List<OpenLineageColumnLineageDataSetFacetTransformation> transformations = null;


    /**
     * Default constructor
     */
    public OpenLineageColumnLineageDataSetFacetInputField()
    {
    }


    /**
     * Return the namespace of the input dataset.
     *
     * @return string
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace of the input dataset.
     *
     * @param namespace string
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the name of the input dataset.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the input dataset.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the name of the input field.
     *
     * @return string
     */
    public String getField()
    {
        return field;
    }


    /**
     * Set up the name of the input field.
     *
     * @param field string
     */
    public void setField(String field)
    {
        this.field = field;
    }


    /**
     * Return the transformations applied to the input field.
     *
     * @return list
     */
    public List<OpenLineageColumnLineageDataSetFacetTransformation> getTransformations()
    {
        return transformations;
    }


    /**
     * Set up the transformations applied to the input field.
     *
     * @param transformations list
     */
    public void setTransformations(List<OpenLineageColumnLineageDataSetFacetTransformation> transformations)
    {
        this.transformations = transformations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageColumnLineageDataSetFacetInputField{" +
                       "namespace='" + namespace + '\'' +
                       ", name='" + name + '\'' +
                       ", field='" + field + '\'' +
                       ", transformations=" + transformations +
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
        OpenLineageColumnLineageDataSetFacetInputField that = (OpenLineageColumnLineageDataSetFacetInputField) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(field, that.field) &&
                       Objects.equals(transformations, that.transformations);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(namespace, name, field, transformations);
    }
}
