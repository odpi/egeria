/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


public class TestInMemoryEntityNeighbourhood
{
    public static final String CLASSIFICATION_1 = "classification1";
    public static final String CLASSIFICATION_2 = "classification2";
    public static final String CLASSIFICATION_3 = "classification3";
    @Mock
    private OMRSRepositoryValidator repositoryValidator;
    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @BeforeMethod

    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetGraph() throws TypeErrorException {
        Map<String, EntityDetail> entityStore = new HashMap<>();
        Map<String, Relationship> relationshipStore = new HashMap<>();
        String rootEntityGUID = "1111";
        List<String> entityTypeGUIDs = null;
        List<String> relationshipTypeGUIDs = null;
        List<InstanceStatus> limitResultsByStatus = null;
        List<String> limitResultsByClassification = null;

        EntityDetail entity1 = new EntityDetail();
        entity1.setGUID("1111");
        entityStore.put(entity1.getGUID(), entity1);
        when(repositoryValidator.verifyInstanceHasRightStatus(any(), any())).thenReturn(true);
        when(repositoryValidator.verifyEntityIsClassified(any(), any())).thenReturn(true);
        when(repositoryHelper.isTypeOf(anyString(),anyString(),anyString())).thenReturn(true);
        // test with 1 entity
        InMemoryEntityNeighbourhood inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,"", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        InstanceGraph graph = inMemoryEntityNeighbourhood.createInstanceGraph();
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertNull(graph.getRelationships());

        // test with 2 entities 1 relationship
        EntityDetail entity2 = new EntityDetail();
        entity2.setGUID("2222");

        Relationship relationship1 = new Relationship();
        relationship1.setGUID("3333");

        InstanceType type = new InstanceType();
        type.setTypeDefGUID("4444");
        EntityProxy entityProxy1 = getEntityProxy(entity1.getGUID(), null, type);
        EntityProxy entityProxy2 = getEntityProxy(entity2.getGUID(), null, type);

        relationship1.setEntityOneProxy(entityProxy1);
        relationship1.setEntityTwoProxy(entityProxy2);

        entityStore.put(entity2.getGUID(), entity2);
        relationshipStore.put(relationship1.getGUID(), relationship1);
        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper, "", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();

        assertTrue(graph.getEntities().size() == 2);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));

        // test with 3 entities 2 relationship, level 1
        EntityDetail entity3 = new EntityDetail();
        entity3.setGUID("5555");
        EntityProxy entityProxy3 = getEntityProxy(entity3.getGUID(), null, type);
        entityStore.put(entity3.getGUID(), entity3);

        Relationship relationship2 = new Relationship();
        relationship2.setGUID("6666");

        relationship2.setEntityOneProxy(entityProxy2);
        relationship2.setEntityTwoProxy(entityProxy3);
        relationshipStore.put(relationship2.getGUID(), relationship2);

        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper, "",repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();

        assertTrue(graph.getEntities().size() == 2);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));

        // test with 3 entities 2 relationship, level 2
        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,"", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 3);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();

        assertTrue(graph.getEntities().size() == 3);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsEntityWithGuid(graph, "5555"));
        assertTrue(graph.getRelationships().size() == 2);
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "6666"));

        // test limit by classification
        List<Classification> classificationsForEntity2 = new ArrayList<>();
        Classification testClassification = new Classification();
        testClassification.setName(CLASSIFICATION_1);
        classificationsForEntity2.add(testClassification);
        testClassification = new Classification();
        testClassification.setName(CLASSIFICATION_2);
        classificationsForEntity2.add(testClassification);
        entity2.setClassifications(classificationsForEntity2);
        entityStore.put(entity2.getGUID(), entity2);
        // check the same results are returned when there is no limitation on classification but one of the entities is classified
        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,"", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();
        assertTrue(graph.getEntities().size() == 2);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));

        // check that specifying the classification in the limitResultsByClassification list gives the same result
        limitResultsByClassification = new ArrayList<>();
        limitResultsByClassification.add(CLASSIFICATION_1);
        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,"", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();
        assertTrue(graph.getEntities().size() == 2);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));

        // check that the same results are returned if there are other classifications in the limitResultsByClassification list
        limitResultsByClassification.add(CLASSIFICATION_2);
        limitResultsByClassification.add(CLASSIFICATION_3);
        limitResultsByClassification = new ArrayList<>();
        limitResultsByClassification.add(CLASSIFICATION_1);
        inMemoryEntityNeighbourhood = new InMemoryEntityNeighbourhood(repositoryHelper,"", repositoryValidator, entityStore, relationshipStore, rootEntityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, 1);
        graph = inMemoryEntityNeighbourhood.createInstanceGraph();
        assertTrue(graph.getEntities().size() == 2);
        assertTrue(graphContainsEntityWithGuid(graph, "1111"));
        assertTrue(graphContainsEntityWithGuid(graph, "2222"));
        assertTrue(graphContainsRelationshipWithGuid(graph, "3333"));

        // Unfortunately checking that limiting on a classification that the entity does not have, means it should not be returned is mocked out in the junit.


    }

    private boolean graphContainsEntityWithGuid(InstanceGraph graph, String guid)
    {
        boolean valid = false;
        if (graph.getEntities() != null)
        {
            for (EntityDetail entity : graph.getEntities())
            {
                if (entity.getGUID().equals(guid))
                {
                    if (valid)
                    {
                        // it should only appear once
                        valid = false;
                    } else
                    {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }

    private boolean graphContainsRelationshipWithGuid(InstanceGraph graph, String guid)
    {
        boolean valid = false;
        if (graph.getRelationships() != null)
        {
            for (Relationship relationship : graph.getRelationships())
            {
                if (relationship.getGUID().equals(guid))
                {
                    if (valid)
                    {
                        // it should only appear once
                        valid = false;
                    }
                    else
                    {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }

    private EntityProxy getEntityProxy(String guid, List<Classification> classifications, InstanceType type)
    {
        EntityProxy testObject = new EntityProxy();

        testObject.setType(type);
        testObject.setCreatedBy("createAuthor");
        testObject.setUpdatedBy("updateAuthor");
        testObject.setCreateTime(new Date(3));
        testObject.setUpdateTime(new Date(40));
        testObject.setVersion(30);
        testObject.setStatus(InstanceStatus.UNKNOWN);
        testObject.setStatusOnDelete(InstanceStatus.UNKNOWN);
        testObject.setGUID(guid);
        testObject.setClassifications(classifications);
        return testObject;
    }
}
