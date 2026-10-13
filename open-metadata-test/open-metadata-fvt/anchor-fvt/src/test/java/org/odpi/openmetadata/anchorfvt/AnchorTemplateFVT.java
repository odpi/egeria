/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A templated create copies a template's anchored graph, and the copy is a graph of its own: anchored in the same
 * shape as the template, linked to the same outside elements, and independent of the template when either is
 * deleted or archived.
 * <br><br>
 * The template is the graph the rest of the suite uses, with a {@code ~{copyName}~} placeholder in every qualified
 * name so that each copy is named after the value supplied for it.  Two kinds of template are covered:
 * <ul>
 *     <li>The database schema - the anchor of its graph.  The whole graph is copied, including the endpoint,
 *     which is anchored to the connection rather than to the database schema; the glossary term, which has an
 *     anchor of its own, is linked rather than copied.</li>
 *     <li>A table - a member of the database schema's graph.  Only the table's own part of the graph is copied:
 *     the table and what lies downstream of it.  Its links to the rest of the template's graph - its schema type,
 *     the column its foreign key points at, the table its lineage mapping points at - belong to the template and
 *     are not reproduced.  The copy is attached and anchored where the caller says.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchorTemplateFVT
{
    private static final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Copying the database schema copies every element of its graph, each with its own name, and joins the
     * copies to each other exactly as the template's elements are joined.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void templatedCreateCopiesTheWholeAnchoredGraph() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "CopyWholeGraph");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            String copyMarker = AnchorFvtTestSupport.getCopyMarker(template, "copy1");

            for (String role : copy.roles())
            {
                if (AnchoredGraph.GLOSSARY_TERM.equals(role))
                {
                    assertEquals(template.guid(role), copy.guid(role), "The glossary term should be linked to the copy, not copied");
                }
                else
                {
                    assertNotEquals(template.guid(role), copy.guid(role), role + " was not copied - the copy is linked to the template's " + role);
                    assertEquals(copyMarker + role, copy.qualifiedNames().get(role), role + " was not named from the placeholder");
                }
            }

            assertEquals(Set.of(copy.guid(AnchoredGraph.COLUMN2A)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, copy.guid(AnchoredGraph.COLUMN1A), OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, true),
                         "The copied foreign key should join the copied columns");
            assertEquals(Set.of(copy.guid(AnchoredGraph.TABLE2)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, copy.guid(AnchoredGraph.TABLE1), OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, true),
                         "The copied lineage mapping should join the copied tables");
            assertEquals(Set.of(template.guid(AnchoredGraph.DATABASE_SCHEMA)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, copy.guid(AnchoredGraph.DATABASE_SCHEMA), OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName, true),
                         "The copy should be SourcedFrom the template");

            assertEquals(template.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, template),
                         "Copying should leave the template in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * A templated create is one operation: every element it creates carries the same lastOperationId, which is how
     * the scope of the copy can be checked afterwards.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void templatedCreateStampsOneOperationId() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "CopyOperationId");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            Map<String, String> operationIds = AnchorFvtTestSupport.findLastOperationIds(openMetadataStore,
                                                                                         AnchorFvtTestSupport.getCopyMarker(template, "copy1"),
                                                                                         List.of(ElementStatus.ACTIVE));

            assertEquals(copy.roles().size() - 1, operationIds.size(), "Every element but the glossary term should be copied: " + operationIds.keySet());
            assertEquals(1, new java.util.HashSet<>(operationIds.values()).size(), "The copy wrote more than one lastOperationId: " + operationIds);
            assertTrue(operationIds.values().iterator().next() != null, "The copy wrote no lastOperationId");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * The copy is anchored in the same shape as the template: the endpoint is anchored to the copied connection,
     * every other copy to the new database schema.  The template's anchors are unchanged.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void templatedCreateMirrorsTheTemplatesAnchors() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "MirrorAnchors");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            for (AnchoredGraph graph : List.of(template, copy))
            {
                String graphName = (graph == template) ? "template " : "copy ";

                for (String role : graph.roles())
                {
                    String expectedAnchorRole = switch (role)
                    {
                        case AnchoredGraph.DATABASE_SCHEMA, AnchoredGraph.GLOSSARY_TERM -> role;
                        case AnchoredGraph.ENDPOINT -> AnchoredGraph.CONNECTION;
                        default -> AnchoredGraph.DATABASE_SCHEMA;
                    };

                    String anchorGUID = AnchorFvtTestSupport.getAnchorGUID(openMetadataStore.getMetadataElementByGUID(graph.guid(role)));

                    if (expectedAnchorRole.equals(role))
                    {
                        assertTrue((anchorGUID == null) || anchorGUID.equals(graph.guid(role)),
                                   graphName + role + " should be its own anchor, not anchored to " + anchorGUID);
                    }
                    else
                    {
                        assertEquals(graph.guid(expectedAnchorRole),
                                     anchorGUID,
                                     graphName + role + " should be anchored to its " + expectedAnchorRole);
                    }
                }
            }
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * Deleting the copy takes only the copy.  It is SourcedFrom the template, and the template's database schema
     * is at end 2 of that relationship, but it is anchored to itself, not to anything being deleted.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfCopyLeavesTheTemplate() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "DeleteCopy");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            openMetadataStore.deleteMetadataElementInStore(copy.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(AnchoredGraph.GLOSSARY_TERM),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, copy),
                         "Deleting the copy should take all of it except the glossary term it is linked to");
            assertEquals(template.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, template),
                         "Deleting the copy should leave the template in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * Deleting the template takes only the template.  Nothing in the copy is anchored to it.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfTemplateLeavesTheCopy() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "DeleteTemplate");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            openMetadataStore.deleteMetadataElementInStore(template.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(AnchoredGraph.GLOSSARY_TERM),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, template),
                         "Deleting the template should take all of it except the glossary term");
            assertEquals(copy.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, copy),
                         "Deleting the template should leave the copy in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * The lineage mapping was copied with the tables, so an ordinary delete of the copied table1 is an archive -
     * and it archives the copied table and its columns, nothing else in the copy and nothing in the template.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void archiveOfCopiedMemberStaysInTheCopy() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     copy              = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "ArchiveCopiedMember");
            copy     = this.createCopy(openMetadataStore, template, "copy1");

            openMetadataStore.deleteMetadataElementInStore(copy.guid(AnchoredGraph.TABLE1),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.LOOK_FOR_LINEAGE, false));

            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore,
                                                       copy,
                                                       Set.of(AnchoredGraph.TABLE1, AnchoredGraph.COLUMN1A, AnchoredGraph.COLUMN1B));
            AnchorFvtTestSupport.checkArchivedElements(openMetadataStore, template, Set.of());
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, copy);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * Copying a table - a member of the template's graph - into another database schema copies the table and its
     * columns and nothing else.  The copies are anchored to the database schema they were added to and attached
     * under its schema type; the table keeps its link to the glossary term, which is outside the template's graph,
     * but none of its links into the template's graph: not to the template's schema type, not to the column its
     * foreign key points at, not to the table its lineage mapping points at.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void templatedCreateFromMemberCopiesOnlyItsOwnPart() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     target            = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "CopyMember");
            target   = this.createCopy(openMetadataStore, template, "target");

            String tableGUID   = this.createTableCopy(openMetadataStore, template, target, "added");
            String addedMarker = AnchorFvtTestSupport.getCopyMarker(template, "added");

            assertEquals(Set.of(addedMarker + AnchoredGraph.TABLE1, addedMarker + AnchoredGraph.COLUMN1A, addedMarker + AnchoredGraph.COLUMN1B),
                         AnchorFvtTestSupport.findQualifiedNames(openMetadataStore, addedMarker, List.of(ElementStatus.ACTIVE)),
                         "Copying table1 should copy the table and its columns only");

            Set<String> columnGUIDs = AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, tableGUID, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName, true);

            assertEquals(2, columnGUIDs.size(), "The added table should have its two columns");

            List<String> addedGUIDs = new ArrayList<>(columnGUIDs);

            addedGUIDs.add(tableGUID);

            for (String guid : addedGUIDs)
            {
                assertEquals(target.guid(AnchoredGraph.DATABASE_SCHEMA),
                             AnchorFvtTestSupport.getAnchorGUID(openMetadataStore.getMetadataElementByGUID(guid)),
                             guid + " should be anchored to the database schema it was added to");

                assertTrue(AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, guid, OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName, true).isEmpty(),
                           guid + " should have no foreign key - its target is in the template's graph");
            }

            assertEquals(Set.of(target.guid(AnchoredGraph.SCHEMA_TYPE)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, tableGUID, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, false),
                         "The added table should be attached under the target's schema type only");
            assertTrue(AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, tableGUID, OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName, true).isEmpty(),
                       "The added table should have no lineage mapping - its target is in the template's graph");
            assertEquals(Set.of(template.guid(AnchoredGraph.GLOSSARY_TERM)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, tableGUID, OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName, true),
                         "The added table should keep its link to the glossary term");

            assertEquals(Set.of(template.guid(AnchoredGraph.TABLE1), template.guid(AnchoredGraph.TABLE2)),
                         AnchorFvtTestSupport.getLinkedGUIDs(openMetadataStore, template.guid(AnchoredGraph.SCHEMA_TYPE), OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, true),
                         "The template's schema type should still have just its own two tables");
            assertEquals(template.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, template),
                         "Copying table1 should leave the template in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, target);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * A table added to a database schema from a template is part of that database schema's graph, so it goes when
     * the database schema is deleted - and the template, which it was copied from, stays.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void deleteOfAnchorTakesMembersAddedFromTemplate() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     template          = null;
        AnchoredGraph     target            = null;

        try
        {
            template = AnchorFvtTestSupport.buildTemplateGraph(openMetadataStore, "DeleteAddedMember");
            target   = this.createCopy(openMetadataStore, template, "target");

            this.createTableCopy(openMetadataStore, template, target, "added");

            openMetadataStore.deleteMetadataElementInStore(target.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                           AnchorFvtTestSupport.getDeleteOptions(DeleteMethod.SOFT_DELETE, true));

            assertEquals(Set.of(),
                         AnchorFvtTestSupport.findQualifiedNames(openMetadataStore, AnchorFvtTestSupport.getCopyMarker(template, "added"), List.of(ElementStatus.ACTIVE)),
                         "The table added from the template should go with the database schema it was added to");
            assertEquals(template.roles(),
                         AnchorFvtTestSupport.getActiveRoles(openMetadataStore, template),
                         "Deleting the target should leave the template in place");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, target);
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, template);
        }
    }


    /**
     * Create a copy of the whole template graph, as its own anchor.
     *
     * @param openMetadataStore store to create through
     * @param template template graph
     * @param copyName value for the copy name placeholder
     * @return the copy
     * @throws Exception problem talking to the server
     */
    private AnchoredGraph createCopy(OpenMetadataStore openMetadataStore,
                                     AnchoredGraph     template,
                                     String            copyName) throws Exception
    {
        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setIsOwnAnchor(true);

        String copyGUID = openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                              templateOptions,
                                                                              template.guid(AnchoredGraph.DATABASE_SCHEMA),
                                                                              propertyHelper.addStringProperty(null,
                                                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                               AnchorFvtTestSupport.getCopyMarker(template, copyName) + AnchoredGraph.DATABASE_SCHEMA),
                                                                              null,
                                                                              Map.of(AnchorFvtTestSupport.COPY_NAME_PLACEHOLDER, copyName),
                                                                              null);

        return AnchorFvtTestSupport.getCopiedGraph(openMetadataStore, copyGUID);
    }


    /**
     * Copy the template's table1 into another database schema: anchored to that database schema and attached
     * under its schema type.
     *
     * @param openMetadataStore store to create through
     * @param template template graph
     * @param target graph to add the table to
     * @param copyName value for the copy name placeholder
     * @return the new table
     * @throws Exception problem talking to the server
     */
    private String createTableCopy(OpenMetadataStore openMetadataStore,
                                   AnchoredGraph     template,
                                   AnchoredGraph     target,
                                   String            copyName) throws Exception
    {
        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setAnchorGUID(target.guid(AnchoredGraph.DATABASE_SCHEMA));
        templateOptions.setParentGUID(target.guid(AnchoredGraph.SCHEMA_TYPE));
        templateOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);
        templateOptions.setParentAtEnd1(true);

        return openMetadataStore.createMetadataElementFromTemplate(OpenMetadataType.RELATIONAL_TABLE.typeName,
                                                                   templateOptions,
                                                                   template.guid(AnchoredGraph.TABLE1),
                                                                   propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                    AnchorFvtTestSupport.getCopyMarker(template, copyName) + AnchoredGraph.TABLE1),
                                                                   null,
                                                                   Map.of(AnchorFvtTestSupport.COPY_NAME_PLACEHOLDER, copyName),
                                                                   null);
    }
}
