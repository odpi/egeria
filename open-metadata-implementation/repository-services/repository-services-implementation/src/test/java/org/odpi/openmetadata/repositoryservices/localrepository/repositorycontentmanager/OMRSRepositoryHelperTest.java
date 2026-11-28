/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

public class OMRSRepositoryHelperTest
{
    @Test
    void testformatRelationshipResults() throws PropertyErrorException, PagingErrorException {
        List<Relationship>   fullResults =null;
        int fromElement =0;
        // test null
        List<Relationship>  relationships = createHelper().formatRelationshipResults(fullResults,
            fromElement,
            null,
            SequencingOrder.ANY,
            0);
        assertTrue(relationships ==null);


        // test 1 relationship no paging
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                0);
        assertTrue(relationships.size()==1);

        // test 1 relationship page size 1
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                1);
        assertTrue(relationships.size()==1);


        // test 1 relationship page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==1);

        // test 3 relationships page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==3);

        // test 4 relationships page size 3
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertEquals(relationships.size(),3);

        // test 4 relationships page size 3 offset 3
        fromElement =3;
        fullResults = new ArrayList<>();
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        fullResults.add(new Relationship());
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==1);

        // test 4 relationships page size 3 offset 2
        fromElement =2;
        relationships = createHelper().formatRelationshipResults(fullResults,
                fromElement,
                null,
                SequencingOrder.ANY,
                3);
        assertTrue(relationships.size()==2);



    }


    @Test
    void testFormatResultsWithLargePageSizeBeyondFirstPage() throws PropertyErrorException, PagingErrorException {
        List<Relationship> fullRelationships = new ArrayList<>();
        fullRelationships.add(new Relationship());
        fullRelationships.add(new Relationship());
        fullRelationships.add(new Relationship());

        List<Relationship> relationships = createHelper().formatRelationshipResults(fullRelationships,
                1,
                null,
                SequencingOrder.ANY,
                Integer.MAX_VALUE);
        assertEquals(relationships.size(), 2);

        List<EntityDetail> fullEntities = new ArrayList<>();
        fullEntities.add(new EntityDetail());
        fullEntities.add(new EntityDetail());
        fullEntities.add(new EntityDetail());

        List<EntityDetail> entities = createHelper().formatEntityResults(fullEntities,
                1,
                null,
                SequencingOrder.ANY,
                Integer.MAX_VALUE);
        assertEquals(entities.size(), 2);
    }


    /**
     * An unrestricted request (pageSize == 0) must still start at fromElement and be sequenced
     * deterministically, exactly like a paged request - and like formatRelationshipResults
     * already does.  Before the fix the entity version returned the whole unsequenced list
     * from element zero, so a caller paging through an unrestricted find received overlapping
     * results.
     */
    @Test
    void testFormatEntityResultsUnrestrictedRespectsFromElement() throws PropertyErrorException, PagingErrorException
    {
        List<EntityDetail> fullEntities = createEntitiesWithGUIDs(4);

        List<EntityDetail> page = createHelper().formatEntityResults(fullEntities, 2, null, SequencingOrder.ANY, 0);
        assertEquals(2, page.size());
        assertEquals("entity-2", page.get(0).getGUID());
        assertEquals("entity-3", page.get(1).getGUID());

        // a fromElement beyond the end of an unrestricted request has no results
        assertNull(createHelper().formatEntityResults(fullEntities, 9, null, SequencingOrder.ANY, 0));
    }

    /**
     * The relationship twin must keep agreeing with the entity version on unrestricted requests.
     */
    @Test
    void testFormatRelationshipResultsUnrestrictedRespectsFromElement() throws PropertyErrorException, PagingErrorException
    {
        List<Relationship> fullRelationships = createRelationshipsWithGUIDs(4);

        List<Relationship> page = createHelper().formatRelationshipResults(fullRelationships, 2, null, SequencingOrder.ANY, 0);
        assertEquals(2, page.size());
        assertEquals("entity-2", page.get(0).getGUID());
        assertEquals("entity-3", page.get(1).getGUID());

        assertNull(createHelper().formatRelationshipResults(fullRelationships, 9, null, SequencingOrder.ANY, 0));
    }

    /**
     * Values of different primitive types for the same property must not compare as equal.
     * Equality is reserved for values that the GUID tie-break can separate into a total order;
     * reporting mixed-type pairs as equal while ordering same-type pairs strictly is not
     * transitive and makes TimSort throw "Comparison method violates its general contract!".
     */
    @Test
    void testComparePropertiesIsTotalOrderAcrossPrimitiveTypes()
    {
        OMRSRepositoryHelper helper = createHelper();

        InstanceProperties stringB = singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING, "b");
        InstanceProperties stringA = singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING, "a");
        InstanceProperties intOne  = singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT, 1);

        // same-type values still order by value
        assertTrue(helper.compareProperties(stringB, stringA, "p", SequencingOrder.PROPERTY_ASCENDING) > 0);
        assertTrue(helper.compareProperties(stringA, stringB, "p", SequencingOrder.PROPERTY_ASCENDING) < 0);
        assertEquals(0, helper.compareProperties(stringB, stringB, "p", SequencingOrder.PROPERTY_ASCENDING));

        // mixed-type values order deterministically instead of comparing as equal
        assertNotEquals(0, helper.compareProperties(stringB, intOne, "p", SequencingOrder.PROPERTY_ASCENDING));
        assertNotEquals(0, helper.compareProperties(intOne, stringB, "p", SequencingOrder.PROPERTY_ASCENDING));

        // equivalence stays transitive: if a ~ b and b ~ c then a ~ c
        if (helper.compareProperties(stringB, intOne, "p", SequencingOrder.PROPERTY_ASCENDING) == 0 &&
            helper.compareProperties(intOne, stringA, "p", SequencingOrder.PROPERTY_ASCENDING) == 0)
        {
            assertEquals(0, helper.compareProperties(stringB, stringA, "p", SequencingOrder.PROPERTY_ASCENDING));
        }
    }

    /**
     * A primitive that carries a type but no value (a defined-but-unset property as serialised
     * by a remote repository) must be ordered, not crash the sort with a NullPointerException.
     */
    @Test
    void testComparePropertiesWithUnsetPrimitiveValueDoesNotThrow()
    {
        OMRSRepositoryHelper helper = createHelper();

        InstanceProperties unset = singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING, null);
        InstanceProperties set   = singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING, "abc");

        // an unset value sorts before a set one, matching the missing-property handling
        assertTrue(helper.compareProperties(unset, set, "p", SequencingOrder.PROPERTY_ASCENDING) < 0);
        assertTrue(helper.compareProperties(set, unset, "p", SequencingOrder.PROPERTY_ASCENDING) > 0);
        assertEquals(0, helper.compareProperties(unset, unset, "p", SequencingOrder.PROPERTY_ASCENDING));
    }

    /**
     * Sequencing a find over instances whose property has different primitive types must not
     * abort with IllegalArgumentException("Comparison method violates its general contract!").
     * The instances alternate between two primitive types in blocks, with values descending,
     * so same-type pairs order strictly while mixed-type pairs used to compare as equal.
     */
    @Test
    void testFormatEntityResultsMixedPrimitiveTypesSortWithoutContractFailure() throws PropertyErrorException, PagingErrorException
    {
        List<EntityDetail> fullEntities = new ArrayList<>();
        for (int i = 0; i < 64; i++)
        {
            EntityDetail entity = new EntityDetail();
            entity.setGUID("entity-" + i);

            if (i % 3 == 0)
            {
                entity.setProperties(singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING, String.format("%03d", 64 - i)));
            }
            else
            {
                entity.setProperties(singlePrimitiveProperty("p", PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT, 64 - i));
            }
            fullEntities.add(entity);
        }

        List<EntityDetail> sorted = createHelper().formatEntityResults(fullEntities, 0, "p", SequencingOrder.PROPERTY_ASCENDING, 64);
        assertEquals(64, sorted.size());

        // the result is a total order: each primitive type occupies one contiguous block and
        // values inside a block are ascending
        Map<String, Integer> blockStarts  = new HashMap<>();
        int                  lastIntValue = Integer.MIN_VALUE;
        String               lastString   = null;
        String               lastTypeName = null;

        for (EntityDetail entity : sorted)
        {
            PrimitivePropertyValue value    = (PrimitivePropertyValue) entity.getProperties().getPropertyValue("p");
            String                 typeName = value.getTypeName();

            if (!typeName.equals(lastTypeName))
            {
                assertNull(blockStarts.put(typeName, sorted.indexOf(entity)), "type " + typeName + " appears in more than one block");
                lastIntValue = Integer.MIN_VALUE;
                lastString   = null;
                lastTypeName = typeName;
            }

            if (value.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT)
            {
                int intValue = (Integer) value.getPrimitiveValue();
                assertTrue(intValue >= lastIntValue, "int block is not ascending");
                lastIntValue = intValue;
            }
            else
            {
                String stringValue = (String) value.getPrimitiveValue();
                if (lastString != null)
                {
                    assertTrue(stringValue.compareTo(lastString) >= 0, "string block is not ascending");
                }
                lastString = stringValue;
            }
        }
    }

    private InstanceProperties singlePrimitiveProperty(String                propertyName,
                                                       PrimitiveDefCategory  category,
                                                       Object                value)
    {
        PrimitivePropertyValue primitive = new PrimitivePropertyValue();
        primitive.setPrimitiveDefCategory(category);

        if (value != null)
        {
            primitive.setPrimitiveValue(value);
        }
        primitive.setTypeName(category.getName());

        InstanceProperties properties = new InstanceProperties();
        properties.setProperty(propertyName, primitive);
        return properties;
    }

    private List<EntityDetail> createEntitiesWithGUIDs(int count)
    {
        // inserted in a scrambled order so that the tests also exercise the sequencing
        String[] guids = new String[count];
        for (int i = 0; i < count; i++)
        {
            guids[i] = "entity-" + i;
        }
        List<EntityDetail> entities = new ArrayList<>();
        for (String guid : guids)
        {
            EntityDetail entity = new EntityDetail();
            entity.setGUID(guid);
            entities.add(entity);
        }
        Collections.reverse(entities);
        return entities;
    }

    private List<Relationship> createRelationshipsWithGUIDs(int count)
    {
        List<Relationship> relationships = new ArrayList<>();
        for (int i = count - 1; i >= 0; i--)
        {
            Relationship relationship = new Relationship();
            relationship.setGUID("entity-" + i);
            relationships.add(relationship);
        }
        return relationships;
    }

    @Test
    void testCommonDifferencesAcrossInstances() {

        OMRSRepositoryHelper helper = createHelper();

        EntitySummary left = getTestEntitySummary();
        EntitySummary right = getTestEntitySummary();

        // Test some of the base properties that exist on all instances
        String otherUpdater = "DifferentUpdater";
        right.setUpdatedBy(otherUpdater);
        EntitySummaryDifferences differences = helper.getEntitySummaryDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        Set<String> differentProperties = differences.getNames();
        assertNotNull(differentProperties);
        assertEquals(differentProperties.size(), 1);
        String differingPropertyName = "UpdatedBy";
        assertTrue(differentProperties.contains(differingPropertyName));
        assertEquals(differences.getLeftValue(differingPropertyName), left.getUpdatedBy());
        assertEquals(differences.getRightValue(differingPropertyName), otherUpdater);

        String otherAuthor = "DifferentAuthor";
        right.setCreatedBy(otherAuthor);
        differences = helper.getEntitySummaryDifferences(left, right, true);
        assertTrue(differences.hasDifferences());
        differentProperties = differences.getNames();
        assertNotNull(differentProperties);
        assertEquals(differentProperties.size(), 1);
        differingPropertyName = "CreatedBy";
        assertTrue(differentProperties.contains(differingPropertyName));
        assertEquals(differences.getLeftValue(differingPropertyName), left.getCreatedBy());
        assertEquals(differences.getRightValue(differingPropertyName), otherAuthor);

        String otherCollection = "DifferentCollection";
        left.setMetadataCollectionId(otherCollection);
        differences = helper.getEntitySummaryDifferences(left, right, true);
        assertTrue(differences.hasDifferences());
        differentProperties = differences.getNames();
        assertNotNull(differentProperties);
        assertEquals(differentProperties.size(), 2);
        assertTrue(differentProperties.contains(differingPropertyName));
        assertTrue(differentProperties.contains("MetadataCollectionId"));
        assertEquals(differences.getLeftValue(differingPropertyName), left.getCreatedBy());
        assertEquals(differences.getRightValue(differingPropertyName), otherAuthor);
        assertEquals(differences.getLeftValue("MetadataCollectionId"), otherCollection);
        assertEquals(differences.getRightValue("MetadataCollectionId"), right.getMetadataCollectionId());

    }

    @Test
    void testRelationshipDifferences() {

        final String methodName = "testRelationshipDifferences";
        OMRSRepositoryHelper helper = createHelper();

        Relationship left = getTestRelationship();
        Relationship right = getTestRelationship();

        RelationshipDifferences differences = helper.getRelationshipDifferences(left, right, false);
        assertNotNull(differences);
        assertFalse(differences.hasDifferences());

        // Change an instance property and ensure that shows as a difference
        InstanceProperties ip = new InstanceProperties();
        ip = helper.addStringPropertyToInstance("test", ip, "testPropertyName", "testValue", methodName);
        right.setProperties(ip);

        differences = helper.getRelationshipDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasInstancePropertiesDifferences());
        assertFalse(differences.hasEntityProxyDifferences());
        InstancePropertiesDifferences ipd = differences.getInstancePropertiesDifferences();
        assertNotNull(ipd);
        assertTrue(ipd.isDifferent("testPropertyName"));
        Set<String> differingInstanceProperties = ipd.getNames();
        assertNotNull(differingInstanceProperties);
        assertEquals(differingInstanceProperties.size(), 2);
        assertTrue(differingInstanceProperties.contains("testPropertyName"));
        assertNull(ipd.getLeftValue("testPropertyName"));
        assertEquals(ipd.getRightValue("testPropertyName"), ip.getPropertyValue("testPropertyName"));
        assertEquals(ipd.getLeftValue("propertyName"), left.getProperties().getPropertyValue("propertyName"));
        assertNull(ipd.getRightValue("propertyName"));

        // Change an entity proxy and ensure that shows as a difference
        EntityProxy other = getTestEntityProxy();
        other.setUniqueProperties(ip);
        right.setEntityTwoProxy(other);
        differences = helper.getRelationshipDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasInstancePropertiesDifferences());
        assertTrue(differences.hasEntityProxyDifferences());
        assertFalse(differences.hasEntityProxyOneDifferences());
        assertTrue(differences.hasEntityProxyTwoDifferences());
        EntityProxyDifferences two = differences.getEntityProxyTwoDifferences();
        assertTrue(two.hasDifferences());
        assertTrue(two.hasUniquePropertiesDifferences());
        assertFalse(two.hasClassificationDifferences());
        ipd = two.getUniquePropertiesDifferences();
        Set<String> differingUniqueProperties = ipd.getNames();
        assertNotNull(differingUniqueProperties);
        assertEquals(differingUniqueProperties.size(), 2);
        assertTrue(differingUniqueProperties.contains("testPropertyName"));
        assertNull(ipd.getLeftValue("testPropertyName"));
        assertEquals(ipd.getRightValue("testPropertyName"), ip.getPropertyValue("testPropertyName"));
        assertEquals(ipd.getLeftValue("propertyName"), left.getEntityTwoProxy().getUniqueProperties().getPropertyValue("propertyName"));
        assertNull(ipd.getRightValue("propertyName"));

    }

    @Test
    void testEntityDetailDifferences() {

        final String methodName = "testEntityDetailDifferences";
        OMRSRepositoryHelper helper = createHelper();

        EntityDetail left = getTestEntityDetail();
        EntityDetail right = getTestEntityDetail();

        EntityDetailDifferences differences = helper.getEntityDetailDifferences(left, right, false);
        assertNotNull(differences);
        assertFalse(differences.hasDifferences());

        // Change an instance property and ensure that shows as a difference
        InstanceProperties ip = new InstanceProperties();
        ip = helper.addStringPropertyToInstance("test", ip, "testPropertyName", "testValue", methodName);
        right.setProperties(ip);

        differences = helper.getEntityDetailDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasInstancePropertiesDifferences());
        assertFalse(differences.hasClassificationDifferences());
        InstancePropertiesDifferences ipd = differences.getInstancePropertiesDifferences();
        assertNotNull(ipd);
        assertTrue(ipd.isDifferent("testPropertyName"));
        Set<String> differingInstanceProperties = ipd.getNames();
        assertNotNull(differingInstanceProperties);
        assertEquals(differingInstanceProperties.size(), 2);
        assertTrue(differingInstanceProperties.contains("testPropertyName"));
        assertNull(ipd.getLeftValue("testPropertyName"));
        assertEquals(ipd.getRightValue("testPropertyName"), ip.getPropertyValue("testPropertyName"));
        assertEquals(ipd.getLeftValue("propertyName"), left.getProperties().getPropertyValue("propertyName"));
        assertNull(ipd.getRightValue("propertyName"));

        // (Classification changes are tested in testEntitySummaryDifferences further below)

    }

    @Test
    void testEntityProxyDifferences() {

        final String methodName = "testEntityProxyDifferences";
        OMRSRepositoryHelper helper = createHelper();

        EntityProxy left = getTestEntityProxy();
        EntityProxy right = getTestEntityProxy();

        EntityProxyDifferences differences = helper.getEntityProxyDifferences(left, right, false);
        assertNotNull(differences);
        assertFalse(differences.hasDifferences());

        // Change a unique property and ensure that shows as a difference
        InstanceProperties ip = new InstanceProperties();
        ip = helper.addStringPropertyToInstance("test", ip, "propertyName", "testValue", methodName);
        right.setUniqueProperties(ip);

        differences = helper.getEntityProxyDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasUniquePropertiesDifferences());
        InstancePropertiesDifferences ipd = differences.getUniquePropertiesDifferences();
        assertNotNull(ipd);
        assertTrue(ipd.isDifferent("propertyName"));
        Set<String> differingInstanceProperties = ipd.getNames();
        assertNotNull(differingInstanceProperties);
        assertEquals(differingInstanceProperties.size(), 1);
        assertTrue(differingInstanceProperties.contains("propertyName"));
        assertEquals(ipd.getLeftValue("propertyName"), left.getUniqueProperties().getPropertyValue("propertyName"));
        assertEquals(ipd.getRightValue("propertyName"), ip.getPropertyValue("propertyName"));
        assertNotEquals(left.getUniqueProperties().getPropertyValue("propertyName"), ip.getPropertyValue("propertyName"));

    }

    @Test
    void testEntitySummaryDifferences() {

        final String methodName = "testEntitySummaryDifferences";
        OMRSRepositoryHelper helper = createHelper();

        EntitySummary left = getTestEntitySummary();
        EntitySummary right = getTestEntitySummary();

        EntitySummaryDifferences differences = helper.getEntitySummaryDifferences(left, right, false);
        assertNotNull(differences);
        assertFalse(differences.hasDifferences());

        // Change just a property within the classification and ensure that flags differences
        InstanceProperties ip = new InstanceProperties();
        ip = helper.addStringPropertyToInstance("test", ip, "testPropertyName", "testValue", methodName);
        Classification update = new Classification();
        update.setName("TestClassification");
        update.setProperties(ip);
        List<Classification> list = new ArrayList<>();
        list.add(update);
        right.setClassifications(list);
        differences = helper.getEntitySummaryDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasClassificationDifferences());
        ClassificationDifferences cd = differences.getClassificationDifferences();
        assertNotNull(cd);
        assertTrue(cd.isDifferent("TestClassification"));
        Set<String> differingClassifications = cd.getNames();
        assertNotNull(differingClassifications);
        assertEquals(differingClassifications.size(), 1);
        assertTrue(differingClassifications.contains("TestClassification"));

        // Calculate the instance properties differences within the classification to show their difference
        InstancePropertiesDifferences ipd = new InstancePropertiesDifferences();
        ipd.check(
                ((Classification)cd.getLeftValue("TestClassification")).getProperties(),
                ((Classification)cd.getRightValue("TestClassification")).getProperties()
        );
        assertTrue(ipd.hasDifferences());
        assertTrue(ipd.isDifferent("testPropertyName"));
        assertTrue(ipd.getOnlyOnRight().containsKey("testPropertyName"));

        // Change the classifications list and ensure that shows as a difference
        List<Classification> classifications = new ArrayList<>();
        Classification confidentiality = new Classification();
        confidentiality.setName("Confidentiality");
        classifications.add(confidentiality);
        right.setClassifications(classifications);
        differences = helper.getEntitySummaryDifferences(left, right, false);
        assertTrue(differences.hasDifferences());
        assertTrue(differences.hasClassificationDifferences());
        cd = differences.getClassificationDifferences();
        assertNotNull(cd);
        assertTrue(cd.isDifferent("Confidentiality"));
        differingClassifications = cd.getNames();
        assertNotNull(differingClassifications);
        assertEquals(differingClassifications.size(), 2);
        assertTrue(differingClassifications.contains("Confidentiality"));
        assertNull(cd.getLeftValue("Confidentiality"));
        assertEquals(cd.getRightValue("Confidentiality"), confidentiality);
        Map<String, Object> onlyOnRight = cd.getOnlyOnRight();
        assertTrue(onlyOnRight.containsKey("Confidentiality"));
        assertEquals(onlyOnRight.get("Confidentiality"), confidentiality);

    }

    private Relationship getTestRelationship() {
        Relationship relationship = new Relationship();
        setupTestObject(relationship);
        relationship.setEntityOneProxy(getTestEntityProxy());
        relationship.setEntityTwoProxy(getTestEntityProxy());
        InstanceProperties ip = createHelper().addStringPropertyToInstance("test", null, "propertyName", "propertyValue", "getTestRelationship");
        relationship.setProperties(ip);
        return relationship;
    }

    private EntityDetail getTestEntityDetail() {
        EntityDetail entityDetail = new EntityDetail();
        setupEntitySummary(entityDetail);
        InstanceProperties ip = createHelper().addStringPropertyToInstance("test", null, "propertyName", "propertyValue", "getTestEntityDetail");
        entityDetail.setProperties(ip);
        return entityDetail;
    }

    private EntityProxy getTestEntityProxy() {
        EntityProxy entityProxy = new EntityProxy();
        setupEntitySummary(entityProxy);
        InstanceProperties ip = createHelper().addStringPropertyToInstance("test", null, "propertyName", "propertyValue", "getTestEntityDetail");
        entityProxy.setUniqueProperties(ip);
        return entityProxy;
    }

    private EntitySummary getTestEntitySummary() {
        EntitySummary entitySummary = new EntitySummary();
        setupEntitySummary(entitySummary);
        return entitySummary;
    }

    private <T extends EntitySummary> void setupEntitySummary(T object) {
        setupTestObject(object);
        Classification classification = new Classification();
        classification.setName("TestClassification");
        List<Classification> classifications = new ArrayList<>();
        classifications.add(classification);
        object.setClassifications(classifications);
    }

    private <T extends InstanceHeader> void setupTestObject(T object) {
        object.setType(new InstanceType());
        object.setCreatedBy("TestAuthor");
        object.setUpdatedBy("TestEditor");
        object.setCreateTime(new Date(23));
        object.setUpdateTime(new Date(45));
        object.setVersion(30L);
        object.setStatus(InstanceStatus.UNKNOWN);
        object.setStatusOnDelete(InstanceStatus.UNKNOWN);
        object.setInstanceProvenanceType(InstanceProvenanceType.CONTENT_PACK);
        object.setMetadataCollectionId("TestHomeId");
        object.setGUID("TestInstanceGUID");
    }

    private OMRSRepositoryHelper createHelper() {
        return new OMRSRepositoryContentHelper(null);
    }
}
