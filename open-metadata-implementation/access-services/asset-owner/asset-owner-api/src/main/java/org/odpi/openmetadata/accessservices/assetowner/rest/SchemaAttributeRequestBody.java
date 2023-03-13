/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributeRequestBody carries the common parameters for creating or updating a schema attribute.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributeRequestBody extends SchemaAttributeProperties
{
    private static final long    serialVersionUID = 1L;

    private SchemaAttributeProperties schemaAttributeProperties = null;


    /**
     * Default constructor
     */
    public SchemaAttributeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttributeRequestBody(SchemaAttributeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            schemaAttributeProperties = template.getSchemaAttributeProperties();
        }
    }


    /**
     * Return the properties that describe the schema attribute.
     *
     * @return schema attribute properties
     */
    public SchemaAttributeProperties getSchemaAttributeProperties()
    {
        return schemaAttributeProperties;
    }


    /**
     * Set up the properties that describe the schema attribute.
     *
     * @param schemaAttributeProperties schema attribute properties
     */
    public void setSchemaAttributeProperties(SchemaAttributeProperties schemaAttributeProperties)
    {
        this.schemaAttributeProperties = schemaAttributeProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaAttributeRequestBody{" +
                "schemaAttributeProperties=" + schemaAttributeProperties +
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
        SchemaAttributeRequestBody that = (SchemaAttributeRequestBody) objectToCompare;
        return Objects.equals(schemaAttributeProperties, that.schemaAttributeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(schemaAttributeProperties);
    }
}
