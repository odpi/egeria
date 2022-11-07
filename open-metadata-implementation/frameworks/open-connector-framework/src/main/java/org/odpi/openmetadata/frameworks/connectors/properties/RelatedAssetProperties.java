/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;


/**
 * RelatedAssetProperties returns detailed information about an asset that is related to an asset
 * that is linked to the original anchor asset with a relationship.
 *
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 *
 * The RelatedAssetProperties returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> - used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> - used to display all the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> - used to define the broader context for the asset</li>
 * </ul>
 *
 * RelatedAssetProperties is a base class for the asset information that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public abstract class RelatedAssetProperties extends RelatedAsset
{
    private static final long     serialVersionUID = 1L;

    /*
     * AssetUniverse extends AssetDetails which extends AssetSummary.  The interaction with the metadata repository
     * pulls the asset universe in one single network interaction and the caller can then explore the metadata
     * property by property without incurring many network interactions (unless there are too many instances
     * of a particular type of property and one of the iterators is forced to use paging).
     *
     * If null is returned, the caller is not linked to a metadata repository.
     */
    protected AssetUniverse   assetProperties = null;


    /**
     * Default constructor used by subclasses
     */
    protected RelatedAssetProperties()
    {
        super();
    }


    /**
     * Typical constructor.
     *
     * @param relatedAsset asset to extract the full set of properties.
     */
    public RelatedAssetProperties(RelatedAsset relatedAsset)
    {
        super(relatedAsset);
    }


    /**
     * Copy/clone constructor*
     *
     * @param templateProperties template to copy
     */
    public RelatedAssetProperties(RelatedAssetProperties templateProperties)
    {
        super (templateProperties);

        if (templateProperties != null)
        {
            AssetUniverse templateAssetUniverse = templateProperties.getAssetUniverse();
            if (templateAssetUniverse != null)
            {
                assetProperties = new AssetUniverse(templateAssetUniverse);
            }
        }
    }


    /**
     * Returns the summary information organized in the assetSummary structure.
     *
     * @return AssetSummary summary object
     */
    public AssetSummary getAssetSummary() { return assetProperties; }



    /**
     * Returns detailed information about the asset organized in the assetDetail structure.
     *
     * @return AssetDetail detail object
     */
    public AssetDetail getAssetDetail() { return assetProperties; }


    /**
     * Returns all the detail of the asset and information connected to it in organized in the assetUniverse
     * structure.
     *
     * @return AssetUniverse universe object
     */
    public AssetUniverse getAssetUniverse() { return assetProperties; }


    /**
     * Request the values in the RelatedAssetProperties are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException there is a problem connecting to the server to retrieve metadata.
     * @throws UserNotAuthorizedException the userId associated with the connector is not authorized to
     *                                    access the asset properties.
     */
    public abstract void refresh() throws PropertyServerException, UserNotAuthorizedException;


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedAssetProperties{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + getMeanings() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", relationshipName='" + getRelationshipName() + '\'' +
                       ", attributeName='" + getAttributeName() + '\'' +
                       '}';
    }
}

