/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

/**
 * Maps singular EnumPropertyValues between persistence and objects.
 *
 * These cannot simply be serialized to JSON as that would impact the ability to search their values correctly, so we
 * will serde and search based strictly on their ordinal values:
 * <code>
 *     {
 *         ...
 *         :instanceProvenanceType 1
 *         :currentStatus 15
 *         ...
 *     }
 * </code>
 */
public class EnumPropertyValueMapping extends InstancePropertyValueMapping {

    /**
     * Add the provided enum value to the XTDB document.
     * @param builder to which to add the property value
     * @param keywords of the property
     * @param value of the property
     */
    public static void addEnumPropertyValueToDoc(XtdbDocument.Builder builder,
                                                 PropertyKeywords keywords,
                                                 EnumPropertyValue value) {
        builder.put(keywords.getSearchablePath(), getEnumPropertyValueForComparison(value));
    }

    /**
     * Add the provided enum value to the XTDB map.
     * @param doc the XTDB map to which to add the property
     * @param propertyKeyword the property whose value should be set, fully-qualified with namespace and type name
     * @param value of the property
     * @return IPersistentMap of the updated XTDB doc
     */
    public static IPersistentMap addEnumPropertyValueToDoc(IPersistentMap doc,
                                                           Keyword propertyKeyword,
                                                           EnumPropertyValue value) {
        return doc.assoc(propertyKeyword, getEnumPropertyValueForComparison(value));
    }

    /**
     * Convert the provided enumeration property value into a XTDB comparable form.
     * @param epv Egeria value to translate to XTDB-comparable value
     * @return Integer value that XTDB can compare
     */
    public static Integer getEnumPropertyValueForComparison(EnumPropertyValue epv) {
        return epv == null ? null : epv.getOrdinal();
    }

    /**
     * Convert the provided ordinal into its InstanceProvenanceType.
     * @param xtdbConnector connectivity to the repository
     * @param ordinal to convert
     * @return InstanceProvenanceType
     */
    public static InstanceProvenanceType getInstanceProvenanceTypeFromOrdinal(XTDBOMRSRepositoryConnector xtdbConnector, Integer ordinal) {
        final String methodName = "getInstanceProvenanceTypeFromOrdinal";
        if (ordinal != null) {
            for (InstanceProvenanceType b : InstanceProvenanceType.values()) {
                if (b.getOrdinal() == ordinal) {
                    return b;
                }
            }
            xtdbConnector.logProblem(EnumPropertyValueMapping.class.getName(),
                                     methodName,
                                     XTDBAuditCode.NON_EXISTENT_ENUM,
                                     null,
                                     "InstanceProvenanceType",
                                     ordinal.toString());
        }
        return null;
    }

    /**
     * Convert the provided ordinal into its InstanceProvenanceType.
     * @param ordinal to convert
     * @return InstanceProvenanceType
     * @throws InvalidParameterException if there is no such enumeration
     */
    public static InstanceProvenanceType getInstanceProvenanceTypeFromOrdinal(Integer ordinal) throws InvalidParameterException {
        final String methodName = "getInstanceProvenanceTypeFromOrdinal";
        if (ordinal != null) {
            for (InstanceProvenanceType b : InstanceProvenanceType.values()) {
                if (b.getOrdinal() == ordinal) {
                    return b;
                }
            }
            throw new InvalidParameterException(XTDBErrorCode.UNKNOWN_ENUMERATED_VALUE.getMessageDefinition(
                    ordinal.toString(), "InstanceProvenanceType"), EnumPropertyValueMapping.class.getName(), methodName, "ordinal");
        }
        return null;
    }

    /**
     * Convert the provided InstanceProvenanceType into its symbolic name.
     * @param ipt to convert
     * @return Integer
     */
    public static Integer getOrdinalForInstanceProvenanceType(InstanceProvenanceType ipt) {
        return ipt == null ? null : ipt.getOrdinal();
    }

    /**
     * Convert the provided ordinal into its InstanceStatus.
     * @param xtdbConnector connectivity to the repository
     * @param ordinal to convert
     * @return InstanceStatus
     */
    public static InstanceStatus getInstanceStatusFromOrdinal(XTDBOMRSRepositoryConnector xtdbConnector, Integer ordinal) {
        final String methodName = "getInstanceStatusFromOrdinal";
        if (ordinal != null) {
            for (InstanceStatus b : InstanceStatus.values()) {
                if (b.getOrdinal() == ordinal) {
                    return b;
                }
            }
            xtdbConnector.logProblem(EnumPropertyValueMapping.class.getName(),
                                     methodName,
                                     XTDBAuditCode.NON_EXISTENT_ENUM,
                                     null,
                                     "InstanceStatus",
                                     ordinal.toString());
        }
        return null;
    }

    /**
     * Convert the provided ordinal into its InstanceStatus.
     * @param ordinal to convert
     * @return InstanceStatus
     * @throws InvalidParameterException if there is no such enumeration
     */
    public static InstanceStatus getInstanceStatusFromOrdinal(Integer ordinal) throws InvalidParameterException {
        final String methodName = "getInstanceStatusFromOrdinal";
        if (ordinal != null) {
            for (InstanceStatus b : InstanceStatus.values()) {
                if (b.getOrdinal() == ordinal) {
                    return b;
                }
            }
            throw new InvalidParameterException(XTDBErrorCode.UNKNOWN_ENUMERATED_VALUE.getMessageDefinition(
                    ordinal.toString(), "InstanceStatus"), EnumPropertyValueMapping.class.getName(), methodName, "ordinal");
        }
        return null;
    }

    /**
     * Convert the provided InstanceStatus into its ordinal.
     * @param is to convert
     * @return Integer
     */
    public static Integer getOrdinalForInstanceStatus(InstanceStatus is) {
        return is == null ? null : is.getOrdinal();
    }

}
