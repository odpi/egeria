/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Verify that a type definition survives the trip into a type_definition row and back out again - which is the
 * trip every stored type makes when the server restarts.  No database is needed: the row is the map of column
 * values that the JDBC resource connector would insert and later return.
 */
public class TypeDefinitionMapperTest
{
    private static final String repositoryName = "TestRepository";
    private static final String origin         = "b0a4a2f5-2a1d-4a1e-9c6a-6f2f3f7f0c11";


    /**
     * An entity type with a supertype and an attribute whose type is an enum restores as an EntityDef equal to
     * the one stored, and the columns describing it are filled in.
     *
     * @throws Exception test failure
     */
    @Test
    public void testEntityDefRoundTrip() throws Exception
    {
        EntityDef entityDef = new EntityDef(TypeDefCategory.ENTITY_DEF, "4d6b3a3e-8a5f-4c43-8d8f-7f4f3a5b6c01", "TestEntity", 3L, "1.2");

        entityDef.setOrigin(origin);
        entityDef.setDescription("An entity type defined through the API - with a quote ' in its description.");
        entityDef.setSuperType(this.getTypeDefLink("896d14c2-7522-4f6c-8519-757711943fe6", "OpenMetadataRoot"));
        entityDef.setCreatedBy("garygeeke");
        entityDef.setCreateTime(new Date(1_700_000_000_000L));
        entityDef.setOptions(Map.of("anchorScope", "self"));

        TypeDefAttribute attribute = new TypeDefAttribute();

        attribute.setAttributeName("colour");
        attribute.setAttributeType(this.getEnumDef());
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setAttributeStatus(TypeDefAttributeStatus.ACTIVE_ATTRIBUTE);
        entityDef.setPropertiesDefinition(List.of(attribute));

        Date firstStoredTime = new Date(1_700_000_100_000L);
        Date lastStoredTime  = new Date(1_700_000_200_000L);

        TypeDefinitionMapper storedMapper   = new TypeDefinitionMapper(repositoryName, entityDef, firstStoredTime, lastStoredTime);
        TypeDefinitionMapper restoredMapper = new TypeDefinitionMapper(repositoryName, this.getReturnedRow(storedMapper.getTypeDefinitionRow()));

        assertEquals(restoredMapper.getTypeGUID(), entityDef.getGUID());
        assertEquals(restoredMapper.getTypeName(), entityDef.getName());
        assertEquals(restoredMapper.getTypeVersion(), entityDef.getVersion());
        assertEquals(restoredMapper.getTypeCategory(), TypeDefinitionMapper.TYPE_DEF_CATEGORY);
        assertEquals(restoredMapper.getFirstStoredTime(), firstStoredTime);
        assertEquals(restoredMapper.getLastStoredTime(), lastStoredTime);
        assertTrue(restoredMapper.isTypeDef());
        assertFalse(restoredMapper.isAttributeTypeDef());

        TypeDef restoredTypeDef = restoredMapper.getTypeDef();

        assertTrue(restoredTypeDef instanceof EntityDef);
        assertEquals(restoredTypeDef, entityDef);
        assertTrue(restoredTypeDef.getPropertiesDefinition().get(0).getAttributeType() instanceof EnumDef);
    }


    /**
     * A relationship type restores as a RelationshipDef with its ends.
     *
     * @throws Exception test failure
     */
    @Test
    public void testRelationshipDefRoundTrip() throws Exception
    {
        RelationshipDef relationshipDef = new RelationshipDef();

        relationshipDef.setGUID("0d3a2b7c-51d2-4f7e-9b0e-a1c9d8e7f602");
        relationshipDef.setName("TestRelationship");
        relationshipDef.setVersion(1L);
        relationshipDef.setOrigin(origin);
        relationshipDef.setMultiLink(true);
        relationshipDef.setEndDef1(this.getEndDef("owner"));
        relationshipDef.setEndDef2(this.getEndDef("owned"));

        Date storeTime = new Date();

        TypeDefinitionMapper storedMapper   = new TypeDefinitionMapper(repositoryName, relationshipDef, storeTime, storeTime);
        TypeDefinitionMapper restoredMapper = new TypeDefinitionMapper(repositoryName, this.getReturnedRow(storedMapper.getTypeDefinitionRow()));

        TypeDef restoredTypeDef = restoredMapper.getTypeDef();

        assertTrue(restoredTypeDef instanceof RelationshipDef);
        assertEquals(restoredTypeDef, relationshipDef);
    }


