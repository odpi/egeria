/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


/**
 * Verify the secrets collection names column of the user identities data set.
 */
public class UserIdentitiesTabularDataSetConnectorTest
{
    private RelatedMetadataElementSummary collection(String resourceName, String displayName)
    {
        SecretsCollectionProperties properties = new SecretsCollectionProperties();

        properties.setResourceName(resourceName);
        properties.setDisplayName(displayName);

        MetadataElementSummary element = new MetadataElementSummary();
        element.setProperties(properties);

        RelatedMetadataElementSummary related = new RelatedMetadataElementSummary();
        related.setRelatedElement(element);

        return related;
    }


    private OpenMetadataRootElement userIdentity(List<RelatedMetadataElementSummary> collections)
    {
        OpenMetadataRootElement userIdentity = new OpenMetadataRootElement();

        userIdentity.setConfiguredInSecurityCollections(collections);

        return userIdentity;
    }


    /**
     * A user with accounts in named collections lists them sorted.  This failed with a NullPointerException
     * because the TreeSet of names was asked to remove null.
     */
    @Test
    public void testNamedCollections()
    {
        assertEquals(UserIdentitiesTabularDataSetConnector.getSecretsCollectionNames(userIdentity(List.of(collection("zeta", null),
                                                                                                             collection(null, "alpha"),
                                                                                                             collection("zeta", "other")))),
                     "alpha,zeta");
    }


    /**
     * A collection with neither a resource name nor a display name is skipped.
     */
    @Test
    public void testUnnamedCollection()
    {
        List<RelatedMetadataElementSummary> collections = new ArrayList<>();

        collections.add(collection(null, null));
        collections.add(null);
        collections.add(new RelatedMetadataElementSummary());

        assertNull(UserIdentitiesTabularDataSetConnector.getSecretsCollectionNames(userIdentity(collections)));

        collections.add(collection("beta", null));

        assertEquals(UserIdentitiesTabularDataSetConnector.getSecretsCollectionNames(userIdentity(collections)), "beta");
    }


    /**
     * A user with no accounts has no names.
     */
    @Test
    public void testNoCollections()
    {
        assertNull(UserIdentitiesTabularDataSetConnector.getSecretsCollectionNames(userIdentity(null)));
        assertNull(UserIdentitiesTabularDataSetConnector.getSecretsCollectionNames(userIdentity(new ArrayList<>())));
    }
}
