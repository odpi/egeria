/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * The zoneMembership property of the Anchors classification holds the zones that govern each element: its own
 * zones if it has a ZoneMembership classification, otherwise its anchor's.  A query can then filter on the zones
 * without reaching across to the anchor.  It used to be set only when a member was created, so it went stale as
 * soon as the anchor's zones changed.
 */
public class AnchorsZoneMembershipTest
{
    private static final String ANCHOR = "anchor";
    private static final String PLAIN  = "member-without-zones";
    private static final String ZONED  = "member-with-own-zones";


    /**
     * Changing the anchor's zones updates its own Anchors classification and those of its members without zones
     * of their own.  A member with its own zones keeps them.
     */
    @Test
    public void testAnchorZoneChangeReachesMembers()
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity(ANCHOR, ANCHOR, List.of("old"), List.of("new"));
        repository.addEntity(PLAIN, ANCHOR, List.of("old"), null);
        repository.addEntity(ZONED, ANCHOR, List.of("own"), List.of("own"));

        OpenMetadataAPIAnchorHandler.AnchorsZoneCorrections corrections = repository.handler.synchronizeAnchorsZoneMembership(ANCHOR, "guid", "Referenceable", false, false, null, "test");

        assertEquals(corrections.getCorrected(), 2L);
        assertEquals(corrections.getFailed(), 0L);
        assertEquals(repository.anchorsZones.get(ANCHOR), List.of("new"));
        assertEquals(repository.anchorsZones.get(PLAIN), List.of("new"));
        assertFalse(repository.anchorsZones.containsKey(ZONED), "A member with its own zones was changed");
    }


    /**
     * A member that loses its own zones is governed by its anchor's zones again.
     */
    @Test
    public void testMemberLosingItsZonesTakesAnchorZones()
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity(ANCHOR, ANCHOR, List.of("anchor-zone"), List.of("anchor-zone"));
        repository.addEntity(PLAIN, ANCHOR, List.of("its-old-zone"), null);

        repository.handler.synchronizeAnchorsZoneMembership(PLAIN, "guid", "Referenceable", false, false, null, "test");

        assertEquals(repository.anchorsZones.get(PLAIN), List.of("anchor-zone"));
        assertFalse(repository.anchorsZones.containsKey(ANCHOR), "The anchor was changed");
    }


    /**
     * An anchor that loses its zones clears them from its members.
     */
    @Test
    public void testAnchorLosingItsZonesClearsMembers()
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity(ANCHOR, ANCHOR, List.of("old"), null);
        repository.addEntity(PLAIN, ANCHOR, List.of("old"), null);

        repository.handler.synchronizeAnchorsZoneMembership(ANCHOR, "guid", "Referenceable", false, false, null, "test");

        assertNull(repository.anchorsZones.get(ANCHOR));
        assertNull(repository.anchorsZones.get(PLAIN));
        assertEquals(repository.anchorsZones.size(), 2);
    }


    /**
     * Checking the whole repository corrects every stale Anchors classification - on anchors and on members, with
     * and without zones of their own - leaves the correct ones alone, and reads each anchor once however many
     * members it has.
     */
    @Test
    public void testRepositorySweepCorrectsStaleClassifications() throws Exception
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity("anchor-a", "anchor-a", List.of("stale"), List.of("a"));
        repository.addEntity("a-stale-member", "anchor-a", List.of("old"), null);
        repository.addEntity("a-correct-member", "anchor-a", List.of("a"), null);
        repository.addEntity("a-member-with-own-zones", "anchor-a", List.of("a"), List.of("m"));
        repository.addEntity("anchor-b", "anchor-b", List.of("x"), null);
        repository.addEntity("b-member", "anchor-b", null, null);

        OpenMetadataAPIAnchorHandler.AnchorsZoneCorrections corrections = repository.handler.synchronizeAllAnchorsZoneMembership(false, false, null, "test");

        assertEquals(corrections.getCorrected(), 4L);
        assertEquals(corrections.getFailed(), 0L);
        assertEquals(repository.anchorsZones.get("anchor-a"), List.of("a"));
        assertEquals(repository.anchorsZones.get("a-stale-member"), List.of("a"));
        assertEquals(repository.anchorsZones.get("a-member-with-own-zones"), List.of("m"));
        assertTrue(repository.anchorsZones.containsKey("anchor-b"));
        assertNull(repository.anchorsZones.get("anchor-b"));
        assertFalse(repository.anchorsZones.containsKey("a-correct-member"), "A correct classification was rewritten");
        assertFalse(repository.anchorsZones.containsKey("b-member"), "A correct classification was rewritten");
        assertEquals(repository.retrievals.getOrDefault("anchor-a", 0), Integer.valueOf(1), "The anchor's zones were not remembered");
    }


    /**
     * Checking a repository whose Anchors classifications are all correct changes nothing.
     */
    @Test
    public void testRepositorySweepOfCorrectRepository() throws Exception
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity(ANCHOR, ANCHOR, List.of("z"), List.of("z"));
        repository.addEntity(PLAIN, ANCHOR, List.of("z"), null);
        repository.addEntity(ZONED, ANCHOR, List.of("own"), List.of("own"));

        assertEquals(repository.handler.synchronizeAllAnchorsZoneMembership(false, false, null, "test").getCorrected(), 0L);
        assertEquals(repository.handler.resynchronizeAnchorsZoneMembership("user", null, "guid", false, false, null, "test"), 0L);
        assertTrue(repository.anchorsZones.isEmpty());
    }


    /**
     * An element with no zones has zoneMembership unset in its Anchors classification.  One that holds an empty
     * list instead - which an archive can supply - would be hidden from every search by the zone filter, so the
     * sweep rewrites it, while leaving an unset value alone.
     */
    @Test
    public void testEmptyZoneListIsRewritten() throws Exception
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity("anchor-with-empty-list", "anchor-with-empty-list", List.of(), null);
        repository.addEntity("anchor-with-no-zones", "anchor-with-no-zones", null, null);

        assertTrue(AnchorsZoneMembership.holdsEmptyZoneList(AnchorsZoneMembership.getClassification(repository.entities.get("anchor-with-empty-list"),
                                                                                                    OpenMetadataType.ANCHORS_CLASSIFICATION.typeName)));
        assertFalse(AnchorsZoneMembership.holdsEmptyZoneList(AnchorsZoneMembership.getClassification(repository.entities.get("anchor-with-no-zones"),
                                                                                                     OpenMetadataType.ANCHORS_CLASSIFICATION.typeName)));

        OpenMetadataAPIAnchorHandler.AnchorsZoneCorrections corrections = repository.handler.synchronizeAllAnchorsZoneMembership(false, false, null, "test");

        assertEquals(corrections.getCorrected(), 1L);
        assertEquals(corrections.getFailed(), 0L);
        assertTrue(repository.anchorsZones.containsKey("anchor-with-empty-list"));
        assertNull(repository.anchorsZones.get("anchor-with-empty-list"), "The empty list should be rewritten as no zones");
        assertFalse(repository.anchorsZones.containsKey("anchor-with-no-zones"), "An unset zone list was rewritten");
    }


    /**
     * An update the repository refuses - for example because the cohort member that owns the classification is
     * not connected - is counted, the sweep carries on with the other elements, and the request then fails rather
     * than reporting that the zones are up to date.
     */
    @Test
    public void testRefusedCorrectionFailsTheRequest()
    {
        FakeRepository repository = new FakeRepository();

        repository.addEntity("anchor-a", "anchor-a", List.of("stale"), List.of("a"));
        repository.addEntity("a-unreachable-member", "anchor-a", List.of("old"), null);
        repository.addEntity("a-stale-member", "anchor-a", List.of("old"), null);
        repository.refusedUpdates.add("a-unreachable-member");

        OpenMetadataAPIAnchorHandler.AnchorsZoneCorrections corrections;

        try
        {
            corrections = repository.handler.synchronizeAllAnchorsZoneMembership(false, false, null, "test");
        }
        catch (Exception error)
        {
            throw new AssertionError("The sweep stopped: " + error.getMessage(), error);
        }

        assertEquals(corrections.getCorrected(), 2L);
        assertEquals(corrections.getFailed(), 1L);
        assertEquals(repository.anchorsZones.get("a-stale-member"), List.of("a"), "The sweep stopped at the refused update");

        PropertyServerException error = expectThrows(PropertyServerException.class,
                                                     () -> repository.handler.resynchronizeAnchorsZoneMembership("user", null, "guid", false, false, null, "test"));

        assertTrue(error.getReportedErrorMessageId().equals("OMAG-GENERIC-HANDLERS-500-014"), error.getReportedErrorMessage());
    }


    /**
     * A new element given zones of its own carries them in its Anchors classification, rather than its anchor's.
     */
    @Test
    public void testNewElementWithOwnZones()
    {
        OMRSRepositoryHelper repositoryHelper = Mockito.mock(OMRSRepositoryHelper.class, FakeRepository::answerRepositoryHelper);

        Classification anchors        = FakeRepository.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName, ANCHOR, List.of("anchor-zone"));
        Classification zoneMembership = FakeRepository.getClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, null, List.of("own"));

        List<Classification> aligned = AnchorsZoneMembership.alignNewClassifications(List.of(anchors, zoneMembership), repositoryHelper, "test");

        assertEquals(AnchorsZoneMembership.getZones(aligned.get(0), repositoryHelper, "test"), List.of("own"));
        assertEquals(AnchorsZoneMembership.getZones(anchors, repositoryHelper, "test"), List.of("anchor-zone"), "The caller's classification was changed");
    }


    /**
     * The zone filter leaves out elements whose Anchors classification lists zones, none of them readable:
     * NONE of [Anchors where zoneMembership is set and NONE of (zoneMembership IN readable zones)].
     */
    @Test
    public void testZoneFilterShape()
    {
        SearchClassifications filter = AnchorsZoneMembership.addReadableZonesFilter(null, List.of("z1", "z2"));

        assertEquals(filter.getMatchCriteria(), MatchCriteria.NONE);
        assertEquals(filter.getConditions().size(), 1);

        ClassificationCondition condition = filter.getConditions().get(0);

        assertEquals(condition.getName(), OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        assertEquals(condition.getMatchProperties().getMatchCriteria(), MatchCriteria.ALL);

        PropertyCondition hasZones = condition.getMatchProperties().getConditions().get(0);
        SearchProperties  noneIn   = condition.getMatchProperties().getConditions().get(1).getNestedConditions();

        assertEquals(hasZones.getOperator(), PropertyComparisonOperator.NOT_NULL);
        assertEquals(noneIn.getMatchCriteria(), MatchCriteria.NONE);
        assertEquals(noneIn.getConditions().get(0).getOperator(), PropertyComparisonOperator.IN);
        assertEquals(((ArrayPropertyValue)noneIn.getConditions().get(0).getValue()).getArrayCount(), 2);
    }


    /**
     * The filter joins a caller's NONE conditions, is not added to ALL or ANY conditions (which cannot be
     * combined with it), and is not added at all when the security connector cannot list the zones.
     */
    @Test
    public void testZoneFilterCombination()
    {
        ClassificationCondition callerCondition = new ClassificationCondition();

        callerCondition.setName(OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);

        SearchClassifications callerNone = new SearchClassifications();

        callerNone.setConditions(List.of(callerCondition));
        callerNone.setMatchCriteria(MatchCriteria.NONE);

        SearchClassifications combined = AnchorsZoneMembership.addReadableZonesFilter(callerNone, List.of("z1"));

        assertEquals(combined.getConditions().size(), 2);
        assertSame(combined.getConditions().get(0), callerCondition);
        assertEquals(callerNone.getConditions().size(), 1, "The caller's conditions were changed");

        SearchClassifications callerAll = new SearchClassifications();

        callerAll.setConditions(List.of(callerCondition));
        callerAll.setMatchCriteria(MatchCriteria.ALL);

        assertSame(AnchorsZoneMembership.addReadableZonesFilter(callerAll, List.of("z1")), callerAll);
        assertSame(AnchorsZoneMembership.addReadableZonesFilter(callerAll, null), callerAll);
        assertNull(AnchorsZoneMembership.addReadableZonesFilter(null, null));
        assertTrue(AnchorsZoneMembership.addReadableZonesFilter(new SearchClassifications(), List.of("z1")).getConditions().size() == 1);
    }


    /**
     * Entities behind mocked repository handler and helper, answering by method name.
     */
    private static class FakeRepository
    {
        final Map<String, EntityDetail>  entities       = new LinkedHashMap<>();
        final Map<String, List<String>>  anchorsZones   = new HashMap<>();
        final Map<String, Integer>       retrievals     = new HashMap<>();
        final Set<String>                refusedUpdates = new HashSet<>();

        final OpenMetadataAPIGenericHandler<Object> handler;

        FakeRepository()
        {
            RepositoryHandler       repositoryHandler       = Mockito.mock(RepositoryHandler.class, this::answerRepositoryHandler);
            OMRSRepositoryHelper    repositoryHelper        = Mockito.mock(OMRSRepositoryHelper.class, FakeRepository::answerRepositoryHelper);
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


        /**
         * Add an entity with an Anchors classification carrying the supplied zones, and, if ownZones is not
         * null, a ZoneMembership classification.
         */
        void addEntity(String guid, String anchorGUID, List<String> anchorsZones, List<String> ownZones)
        {
            EntityDetail entity = new EntityDetail();
            InstanceType type   = new InstanceType();

            type.setTypeDefName("Referenceable");
            entity.setGUID(guid);
            entity.setType(type);

            List<Classification> classifications = new ArrayList<>();

            classifications.add(getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName, anchorGUID, anchorsZones));

            if (ownZones != null)
            {
                classifications.add(getClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, null, ownZones));
            }

            entity.setClassifications(classifications);
            entities.put(guid, entity);
        }


        static Classification getClassification(String name, String anchorGUID, List<String> zones)
        {
            Classification     classification = new Classification();
            InstanceProperties properties     = new InstanceProperties();

            if (anchorGUID != null)
            {
                PrimitivePropertyValue anchorGUIDValue = new PrimitivePropertyValue();

                anchorGUIDValue.setPrimitiveValue(anchorGUID);
                properties.setProperty(OpenMetadataProperty.ANCHOR_GUID.name, anchorGUIDValue);
            }

            if (zones != null)
            {
                properties.setProperty(OpenMetadataProperty.ZONE_MEMBERSHIP.name, getArray(zones));
            }

            classification.setName(name);
            classification.setProperties(properties);

            return classification;
        }


        static ArrayPropertyValue getArray(List<String> values)
        {
            ArrayPropertyValue arrayValue = new ArrayPropertyValue();
            InstanceProperties elements   = new InstanceProperties();

            for (int i = 0; i < values.size(); i++)
            {
                PrimitivePropertyValue element = new PrimitivePropertyValue();

                element.setPrimitiveValue(values.get(i));
                elements.setProperty(Integer.toString(i), element);
            }

            arrayValue.setArrayCount(values.size());
            arrayValue.setArrayValues(elements);

            return arrayValue;
        }


        private Object answerRepositoryHandler(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();

            switch (invocation.getMethod().getName())
            {
                case "getEntityByGUID":
                    retrievals.merge((String)args[1], 1, Integer::sum);
                    return entities.get((String)args[1]);

                case "findEntities":
                    /*
                     * getAnchoredEntities: every entity is anchored to the one anchor, and all fit on the first page.
                     */
                    return new ArrayList<>(entities.values());

                case "reclassifyEntity":
                    String             guid       = (String)args[3];
                    InstanceProperties properties = (InstanceProperties)args[9];

                    if (refusedUpdates.contains(guid))
                    {
                        throw new IllegalStateException("No home repository is connected for " + guid);
                    }

                    anchorsZones.put(guid, answerGetStringArray(properties));
                    return null;
            }

            if (List.class.isAssignableFrom(invocation.getMethod().getReturnType()))
            {
                return null;
            }

            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        }


        static List<String> answerGetStringArray(InstanceProperties properties)
        {
            if (properties != null)
            {
                InstancePropertyValue value = properties.getPropertyValue(OpenMetadataProperty.ZONE_MEMBERSHIP.name);

                if (value instanceof ArrayPropertyValue arrayValue)
                {
                    List<String> results = new ArrayList<>();

                    for (int i = 0; i < arrayValue.getArrayCount(); i++)
                    {
                        results.add(((PrimitivePropertyValue)arrayValue.getArrayValues().getPropertyValue(Integer.toString(i))).getPrimitiveValue().toString());
                    }

                    return results;
                }
            }

            return null;
        }


        static Object answerRepositoryHelper(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();

            switch (invocation.getMethod().getName())
            {
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

                case "getStringArrayProperty":
                    return answerGetStringArray((InstanceProperties)args[2]);

                case "addStringArrayPropertyToInstance":
                    InstanceProperties properties = (args[1] == null) ? new InstanceProperties() : (InstanceProperties)args[1];

                    @SuppressWarnings("unchecked")
                    List<String> values = (List<String>)args[3];

                    if (values != null)
                    {
                        properties.setProperty((String)args[2], getArray(values));
                    }
                    return properties;
            }

            if (List.class.isAssignableFrom(invocation.getMethod().getReturnType()))
            {
                return null;
            }

            return Mockito.RETURNS_DEFAULTS.answer(invocation);
        }
    }
}
