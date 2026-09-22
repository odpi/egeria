/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

/**
 * SchemaCatalogTargetTest covers the two things that have to hold for a single database schema to work as a
 * catalog target of the JDBC cataloguer.
 * <br><br>
 * The catalog-&lt;vendor&gt;-schema governance action processes in the database vendors' content packs link a
 * DeployedDatabaseSchema asset to this connector.  Two details make that harder than it looks, and both fail
 * quietly rather than loudly, which is why they are pinned here rather than left to an FVT:
 * <ol>
 *     <li>DeployedDatabaseSchema is <b>not</b> a subtype of RelationalDatabase, so a connector that checks only
 *     for RelationalDatabase rejects it.</li>
 *     <li>A schema asset's qualified name already contains the schema, so building a column's qualified name
 *     the database-level way inserts the schema twice and the column is never found.</li>
 * </ol>
 */
public class SchemaCatalogTargetTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * A schema asset is not a relational database, so checking only for RelationalDatabase turns every
     * catalog-&lt;vendor&gt;-schema process into a "wrong type of catalog target" failure.
     * <br><br>
     * DeployedDatabaseSchema descends from DataSet; RelationalDatabase descends from DataStore.  They share no
     * branch, so no amount of supertype walking connects them.
     */
    @Test
    public void aSchemaAssetIsAcceptedAsASchema()
    {
        ElementControlHeader schemaHeader = headerFor(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                      List.of(OpenMetadataType.DATA_SET.typeName,
                                                              OpenMetadataType.DATA_ASSET.typeName,
                                                              OpenMetadataType.ASSET.typeName,
                                                              OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * First, the fact that makes the second check necessary at all.  If this ever became true the connector
         * would not need to recognise schemas separately - and this test would be asserting nothing.
         */
        assertFalse(propertyHelper.isTypeOf(schemaHeader, OpenMetadataType.RELATIONAL_DATABASE.typeName),
                    "DeployedDatabaseSchema descends from DataSet and RelationalDatabase from DataStore, so no" +
                            " amount of supertype walking should connect them");

        assertEquals(JDBCIntegrationCatalogTargetProcessor.catalogTargetKind(propertyHelper, schemaHeader),
                     JDBCIntegrationCatalogTargetProcessor.CatalogTargetKind.SCHEMA,
                     "a schema target has to be accepted, or every catalog-<vendor>-schema process fails with" +
                             " \"wrong type of catalog target\"");
    }


    /**
     * A database asset is still treated as a whole database.
     */
    @Test
    public void aDatabaseAssetIsStillTreatedAsADatabase()
    {
        ElementControlHeader databaseHeader = headerFor(OpenMetadataType.RELATIONAL_DATABASE.typeName,
                                                        List.of(OpenMetadataType.DATABASE.typeName,
                                                                OpenMetadataType.DATA_STORE.typeName,
                                                                OpenMetadataType.DATA_ASSET.typeName,
                                                                OpenMetadataType.ASSET.typeName,
                                                                OpenMetadataType.REFERENCEABLE.typeName));

        assertEquals(JDBCIntegrationCatalogTargetProcessor.catalogTargetKind(propertyHelper, databaseHeader),
                     JDBCIntegrationCatalogTargetProcessor.CatalogTargetKind.DATABASE,
                     "accepting schemas must not change how a database target is handled");
    }


    /**
     * Anything else is still refused, rather than being catalogued as though it were a database.
     */
    @Test
    public void anUnrelatedAssetIsStillRefused()
    {
        ElementControlHeader topicHeader = headerFor(OpenMetadataType.TOPIC.typeName,
                                                     List.of(OpenMetadataType.DATA_FEED.typeName,
                                                             OpenMetadataType.DATA_ASSET.typeName,
                                                             OpenMetadataType.ASSET.typeName,
                                                             OpenMetadataType.REFERENCEABLE.typeName));

        assertNull(JDBCIntegrationCatalogTargetProcessor.catalogTargetKind(propertyHelper, topicHeader),
                   "widening the check to accept schemas must not widen it to accept every asset that shares" +
                           " a supertype with them");
    }


    /**
     * Cataloguing a whole database names a column under the database, with the schema in the middle.
     */
    @Test
    public void aDatabaseScopedRefreshPutsTheSchemaInTheColumnName()
    {
        assertEquals(RelationalDatabaseCataloguer.columnQualifiedName("RelationalDatabase::localhost:5442/warehouse",
                                                                      null,
                                                                      "sales",
                                                                      "customers",
                                                                      "id"),
                     "RelationalDatabase::localhost:5442/warehouse::sales::customers::id");
    }


    /**
     * A table sitting directly under the database, with no schema, has no schema segment.
     */
    @Test
    public void aTableWithNoSchemaHasNoSchemaSegment()
    {
        assertEquals(RelationalDatabaseCataloguer.columnQualifiedName("RelationalDatabase::localhost:5442/warehouse",
                                                                      null,
                                                                      null,
                                                                      "customers",
                                                                      "id"),
                     "RelationalDatabase::localhost:5442/warehouse::customers::id");
    }


    /**
     * Cataloguing one schema names a column under the schema asset, <b>without</b> repeating the schema.
     * <br><br>
     * This is the assertion that matters.  A schema asset built from a vendor content pack's schema template is
     * named "&lt;type&gt;::&lt;server&gt;::&lt;database&gt;.&lt;schema&gt;" - the schema is already in there -
     * and its tables are catalogued as "&lt;that&gt;::&lt;table&gt;".  Naming a column the database-level way
     * would look for a "::sales::" segment that was never created, find nothing, and skip every foreign key
     * while still reporting a successful refresh.
     */
    @Test
    public void aSchemaScopedRefreshDoesNotRepeatTheSchema()
    {
        String schemaAssetQualifiedName = "PostgreSQL Relational Database Schema::localhost:5442::warehouse.sales";

        assertEquals(RelationalDatabaseCataloguer.columnQualifiedName(schemaAssetQualifiedName,
                                                                      "sales",
                                                                      "sales",
                                                                      "customers",
                                                                      "id"),
                     schemaAssetQualifiedName + "::customers::id",
                     "the schema appears twice, so this name matches nothing the refresh created");
    }


    /**
     * A foreign key reaching out of the schema being catalogued is skipped rather than misnamed.
     * <br><br>
     * Foreign keys are collected from both ends, so a key can point at a table in another schema.  That table
     * was not catalogued by a schema scoped refresh and cannot be named from this schema asset, so returning
     * null is how the caller knows to leave the key alone.
     */
    @Test
    public void aForeignKeyLeavingTheScopedSchemaIsSkipped()
    {
        assertNull(RelationalDatabaseCataloguer.columnQualifiedName("PostgreSQL Relational Database Schema::localhost:5442::warehouse.sales",
                                                                    "sales",
                                                                    "finance",
                                                                    "ledger",
                                                                    "id"),
                   "naming a column in another schema from this schema's asset would invent a qualified name" +
                           " for an element this refresh never created");
    }


    /**
     * Build an element header the way the repository would return one.
     *
     * @param typeName the element's own type
     * @param superTypeNames its supertypes, nearest first
     * @return header
     */
    private ElementControlHeader headerFor(String typeName, List<String> superTypeNames)
    {
        ElementType elementType = new ElementType();

        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(superTypeNames);

        ElementControlHeader header = new ElementControlHeader();

        header.setType(elementType);

        return header;
    }
}
