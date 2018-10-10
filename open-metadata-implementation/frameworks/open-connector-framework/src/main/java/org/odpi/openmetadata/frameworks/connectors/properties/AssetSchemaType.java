/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *     The AssetSchemaType object provides a base class for the pieces that make up a schema for a data asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 * </p>
 *     SchemaElement is an abstract class - used to enable the most accurate and precise mapping of the
 *     elements in a schema to the asset.
 *     <ul>
 *         <li>AssetPrimitiveSchemaType is for a leaf element in a schema.</li>
 *         <li>AssetComplexSchemaType is a complex structure of nested schema elements.</li>
 *         <li>AssetMapSchemaElement is for an attribute of type Map</li>
 *         <li>DerivedSchemaElement is for an attribute that is derived from other schema attributes</li>
 *     </ul>
 *     Most assets will be linked to a AssetComplexSchemaType.
 * <p>
 *     Schema elements can be linked to one another using SchemaLink.
 * </p>
 */
public abstract class AssetSchemaType extends AssetReferenceable
{
    protected AssetMeanings assetMeanings;

    /**
     * Bean constructor
     *
     * @param schemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetSchemaType(SchemaElement schemaElementBean,
                           AssetMeanings assetMeanings)
    {
        super(schemaElementBean);

        if (assetMeanings == null)
        {
            this.assetMeanings = null;
        }
        else
        {
            this.assetMeanings = assetMeanings.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public AssetSchemaType(AssetDescriptor parentAsset,
                           SchemaElement   schemaElementBean,
                           AssetMeanings   assetMeanings)
    {
        super(parentAsset, schemaElementBean);

        if (assetMeanings == null)
        {
            this.assetMeanings = null;
        }
        else
        {
            this.assetMeanings = assetMeanings.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param templateSchema template object to copy.
     */
    public AssetSchemaType(AssetDescriptor  parentAsset, AssetSchemaType templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.assetMeanings = null;
        }
        else
        {
            AssetMeanings assetMeanings = templateSchema.getAssetMeanings();
            if (assetMeanings == null)
            {
                this.assetMeanings = null;
            }
            else
            {
                this.assetMeanings = assetMeanings.cloneIterator(super.getParentAsset());
            }
        }
    }


    /**
     * Return a list of the glossary terms attached to this referenceable object.  Null means no terms available.
     *
     * @return list of glossary terms (summary)
     */
    public AssetMeanings getAssetMeanings()
    {
        if (assetMeanings == null)
        {
            return null;
        }
        else
        {
            return assetMeanings.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    protected abstract AssetSchemaType cloneAssetSchemaElement(AssetDescriptor  parentAsset);


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    protected abstract SchemaElement getSchemaElementBean();


    /**
     * Return the version number of the schema element - null means no version number.
     *
     * @return String version number
     */
    public String getVersionNumber()
    {
        return this.getSchemaElementBean().getVersionNumber();
    }


    /**
     * Return the name of the author of the schema element.  Null means the author is unknown.
     *
     * @return String author name
     */
    public String getAuthor()
    {
        return this.getSchemaElementBean().getAuthor();
    }


    /**
     * Return the usage guidance for this schema element. Null means no guidance available.
     *
     * @return String usage guidance
     */
    public String getUsage()
    {
        return this.getSchemaElementBean().getUsage();
    }


    /**
     * Return the format (encoding standard) used for this schema.  It may be XML, JSON, SQL DDL or something else.
     * Null means the encoding standard is unknown or there are many choices.
     *
     * @return String encoding standard
     */
    public String getEncodingStandard()
    {
        return this.getSchemaElementBean().getEncodingStandard();
    }


    /**
     * Return the properties that come from the technology-specific subtypes of SchemaElement.
     *
     * @return AdditionalProperties object
     */
    public AdditionalProperties getSchemaProperties()
    {
        Map<String, Object> schemaProperties = this.getSchemaElementBean().getSchemaProperties();

        if (schemaProperties == null)
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), schemaProperties);
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
        return "AssetSchemaType{" +
                "assetMeanings=" + assetMeanings +
                ", referenceableBean=" + referenceableBean +
                ", parentAsset=" + parentAsset +
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
        if (!(objectToCompare instanceof AssetSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaType that = (AssetSchemaType) objectToCompare;
        return Objects.equals(getAssetMeanings(), that.getAssetMeanings());
    }
}