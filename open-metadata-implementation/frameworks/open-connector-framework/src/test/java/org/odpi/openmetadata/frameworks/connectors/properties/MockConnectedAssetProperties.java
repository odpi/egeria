/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables the testing of ConnectedAssetProperties
 */
public class MockConnectedAssetProperties extends ConnectedAssetProperties
{
    private static final long     serialVersionUID = 1L;

    private AssetExternalIdentifiers    externalIdentifiers    = null;
    private AssetRelatedMediaReferences relatedMediaReferences = null;
    private AssetNoteLogs               noteLogs               = null;
    private AssetExternalReferences     externalReferences     = null;
    private AssetConnections            connections            = null;
    private AssetLicenses               licenses               = null;
    private AssetCertifications         certifications         = null;


    /**
     * Typical constructor.
     */
    public MockConnectedAssetProperties()
    {
        /*
         * Nothing to do except initialize superclass.
         */
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template to copy.
     */
    public MockConnectedAssetProperties(ConnectedAssetProperties templateProperties)
    {
        super(templateProperties);
    }


    /**
     * Request the values in the ConnectedAssetProperties are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException there is a problem connecting to the server to retrieve metadata.
     */
    public void refresh() throws PropertyServerException
    {
        AssetInformalTags  informalTags = new MockAssetInformalTags(null, 15, 50);
        AssetLikes         likes        = new MockAssetLikes(null, 15, 50);
        AssetRatings       ratings      = new MockAssetRatings(null, 15, 50);
        AssetComments      comments     = new MockAssetComments(null, 15, 50);
        List<AssetMeaning> meanings     = new ArrayList<>();
        AssetSchemaType    schema       = new AssetPrimitiveSchemaType((PrimitiveSchemaType)null);
        AssetFeedback feedback               = new AssetFeedback(null,
                                                                 informalTags,
                                                                 likes,
                                                                 ratings,
                                                                 comments);
        AssetLocations     knownLocations = new MockAssetLocations(null, 15, 50);
        AssetLineage       lineage        = new AssetLineage();
        AssetRelatedAssets relatedAssets  = new MockRelatedAssets(null, 15, 50);

        super.assetProperties    = new AssetUniverse(new Asset(),
                                                     externalIdentifiers,
                                                     relatedMediaReferences,
                                                     noteLogs,
                                                     externalReferences,
                                                     connections,
                                                     licenses,
                                                     certifications,
                                                     meanings,
                                                     schema,
                                                     feedback,
                                                     knownLocations,
                                                     lineage,
                                                     relatedAssets);
    }

}
