/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;

import java.io.Serial;
import java.util.Objects;

/**
 * An asset's schema provides information about how the asset structures the data it supports.
 * The NestedSchemaType object describes a nested structure of schema attributes and types.
 */
public  class NestedSchemaType extends ComplexSchemaType
{
    @Serial
    private static final long serialVersionUID = 1L;

    protected SchemaAttributes  schemaAttributes = null;


    /**
     * Constructor used by the subclasses
     */
    protected NestedSchemaType()
    {
        super();
    }



    /**
     * Bean constructor
     *
     * @param schemaBean bean containing the schema properties
     */
    public NestedSchemaType(ComplexSchemaType schemaBean)
    {
        super(schemaBean);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public NestedSchemaType(NestedSchemaType template)
    {
        super(template);

        if (template != null)
        {
            this.schemaAttributes = template.getSchemaAttributes();
        }
    }


    /**
     * Return the list of schema attributes in this schema.
     *
     * @return SchemaAttributes
     */
    public SchemaAttributes getSchemaAttributes()
    {
        if (schemaAttributes == null)
        {
            return null;
        }

        return schemaAttributes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NestedSchemaType{" +
                "schemaAttributes=" + schemaAttributes +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", formula='" + getFormula() + '\'' +
                ", queries=" + getQueries() +
                ", deprecated=" + getIsDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", meanings=" + getMeanings() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", assetClassifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        NestedSchemaType that = (NestedSchemaType) objectToCompare;
        return Objects.equals(schemaAttributes, that.schemaAttributes);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), schemaAttributes);
    }
}