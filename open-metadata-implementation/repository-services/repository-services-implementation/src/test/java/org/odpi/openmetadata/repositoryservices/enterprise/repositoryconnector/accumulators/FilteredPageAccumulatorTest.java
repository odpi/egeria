/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

import org.mockito.Mockito;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * A repository returns null when it has no more matching instances, and an empty list when it retrieved instances
 * for the page but filtered them all out (the local repository's security verifier does this).  A caller paging
 * through the results keeps going on an empty list and stops on null, so the enterprise accumulators must not turn
 * a filtered page into null.
 */
public class FilteredPageAccumulatorTest
{
    private static final String LOCAL_COLLECTION  = "local-collection";
    private static final String REMOTE_COLLECTION = "remote-collection";


    /**
     * Default constructor
     */
    public FilteredPageAccumulatorTest()
    {
    }


    /**
     * Every repository returned null: there are no more entities.
     */
    @Test
    public void testNoEntitiesIsNull()
    {
        EntitiesAccumulator accumulator = getEntitiesAccumulator();

        accumulator.addEntities(null, LOCAL_COLLECTION);
        accumulator.addEntities(null, REMOTE_COLLECTION);

        assertNull(accumulator.getResults(null, Mockito.mock(OMRSMetadataCollection.class)));
    }


    /**
     * One repository returned an empty list: its page was filtered empty, so the result is an empty list.
     */
    @Test
    public void testFilteredEntityPageIsEmptyNotNull()
    {
        EntitiesAccumulator accumulator = getEntitiesAccumulator();

        accumulator.addEntities(new ArrayList<>(), LOCAL_COLLECTION);
        accumulator.addEntities(null, REMOTE_COLLECTION);

        List<EntityDetail> results = accumulator.getResults(null, Mockito.mock(OMRSMetadataCollection.class));

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }


    /**
     * Every repository returned null: there are no more relationships.
     */
    @Test
    public void testNoRelationshipsIsNull()
    {
        RelationshipsAccumulator accumulator = getRelationshipsAccumulator();

        accumulator.addRelationships(null, LOCAL_COLLECTION);
        accumulator.addRelationships(null, REMOTE_COLLECTION);

        assertNull(accumulator.getResults(null));
    }


    /**
     * One repository returned an empty list: its page was filtered empty, so the result is an empty list.
     */
    @Test
    public void testFilteredRelationshipPageIsEmptyNotNull()
    {
        RelationshipsAccumulator accumulator = getRelationshipsAccumulator();

        accumulator.addRelationships(null, LOCAL_COLLECTION);
        accumulator.addRelationships(new ArrayList<>(), REMOTE_COLLECTION);

        List<Relationship> results = accumulator.getResults(null);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }


    private static EntitiesAccumulator getEntitiesAccumulator()
    {
        return new EntitiesAccumulator(LOCAL_COLLECTION,
                                       Mockito.mock(AuditLog.class),
                                       Mockito.mock(OMRSRepositoryValidator.class));
    }


    private static RelationshipsAccumulator getRelationshipsAccumulator()
    {
        return new RelationshipsAccumulator(LOCAL_COLLECTION,
                                            Mockito.mock(AuditLog.class),
                                            Mockito.mock(OMRSRepositoryValidator.class));
    }
}
