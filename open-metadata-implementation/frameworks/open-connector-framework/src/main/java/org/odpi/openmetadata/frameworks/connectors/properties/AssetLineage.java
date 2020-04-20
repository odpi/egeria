/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


/**
 * Lineage shows the origin of the connected asset.  It covers:
 * <ul>
 *     <li>Design lineage - the known data movement and data stores that can supply data to this asset.</li>
 *     <li>Operational lineage - showing the jobs that ran to create this asset</li>
 * </ul>
 *
 * Currently lineage is not implemented in the ConnectedAssetProperties interface because more design work is needed.
 * This class is therefore a placeholder for lineage information.
 */
public class AssetLineage extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    protected AssetLineage()
    {
        super(null);
    }


    /**
     * Typical constructor.
     *
     * @param parentAsset description of the asset that this lineage is attached to.
     */
    public AssetLineage(AssetDescriptor  parentAsset)
    {
        /*
         * Save descriptor of the asset that this lineage is attached to
         */
        super(parentAsset);
    }


    /**
     * Copy/clone constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the lineage clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this lineage is attached to.
     * @param templateLineage lineage object to copy.
     */
    public AssetLineage(AssetDescriptor  parentAsset, AssetLineage templateLineage)
    {
        super(parentAsset, templateLineage);

        /*
         * The open lineage design is still in progress so for the time being, this object does not do anything
         * useful
         */
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetLineage{}";
    }
}