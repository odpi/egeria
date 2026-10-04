/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
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
