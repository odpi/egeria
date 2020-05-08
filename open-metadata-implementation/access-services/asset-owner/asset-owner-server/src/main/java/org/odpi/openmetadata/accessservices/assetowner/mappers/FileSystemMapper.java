/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.mappers;

/**
 * FileSystemMapper provides property name mapping for file systems.
 */
public class FileSystemMapper
{
    public static final String FILE_SYSTEM_ENTITY_TYPE_GUID             = "fe30a033-8f86-4d17-8986-e6166fa24177";
    public static final String FILE_SYSTEM_ENTITY_TYPE_NAME             = "SoftwareServerCapability";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME                = "name";                                 /* from SoftwareServerCapability entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from SoftwareServerCapability entity */
    public static final String TYPE_PROPERTY_NAME                        = "type";                                 /* from SoftwareServerCapability entity */
    public static final String VERSION_PROPERTY_NAME                     = "version";                              /* from SoftwareServerCapability entity */
    public static final String PATCH_LEVEL_PROPERTY_NAME                 = "patchLevel";                           /* from SoftwareServerCapability entity */
    public static final String SOURCE_PROPERTY_NAME                      = "source";                               /* from SoftwareServerCapability entity */

    public static final String FILE_SYSTEM_CLASSIFICATION_TYPE_GUID      = "cab5ba1d-cfd3-4fca-857d-c07711fc4157";
    public static final String FILE_SYSTEM_CLASSIFICATION_TYPE_NAME      = "FileSystem";

    public static final String FORMAT_PROPERTY_NAME                      = "format";                               /* from FileSystem classification */
    public static final String ENCRYPTION_PROPERTY_NAME                  = "encryption";                           /* from FileSystem classification */
}
