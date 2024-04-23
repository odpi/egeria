/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.when;

public class GlossaryCategoryServiceTest extends GlossaryViewOmasBase {

    private final CategoryService underTest = new CategoryService();

    @Before
    public void before() throws Exception{
        super.before(underTest);
    }

    @Test
    public void getCategory() throws Exception{
        when(entitiesHandler.getEntityFromRepository(USER_ID, categories.get(0).getGUID(),
                                                     OpenMetadataProperty.GUID.name, CATEGORY_TYPE_NAME, null,
                                                     null, false, false, null,
                                                     null, "getCategory")).thenReturn(categories.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getCategory(USER_ID, SERVER_NAME, categories.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryCategoryProperties(categories.get(0), (GlossaryCategory) response.getResult().get(0));
    }

    @Test
    public void getAllCategories() throws Exception{
        when(entitiesHandler.getEntitiesByType(USER_ID, CATEGORY_TYPE_GUID, CATEGORY_TYPE_NAME, null,
                false, false, null, 0, 10, null,
                "getAllCategories")).thenReturn(categories);

        GlossaryViewEntityDetailResponse response = underTest.getAllCategories(USER_ID, SERVER_NAME, 0, 10);

        assertEquals(3, response.getResult().size());

        assertGlossaryCategoryProperties(categories.get(0), (GlossaryCategory)response.getResult().get(0));
        assertGlossaryCategoryProperties(categories.get(1), (GlossaryCategory)response.getResult().get(1));
        assertGlossaryCategoryProperties(categories.get(2), (GlossaryCategory)response.getResult().get(2));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));

    }

    @Test
    public void getCategoriesViaCategoryAnchorRelationships() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, glossaries.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 GLOSSARY_TYPE_NAME, CATEGORY_ANCHOR_RELATIONSHIP_GUID, CATEGORY_ANCHOR_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10000, null, "getCategoriesViaCategoryAnchorRelationships")).thenReturn(categories);

        GlossaryViewEntityDetailResponse response = underTest.getCategoriesViaCategoryAnchorRelationships(USER_ID, SERVER_NAME,
                glossaries.get(0).getGUID(), 0, 10000);

        assertEquals(3, response.getResult().size());

        assertGlossaryCategoryProperties(categories.get(0), (GlossaryCategory) response.getResult().get(0));
        assertGlossaryCategoryProperties(categories.get(1), (GlossaryCategory) response.getResult().get(1));
        assertGlossaryCategoryProperties(categories.get(2), (GlossaryCategory) response.getResult().get(2));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));
    }

    @Test
    public void getSubcategories() throws Exception{
       when(entitiesHandler.getAttachedEntities(USER_ID, categories.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                CATEGORY_TYPE_NAME, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_GUID, CATEGORY_HIERARCHY_LINK_RELATIONSHIP_NAME,
                                                null, null, null, 2,
                                                false, false, null, 0, 10000,
                                                null, "getSubcategories")).thenReturn(Arrays.asList(categories.get(1), categories.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getSubcategories(USER_ID, SERVER_NAME,
                categories.get(0).getGUID(), 0, 10000);

        assertEquals(2, response.getResult().size());

        assertGlossaryCategoryProperties(categories.get(1), (GlossaryCategory) response.getResult().get(0));
        assertGlossaryCategoryProperties(categories.get(2), (GlossaryCategory) response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getExternalGlossaryLinks() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, categories.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 CATEGORY_TYPE_NAME, LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_GUID, LIBRARY_CATEGORY_REFERENCE_RELATIONSHIP_NAME,
                                                 null, null, null, 0, false, false, 0, 10, null, "getExternalGlossaryLinks"))
                .thenReturn(Collections.singletonList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME,
                categories.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertExternalGlossaryLinkProperties(externalGlossaryLink, (ExternalGlossaryLink) response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    private void assertGlossaryCategoryProperties(EntityDetail expected, GlossaryCategory actual){
        assertEquals(expected.getGUID(), actual.getGuid());
        assertEquals(expected.getProperties().getPropertyValue("qualifiedName").valueAsString(), actual.getQualifiedName());
        assertEquals(expected.getProperties().getPropertyValue("displayName").valueAsString(), actual.getDisplayName());
        assertEquals(expected.getProperties().getPropertyValue("description").valueAsString(), actual.getDescription());
    }

}
