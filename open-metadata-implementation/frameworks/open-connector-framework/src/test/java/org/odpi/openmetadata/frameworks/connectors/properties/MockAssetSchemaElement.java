/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.MockSchemaElement;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

/**
 * <p>
 *     The SchemaElement object provides a base class for the pieces that make up a schema for a data asset.
 *     A schema provides information about how the data is structured in the asset.  Schemas are typically
 *     described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 * </p>
 *     SchemaElement is an abstract class - used to enable the most accurate and precise mapping of the
 *     elements in a schema to the asset.
 *     <ul>
 *         <li>PrimitiveSchemaElement is for a leaf element in a schema.</li>
 *         <li>Schema is a complex structure of nested schema elements.</li>
 *         <li>MapSchemaElement is for an attribute of type Map</li>
 *     </ul>
 *     Most assets will be linked to a Schema.
 * <p>
 *     Schema elements can be linked to one another using SchemaLink.
 * </p>
 */
public class MockAssetSchemaElement extends AssetSchemaElement
{
    private SchemaElement schemaElementBean = null;

    /**
     * Bean constructor
     *
     * @param schemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public MockAssetSchemaElement(SchemaElement schemaElementBean,
                                  AssetMeanings assetMeanings)
    {
        super(schemaElementBean, assetMeanings);

        if (schemaElementBean == null)
        {
            this.schemaElementBean = new MockSchemaElement();
        }
        else
        {
            this.schemaElementBean = schemaElementBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param schemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     */
    public MockAssetSchemaElement(AssetDescriptor parentAsset,
                                  SchemaElement   schemaElementBean,
                                  AssetMeanings   assetMeanings)
    {
        super(parentAsset, schemaElementBean, assetMeanings);

        if (schemaElementBean == null)
        {
            this.schemaElementBean = new MockSchemaElement();
        }
        else
        {
            this.schemaElementBean = schemaElementBean;
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
    public MockAssetSchemaElement(AssetDescriptor  parentAsset, AssetSchemaElement templateSchema)
    {
        super(parentAsset, templateSchema);

        if (templateSchema == null)
        {
            this.schemaElementBean = new MockSchemaElement();
        }
        else
        {
            SchemaElement schemaElementBean =  templateSchema.getSchemaElementBean();

            if (schemaElementBean == null)
            {
                this.schemaElementBean = new MockSchemaElement();
            }
            else
            {
                this.schemaElementBean = schemaElementBean;
            }
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaElement
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    protected MockAssetSchemaElement cloneAssetSchemaElement(AssetDescriptor  parentAsset)
    {
        return new MockAssetSchemaElement(parentAsset, this);
    }


    /**
     * Return this schema element bean.  This method is needed because SchemaElement
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    protected SchemaElement getSchemaElementBean()
    {
        return this.schemaElementBean;
    }
}