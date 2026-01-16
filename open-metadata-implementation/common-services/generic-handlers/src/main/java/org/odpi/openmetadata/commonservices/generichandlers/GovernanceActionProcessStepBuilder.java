/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GovernanceActionProcessStepBuilder creates the parts for an entity that represents a governance action process step.
 */
public class GovernanceActionProcessStepBuilder extends ReferenceableBuilder
{
    private final int                 domainIdentifier;
    private final String              displayName;
    private final String              description;
    private final boolean             ignoreMultipleTriggers;
    private final int                 waitTime;

    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     * @param waitTime minimum number of minutes to wait before running the governance action
     * @param additionalProperties additional properties for a governance action
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceActionProcessStepBuilder(String               qualifiedName,
                                       int                  domainIdentifier,
                                       String               displayName,
                                       String               description,
                                       boolean              ignoreMultipleTriggers,
                                       int                  waitTime,
                                       Map<String, String>  additionalProperties,
                                       OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName,
                                       String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeGUID,
              OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.domainIdentifier       = domainIdentifier;
        this.displayName            = displayName;
        this.description            = description;
        this.ignoreMultipleTriggers = ignoreMultipleTriggers;
        this.waitTime               = waitTime;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                               domainIdentifier,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS.name,
                                                                   ignoreMultipleTriggers,
                                                                   methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.WAIT_TIME.name,
                                                               waitTime,
                                                               methodName);

        return properties;
    }
}
