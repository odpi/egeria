/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.BoundedSchemaCategory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.BoundedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * The AssetBoundedSchemaType object provides schema information for sets and arrays.
 */
@Deprecated
public class AssetBoundedSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    @Deprecated
    protected BoundedSchemaType boundedSchemaTypeBean    = null;
    protected AssetSchemaType   boundedSchemaElementType = null;

    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetBoundedSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param boundedSchemaType bean containing the schema properties
     */
    public AssetBoundedSchemaType(BoundedSchemaType boundedSchemaType)
    {
        super(boundedSchemaType);

        if (boundedSchemaType == null)
        {
            this.boundedSchemaTypeBean = new BoundedSchemaType();
        }
        else
        {
            this.boundedSchemaTypeBean = boundedSchemaType;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param boundedSchemaType bean containing the schema properties
     */
    public AssetBoundedSchemaType(AssetDescriptor   parentAsset,
                                  BoundedSchemaType boundedSchemaType)
    {
        super(parentAsset, boundedSchemaType);

        if (boundedSchemaType == null)
        {
            this.boundedSchemaTypeBean = new BoundedSchemaType();
        }
        else
        {
            this.boundedSchemaTypeBean = boundedSchemaType;
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema is attached to.
     * @param template template object to copy.
     */
    public AssetBoundedSchemaType(AssetDescriptor parentAsset, AssetBoundedSchemaType template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.boundedSchemaTypeBean = new BoundedSchemaType();
        }
        else
        {
            this.boundedSchemaTypeBean = template.boundedSchemaTypeBean;
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    @Override
    protected AssetSchemaType cloneAssetSchemaType(AssetDescriptor parentAsset)
    {
        return new AssetBoundedSchemaType(parentAsset, this);
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(BoundedSchemaType bean)
    {
        super.setBean(bean);
        this.boundedSchemaTypeBean = bean;
    }


    /**
     * Return the bean that contains the properties of the schema.
     *
     * @return schema bean
     */
    protected SchemaType getSchemaBean()
    {
        return  boundedSchemaTypeBean;
    }


    /**
     * Return the category of the schema.
     *
     * @return BoundedSchemaCategory
     */
    public BoundedSchemaCategory getBoundedSchemaCategory() { return boundedSchemaTypeBean.getBoundedSchemaCategory(); }


    /**
     * Return the type of the elements in the schema.
     *
     * @return AssetSchemaType
     */
    public AssetSchemaType getBoundedSchemaElementType()
    {
        if (boundedSchemaTypeBean.getElementType() == null)
        {
            return null;
        }
        else
        {
            return AssetSchemaType.createAssetSchemaType(parentAsset, boundedSchemaTypeBean.getElementType());
        }
    }


    /**
     * Return the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.
     *
     * @return int maximum number of elements allowed
     */
    public int getMaximumElements()
    {
        return boundedSchemaTypeBean.getMaximumElements();
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaType
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    @Override
    protected SchemaType getSchemaTypeBean()
    {
        return boundedSchemaTypeBean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return boundedSchemaTypeBean.toString();
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
        if (!(objectToCompare instanceof AssetBoundedSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetBoundedSchemaType that = (AssetBoundedSchemaType) objectToCompare;
        return Objects.equals(getSchemaBean(), that.getSchemaBean());
    }
}