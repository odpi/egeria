/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Deleting or archiving an element that is anchored to another element takes that element and what lies downstream
 * of it (at end 2 of its relationships) - never its siblings, and never its anchor.  The Anchors classification names
 * the root of the graph, so siblings, descendants and the anchor itself all carry the same anchorGUID and only the
 * direction of the relationships tells them apart.  Archiving used to walk both directions and so took the whole
 * graph, anchor included; deleting stopped at the element itself and left its descendants behind.  Only
 * relationships whose purpose is STRUCTURAL or ATTACHMENT are followed: the element at end 2 of any other
 * relationship is independent of the element being deleted, even when it shares the anchor.
 * <br><br>
 * Unlabelled links below are AttributeForSchema (STRUCTURAL).
 * <br><br>
 * The graph used throughout:
 * <pre>
 *   A (anchor) --&gt; B --&gt; C --&gt; D        B, C, D, S anchored to A
 *   A --&gt; S --&gt; B                       S links to B: S is upstream of B
 *   B --&gt; A                             B links back to the anchor: A is at end 2 but is still the anchor
 *   B --&gt; X                             X is its own anchor
 *   A --&gt; F, B --ForeignKey--&gt; F         F is at end 2 of B, but ForeignKey is an ASSOCIATION
 *   A --&gt; G, B --NotificationSubscriber--&gt; G   G is at end 2 of B, but NotificationSubscriber is an ACTION
 *   E --&gt; B                             E is anchored to B itself, so belongs to B wherever it is linked
 * </pre>
 */
public class AnchoredElementCascadeTest
{
    private static final String ELEMENT_TYPE = "TestElement";
    private static final String LINK_TYPE    = OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName;
    private static final String USER         = "testUser";


    @Test
    public void testDeletingAnchoredElementTakesOnlyDownstreamElements() throws Exception
    {
        FakeRepository repository = this.getAnchoredGraph();

        repository.handler.deleteBeanInRepository(USER, null, null, "B", "guid", null, ELEMENT_TYPE, false, null, null, false, false, null, "test");

        assertEquals(repository.deletedEntities, Set.of("B", "C", "D", "E"));
    }


    @Test
    public void testArchivingAnchoredElementTakesOnlyDownstreamElements() throws Exception
    {
        FakeRepository repository = this.getAnchoredGraph();

        repository.handler.archiveBeanInRepository(USER, null, null, "B", "guid", ELEMENT_TYPE, null, false, false, null, "test");

        assertEquals(repository.archivedEntities, Set.of("B", "C", "D", "E"));
    }


    @Test
    public void testDeletingAnchorTakesWholeAnchoredGraph() throws Exception
    {
        FakeRepository repository = this.getAnchoredGraph();

        repository.handler.deleteBeanInRepository(USER, null, null, "A", "guid", null, ELEMENT_TYPE, false, null, null, false, false, null, "test");

        assertEquals(repository.deletedEntities, Set.of("A", "B", "C", "D", "E", "F", "G", "S"));
    }


    @Test
    public void testArchivingAnchorTakesWholeAnchoredGraph() throws Exception
    {
        FakeRepository repository = this.getAnchoredGraph();

        repository.handler.archiveBeanInRepository(USER, null, null, "A", "guid", ELEMENT_TYPE, null, false, false, null, "test");

        assertEquals(repository.archivedEntities, Set.of("A", "B", "C", "D", "E", "F", "G", "S"));
    }


    /**
     * The deployed element is at end 1 of DeployedOn.  The cascade used to delete end 2 - the infrastructure that
     * was itself being deleted - which recursed until the stack ran out, and the deployed element was never reached.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testCascadedDeleteOfInfrastructureDeletesDeployedElements() throws Exception
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity("H", OpenMetadataType.IT_INFRASTRUCTURE.typeName, "H");
        repository.addEntity("E", ELEMENT_TYPE, "E");
        repository.addRelationship("E", "H", OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName);

        repository.handler.deleteBeanInRepository(USER, null, null, "H", "guid", null, OpenMetadataType.IT_INFRASTRUCTURE.typeName, true, null, null, false, false, null, "test");

        assertEquals(repository.deletedEntities, Set.of("E", "H"));
    }


    /**
     * Build the graph described in the class comment.
     *
     * @return populated repository
     */
    private FakeRepository getAnchoredGraph()
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity("A", ELEMENT_TYPE, "A");
        repository.addEntity("B", ELEMENT_TYPE, "A");
        repository.addEntity("C", ELEMENT_TYPE, "A");
        repository.addEntity("D", ELEMENT_TYPE, "A");
        repository.addEntity("S", ELEMENT_TYPE, "A");
        repository.addEntity("X", ELEMENT_TYPE, "X");
        repository.addEntity("F", ELEMENT_TYPE, "A");
        repository.addEntity("G", ELEMENT_TYPE, "A");
        repository.addEntity("E", ELEMENT_TYPE, "B");