    /**
     * A classification type restores as a ClassificationDef with its valid entity types.
     *
     * @throws Exception test failure
     */
    @Test
    public void testClassificationDefRoundTrip() throws Exception
    {
        ClassificationDef classificationDef = new ClassificationDef();

        classificationDef.setGUID("8e5b9f0a-3c2d-4b1a-8f7e-6d5c4b3a2f03");
        classificationDef.setName("TestClassification");
        classificationDef.setVersion(2L);
        classificationDef.setOrigin(origin);
        classificationDef.setPropagatable(true);
        classificationDef.setValidEntityDefs(List.of(this.getTypeDefLink("4d6b3a3e-8a5f-4c43-8d8f-7f4f3a5b6c01", "TestEntity")));

        Date storeTime = new Date();

        TypeDefinitionMapper storedMapper   = new TypeDefinitionMapper(repositoryName, classificationDef, storeTime, storeTime);
        TypeDefinitionMapper restoredMapper = new TypeDefinitionMapper(repositoryName, this.getReturnedRow(storedMapper.getTypeDefinitionRow()));

        TypeDef restoredTypeDef = restoredMapper.getTypeDef();

        assertTrue(restoredTypeDef instanceof ClassificationDef);
        assertEquals(restoredTypeDef, classificationDef);
    }


    /**
     * An enum restores as an EnumDef, keeping its origin, and is recorded as an AttributeTypeDef.
     *
     * @throws Exception test failure
     */
    @Test
    public void testEnumDefRoundTrip() throws Exception
    {
        EnumDef enumDef = this.getEnumDef();

        Date storeTime = new Date();

        TypeDefinitionMapper storedMapper   = new TypeDefinitionMapper(repositoryName, enumDef, storeTime, storeTime);
        TypeDefinitionMapper restoredMapper = new TypeDefinitionMapper(repositoryName, this.getReturnedRow(storedMapper.getTypeDefinitionRow()));

        assertEquals(restoredMapper.getTypeCategory(), TypeDefinitionMapper.ATTRIBUTE_TYPE_DEF_CATEGORY);
        assertTrue(restoredMapper.isAttributeTypeDef());
        assertFalse(restoredMapper.isTypeDef());

        AttributeTypeDef restoredAttributeTypeDef = restoredMapper.getAttributeTypeDef();

        assertTrue(restoredAttributeTypeDef instanceof EnumDef);
        assertEquals(restoredAttributeTypeDef, enumDef);
        assertEquals(restoredAttributeTypeDef.getOrigin(), origin);
    }


    /**
     * The row carries every column of the table, and the version as the type the table declares.
     *
     * @throws Exception test failure
     */
    @Test
    public void testRowColumns() throws Exception
    {
        Date storeTime = new Date();

        Map<String, JDBCDataValue> row = new TypeDefinitionMapper(repositoryName, this.getEnumDef(), storeTime, storeTime).getTypeDefinitionRow();

        assertEquals(row.keySet(), Set.of(RepositoryColumn.TYPE_GUID.getColumnName(),
                                          RepositoryColumn.TYPE_NAME.getColumnName(),
                                          RepositoryColumn.TYPE_CATEGORY.getColumnName(),
                                          RepositoryColumn.TYPE_VERSION.getColumnName(),
                                          RepositoryColumn.TYPE_DEFINITION.getColumnName(),
                                          RepositoryColumn.FIRST_STORED_TIME.getColumnName(),
                                          RepositoryColumn.LAST_STORED_TIME.getColumnName()));
        assertEquals(row.get(RepositoryColumn.TYPE_VERSION.getColumnName()).getTargetSQLType(),
                     RepositoryColumn.TYPE_VERSION.getColumnType().getJdbcType());
    }


