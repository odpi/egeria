/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Verify the SQL generated for property conditions: an "or" of attribute-row conditions is tested by one
 * sub-select on the attribute table, which its indexes can serve; other conditions keep their own clauses.
 */
public class QueryBuilderPropertyConditionsTest
{
    private static final String IN_SUB_SELECT = "(entity.instance_guid, entity.version) in (select entity_attribute_value.instance_guid, entity_attribute_value.version from entity_attribute_value where ";


    @Test
    public void testOrOfValueConditionsIsOneSubSelect() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ANY,
                                         this.getCondition("qualifiedName", PropertyComparisonOperator.EQ, "A::B"),
                                         this.getCondition("resourceName", PropertyComparisonOperator.EQ, "it's"),
                                         this.getCondition("displayName", PropertyComparisonOperator.CASE_INSENSITIVE_LIKE, "sup_plier"));

        assertTrue(sql.contains(IN_SUB_SELECT), sql);
        assertFalse(sql.contains("exists"), sql);
        assertTrue(sql.contains("(entity_attribute_value.attribute_name = 'qualifiedName' and property_value = 'A::B') or " +
                                "(entity_attribute_value.attribute_name = 'resourceName' and property_value = 'it''s') or " +
                                "(entity_attribute_value.attribute_name = 'displayName' and property_value ilike '%sup\\_plier%')"), sql);
    }


    @Test
    public void testSingleValueConditionKeepsExists() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ANY,
                                         this.getCondition("qualifiedName", PropertyComparisonOperator.EQ, "A::B"));

        assertFalse(sql.contains(IN_SUB_SELECT), sql);
        assertTrue(sql.contains("exists (select 1 from entity_attribute_value where entity.instance_guid = entity_attribute_value.instance_guid " +
                                "and entity.version = entity_attribute_value.version and entity_attribute_value.attribute_name = 'qualifiedName' " +
                                "and property_value = 'A::B')"), sql);
    }


    @Test
    public void testAndOfValueConditionsIsUnchanged() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ALL,
                                         this.getCondition("qualifiedName", PropertyComparisonOperator.EQ, "A::B"),
                                         this.getCondition("displayName", PropertyComparisonOperator.EQ, "B"));

        assertFalse(sql.contains(IN_SUB_SELECT), sql);
        assertEquals(sql.split("exists \\(select 1").length - 1, 2, sql);
        assertTrue(sql.contains(" and "), sql);
    }


    @Test
    public void testIsNullAndHeaderColumnsStaySeparate() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ANY,
                                         this.getCondition("qualifiedName", PropertyComparisonOperator.EQ, "A::B"),
                                         this.getCondition("displayName", PropertyComparisonOperator.EQ, "B"),
                                         this.getCondition("description", PropertyComparisonOperator.IS_NULL, null),
                                         this.getCondition("createdBy", PropertyComparisonOperator.EQ, "garygeeke"));

        assertTrue(sql.contains(IN_SUB_SELECT), sql);
        assertTrue(sql.contains("not exists (select 1 from entity_attribute_value"), sql);
        assertTrue(sql.contains("(entity.created_by = 'garygeeke')"), sql);
        assertFalse(sql.contains("attribute_name = 'description' and"), sql);
    }


    /**
     * IN tests the attribute rows against each listed value.  The elements of an array property are rows of their
     * own under the array's attribute name, so the same SQL matches an array sharing any value with the list.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testInListMatchesAnyListedValue() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ALL,
                                         this.getListCondition("zoneMembership", "quarantine", "it's"));

        assertTrue(sql.contains("entity_attribute_value.attribute_name = 'zoneMembership' and property_value = 'quarantine')"), sql);
        assertTrue(sql.contains(" or "), sql);
        assertTrue(sql.contains("entity_attribute_value.attribute_name = 'zoneMembership' and property_value = 'it''s')"), sql);
    }


    /**
     * An empty list matches nothing.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testEmptyInListMatchesNothing() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.ALL,
                                         this.getListCondition("zoneMembership"));

        assertTrue(sql.contains("(false)"), sql);
    }


    /**
     * NONE is the negation of ANY.  It used to be treated as ALL.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testNoneNegatesTheConditions() throws Exception
    {
        String sql = this.getWhereClause(MatchCriteria.NONE,
                                         this.getCondition("qualifiedName", PropertyComparisonOperator.EQ, "A::B"),
                                         this.getCondition("displayName", PropertyComparisonOperator.EQ, "B"));

        assertTrue(sql.contains(" not ( " + IN_SUB_SELECT), sql);
        assertFalse(sql.contains("exists"), sql);
    }


    /**
     * An entity's classifications share its guid, so a classification's attribute rows have to be matched on
     * its name as well as its guid and version.  Without it, a condition on one classification is tested against
     * the properties of another at the same version - the zone filter's Anchors condition on zoneMembership was
     * satisfied by the entity's ZoneMembership classification, so stale Anchors zones did not hide anything.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testClassificationPropertiesAreMatchedOnTheirOwnClassification() throws Exception
    {
        final String nameJoin = "classification.classification_name = classification_attribute_value.classification_name";

        String single = this.getClassificationMembershipClause(MatchCriteria.ALL,
                                                               this.getCondition("zoneMembership", PropertyComparisonOperator.NOT_NULL, null));

        assertTrue(single.contains("exists (select 1 from classification_attribute_value where classification.instance_guid = classification_attribute_value.instance_guid " +
                                   "and classification.version = classification_attribute_value.version and " + nameJoin), single);

        String several = this.getClassificationMembershipClause(MatchCriteria.ANY,
                                                                this.getCondition("anchorGUID", PropertyComparisonOperator.EQ, "a"),
                                                                this.getCondition("anchorTypeName", PropertyComparisonOperator.EQ, "b"));

        assertTrue(several.contains("(classification.instance_guid, classification.version, classification.classification_name) in (select " +
                                    "classification_attribute_value.instance_guid, classification_attribute_value.version, " +
                                    "classification_attribute_value.classification_name from classification_attribute_value"), several);

        String entity = this.getWhereClause(MatchCriteria.ALL, this.getCondition("qualifiedName", PropertyComparisonOperator.NOT_NULL, null));

        assertFalse(entity.contains("classification_name"), entity);
    }


    private String getClassificationMembershipClause(MatchCriteria propertyMatchCriteria, PropertyCondition... conditions) throws Exception
    {
        SearchProperties matchProperties = new SearchProperties();

        matchProperties.setMatchCriteria(propertyMatchCriteria);
        matchProperties.setConditions(new ArrayList<>(List.of(conditions)));

        ClassificationCondition classificationCondition = new ClassificationCondition();

        classificationCondition.setName("Anchors");
        classificationCondition.setMatchProperties(matchProperties);

        SearchClassifications searchClassifications = new SearchClassifications();

        searchClassifications.setMatchCriteria(MatchCriteria.NONE);
        searchClassifications.setConditions(new ArrayList<>(List.of(classificationCondition)));

        QueryBuilder queryBuilder = new QueryBuilder("classification", "classification_attribute_value", null, "TestRepository");

        queryBuilder.setSearchClassifications(searchClassifications);

        return queryBuilder.getClassificationMembershipClause("entity.instance_guid").replaceAll("\\s+", " ");
    }


    private PropertyCondition getListCondition(String name, String... values)
    {
        PropertyCondition  condition  = new PropertyCondition();
        ArrayPropertyValue listValue  = new ArrayPropertyValue();
        InstanceProperties listValues = new InstanceProperties();

        for (int i = 0; i < values.length; i++)
        {
            PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

            propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            propertyValue.setTypeName("string");
            propertyValue.setPrimitiveValue(values[i]);
            listValues.setProperty(Integer.toString(i), propertyValue);
        }

        listValue.setArrayCount(values.length);
        listValue.setArrayValues(listValues);

        condition.setProperty(name);
        condition.setOperator(PropertyComparisonOperator.IN);
        condition.setValue(listValue);

        return condition;
    }


    private String getWhereClause(MatchCriteria matchCriteria, PropertyCondition... conditions) throws Exception
    {
        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setMatchCriteria(matchCriteria);
        searchProperties.setConditions(new ArrayList<>(List.of(conditions)));

        QueryBuilder queryBuilder = new QueryBuilder("entity", "entity_attribute_value", null, "TestRepository");

        queryBuilder.setSearchProperties(searchProperties);

        return queryBuilder.getAsOfTimeWhereClause().replaceAll("\\s+", " ");
    }


    private PropertyCondition getCondition(String name, PropertyComparisonOperator operator, String value)
    {
        PropertyCondition condition = new PropertyCondition();

        condition.setProperty(name);
        condition.setOperator(operator);

        if (value != null)
        {
            PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

            propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            propertyValue.setTypeName("string");
            propertyValue.setPrimitiveValue(value);
            condition.setValue(propertyValue);
        }
        else
        {
            condition.setValue((InstancePropertyValue) null);
        }

        return condition;
    }
}
