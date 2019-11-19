/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.mappers.GovernanceZoneMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
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


    /**
     * Minimal constructor used for searching
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GovernanceZoneBuilder(String               qualifiedName,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = null;
        this.description = null;
        this.criteria = null;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param criteria new value for the criteria.
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GovernanceZoneBuilder(String               qualifiedName,
                                 String               displayName,
                                 String               description,
                                 String               criteria,
                                 Map<String, String>  additionalProperties,
                                 Map<String, Object>  extendedProperties,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(qualifiedName, additionalProperties, extendedProperties, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
        this.criteria = criteria;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GovernanceZoneMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GovernanceZoneMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (criteria != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GovernanceZoneMapper.CRITERIA_PROPERTY_NAME,
                                                                      criteria,
                                                                      methodName);
        }

        return properties;
    }


}
