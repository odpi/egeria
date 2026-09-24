/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Verify that the type_definition table is part of the schema that the connector creates at start-up.
 */
public class RepositoryTableTest
{
    /**
     * The table is in the list the connector passes to the DDL generator.
     */
    @Test
    public void testTypeDefinitionTableIsDefined()
    {
        assertTrue(RepositoryTable.getTables().contains(RepositoryTable.TYPE_DEFINITION));
    }


    /**
     * The generated DDL creates the table only if it does not exist - which is how it reaches a repository
     * created before the table was defined, without disturbing one that already has it - and keys it on the
     * type's unique identifier.
     *
     * @throws Exception test failure
     */
    @Test
    public void testTypeDefinitionTableDDL() throws Exception
    {
        List<String> ddlStatements = new PostgreSQLSchemaDDL("repository_test", "Test repository", RepositoryTable.getTables()).getDDLStatements();

        String createStatement = null;

        for (String ddlStatement : ddlStatements)
        {
            if (ddlStatement.startsWith("create table if not exists type_definition("))
            {
                createStatement = ddlStatement;
            }
        }

        assertNotNull(createStatement, "No create statement for type_definition in " + ddlStatements);
        assertTrue(createStatement.contains("type_guid text not null"), createStatement);
        assertTrue(createStatement.contains("type_version bigint not null"), createStatement);
        assertTrue(createStatement.contains("type_definition text not null"), createStatement);
        assertTrue(createStatement.contains("first_stored_time timestamp"), createStatement);
        assertTrue(createStatement.contains("constraint type_definition_pk primary key (type_guid)"), createStatement);
    }
}
