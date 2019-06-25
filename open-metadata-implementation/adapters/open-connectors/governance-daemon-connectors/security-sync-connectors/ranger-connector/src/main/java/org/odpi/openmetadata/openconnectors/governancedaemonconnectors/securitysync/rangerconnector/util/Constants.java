/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util;

public class Constants {

    public static final String DEFAULT_SCHEMA_NAME = "gaian";
    public static final String RANGER_CONNECTOR = "Egeria - Ranger Connector";
    public static final String SECURITY_TAGS = "SecurityTags";
    /**
     * In Ranger, the default owner is 0 and the global owner is 1.
     */
    public static final Short OPEN_METADATA_OWNER = 2;
    public static final String TABLE = "table";
    public static final String COLUMN = "column";
    public static final String SCHEMA = "schema";
    public static final String NAME = "name";
    public static final String SERVICE_TAGS_RESOURCE_BY_GUID = "{0}/service/tags/resource/guid/{1}";
    public static final String SERVICE_TAGS_TAG_RESOURCE_MAPS = "{0}/service/tags/tagresourcemaps";
    public static final String GOVERNED_ASSETS = "{0}/assets?classification={1}";
    public static final String TAG_RESOURCE_ASSOCIATION = "{0}/service/tags/tagresourcemap/{1}";
    public static final String SERVICE_TAGS_MAP_TAG_GUID_RESOURCE_GUI = "{0}/service/tags/tagresourcemaps?tag-guid={1}&resource-guid={2}";
    public static final String SERVICE_TAGS_RESOURCES = "{0}/service/tags/resources/";
    public static final String SERVICE_TAGS = "{0}/service/tags/tags";
    public static final String SERVICE_TAGS_TAGDEF = "{0}/service/tags/tagdefs";
    public static final String SECURITY_SERVER_AUTHORIZATION = "securityServerAuthorization";
    private Constants() {
    }
}