    /**
     * JSON that does not describe a type definition is reported as a RepositoryErrorException rather than
     * escaping as a Jackson exception.
     *
     * @throws Exception test failure
     */
    @Test
    public void testUnreadableJSON() throws Exception
    {
        Date storeTime = new Date();

        Map<String, JDBCDataValue> row = this.getReturnedRow(new TypeDefinitionMapper(repositoryName, this.getEnumDef(), storeTime, storeTime).getTypeDefinitionRow());

        row.put(RepositoryColumn.TYPE_DEFINITION.getColumnName(), new JDBCDataValue("{\"class\":\"NotAType\"}", RepositoryColumn.TYPE_DEFINITION.getColumnType().getJdbcType()));

        TypeDefinitionMapper restoredMapper = new TypeDefinitionMapper(repositoryName, row);

        assertThrows(RepositoryErrorException.class, restoredMapper::getAttributeTypeDef);
    }


    /**
     * Return the row as the JDBC resource connector hands it back from a query: bigint columns arrive as
     * BigDecimal rather than the Long that was inserted.
     *
     * @param insertedRow row as inserted
     * @return row as returned
     */
    private Map<String, JDBCDataValue> getReturnedRow(Map<String, JDBCDataValue> insertedRow)
    {
        Map<String, JDBCDataValue> returnedRow = new HashMap<>();

        for (Map.Entry<String, JDBCDataValue> column : insertedRow.entrySet())
        {
            JDBCDataValue value = column.getValue();

            if (value.getDataValue() instanceof Long longValue)
            {
                returnedRow.put(column.getKey(), new JDBCDataValue(BigDecimal.valueOf(longValue), value.getTargetSQLType()));
            }
            else
            {
                returnedRow.put(column.getKey(), value);
            }
        }

        return returnedRow;
    }


    /**
     * Return an enum definition homed in the test repository.
     *
     * @return enum def
     */
    private EnumDef getEnumDef()
    {
        EnumDef enumDef = new EnumDef();

        enumDef.setGUID("c3e2a1b0-9f8e-4d7c-b6a5-948372615a04");
        enumDef.setName("TestColour");
        enumDef.setVersion(1L);
        enumDef.setVersionName("1.0");
        enumDef.setDescription("Colours for testing.");
        enumDef.setOrigin(origin);

        EnumElementDef red = new EnumElementDef();

        red.setOrdinal(0);
        red.setValue("Red");
        red.setDescription("The colour red.");

        EnumElementDef blue = new EnumElementDef();

        blue.setOrdinal(1);
        blue.setValue("Blue");
        blue.setDescription("The colour blue.");

        enumDef.setElementDefs(List.of(red, blue));
        enumDef.setDefaultValue(red);

        return enumDef;
    }


    /**
     * Return a link to a type.
     *
     * @param guid unique identifier of the type
     * @param name unique name of the type
     * @return link
     */
    private TypeDefLink getTypeDefLink(String guid,
                                       String name)
    {
        TypeDefLink typeDefLink = new TypeDefLink();

        typeDefLink.setGUID(guid);
        typeDefLink.setName(name);

        return typeDefLink;
    }


    /**
     * Return a relationship end that attaches to the test entity type.
     *
     * @param attributeName name of the end
     * @return end definition
     */
    private RelationshipEndDef getEndDef(String attributeName)
    {
        RelationshipEndDef endDef = new RelationshipEndDef();

        endDef.setEntityType(this.getTypeDefLink("4d6b3a3e-8a5f-4c43-8d8f-7f4f3a5b6c01", "TestEntity"));
        endDef.setAttributeName(attributeName);
        endDef.setAttributeCardinality(RelationshipEndCardinality.ANY_NUMBER);

        return endDef;
    }
}
