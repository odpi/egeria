/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;

import org.odpi.openmetadata.accessservices.informationview.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseSource;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DatabaseContextHandlerTest extends InMemoryRepositoryTest{

    private DatabaseContextHandler databaseContextHandler;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
        databaseContextHandler = new DatabaseContextHandler(omEntityDao, enterpriseConnector, auditLog);
    }


    @Test
    public void testDatabaseContextServices() throws Exception{
        List<DatabaseSource> databases = databaseContextHandler.getDataStores(0, 10);
        assertNotNull(databases);
        assertTrue( databases.size() == 1, "Database was not retrieved");

        List<TableSource> tables =
                databaseContextHandler.getTables(databases.get(0).getGuid(), 0, 10);

        assertNotNull(tables);
        assertEquals("EMPLOYEE", tables.get(0).getName());
        assertEquals("HR", tables.get(0).getSchemaName());

        List<TableContextEvent> contexts = databaseContextHandler.getTableContext(tables.get(0).getGuid());
        assertNotNull(contexts);
        assertEquals("EMPLOYEE", contexts.get(0).getTableSource().getName());
        assertEquals("HR", contexts.get(0).getTableSource().getSchemaName());
        assertEquals("XE", contexts.get(0).getTableSource().getDatabaseSource().getName());
        assertEquals("host", contexts.get(0).getTableSource().getDatabaseSource().getEndpointSource().getNetworkAddress());
        assertNotNull(contexts.get(0).getTableColumns());
        assertEquals(3, contexts.get(0).getTableColumns().size());
        List<String> columnNames = contexts.get(0).getTableColumns().stream().map(c -> c.getName()).collect(Collectors.toList());
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("ROLE");
        expectedNames.add("FNAME");
        expectedNames.add("LNAME");
        Collections.sort(columnNames);
        Collections.sort(expectedNames);
        assertEquals(expectedNames, columnNames);
    }

}
