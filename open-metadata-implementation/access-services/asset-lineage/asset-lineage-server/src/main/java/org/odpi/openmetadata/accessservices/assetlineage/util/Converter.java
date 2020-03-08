/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * The converter is used for creating and mapping required properties between different objects.
 */
public class Converter {


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
     * Creates the lineage relationship.
     *
     * @param relationship the relationship details
     * @return the lineage relationship
     */
    public LineageRelationship createLineageRelationship(Relationship relationship) {
        LineageRelationship lineageRelationship = new LineageRelationship();
        setInstanceHeaderProperties(relationship, lineageRelationship);
        lineageRelationship.setProperties(instancePropertiesToMap(relationship.getProperties()));

        lineageRelationship.setFirstEndGUID(relationship.getEntityTwoProxy().getGUID());
        lineageRelationship.setSecondEndGUID(relationship.getEntityTwoProxy().getGUID());

        return lineageRelationship;
    }
    /**
     * Retrieve the properties from an InstanceProperties object and return them as a map
     *
     * @param properties the properties
     * @return the map properties
     */
    public Map<String, String> instancePropertiesToMap(InstanceProperties properties) {
        Map<String, String> attributes = new HashMap<>();

        if (properties == null)
            return attributes;

        Map<String, InstancePropertyValue> instanceProperties = properties.getInstanceProperties();

        if (instanceProperties == null)
            return attributes;

        for (Map.Entry<String, InstancePropertyValue> property : instanceProperties.entrySet()) {

            if (property.getValue() == null)
                continue;

            String propertyValue = propertyValueToString(property.getValue());
            if (propertyValue.equals(""))
                continue;

            if (property.getKey().equals("name"))
                attributes.put("displayName", propertyValue);
            else
                attributes.put(property.getKey(), propertyValue);
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
    }

    private String propertyValueToString(InstancePropertyValue ipv) {
        if (ipv instanceof PrimitivePropertyValue) {
            PrimitiveDefCategory primtype =
                    ((PrimitivePropertyValue) ipv).getPrimitiveDefCategory();
            switch (primtype) {
                case OM_PRIMITIVE_TYPE_STRING:
                    return (String) ((PrimitivePropertyValue) ipv).getPrimitiveValue();
                case OM_PRIMITIVE_TYPE_INT:
                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                case OM_PRIMITIVE_TYPE_BOOLEAN:
                case OM_PRIMITIVE_TYPE_BYTE:
                case OM_PRIMITIVE_TYPE_CHAR:
                case OM_PRIMITIVE_TYPE_DATE:
                case OM_PRIMITIVE_TYPE_DOUBLE:
                case OM_PRIMITIVE_TYPE_FLOAT:
                case OM_PRIMITIVE_TYPE_LONG:
                case OM_PRIMITIVE_TYPE_SHORT:
                    PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) ipv;
                    if (primitivePropertyValue.getPrimitiveValue() != null) {
                        return ((PrimitivePropertyValue) ipv).getPrimitiveValue().toString();
                    } else {
                        return "null";
                    }
                case OM_PRIMITIVE_TYPE_UNKNOWN:
                default:
                    return "";
            }
        } else {
            if (ipv instanceof EnumPropertyValue) {
                return ((EnumPropertyValue) ipv).getSymbolicName();
            } else {
                return "";
            }
        }
    }

}