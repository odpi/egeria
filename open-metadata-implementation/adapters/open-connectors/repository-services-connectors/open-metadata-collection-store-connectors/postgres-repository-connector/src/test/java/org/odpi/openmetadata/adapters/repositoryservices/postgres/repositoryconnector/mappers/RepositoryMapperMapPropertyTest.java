/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.lang.reflect.Proxy;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Verify that map properties whose keys contain colons (such as the "tag:coco" keys that the OpenLineage
 * cataloguer puts in additionalProperties) are stored and read back intact, and that rows stored before
 * nested property names were escaped can still be read.
 */
public class RepositoryMapperMapPropertyTest
{
    private static final String repositoryName = "TestRepository";
    private static final String mapTypeName    = "map<string,string>";


    @Test
    public void testMapKeysWithColonsRoundTrip() throws Exception
    {
        Map<String, String> additionalProperties = new LinkedHashMap<>();

        additionalProperties.put("jobType.integration", "AIRFLOW");
        additionalProperties.put("tag:coco", "coco");
        additionalProperties.put("tag:Coco core", "Coco core");
        additionalProperties.put("tag:digital-product-feed", "digital-product-feed");
        additionalProperties.put("tag", "a key that is also the prefix of others");
        additionalProperties.put("ends\\", "a key ending in a backslash");
        additionalProperties.put("a\\:b", "a key with an escape sequence in it");

        InstanceProperties properties = new InstanceProperties();
        properties.setProperty("displayName", this.getStringValue("coco_feed_coco_pharma"));
        properties.setProperty("additionalProperties", this.getMapValue(additionalProperties));

        RepositoryMapper mapper = new RepositoryMapper(this.getRepositoryHelper(), repositoryName);

        List<Map<String, JDBCDataValue>> rows = mapper.extractValuesFromInstanceProperties("guid-1", null, 1L, "DeployedSoftwareComponent",
                                                                                          properties, null, null);
        InstanceProperties restored = mapper.getInstanceProperties(null, rows);

        assertEquals(this.getMapContents(restored.getPropertyValue("additionalProperties")), additionalProperties);
        assertEquals(((PrimitivePropertyValue) restored.getPropertyValue("displayName")).getPrimitiveValue(), "coco_feed_coco_pharma");

        Set<String> storedNames = new HashSet<>();
        for (Map<String, JDBCDataValue> row : rows)
        {
            storedNames.add(row.get(RepositoryColumn.PROPERTY_NAME.getColumnName()).getDataValue().toString());
        }
        assertTrue(storedNames.contains("additionalProperties:tag\\:coco"), storedNames.toString());
    }


    @Test
    public void testLegacyUnescapedKeysWithColonsAreReadable() throws Exception
    {
        /*
         * The rows the OpenLineage cataloguer created before nested names were escaped: three keys with colons,
         * stored as if "tag" were a nested map with no row of its own.
         */
        List<Map<String, JDBCDataValue>> rows = new ArrayList<>();

        rows.add(this.getRow("additionalProperties", "additionalProperties", null, InstancePropertyCategory.MAP.getName(), mapTypeName));
        rows.add(this.getRow("additionalProperties:jobType.integration", "additionalProperties", "AIRFLOW", "string", "string"));
        rows.add(this.getRow("additionalProperties:tag:coco", "additionalProperties", "coco", "string", "string"));
        rows.add(this.getRow("additionalProperties:tag:Coco core", "additionalProperties", "Coco core", "string", "string"));
        rows.add(this.getRow("additionalProperties:tag:digital-product-feed", "additionalProperties", "digital-product-feed", "string", "string"));

        RepositoryMapper mapper = new RepositoryMapper(this.getRepositoryHelper(), repositoryName);

        InstanceProperties restored = mapper.getInstanceProperties(null, rows);

        Map<String, String> expected = new HashMap<>();
        expected.put("jobType.integration", "AIRFLOW");
        expected.put("tag:coco", "coco");
        expected.put("tag:Coco core", "Coco core");
        expected.put("tag:digital-product-feed", "digital-product-feed");

        assertEquals(this.getMapContents(restored.getPropertyValue("additionalProperties")), expected);
    }


