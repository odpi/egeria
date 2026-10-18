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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataRelationshipSummariesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataRelationshipSummaryResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryPropertiesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConsolidatedDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernancePrincipleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ScopedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRelationshipRequestBody;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClassificationExplorerRelationshipsFVT covers the Classification Explorer's relationship requests: each
 * attach request links a data set to another element, the matching query requests return the link from both
 * ends, and the detach request removes it.  The governance definitions, licenses and certifications are made
 * with the Governance Officer; the generic relationship and related-element queries are checked against a
 * MoreInformation relationship with known properties.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationExplorerRelationshipsFVT
{
    private static final ViewServiceClient explorer           = ClassificationExplorerFvtFixtures.classificationExplorer;
    private static final ViewServiceClient governanceOfficer  = ClassificationExplorerFvtFixtures.governanceOfficer;
    private static final ViewServiceClient glossaryManager    = new ViewServiceClient(ViewServiceDescription.GLOSSARY_MANAGER);
    private static final ViewServiceClient collectionManager  = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient actorManager       = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient automatedCuration  = new ViewServiceClient(ViewServiceDescription.AUTOMATED_CURATION);

    private static final String STEWARD = "steward-" + OMAGPlatformExtension.RUN_ID;
    private static final String LABEL   = "label-" + OMAGPlatformExtension.RUN_ID;

    private static String elementGUID        = null;
    private static String otherGUID          = null;
    private static String glossaryGUID       = null;
    private static String termGUID           = null;
    private static String actorGUID          = null;
    private static String definitionGUID     = null;
    private static String licenseTypeGUID    = null;
    private static String certificationTypeGUID = null;
    private static String copyGUID           = null;


    @Test
    @Order(1)
    void createElements()
    {
        elementGUID = ClassificationExplorerFvtFixtures.createDataSet("related", "Related data set");
        otherGUID   = ClassificationExplorerFvtFixtures.createDataSet("related-other", "Other related data set");

        GlossaryProperties glossaryProperties = new GlossaryProperties();
        glossaryProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Glossary", "classification-explorer"));
        glossaryProperties.setDisplayName("Classification explorer glossary");

        NewElementRequestBody glossaryRequest = new NewElementRequestBody();
        glossaryRequest.setIsOwnAnchor(true);
        glossaryRequest.setProperties(glossaryProperties);

        glossaryGUID = collectionManager.create("/collections", glossaryRequest);

        GlossaryTermProperties termProperties = new GlossaryTermProperties();
        termProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GlossaryTerm", "customer"));
        termProperties.setDisplayName("Customer");

        NewElementRequestBody termRequest = new NewElementRequestBody();
        termRequest.setAnchorGUID(glossaryGUID);
        termRequest.setIsOwnAnchor(false);
        termRequest.setParentGUID(glossaryGUID);
        termRequest.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        termRequest.setParentAtEnd1(true);
        termRequest.setProperties(termProperties);

        termGUID = glossaryManager.create("/glossaries/terms", termRequest);

        PersonProperties personProperties = new PersonProperties();
        personProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Person", "data-steward"));
        personProperties.setDisplayName("Data steward");

        NewElementRequestBody personRequest = new NewElementRequestBody();
        personRequest.setIsOwnAnchor(true);
        personRequest.setProperties(personProperties);

        actorGUID = actorManager.create("/actor-profiles", personRequest);

        GovernancePrincipleProperties principle = new GovernancePrincipleProperties();
        principle.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GovernancePrinciple", "know-your-data"));
        principle.setDisplayName("Know your data");

        definitionGUID = ClassificationExplorerFvtFixtures.createGovernanceDefinition(principle);

        LicenseTypeProperties licenseType = new LicenseTypeProperties();
        licenseType.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("LicenseType", "internal-use"));
        licenseType.setDisplayName("Internal use only");

        licenseTypeGUID = ClassificationExplorerFvtFixtures.createGovernanceDefinition(licenseType);

        CertificationTypeProperties certificationType = new CertificationTypeProperties();
        certificationType.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("CertificationType", "gold-data"));
        certificationType.setDisplayName("Gold data");

        certificationTypeGUID = ClassificationExplorerFvtFixtures.createGovernanceDefinition(certificationType);
    }


    /**
     * setupSemanticAssignment links the data set to the term; getMeanings and getSemanticAssignees return the
     * link from each end; clearSemanticAssignment removes it.
     */
    @Test
    @Order(2)
    void semanticAssignment()
    {
        assignTerm(elementGUID);

        assertEquals(List.of(termGUID), meanings(elementGUID, new SemanticAssignmentQueryProperties()));

        SemanticAssignmentQueryProperties bySteward = new SemanticAssignmentQueryProperties();
        bySteward.setSteward(STEWARD);

        assertEquals(List.of(elementGUID), assignees(bySteward));

        explorer.call("/elements/" + elementGUID + "/semantic-assignment/terms/" + termGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(meanings(elementGUID, new SemanticAssignmentQueryProperties()).isEmpty(), "getMeanings still returns the term after detach");
        assertTrue(assignees(new SemanticAssignmentQueryProperties()).isEmpty(), "getSemanticAssignees still returns the element after detach");
    }


    /**
     * getMeanings should apply the query properties in its request body: a filter that matches nothing returns
     * nothing.  The request body parameter has no @RequestBody annotation, so the filter never arrives.
     */
    @Test
    @Order(3)
    void meaningsAreFiltered()
    {
        assignTerm(elementGUID);

        try
        {
            SemanticAssignmentQueryProperties noMatch = new SemanticAssignmentQueryProperties();
            noMatch.setSteward(STEWARD + "-nobody");

            assertTrue(meanings(elementGUID, noMatch).isEmpty(), "getMeanings ignored the steward filter");
        }
        finally
        {
            explorer.call("/elements/" + elementGUID + "/semantic-assignment/terms/" + termGUID + "/detach", new DeleteRelationshipRequestBody());
        }
    }


    /**
     * getSemanticAssignees with a filter should return every assignment that matches, not just the first.
     */
    @Test
    @Order(4)
    void allMatchingAssigneesAreReturned()
    {
        assignTerm(elementGUID);
        assignTerm(otherGUID);

        try
        {
            SemanticAssignmentQueryProperties bySteward = new SemanticAssignmentQueryProperties();
            bySteward.setSteward(STEWARD);

            List<String> assignees = assignees(bySteward);

            assertTrue(assignees.contains(elementGUID) && assignees.contains(otherGUID),
                       "getSemanticAssignees should return both elements assigned by " + STEWARD + ": " + assignees);
        }
        finally
        {
            explorer.call("/elements/" + elementGUID + "/semantic-assignment/terms/" + termGUID + "/detach", new DeleteRelationshipRequestBody());
            explorer.call("/elements/" + otherGUID + "/semantic-assignment/terms/" + termGUID + "/detach", new DeleteRelationshipRequestBody());
        }
    }


    /**
     * addScopeToElement / getScopes / getScopedElements / removeScopeFromElement.
     */
    @Test
    @Order(5)
    void scopedBy()
    {
        String path = "/elements/" + elementGUID + "/scoped-by/" + otherGUID;

        explorer.call(path + "/attach", relationship(new ScopedByProperties()));

        assertEquals(List.of(otherGUID), query("/elements/" + elementGUID + "/scoped-by"), "getScopes");
        assertEquals(List.of(elementGUID), query("/elements/scoped-by/" + otherGUID), "getScopedElements");

        explorer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(query("/elements/" + elementGUID + "/scoped-by").isEmpty(), "getScopes after detach");
        assertTrue(query("/elements/scoped-by/" + otherGUID).isEmpty(), "getScopedElements after detach");
    }


    /**
     * addSupplementaryPropertiesToElement / removeSupplementaryPropertiesFromElement.
     */
    @Test
    @Order(6)
    void supplementaryProperties()
    {
        String path = "/elements/" + elementGUID + "/supplementary-properties/" + termGUID;

        SupplementaryPropertiesProperties properties = new SupplementaryPropertiesProperties();
        properties.setLabel("glossary entry");

        explorer.call(path + "/attach", relationship(properties));

        assertEquals(List.of(termGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName));
        assertEquals(List.of(elementGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(termGUID, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName));

        explorer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName).isEmpty());
    }


    /**
     * assignActorToElement / unassignActorFromElement.
     */
    @Test
    @Order(7)
    void assignedToActor()
    {
        String path = "/elements/" + elementGUID + "/assigned-to-actor/" + actorGUID;

        explorer.call(path + "/attach", relationship(new AssignmentScopeProperties()));

        assertEquals(List.of(actorGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName));
        assertEquals(List.of(elementGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(actorGUID, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName));

        explorer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName).isEmpty());
    }


    /**
     * addResourceListToElement / getResourceList / getSupportedByResource / removeResourceListFromElement.
     */
    @Test
    @Order(8)
    void resourceList()
    {
        String path = "/elements/" + elementGUID + "/resource-list/" + otherGUID;

        explorer.call(path + "/attach", relationship(new ResourceListProperties()));

        assertEquals(List.of(otherGUID), query("/elements/" + elementGUID + "/resource-list"), "getResourceList");
        assertEquals(List.of(elementGUID), query("/elements/resource-list/" + otherGUID), "getSupportedByResource");

        explorer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(query("/elements/" + elementGUID + "/resource-list").isEmpty(), "getResourceList after detach");
    }


    /**
     * addMoreInformationToElement / removeMoreInformationFromElement.
     */
    @Test
    @Order(9)
    void moreInformation()
    {
        String path = "/elements/" + elementGUID + "/more-information/" + otherGUID;

        MoreInformationProperties properties = new MoreInformationProperties();
        properties.setLabel("background");

        explorer.call(path + "/attach", relationship(properties));

        assertEquals(List.of(otherGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName));

        explorer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(ClassificationExplorerFvtFixtures.relatedGUIDs(elementGUID, OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName).isEmpty());
    }


    /**
     * getGovernedByDefinitions for a GovernedBy link made by the Governance Officer.
     */
    @Test
    @Order(10)
    void governedBy()
    {
        String path = "/elements/" + elementGUID + "/governed-by/definition/" + definitionGUID;

        GovernedByProperties properties = new GovernedByProperties();
        properties.setLabel("principle");

        governanceOfficer.call(path + "/attach", relationship(properties));

        assertEquals(List.of(definitionGUID), query("/elements/" + elementGUID + "/governed-by"), "getGovernedByDefinitions");

        governanceOfficer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(query("/elements/" + elementGUID + "/governed-by").isEmpty(), "getGovernedByDefinitions after detach");
    }


    /**
     * getGovernedElements should return the elements governed by a definition.  GovernedBy has the governed
     * element at end 1 and the definition at end 2, but the query starts from the definition at end 1.
     */
    @Test
    @Order(11)
    void governedElements()
    {
        String path = "/elements/" + elementGUID + "/governed-by/definition/" + definitionGUID;

        governanceOfficer.call(path + "/attach", relationship(new GovernedByProperties()));

        try
        {
            assertEquals(List.of(elementGUID), query("/elements/governed-by/" + definitionGUID), "getGovernedElements");
        }
        finally
        {
            governanceOfficer.call(path + "/detach", new DeleteRelationshipRequestBody());
        }
    }


    /**
     * getLicensedElements / getLicenses for a License made by the Governance Officer.
     */
    @Test
    @Order(12)
    void licenses()
    {
        LicenseProperties properties = new LicenseProperties();
        properties.setLicenseId("LIC-" + OMAGPlatformExtension.RUN_ID);
        properties.setLicensee("Analytics team");

        String licenseGUID = governanceOfficer.create("/elements/" + elementGUID + "/license-types/" + licenseTypeGUID + "/license", relationship(properties));

        assertEquals(List.of(elementGUID), query("/elements/licenses/" + licenseTypeGUID), "getLicensedElements");
        assertEquals(List.of(licenseTypeGUID), query("/elements/" + elementGUID + "/licenses"), "getLicenses");

        governanceOfficer.call("/licenses/" + licenseGUID + "/delete", new DeleteRelationshipRequestBody());

        assertTrue(query("/elements/" + elementGUID + "/licenses").isEmpty(), "getLicenses after delete");
    }


    /**
     * geCertifiedElements / getCertifications for a Certification made by the Governance Officer.
     */
    @Test
    @Order(13)
    void certifications()
    {
        CertificationProperties properties = new CertificationProperties();
        properties.setCertificateId("CERT-" + OMAGPlatformExtension.RUN_ID);
        properties.setRecipient("Analytics team");

        String certificationGUID = governanceOfficer.create("/elements/" + elementGUID + "/certification-types/" + certificationTypeGUID + "/certify", relationship(properties));

        assertEquals(List.of(elementGUID), query("/elements/certifications/" + certificationTypeGUID), "geCertifiedElements");
        assertEquals(List.of(certificationTypeGUID), query("/elements/" + elementGUID + "/certifications"), "getCertifications");

        governanceOfficer.call("/certifications/" + certificationGUID + "/delete", new DeleteRelationshipRequestBody());

        assertTrue(query("/elements/" + elementGUID + "/certifications").isEmpty(), "getCertifications after delete");
    }


    /**
     * An element created from the data set as a template is linked to it by SourcedFrom.
     */
    @Test
    @Order(14)
    void createFromTemplate()
    {
        DataSetProperties replacement = new DataSetProperties();
        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataSet", "related-copy"));
        replacement.setDisplayName("Copy of the related data set");

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setTemplateGUID(elementGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setMetadataElementSubtypeName(OpenMetadataType.DATA_SET.typeName);
        requestBody.setReplacementProperties(replacement);

        copyGUID = automatedCuration.create("/catalog-templates/new-element", requestBody);

        assertEquals(List.of(elementGUID), ClassificationExplorerFvtFixtures.relatedGUIDs(copyGUID, OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName));
    }


    /**
     * getSourceElements returns the template an element was created from, and getElementsSourcedFrom the
     * elements created from a template.  SourcedFrom has the new element at end 1 and the template at end 2;
     * both queries start from the wrong end.
     */
    @Test
    @Order(15)
    void sourcedFrom()
    {
        assertAll(() -> assertEquals(List.of(elementGUID), query("/elements/" + copyGUID + "/source"), "getSourceElements"),
                  () -> assertTrue(query("/elements/" + elementGUID + "/sourced-from").contains(copyGUID), "getElementsSourcedFrom"));
    }


    /**
     * linkElementsAsPeerDuplicates / unlinkElementsAsPeerDuplicates.  PeerDuplicateLink relationships are only
     * visible to requests made for duplicate processing.
     */
    @Test
    @Order(16)
    void peerDuplicates()
    {
        String path = "/related-elements/" + elementGUID + "/peer-duplicate/" + otherGUID;

        PeerDuplicateLinkProperties properties = new PeerDuplicateLinkProperties();
        properties.setStatusIdentifier(1);
        properties.setSteward(STEWARD);

        NewRelationshipRequestBody requestBody = relationship(properties);
        requestBody.setForDuplicateProcessing(true);

        explorer.call(path + "/attach", requestBody);

        assertEquals(1, duplicateLinks(OpenMetadataType.PEER_DUPLICATE_LINK.typeName).size(), "PeerDuplicateLink not created");

        DeleteRelationshipRequestBody detach = new DeleteRelationshipRequestBody();
        detach.setForDuplicateProcessing(true);

        explorer.call(path + "/detach", detach);

        assertTrue(duplicateLinks(OpenMetadataType.PEER_DUPLICATE_LINK.typeName).isEmpty(), "PeerDuplicateLink still present after detach");
    }


    /**
     * linkConsolidatedDuplicateToSource / unlinkConsolidatedDuplicateFromSourceElement.
     */
    @Test
    @Order(17)
    void consolidatedDuplicateSource()
    {
        String path = "/related-elements/" + elementGUID + "/consolidated-duplicate-source/" + otherGUID;

        NewRelationshipRequestBody requestBody = relationship(new ConsolidatedDuplicateLinkProperties());
        requestBody.setForDuplicateProcessing(true);

        explorer.call(path + "/attach", requestBody);

        assertEquals(1, duplicateLinks(OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName).size(), "ConsolidatedDuplicateLink not created");

        DeleteRelationshipRequestBody detach = new DeleteRelationshipRequestBody();
        detach.setForDuplicateProcessing(true);

        explorer.call(path + "/detach", detach);

        assertTrue(duplicateLinks(OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName).isEmpty(), "ConsolidatedDuplicateLink still present after detach");
    }


    /**
     * The generic relationship and related-element queries, checked against a MoreInformation relationship
     * with a run-unique label.
     */
    @Test
    @Order(18)
    void relationshipAndRelatedElementQueries()
    {
        String typeName = OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName;

        MoreInformationProperties properties = new MoreInformationProperties();
        properties.setLabel(LABEL);
        properties.setDescription("Background reading for the related data set.");

        explorer.call("/elements/" + elementGUID + "/more-information/" + otherGUID + "/attach", relationship(properties));

        try
        {
            /*
             * Relationship queries
             */
            FindRelationshipRequestBody betweenEnds = new FindRelationshipRequestBody();
            betweenEnds.setEnd1EntityGUIDs(List.of(elementGUID));
            betweenEnds.setEnd2EntityGUIDs(List.of(otherGUID));

            List<MetadataRelationshipSummary> byType = relationships("/relationships/" + typeName, betweenEnds);

            assertEquals(1, byType.size(), "getRelationships/{type} between the two elements: " + byType);

            String relationshipGUID = byType.get(0).getRelationshipHeader().getGUID();

            assertTrue(relationshipGUIDs(relationships("/relationships", betweenEnds)).contains(relationshipGUID),
                       "getRelationships (any type) between the two elements does not return the relationship");

            FindPropertyNamesProperties exactLabel = new FindPropertyNamesProperties();
            exactLabel.setPropertyValue(LABEL);
            exactLabel.setPropertyNames(List.of("label"));

            assertTrue(relationshipGUIDs(relationships("/relationships/with-exact-property-value", exactLabel)).contains(relationshipGUID),
                       "relationships/with-exact-property-value");
            assertEquals(List.of(relationshipGUID), relationshipGUIDs(relationships("/relationships/" + typeName + "/with-exact-property-value", exactLabel)),
                         "relationships/{type}/with-exact-property-value");

            FindPropertyNamesProperties searchLabel = new FindPropertyNamesProperties();
            searchLabel.setPropertyValue(OMAGPlatformExtension.RUN_ID);
            searchLabel.setPropertyNames(List.of("label"));

            assertTrue(relationshipGUIDs(relationships("/relationships/with-property-value-search", searchLabel)).contains(relationshipGUID),
                       "relationships/with-property-value-search");
            assertEquals(List.of(relationshipGUID), relationshipGUIDs(relationships("/relationships/" + typeName + "/with-property-value-search", searchLabel)),
                         "relationships/{type}/with-property-value-search");

            MetadataRelationshipSummary summary = explorer.post("/relationship-summaries/by-guid/" + relationshipGUID,
                                                                 new GetRequestBody(),
                                                                 MetadataRelationshipSummaryResponse.class).getRelationship();

            assertNotNull(summary);
            assertEquals(elementGUID, summary.getEnd1().getGUID());
            assertEquals(otherGUID, summary.getEnd2().getGUID());
            assertEquals(LABEL, assertInstanceOf(MoreInformationProperties.class, summary.getRelationshipProperties()).getLabel());

            /*
             * Related element queries
             */
            String related = "/elements/" + elementGUID + "/by-relationship";

            assertTrue(query(related).contains(otherGUID), "by-relationship (any type)");
            assertEquals(List.of(otherGUID), query(related + "/" + typeName + "?startingAtEnd=1"), "by-relationship/{type} starting at end 1");
            assertTrue(query(related + "/" + typeName + "?startingAtEnd=2").isEmpty(), "by-relationship/{type} starting at end 2 - the element is at end 1");

            assertEquals(List.of(otherGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements(related + "/" + typeName + "/with-exact-property-value", exactLabel)),
                         "by-relationship/{type}/with-exact-property-value");

            FindPropertyNamesProperties wrongLabel = new FindPropertyNamesProperties();
            wrongLabel.setPropertyValue(LABEL + "-wrong");
            wrongLabel.setPropertyNames(List.of("label"));

            assertTrue(explorer.elements(related + "/" + typeName + "/with-exact-property-value", wrongLabel).isEmpty(),
                       "by-relationship/{type}/with-exact-property-value matched the wrong label");

            assertEquals(List.of(otherGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements(related + "/" + typeName + "/with-property-value-search", searchLabel)),
                         "by-relationship/{type}/with-property-value-search");
        }
        finally
        {
            explorer.call("/elements/" + elementGUID + "/more-information/" + otherGUID + "/detach", new DeleteRelationshipRequestBody());
        }
    }


    @AfterAll
    static void deleteElements()
    {
        ClassificationExplorerFvtFixtures.deleteAsset(copyGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(elementGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(otherGUID);

        if (termGUID != null)
        {
            glossaryManager.call("/glossaries/terms/" + termGUID + "/delete", new DeleteElementRequestBody());
        }

        if (glossaryGUID != null)
        {
            collectionManager.call("/collections/" + glossaryGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + glossaryGUID + "/retrieve");
        }

        if (actorGUID != null)
        {
            actorManager.call("/actor-profiles/" + actorGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + actorGUID + "/retrieve");
        }

        ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(definitionGUID);
        ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(licenseTypeGUID);
        ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(certificationTypeGUID);
    }


    private static void assignTerm(String guid)
    {
        SemanticAssignmentProperties properties = new SemanticAssignmentProperties();

        properties.setExpression("customer");
        properties.setDescription("The data set describes customers.");
        properties.setTermAssignmentStatus(TermAssignmentStatus.VALIDATED);
        properties.setConfidence(90);
        properties.setSteward(STEWARD);

        explorer.call("/elements/" + guid + "/semantic-assignment/terms/" + termGUID + "/attach", relationship(properties));
    }


    private static List<String> meanings(String                            guid,
                                         SemanticAssignmentQueryProperties query)
    {
        return ClassificationExplorerFvtFixtures.guids(explorer.elements("/glossaries/terms/by-semantic-assignment/" + guid, query));
    }


    private static List<String> assignees(SemanticAssignmentQueryProperties query)
    {
        return ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-semantic-assignment/" + termGUID, query));
    }


    private static List<String> query(String path)
    {
        return ClassificationExplorerFvtFixtures.guids(explorer.elements(path, new ResultsRequestBody()));
    }


    private static NewRelationshipRequestBody relationship(RelationshipBeanProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    private static List<MetadataRelationshipSummary> relationships(String path,
                                                                   Object requestBody)
    {
        List<MetadataRelationshipSummary> relationships = explorer.post(path, requestBody, MetadataRelationshipSummariesResponse.class).getRelationships();

        return (relationships == null) ? List.of() : relationships;
    }


    private static List<String> relationshipGUIDs(List<MetadataRelationshipSummary> relationships)
    {
        List<String> guids = new ArrayList<>();

        for (MetadataRelationshipSummary relationship : relationships)
        {
            guids.add(relationship.getRelationshipHeader().getGUID());
        }

        return guids;
    }


    /**
     * The duplicate links between the two data sets, in either direction, seen by a duplicate-processing query.
     */
    private static List<MetadataRelationshipSummary> duplicateLinks(String typeName)
    {
        FindRelationshipRequestBody requestBody = new FindRelationshipRequestBody();

        requestBody.setEnd1EntityGUIDs(List.of(elementGUID, otherGUID));
        requestBody.setEnd2EntityGUIDs(List.of(elementGUID, otherGUID));
        requestBody.setForDuplicateProcessing(true);

        return relationships("/relationships/" + typeName, requestBody);
    }
}
