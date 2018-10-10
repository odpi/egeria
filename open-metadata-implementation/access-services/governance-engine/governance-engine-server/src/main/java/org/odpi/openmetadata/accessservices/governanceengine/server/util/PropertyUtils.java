/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.util;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

public class PropertyUtils {

        public static String getStringForPropertyValue(InstancePropertyValue ipv) {

            // First deal with primitive types
            if (ipv instanceof PrimitivePropertyValue) {
                PrimitiveDefCategory primtype =
                        ((PrimitivePropertyValue) ipv).getPrimitiveDefCategory();
                switch (primtype) {
                    // case may be unnecessary since all of these types we expect .toString() to work 'sensibly' but leaving
                    // for future decoding options
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
                        // For these primitive types we will just use tostring
                        return ((PrimitivePropertyValue)ipv).getPrimitiveValue().toString();
                    case OM_PRIMITIVE_TYPE_UNKNOWN:
                    default:
                        // We don't know how to convert to string, so will ignore / leave as null
                        return "";
                }

            } else
            {
                if (ipv instanceof EnumPropertyValue) {
                    return ((EnumPropertyValue) ipv).getSymbolicName();
                }
                else
                {
                    // We WILL NOT decode ArrayPropertyValue, InstancePropertyValueMock, MapPropertyValue,
                    // StructPropertyValue
                    return "";
                }
            }

        }


}
