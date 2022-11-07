/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.APISchemaType;

import java.util.Objects;

/**
 * An asset's schema provides information about how the asset structures the data it supports.
 * The NestedSchemaType object describes a nested structure of schema attributes and types.
 */
public  class DeployedAPISchemaType extends APISchemaType
{
    private static final long     serialVersionUID = 1L;

    protected APIOperations apiOperations = null;


    /**
     * Constructor used by the subclasses
     */
    protected DeployedAPISchemaType()
    {
        super();
    }



    /**
     * Bean constructor
     *
     * @param schemaBean bean containing the schema properties
     */
    public DeployedAPISchemaType(APISchemaType schemaBean)
    {
        super(schemaBean);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DeployedAPISchemaType(DeployedAPISchemaType template)
    {
        super(template);

        if (template != null)
        {
            this.apiOperations = template.getAPIOperations();
        }
    }


    /**
     * Return the list of schema attributes in this schema.
     *
     * @return SchemaAttributes
     */
    public APIOperations getAPIOperations()
    {
        if (apiOperations == null)
        {
            return null;
        }

        return apiOperations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DeployedAPISchemaType{" +
                "schemaAttributes=" + apiOperations +
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
        DeployedAPISchemaType that = (DeployedAPISchemaType) objectToCompare;
        return Objects.equals(apiOperations, that.apiOperations);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), apiOperations);
    }
}