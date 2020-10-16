/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttributeRelationship;

import java.util.Map;
import java.util.Objects;

/**
 * AssetSchemaAttributeRelationship defines a relationship between 2 SchemaAttributes.
 * It is used in network type schemas such as a graph and for foreign keys in a relational database schema.
 */
public class AssetSchemaAttributeRelationship extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected SchemaAttributeRelationship schemaAttributeRelationshipBean;


    /**
     * Bean constructor
     *
     * @param schemaAttributeRelationshipBean bean containing all of the properties
     */
    public AssetSchemaAttributeRelationship(SchemaAttributeRelationship schemaAttributeRelationshipBean)
    {
        super(null);

        if (schemaAttributeRelationshipBean == null)
        {
            this.schemaAttributeRelationshipBean = new SchemaAttributeRelationship();
        }
        else
        {
            this.schemaAttributeRelationshipBean = schemaAttributeRelationshipBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor of parent asset
     * @param schemaAttributeRelationship bean containing all of the properties
     */
    public AssetSchemaAttributeRelationship(AssetDescriptor             parentAsset,
                                            SchemaAttributeRelationship schemaAttributeRelationship)
    {
        super(parentAsset);

        if (schemaAttributeRelationship == null)
        {
            this.schemaAttributeRelationshipBean = new SchemaAttributeRelationship();
        }
        else
        {
            this.schemaAttributeRelationshipBean = schemaAttributeRelationship;
        }
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param parentAsset   descriptor of parent asset
     * @param template   template object to copy
     */
    public AssetSchemaAttributeRelationship(AssetDescriptor parentAsset, AssetSchemaAttributeRelationship template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaAttributeRelationshipBean = new SchemaAttributeRelationship();
        }
        else
        {
            this.schemaAttributeRelationshipBean = template.getSchemaAttributeRelationshipBean();
        }
    }


    /**
     * Return the bean with all of the properties
     *
     * @return schema link bean
     */
    protected SchemaAttributeRelationship getSchemaAttributeRelationshipBean()
    {
        return schemaAttributeRelationshipBean;
    }


    /**
     * Return the type of the relationship.
     *
     * @return String link type
     */
    public String getLinkType() { return schemaAttributeRelationshipBean.getLinkType(); }


    /**
     * Return the list of properties associated with this schema link.
     *
     * @return map of properties
     */
    public  Map<String, Object> getLinkProperties()
    {
        return schemaAttributeRelationshipBean.getLinkProperties();
    }


    /**
     * Return the GUID of the schema attribute that this link connects together.
     *
     * @return  unique identifier
     */
    public String getLinkedAttributeGUID()
    {
        return schemaAttributeRelationshipBean.getLinkedAttributeGUID();
    }


    /**
     * Return the name of the schema type that this link connects together.
     *
     * @return  unique name
     */
    public String getLinkedAttributeName()
    {
        return schemaAttributeRelationshipBean.getLinkedAttributeName();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return schemaAttributeRelationshipBean.toString();
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
        if (!(objectToCompare instanceof AssetSchemaAttributeRelationship))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaAttributeRelationship that = (AssetSchemaAttributeRelationship) objectToCompare;
        return Objects.equals(getSchemaAttributeRelationshipBean(), that.getSchemaAttributeRelationshipBean());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return schemaAttributeRelationshipBean.hashCode();
    }
}