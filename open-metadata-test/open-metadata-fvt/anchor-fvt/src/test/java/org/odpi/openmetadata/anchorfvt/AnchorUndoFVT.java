/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.anchorfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.anchorfvt.AnchorFvtTestSupport.AnchoredGraph;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Undo reverses the last update to one element or one relationship.  Unlike restore it never spreads: an update is
 * made to a single element, so its undo is too, and the elements anchored with it are untouched.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AnchorUndoFVT
{
    private static final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Undoing an update to a table puts its description back, and leaves its columns' versions as they were.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void undoOfElementUpdateRestoresPreviousVersion() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "UndoElement");

            long columnVersion = openMetadataStore.getMetadataElementByGUID(graph.guid(AnchoredGraph.COLUMN1A)).getVersions().getVersion();

            openMetadataStore.updateMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1),
                                                           new UpdateOptions(),
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "first"));
            openMetadataStore.updateMetadataElementInStore(graph.guid(AnchoredGraph.TABLE1),
                                                           new UpdateOptions(),
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "second"));

            openMetadataStore.undoMetadataElementUpdate(graph.guid(AnchoredGraph.TABLE1), new MetadataSourceOptions());

            OpenMetadataElement table = openMetadataStore.getMetadataElementByGUID(graph.guid(AnchoredGraph.TABLE1));

            assertEquals("first",
                         propertyHelper.getStringProperty("anchor-fvt", OpenMetadataProperty.DESCRIPTION.name, table.getElementProperties(), "test"),
                         "Undo should restore the description from before the last update");
            assertEquals(graph.qualifiedNames().get(AnchoredGraph.TABLE1),
                         AnchorFvtTestSupport.getQualifiedName(table),
                         "Undo should keep the properties the last update did not change");
            assertEquals(columnVersion,
                         openMetadataStore.getMetadataElementByGUID(graph.guid(AnchoredGraph.COLUMN1A)).getVersions().getVersion(),
                         "Undo of the table should not touch its columns");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * Undoing an update to a foreign key puts its properties back as they were before the update.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void undoOfRelationshipUpdateRestoresPreviousProperties() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            graph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "UndoRelationship");

            String foreignKeyGUID = AnchorFvtTestSupport.getRelationshipGUIDs(openMetadataStore,
                                                                              graph.guid(AnchoredGraph.COLUMN1A),
                                                                              OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                                              true).iterator().next();

            openMetadataStore.updateRelatedElementsInStore(foreignKeyGUID,
                                                           new UpdateOptions(),
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, "changed"));

            openMetadataStore.undoRelationshipUpdate(foreignKeyGUID, new MetadataSourceOptions());

            OpenMetadataRelationship foreignKey = openMetadataStore.getRelationshipByGUID(foreignKeyGUID);

            assertNull(propertyHelper.getStringProperty("anchor-fvt", OpenMetadataProperty.DESCRIPTION.name, foreignKey.getRelationshipProperties(), "test"),
                       "Undo should remove the description the update added");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }


    /**
     * An element that has never been updated has no previous version to go back to.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void undoWithNoPreviousVersionIsRefused() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        AnchoredGraph     graph             = null;

        try
        {
            final AnchoredGraph builtGraph = AnchorFvtTestSupport.buildAnchoredGraph(openMetadataStore, "UndoNothing");

            graph = builtGraph;

            assertThrows(Exception.class,
                         () -> openMetadataStore.undoMetadataElementUpdate(builtGraph.guid(AnchoredGraph.COLUMN1B), new MetadataSourceOptions()),
                         "Undo of an element that has never been updated should be refused");
        }
        finally
        {
            AnchorFvtTestSupport.purgeGraph(openMetadataStore, graph);
        }
    }
}
