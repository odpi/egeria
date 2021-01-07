/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.utils;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EntityPropertiesUtils {

    private static final Logger log = LoggerFactory.getLogger(EntityPropertiesUtils.class);


    public static PrimitivePropertyValue createPrimitiveStringPropertyValue(String value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue(value);
        propertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
        propertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        return propertyValue;
    }

    public static PrimitivePropertyValue createPrimitiveIntPropertyValue(Integer value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        propertyValue.setPrimitiveValue(value);
        propertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getGUID());
        propertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getName());
        return propertyValue;
    }

    public static PrimitivePropertyValue createPrimitiveDatePropertyValue(Long timestamp) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        propertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getGUID());
        propertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getName());
        propertyValue.setPrimitiveValue(timestamp);
        return propertyValue;
    }

    public static boolean matchExactlyInstanceProperties(InstanceProperties properties1, InstanceProperties properties2) {
        //TODO change logic for this as it is not complete
        if (properties1.getInstanceProperties().size() != properties2.getInstanceProperties().size()) {
            return false;
        }
        for (Map.Entry<String, InstancePropertyValue> property : properties2.getInstanceProperties().entrySet()) {

            if (!matchExactlyPropertiesValues(property.getValue(), properties1.getPropertyValue(property.getKey()))) {
                log.info("ElementProperties don't match");
                return false;
            }
        }
        log.info("ElementProperties match");
        return true;
    }


    public static boolean matchExactlyPropertiesValues(InstancePropertyValue value1, InstancePropertyValue value2) {

        if (value1 == null && value2 == null)
            return true;
        if (value1 == value2)
            return true;
        if(value1 == null || value2 == null){
            return false;
        }
        if (value1 instanceof PrimitivePropertyValue && value2 instanceof PrimitivePropertyValue) {
            if (((PrimitivePropertyValue) value1).getPrimitiveDefCategory() == ((PrimitivePropertyValue) value2).getPrimitiveDefCategory()) {
                Object primitiveValue1 = ((PrimitivePropertyValue) value1).getPrimitiveValue();
                Object primitiveValue2 = ((PrimitivePropertyValue) value2).getPrimitiveValue();

                if(primitiveValue1 == null && primitiveValue2 == null){
                    return true;
                }
                if(primitiveValue1 == null && primitiveValue2 != null){
                    return false;
                }

                return primitiveValue1.equals(primitiveValue2);


            }
        }
        return false;
    }


}
