/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables the testing of ConnectedAssetProperties
 */
public class MockConnectedAssetProperties extends ConnectedAssetProperties
{
    private final ExternalIdentifiers    externalIdentifiers    = null;
    private final RelatedMediaReferences relatedMediaReferences = null;
    private final NoteLogs               noteLogs               = null;
    private final ExternalReferences     externalReferences     = null;
    private final Connections            connections            = null;
    private final Licenses               licenses               = null;
    private final Certifications         certifications         = null;


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
        InformalTags informalTags = new MockInformalTags( 15, 50);
        Likes        likes        = new MockLikes( 15, 50);
        Ratings      ratings      = new MockRatings( 15, 50);
        Comments      comments = new MockComments(15, 50);
        List<Meaning> meanings = new ArrayList<>();
        SchemaType    schema   = new PrimitiveSchemaType(null);
        AssetFeedback feedback               = new AssetFeedback(informalTags,
                                                                 likes,
                                                                 ratings,
                                                                 comments);
        Locations    knownLocations = new MockLocations( 15, 50);
        AssetLineage lineage        = new AssetLineage();
        RelatedAssets relatedAssets = new MockRelatedAssets( 15, 50);

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
