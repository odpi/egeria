/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GraphQueryDepthFVT checks that {@code GetOptions.graphQueryDepth} controls whether an
 * {@code OpenMetadataRootElement} comes back with its relationships populated: a
 * {@code graphQueryDepth} of zero should suppress every relationship, while the default (non-zero) depth
 * should include them.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class GraphQueryDepthFVT
{
    @Test
    void graphQueryDepthZeroSuppressesRelationships() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();

        String parentQualifiedName = QueryFvtTestSupport.newQualifiedName("GraphDepthParent");
        String memberQualifiedName = QueryFvtTestSupport.newQualifiedName("GraphDepthMember");

        NewElementOptions parentOptions = new NewElementOptions();
        parentOptions.setIsOwnAnchor(true);

        CollectionProperties parentProperties = new CollectionProperties();
        parentProperties.setQualifiedName(parentQualifiedName);
        parentProperties.setDisplayName("query-fvt Graph Depth Parent Collection");

        String parentGUID = collectionClient.createCollection(parentOptions, null, parentProperties, null);

        NewElementOptions memberOptions = new NewElementOptions();
        memberOptions.setIsOwnAnchor(true);

        CollectionProperties memberProperties = new CollectionProperties();
        memberProperties.setQualifiedName(memberQualifiedName);
        memberProperties.setDisplayName("query-fvt Graph Depth Member Collection");

        String memberGUID = collectionClient.createCollection(memberOptions, null, memberProperties, null);

        try
        {
            collectionClient.addToCollection(parentGUID, memberGUID, new MakeAnchorOptions(), new CollectionMembershipProperties());

            GetOptions defaultDepthOptions = new GetOptions();

            assertTrue(defaultDepthOptions.getGraphQueryDepth() > 0,
                       "GetOptions' default graphQueryDepth should be greater than zero, otherwise this test proves nothing");

            OpenMetadataRootElement withRelationships = collectionClient.getCollectionByGUID(parentGUID, defaultDepthOptions);

            assertNotNull(withRelationships);

            List<RelatedMetadataElementSummary> membersWithDefaultDepth = withRelationships.getCollectionMembers();

            assertNotNull(membersWithDefaultDepth,
                          "With the default graphQueryDepth, the parent collection's members list should be populated");
            assertEquals(1, membersWithDefaultDepth.size());
            assertEquals(memberGUID, membersWithDefaultDepth.get(0).getRelatedElement().getElementHeader().getGUID());

            GetOptions zeroDepthOptions = new GetOptions();
            zeroDepthOptions.setGraphQueryDepth(0);

            OpenMetadataRootElement withoutRelationships = collectionClient.getCollectionByGUID(parentGUID, zeroDepthOptions);

            assertNotNull(withoutRelationships);

            List<RelatedMetadataElementSummary> membersWithZeroDepth = withoutRelationships.getCollectionMembers();

            assertTrue((membersWithZeroDepth == null) || membersWithZeroDepth.isEmpty(),
                       "With graphQueryDepth=0, the parent collection's members list should be empty, even though the " +
                               "relationship genuinely exists (as shown by the default-depth query above)");

            // The element's own identity and properties should still be returned - only the relationships are suppressed.
            assertEquals(parentGUID, withoutRelationships.getElementHeader().getGUID());
        }
        finally
        {
            collectionClient.detachCollection(memberGUID, parentGUID, new org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions());
            QueryFvtTestSupport.purgeElement(openMetadataStore, memberGUID);
            QueryFvtTestSupport.purgeElement(openMetadataStore, parentGUID);
        }
    }
}
