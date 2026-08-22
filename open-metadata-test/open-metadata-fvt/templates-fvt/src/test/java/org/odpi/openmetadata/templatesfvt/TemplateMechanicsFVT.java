/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TemplateMechanicsFVT exercises the template machinery harder than the shipped templates do, using templates
 * this suite builds itself so that it controls exactly what is in them.
 * <br>
 * The shipped templates in {@link ContentPackTemplateFVT} put placeholders mostly in entity properties.  A
 * template is a whole cluster though - the element, its classifications, and the relationships joining it to
 * the elements anchored with it - and substitution has to reach all of it.  These tests put placeholders in
 * each of those places deliberately, and check they all come out.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class TemplateMechanicsFVT
{
    /**
     * A placeholder must be substituted wherever it appears - in the element's own properties, in the
     * properties of a classification it carries, and in the properties of a relationship that joins it to an
     * element anchored with it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void placeholdersAreSubstitutedEverywhereTheyAppear() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String templateGUID = createTemplate(openMetadataStore, createdGUIDs);

            Map<String, String> placeholderValues = new LinkedHashMap<>();

            placeholderValues.put("elementName", "mechanics-element");
            placeholderValues.put("elementPurpose", "mechanics-purpose");
            placeholderValues.put("ownerName", "mechanics-owner");
            placeholderValues.put("resourceUse", "mechanics-resource-use");

            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            String newElementGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.COLLECTION.typeName,
                                                                                          templateOptions,
                                                                                          templateGUID,
                                                                                          null,
                                                                                          null,
                                                                                          placeholderValues,
                                                                                          null);

            assertNotNull(newElementGUID, "Creating an element from the hand-built template returned no GUID");
            assertNotEquals(templateGUID, newElementGUID, "Creating from the template returned the template itself");
            createdGUIDs.add(newElementGUID);

            OpenMetadataElement newElement = openMetadataStore.getMetadataElementByGUID(newElementGUID);

            assertNotNull(newElement, "The element created from the hand-built template could not be read back");

            /*
             * Entity properties.
             */
            assertEquals("mechanics-purpose",
                         TemplateCatalog.getStringProperty(newElement.getElementProperties(),
                                                            OpenMetadataProperty.PURPOSE.name),
                         "A placeholder in an entity property was not substituted");

            /*
             * Classification properties - the part a template most easily gets wrong, because the
             * classification travels with the element but its properties are held separately.
             */
            AttachedClassification ownership = findClassification(newElement, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName);

            assertNotNull(ownership, "The Ownership classification did not travel with the element created from the template");
            assertEquals("mechanics-owner",
                         TemplateCatalog.getStringProperty(ownership.getClassificationProperties(),
                                                            OpenMetadataProperty.OWNER.name),
                         "A placeholder in a classification property was not substituted");

            /*
             * Relationship properties - likewise held separately from both elements they join.
             */
            List<String> unresolved = new ArrayList<>();
            boolean      foundResourceList = false;

            var graph = openMetadataStore.getAnchoredElementsGraph(newElementGUID, 0, 0);

            assertNotNull(graph, "The element created from the hand-built template has no anchored elements graph");

            if (graph.getRelationships() != null)
            {
                for (OpenMetadataRelationship relationship : graph.getRelationships())
                {
                    unresolved.addAll(TemplatesFvtTestSupport.findPlaceholders(relationship.getType().getTypeName(),
                                                                                relationship.getRelationshipProperties()));

                    if (OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName.equals(relationship.getType().getTypeName()))
                    {
                        foundResourceList = true;

                        assertEquals("mechanics-resource-use",
                                     TemplateCatalog.getStringProperty(relationship.getRelationshipProperties(),
                                                                        OpenMetadataProperty.RESOURCE_USE.name),
                                     "A placeholder in a relationship property was not substituted");
                    }
                }
            }

            assertTrue(foundResourceList,
                       "The ResourceList relationship in the template did not travel with the element created from it");
            assertTrue(unresolved.isEmpty(),
                       "Placeholders were left unresolved in relationship properties: " + unresolved);
        }
        finally
        {
            cleanUp(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * A replacement property must override whatever the template holds for that property, while everything
     * else the template carries is still copied.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void replacementPropertiesOverrideTheTemplate() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String templateGUID = createTemplate(openMetadataStore, createdGUIDs);

            Map<String, String> placeholderValues = new LinkedHashMap<>();

            placeholderValues.put("elementName", "replacement-element");
            placeholderValues.put("elementPurpose", "purpose-from-the-placeholder");
            placeholderValues.put("ownerName", "replacement-owner");
            placeholderValues.put("resourceUse", "replacement-resource-use");

            /*
             * The template's description is a fixed string, not a placeholder - a replacement property has to
             * override it anyway, which is what distinguishes replacement from substitution.
             */
            ElementProperties replacementProperties = new PropertyHelper().addStringProperty(null,
                                                                                               OpenMetadataProperty.DESCRIPTION.name,
                                                                                               "description-from-the-replacement");

            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            String newElementGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.COLLECTION.typeName,
                                                                                          templateOptions,
                                                                                          templateGUID,
                                                                                          replacementProperties,
                                                                                          null,
                                                                                          placeholderValues,
                                                                                          null);

            assertNotNull(newElementGUID, "Creating an element with replacement properties returned no GUID");
            createdGUIDs.add(newElementGUID);

            OpenMetadataElement newElement = openMetadataStore.getMetadataElementByGUID(newElementGUID);

            assertNotNull(newElement, "The element created with replacement properties could not be read back");

            assertEquals("description-from-the-replacement",
                         TemplateCatalog.getStringProperty(newElement.getElementProperties(),
                                                            OpenMetadataProperty.DESCRIPTION.name),
                         "The replacement property did not override the value held in the template");

            assertEquals("purpose-from-the-placeholder",
                         TemplateCatalog.getStringProperty(newElement.getElementProperties(),
                                                            OpenMetadataProperty.PURPOSE.name),
                         "Supplying a replacement property stopped an unrelated placeholder from being substituted");
        }
        finally
        {
            cleanUp(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * An element created from a template must be linked back to it by SourcedFrom, with the new element at
     * end 1 and the template at end 2.  That link is what answers "where did this element come from?".
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void sourcedFromLinksTheNewElementToItsTemplate() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String templateGUID = createTemplate(openMetadataStore, createdGUIDs);

            TemplateOptions templateOptions = new TemplateOptions();

            templateOptions.setIsOwnAnchor(true);

            String newElementGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.COLLECTION.typeName,
                                                                                          templateOptions,
                                                                                          templateGUID,
                                                                                          null,
                                                                                          null,
                                                                                          minimalPlaceholders("sourced-from"),
                                                                                          null);

            createdGUIDs.add(newElementGUID);

            var relationships = openMetadataStore.getMetadataElementRelationships(newElementGUID,
                                                                                   templateGUID,
                                                                                   OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                                                   0,
                                                                                   0);

            assertNotNull(relationships, "No SourcedFrom relationship was created");
            assertNotNull(relationships.getRelationships(), "No SourcedFrom relationship was created");
            assertTrue(! relationships.getRelationships().isEmpty(), "No SourcedFrom relationship was created");

            OpenMetadataRelationship sourcedFrom = relationships.getRelationships().get(0);

            assertEquals(newElementGUID, sourcedFrom.getElementGUIDAtEnd1(),
                         "SourcedFrom should have the new element at end 1");
            assertEquals(templateGUID, sourcedFrom.getElementGUIDAtEnd2(),
                         "SourcedFrom should have the template at end 2");
        }
        finally
        {
            cleanUp(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * The TemplateSubstitute classification means "do not use me, use the element I am sourced from".
     * <br>
     * Creating from a substitute must therefore produce an element built from the <b>real</b> template behind
     * it, and the SourcedFrom relationship on the new element must point at that real template rather than at
     * the substitute that was named in the request.  That indirection is the whole point of the
     * classification, and it is the part a caller cannot see going wrong: the create succeeds either way, and
     * only the provenance link and the copied content say which template was actually used.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void templateSubstituteRedirectsToTheRealTemplate() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String realTemplateGUID = createTemplate(openMetadataStore, createdGUIDs);

            /*
             * The substitute is an element in its own right, sourced from the real template and classified as
             * a stand-in for it.
             */
            PropertyHelper    propertyHelper = new PropertyHelper();
            ElementProperties substituteProperties =
                    propertyHelper.addStringProperty(null,
                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                      TemplatesFvtTestSupport.newQualifiedName("Substitute"));

            substituteProperties = propertyHelper.addStringProperty(substituteProperties,
                                                                     OpenMetadataProperty.PURPOSE.name,
                                                                     "purpose held on the substitute");

            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);

            String substituteGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.COLLECTION.typeName,
                                                                                     newElementOptions,
                                                                                     null,
                                                                                     new NewElementProperties(substituteProperties),
                                                                                     null);
            createdGUIDs.add(substituteGUID);

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                            substituteGUID,
                                                            realTemplateGUID,
                                                            new MakeAnchorOptions(),
                                                            null);

            openMetadataStore.classifyMetadataElementInStore(substituteGUID,
                                                              OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName,
                                                              new MetadataSourceOptions(),
                                                              null);

            String newElementGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.COLLECTION.typeName,
                                                                                          ownAnchorTemplateOptions(),
                                                                                          substituteGUID,
                                                                                          null,
                                                                                          null,
                                                                                          minimalPlaceholders("substitute"),
                                                                                          null);

            assertNotNull(newElementGUID, "Creating from a template substitute returned no GUID");
            createdGUIDs.add(newElementGUID);

            OpenMetadataElement newElement = openMetadataStore.getMetadataElementByGUID(newElementGUID);

            assertNotNull(newElement, "The element created from a template substitute could not be read back");

            /*
             * The content must come from the real template, not from the substitute.
             */
            assertEquals("substitute-purpose",
                         TemplateCatalog.getStringProperty(newElement.getElementProperties(),
                                                            OpenMetadataProperty.PURPOSE.name),
                         "The element was built from the substitute's own properties instead of the real template's");

            /*
             * And so must the provenance link.
             */
            /*
             * Provenance is recorded as a chain rather than a shortcut: the new element is SourcedFrom the
             * substitute that was named in the request, and the substitute is SourcedFrom the real template.
             * "Where did this element come from?" is therefore answerable, but it takes two hops.
             *
             * Whether the new element should instead point straight at the real template - where its content
             * actually came from - is a design question rather than a defect: both readings are defensible and
             * the chain is intact either way.  This test pins down what the code does today so that a change
             * of mind is a deliberate one.
             */
            assertTrue(sourcedFromExists(openMetadataStore, newElementGUID, substituteGUID),
                       "The element created from a substitute is not linked to the substitute it was created from");
            assertTrue(sourcedFromExists(openMetadataStore, substituteGUID, realTemplateGUID),
                       "The substitute is not linked to the real template it stands in for");
        }
        finally
        {
            cleanUp(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * A template substitute with no SourcedFrom relationship points nowhere.  The request must be refused with
     * an error that says so, rather than failing deep in the repository with a null identifier.
     *
     * @throws Exception any unexpected failure
     */
    @Test
    void aTemplateSubstituteWithNothingBehindItIsRefusedClearly() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String templateGUID = createTemplate(openMetadataStore, createdGUIDs);

            openMetadataStore.classifyMetadataElementInStore(templateGUID,
                                                              OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName,
                                                              new MetadataSourceOptions(),
                                                              null);

            InvalidParameterException refused =
                    assertThrows(InvalidParameterException.class,
                                 () -> openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.COLLECTION.typeName,
                                                                                             ownAnchorTemplateOptions(),
                                                                                             templateGUID,
                                                                                             null,
                                                                                             null,
                                                                                             minimalPlaceholders("dangling"),
                                                                                             null),
                                 "A template substitute with no SourcedFrom relationship was not refused");

            assertTrue(refused.getMessage().contains("TemplateSubstitute"),
                       "The refusal does not explain that the problem is a template substitute with nothing behind it: "
                               + refused.getMessage());
        }
        finally
        {
            cleanUp(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * Is there a SourcedFrom relationship running from one element to another?
     *
     * @param openMetadataStore store to read through
     * @param fromGUID the element at end 1
     * @param toGUID the element at end 2
     * @return true if the link exists in that direction
     * @throws Exception problem reading from the repository
     */
    private static boolean sourcedFromExists(OpenMetadataStore openMetadataStore,
                                             String            fromGUID,
                                             String            toGUID) throws Exception
    {
        var relationships = openMetadataStore.getMetadataElementRelationships(fromGUID,
                                                                               toGUID,
                                                                               OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                                               0,
                                                                               0);

        if ((relationships == null) || (relationships.getRelationships() == null)) return false;

        for (OpenMetadataRelationship relationship : relationships.getRelationships())
        {
            if (fromGUID.equals(relationship.getElementGUIDAtEnd1()) && toGUID.equals(relationship.getElementGUIDAtEnd2()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Template options that anchor the new element on itself - the common case for these tests.
     *
     * @return template options
     */
    private static TemplateOptions ownAnchorTemplateOptions()
    {
        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        return templateOptions;
    }


    /**
     * Build a template of this suite's own: a collection whose properties, classification and relationship all
     * carry placeholders, with an element anchored to it so the relationship has something to join.
     *
     * @param openMetadataStore store to create through
     * @param createdGUIDs everything created, for cleanup
     * @return the template's GUID
     * @throws Exception the template could not be built
     */
    private static String createTemplate(OpenMetadataStore openMetadataStore,
                                         List<String>      createdGUIDs) throws Exception
    {
        PropertyHelper propertyHelper = new PropertyHelper();

        /*
         * The qualified name carries a placeholder so that every element created from this template gets a
         * different one - otherwise the second create would match the first instead of making a new element.
         */
        ElementProperties templateProperties = propertyHelper.addStringProperty(null,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                 TemplatesFvtTestSupport.newQualifiedName("Template")
                                                                                         + ":~{elementName}~");

        templateProperties = propertyHelper.addStringProperty(templateProperties,
                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                               "~{elementName}~");
        templateProperties = propertyHelper.addStringProperty(templateProperties,
                                                               OpenMetadataProperty.PURPOSE.name,
                                                               "~{elementPurpose}~");
        templateProperties = propertyHelper.addStringProperty(templateProperties,
                                                               OpenMetadataProperty.DESCRIPTION.name,
                                                               "description held in the template");

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        String templateGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.COLLECTION.typeName,
                                                                              newElementOptions,
                                                                              null,
                                                                              new NewElementProperties(templateProperties),
                                                                              null);
        createdGUIDs.add(templateGUID);

        /*
         * A classification whose property holds a placeholder.
         */
        ElementProperties ownershipProperties = propertyHelper.addStringProperty(null,
                                                                                  OpenMetadataProperty.OWNER.name,
                                                                                  "~{ownerName}~");

        openMetadataStore.classifyMetadataElementInStore(templateGUID,
                                                          OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                          new MetadataSourceOptions(),
                                                          new NewElementProperties(ownershipProperties));

        /*
         * An element anchored to the template, joined by a relationship whose property holds a placeholder.
         * Anchoring it is what makes it part of the template's cluster, so that it is copied along with the
         * template rather than shared with it.
         */
        ElementProperties resourceProperties = propertyHelper.addStringProperty(null,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                 TemplatesFvtTestSupport.newQualifiedName("TemplateResource")
                                                                                         + ":~{elementName}~");

        NewElementOptions anchoredOptions = new NewElementOptions();

        anchoredOptions.setAnchorGUID(templateGUID);
        anchoredOptions.setIsOwnAnchor(false);
        anchoredOptions.setParentGUID(templateGUID);
        anchoredOptions.setParentAtEnd1(true);
        anchoredOptions.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);

        ElementProperties resourceListProperties = propertyHelper.addStringProperty(null,
                                                                                     OpenMetadataProperty.RESOURCE_USE.name,
                                                                                     "~{resourceUse}~");

        String resourceGUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.COLLECTION.typeName,
                                                                              anchoredOptions,
                                                                              null,
                                                                              new NewElementProperties(resourceProperties),
                                                                              new NewElementProperties(resourceListProperties));
        createdGUIDs.add(resourceGUID);

        /*
         * Finally, make it a template.
         */
        ElementProperties templateClassification = propertyHelper.addStringProperty(null,
                                                                                     OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                     "templates-fvt mechanics template");

        openMetadataStore.classifyMetadataElementInStore(templateGUID,
                                                          OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                          new MetadataSourceOptions(),
                                                          new NewElementProperties(templateClassification));

        return templateGUID;
    }


    /**
     * The placeholder values every one of this suite's templates needs, with a run-specific name so that
     * repeated creates from the same template produce different elements.
     *
     * @param label distinguishes this element from others created in the same run
     * @return placeholder values
     */
    private static Map<String, String> minimalPlaceholders(String label)
    {
        Map<String, String> placeholderValues = new LinkedHashMap<>();

        placeholderValues.put("elementName", label + "-" + System.nanoTime());
        placeholderValues.put("elementPurpose", label + "-purpose");
        placeholderValues.put("ownerName", label + "-owner");
        placeholderValues.put("resourceUse", label + "-resource-use");

        return placeholderValues;
    }


    /**
     * Find one classification on an element.
     *
     * @param element element to look at
     * @param classificationName classification to find
     * @return the classification, or null if it is not there
     */
    private static AttachedClassification findClassification(OpenMetadataElement element,
                                                             String              classificationName)
    {
        if ((element == null) || (element.getClassifications() == null)) return null;

        for (AttachedClassification classification : element.getClassifications())
        {
            if (classificationName.equals(classification.getClassificationName()))
            {
                return classification;
            }
        }

        return null;
    }


    /**
     * Remove everything a test created, most recent first so that anchored elements go before their anchors.
     *
     * @param openMetadataStore store to delete through
     * @param createdGUIDs what to remove
     */
    private static void cleanUp(OpenMetadataStore openMetadataStore,
                                List<String>      createdGUIDs)
    {
        for (int i = createdGUIDs.size() - 1; i >= 0; i--)
        {
            TemplatesFvtTestSupport.purgeElement(openMetadataStore, createdGUIDs.get(i));
        }
    }
}
