/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaElement;

import java.util.Objects;

/**
 * The AssetSchemaElement object provides a base class for the pieces that make up a schema for a data asset.
 * A schema provides information about how the data is structured in the asset.
 */
public abstract class AssetSchemaElement extends AssetReferenceable
{
    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetSchemaElement(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Bean constructor
     *
     * @param schemaElementBean bean containing the schema element properties
     */
    public AssetSchemaElement(SchemaElement schemaElementBean)
    {
        super(schemaElementBean);
    }


    /**
     * Bean constructor with parent.
     *
     * @param parentAsset descriptor of asset that this property relates to.
     * @param schemaElementBean bean containing the schema element properties
     */
    public AssetSchemaElement(AssetDescriptor parentAsset, SchemaElement schemaElementBean)
    {
        super(parentAsset, schemaElementBean);
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param template template object to copy.
     */
    public AssetSchemaElement(AssetDescriptor parentAsset, AssetSchemaElement template)
    {
        super(parentAsset, template);
    }
}