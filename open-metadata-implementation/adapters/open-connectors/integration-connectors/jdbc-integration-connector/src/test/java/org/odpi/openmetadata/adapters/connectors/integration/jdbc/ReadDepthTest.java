/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertSame;

/**
 * ReadDepthTest pins down how much the JDBC cataloguer asks the repository to return with each element it reads.
 * <br><br>
 * A default read returns an element with all of its relationships and expands its schema type into every table
 * and column beneath it.  Once a database has been catalogued, every refresh paid that cost for the database,
 * each schema and each lookup, so a refresh with nothing to change took far longer than the first.  These
 * options are what keep it down.
 */
public class ReadDepthTest
{
    /**
     * The default depth is what makes a read expensive.  If it ever became 0, the helpers would be doing nothing.
     */
    @Test
    public void theDefaultReadReturnsRelationships()
    {
        assertNotEquals(new GetOptions().getGraphQueryDepth(), 0,
                        "a default read returns relationships, which is why the cataloguer turns them off");
    }


    /**
     * A read that needs only the element's own properties returns no relationships, and keeps every other option.
     */
    @Test
    public void withoutRelationshipsReturnsNone()
    {
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(500);

        QueryOptions result = RelationalDatabaseCataloguer.withoutRelationships(queryOptions);

        assertSame(result, queryOptions);
        assertEquals(result.getGraphQueryDepth(), 0);
        assertEquals(result.getPageSize(), 500, "only the depth changes");
    }


    /**
     * A read that needs one relationship returns only that relationship type, and does not go beyond it.
     */
    @Test
    public void withOnlyRelationshipReturnsOneTypeOneLevel()
    {
        GetOptions result = RelationalDatabaseCataloguer.withOnlyRelationship(new GetOptions(),
                                                                             OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

        assertEquals(result.getGraphQueryDepth(), 1);
        assertEquals(result.getIncludeOnlyRelationships(), List.of(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName));
    }
}
