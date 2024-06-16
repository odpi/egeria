/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeChoice supports an element that has a selection of schema types that could be
 * used as the type of the attribute.  When the schema is used, values are only stored in one of the options.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaTypeChoice extends SchemaType
{
    private List<SchemaType>  schemaOptions = null;

    /**
     * Default constructor used by subclasses
     */
    public SchemaTypeChoice()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaTypeChoice(SchemaTypeChoice template)
    {
        super(template);

        if (template != null)
        {
            schemaOptions = template.getSchemaOptions();
        }
    }


    /**
     * Return the list of alternative schema types that this attribute or asset may use.
     *
     * @return list of schema types
     */
    public List<SchemaType> getSchemaOptions()
    {
        if (schemaOptions == null)
        {
            return null;
        }
        else if (schemaOptions.isEmpty())
        {
            return null;
        }

        return schemaOptions;
    }


    /**
     * Set up the list of alternative schema types that this attribute or asset may use.
     *
     * @param schemaOptions list of schema types
     */
    public void setSchemaOptions(List<SchemaType> schemaOptions)
    {
        this.schemaOptions = schemaOptions;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new SchemaTypeChoice(this);
    }


    /**
     * Returns a clone of this object as the abstract SchemaType class.
     *
     * @return SchemaType object
     */
    @Override
    public SchemaType cloneSchemaType()
    {
        return new SchemaTypeChoice(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaTypeChoice{" +
                "schemaOptions=" + schemaOptions +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
                ", headerVersion=" + getHeaderVersion() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SchemaTypeChoice that = (SchemaTypeChoice) objectToCompare;
        return Objects.equals(getSchemaOptions(), that.getSchemaOptions());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSchemaOptions());
    }
}