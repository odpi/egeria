/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.mappers;

/**
 * ProcessPropertiesMapper provides property name mapping for Processes and their relationships.
 */
public class ProcessPropertiesMapper {
    public static final String PROCESS_TYPE_GUID = "d8f33bd7-afa9-4a11-a8c7-07dcec83c050";
    public static final String PROCESS_TYPE_NAME = "Process";

    public static final String PROCESS_PORT_TYPE_GUID = "fB4E00CF-37e4-88CE-4a94-233BAdB84DA2";
    public static final String PROCESS_PORT_TYPE_NAME = "ProcessPort";

    public static final String PROCESS_HIERARCHY_TYPE_NAME = "ProcessHierarchy";

    public static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";
    public static final String GUID_PROPERTY_NAME = "guid";
    public static final String FORMULA_PROPERTY_NAME = "formula";
    public static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    public static final String CONTAINMENT_TYPE = "containmentType";

    private ProcessPropertiesMapper() {
    }
}
