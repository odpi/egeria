/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The converter is used for creating and mapping required properties between different objects.
 */
public class Converter {

    OMRSRepositoryHelper repositoryHelper;

    public Converter(OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHelper = repositoryHelper;
    }

    /**
     * Create entity lineage entity.
     *
     * @param entityDetail the entity detail
     * @return the lineage entity
     */
    public LineageEntity createLineageEntity(EntityDetail entityDetail) {
        LineageEntity lineageEntity = new LineageEntity();
        setInstanceHeaderProperties(entityDetail, lineageEntity);
        lineageEntity.setProperties(instancePropertiesToMap(entityDetail.getProperties()));
        return lineageEntity;
    }

    /**
     * Create entity lineage entity starting from an entity proxy
     *
     * @param entityProxy the entity proxy
     * @return the lineage entity
     */
    public LineageEntity createLineageEntityFromProxy(EntityProxy entityProxy) {
        return getLineageEntityFromLineageProxy(entityProxy);
    }

    /**
     * Creates the lineage relationship.
     *
     * @param relationship the relationship details
     * @return the lineage relationship
     */
    public LineageRelationship createLineageRelationship(Relationship relationship) {
        LineageRelationship lineageRelationship = new LineageRelationship();
        setInstanceHeaderProperties(relationship, lineageRelationship);
        lineageRelationship.setProperties(instancePropertiesToMap(relationship.getProperties()));

        LineageEntity firstEntity = getLineageEntityFromLineageProxy(relationship.getEntityOneProxy());
        LineageEntity secondEntity = getLineageEntityFromLineageProxy(relationship.getEntityTwoProxy());

        lineageRelationship.setSourceEntity(firstEntity);
        lineageRelationship.setTargetEntity(secondEntity);

        return lineageRelationship;
    }

    /**
     * Retrieve the properties from an InstanceProperties object and return them as a map
     *
     * @param properties the properties
     * @return the map properties
     */
    public Map<String, String> instancePropertiesToMap(InstanceProperties properties) {
        Map<String, Object> instancePropertiesAsMap = repositoryHelper.getInstancePropertiesAsMap(properties);
        Map<String, String> attributes = new HashMap<>();

        String methodName = "instancePropertiesToMap";

        if (MapUtils.isNotEmpty(instancePropertiesAsMap)) {
            instancePropertiesAsMap.forEach((key, value) -> {
                if (value instanceof ArrayPropertyValue) {
                    List<String> stringArrayProperty = repositoryHelper.getStringArrayProperty(AssetLineageConstants.ASSET_LINEAGE_OMAS, key, properties, methodName);
                    attributes.put(key, listToString(stringArrayProperty));
                } else if (value instanceof MapPropertyValue) {
                    Map<String, Object> mapProperty = repositoryHelper.getMapFromProperty(AssetLineageConstants.ASSET_LINEAGE_OMAS, key, properties, methodName);
                    attributes.put(key, mapToString(mapProperty));
                } else {
                    if (key.equals("name")) {
                        key = "displayName";
                    }
                    attributes.put(key, String.valueOf(value));
                }
            });
        }
        return attributes;
    }

    private void setInstanceHeaderProperties(InstanceHeader instanceHeader, LineageEntity lineageEntity) {
        lineageEntity.setGuid(instanceHeader.getGUID());
        lineageEntity.setCreatedBy(instanceHeader.getCreatedBy());
        lineageEntity.setCreateTime(instanceHeader.getCreateTime());
        lineageEntity.setTypeDefName(instanceHeader.getType().getTypeDefName());
        lineageEntity.setUpdatedBy(instanceHeader.getUpdatedBy());
        lineageEntity.setUpdateTime(instanceHeader.getUpdateTime());
        lineageEntity.setVersion(instanceHeader.getVersion());
        lineageEntity.setMetadataCollectionId(instanceHeader.getMetadataCollectionId());
    }

    private LineageEntity getLineageEntityFromLineageProxy(EntityProxy entityProxy) {
        LineageEntity entity = new LineageEntity();
        entity.setGuid(entityProxy.getGUID());
        entity.setVersion(entityProxy.getVersion());
        entity.setCreatedBy(entityProxy.getCreatedBy());
        entity.setUpdatedBy(entityProxy.getUpdatedBy());
        entity.setCreateTime(entityProxy.getCreateTime());
        entity.setUpdateTime(entityProxy.getUpdateTime());
        entity.setTypeDefName(entityProxy.getType().getTypeDefName());
        entity.setProperties(instancePropertiesToMap(entityProxy.getUniqueProperties()));
        return entity;
    }

    private String listToString(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return String.join(",", list);
    }

    private String mapToString(Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        return map.keySet().stream().map(key -> key + ": " + map.get(key))
                .collect(Collectors.joining(", "));
    }
}