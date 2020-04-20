/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaAttributesRequestBody carries the common parameters for creating a list of schema attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaAttributesRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private List<SchemaAttributeProperties> schemaAttributeProperties = null;


    /**
     * Default constructor
     */
    public SchemaAttributesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template schema attribute to copy.
     */
    public SchemaAttributesRequestBody(SchemaAttributesRequestBody template)
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
    public List<SchemaAttributeProperties> getSchemaAttributeProperties()
    {
        if (schemaAttributeProperties == null)
        {
            return null;
        }
        else if (schemaAttributeProperties.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(schemaAttributeProperties);
    }


    /**
     * Set up the properties that describe the schema attribute.
     *
     * @param schemaAttributeProperties schema attribute properties
     */
    public void setSchemaAttributeProperties(List<SchemaAttributeProperties> schemaAttributeProperties)
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
        SchemaAttributesRequestBody that = (SchemaAttributesRequestBody) objectToCompare;
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