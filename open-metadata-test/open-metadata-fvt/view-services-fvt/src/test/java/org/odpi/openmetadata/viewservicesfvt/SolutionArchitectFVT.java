/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionPortDirection;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SolutionActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortDelegationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementsResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SolutionArchitectFVT covers the Solution Architect OMVS requests for information supply chains, solution
 * blueprints, solution components (with their subcomponents, wires, actors and implementations) and solution
 * ports.  The design pattern and concept model requests of the same service are in
 * {@link SolutionArchitectDesignModelsFVT}.
 * <br>
 * The solution role that a component interacts with is created through Actor Manager's /solution-roles requests,
 * and the ImplementedBy relationship that getSolutionComponentImplementations returns is created through
 * Governance Officer, because Solution Architect has no requests for either.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SolutionArchitectFVT
{
    private static final ViewServiceClient solutionArchitect = new ViewServiceClient(ViewServiceDescription.SOLUTION_ARCHITECT);
    private static final ViewServiceClient actorManager      = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);
    private static final ViewServiceClient governanceOfficer = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);

    private static final String ISCS       = "/information-supply-chains/";
    private static final String BLUEPRINTS = "/solution-blueprints/";
    private static final String COMPONENTS = "/solution-components/";
    private static final String PORTS      = "/solution-ports/";

    /*
     * Everything created, deleted in reverse order of creation at the end.
     */
    private static final List<String> createdPaths = new ArrayList<>();

    private static String supplyChainGUID = null;
    private static String blueprintGUID   = null;
    private static String componentGUID   = null;
    private static String portGUID        = null;


    /**
     * Create an information supply chain, read it back (with and without its implementation), update it, find it,
     * copy it from a template and link it to a peer supply chain.
     */
    @Test
    @Order(1)
    void informationSupplyChains()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("InformationSupplyChain", "customer-onboarding");

        supplyChainGUID = create(ISCS, supplyChainProperties("customer-onboarding"));

        OpenMetadataRootElement supplyChain = DesignFvtSupport.assertReadable(solutionArchitect, retrieve(ISCS, supplyChainGUID),
                                                                             OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, qualifiedName);
        assertInstanceOf(InformationSupplyChainElement.class, supplyChain, "an information supply chain is returned with its implementation");

        InformationSupplyChainProperties updateProperties = new InformationSupplyChainProperties();

        updateProperties.setScope("Sales");
        updateProperties.setDataProcessingPurposes(List.of("onboarding"));

        assertTrue(solutionArchitect.post(ISCS + supplyChainGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateInformationSupplyChain reported no change");

        InformationSupplyChainProperties readBack = assertInstanceOf(InformationSupplyChainProperties.class,
                                                                     solutionArchitect.element(ISCS + supplyChainGUID + "/retrieve?addImplementation=false", new GetRequestBody()).getProperties());

        assertEquals("Sales", readBack.getScope());
        assertEquals(List.of("onboarding"), readBack.getDataProcessingPurposes());
        assertEquals("customer-onboarding", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(solutionArchitect.elements("/information-supply-chains/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        supplyChainGUID, "getInformationSupplyChainsByName");
        DesignFvtSupport.assertContains(solutionArchitect.elements("/information-supply-chains/by-search-string?addImplementation=false", DesignFvtSupport.runSearch()),
                                        supplyChainGUID, "findInformationSupplyChains");

        InformationSupplyChainProperties copyProperties = new InformationSupplyChainProperties();
        String                           copyName       = ViewServicesFvtTestSupport.qualifiedName("InformationSupplyChain", "customer-onboarding-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = createFromTemplate(ISCS, supplyChainGUID, copyProperties);

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(ISCS, copyGUID), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, copyName);

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            ISCS + supplyChainGUID + "/peer-links/" + copyGUID,
                                            retrieve(ISCS, supplyChainGUID),
                                            OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                            copyGUID,
                                            DesignFvtSupport.labeled(new InformationSupplyChainLinkProperties(), "continues as"));
    }


    /**
     * Create a solution component, read it back, update it, find it, copy it from a template and nest the copy
     * inside it as a subcomponent.
     */
    @Test
    @Order(2)
    void solutionComponents()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("SolutionComponent", "onboarding-service");

        componentGUID = create(COMPONENTS, componentProperties("onboarding-service"));

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(COMPONENTS, componentGUID), OpenMetadataType.SOLUTION_COMPONENT.typeName, qualifiedName);

        SolutionComponentProperties updateProperties = new SolutionComponentProperties();

        updateProperties.setDescription("Updated by SolutionArchitectFVT");
        updateProperties.setPlannedDeployedImplementationType("Java Application");

        assertTrue(solutionArchitect.post(COMPONENTS + componentGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateSolutionComponent reported no change");

        SolutionComponentProperties readBack = assertInstanceOf(SolutionComponentProperties.class, read(COMPONENTS, componentGUID).getProperties());

        assertEquals("Updated by SolutionArchitectFVT", readBack.getDescription());
        assertEquals("Java Application", readBack.getPlannedDeployedImplementationType());
        assertEquals("Microservice", readBack.getSolutionComponentType(), "a merge update should keep the component type");

        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-components/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        componentGUID, "getSolutionComponentsByName");
        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-components/by-search-string", DesignFvtSupport.runSearch()),
                                        componentGUID, "findSolutionComponents");

        SolutionComponentProperties copyProperties = new SolutionComponentProperties();
        String                      copyName       = ViewServicesFvtTestSupport.qualifiedName("SolutionComponent", "onboarding-service-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = createFromTemplate(COMPONENTS, componentGUID, copyProperties);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(solutionArchitect, retrieve(COMPONENTS, copyGUID), OpenMetadataType.SOLUTION_COMPONENT.typeName, copyName);

        assertEquals("Microservice", assertInstanceOf(SolutionComponentProperties.class, copy.getProperties()).getSolutionComponentType(),
                     "the copy should take the template's component type");

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            COMPONENTS + componentGUID + "/subcomponents/" + copyGUID,
                                            retrieve(COMPONENTS, componentGUID),
                                            OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                            copyGUID,
                                            DesignFvtSupport.roled(new SolutionCompositionProperties(), "validation step"));
    }


    /**
     * A solution linking wire is created between two components (returning its GUID), updated through its
     * relationship GUID and detached through it; detaching all the wires between two components removes every
     * wire between them.
     */
    @Test
    @Order(3)
    void solutionLinkingWires()
    {
        String otherGUID = create(COMPONENTS, componentProperties("customer-store"));
        String wirePath  = COMPONENTS + componentGUID + "/wired-to/" + otherGUID;

        SolutionLinkingWireProperties wireProperties = DesignFvtSupport.labeled(new SolutionLinkingWireProperties(), "saves customer");

        wireProperties.setProtocol("JDBC");

        String wireGUID = solutionArchitect.create(wirePath + "/attach", DesignFvtSupport.relationship(wireProperties));

        RelatedMetadataElementSummary wire = DesignFvtSupport.assertLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                                                           OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);
        assertEquals(wireGUID, wire.getRelationshipHeader().getGUID(), "linkSolutionLinkingWire returned the wrong relationship GUID");
        assertEquals("JDBC", assertInstanceOf(SolutionLinkingWireProperties.class, wire.getRelationshipProperties()).getProtocol());

        SolutionLinkingWireProperties updateProperties = new SolutionLinkingWireProperties();

        updateProperties.setProtocol("REST");
        updateProperties.setLabel("saves customer via API");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(updateProperties);

        solutionArchitect.call("/relationships/" + wireGUID + "/update", updateRequestBody);

        wire = DesignFvtSupport.assertLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                             OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);

        SolutionLinkingWireProperties updated = assertInstanceOf(SolutionLinkingWireProperties.class, wire.getRelationshipProperties());

        assertEquals("REST", updated.getProtocol());
        assertEquals("saves customer via API", updated.getLabel());

        solutionArchitect.call(COMPONENTS + "wires/" + wireGUID + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                         OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);

        /*
         * Two wires between the same components are both removed by the detach-all request.
         */
        String firstWireGUID  = solutionArchitect.create(wirePath + "/attach", DesignFvtSupport.relationship(DesignFvtSupport.labeled(new SolutionLinkingWireProperties(), "reads customer")));
        String secondWireGUID = solutionArchitect.create(wirePath + "/attach", DesignFvtSupport.relationship(DesignFvtSupport.labeled(new SolutionLinkingWireProperties(), "writes customer")));

        assertNotEquals(firstWireGUID, secondWireGUID, "a second wire between the same components should be a new relationship");

        long wireCount = DesignFvtSupport.relationships(read(COMPONENTS, componentGUID)).stream()
                .filter(relationship -> OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName.equals(relationship.getRelationshipHeader().getType().getTypeName()))
                .map(relationship -> relationship.getRelationshipHeader().getGUID())
                .distinct()
                .count();

        assertEquals(2, wireCount, "both wires should be visible from the component");

        solutionArchitect.call(wirePath + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                         OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);
    }


    /**
     * The request body of linkSolutionLinkingWire is optional; without one the wire is still created and its
     * GUID should be returned so that it can be updated or detached.
     */
    @Test
    @Order(4)
    void solutionLinkingWireWithoutRequestBody()
    {
        String otherGUID = create(COMPONENTS, componentProperties("customer-cache"));
        HttpHeaders jsonHeaders = new HttpHeaders();

        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        String wireGUID = solutionArchitect.create(COMPONENTS + componentGUID + "/wired-to/" + otherGUID + "/attach",
                                                   new HttpEntity<>(null, jsonHeaders));

        DesignFvtSupport.assertLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                      OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);

        solutionArchitect.call(COMPONENTS + "wires/" + wireGUID + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(solutionArchitect, retrieve(COMPONENTS, componentGUID),
                                         OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, otherGUID);
    }


    /**
     * A solution role (created with Actor Manager) interacts with a solution component.
     */
    @Test
    @Order(5)
    void solutionComponentActors()
    {
        SolutionActorRoleProperties roleProperties = new SolutionActorRoleProperties();

        roleProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SolutionActorRole", "onboarding-clerk"));
        roleProperties.setDisplayName("Onboarding clerk");

        String roleGUID = actorManager.create("/solution-roles", DesignFvtSupport.newElement(roleProperties));

        try
        {
            DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                                "/solution-roles/" + roleGUID + "/solution-component-actors/" + componentGUID,
                                                retrieve(COMPONENTS, componentGUID),
                                                OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                roleGUID,
                                                DesignFvtSupport.roled(new SolutionComponentActorProperties(), "enters customer details"));
        }
        finally
        {
            DesignFvtSupport.deleteAndCheck(actorManager, "/solution-roles/" + roleGUID, "/solution-roles/" + roleGUID + "/retrieve");
        }
    }


    /**
     * getSolutionComponentImplementations returns the elements linked to the component with ImplementedBy
     * (created here with Governance Officer).
     */
    @Test
    @Order(6)
    void solutionComponentImplementations()
    {
        assertTrue(implementations().isEmpty(), "a new component should have no implementations");

        String implementationGUID = create(COMPONENTS, componentProperties("onboarding-service-v1"));
        String implementationPath = "/designs/" + componentGUID + "/implementations/" + implementationGUID;

        governanceOfficer.call(implementationPath + "/attach",
                               DesignFvtSupport.relationship(DesignFvtSupport.roled(new ImplementedByProperties(), "deployed version")));

        List<RelatedMetadataElementSummary> implementations = implementations();

        assertTrue(implementations.stream().anyMatch(implementation -> implementationGUID.equals(implementation.getRelatedElement().getElementHeader().getGUID())),
                   "getSolutionComponentImplementations did not return the implementation");

        governanceOfficer.call(implementationPath + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(implementations().isEmpty(), "the implementation is still returned after it was detached");
    }


    /**
     * Create a solution blueprint, read it back, update it, find it, copy it from a template and link it to the
     * solution component it describes.
     */
    @Test
    @Order(7)
    void solutionBlueprints()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("SolutionBlueprint", "onboarding");

        SolutionBlueprintProperties properties = new SolutionBlueprintProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName("Onboarding blueprint");

        blueprintGUID = create(BLUEPRINTS, properties);

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(BLUEPRINTS, blueprintGUID), OpenMetadataType.SOLUTION_BLUEPRINT.typeName, qualifiedName);

        SolutionBlueprintProperties updateProperties = new SolutionBlueprintProperties();

        updateProperties.setDescription("Updated by SolutionArchitectFVT");

        assertTrue(solutionArchitect.post(BLUEPRINTS + blueprintGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateSolutionBlueprint reported no change");

        SolutionBlueprintProperties readBack = assertInstanceOf(SolutionBlueprintProperties.class, read(BLUEPRINTS, blueprintGUID).getProperties());

        assertEquals("Updated by SolutionArchitectFVT", readBack.getDescription());
        assertEquals("Onboarding blueprint", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-blueprints/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        blueprintGUID, "getSolutionBlueprintsByName");
        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-blueprints/by-search-string", DesignFvtSupport.runSearch()),
                                        blueprintGUID, "findSolutionBlueprints");

        SolutionBlueprintProperties copyProperties = new SolutionBlueprintProperties();
        String                      copyName       = ViewServicesFvtTestSupport.qualifiedName("SolutionBlueprint", "onboarding-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = createFromTemplate(BLUEPRINTS, blueprintGUID, copyProperties);

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(BLUEPRINTS, copyGUID), OpenMetadataType.SOLUTION_BLUEPRINT.typeName, copyName);

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            "/elements/" + componentGUID + "/solution-designs/" + blueprintGUID,
                                            retrieve(COMPONENTS, componentGUID),
                                            OpenMetadataType.SOLUTION_DESIGN_RELATIONSHIP.typeName,
                                            blueprintGUID,
                                            DesignFvtSupport.labeled(new SolutionDesignProperties(), "designed by"));
    }


    /**
     * Create a solution port, read it back, update it, find it, copy it from a template, attach it to the
     * component and delegate it to another port.
     */
    @Test
    @Order(8)
    void solutionPorts()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("SolutionPort", "customer-in");

        portGUID = create(PORTS, portProperties("customer-in"));

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(PORTS, portGUID), OpenMetadataType.SOLUTION_PORT.typeName, qualifiedName);

        SolutionPortProperties updateProperties = new SolutionPortProperties();

        updateProperties.setDescription("Updated by SolutionArchitectFVT");
        updateProperties.setDirection(SolutionPortDirection.OUTPUT);

        assertTrue(solutionArchitect.post(PORTS + portGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateSolutionPort reported no change");

        SolutionPortProperties readBack = assertInstanceOf(SolutionPortProperties.class, read(PORTS, portGUID).getProperties());

        assertEquals("Updated by SolutionArchitectFVT", readBack.getDescription());
        assertEquals(SolutionPortDirection.OUTPUT, readBack.getDirection());

        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-ports/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        portGUID, "getSolutionPortsByName");
        DesignFvtSupport.assertContains(solutionArchitect.elements("/solution-ports/by-search-string", DesignFvtSupport.runSearch()),
                                        portGUID, "findSolutionPorts");

        SolutionPortProperties copyProperties = new SolutionPortProperties();
        String                 copyName       = ViewServicesFvtTestSupport.qualifiedName("SolutionPort", "customer-in-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = createFromTemplate(PORTS, portGUID, copyProperties);

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(PORTS, copyGUID), OpenMetadataType.SOLUTION_PORT.typeName, copyName);

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            COMPONENTS + componentGUID + "/solution-ports/" + portGUID,
                                            retrieve(COMPONENTS, componentGUID),
                                            OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                            portGUID,
                                            DesignFvtSupport.roled(new SolutionComponentPortProperties(), "customer output"));

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PORTS + portGUID + "/port-delegations/" + copyGUID,
                                            retrieve(PORTS, portGUID),
                                            OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName,
                                            copyGUID,
                                            DesignFvtSupport.roled(new SolutionPortDelegationProperties(), "delegated output"));
    }


    /**
     * Delete everything this class created; deleted elements are no longer found.
     */
    @Test
    @Order(99)
    void deleteEverything()
    {
        for (int i = createdPaths.size() - 1; i >= 0; i--)
        {
            String elementPath = createdPaths.get(i);

            DesignFvtSupport.deleteAndCheck(solutionArchitect, elementPath, elementPath + "/retrieve");
        }

        if (supplyChainGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/information-supply-chains/by-search-string", DesignFvtSupport.runSearch()),
                                               supplyChainGUID, "findInformationSupplyChains after delete");
        }

        if (componentGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/solution-components/by-search-string", DesignFvtSupport.runSearch()),
                                               componentGUID, "findSolutionComponents after delete");
        }

        if (blueprintGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/solution-blueprints/by-search-string", DesignFvtSupport.runSearch()),
                                               blueprintGUID, "findSolutionBlueprints after delete");
        }

        if (portGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/solution-ports/by-search-string", DesignFvtSupport.runSearch()),
                                               portGUID, "findSolutionPorts after delete");
        }
    }


    /*
     * Helpers
     */

    private static List<RelatedMetadataElementSummary> implementations()
    {
        RelatedMetadataElementsResponse response = solutionArchitect.post(COMPONENTS + componentGUID + "/implementations",
                                                                          new ResultsRequestBody(),
                                                                          RelatedMetadataElementsResponse.class);

        return (response.getElements() == null) ? List.of() : response.getElements();
    }


    private static String create(String                  collection,
                                 ReferenceableProperties properties)
    {
        String guid = solutionArchitect.create(collection.substring(0, collection.length() - 1), DesignFvtSupport.newElement(properties));

        createdPaths.add(collection + guid);

        return guid;
    }


    private static String createFromTemplate(String                  collection,
                                             String                  templateGUID,
                                             ReferenceableProperties properties)
    {
        String guid = solutionArchitect.post(collection + "from-template",
                                             DesignFvtSupport.fromTemplate(templateGUID, properties),
                                             GUIDResponse.class).getGUID();

        assertNotNull(guid, collection + "from-template returned no GUID");
        assertNotEquals(templateGUID, guid);

        createdPaths.add(collection + guid);

        return guid;
    }


    private static InformationSupplyChainProperties supplyChainProperties(String name)
    {
        InformationSupplyChainProperties properties = new InformationSupplyChainProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("InformationSupplyChain", name));
        properties.setDisplayName(name);

        return properties;
    }


    private static SolutionComponentProperties componentProperties(String name)
    {
        SolutionComponentProperties properties = new SolutionComponentProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SolutionComponent", name));
        properties.setDisplayName(name);
        properties.setSolutionComponentType("Microservice");

        return properties;
    }


    private static SolutionPortProperties portProperties(String name)
    {
        SolutionPortProperties properties = new SolutionPortProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SolutionPort", name));
        properties.setDisplayName(name);
        properties.setDirection(SolutionPortDirection.INPUT);

        return properties;
    }


    private static OpenMetadataRootElement read(String collection,
                                                String guid)
    {
        OpenMetadataRootElement element = solutionArchitect.element(retrieve(collection, guid), new GetRequestBody());

        assertNotNull(element, collection + guid + " is missing");

        return element;
    }


    private static String retrieve(String collection,
                                   String guid)
    {
        return collection + guid + "/retrieve";
    }
}
