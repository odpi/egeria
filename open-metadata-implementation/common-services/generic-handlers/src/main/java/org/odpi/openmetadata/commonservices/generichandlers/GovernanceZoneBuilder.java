/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GovernanceZoneBuilder creates the parts for an entity that represents a governance zone definition.
 */
public class GovernanceZoneBuilder extends ReferenceableBuilder
{
    private String       displayName;
    private String       description;
    private String       criteria;
    private String       scope;
    private int          domainIdentifier;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceZoneBuilder(String               qualifiedName,
                          String               displayName,
                          String               description,
                          String               criteria,
                          String               scope,
                          int                  domainIdentifier,
                          Map<String, String>  additionalProperties,
                          Map<String, Object>  extendedProperties,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.ZONE_TYPE_GUID,
              OpenMetadataAPIMapper.ZONE_TYPE_NAME,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (criteria != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.CRITERIA_PROPERTY_NAME,
                                                                      criteria,
                                                                      methodName);
        }

        if (scope != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SCOPE_PROPERTY_NAME,
                                                                      scope,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                                   domainIdentifier,
                                                                   methodName);

        return properties;
    }
}
