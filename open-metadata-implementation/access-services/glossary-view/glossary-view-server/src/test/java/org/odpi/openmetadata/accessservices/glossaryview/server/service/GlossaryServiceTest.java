/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        assertEquals(glossaries.get(0).getGUID(), response.getResult().get(0).getGuid());
    }

    @Test
    public void getAllGlossaries() throws Exception{
        when(repositoryHandler.getEntitiesByType(eq(USER_ID), eq(GLOSSARY_TYPE_GUID),
                anyInt(), anyInt(), eq("getAllGlossaries"))).thenReturn(glossaries);

        GlossaryViewEntityDetailResponse response = underTest.getAllGlossaries(USER_ID, SERVER_NAME, 0, 10);

        assertEquals(3, response.getResult().size());

        assertEquals(glossaries.get(0).getGUID(), response.getResult().get(0).getGuid());
        assertEquals(glossaries.get(1).getGUID(), response.getResult().get(1).getGuid());
        assertEquals(glossaries.get(2).getGUID(), response.getResult().get(2).getGuid());

        assertEquals(true, isEffective.test(response.getResult().get(0)));
        assertEquals(true, isEffective.test(response.getResult().get(1)));
        assertEquals(true, isEffective.test(response.getResult().get(2)));

    }

    @Test
    public void getTermHomeGlossary() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_ANCHOR_RELATIONSHIP_GUID), eq(TERM_ANCHOR_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTermHomeGlossary"))).thenReturn(Arrays.asList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getTermHomeGlossary(USER_ID, SERVER_NAME, terms.get(0).getGUID());

        assertEquals(1, response.getResult().size());

        assertEquals(glossaries.get(0).getGUID(), response.getResult().get(0).getGuid());

        assertEquals(true, isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getCategoryHomeGlossary() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(categories.get(0).getGUID()), eq(CATEGORY_TYPE_NAME),
                eq(CATEGORY_ANCHOR_RELATIONSHIP_GUID), eq(CATEGORY_ANCHOR_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getCategoryHomeGlossary"))).thenReturn(Arrays.asList(glossaries.get(0)));

        GlossaryViewEntityDetailResponse response = underTest.getCategoryHomeGlossary(USER_ID, SERVER_NAME, categories.get(0).getGUID());

        assertEquals(1, response.getResult().size());

        assertEquals(glossaries.get(0).getGUID(), response.getResult().get(0).getGuid());

        assertEquals(true, isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getExternalGlossaries() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq(GLOSSARY_TYPE_NAME),
                eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_GUID), eq(EXTERNALLY_SOURCED_GLOSSARY_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getExternalGlossaryLinks"))).thenReturn(Arrays.asList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME, glossaries.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());

        assertEquals(externalGlossaryLink.getGUID(), response.getResult().get(0).getGuid() );

        assertEquals(true, isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void throwOmrsExceptionOnGetEntityByGUID() throws Exception{
        PropertyServerException exception = new PropertyServerException(501, "className-getEntityByGUID", "actionDescription-getEntityByGUID",
                "errorMessage-getEntityByGUID", "systemAction-getEntityByGUID", "userAction-getEntityByGUID");
        when(repositoryHandler.getEntityByGUID(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq("guid"),
                eq(GLOSSARY_TYPE_NAME), eq("getGlossary"))).thenThrow(exception);

        GlossaryViewEntityDetailResponse response = underTest.getGlossary(USER_ID, SERVER_NAME, glossaries.get(0).getGUID());

        assertEquals(exception.getReportedHTTPCode(), response.getRelatedHTTPCode());
        assertEquals(exception.getReportingClassName(), response.getExceptionClassName());
        assertEquals(exception.getReportingActionDescription(), response.getActionDescription());
        assertEquals(exception.getErrorMessage(), response.getExceptionErrorMessage());
        assertEquals(exception.getReportedSystemAction(), response.getExceptionSystemAction());
        assertEquals(exception.getReportedUserAction(), response.getExceptionUserAction());
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

        assertEquals(exception.getReportedHTTPCode(), response.getRelatedHTTPCode());
        assertEquals(exception.getReportingClassName(), response.getExceptionClassName());
        assertEquals(exception.getReportingActionDescription(), response.getActionDescription());
        assertEquals(exception.getErrorMessage(), response.getExceptionErrorMessage());
        assertEquals(exception.getReportedSystemAction(), response.getExceptionSystemAction());
        assertEquals(exception.getReportedUserAction(), response.getExceptionUserAction());
    }

}
