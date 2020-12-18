/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GlossaryServiceTest extends GlossaryViewOmasBaseTest{

    private final GlossaryService underTest = new GlossaryService();

    @Before
    public void before() throws Exception{
        super.before(underTest);
    }

    @Test
    public void getGlossary() throws Exception{
        when(repositoryHandler.getEntityByGUID(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq("guid"),
                eq(GLOSSARY_TYPE_NAME), eq("getGlossary"))).thenReturn(glossaries.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getGlossary(USER_ID, SERVER_NAME, glossaries.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
    }

    @Test
    public void getAllGlossaries() throws Exception{
        when(repositoryHandler.getEntitiesByType(eq(USER_ID), eq(GLOSSARY_TYPE_GUID),
                anyInt(), anyInt(), eq("getAllGlossaries"))).thenReturn(glossaries);

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
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_ANCHOR_RELATIONSHIP_GUID), eq(TERM_ANCHOR_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTermHomeGlossary"))).thenReturn(Collections.singletonList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getTermHomeGlossary(USER_ID, SERVER_NAME, terms.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getCategoryHomeGlossary() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(categories.get(0).getGUID()), eq(CATEGORY_TYPE_NAME),
                eq(CATEGORY_ANCHOR_RELATIONSHIP_GUID), eq(CATEGORY_ANCHOR_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getCategoryHomeGlossary"))).thenReturn(Collections.singletonList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getCategoryHomeGlossary(USER_ID, SERVER_NAME, categories.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryProperties(glossaries.get(0), (Glossary)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getExternalGlossaryLinks() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq(GLOSSARY_TYPE_NAME),
                eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID), eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getExternalGlossaryLinks"))).thenReturn(Collections.singletonList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME, glossaries.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertExternalGlossaryLinkProperties(externalGlossaryLink, (ExternalGlossaryLink) response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void throwOmrsExceptionOnGetEntityByGUID() throws Exception{
        PropertyServerException exception = new PropertyServerException(501, "className-getEntityByGUID", "actionDescription-getEntityByGUID",
                "errorMessage-getEntityByGUID", "systemAction-getEntityByGUID", "userAction-getEntityByGUID");
        when(repositoryHandler.getEntityByGUID(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq("guid"),
                eq(GLOSSARY_TYPE_NAME), eq("getGlossary"))).thenThrow(exception);

        GlossaryViewEntityDetailResponse response = underTest.getGlossary(USER_ID, SERVER_NAME, glossaries.get(0).getGUID());

        assertExceptionDataInResponse(exception, response);
    }

    @Test
    public void throwOmrsExceptionOnGetEntitiesForRelationshipType() throws Exception{
        PropertyServerException exception = new PropertyServerException(501, "className-getEntitiesForRelationshipType",
                "actionDescription--getEntitiesForRelationshipType", "errorMessage--getEntitiesForRelationshipType",
                "systemAction--getEntitiesForRelationshipType", "userAction--getEntitiesForRelationshipType");
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq(GLOSSARY_TYPE_NAME),
                eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID), eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getExternalGlossaryLinks"))).thenThrow(exception);

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME, glossaries.get(0).getGUID(),0, 10);

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
