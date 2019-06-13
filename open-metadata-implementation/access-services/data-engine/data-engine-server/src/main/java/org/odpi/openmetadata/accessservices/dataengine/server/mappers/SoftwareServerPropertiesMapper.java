/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.mappers;

/**
 * SoftwareServerPropertiesMapper provides property name mapping for Software Servers.
 *
 * Specifically, a single engine is represented as an SoftwareServerCapabilityRequestBody entity.
 */
public class SoftwareServerPropertiesMapper {
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    public static final String SOFTWARE_SERVER_CAPABILITY_TYPE_NAME = "SoftwareServerCapability";

    public static final String DISPLAY_NAME_PROPERTY_NAME = "name";                                 /* from SoftwareServerCapabilityRequestBody entity */
    public static final String DESCRIPTION_PROPERTY_NAME = "description";                          /* from SoftwareServerCapabilityRequestBody entity */
    public static final String TYPE_DESCRIPTION_PROPERTY_NAME = "type";                                 /* from SoftwareServerCapabilityRequestBody entity */
    public static final String VERSION_PROPERTY_NAME = "version";                              /* from SoftwareServerCapabilityRequestBody entity */
    public static final String PATCH_LEVEL_PROPERTY_NAME = "patchLevel";                           /* from SoftwareServerCapabilityRequestBody entity */
    public static final String SOURCE_PROPERTY_NAME = "source";                               /* from SoftwareServerCapabilityRequestBody entity */
    public static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";

}