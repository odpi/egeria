/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.utils;

import org.odpi.openmetadata.accessservices.informationview.events.ConnectionDetails;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

public class EntityPropertiesUtils {


    public static String getStringValueForProperty(InstanceProperties instanceProperties, String name) {
        if (instanceProperties.getPropertyValue(name) instanceof PrimitivePropertyValue && ((PrimitivePropertyValue) instanceProperties.getPropertyValue(name)).getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING) {
            return (String) ((PrimitivePropertyValue) instanceProperties.getPropertyValue(name)).getPrimitiveValue();
        }
        return "";
    }

    public static Integer getIntegerValueForProperty(InstanceProperties properties, String name) {
        if (properties.getPropertyValue(name) instanceof PrimitivePropertyValue && ((PrimitivePropertyValue) properties.getPropertyValue(name)).getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT) {
            return (Integer) ((PrimitivePropertyValue) properties.getPropertyValue(name)).getPrimitiveValue();
        }
        return 0;
    }

    public static MapPropertyValue getMapValueForProperty(InstanceProperties instanceProperties, String name) {
        if (instanceProperties.getPropertyValue(name) instanceof MapPropertyValue) {
            return (MapPropertyValue) instanceProperties.getPropertyValue(name);
        }
        return null;
    }

    public static PrimitivePropertyValue createPrimitiveStringPropertyValue(String value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue(value);
        return propertyValue;
    }

    public static PrimitivePropertyValue createPrimitiveIntPropertyValue(Integer value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        propertyValue.setPrimitiveValue(value);
        return propertyValue;
    }
}
