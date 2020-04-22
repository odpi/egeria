/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SchemaLink defines a relationship between 2 SchemaElements.
 * It is used in network type schemas such as a graph.
 */
public class AssetSchemaLink extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected SchemaLink schemaLinkBean;


    /**
     * Bean constructor
     *
     * @param schemaLinkBean bean containing all of the properties
     */
    public AssetSchemaLink(SchemaLink schemaLinkBean)
    {
        super(null);

        if (schemaLinkBean == null)
        {
            this.schemaLinkBean = new SchemaLink();
        }
        else
        {
            this.schemaLinkBean = schemaLinkBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor of parent asset
     * @param schemaLinkBean bean containing all of the properties
     */
    public AssetSchemaLink(AssetDescriptor parentAsset,
                           SchemaLink      schemaLinkBean)
    {
        super(parentAsset);

        if (schemaLinkBean == null)
        {
            this.schemaLinkBean = new SchemaLink();
        }
        else
        {
            this.schemaLinkBean = schemaLinkBean;
        }
    }


    /**
     * Copy/clone constructor   makes a copy of the supplied object.
     *
     * @param parentAsset   descriptor of parent asset
     * @param template   template object to copy
     */
    public AssetSchemaLink(AssetDescriptor parentAsset, AssetSchemaLink template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaLinkBean = new SchemaLink();
        }
        else
        {
            this.schemaLinkBean = template.getSchemaLinkBean();
        }
    }


    /**
     * Return the bean with all of the properties
     *
     * @return schema link bean
     */
    protected SchemaLink getSchemaLinkBean()
    {
        return schemaLinkBean;
    }


    /**
     * Return the identifier for the schema link.
     *
     * @return String guid
     */
    public String getLinkGUID() { return schemaLinkBean.getGUID(); }


    /**
     * Return the type of the link   this is related to the type of the schema it is a part of.
     *
     * @return String link type
     */
    public String getLinkType() { return schemaLinkBean.getLinkType(); }


    /**
     * Return the name of this link
     *
     * @return String name
     */
    public String getLinkName() { return schemaLinkBean.getLinkName(); }


    /**
     * Return the list of properties associated with this schema link.
     *
     * @return map of properties
     */
    public  Map<String, String> getLinkProperties()
    {
        return schemaLinkBean.getLinkProperties();
    }


    /**
     * Return the GUID of the schema type that this link connects together.
     *
     * @return  unique identifier
     */
    public String getLinkedSchemaTypeGUID()
    {
        return schemaLinkBean.getLinkedSchemaTypeGUID();
    }


    /**
     * Return the name of the schema type that this link connects together.
     *
     * @return  unique name
     */
    public String getLinkedSchemaTypeName()
    {
        return schemaLinkBean.getLinkedSchemaTypeName();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return schemaLinkBean.toString();
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
        if (!(objectToCompare instanceof AssetSchemaLink))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaLink that = (AssetSchemaLink) objectToCompare;
        return Objects.equals(getSchemaLinkBean(), that.getSchemaLinkBean());
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return schemaLinkBean.hashCode();
    }
}