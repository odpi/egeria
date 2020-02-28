/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
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
        lineageEntity.setGuid(entityDetail.getGUID());
        lineageEntity.setCreatedBy(entityDetail.getCreatedBy());
        lineageEntity.setCreateTime(entityDetail.getCreateTime());
        lineageEntity.setTypeDefName(entityDetail.getType().getTypeDefName());
        lineageEntity.setUpdatedBy(entityDetail.getUpdatedBy());
        lineageEntity.setUpdateTime(entityDetail.getUpdateTime());
        lineageEntity.setVersion(entityDetail.getVersion());
        lineageEntity.setProperties(getMapProperties(entityDetail.getProperties()));
        return lineageEntity;
    }

    /**
     * Gets map properties.
     *
     * @param properties the properties
     * @return the map properties
     */
    public Map<String, String> getMapProperties(InstanceProperties properties) {
        Map<String, String> attributes = new HashMap<>();

        if (properties == null)
            return attributes;

        Map<String, InstancePropertyValue> instanceProperties = properties.getInstanceProperties();

        if (instanceProperties == null)
            return attributes;

        for (Map.Entry<String, InstancePropertyValue> property : instanceProperties.entrySet()) {

            if (property.getValue() == null)
                continue;

            String propertyValue = getStringForPropertyValue(property.getValue());
            if (propertyValue.equals(""))
                return attributes;

            if (property.getKey().equals("name"))
                attributes.put("displayName", propertyValue);
            else
                attributes.put(property.getKey(), propertyValue);
        }
        return attributes;
    }

    private String getStringForPropertyValue(InstancePropertyValue ipv) {

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