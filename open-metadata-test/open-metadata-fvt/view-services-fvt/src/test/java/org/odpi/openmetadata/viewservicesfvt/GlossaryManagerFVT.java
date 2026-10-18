/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateWithTemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.AbstractConceptProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.ActivityDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.CanonicalVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.ContextDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.DataValueMeaningProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.ElementSupplementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.NamingStandardsVocabularyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.QuestionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.TaxonomyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.UsedInContextProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ClassWordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ModifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PrimeWordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GlossaryManagerFVT covers every request of the Glossary Manager OMVS: the glossary classifications, the
 * valid-value lists, the glossary term lifecycle (create, create from template, update, find, delete), the
 * term-to-term relationships, the term classifications, and the used-in-context relationship.
 * <br>
 * Glossaries are collections, so they are created and deleted with the Collection Manager.  Terms are created
 * as members of their glossary, anchored to it.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GlossaryManagerFVT
{
    private static final ViewServiceClient glossaryManager   = new ViewServiceClient(ViewServiceDescription.GLOSSARY_MANAGER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static final List<String> termsToDelete = new ArrayList<>();

    private static String glossaryGUID      = null;
    private static String otherGlossaryGUID = null;
    private static String termGUID          = null;
    private static String secondTermGUID    = null;


    /**
     * The three valid-value lists return the values of their enums, and the term relationship type names are
     * the term-to-term relationship types.
     */
    @Test
    @Order(1)
    void validValueLists()
    {
        assertEquals(List.of(ContentStatus.values()),
                     glossaryManager.get("/glossaries/terms/status-list", GlossaryTermStatusListResponse.class).getStatuses());
        assertEquals(List.of(TermRelationshipStatus.values()),
                     glossaryManager.get("/glossaries/terms/relationships/status-list", GlossaryTermRelationshipStatusListResponse.class).getStatuses());
        assertEquals(List.of(ActivityType.values()),
                     glossaryManager.get("/glossaries/terms/activity-types", GlossaryTermActivityTypeListResponse.class).getTypes());

        List<String> typeNames = glossaryManager.get("/glossaries/terms/relationships/type-names", NameListResponse.class).getNames();

        assertNotNull(typeNames);
        assertTrue(typeNames.containsAll(List.of(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName,
                                                 OpenMetadataType.SYNONYM_RELATIONSHIP.typeName,
                                                 OpenMetadataType.ANTONYM_RELATIONSHIP.typeName)),
                   "getTermRelationshipTypeNames is missing term relationship types: " + typeNames);
    }


    /**
     * The taxonomy, canonical vocabulary and naming standards classifications show on a glossary once set and
     * are gone once cleared.
     */
    @Test
    @Order(2)
    void glossaryClassifications()
    {
        glossaryGUID      = createGlossary("main");
        otherGlossaryGUID = createGlossary("other");

        TaxonomyProperties taxonomyProperties = new TaxonomyProperties();
        taxonomyProperties.setOrganizingPrinciple("Alphabetical");

        CanonicalVocabularyProperties canonicalVocabularyProperties = new CanonicalVocabularyProperties();
        canonicalVocabularyProperties.setScope("View services FVT");

        classifyGlossary("/is-taxonomy", taxonomyProperties, OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName);
        classifyGlossary("/is-canonical-vocabulary", canonicalVocabularyProperties, OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName);
        classifyGlossary("/is-naming-standards-vocabulary", new NamingStandardsVocabularyProperties(), OpenMetadataType.NAMING_STANDARDS_VOCABULARY_CLASSIFICATION.typeName);

        ElementClassification taxonomy = RelatedElementChecks.classification(getGlossary(glossaryGUID).getElementHeader(),
                                                                             OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName);

        assertNotNull(taxonomy);
        assertEquals("Alphabetical",
                     assertInstanceOf(TaxonomyProperties.class, taxonomy.getClassificationProperties()).getOrganizingPrinciple());

        for (String clearPath : List.of("/is-taxonomy/remove", "/is-canonical-vocabulary/remove", "/is-naming-standards-vocabulary/remove"))
        {
            glossaryManager.call("/glossaries/" + glossaryGUID + clearPath, new MetadataSourceRequestBody());
        }

        assertFalse(RelatedElementChecks.classificationNames(getGlossary(glossaryGUID).getElementHeader()).stream()
                            .anyMatch(name -> List.of(OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                      OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                      OpenMetadataType.NAMING_STANDARDS_VOCABULARY_CLASSIFICATION.typeName).contains(name)),
                    "a cleared glossary classification is still present");
    }


    /**
     * A term created in a glossary can be retrieved with its properties, is a member of the glossary, can be
     * updated, and is found by name and search string.
     */
    @Test
    @Order(3)
    void termLifecycle()
    {
        GlossaryTermProperties properties = termProperties("customer");

        properties.setSummary("A person who buys things");
        properties.setAbbreviation("CUST");
        properties.setAliases(List.of("client"));

        termGUID       = createTerm(glossaryGUID, properties);
        secondTermGUID = createTerm(glossaryGUID, termProperties("account"));

        GlossaryTermProperties retrieved = assertInstanceOf(GlossaryTermProperties.class, getTerm(termGUID).getProperties());

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals("A person who buys things", retrieved.getSummary());
        assertEquals("CUST", retrieved.getAbbreviation());
        assertEquals(List.of("client"), retrieved.getAliases());

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/" + glossaryGUID + "/members", new ResultsRequestBody()), termGUID),
                   "the new term is not a member of its glossary");

        GlossaryTermProperties update = new GlossaryTermProperties();

        update.setUsage("Use for retail customers only");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        assertTrue(glossaryManager.post("/glossaries/terms/" + termGUID + "/update", updateRequestBody, BooleanResponse.class).getFlag(),
                   "updateGlossaryTerm reported that nothing changed");

        retrieved = assertInstanceOf(GlossaryTermProperties.class, getTerm(termGUID).getProperties());

        assertEquals("Use for retail customers only", retrieved.getUsage());
        assertEquals("A person who buys things", retrieved.getSummary(), "a merge update should keep the summary");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        List<OpenMetadataRootElement> byNameResults = glossaryManager.elements("/glossaries/terms/by-name", byName);

        assertTrue(RelatedElementChecks.contains(byNameResults, termGUID), "getGlossaryTermsByName did not return the term");
        assertFalse(RelatedElementChecks.contains(byNameResults, secondTermGUID), "getGlossaryTermsByName returned a term with another name");

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = glossaryManager.elements("/glossaries/terms/by-search-string", search);

        assertTrue(RelatedElementChecks.contains(found, termGUID), "findGlossaryTerms did not return the term");
        assertTrue(RelatedElementChecks.contains(found, secondTermGUID), "findGlossaryTerms did not return the second term");
    }


    /**
     * A term created from a template takes the template's properties with the placeholder in its qualified name
     * replaced.  The two requests that work from a template or move a term are accepted - what they do is
     * checked by {@link #updateTermFromTemplateTakesTheTemplatesProperties} and {@link #moveTermChangesItsGlossary}.
     */
    @Test
    @Order(4)
    void termTemplatesAndMove()
    {
        GlossaryTermProperties templateProperties = termProperties("template");

        templateProperties.setQualifiedName(templateProperties.getQualifiedName() + "::~{termName}~");
        templateProperties.setSummary("Summary from the template");

        String templateGUID = createTerm(glossaryGUID, templateProperties);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setAnchorGUID(glossaryGUID);
        requestBody.setIsOwnAnchor(false);
        requestBody.setParentGUID(glossaryGUID);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);
        requestBody.setPlaceholderPropertyValues(Map.of("termName", "copy1"));

        String copyGUID = glossaryManager.create("/glossaries/terms/from-template/" + templateGUID, requestBody);

        termsToDelete.add(copyGUID);

        assertNotEquals(templateGUID, copyGUID);

        GlossaryTermProperties copy = assertInstanceOf(GlossaryTermProperties.class, getTerm(copyGUID).getProperties());

        assertEquals(templateProperties.getQualifiedName().replace("~{termName}~", "copy1"), copy.getQualifiedName());
        assertEquals("Summary from the template", copy.getSummary());

        UpdateWithTemplateRequestBody updateRequestBody = new UpdateWithTemplateRequestBody();

        updateRequestBody.setMergeUpdate(true);

        glossaryManager.call("/glossaries/terms/" + secondTermGUID + "/update/from-template/" + templateGUID, updateRequestBody);

        glossaryManager.call("/glossaries/terms/" + copyGUID + "/move-to/" + otherGlossaryGUID, new DeleteRelationshipRequestBody());
    }


    /**
     * A term updated from a template takes the template's properties that the update does not set.
     */
    @Test
    @Order(5)
    void updateTermFromTemplateTakesTheTemplatesProperties()
    {
        GlossaryTermProperties templateProperties = termProperties("update-template");

        templateProperties.setSummary("Summary from the update template");

        String templateGUID = createTerm(glossaryGUID, templateProperties);
        String targetGUID   = createTerm(glossaryGUID, termProperties("update-target"));

        UpdateWithTemplateRequestBody updateRequestBody = new UpdateWithTemplateRequestBody();

        updateRequestBody.setMergeUpdate(true);

        glossaryManager.call("/glossaries/terms/" + targetGUID + "/update/from-template/" + templateGUID, updateRequestBody);

        assertEquals("Summary from the update template",
                     assertInstanceOf(GlossaryTermProperties.class, getTerm(targetGUID).getProperties()).getSummary());
    }


    /**
     * A term moved to another glossary becomes a member of that glossary and stops being a member of its old
     * one.
     */
    @Test
    @Order(6)
    void moveTermChangesItsGlossary()
    {
        String movingTermGUID = createTerm(glossaryGUID, termProperties("moving"));

        glossaryManager.call("/glossaries/terms/" + movingTermGUID + "/move-to/" + otherGlossaryGUID, new DeleteRelationshipRequestBody());

        assertTrue(RelatedElementChecks.contains(collectionManager.elements("/collections/" + otherGlossaryGUID + "/members", new ResultsRequestBody()), movingTermGUID),
                   "the moved term is not a member of its new glossary");
        assertFalse(RelatedElementChecks.contains(collectionManager.elements("/collections/" + glossaryGUID + "/members", new ResultsRequestBody()), movingTermGUID),
                    "the moved term is still a member of its old glossary");
    }


    /**
     * A term-to-term relationship shows on the term with its properties, can be updated and is gone once
     * removed.
     */
    @Test
    @Order(7)
    void termRelationships()
    {
        String relationshipPath = "/glossaries/terms/" + termGUID + "/relationships/" + OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName + "/terms/" + secondTermGUID;

        GlossaryTermRelationshipProperties properties = new GlossaryTermRelationshipProperties();

        properties.setDescription("Customers own accounts");
        properties.setTermRelationshipStatus(TermRelationshipStatus.DRAFT);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        glossaryManager.call(relationshipPath, requestBody);

        assertTrue(RelatedElementChecks.isLinked(getTerm(termGUID), OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName, secondTermGUID),
                   "the term does not show its related term");

        GlossaryTermRelationshipProperties update = new GlossaryTermRelationshipProperties();

        update.setTermRelationshipStatus(TermRelationshipStatus.ACTIVE);

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(update);

        glossaryManager.call(relationshipPath + "/update", updateRequestBody);

        RelatedMetadataElementSummary relationship = RelatedElementChecks.relationship(getTerm(termGUID),
                                                                                       OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName,
                                                                                       secondTermGUID);

        assertNotNull(relationship);

        GlossaryTermRelationshipProperties retrieved = assertInstanceOf(GlossaryTermRelationshipProperties.class, relationship.getRelationshipProperties());

        assertEquals(TermRelationshipStatus.ACTIVE, retrieved.getTermRelationshipStatus());
        assertEquals("Customers own accounts", retrieved.getDescription(), "a merge update should keep the description");

        glossaryManager.call(relationshipPath + "/remove", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(getTerm(termGUID), OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName, secondTermGUID),
                    "the term still shows the removed related term");
    }


    /**
     * Each term classification shows on the term once set and is gone once cleared.
     */
    @Test
    @Order(8)
    void termClassifications()
    {
        ActivityDescriptionProperties activityDescriptionProperties = new ActivityDescriptionProperties();
        activityDescriptionProperties.setActivityType(ActivityType.TASK);

        ContextDefinitionProperties contextDefinitionProperties = new ContextDefinitionProperties();
        contextDefinitionProperties.setDescription("Retail");
        contextDefinitionProperties.setScope("Sales");

        classifyTerm("/glossaries/terms/", "/is-abstract-concept", new AbstractConceptProperties(), "/is-abstract-concept/delete",
                     OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-prime-word", new PrimeWordProperties(), "/is-prime-word/delete",
                     OpenMetadataType.PRIME_WORD_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-modifier", new ModifierProperties(), "/is-modifier/delete",
                     OpenMetadataType.MODIFIER_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-class-word", new ClassWordProperties(), "/is-class-word/delete",
                     OpenMetadataType.CLASS_WORD_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-data-value", new DataValueMeaningProperties(), "/is-data-value/remove",
                     OpenMetadataType.DATA_VALUE_MEANING_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-question", new QuestionProperties(), "/is-question/remove",
                     OpenMetadataType.QUESTION_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-activity", activityDescriptionProperties, "/is-activity/remove",
                     OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName);
        classifyTerm("/glossaries/terms/", "/is-context-definition", contextDefinitionProperties, "/is-context-definition/delete",
                     OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION.typeName);
        classifyTerm("/glossary-terms/", "/element-supplement", new ElementSupplementProperties(), "/element-supplement/remove",
                     OpenMetadataType.ELEMENT_SUPPLEMENT_CLASSIFICATION.typeName);
    }


    /**
     * setTermAsQuestion with no request body must classify the term as a question.
     */
    @Test
    @Order(9)
    void questionWithNoRequestBody()
    {
        /*
         * A JSON request with an empty body, which Spring passes to the service as a null request body.
         */
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse response =
                new org.springframework.web.client.RestTemplate().postForObject(OMAGPlatformExtension.getPlatformURLRoot() + "/servers/" +
                                                                                        OMAGPlatformExtension.VIEW_SERVER_NAME +
                                                                                        "/api/open-metadata/glossary-manager/glossaries/terms/" +
                                                                                        termGUID + "/is-question",
                                                                                new org.springframework.http.HttpEntity<>(headers),
                                                                                org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse.class);

        assertNotNull(response);
        assertEquals(200, response.getRelatedHTTPCode(), "setTermAsQuestion with no request body was refused: " + response.getExceptionErrorMessage());

        java.util.Set<String> classificationNames = RelatedElementChecks.classificationNames(getTerm(termGUID).getElementHeader());

        assertTrue(classificationNames.contains(OpenMetadataType.QUESTION_CLASSIFICATION.typeName), "the term was not classified as a question");
        assertFalse(classificationNames.contains(OpenMetadataType.DATA_VALUE_MEANING_CLASSIFICATION.typeName), "the term was classified as a data value");

        glossaryManager.call("/glossaries/terms/" + termGUID + "/is-question/remove", new DeleteClassificationRequestBody());
    }


    /**
     * A term used in the context of another element shows that element until it is detached.
     */
    @Test
    @Order(10)
    void usedInContext()
    {
        UsedInContextProperties properties = new UsedInContextProperties();

        properties.setDescription("Customer in the context of an account");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        glossaryManager.call("/glossary-terms/" + termGUID + "/used-in-contexts/" + secondTermGUID + "/attach", requestBody);

        assertTrue(RelatedElementChecks.isLinked(getTerm(termGUID), OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName, secondTermGUID),
                   "the term does not show the context it is used in");

        glossaryManager.call("/glossary-terms/" + termGUID + "/used-in-contexts/" + secondTermGUID + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(getTerm(termGUID), OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName, secondTermGUID),
                    "the term still shows the detached context");
    }


    /**
     * Every term can be deleted, after which it can no longer be retrieved.
     */
    @Test
    @Order(99)
    void deleteTerms()
    {
        for (String guid : termsToDelete)
        {
            glossaryManager.call("/glossaries/terms/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(glossaryManager, "/glossaries/terms/" + guid);
        }

        termsToDelete.clear();
    }


    @AfterAll
    static void deleteGlossaries()
    {
        for (String guid : new String[]{glossaryGUID, otherGlossaryGUID})
        {
            if (guid != null)
            {
                collectionManager.call("/collections/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + guid + "/retrieve");
            }
        }

        glossaryGUID      = null;
        otherGlossaryGUID = null;
    }


    private void classifyGlossary(String                       classifyPath,
                                  ClassificationBeanProperties properties,
                                  String                       classificationName)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        glossaryManager.call("/glossaries/" + glossaryGUID + classifyPath, requestBody);

        assertTrue(RelatedElementChecks.classificationNames(getGlossary(glossaryGUID).getElementHeader()).contains(classificationName),
                   classifyPath + " did not add " + classificationName);
    }


    private void classifyTerm(String                       pathRoot,
                              String                       classifyPath,
                              ClassificationBeanProperties properties,
                              String                       clearPath,
                              String                       classificationName)
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        glossaryManager.call(pathRoot + termGUID + classifyPath, requestBody);

        assertTrue(RelatedElementChecks.classificationNames(getTerm(termGUID).getElementHeader()).contains(classificationName),
                   classifyPath + " did not add " + classificationName);

        glossaryManager.call(pathRoot + termGUID + clearPath, new DeleteClassificationRequestBody());

        assertFalse(RelatedElementChecks.classificationNames(getTerm(termGUID).getElementHeader()).contains(classificationName),
                    clearPath + " did not remove " + classificationName);
    }


    private static String createGlossary(String name)
    {
        GlossaryProperties properties = new GlossaryProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Glossary", name));
        properties.setDisplayName("Glossary manager FVT " + name);
        properties.setLanguage("English");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return collectionManager.create("/collections", requestBody);
    }


    private static GlossaryTermProperties termProperties(String name)
    {
        GlossaryTermProperties properties = new GlossaryTermProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GlossaryTerm", name));
        properties.setDisplayName(name);

        return properties;
    }


    private static String createTerm(String                 glossary,
                                     GlossaryTermProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setAnchorGUID(glossary);
        requestBody.setIsOwnAnchor(false);
        requestBody.setParentGUID(glossary);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);
        requestBody.setProperties(properties);

        String guid = glossaryManager.create("/glossaries/terms", requestBody);

        termsToDelete.add(guid);

        return guid;
    }


    private static OpenMetadataRootElement getTerm(String guid)
    {
        OpenMetadataRootElement element = glossaryManager.element("/glossaries/terms/" + guid, new GetRequestBody());

        assertNotNull(element, "glossary term " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement getGlossary(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "glossary " + guid + " is missing");

        return element;
    }
}
