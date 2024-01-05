/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GlossaryTermServiceTest extends GlossaryViewOmasBase {

    private final TermService underTest = new TermService();

    @Before
    public void before() throws Exception{
        super.before(underTest);
    }

    @Test
    public void successfullyGetTerm() throws Exception{
        when(entitiesHandler.getEntityFromRepository(USER_ID, terms.get(0).getGUID(),
                                                     OpenMetadataProperty.GUID.name, TERM_TYPE_NAME, null, null,
                                                     false, false, null, null, "getTerm"))
                .thenReturn(terms.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getTerm(USER_ID, SERVER_NAME, terms.get(0).getGUID());

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(0), (GlossaryTerm)response.getResult().get(0));
    }

    @Test
    public void findNoTerm() throws Exception{
        when(entitiesHandler.getEntityFromRepository(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                     TERM_TYPE_NAME, null, null, false, false,
                                                     null, null, "getTerm")).thenReturn(terms.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getTerm(USER_ID, SERVER_NAME,"no-term-at-this-guid");

        assertEquals(0, response.getResult().size());
    }

    @Test
    public void getAllTerms() throws Exception{
        when(entitiesHandler.getEntitiesByType(USER_ID, TERM_TYPE_GUID, TERM_TYPE_NAME, null,
                false, false, null, 0, 10, null,
                "getAllTerms")).thenReturn(terms);

        GlossaryViewEntityDetailResponse response = underTest.getAllTerms(USER_ID, SERVER_NAME, 0, 10);

        assertEquals(5, response.getResult().size());

        assertGlossaryTermProperties(terms.get(0), (GlossaryTerm) response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm) response.getResult().get(1));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm) response.getResult().get(2));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm) response.getResult().get(3));
        assertGlossaryTermProperties(terms.get(4), (GlossaryTerm) response.getResult().get(4));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));
        assertTrue(isEffective.test(response.getResult().get(3)));
        assertTrue(isEffective.test(response.getResult().get(4)));

    }

    @Test
    public void getTermsViaTermAnchorRelationships() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, glossaries.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 GLOSSARY_TYPE_NAME, TERM_ANCHOR_RELATIONSHIP_GUID, TERM_ANCHOR_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10000, null, "getTermsViaTermAnchorRelationships")).thenReturn(terms);

        GlossaryViewEntityDetailResponse response = underTest.getTermsViaTermAnchorRelationships(USER_ID, SERVER_NAME,
                glossaries.get(0).getGUID(), 0, 10000);

        assertEquals(5, response.getResult().size());

        assertGlossaryTermProperties(terms.get(0), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(1));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(2));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(3));
        assertGlossaryTermProperties(terms.get(4), (GlossaryTerm)response.getResult().get(4));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));
        assertTrue(isEffective.test(response.getResult().get(3)));
        assertTrue(isEffective.test(response.getResult().get(4)));
    }

    @Test
    public void getTermsViaTermCategorizationRelationships() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, categories.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 CATEGORY_TYPE_NAME, TERM_CATEGORIZATION_RELATIONSHIP_GUID, TERM_CATEGORIZATION_RELATIONSHIP_NAME,
                                                 null,
                                                 null, null, 0, false, false,
                                                 0, 10000, null, "getTermsViaTermCategorizationRelationships"))
                .thenReturn(Arrays.asList(terms.get(0), terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getTermsViaTermCategorizationRelationships(USER_ID, SERVER_NAME,
                categories.get(0).getGUID(), 0, 10000);

        assertEquals(3, response.getResult().size() );

        assertGlossaryTermProperties(terms.get(0), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(1));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(2));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));
    }

    @Test
    public void getExternalGlossaries() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID, LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME,
                                                 null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getExternalGlossaryLinks"))
                .thenReturn(Collections.singletonList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaryLinks(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertExternalGlossaryLinkProperties(externalGlossaryLink, (ExternalGlossaryLink) response.getResult().get(0) );
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getRelatedTerms() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, RELATED_TERM_RELATIONSHIP_GUID, RELATED_TERM_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getRelatedTerms")).thenReturn(Arrays.asList(terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getRelatedTerms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getSynonyms() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, SYNONYM_RELATIONSHIP_GUID, SYNONYM_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getSynonyms")).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getSynonyms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getAntonyms() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, ANTONYM_RELATIONSHIP_GUID, ANTONYM_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getAntonyms")).thenReturn(Arrays.asList(terms.get(1), terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getAntonyms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(4), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getPreferredTerms() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, PREFERRED_TERM_RELATIONSHIP_GUID, PREFERRED_TERM_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getPreferredTerms")).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getPreferredTerms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getReplacementTerms() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, REPLACEMENT_TERM_RELATIONSHIP_GUID, REPLACEMENT_TERM_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getReplacementTerms"))
                .thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getReplacementTerms(USER_ID, SERVER_NAME,
                terms.get(0).getGUID(),0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getTranslations() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, TRANSLATION_RELATIONSHIP_GUID, TRANSLATION_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getTranslations")).thenReturn(Collections.singletonList(terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getTranslations(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getIsA() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, IS_A_RELATIONSHIP_GUID, IS_A_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getIsA")).thenReturn(Collections.singletonList(terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getIsA(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getValidValues() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, VALID_VALUE_RELATIONSHIP_GUID, VALID_VALUE_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getValidValues")).thenReturn(Arrays.asList(terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getValidValues(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getUsedInContexts() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, USED_IN_CONTEXT_RELATIONSHIP_GUID, USED_IN_CONTEXT_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getUsedInContexts"))
                .thenReturn(Arrays.asList(terms.get(1), terms.get(2), terms.get(3), terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getUsedInContexts(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(4, response.getResult().size());

        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(1));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(2));
        assertGlossaryTermProperties(terms.get(4), (GlossaryTerm)response.getResult().get(3));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
        assertTrue(isEffective.test(response.getResult().get(2)));
        assertTrue(isEffective.test(response.getResult().get(3)));
    }

    @Test
    public void getAssignedElements() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID, SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getAssignedElements"))
                .thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getAssignedElements(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(2, response.getResult().size());

        assertGlossaryTermProperties(terms.get(2), (GlossaryTerm)response.getResult().get(0));
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(1));

        assertTrue(isEffective.test(response.getResult().get(0)));
        assertTrue(isEffective.test(response.getResult().get(1)));
    }

    @Test
    public void getAttributes() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, TERM_HAS_A_RELATIONSHIP_GUID, TERM_HAS_A_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getAttributes")).thenReturn(Collections.singletonList(terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getAttributes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(3), (GlossaryTerm)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getSubtypes() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, TERM_IS_A_TYPE_OF_RELATIONSHIP_GUID, TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getSubtypes")).thenReturn(Collections.singletonList(terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getSubtypes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(4), (GlossaryTerm)response.getResult().get(0));
        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    @Test
    public void getTypes() throws Exception{
        when(entitiesHandler.getAttachedEntities(USER_ID, terms.get(0).getGUID(), OpenMetadataProperty.GUID.name,
                                                 TERM_TYPE_NAME, TERM_TYPED_BY_RELATIONSHIP_GUID, TERM_TYPED_BY_RELATIONSHIP_NAME, null,
                                                 null, null, 0, false, false,
                                                 0, 10, null, "getTypes")).thenReturn(Collections.singletonList(terms.get(1)));

        GlossaryViewEntityDetailResponse response = underTest.getTypes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getResult().size());
        assertGlossaryTermProperties(terms.get(1), (GlossaryTerm)response.getResult().get(0));

        assertTrue(isEffective.test(response.getResult().get(0)));
    }

    private void assertGlossaryTermProperties(EntityDetail expected, GlossaryTerm actual){
        assertEquals(expected.getGUID(), actual.getGuid());
        assertEquals(expected.getProperties().getPropertyValue("qualifiedName").valueAsString(), actual.getQualifiedName());
        assertEquals(expected.getProperties().getPropertyValue("displayName").valueAsString(), actual.getDisplayName());
        assertEquals(expected.getProperties().getPropertyValue("summary").valueAsString(), actual.getSummary());
        assertEquals(expected.getProperties().getPropertyValue("description").valueAsString(), actual.getDescription());
        assertEquals(expected.getProperties().getPropertyValue("examples").valueAsString(), actual.getExamples());
        assertEquals(expected.getProperties().getPropertyValue("abbreviation").valueAsString(), actual.getAbbreviation());
        assertEquals(expected.getProperties().getPropertyValue("usage").valueAsString(), actual.getUsage());
    }

}
