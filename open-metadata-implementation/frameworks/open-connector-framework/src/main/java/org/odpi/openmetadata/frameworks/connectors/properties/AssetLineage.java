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
 * Currently lineage is not implemented in the ConnectedAssetDetails interface because more design work is needed.
 * This class is therefore a placeholder for lineage information.
 */
public class AssetLineage extends AssetPropertyElementBase
{
    /**
     * Typical constructor.
     */
    public AssetLineage()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateLineage lineage object to copy.
     */
    public AssetLineage(AssetLineage templateLineage)
    {
        super(templateLineage);

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