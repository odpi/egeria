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
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadAttributeLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadExtensionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadRelationshipEndProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptDesignProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptModelElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.IsAConceptBeadProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.TypedByConceptBeadProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.DesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.NestedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.RelatedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.SpecializedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SolutionArchitectDesignModelsFVT covers the Solution Architect OMVS requests for design patterns and concept
 * models: the lifecycle of design patterns and concept model elements (concept beads, their attributes and the
 * relationships between beads), the nested/specialized/related links between design patterns, the links that
 * build a concept model (relationship ends, typed-by, is-a, attribute links and extensions) and the concept
 * design link from an element to its concept model.  The rest of the service is covered by
 * {@link SolutionArchitectFVT}.
 * <br>
 * A concept model is a collection, so it is created and deleted with Collection Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SolutionArchitectDesignModelsFVT
{
    private static final ViewServiceClient solutionArchitect = new ViewServiceClient(ViewServiceDescription.SOLUTION_ARCHITECT);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static final String PATTERNS = "/design-patterns/";
    private static final String ELEMENTS = "/concept-model-elements/";

    private static final List<String> createdPatterns = new ArrayList<>();
    private static final List<String> createdElements = new ArrayList<>();

    private static String patternGUID         = null;
    private static String generalPatternGUID  = null;
    private static String beadGUID            = null;
    private static String otherBeadGUID       = null;
    private static String attributeGUID       = null;
    private static String beadRelationshipGUID = null;


    /**
     * Create a design pattern, read it back, update it, find it and copy it from a template.
     */
    @Test
    @Order(1)
    void designPatternLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("DesignPattern", "event-sourcing");

        patternGUID = createPattern("event-sourcing");

        DesignFvtSupport.assertReadable(solutionArchitect, PATTERNS + patternGUID, OpenMetadataType.DESIGN_PATTERN.typeName, qualifiedName);

        DesignPatternProperties updateProperties = new DesignPatternProperties();

        updateProperties.setProblemStatement("State changes must be auditable");
        updateProperties.setSolutionDescription("Store every change as an event");

        assertTrue(solutionArchitect.post(PATTERNS + patternGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateDesignPattern reported no change");

        DesignPatternProperties readBack = assertInstanceOf(DesignPatternProperties.class,
                                                            solutionArchitect.element(PATTERNS + patternGUID, new GetRequestBody()).getProperties());

        assertEquals("State changes must be auditable", readBack.getProblemStatement());
        assertEquals("Store every change as an event", readBack.getSolutionDescription());
        assertEquals("event-sourcing", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(solutionArchitect.elements("/design-patterns/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        patternGUID, "getDesignPatternsByName");
        DesignFvtSupport.assertContains(solutionArchitect.elements("/design-patterns/by-search-string", DesignFvtSupport.runSearch()),
                                        patternGUID, "findDesignPatterns");

        DesignPatternProperties copyProperties = new DesignPatternProperties();
        String                  copyName       = ViewServicesFvtTestSupport.qualifiedName("DesignPattern", "event-sourcing-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = solutionArchitect.create("/design-patterns/from-template", DesignFvtSupport.fromTemplate(patternGUID, copyProperties));

        createdPatterns.add(copyGUID);
        assertNotEquals(patternGUID, copyGUID);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(solutionArchitect, PATTERNS + copyGUID, OpenMetadataType.DESIGN_PATTERN.typeName, copyName);

        assertEquals("State changes must be auditable", assertInstanceOf(DesignPatternProperties.class, copy.getProperties()).getProblemStatement(),
                     "the copy should take the template's problem statement");
    }


    /**
     * Design patterns are nested, specialized and related to one another.
     */
    @Test
    @Order(2)
    void designPatternRelationships()
    {
        generalPatternGUID = createPattern("audit-trail");

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PATTERNS + generalPatternGUID + "/nested-design-patterns/" + patternGUID,
                                            PATTERNS + generalPatternGUID,
                                            OpenMetadataType.NESTED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                            patternGUID,
                                            DesignFvtSupport.labeled(new NestedDesignPatternProperties(), "uses"));

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PATTERNS + generalPatternGUID + "/specialized-design-patterns/" + patternGUID,
                                            PATTERNS + generalPatternGUID,
                                            OpenMetadataType.SPECIALIZED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                            patternGUID,
                                            null);

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PATTERNS + generalPatternGUID + "/related-design-patterns/" + patternGUID,
                                            PATTERNS + generalPatternGUID,
                                            OpenMetadataType.RELATED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                            patternGUID,
                                            null);
    }


    /**
     * The specialized and related design pattern links each have their own properties class with a label and
     * description, which should be stored with the relationship.
     */
    @Test
    @Order(3)
    void designPatternRelationshipProperties()
    {
        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PATTERNS + generalPatternGUID + "/specialized-design-patterns/" + patternGUID,
                                            PATTERNS + generalPatternGUID,
                                            OpenMetadataType.SPECIALIZED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                            patternGUID,
                                            DesignFvtSupport.labeled(new SpecializedDesignPatternProperties(), "is a kind of"));

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            PATTERNS + generalPatternGUID + "/related-design-patterns/" + patternGUID,
                                            PATTERNS + generalPatternGUID,
                                            OpenMetadataType.RELATED_DESIGN_PATTERN_RELATIONSHIP.typeName,
                                            patternGUID,
                                            DesignFvtSupport.labeled(new RelatedDesignPatternProperties(), "often used with"));
    }


    /**
     * Create concept beads, a bead attribute and a bead relationship; read one back, update it, find it and copy
     * it from a template.
     */
    @Test
    @Order(4)
    void conceptModelElementLifecycle()
    {
        String qualifiedName = ViewServicesFvtTestSupport.qualifiedName("ConceptBead", "customer");

        beadGUID             = createElement(new ConceptBeadProperties(), "customer");
        otherBeadGUID        = createElement(new ConceptBeadProperties(), "account");
        attributeGUID        = createElement(new ConceptBeadAttributeProperties(), "customer-name");
        beadRelationshipGUID = createElement(new ConceptBeadRelationshipProperties(), "customer-holds-account");

        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(beadGUID), OpenMetadataType.CONCEPT_BEAD.typeName, qualifiedName);
        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(attributeGUID), OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE.typeName,
                                        ViewServicesFvtTestSupport.qualifiedName("ConceptBeadAttribute", "customer-name"));
        DesignFvtSupport.assertReadable(solutionArchitect, retrieve(beadRelationshipGUID), OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP.typeName,
                                        ViewServicesFvtTestSupport.qualifiedName("ConceptBeadRelationship", "customer-holds-account"));

        ConceptBeadProperties updateProperties = new ConceptBeadProperties();

        updateProperties.setDescription("Updated by SolutionArchitectDesignModelsFVT");
        updateProperties.setCanonicalName("Customer");

        assertTrue(solutionArchitect.post(ELEMENTS + beadGUID + "/update", DesignFvtSupport.update(updateProperties), BooleanResponse.class).getFlag(),
                   "updateConceptModelElement reported no change");

        ConceptBeadProperties readBack = assertInstanceOf(ConceptBeadProperties.class,
                                                          solutionArchitect.element(retrieve(beadGUID), new GetRequestBody()).getProperties());

        assertEquals("Updated by SolutionArchitectDesignModelsFVT", readBack.getDescription());
        assertEquals("Customer", readBack.getCanonicalName());
        assertEquals("customer", readBack.getDisplayName(), "a merge update should keep the display name");

        DesignFvtSupport.assertContains(solutionArchitect.elements("/concept-model-elements/by-name", DesignFvtSupport.byName(qualifiedName)),
                                        beadGUID, "getConceptModelElementsByName");

        List<OpenMetadataRootElement> found = solutionArchitect.elements("/concept-model-elements/by-search-string", DesignFvtSupport.runSearch());

        for (String guid : List.of(beadGUID, otherBeadGUID, attributeGUID, beadRelationshipGUID))
        {
            DesignFvtSupport.assertContains(found, guid, "findConceptModelElements");
        }

        ConceptBeadProperties copyProperties = new ConceptBeadProperties();
        String                copyName       = ViewServicesFvtTestSupport.qualifiedName("ConceptBead", "customer-copy");

        copyProperties.setQualifiedName(copyName);

        String copyGUID = solutionArchitect.create("/concept-model-elements/from-template", DesignFvtSupport.fromTemplate(beadGUID, copyProperties));

        createdElements.add(copyGUID);
        assertNotEquals(beadGUID, copyGUID);

        OpenMetadataRootElement copy = DesignFvtSupport.assertReadable(solutionArchitect, retrieve(copyGUID), OpenMetadataType.CONCEPT_BEAD.typeName, copyName);

        assertEquals("Customer", assertInstanceOf(ConceptBeadProperties.class, copy.getProperties()).getCanonicalName(),
                     "the copy should take the template's canonical name");
    }


    /**
     * The links that build a concept model: a bead relationship's ends, an attribute's type, is-a, attribute
     * links and extensions.
     */
    @Test
    @Order(5)
    void conceptBeadLinks()
    {
        ConceptBeadRelationshipEndProperties endProperties = new ConceptBeadRelationshipEndProperties();

        endProperties.setAttributeName("holder");
        endProperties.setPosition(1);

        String endPath = "/concept-bead-relationships/" + beadRelationshipGUID + "/relationship-ends/" + beadGUID;

        solutionArchitect.call(endPath + "/attach", DesignFvtSupport.relationship(endProperties));

        RelatedMetadataElementSummary end = DesignFvtSupport.assertLinked(solutionArchitect, retrieve(beadRelationshipGUID),
                                                                          OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP_END_RELATIONSHIP.typeName, beadGUID);

        assertEquals("holder", assertInstanceOf(ConceptBeadRelationshipEndProperties.class, end.getRelationshipProperties()).getAttributeName());

        solutionArchitect.call(endPath + "/detach", new DeleteRelationshipRequestBody());

        DesignFvtSupport.assertNotLinked(solutionArchitect, retrieve(beadRelationshipGUID),
                                         OpenMetadataType.CONCEPT_BEAD_RELATIONSHIP_END_RELATIONSHIP.typeName, beadGUID);

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            "/concept-bead-attributes/" + attributeGUID + "/typed-by/" + otherBeadGUID,
                                            retrieve(attributeGUID),
                                            OpenMetadataType.TYPED_BY_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                            otherBeadGUID,
                                            new TypedByConceptBeadProperties());

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            "/concept-beads/" + beadGUID + "/is-a/" + otherBeadGUID,
                                            retrieve(beadGUID),
                                            OpenMetadataType.IS_A_CONCEPT_BEAD_RELATIONSHIP.typeName,
                                            otherBeadGUID,
                                            new IsAConceptBeadProperties());

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            "/concept-beads/" + beadGUID + "/attribute-links/" + attributeGUID,
                                            retrieve(beadGUID),
                                            OpenMetadataType.CONCEPT_BEAD_ATTRIBUTE_LINK_RELATIONSHIP.typeName,
                                            attributeGUID,
                                            DesignFvtSupport.positioned(new ConceptBeadAttributeLinkProperties(), 1));

        DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                            "/concept-beads/" + beadGUID + "/extensions/" + otherBeadGUID,
                                            retrieve(beadGUID),
                                            OpenMetadataType.CONCEPT_BEAD_EXTENSION_RELATIONSHIP.typeName,
                                            otherBeadGUID,
                                            DesignFvtSupport.roled(new ConceptBeadExtensionProperties(), "account details"));
    }


    /**
     * ConceptDesign links an element (here a concept bead) to the concept model (created with Collection
     * Manager) that describes it.
     */
    @Test
    @Order(6)
    void conceptDesign()
    {
        ConceptModelProperties modelProperties = new ConceptModelProperties();

        modelProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ConceptModel", "customer-model"));
        modelProperties.setDisplayName("Customer concept model");

        String modelGUID = collectionManager.create("/collections", DesignFvtSupport.newElement(modelProperties));

        try
        {
            DesignFvtSupport.linkCheckAndDetach(solutionArchitect,
                                                "/elements/" + beadGUID + "/concept-designs/" + modelGUID,
                                                retrieve(beadGUID),
                                                OpenMetadataType.CONCEPT_DESIGN_RELATIONSHIP.typeName,
                                                modelGUID,
                                                DesignFvtSupport.labeled(new ConceptDesignProperties(), "modelled in"));
        }
        finally
        {
            DesignFvtSupport.deleteAndCheck(collectionManager, "/collections/" + modelGUID, "/collections/" + modelGUID + "/retrieve");
        }
    }


    /**
     * Delete everything this class created; deleted elements are no longer found.
     */
    @Test
    @Order(99)
    void deleteEverything()
    {
        for (String guid : createdElements)
        {
            DesignFvtSupport.deleteAndCheck(solutionArchitect, ELEMENTS + guid, retrieve(guid));
        }

        for (String guid : createdPatterns)
        {
            DesignFvtSupport.deleteAndCheck(solutionArchitect, PATTERNS + guid, PATTERNS + guid);
        }

        if (beadGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/concept-model-elements/by-search-string", DesignFvtSupport.runSearch()),
                                               beadGUID, "findConceptModelElements after delete");
        }

        if (patternGUID != null)
        {
            DesignFvtSupport.assertNotContains(solutionArchitect.elements("/design-patterns/by-search-string", DesignFvtSupport.runSearch()),
                                               patternGUID, "findDesignPatterns after delete");
        }
    }


    /*
     * Helpers
     */

    private static String createPattern(String name)
    {
        DesignPatternProperties properties = new DesignPatternProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DesignPattern", name));
        properties.setDisplayName(name);

        String guid = solutionArchitect.create("/design-patterns", DesignFvtSupport.newElement(properties));

        createdPatterns.add(0, guid);

        return guid;
    }


    private static String createElement(ConceptModelElementProperties properties,
                                        String                        name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getTypeName(), name));
        properties.setDisplayName(name);

        String guid = solutionArchitect.create("/concept-model-elements", DesignFvtSupport.newElement(properties));

        assertNotNull(guid);
        createdElements.add(0, guid);

        return guid;
    }


    private static String retrieve(String guid)
    {
        return ELEMENTS + guid + "/retrieve";
    }
}