    @Test
    public void testLegacySingleUnescapedKeyKeepsItsFullName() throws Exception
    {
        List<Map<String, JDBCDataValue>> rows = new ArrayList<>();

        rows.add(this.getRow("additionalProperties", "additionalProperties", null, InstancePropertyCategory.MAP.getName(), mapTypeName));
        rows.add(this.getRow("additionalProperties:tag:coco", "additionalProperties", "coco", "string", "string"));

        RepositoryMapper mapper = new RepositoryMapper(this.getRepositoryHelper(), repositoryName);

        assertEquals(this.getMapContents(mapper.getInstanceProperties(null, rows).getPropertyValue("additionalProperties")),
                     Map.of("tag:coco", "coco"));
    }


    private Map<String, String> getMapContents(InstancePropertyValue value)
    {
        assertTrue(value instanceof MapPropertyValue, String.valueOf(value));

        Map<String, String> contents = new HashMap<>();
        InstanceProperties mapValues = ((MapPropertyValue) value).getMapValues();

        for (String key : mapValues.getInstanceProperties().keySet())
        {
            contents.put(key, String.valueOf(((PrimitivePropertyValue) mapValues.getPropertyValue(key)).getPrimitiveValue()));
        }

        return contents;
    }


    private PrimitivePropertyValue getStringValue(String value)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setTypeName("string");
        propertyValue.setPrimitiveValue(value);

        return propertyValue;
    }


    private MapPropertyValue getMapValue(Map<String, String> contents)
    {
        InstanceProperties mapValues = new InstanceProperties();

        for (Map.Entry<String, String> entry : contents.entrySet())
        {
            mapValues.setProperty(entry.getKey(), this.getStringValue(entry.getValue()));
        }

        MapPropertyValue mapPropertyValue = new MapPropertyValue();

        mapPropertyValue.setTypeName(mapTypeName);
        mapPropertyValue.setMapValues(mapValues);

        return mapPropertyValue;
    }


    private Map<String, JDBCDataValue> getRow(String propertyName,
                                              String attributeName,
                                              String propertyValue,
                                              String propertyCategory,
                                              String attributeTypeName)
    {
        Map<String, JDBCDataValue> row = new HashMap<>();

        row.put(RepositoryColumn.PROPERTY_NAME.getColumnName(), new JDBCDataValue(propertyName, ColumnType.STRING.getJdbcType()));
        row.put(RepositoryColumn.ATTRIBUTE_NAME.getColumnName(), new JDBCDataValue(attributeName, ColumnType.STRING.getJdbcType()));
        row.put(RepositoryColumn.PROPERTY_VALUE.getColumnName(), new JDBCDataValue(propertyValue, ColumnType.STRING.getJdbcType()));
        row.put(RepositoryColumn.PROPERTY_CATEGORY.getColumnName(), new JDBCDataValue(propertyCategory, ColumnType.STRING.getJdbcType()));
        row.put(RepositoryColumn.ATTRIBUTE_TYPE_NAME.getColumnName(), new JDBCDataValue(attributeTypeName, ColumnType.STRING.getJdbcType()));
        row.put(RepositoryColumn.IS_UNIQUE_ATTRIBUTE.getColumnName(), new JDBCDataValue(false, ColumnType.BOOLEAN.getJdbcType()));

        return row;
    }


    /**
     * A repository helper that answers only the questions the mapper asks about the properties.
     *
     * @return repository helper
     */
    private OMRSRepositoryHelper getRepositoryHelper()
    {
        PrimitiveDef stringDef = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        stringDef.setGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
        stringDef.setName("string");

        CollectionDef mapDef = new CollectionDef(CollectionDefCategory.OM_COLLECTION_MAP);
        mapDef.setGUID("005c7c14-ac84-4136-beed-959401b041f8");
        mapDef.setName(mapTypeName);

        return (OMRSRepositoryHelper) Proxy.newProxyInstance(
                OMRSRepositoryHelper.class.getClassLoader(),
                new Class<?>[]{OMRSRepositoryHelper.class},
                (proxy, method, args) -> switch (method.getName())
                {
                    case "getUniqueAttributesList" -> new ArrayList<String>();
                    case "getAttributeTypeDefByName" -> mapTypeName.equals(args[1]) ? mapDef : stringDef;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
