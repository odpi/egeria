/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * GovernanceActionBuilder creates the parts for an entity that represents a governance action.
 */
public class GovernanceActionTypeBuilder extends ReferenceableBuilder
{
    private final int          domainIdentifier;
    private final String       displayName;
    private final String       description;
    private final List<String> supportedGuards;
    private final boolean      ignoreMultipleTriggers;
    private final int          waitTime;

    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param supportedGuards list of guards that triggered this governance action
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param additionalProperties additional properties for a governance action
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceActionTypeBuilder(String               qualifiedName,
                                int                  domainIdentifier,
                                String               displayName,
                                String               description,
                                List<String>         supportedGuards,
                                boolean              ignoreMultipleTriggers,
                                int                  waitTime,
                                Map<String, String>  additionalProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
              OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.domainIdentifier = domainIdentifier;
        this.displayName = displayName;
        this.description = description;
        this.supportedGuards = supportedGuards;
        this.ignoreMultipleTriggers = ignoreMultipleTriggers;
        this.waitTime = waitTime;
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

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               domainIdentifier,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.PRODUCED_GUARDS_PROPERTY_NAME,
                                                                       supportedGuards,
                                                                       methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
                                                                   ignoreMultipleTriggers,
                                                                   methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.WAIT_TIME_PROPERTY_NAME,
                                                               waitTime,
                                                               methodName);

        return properties;
    }
}
