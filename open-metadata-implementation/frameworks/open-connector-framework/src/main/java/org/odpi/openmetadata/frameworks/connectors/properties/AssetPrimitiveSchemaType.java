/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaElement;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import java.util.Objects;

/**
 * PrimitiveSchemaElement describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
public class AssetPrimitiveSchemaType extends AssetSchemaType
{
    protected PrimitiveSchemaElement  primitiveSchemaElementBean;


    /**
     * Bean constructor
     *
     * @param primitiveSchemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetPrimitiveSchemaType(PrimitiveSchemaElement  primitiveSchemaElementBean,
                                    AssetMeanings           assetMeanings)
    {
        super(primitiveSchemaElementBean, assetMeanings);

        if (primitiveSchemaElementBean == null)
        {
            this.primitiveSchemaElementBean = new PrimitiveSchemaElement();
        }
        else
        {
            this.primitiveSchemaElementBean = primitiveSchemaElementBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param primitiveSchemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetPrimitiveSchemaType(AssetDescriptor         parentAsset,
                                    PrimitiveSchemaElement  primitiveSchemaElementBean,
                                    AssetMeanings           assetMeanings)
    {
        super(parentAsset, primitiveSchemaElementBean, assetMeanings);

        if (primitiveSchemaElementBean == null)
        {
            this.primitiveSchemaElementBean = new PrimitiveSchemaElement();
        }
        else
        {
            this.primitiveSchemaElementBean = primitiveSchemaElementBean;
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param templateSchemaElement template object to copy.
     */
    public AssetPrimitiveSchemaType(AssetDescriptor             parentAsset,
                                    AssetPrimitiveSchemaType templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.primitiveSchemaElementBean = new PrimitiveSchemaElement();
        }
        else
        {
            this.primitiveSchemaElementBean = templateSchemaElement.getPrimitiveSchemaElementBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return primitive element bean
     */
    protected PrimitiveSchemaElement  getPrimitiveSchemaElementBean()
    {
        return primitiveSchemaElementBean;
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String DataType
     */
    public String getDataType() { return primitiveSchemaElementBean.getDataType(); }


    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue() { return primitiveSchemaElementBean.getDefaultValue(); }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    @Override
    protected AssetSchemaType cloneAssetSchemaElement(AssetDescriptor  parentAsset)
    {
        return new AssetPrimitiveSchemaType(parentAsset, this);
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    @Override
    protected SchemaElement getSchemaElementBean()
    {
        return primitiveSchemaElementBean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return primitiveSchemaElementBean.toString();
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
        if (!(objectToCompare instanceof AssetPrimitiveSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetPrimitiveSchemaType that = (AssetPrimitiveSchemaType) objectToCompare;
        return Objects.equals(getPrimitiveSchemaElementBean(), that.getPrimitiveSchemaElementBean());
    }
}