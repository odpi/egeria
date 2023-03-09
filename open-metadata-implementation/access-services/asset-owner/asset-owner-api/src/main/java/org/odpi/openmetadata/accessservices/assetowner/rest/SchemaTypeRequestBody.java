/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaTypeProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaTypeRequestBody carries the common parameters for creating or updating schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Deprecated
// Use SchemaTypeProperties
public class SchemaTypeRequestBody extends SchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    private SchemaTypeProperties schemaTypeProperties = null;

    /**
     * Default constructor
     */
    public SchemaTypeRequestBody()
    {
        super();
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public SchemaTypeRequestBody(SchemaTypeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            schemaTypeProperties = template.getSchemaTypeProperties();
        }
    }


    /**
     * Return the properties for the schema type.
     *
     * @return schema type properties
     */
    public SchemaTypeProperties getSchemaTypeProperties()
    {
        return schemaTypeProperties;
    }


    /**
     * Set up schema type
     *
     * @param schemaTypeProperties schema type properties
     */
    public void setSchemaTypeProperties(SchemaTypeProperties schemaTypeProperties)
    {
        this.schemaTypeProperties = schemaTypeProperties;
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
        SchemaTypeRequestBody that = (SchemaTypeRequestBody) objectToCompare;
        return Objects.equals(schemaTypeProperties, that.schemaTypeProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(schemaTypeProperties);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SchemaTypeRequestBody{" +
                "schemaTypeProperties=" + schemaTypeProperties +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", formula='" + getFormula() + '\'' +
                ", queries=" + getQueries() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", deprecated=" + getIsDeprecated() +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}
