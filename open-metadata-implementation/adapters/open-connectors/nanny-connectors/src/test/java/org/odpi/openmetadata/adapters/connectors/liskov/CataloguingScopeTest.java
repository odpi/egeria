/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;


/**
 * Checks which elements are taken to catalog a schema, so that a schema whose database is already catalogued is
 * not made a catalog target of its own.
 */
public class CataloguingScopeTest
{
    private static ElementHeader header(String guid, String typeName, String... superTypeNames)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(Arrays.asList(superTypeNames));

        ElementHeader elementHeader = new ElementHeader();

        elementHeader.setGUID(guid);
        elementHeader.setType(elementType);

        return elementHeader;
    }


    private static RelatedMetadataElementSummary related(ElementHeader elementHeader)
    {
        MetadataElementSummary relatedElement = new MetadataElementSummary();

        relatedElement.setElementHeader(elementHeader);

        RelatedMetadataElementSummary related = new RelatedMetadataElementSummary();

        related.setRelatedElement(relatedElement);

        return related;
    }


    /**
     * A schema is catalogued by itself, or by the database that holds it; another kind of data store holding it
     * does not catalog it.
     */
    @Test
    public void testSchemaInDatabase()
    {
        OpenMetadataRootElement schema = new OpenMetadataRootElement();

        schema.setElementHeader(header("schema-guid", "DeployedDatabaseSchema", "DataSet", "Asset", "Referenceable", "OpenMetadataRoot"));
        schema.setDataSetContent(List.of(related(header("database-guid", "RelationalDatabase", "Database", "DataStore", "Asset")),
                                         related(header("folder-guid", "FileFolder", "DataStore", "Asset"))));

        assertEquals(DataSharingHubManagerTargetProcessor.getCataloguingScopeGUIDs(schema, new PropertyHelper()),
                     List.of("schema-guid", "database-guid"));
    }


    /**
     * An element held by nothing is catalogued only by itself.
     */
    @Test
    public void testStandaloneElement()
    {
        OpenMetadataRootElement database = new OpenMetadataRootElement();

        database.setElementHeader(header("database-guid", "RelationalDatabase", "Database", "DataStore", "Asset"));

        assertEquals(DataSharingHubManagerTargetProcessor.getCataloguingScopeGUIDs(database, new PropertyHelper()),
                     List.of("database-guid"));
    }
}
