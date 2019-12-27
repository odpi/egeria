/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.mappers;

/**
 * DataEnginePropertiesMapper provides property name mapping for external data engines. Specifically, a single data
 * engine is represented as an DataEngineRegistrationRequestBody entity.
 */
public class DataEnginePropertiesMapper {
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_NAME = "SoftwareServerCapability";

    public static final String DISPLAY_NAME_PROPERTY_NAME = "name";
    public static final String DESCRIPTION_PROPERTY_NAME = "description";
    public static final String TYPE_DESCRIPTION_PROPERTY_NAME = "type";
    public static final String VERSION_PROPERTY_NAME = "version";
    public static final String PATCH_LEVEL_PROPERTY_NAME = "patchLevel";
    public static final String SOURCE_PROPERTY_NAME = "source";
    public static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";

    private DataEnginePropertiesMapper() {
    }
}