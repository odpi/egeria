/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.AssetBuilder;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryServiceBuilder creates repository entities and relationships from properties for a discovery service.
 * Specifically, a single discovery service is represented as a DiscoveryService entity linked to a Connection
 * entity using an ConnectionToAsset relationship.
 */
public class DiscoveryServiceBuilder extends AssetBuilder
{
    public DiscoveryServiceBuilder(String               qualifiedName,
                                   String               displayName,
                                   String               description,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(qualifiedName, displayName, description, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param owner name of the owner
     * @param ownerType type of owner
     * @param zoneMembership list of zones that this discovery service belongs to.
     * @param latestChange description of the last change to the entity.
     * @param origin properties that describe the origin of the asset.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryServiceBuilder(String               qualifiedName,
                                   String               displayName,
                                   String               description,
                                   String               owner,
                                   OwnerType            ownerType,
                                   List<String>         zoneMembership,
                                   Map<String, String>  origin,
                                   String               latestChange,
                                   Map<String, String>  additionalProperties,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(qualifiedName,
              displayName,
              description,
              owner,
              ownerType,
              zoneMembership,
              origin,
              latestChange,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);
    }
}
