/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.OrganizationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ApprovedPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.BusinessImperativeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ControlPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.EnforcementPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExceptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExceptionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ExecutionPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMetricProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernancePrincipleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceResultsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyAdministrationPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyDecisionPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyEnforcementPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyInformationPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyManagementPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PolicyRetrievalPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.RegulationCertificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.RegulationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.RegulatorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.VerificationPointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.rest.GovernanceActionProcessGraphResponse;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.guids;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.search;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.template;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.updateElement;

/**
 * GovernanceOfficerFVT covers every request of the Governance Officer OMVS: the governance definition
 * lifecycle; the links between governance definitions (peer and supporting definitions, regulators,
 * certification types required by a regulation); the links from definitions to the elements they govern
 * (governed-by, approved purposes, licenses, certifications, exceptions, governance results, designs and their
 * implementations); the ten governance-point classifications; and the governance action process graph.
 * <br>
 * The element that is governed, licensed, certified and classified is a software server asset made with Asset
 * Maker; the regulator is an organization made with Actor Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GovernanceOfficerFVT
{
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    private static final ViewServiceClient actorManager      = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    private static String principleGUID         = null;
    private static String principleCopyGUID     = null;
    private static String secondPrincipleGUID   = null;
    private static String imperativeGUID        = null;
    private static String regulationGUID        = null;
    private static String certificationTypeGUID = null;
    private static String licenseTypeGUID       = null;
    private static String exceptionTypeGUID     = null;
    private static String metricGUID            = null;
    private static String purposeGUID           = null;
    private static String regulatorGUID         = null;
    private static String serverGUID            = null;
    private static String processGUID           = null;


    /**
     * Create the definitions and elements that the tests link together.
     */
    @BeforeAll
    static void createElements()
    {
        secondPrincipleGUID   = definition("GovernancePrinciple", "share-by-default", GovernancePrincipleProperties::new);
        imperativeGUID        = definition("BusinessImperative", "serve-customers", BusinessImperativeProperties::new);
        regulationGUID        = definition("Regulation", "data-act", RegulationProperties::new);
        certificationTypeGUID = definition("CertificationType", "audited", CertificationTypeProperties::new);
        licenseTypeGUID       = definition("LicenseType", "internal-use", LicenseTypeProperties::new);
        exceptionTypeGUID     = definition("ExceptionType", "legacy-system", ExceptionTypeProperties::new);
        metricGUID            = definition("GovernanceMetric", "completeness", GovernanceMetricProperties::new);
        purposeGUID           = definition("DataProcessingPurpose", "billing", DataProcessingPurposeProperties::new);

        OrganizationProperties regulatorProperties = new OrganizationProperties();

        regulatorProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Organization", "regulator"));
        regulatorProperties.setDisplayName("Data regulator");

        regulatorGUID = actorManager.create("/actor-profiles", newElement(regulatorProperties));

        SoftwareServerProperties serverProperties = new SoftwareServerProperties();

        serverProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServer", "governed-server"));
        serverProperties.setDisplayName("Governed server");

        serverGUID = assetMaker.create("/assets", newElement(serverProperties));

        processGUID = definition("GovernanceActionProcess", "onboard-server", GovernanceActionProcessProperties::new);
    }


    /**
     * A governance principle is created, read back, found by name and by search string, updated, and copied
     * from a template.
     */
    @Test
    @Order(1)
    void governanceDefinitionLifecycle()
    {
        GovernancePrincipleProperties properties = new GovernancePrincipleProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GovernancePrinciple", "know-your-data"));
        properties.setDisplayName("Know your data");
        properties.setSummary("Every data set has an owner");

        principleGUID = governanceOfficer.create("/governance-definitions", newElement(properties));

        OpenMetadataRootElement principle = governanceDefinition(principleGUID);

        assertEquals(OpenMetadataType.GOVERNANCE_PRINCIPLE.typeName, principle.getElementHeader().getType().getTypeName());
        assertEquals("Every data set has an owner", ((GovernanceDefinitionProperties) principle.getProperties()).getSummary());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(principleGUID), List.copyOf(guids(governanceOfficer.elements("/governance-definitions/by-name", byName))));
        assertTrue(guids(governanceOfficer.elements("/governance-definitions/by-search-string", search())).contains(principleGUID));

        GovernancePrincipleProperties update = new GovernancePrincipleProperties();

        update.setSummary("Every data set has an owner and a steward");

        governanceOfficer.post("/governance-definitions/" + principleGUID + "/update", updateElement(update), BooleanResponse.class);

        GovernanceDefinitionProperties updated = (GovernanceDefinitionProperties) governanceDefinition(principleGUID).getProperties();

        assertEquals(update.getSummary(), updated.getSummary());
        assertEquals("Know your data", updated.getDisplayName(), "a merge update should keep the display name");

        GovernancePrincipleProperties replacement = new GovernancePrincipleProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("GovernancePrinciple", "know-your-data-copy"));

        principleCopyGUID = governanceOfficer.create("/governance-definitions/from-template", template(principleGUID, replacement));

        assertNotEquals(principleGUID, principleCopyGUID);

        OpenMetadataRootElement copy = governanceDefinition(principleCopyGUID);

        assertEquals(OpenMetadataType.GOVERNANCE_PRINCIPLE.typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), ((GovernanceDefinitionProperties) copy.getProperties()).getQualifiedName());
    }


    /**
     * Two principles are linked as peers with GovernancePolicyLink, and a business imperative is supported by a
     * principle through GovernanceResponse.
     */
    @Test
    @Order(2)
    void peerAndSupportingDefinitions()
    {
        PeerDefinitionProperties peerProperties = new PeerDefinitionProperties();

        peerProperties.setLabel("complements");

        String peerPath = "/governance-definitions/" + principleGUID + "/peer-definitions/" + OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName + "/" + secondPrincipleGUID;

        linkAndUnlink(peerPath, peerProperties, principleGUID, GovernanceOfficerFVT::governanceDefinition,
                      secondPrincipleGUID, GovernanceOfficerFVT::governanceDefinition, OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName);

        SupportingDefinitionProperties supportingProperties = new SupportingDefinitionProperties();

        supportingProperties.setRationale("Customers trust a business that knows its data");

        String supportingPath = "/governance-definitions/" + imperativeGUID + "/supporting-definitions/" + OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName + "/" + principleGUID;

        linkAndUnlink(supportingPath, supportingProperties, imperativeGUID, GovernanceOfficerFVT::governanceDefinition,
                      principleGUID, GovernanceOfficerFVT::governanceDefinition, OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName);
    }


    /**
     * The regulation names its regulator and the certification it requires.
     */
    @Test
    @Order(3)
    void regulationLinks()
    {
        RegulatorProperties regulatorProperties = new RegulatorProperties();

        regulatorProperties.setScope("National");

        linkAndUnlink("/regulations/" + regulationGUID + "/regulators/organizations/" + regulatorGUID, regulatorProperties,
                      regulationGUID, GovernanceOfficerFVT::governanceDefinition,
                      regulatorGUID, GovernanceOfficerFVT::actorProfile, OpenMetadataType.REGULATOR_RELATIONSHIP.typeName);

        linkAndUnlink("/regulations/" + regulationGUID + "/regulation-certification-types/" + certificationTypeGUID, new RegulationCertificationTypeProperties(),
                      regulationGUID, GovernanceOfficerFVT::governanceDefinition,
                      certificationTypeGUID, GovernanceOfficerFVT::governanceDefinition, OpenMetadataType.REGULATION_CERTIFICATION_TYPE.typeName);
    }


    /**
     * The server is governed by the principle, approved for the billing purpose, and is the data source of the
     * completeness metric's measurements.
     */
    @Test
    @Order(4)
    void governedElementLinks()
    {
        GovernedByProperties governedByProperties = new GovernedByProperties();

        governedByProperties.setLabel("owned");

        linkAndUnlink("/elements/" + serverGUID + "/governed-by/definition/" + principleGUID, governedByProperties,
                      principleGUID, GovernanceOfficerFVT::governanceDefinition,
                      serverGUID, GovernanceOfficerFVT::asset, OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName);

        linkAndUnlink("/elements/" + serverGUID + "/approved-purposes/" + purposeGUID, new ApprovedPurposeProperties(),
                      serverGUID, GovernanceOfficerFVT::asset,
                      purposeGUID, GovernanceOfficerFVT::governanceDefinition, OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.typeName);

        GovernanceResultsProperties resultsProperties = new GovernanceResultsProperties();

        resultsProperties.setQuery("select count(*) from servers");

        linkAndUnlink("/governance-metrics/" + metricGUID + "/measurements/" + serverGUID, resultsProperties,
                      metricGUID, GovernanceOfficerFVT::governanceDefinition,
                      serverGUID, GovernanceOfficerFVT::asset, OpenMetadataType.GOVERNANCE_RESULTS_RELATIONSHIP.typeName);
    }


    /**
     * The principle is implemented by the governance action process, which uses the server as an implementation
     * resource.
     */
    @Test
    @Order(5)
    void designAndImplementation()
    {
        ImplementedByProperties implementedByProperties = new ImplementedByProperties();

        implementedByProperties.setDesignStep("automation");

        linkAndUnlink("/designs/" + principleGUID + "/implementations/" + processGUID, implementedByProperties,
                      principleGUID, GovernanceOfficerFVT::governanceDefinition,
                      processGUID, GovernanceOfficerFVT::governanceDefinition, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName);

        linkAndUnlink("/designs/" + processGUID + "/implementation-resources/" + serverGUID, new ImplementationResourceProperties(),
                      processGUID, GovernanceOfficerFVT::governanceDefinition,
                      serverGUID, GovernanceOfficerFVT::asset, OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName);
    }


    /**
     * The server is licensed; the license is updated through its relationship GUID and then removed.
     */
    @Test
    @Order(6)
    void license()
    {
        LicenseProperties properties = new LicenseProperties();

        properties.setLicenseId("LIC-1");
        properties.setConditions("Internal use only");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        String licenseGUID = governanceOfficer.create("/elements/" + serverGUID + "/license-types/" + licenseTypeGUID + "/license", link);

        assertEquals("LIC-1", ((LicenseProperties) relationshipProperties(asset(serverGUID), licenseGUID)).getLicenseId());

        LicenseProperties update = new LicenseProperties();

        update.setLicenseId("LIC-2");

        UpdateRelationshipRequestBody updateBody = new UpdateRelationshipRequestBody();

        updateBody.setProperties(update);

        governanceOfficer.call("/licenses/" + licenseGUID + "/update", updateBody);

        LicenseProperties updated = (LicenseProperties) relationshipProperties(asset(serverGUID), licenseGUID);

        assertEquals("LIC-2", updated.getLicenseId());
        assertEquals("Internal use only", updated.getConditions(), "a merge update should keep the conditions");

        governanceOfficer.call("/licenses/" + licenseGUID + "/delete", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(serverGUID), OpenMetadataType.LICENSE_RELATIONSHIP.typeName, licenseTypeGUID);
    }


    /**
     * The server is certified; the certification is updated through its relationship GUID and then removed.
     */
    @Test
    @Order(7)
    void certification()
    {
        CertificationProperties properties = new CertificationProperties();

        properties.setCertificateId("CERT-1");
        properties.setConditions("Annual audit");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        String certificationGUID = governanceOfficer.create("/elements/" + serverGUID + "/certification-types/" + certificationTypeGUID + "/certify", link);

        assertEquals("CERT-1", ((CertificationProperties) relationshipProperties(asset(serverGUID), certificationGUID)).getCertificateId());

        CertificationProperties update = new CertificationProperties();

        update.setCertificateId("CERT-2");

        UpdateRelationshipRequestBody updateBody = new UpdateRelationshipRequestBody();

        updateBody.setProperties(update);

        governanceOfficer.call("/certifications/" + certificationGUID + "/update", updateBody);

        CertificationProperties updated = (CertificationProperties) relationshipProperties(asset(serverGUID), certificationGUID);

        assertEquals("CERT-2", updated.getCertificateId());
        assertEquals("Annual audit", updated.getConditions(), "a merge update should keep the conditions");

        governanceOfficer.call("/certifications/" + certificationGUID + "/delete", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(serverGUID), OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName, certificationTypeGUID);
    }


    /**
     * The server is granted an exception; the exception is updated through its relationship GUID and then
     * removed.
     */
    @Test
    @Order(8)
    void exception()
    {
        ExceptionProperties properties = new ExceptionProperties();

        properties.setLabel("legacy");
        properties.setConditions("Until the server is replaced");

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        String exceptionGUID = governanceOfficer.create("/elements/" + serverGUID + "/exceptions/" + exceptionTypeGUID + "/attach", link);

        assertEquals("legacy", ((ExceptionProperties) relationshipProperties(asset(serverGUID), exceptionGUID)).getLabel());

        ExceptionProperties update = new ExceptionProperties();

        update.setConditions("Until 2027");

        UpdateRelationshipRequestBody updateBody = new UpdateRelationshipRequestBody();

        updateBody.setProperties(update);

        governanceOfficer.call("/exceptions/" + exceptionGUID + "/update", updateBody);

        ExceptionProperties updated = (ExceptionProperties) relationshipProperties(asset(serverGUID), exceptionGUID);

        assertEquals("Until 2027", updated.getConditions());
        assertEquals("legacy", updated.getLabel(), "a merge update should keep the label");

        governanceOfficer.call("/exceptions/" + exceptionGUID + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(serverGUID), OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName, exceptionTypeGUID);
    }


    /**
     * Each of the ten governance-point classifications is set on the server, visible, and then removed.
     */
    @Test
    @Order(9)
    void governancePointClassifications()
    {
        classifyAndDeclassify("control-point", OpenMetadataType.CONTROL_POINT_CLASSIFICATION.typeName, ControlPointProperties::new);
        classifyAndDeclassify("verification-point", OpenMetadataType.VERIFICATION_POINT_CLASSIFICATION.typeName, VerificationPointProperties::new);
        classifyAndDeclassify("enforcement-point", OpenMetadataType.ENFORCEMENT_POINT_CLASSIFICATION.typeName, EnforcementPointProperties::new);
        classifyAndDeclassify("execution-point", OpenMetadataType.EXECUTION_POINT_CLASSIFICATION.typeName, ExecutionPointProperties::new);
        classifyAndDeclassify("policy-administration-point", OpenMetadataType.POLICY_ADMINISTRATION_POINT_CLASSIFICATION.typeName, PolicyAdministrationPointProperties::new);
        classifyAndDeclassify("policy-decision-point", OpenMetadataType.POLICY_DECISION_POINT_CLASSIFICATION.typeName, PolicyDecisionPointProperties::new);
        classifyAndDeclassify("policy-enforcement-point", OpenMetadataType.POLICY_ENFORCEMENT_POINT_CLASSIFICATION.typeName, PolicyEnforcementPointProperties::new);
        classifyAndDeclassify("policy-information-point", OpenMetadataType.POLICY_INFORMATION_POINT_CLASSIFICATION.typeName, PolicyInformationPointProperties::new);
        classifyAndDeclassify("policy-management-point", OpenMetadataType.POLICY_MANAGEMENT_POINT_CLASSIFICATION.typeName, PolicyManagementPointProperties::new);
        classifyAndDeclassify("policy-retrieval-point", OpenMetadataType.POLICY_RETRIEVAL_POINT_CLASSIFICATION.typeName, PolicyRetrievalPointProperties::new);
    }


    /**
     * The graph of a governance action process returns the process itself, with no steps since none were added.
     */
    @Test
    @Order(10)
    void governanceActionProcessGraph()
    {
        GovernanceActionProcessGraphResponse response = governanceOfficer.post("/governance-action-processes/" + processGUID + "/graph",
                                                                               new ResultsRequestBody(),
                                                                               GovernanceActionProcessGraphResponse.class);

        assertNotNull(response.getElement(), "getGovernanceActionProcessGraph returned no graph");
        assertNotNull(response.getElement().getGovernanceActionProcess(), "the graph has no process");
        assertEquals(processGUID, response.getElement().getGovernanceActionProcess().getElementHeader().getGUID());
        assertNull(response.getElement().getFirstProcessStep(), "a process with no steps reported a first step");
    }


    /**
     * Delete everything this class created.
     */
    @AfterAll
    static void deleteElements()
    {
        for (String guid : new String[]{principleGUID, principleCopyGUID, secondPrincipleGUID, imperativeGUID, regulationGUID,
                                        certificationTypeGUID, licenseTypeGUID, exceptionTypeGUID, metricGUID, purposeGUID, processGUID})
        {
            if (guid != null)
            {
                governanceOfficer.call("/governance-definitions/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + guid + "/retrieve");
            }
        }

        assetMaker.call("/assets/" + serverGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + serverGUID + "/retrieve");

        actorManager.call("/actor-profiles/" + regulatorGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(actorManager, "/actor-profiles/" + regulatorGUID + "/retrieve");
    }


    private static String definition(String                                   typeName,
                                     String                                   name,
                                     Supplier<? extends GovernanceDefinitionProperties> newProperties)
    {
        GovernanceDefinitionProperties properties = newProperties.get();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, name));
        properties.setDisplayName(name);

        String guid = governanceOfficer.create("/governance-definitions", newElement(properties));

        assertEquals(typeName, governanceDefinition(guid).getElementHeader().getType().getTypeName());

        return guid;
    }


    /**
     * Attach a relationship, check it from both ends, detach it and check it has gone from both ends.
     */
    private static void linkAndUnlink(String                                    path,
                                      RelationshipBeanProperties                properties,
                                      String                                    end1GUID,
                                      Function<String, OpenMetadataRootElement> retrieveEnd1,
                                      String                                    end2GUID,
                                      Function<String, OpenMetadataRootElement> retrieveEnd2,
                                      String                                    relationshipTypeName)
    {
        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        governanceOfficer.call(path + "/attach", link);

        assertLinked(retrieveEnd1.apply(end1GUID), relationshipTypeName, end2GUID);
        assertLinked(retrieveEnd2.apply(end2GUID), relationshipTypeName, end1GUID);

        governanceOfficer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieveEnd1.apply(end1GUID), relationshipTypeName, end2GUID);
        assertNotLinked(retrieveEnd2.apply(end2GUID), relationshipTypeName, end1GUID);
    }


    private static void classifyAndDeclassify(String                                         pathName,
                                              String                                         classificationName,
                                              Supplier<? extends ClassificationBeanProperties> newProperties)
    {
        NewClassificationRequestBody classify = new NewClassificationRequestBody();

        classify.setProperties(newProperties.get());

        governanceOfficer.call("/elements/" + serverGUID + "/" + pathName, classify);

        ElementClassification classification = ActorGovernanceFvtSupport.classification(asset(serverGUID), classificationName);

        assertNotNull(classification, pathName + " did not add the " + classificationName + " classification");

        governanceOfficer.call("/elements/" + serverGUID + "/" + pathName + "/remove", new DeleteClassificationRequestBody());

        assertNull(ActorGovernanceFvtSupport.classification(asset(serverGUID), classificationName),
                   pathName + "/remove left the " + classificationName + " classification in place");
    }


    /**
     * Return the properties of the relationship with the given GUID, found from one of its ends.
     */
    private static RelationshipBeanProperties relationshipProperties(OpenMetadataRootElement element,
                                                                     String                  relationshipGUID)
    {
        for (RelatedMetadataElementSummary summary : ActorGovernanceFvtSupport.relationships(element))
        {
            if ((summary.getRelationshipHeader() != null) && relationshipGUID.equals(summary.getRelationshipHeader().getGUID()))
            {
                assertNotNull(summary.getRelationshipProperties(), "relationship " + relationshipGUID + " has no properties");

                return summary.getRelationshipProperties();
            }
        }

        throw new AssertionError(element.getElementHeader().getGUID() + " has no relationship " + relationshipGUID);
    }


    private static OpenMetadataRootElement governanceDefinition(String guid)
    {
        OpenMetadataRootElement element = governanceOfficer.element("/governance-definitions/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "governance definition " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement asset(String guid)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "asset " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement actorProfile(String guid)
    {
        OpenMetadataRootElement element = actorManager.element("/actor-profiles/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "actor profile " + guid + " is missing");

        return element;
    }
}
