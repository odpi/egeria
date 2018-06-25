/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Schema;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;

/**
 * The Schema object provides information about how the asset structures the data it supports.  Schemas are typically
 * described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 *
 * The schema object can be used to represent a Struct, Array, Set or Map.
 * <ul>
 *     <li>
 *         A Struct has an ordered list of attributes - the position of an attribute is set up as one of its properties.
 *     </li>
 *     <li>
 *         An Array has one schema attribute and a maximum size plus element count.
 *     </li>
 *     <li>
 *         A Set also has one schema attribute and a maximum size plus element count.
 *     </li>
 *     <li>
 *         A Map is a Set of MapSchemaElements
 *     </li>
 * </ul>
 */
public class AssetSchema extends AssetSchemaElement
{
    /*
     * Properties specific to a Schema
     */
    protected Schema                schemaBean;
    protected AssetSchemaAttributes schemaAttributes;
    protected AssetSchemaLinks      schemaLinks;


    /**
     * Bean constructor
     *
     * @param schemaBean bean containing the schema properties
     * @param assetMeanings iterator for the asset assetMeanings
     * @param schemaAttributes the attributes that make up this schema
     * @param schemaLinks the links to other schema
     */
    public AssetSchema(Schema                schemaBean,
                       AssetMeanings         assetMeanings,
                       AssetSchemaAttributes schemaAttributes,
                       AssetSchemaLinks      schemaLinks)
    {
        super(schemaBean, assetMeanings);

        if (schemaBean == null)
        {
            this.schemaBean = new Schema();
        }
        else
        {
            this.schemaBean = schemaBean;
        }

        if (schemaAttributes == null)
        {
            this.schemaAttributes = null;
        }
        else
        {
            this.schemaAttributes = schemaAttributes.cloneIterator(super.getParentAsset());
        }

        if (schemaLinks == null)
        {
            this.schemaLinks = null;
        }
        else
        {
            this.schemaLinks = schemaLinks.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaBean bean containing the schema properties
     * @param assetMeanings iterator for the asset assetMeanings
     * @param schemaAttributes the attributes that make up this schema
     * @param schemaLinks the links to other schema
     */
    public AssetSchema(AssetDescriptor       parentAsset,
                       Schema                schemaBean,
                       AssetMeanings         assetMeanings,
                       AssetSchemaAttributes schemaAttributes,
                       AssetSchemaLinks      schemaLinks)
    {
        super(parentAsset, schemaBean, assetMeanings);

        if (schemaBean == null)
        {
            this.schemaBean = new Schema();
        }
        else
        {
            this.schemaBean = schemaBean;
        }

        if (schemaAttributes == null)
        {
            this.schemaAttributes = null;
        }
        else
        {
            this.schemaAttributes = schemaAttributes.cloneIterator(super.getParentAsset());
        }

        if (schemaLinks == null)
        {
            this.schemaLinks = null;
        }
        else
        {
            this.schemaLinks = schemaLinks.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema is attached to.
     * @param templateSchema template object to copy.
     */
    public AssetSchema(AssetDescriptor  parentAsset, AssetSchema templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.schemaBean = new Schema();
            this.schemaAttributes = null;
            this.schemaLinks = null;
        }
        else
        {
            this.schemaBean = templateSchema.getSchemaBean();

            AssetSchemaAttributes schemaAttributes = templateSchema.getSchemaAttributes();

            if (schemaAttributes == null)
            {
                this.schemaAttributes = null;
            }
            else
            {
                this.schemaAttributes = schemaAttributes.cloneIterator(super.getParentAsset());
            }

            AssetSchemaLinks schemaLinks = templateSchema.getSchemaLinks();

            if (schemaLinks == null)
            {
                this.schemaLinks = null;
            }
            else
            {
                this.schemaLinks = schemaLinks.cloneIterator(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean that contains the properties of the schema.
     *
     * @return schema bean
     */
    protected Schema  getSchemaBean()
    {
        return  schemaBean;
    }


    /**
     * Return the type of the schema.
     *
     * @return SchemaType
     */
    public SchemaType getSchemaType() { return schemaBean.getSchemaType(); }


    /**
     * Return the list of schema attributes in this schema.
     *
     * @return SchemaAttributes
     */
    public AssetSchemaAttributes getSchemaAttributes()
    {
        if (schemaAttributes == null)
        {
            return null;
        }
        else
        {
            return schemaAttributes.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Return the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.  For a STRUCT the max elements are the number of elements in
     * the asset attributes and this method returns 0,
     *
     * @return int maximum number of elements allowed
     */
    public int getMaximumElements()
    {
        if (schemaBean.getSchemaType() == SchemaType.STRUCT)
        {
            return 0;
        }

        return schemaBean.getMaximumElements();
    }


    /**
     * Return a list of any links that exist between the schema attributes of this schema (or others).
     * These links are typically used for network type schemas such as a grpah schema - or may be used to show
     * linkage to an element in another schema.
     *
     * @return SchemaLinks list of linked schema attributes
     */
    public AssetSchemaLinks getSchemaLinks()
    {
        if (schemaLinks == null)
        {
            return null;
        }
        else
        {
            return schemaLinks.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaElement
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    @Override

    protected  AssetSchemaElement cloneAssetSchemaElement(AssetDescriptor  parentAsset)
    {
        return new AssetSchema(parentAsset, this);
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    @Override
    protected   SchemaElement getSchemaElementBean()
    {
        return schemaBean;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return schemaBean.toString();
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
        if (!(objectToCompare instanceof AssetSchema))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchema that = (AssetSchema) objectToCompare;
        return Objects.equals(getSchemaBean(), that.getSchemaBean()) &&
                Objects.equals(getSchemaAttributes(), that.getSchemaAttributes()) &&
                Objects.equals(getSchemaLinks(), that.getSchemaLinks());
    }
}