        repository.addRelationship("A", "B", LINK_TYPE);
        repository.addRelationship("B", "C", LINK_TYPE);
        repository.addRelationship("C", "D", LINK_TYPE);
        repository.addRelationship("A", "S", LINK_TYPE);
        repository.addRelationship("S", "B", LINK_TYPE);
        repository.addRelationship("B", "A", LINK_TYPE);
        repository.addRelationship("B", "X", LINK_TYPE);
        repository.addRelationship("A", "F", LINK_TYPE);
        repository.addRelationship("B", "F", OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName);
        repository.addRelationship("A", "G", LINK_TYPE);
        repository.addRelationship("B", "G", OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName);
        repository.addRelationship("E", "B", LINK_TYPE);

        return repository;
    }


    /**
     * A handful of entities and relationships behind mocked repository handler and helper.  The mocks answer by
     * method name so that the many overloads of the repository handler need not be matched argument by argument.
     */
    private static class FakeRepository
    {
        final Map<String, EntityDetail> entities         = new LinkedHashMap<>();
        final List<Relationship>        relationships    = new ArrayList<>();
        final Set<String>               deletedEntities  = new HashSet<>();
        final Set<String>               archivedEntities = new HashSet<>();

        final OpenMetadataAPIGenericHandler<Object> handler;

        FakeRepository()
        {
            RepositoryHandler       repositoryHandler       = Mockito.mock(RepositoryHandler.class, this::answerRepositoryHandler);
            OMRSRepositoryHelper    repositoryHelper        = Mockito.mock(OMRSRepositoryHelper.class, this::answerRepositoryHelper);
            InvalidParameterHandler invalidParameterHandler = Mockito.mock(InvalidParameterHandler.class);

            Mockito.when(invalidParameterHandler.getMaxPagingSize()).thenReturn(100);

            handler = new OpenMetadataAPIGenericHandler<>(null,
                                                          Object.class,
                                                          "testService",
                                                          "testServer",
                                                          invalidParameterHandler,
                                                          repositoryHandler,
                                                          repositoryHelper,
                                                          "localServerUser",
                                                          Mockito.mock(OpenMetadataServerSecurityVerifier.class),
                                                          Mockito.mock(AuditLog.class));
        }


        void addEntity(String guid, String typeName, String anchorGUID)
        {
            EntityDetail       entity                = new EntityDetail();
            Classification     anchorsClassification = new Classification();
            InstanceProperties anchorsProperties     = new InstanceProperties();
            PrimitivePropertyValue anchorGUIDValue   = new PrimitivePropertyValue();

            anchorGUIDValue.setPrimitiveValue(anchorGUID);
            anchorsProperties.setProperty(OpenMetadataProperty.ANCHOR_GUID.name, anchorGUIDValue);
            anchorsClassification.setName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
            anchorsClassification.setProperties(anchorsProperties);

            entity.setGUID(guid);
            entity.setType(getType(typeName));
            entity.setClassifications(new ArrayList<>(List.of(anchorsClassification)));

            entities.put(guid, entity);
        }


        void addRelationship(String end1GUID, String end2GUID, String typeName)
        {
            Relationship relationship = new Relationship();

            relationship.setGUID(end1GUID + "->" + end2GUID);
            relationship.setType(getType(typeName));
            relationship.setEntityOneProxy(getProxy(entities.get(end1GUID)));
            relationship.setEntityTwoProxy(getProxy(entities.get(end2GUID)));

            relationships.add(relationship);
        }


        private static InstanceType getType(String typeName)
        {
            InstanceType type = new InstanceType();

            type.setTypeDefName(typeName);

            return type;
        }


        private static EntityProxy getProxy(EntityDetail entity)
        {
            EntityProxy proxy = new EntityProxy();

            proxy.setGUID(entity.getGUID());
            proxy.setType(entity.getType());

            return proxy;
        }


        private Object answerRepositoryHandler(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();

            switch (invocation.getMethod().getName())
            {
                case "getEntityByGUID":
                    String guid = (String)args[1];

                    return deletedEntities.contains(guid) ? null : entities.get(guid);

                case "getRelationshipsByType":
                    if ((args.length == 16) && (args[1] instanceof EntityDetail startingEntity))
                    {
                        return this.getRelationships(startingEntity.getGUID(), (String)args[4], (int)args[5], (int)args[12]);
                    }
                    break;

                case "getOtherEnd":
                    Relationship relationship = (Relationship)args[2];
                    int          end          = (int)args[3];

                    if (end == 1)
                    {
                        return relationship.getEntityOneProxy();
                    }
                    else if (end == 2)
                    {
                        return relationship.getEntityTwoProxy();
                    }

                    return args[0].equals(relationship.getEntityOneProxy().getGUID()) ? relationship.getEntityTwoProxy() : relationship.getEntityOneProxy();

                case "removeRelationship":
                    if (args[3] instanceof Relationship removedRelationship)
                    {
                        relationships.remove(removedRelationship);
                        return null;
                    }
                    break;

                case "removeEntity":
                    String removedGUID = (String)args[3];

                    relationships.removeIf(candidate -> candidate.getEntityOneProxy().getGUID().equals(removedGUID)
                                                     || candidate.getEntityTwoProxy().getGUID().equals(removedGUID));
                    deletedEntities.add(removedGUID);
                    return null;

                case "classifyEntity":
                    if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(args[8]))
                    {
                        archivedEntities.add((String)args[3]);
                    }
                    return null;
            }

            /*
             * Nothing else is backed by the graph.  A list answers null - the paging loops stop only on null.
             */
            if (List.class.isAssignableFrom(invocation.getMethod().getReturnType()))
            {
                return null;
            }

            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        }


        /**
         * One page of the relationships for an entity, with the same end selection as the real repository handler:
         * the end names where the other entity is.
         */
        private List<Relationship> getRelationships(String startingGUID, String relationshipTypeName, int otherEnd, int startingFrom)
        {
            if (startingFrom > 0)
            {
                return null;
            }

            List<Relationship> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                boolean atEnd1 = relationship.getEntityOneProxy().getGUID().equals(startingGUID);
                boolean atEnd2 = relationship.getEntityTwoProxy().getGUID().equals(startingGUID);

                if (((relationshipTypeName == null) || (relationshipTypeName.equals(relationship.getType().getTypeDefName())))
                        && (((otherEnd == 0) && (atEnd1 || atEnd2)) || ((otherEnd == 1) && atEnd2) || ((otherEnd == 2) && atEnd1)))
                {
                    results.add(relationship);
                }
            }

            return results.isEmpty() ? null : results;
        }


        private Object answerRepositoryHelper(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();

            switch (invocation.getMethod().getName())
            {
                case "getClassificationFromEntity":
                    if (args[1] instanceof EntitySummary entity)
                    {
                        for (Classification classification : entity.getClassifications())
                        {
                            if (classification.getName().equals(args[2]))
                            {
                                return classification;
                            }
                        }
                    }
                    return null;

                case "getStringProperty":
                    if (args[2] instanceof InstanceProperties properties)
                    {
                        InstancePropertyValue value = properties.getPropertyValue((String)args[1]);

                        if (value instanceof PrimitivePropertyValue primitiveValue)
                        {
                            return primitiveValue.getPrimitiveValue();
                        }
                    }
                    return null;

                case "isTypeOf":
                    return (args.length == 3) && (args[1] != null) && args[1].equals(args[2]);
            }

            if (List.class.isAssignableFrom(invocation.getMethod().getReturnType()))
            {
                return null;
            }

            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        }
    }
}
