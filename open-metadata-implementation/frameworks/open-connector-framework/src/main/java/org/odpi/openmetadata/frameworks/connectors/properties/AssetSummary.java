/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;


/**
 * AssetSummary holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>type - metadata type information for the asset</li>
 *     <li>guid - globally unique identifier for the asset</li>
 *     <li>url - external link for the asset</li>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (Sourced from qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the asset's qualifiedName
 *     for use on user interfaces and messages.   The asset's displayName should be only be used for audit logs and error
 *     messages if the qualifiedName is not set. (Sourced from displayName attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>owner - name of the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerTypeName - name of the element type identifying the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerPropertyName - name of the property identifying person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership or Ownership attached to Asset - model 0445)</li>
 *     <li>ownerType - type of the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership attached to Asset - model 0445)</li>
 *     <li>zoneMembership - list of governance zones assigned to the asset</li>
 *     <li>classifications - list of classifications assigned to the asset</li>
 * </ul>
 */
public class AssetSummary extends Asset
{
    /**
     * Default constructor only for subclasses
     */
    protected AssetSummary()
    {
        super();
    }


    /**
     * Bean constructor - initializes AssetSummary using a bean returned by the REST interface.
     *
     * @param assetBean asset properties bean
     */
    public AssetSummary(Asset assetBean)
    {
        super(assetBean);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param templateAssetSummary template values for asset summary
     */
    public AssetSummary(AssetSummary templateAssetSummary)
    {
        super(templateAssetSummary);
    }
}