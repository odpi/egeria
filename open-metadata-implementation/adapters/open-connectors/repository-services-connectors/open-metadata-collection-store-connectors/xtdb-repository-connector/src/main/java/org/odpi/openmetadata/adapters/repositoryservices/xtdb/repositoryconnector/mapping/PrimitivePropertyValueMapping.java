/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import java.util.Date;

/**
 * Maps singular PrimitivePropertyValues between persistence and objects.
 *
 * These cannot simply be serialized to JSON as that would impact the ability to search their values correctly, so we
 * must break apart the values and the types for each property:
 * <code>
 *     {
 *         ...
 *         :entityProperties/qualifiedName.json {:json "{\"class\":\"PrimitivePropertyValue\",\"instancePropertyCategory\":\"PRIMITIVE\",\"primitiveDefCategory\":\"OM_PRIMITIVE_TYPE_STRING\",\"primitiveValue\":\"A Simple Term\"}"}
 *         :entityProperties/Referenceable.qualifiedName.value "A Simple Term"
 *         ...
 *         :classifications.Confidentiality.classificationProperties/Confidentiality.level.json {:json "{\"class\":\"PrimitivePropertyValue\",\"instancePropertyCategory\":\"PRIMITIVE\",\"primitiveDefCategory\":\"OM_PRIMITIVE_TYPE_INT\",\"primitiveValue\":5}"}
 *         :classifications.Confidentiality.classificationProperties/Confidentiality.level.value 5
 *         ...
 *     }
 * </code>
 */
public class PrimitivePropertyValueMapping extends InstancePropertyValueMapping {

    /**
     * Add the provided primitive value to the XTDB document.
     * @param builder to which to add the property value
     * @param keywords of the property
     * @param value of the property
     */
    public static void addPrimitivePropertyValueToDoc(XtdbDocument.Builder builder,
                                                      PropertyKeywords keywords,
                                                      PrimitivePropertyValue value) {
        builder.put(keywords.getSearchablePath(), getPrimitiveValueForComparison(value));
    }

    /**
     * Add the provided primitive value to the XTDB map.
     * @param doc the XTDB map to which to add the property
     * @param propertyKeyword the property whose value should be set, fully-qualified with namespace and type name
     * @param value of the property
     * @return IPersistentMap of the updated XTDB doc
     */
    public static IPersistentMap addPrimitivePropertyValueToDoc(IPersistentMap doc,
                                                                Keyword propertyKeyword,
                                                                PrimitivePropertyValue value) {
        return doc.assoc(propertyKeyword, getPrimitiveValueForComparison(value));
    }

    /**
     * Convert the provided primitive property value into a XTDB comparable form.
     * @param ppv Egeria value to translate to XTDB-comparable value
     * @return Object value that XTDB can compare
     */
    public static Object getPrimitiveValueForComparison(PrimitivePropertyValue ppv) {
        PrimitiveDefCategory category = ppv.getPrimitiveDefCategory();
        Object value = null;
        if (category != null) {
            switch (category) {
                case OM_PRIMITIVE_TYPE_DATE:
                    // Dates are the only thing we need to translate, from their native Long form into XTDB's
                    // native Date form
                    Object longForm = ppv.getPrimitiveValue();
                    if (longForm instanceof Long) {
                        value = new Date((Long) longForm);
                    }
                    break;
                case OM_PRIMITIVE_TYPE_BOOLEAN:
                    // Clojure's representation of boolean is a bit tricky, in that it only recognizes the
                    // precise Boolean.FALSE value as false, not (for example) new Boolean(false).
                    // So here we explicitly encode it to those static Boolean constants accordingly
                    Object bool = ppv.getPrimitiveValue();
                    if (bool instanceof Boolean) {
                        value = (Boolean)bool ? Boolean.TRUE : Boolean.FALSE;
                    }
                    break;
                case OM_PRIMITIVE_TYPE_STRING:
                    // Note: further translation of strings into regexes is only necessary for queries, so that will be
                    // done in the XTDBQuery class directly.
                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                case OM_PRIMITIVE_TYPE_DOUBLE:
                case OM_PRIMITIVE_TYPE_SHORT:
                case OM_PRIMITIVE_TYPE_FLOAT:
                case OM_PRIMITIVE_TYPE_LONG:
                case OM_PRIMITIVE_TYPE_INT:
                case OM_PRIMITIVE_TYPE_CHAR:
                case OM_PRIMITIVE_TYPE_BYTE:
                case OM_PRIMITIVE_TYPE_UNKNOWN:
                default:
                    // For everything else, we can translate directly into a straight Java object
                    value = ppv.getPrimitiveValue();
                    break;
            }
        }
        return value;
    }

}
