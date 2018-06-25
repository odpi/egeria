/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaLink;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * SchemaLink defines a relationship between 2 SchemaElements.
 * It is used in network type schemas such as a graph.
 */
public class AssetSchemaLink extends AssetPropertyBase
{
    protected SchemaLink   schemaLinkBean;


    /**
     * Bean constructor
     *
     * @param schemaLinkBean bean containing all of the properties
     */
    public AssetSchemaLink(SchemaLink   schemaLinkBean)
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
    protected SchemaLink   getSchemaLinkBean()
    {
        return schemaLinkBean;
    }


    /**
     * Return the identifier for the schema link.
     *
     * @return String guid
     */
    public String getLinkGUID() { return schemaLinkBean.getLinkGUID(); }


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
     * @return AdditionalProperties
     */
    public AdditionalProperties getLinkProperties()
    {
        Map<String, Object>  linkProperties = schemaLinkBean.getLinkProperties();

        if (linkProperties == null)
        {
            return null;
        }
        else if (linkProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), linkProperties);
        }
    }


    /**
     * Return the GUIDs of the schema attributes that this link connects together.
     *
     * @return SchemaAttributeGUIDs   GUIDs for either end of the link   return as a list.
     */
    public List<String> getLinkedAttributeGUIDs()
    {
        List<String>   linkedAttributeGUIDs = schemaLinkBean.getLinkedAttributeGUIDs();

        if (linkedAttributeGUIDs == null)
        {
            return null;
        }
        else if (linkedAttributeGUIDs.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(linkedAttributeGUIDs);
        }
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