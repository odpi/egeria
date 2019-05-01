/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util;

public class Constants {

    private Constants() {
    }

    public static final String ADD_OR_UPDATE = "add_or_update";
    public static final String DEFAULT_SCHEMA_NAME = "gaian";
    public static final String RANGER_CONNECTOR = "Egeria - Ranger Connector";
    public static final String CONFIDENTIALITY = "Confidentiality";
    /**
     * In Ranger, the default owner is 0 and the global owner is 1.
     */
    public static final Short OPEN_METADATA_OWNER = 2;
    public static final String LEVEL = "level";

    public static final String TABLE = "table";
    public static final String COLUMN = "column";
    public static final String SCHEMA = "schema";

    public static final String SERVICE_TAGS_IMPORT_SERVICE_TAGS = "{0}/service/tags/importservicetags";
    public static final String SERVICE_TAGS_RESOURCE_BY_GUID = "{0}/service/tags/resource/guid/{1}";
    public static final String SERVICE_ALL_TAGS = "{0}/service/tags/tags";
    public static final String SERVICE_TAGS_TAG_RESOURCE_MAPS = "{0}/service/tags/tagresourcemaps";
    public static final String GOVERNED_ASSETS = "{0}/assets?classification={1}";
    public static final String TAG_RESOURCE_ASSOCIATION = "{0}/service/tags/tagresourcemap/{1}";
}