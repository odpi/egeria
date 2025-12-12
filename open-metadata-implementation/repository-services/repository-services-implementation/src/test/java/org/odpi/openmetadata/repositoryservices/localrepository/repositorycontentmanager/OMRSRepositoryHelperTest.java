/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.testng.annotations.Test;

import java.util.*;
import java.util.regex.Pattern;

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
    void testRegexHelpers() {

        // Start with a regex and a string that it will match
        String testRegex = ".*[A-Za-z].*";
        String testMatch = "somethingABCelse";

        // test 1 ensure that the regular expression is match-able
        assertTrue(Pattern.matches(testRegex, testMatch));

        // test 2 ensure that it is not recognized as an exact match (non-regex)
        assertFalse(createHelper().isExactMatchRegex(testRegex));
        assertFalse(createHelper().isExactMatchRegex(testRegex, true));

        // test 3 ensure that getting the exact match literal IS recognized as an exact match (not a regex)
        String testExactMatch = createHelper().getExactMatchRegex(testRegex);
        assertTrue(createHelper().isExactMatchRegex(testExactMatch));
        assertTrue(createHelper().isExactMatchRegex(testExactMatch, false));
        assertFalse(createHelper().isExactMatchRegex(testExactMatch, true));

        // test 4 ensure that the exact match literal does NOT match the test match any more
        assertFalse(Pattern.matches(testExactMatch, testMatch));

        // test 5 ensure that the exact match literal matches the original (regex) string
        assertTrue(Pattern.matches(testExactMatch, testRegex));

        // test 6 ensure that un-qualifying the exact match gives us our original string back
        String testBack = createHelper().getUnqualifiedLiteralString(testExactMatch);
        assertEquals(testRegex, testBack);

        String testString = "a-b-c-d-e-f-g";

        // test 7 test that "contains" works
        String contains = "c-d-e";
        String testContains = createHelper().getContainsRegex(contains);
        assertTrue(createHelper().isContainsRegex(testContains));
        assertTrue(createHelper().isContainsRegex(testContains, false));
        assertFalse(createHelper().isContainsRegex(testContains, true));
        assertTrue(Pattern.matches(testContains, testString));
        assertFalse(Pattern.matches(testContains, "d"));
        assertEquals(contains, createHelper().getUnqualifiedLiteralString(testContains));
        contains = "C-d-E";
        testContains = createHelper().getContainsRegex(contains, true);
        assertTrue(createHelper().isContainsRegex(testContains));
        assertTrue(createHelper().isContainsRegex(testContains, true));
        assertFalse(createHelper().isContainsRegex(testContains, false));
        assertTrue(Pattern.matches(testContains, testString));
        assertFalse(Pattern.matches(testContains, "d"));
        assertEquals(contains, createHelper().getUnqualifiedLiteralString(testContains));

        // test 8 test that "startswith" works
        String startsWith = "a-b-c";
        String testStartsWith = createHelper().getStartsWithRegex(startsWith);
        assertTrue(createHelper().isStartsWithRegex(testStartsWith));
        assertTrue(createHelper().isStartsWithRegex(testStartsWith, false));
        assertFalse(createHelper().isStartsWithRegex(testStartsWith, true));
        assertTrue(Pattern.matches(testStartsWith, testString));
        assertFalse(Pattern.matches(testStartsWith, "x" + testString));
        assertEquals(startsWith, createHelper().getUnqualifiedLiteralString(testStartsWith));
        startsWith = "A-B-c";
        testStartsWith = createHelper().getStartsWithRegex(startsWith, true);
        assertTrue(createHelper().isStartsWithRegex(testStartsWith));
        assertTrue(createHelper().isStartsWithRegex(testStartsWith, true));
        assertFalse(createHelper().isStartsWithRegex(testStartsWith, false));
        assertTrue(Pattern.matches(testContains, testString));
        assertFalse(Pattern.matches(testContains, "d"));
        assertEquals(startsWith, createHelper().getUnqualifiedLiteralString(testStartsWith));

        // test 9 test that "endswith" works
        String endsWith = "e-f-g";
        String testEndsWith = createHelper().getEndsWithRegex(endsWith);
        assertTrue(createHelper().isEndsWithRegex(testEndsWith));
        assertTrue(createHelper().isEndsWithRegex(testEndsWith, false));
        assertFalse(createHelper().isEndsWithRegex(testEndsWith, true));
        assertTrue(Pattern.matches(testEndsWith, testString));
        assertFalse(Pattern.matches(testEndsWith, testString + "x"));
        assertEquals(endsWith, createHelper().getUnqualifiedLiteralString(testEndsWith));
        endsWith = "e-F-G";
        testEndsWith = createHelper().getEndsWithRegex(endsWith, true);
        assertTrue(createHelper().isEndsWithRegex(testEndsWith));
        assertTrue(createHelper().isEndsWithRegex(testEndsWith, true));
        assertFalse(createHelper().isEndsWithRegex(testEndsWith, false));
        assertTrue(Pattern.matches(testEndsWith, testString));
        assertFalse(Pattern.matches(testEndsWith, testString + "x"));
        assertEquals(endsWith, createHelper().getUnqualifiedLiteralString(testEndsWith));

        // test 10 ensure that multi-literal regexes do not match
        String multiExact = "\\Qsome.exact\\Eand.not\\Qand.more\\E";
        String multiError = "\\Qsome.exact\\Eand.not\\E";
        String multiContains = ".*" + multiExact + ".*";
        String multiStarts = multiExact + ".*";
        String multiEnds = ".*" + multiExact;
        assertFalse(createHelper().isExactMatchRegex(multiExact));
        assertFalse(createHelper().isExactMatchRegex(multiError));
        assertFalse(createHelper().isContainsRegex(multiContains));
        assertFalse(createHelper().isStartsWithRegex(multiStarts));
        assertFalse(createHelper().isEndsWithRegex(multiEnds));

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
