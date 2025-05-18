/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GovernanceZoneBuilder creates the parts for an entity that represents a governance zone definition.
 */
public class GovernanceZoneBuilder extends ReferenceableBuilder
{
    private final String       zoneName;
    private final String       displayName;
    private final String       description;
    private final String       criteria;
    private final String       scope;
    private final int          domainIdentifier;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the zone entity
     * @param zoneName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this zone applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a governance zone
     * @param typeGUID unique identifier of the type for the zone
     * @param typeName unique name for the type for the zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceZoneBuilder(String               qualifiedName,
                          String               zoneName,
                          String               displayName,
                          String               description,
                          String               criteria,
                          String               scope,
                          int                  domainIdentifier,
                          Map<String, String>  additionalProperties,
                          String               typeGUID,
                          String               typeName,
                          Map<String, Object>  extendedProperties,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.zoneName = zoneName;
        this.displayName = displayName;
        this.description = description;
        this.criteria = criteria;
        this.scope = scope;
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ZONE_NAME.name,
                                                                  zoneName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.CRITERIA.name,
                                                                  criteria,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SCOPE.name,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                               domainIdentifier,
                                                               methodName);

        return properties;
    }
}
