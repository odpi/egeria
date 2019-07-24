package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.junit.Before;
import org.junit.Test;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TermServiceTest extends GlossaryViewOmasBaseTest{

    private final TermService underTest = new TermService();

    @Before
    public void before() throws Exception{
        super.before(underTest);
    }

    @Test
    public void successfullyGetTerm() throws Exception{
        when(repositoryHandler.getEntityByGUID(eq(USER_ID), eq(terms.get(0).getGUID()), eq(GUID_PARAM_NAME),
                eq(TERM_TYPE_NAME), anyString())).thenReturn(terms.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getTerm(USER_ID, SERVER_NAME, terms.get(0).getGUID());

        assertEquals(1, response.getGlossaryViewEntityDetails().size());
        assertEquals(terms.get(0).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid());
    }

    @Test
    public void findNoTerm() throws Exception{
        when(repositoryHandler.getEntityByGUID(eq(USER_ID), eq(terms.get(0).getGUID()), eq(GUID_PARAM_NAME),
                eq(TERM_TYPE_NAME), anyString())).thenReturn(terms.get(0));

        GlossaryViewEntityDetailResponse response = underTest.getTerm(USER_ID, SERVER_NAME,"no-term-at-this-guid");

        assertEquals(1, response.getGlossaryViewEntityDetails().size());
        assertEquals(null, response.getGlossaryViewEntityDetails().get("entities"));
    }

    @Test
    public void getTermsViaTermAnchorRelationships() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(glossaries.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_ANCHOR_RELATIONSHIP_GUID), eq(TERM_ANCHOR_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTermsViaTermAnchorRelationships"))).thenReturn(terms);

        GlossaryViewEntityDetailResponse response = underTest.getTermsViaTermAnchorRelationships(USER_ID, SERVER_NAME,
                glossaries.get(0).getGUID(), 0, 10000);

        assertEquals(5, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(0).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(2).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(3).getGuid() );
        assertEquals(terms.get(4).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(4).getGuid() );
    }

    @Test
    public void getTermsViaTermCategorizationRelationships() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(categories.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_CATEGORIZATION_RELATIONSHIP_GUID), eq(TERM_CATEGORIZATION_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTermsViaTermCategorizationRelationships"))).thenReturn(Arrays.asList(terms.get(0), terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getTermsViaTermCategorizationRelationships(USER_ID, SERVER_NAME,
                categories.get(0).getGUID(), 0, 10000);

        assertEquals(3, response.getGlossaryViewEntityDetails().get("terms").size() );
        assertEquals(terms.get(0).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(2).getGuid() );
    }

    @Test
    public void getExternalGlossaries() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(LIBRARY_TERM_REFERENCE_RELATIONSHIP_GUID), eq(LIBRARY_TERM_REFERENCE_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getExternalGlossaries"))).thenReturn(Arrays.asList(externalGlossaryLink));

        GlossaryViewEntityDetailResponse response = underTest.getExternalGlossaries(USER_ID, SERVER_NAME, terms.get(0).getGUID(),0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().size());
        assertEquals(externalGlossaryLink.getGUID(), response.getGlossaryViewEntityDetails().get("externalGlossaryLinks").get(0).getGuid() );
    }

    @Test
    public void getRelatedTerms() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(RELATED_TERM_RELATIONSHIP_GUID), eq(RELATED_TERM_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getRelatedTerms"))).thenReturn(Arrays.asList(terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getRelatedTerms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getSynonyms() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(SYNONYM_RELATIONSHIP_GUID), eq(SYNONYM_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getSynonyms"))).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getSynonyms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getAntonyms() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(ANTONYM_RELATIONSHIP_GUID), eq(ANTONYM_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getAntonyms"))).thenReturn(Arrays.asList(terms.get(1), terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getAntonyms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(4).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getPreferredTerms() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(PREFERRED_TERM_RELATIONSHIP_GUID), eq(PREFERRED_TERM_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getPreferredTerms"))).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getPreferredTerms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getReplacementTerms() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(REPLACEMENT_TERM_RELATIONSHIP_GUID), eq(REPLACEMENT_TERM_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getReplacementTerms"))).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getReplacementTerms(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getTranslations() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TRANSLATION_RELATIONSHIP_GUID), eq(TRANSLATION_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTranslations"))).thenReturn(Arrays.asList(terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getTranslations(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
    }

    @Test
    public void getIsA() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(IS_A_RELATIONSHIP_GUID), eq(IS_A_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getIsA"))).thenReturn(Arrays.asList(terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getIsA(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
    }

    @Test
    public void getValidValues() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(VALID_VALUE_RELATIONSHIP_GUID), eq(VALID_VALUE_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getValidValues"))).thenReturn(Arrays.asList(terms.get(1), terms.get(2)));

        GlossaryViewEntityDetailResponse response = underTest.getValidValues(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getUsedInContexts() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(USED_IN_CONTEXT_RELATIONSHIP_GUID), eq(USED_IN_CONTEXT_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getUsedInContexts"))).thenReturn(Arrays.asList(terms.get(1), terms.get(2), terms.get(3), terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getUsedInContexts(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(4, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(2).getGuid() );
        assertEquals(terms.get(4).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(3).getGuid() );
    }

    @Test
    public void getAssignedElements() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(SEMANTIC_ASSIGNMENT_RELATIONSHIP_GUID), eq(SEMANTIC_ASSIGNMENT_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getAssignedElements"))).thenReturn(Arrays.asList(terms.get(2), terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getAssignedElements(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(2, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(2).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(1).getGuid() );
    }

    @Test
    public void getAttributes() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_HAS_A_RELATIONSHIP_GUID), eq(TERM_HAS_A_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getAttributes"))).thenReturn(Arrays.asList(terms.get(3)));

        GlossaryViewEntityDetailResponse response = underTest.getAttributes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(3).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
    }

    @Test
    public void getSubtypes() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_IS_A_TYPE_OF_RELATIONSHIP_GUID), eq(TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getSubtypes"))).thenReturn(Arrays.asList(terms.get(4)));

        GlossaryViewEntityDetailResponse response = underTest.getSubtypes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(4).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
    }

    @Test
    public void getTypes() throws Exception{
        when(repositoryHandler.getEntitiesForRelationshipType(eq(USER_ID), eq(terms.get(0).getGUID()), eq(TERM_TYPE_NAME),
                eq(TERM_TYPED_BY_RELATIONSHIP_GUID), eq(TERM_TYPED_BY_RELATIONSHIP_NAME), anyInt(), anyInt(),
                eq("getTypes"))).thenReturn(Arrays.asList(terms.get(1)));

        GlossaryViewEntityDetailResponse response = underTest.getTypes(USER_ID, SERVER_NAME, terms.get(0).getGUID(),
                0, 10);

        assertEquals(1, response.getGlossaryViewEntityDetails().get("terms").size());
        assertEquals(terms.get(1).getGUID(), response.getGlossaryViewEntityDetails().get("terms").get(0).getGuid() );
    }

}
