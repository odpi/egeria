/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRelationshipRESTService;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class AssetCatalogRelationshipResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";
    private static final String FIST_END_GUID = "b1c497ce.60641b50.0v9mgsb1m.9vbkmkr";
    private static final String SECOND_END_GUID = "6662c0f2.e1b1ec6c.00263shmd.6u8o50l.dhhet6.5a7eo9o6q9j8vikjs6ds4";
    private static final String RELATIONSHIP_TYPE_NAME = "SemanticAssignment";

    @Mock
    private AssetCatalogRelationshipRESTService assetCatalogRelationshipService;

    @InjectMocks
    private AssetCatalogRelationshipResource assetCatalogRelationshipResource;

    @Test
    void testGetRelationshipBetweenEntities() {
        assetCatalogRelationshipResource.getRelationshipBetweenEntities(SERVER_NAME, USER,
                FIST_END_GUID,
                SECOND_END_GUID,
                RELATIONSHIP_TYPE_NAME);

        verify(assetCatalogRelationshipService, atLeastOnce()).getRelationshipBetweenEntities(SERVER_NAME,
                USER,
                FIST_END_GUID,
                SECOND_END_GUID,
                RELATIONSHIP_TYPE_NAME);

    }
}