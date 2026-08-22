/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementGraph;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ContentPackTemplateFVT uses every template shipped in the content packs, the way a caller is meant to.
 * <br>
 * For each template it reads the placeholder specification attached to the template, supplies a value for
 * every placeholder, creates an element, and then checks the two things that decide whether templated
 * cataloguing has actually worked:
 * <ol>
 *     <li><b>No placeholder survived.</b> The whole anchored graph of the new element - not just its root -
 *     is searched for <code>~{...}~</code> markers, in entity properties, classification properties and
 *     relationship properties alike. A marker left behind means a value the template declared was never
 *     substituted, and the catalogued element carries a variable name where a real value should be.</li>
 *     <li><b>SourcedFrom is correctly linked.</b> The new element must be joined to the template it came from,
 *     with the new element at end 1 and the template at end 2. That link is what lets governance answer
 *     "where did this element come from?" later.</li>
 * </ol>
 * One test case per template, named after it, so a failure names the template that is broken rather than just
 * reporting that templating is broken.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ContentPackTemplateFVT
{
    /**
     * How many templates to take from the repository.  The content packs ship well under this; the cap is
     * there so that a repository which has accumulated a surprising amount of content still produces a run
     * that finishes.
     */
    private static final int MAX_TEMPLATES = 400;

    private static List<TemplateCatalog.Template> templates = null;


    /**
     * Every template in the repository, discovered once and shared by the test cases.
     *
     * @return templates
     * @throws Exception problem reading from the repository
     */
    static synchronized List<TemplateCatalog.Template> contentPackTemplates() throws Exception
    {
        if (templates == null)
        {
            templates = TemplateCatalog.findTemplates(ConnectorContextFactory.newContext(), MAX_TEMPLATES);
        }

        return templates;
    }


    /**
     * The content packs must actually contain templates.  Without this, a fault that made the discovery query
     * return nothing would leave the parameterised test below with no cases and the suite would pass while
     * testing nothing at all.
     *
     * @throws Exception problem reading from the repository
     */
    @Test
    void theContentPacksShipTemplates() throws Exception
    {
        List<TemplateCatalog.Template> found = contentPackTemplates();

        assertFalse(found.isEmpty(),
                    "No elements carrying the Template classification were found.  Either the content pack archives "
                            + "did not load, or the query for templates is not working - both of which would leave the "
                            + "rest of this class silently testing nothing.");
    }


    /**
     * A template is only usable if it declares what the caller has to supply.
     * <br>
     * The documented way to use a template is to read the {@code SpecificationPropertyAssignment}
     * relationships attached to it, take the ones marked {@code placeholderProperty}, and supply a value for
     * each.  A template that uses <code>~{...}~</code> markers but attaches no such specification cannot be
     * used that way: the caller has no way to discover what the template needs, and an element created
     * without those values carries variable names where real values belong.
     *
     * @throws Exception problem reading from the repository
     */
    @Test
    void everyTemplateThatUsesPlaceholdersDeclaresThem() throws Exception
    {
        List<String> undeclared = new ArrayList<>();

        for (TemplateCatalog.Template template : contentPackTemplates())
        {
            if ((! template.placeholdersWereDeclared()) && (! template.placeholders().isEmpty()))
            {
                List<String> names = new ArrayList<>();

                template.placeholders().forEach(placeholder -> names.add(placeholder.name()));

                undeclared.add(template.templateName() + " uses " + names);
            }
        }

        assertTrue(undeclared.isEmpty(),
                   undeclared.size() + " template(s) use placeholders but attach no placeholderProperty "
                           + "specification, so a caller reading the specification cannot tell what to supply: "
                           + undeclared);
    }


    /**
     * Use one template: supply its placeholders, create an element, and check the result.
     *
     * @param template template under test
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("contentPackTemplates")
    void templateCreatesAFullyResolvedElement(TemplateCatalog.Template template) throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        Map<String, String> placeholderValues = TemplateCatalog.getPlaceholderValues(template);

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        String newElementGUID = null;

        try
        {
            newElementGUID = openMetadataStore.createMetadataElementFromTemplate(template.typeName(),
                                                                                  templateOptions,
                                                                                  template.templateGUID(),
                                                                                  null,
                                                                                  null,
                                                                                  placeholderValues,
                                                                                  null);

            assertNotNull(newElementGUID, "Creating an element from template " + template.templateName()
                                  + " returned no GUID");

            /*
             * Creating from a template must produce a *new* element.  Getting the template's own GUID back
             * means the create matched an element that already existed rather than making one - which happens
             * when the qualified name it would have given the new element is not unique, and that in turn
             * happens when a placeholder in the qualified name was never substituted.  Saying so here is far
             * clearer than the wave of "unresolved placeholder" failures it otherwise produces.
             */
            assertNotEquals(template.templateGUID(), newElementGUID,
                            "Creating an element from template " + template.templateName() + " returned the template's "
                                    + "own GUID instead of a new element.  The template declares "
                                    + template.placeholders().size() + " placeholder(s), so the qualified name of the "
                                    + "element it would create is not unique.");

            /*
             * The whole anchored graph is checked, not just the root element.  A template usually brings a
             * cluster of elements with it - a connection, an endpoint, schema attributes - and a placeholder
             * left unresolved in one of those is just as broken as one left in the root.
             */
            OpenMetadataElementGraph graph = openMetadataStore.getAnchoredElementsGraph(newElementGUID, 0, 0);

            assertNotNull(graph, "The element created from " + template.templateName()
                                  + " has no anchored elements graph");

            List<String> unresolved = findUnresolvedPlaceholders(graph, template.templateGUID());

            assertTrue(unresolved.isEmpty(),
                       "Creating an element from template " + template.templateName() + " left placeholders "
                               + "unresolved, so the catalogued element carries variable names instead of values."
                               + "  Supplied: " + ((placeholderValues == null) ? "nothing" : placeholderValues.keySet())
                               + " (" + (template.placeholdersWereDeclared() ? "from the template's specification"
                                                 : "discovered from the template's own properties, it declares none")
                               + ").  Still unresolved: " + unresolved);

            assertSourcedFromIsLinked(openMetadataStore, newElementGUID, template);
        }
        finally
        {
            if (newElementGUID != null)
            {
                TemplatesFvtTestSupport.purgeElement(openMetadataStore, newElementGUID);
            }
        }
    }


    /**
     * Check the new element is joined to its template by SourcedFrom, in the right direction.
     *
     * @param openMetadataStore store to read through
     * @param newElementGUID the element that was created
     * @param template the template it came from
     * @throws Exception problem reading from the repository
     */
    private static void assertSourcedFromIsLinked(OpenMetadataStore        openMetadataStore,
                                                  String                   newElementGUID,
                                                  TemplateCatalog.Template template) throws Exception
    {
        var relationships = openMetadataStore.getMetadataElementRelationships(newElementGUID,
                                                                               template.templateGUID(),
                                                                               OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                                               0,
                                                                               0);

        assertNotNull(relationships, "No SourcedFrom relationship joins the new element to template "
                              + template.templateName());
        assertNotNull(relationships.getRelationships(), "No SourcedFrom relationship joins the new element to template "
                              + template.templateName());
        assertFalse(relationships.getRelationships().isEmpty(),
                    "No SourcedFrom relationship joins the new element to template " + template.templateName()
                            + " - nothing records where this element came from");

        /*
         * SourcedFrom runs from the new element (end 1, "templateCreatedElements") to the template it was
         * copied from (end 2, "sourcedFromTemplate").  A link the other way round would answer the provenance
         * question backwards.
         */
        OpenMetadataRelationship sourcedFrom = relationships.getRelationships().get(0);

        assertTrue((sourcedFrom.getElementGUIDAtEnd1() != null)
                           && sourcedFrom.getElementGUIDAtEnd1().equals(newElementGUID),
                   "SourcedFrom is linked backwards for template " + template.templateName()
                           + ": the new element should be at end 1 and the template at end 2");
        assertTrue((sourcedFrom.getElementGUIDAtEnd2() != null)
                           && sourcedFrom.getElementGUIDAtEnd2().equals(template.templateGUID()),
                   "SourcedFrom is linked backwards for template " + template.templateName()
                           + ": the new element should be at end 1 and the template at end 2");
    }


    /**
     * Search a whole anchored graph for placeholder markers that were never substituted.
     *
     * @param graph the graph of the new element and everything anchored to it
     * @return descriptions of what was found, empty if the graph is clean
     */
    private static List<String> findUnresolvedPlaceholders(OpenMetadataElementGraph graph,
                                                           String                   templateGUID)
    {
        List<String> unresolved = new ArrayList<>(findPlaceholders(graph));

        if (graph.getAnchoredElements() != null)
        {
            for (OpenMetadataElement anchoredElement : graph.getAnchoredElements())
            {
                /*
                 * The template itself is reachable from the new element - that is what the SourcedFrom
                 * relationship is for - and a template is *supposed* to be full of placeholder markers.
                 * Scanning it would report every template as broken.
                 */
                if ((anchoredElement != null) && templateGUID.equals(anchoredElement.getElementGUID()))
                {
                    continue;
                }

                unresolved.addAll(findPlaceholders(anchoredElement));
            }
        }

        if (graph.getRelationships() != null)
        {
            for (OpenMetadataRelationship relationship : graph.getRelationships())
            {
                unresolved.addAll(TemplatesFvtTestSupport.findPlaceholders(relationship.getType().getTypeName()
                                                                                    + " relationship properties",
                                                                            relationship.getRelationshipProperties()));
            }
        }

        return unresolved;
    }


    /**
     * Search one element - its own properties and those of its classifications - for placeholder markers.
     *
     * @param element element to search
     * @return descriptions of what was found, empty if the element is clean
     */
    private static List<String> findPlaceholders(OpenMetadataElement element)
    {
        List<String> unresolved = new ArrayList<>();

        if (element == null) return unresolved;

        String elementLabel = element.getType().getTypeName() + " " + element.getElementGUID();

        unresolved.addAll(TemplatesFvtTestSupport.findPlaceholders(elementLabel, element.getElementProperties()));

        if (element.getClassifications() != null)
        {
            element.getClassifications().forEach(classification ->
                unresolved.addAll(TemplatesFvtTestSupport.findPlaceholders(elementLabel + " ["
                                                                                    + classification.getClassificationName()
                                                                                    + " classification]",
                                                                            classification.getClassificationProperties())));
        }

        return unresolved;
    }
}
