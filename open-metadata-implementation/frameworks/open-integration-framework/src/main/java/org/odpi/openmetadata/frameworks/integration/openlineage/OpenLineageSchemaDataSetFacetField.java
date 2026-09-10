/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single field in the schema dataset facet.  Fields may be nested.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageSchemaDataSetFacetField
{
    private String                                   name = null;
    private String                                   type = null;
    private String                                   description = null;
    private Long                                     ordinalPosition = null;
    private List<OpenLineageSchemaDataSetFacetField> fields = null;


    /**
     * Default constructor
     */
    public OpenLineageSchemaDataSetFacetField()
    {
    }


    /**
     * Return the name of the field.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the field.
     *
     * @param name string
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the type of the field.
     *
     * @return string
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of the field.
     *
     * @param type string
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the description of the field.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the field.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the ordinal position of the field in the schema (1-indexed).
     *
     * @return long
     */
    @JsonProperty("ordinal_position")
    public Long getOrdinalPosition()
    {
        return ordinalPosition;
    }


    /**
     * Set up the ordinal position of the field in the schema (1-indexed).
     *
     * @param ordinalPosition long
     */
    @JsonProperty("ordinal_position")
    public void setOrdinalPosition(Long ordinalPosition)
    {
        this.ordinalPosition = ordinalPosition;
    }


    /**
     * Return the nested struct fields.
     *
     * @return list
     */
    public List<OpenLineageSchemaDataSetFacetField> getFields()
    {
        return fields;
    }


    /**
     * Set up the nested struct fields.
     *
     * @param fields list
     */
    public void setFields(List<OpenLineageSchemaDataSetFacetField> fields)
    {
        this.fields = fields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageSchemaDataSetFacetField{" +
                       "name='" + name + '\'' +
                       ", type='" + type + '\'' +
                       ", description='" + description + '\'' +
                       ", ordinalPosition=" + ordinalPosition +
                       ", fields=" + fields +
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
        OpenLineageSchemaDataSetFacetField that = (OpenLineageSchemaDataSetFacetField) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(ordinalPosition, that.ordinalPosition) &&
                       Objects.equals(fields, that.fields);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, type, description, ordinalPosition, fields);
    }
}
