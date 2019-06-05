/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.mappers;

/**
 * DiscoveryServicePropertiesMapper provides property name mapping for Discovery Services.
 *
 * Specifically, a single discovery service is represented as an OpenDiscoveryService entity linked to a Connection
 * entity using an ConnectionToAsset relationship.
 */
public class DiscoveryServicePropertiesMapper
{
    public static final String DISCOVERY_SERVICE_TYPE_GUID               = "2f278dfc-4640-4714-b34b-303e84e4fc40";
    public static final String DISCOVERY_SERVICE_TYPE_NAME               = "OpenDiscoveryService";

    public static final String CONNECTION_TO_ASSET_TYPE_GUID             = "e777d660-8dbe-453e-8b83-903771f054c0";
    public static final String CONNECTION_TO_ASSET_TYPE_NAME             = "ConnectionToAsset";
    /* End1 = Connection; End 2 = Asset */
}
