/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GlossaryServiceTest extends GlossaryViewOmasBase {

    private final GlossaryService underTest = new GlossaryService();
    private static final String GUID = "guid";

    @Before
    public void before() throws Exception{
        super.before(underTest);
    }

    @Test
    public void getGlossary() throws Exception{
        when(entitiesHandler.getEntityFromRepository(USER_ID, glossaries.get(0).getGUID(),
                                                     OpenMetadataProperty.GUID.name, GLOSSARY_TYPE_NAME, null,
                                                     null, false, false, null,
                                                     null, "getGlossary")).thenReturn(glossaries.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getGlossary(USER_ID, SERVER_NAME, glossaries.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
    }

    @Test
    public void getAllGlossaries() throws Exception{
        when(entitiesHandler.getEntitiesByType(USER_ID, GLOSSARY_TYPE_GUID, GLOSSARY_TYPE_NAME, null,
                false, false, null, 0, 10, null,
                "getAllGlossaries")).thenReturn(glossaries);

        GlossaryViewEntityDetailResponse response = underTest.getAllGlossaries(USER_ID, SERVER_NAME, 0, 10);

        assertEquals(3, response.getResult().size());

        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
        assertGlossaryProperties(glossaries.get(1), (Glossary)response.getResult().get(1));
        assertGlossaryProperties(glossaries.get(2), (Glossary)response.getResult().get(2));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));

    }

    @Test
    public void getTermHomeGlossary() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, TERM_ANCHOR_RELATIONSHIP_GUID, TERM_ANCHOR_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 0, null, "getTermHomeGlossary"))
                .thenReturn(Collections.singletonList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getTermHomeGlossary(USER_ID, SERVER_NAME, terms.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getCategoryHomeGlossary() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, categories.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 CATEGORY_TYPE_NAME, CATEGORY_ANCHOR_RELATIONSHIP_GUID, CATEGORY_ANCHOR_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 0, null, "getCategoryHomeGlossary"))
                .thenReturn(Collections.singletonList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getCategoryHomeGlossary(USER_ID, SERVER_NAME, categories.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getExternalGlossaryLinks() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, glossaries.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 GLOSSARY_TYPE_NAME, EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID, EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME,
                                                 null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getExternalGlossaryLinks"))
                .thenReturn(Collections.singletonList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME,
                glossaries.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertExternalGlossaryLinkProperties(externalGlossaryLink, (ExternalGlossaryLink) response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void throwOmrsExceptionOnGetEntityByGUID() throws Exception{
        PropertyServerException exception = new PropertyServerException(501, "className-getEntityFromRepository",
                "actionDescription-getEntityByGUID", "errorMessage-getEntityByGUID", null, null,
                "systemAction-getEntityByGUID", "userAction-getEntityByGUID", null, null);

        when(entitiesHandler.getEntityFromRepository(USER_ID, glossaries.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                     GLOSSARY_TYPE_NAME, null, null, false,
                                                     false, null, null, "getGlossary"))
                .thenThrow(exception);

        GlossaryViewEntityDetailResponse response = underTest.getGlossary(USER_ID, SERVER_NAME, glossaries.get(0).getGUID());

        assertExceptionDataInResponse(exception, response);
    }

    @Test
    public void throwOmrsExceptionOnGetEntitiesForRelationshipType() throws Exception{
        PropertyServerException exception = new PropertyServerException(501, "className-getAttachedEntities",
                "actionDescription--getEntitiesForRelationshipType", "errorMessage--getEntitiesForRelationshipType", null, null,
                "systemAction--getEntitiesForRelationshipType", "userAction--getEntitiesForRelationshipType", null, null);
        when(entitiesHandler.getAttachedEntities(USER_ID, glossaries.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 GLOSSARY_TYPE_NAME, EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID, EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME,
                                                 null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getExternalGlossaryLinks")).thenThrow(exception);

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME,
                glossaries.get(0).getGUID(),0, 10);

        assertExceptionDataInResponse(exception, response);
    }

    private void assertGlossaryProperties(EntityDetail expected, Glossary actual){
        assertEquals(expected.getGUID(), actual.getGuid());
        assertEquals(expected.getProperties().getPropertyValue("qualifiedName").valueAsString(), actual.getQualifiedName());
        assertEquals(expected.getProperties().getPropertyValue("displayName").valueAsString(), actual.getDisplayName());
        assertEquals(expected.getProperties().getPropertyValue("language").valueAsString(), actual.getLanguage());
        assertEquals(expected.getProperties().getPropertyValue("description").valueAsString(), actual.getDescription());
        assertEquals(expected.getProperties().getPropertyValue("usage").valueAsString(), actual.getUsage());
    }

}
