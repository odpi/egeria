/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworkservices.gaf.converters.OpenMetadataStoreConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * GovernanceEngineOMASConverter provides the generic methods for the Governance Engine beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
public class GovernanceEngineOMASConverter<B> extends OpenMetadataStoreConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       name of this server
     */
    public GovernanceEngineOMASConverter(OMRSRepositoryHelper repositoryHelper,
                                         String serviceName,
                                         String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Retrieve and delete the GovernanceActionStatus enum property from the instance properties of an entity
     *
     * @param properties entity properties
     *
     * @return AssetOwnerType  enum value
     */
    @SuppressWarnings(value = "deprecation")
    protected GovernanceActionStatus removeGovernanceActionStatus(String propertyName,
                                                                  InstanceProperties properties)
    {
        GovernanceActionStatus actionStatus = this.getGovernanceActionStatus(propertyName, properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(propertyName);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return actionStatus;
    }


    /**
     * Retrieve the ActionStatus enum property from the instance properties of a Governance Action.
     *
     * @param propertyName name ot property to extract the enum from
     * @param properties   entity properties
     *
     * @return ActionStatus  enum value
     */
    @SuppressWarnings(value = "deprecation")
    private GovernanceActionStatus getGovernanceActionStatus(String propertyName,
                                                             InstanceProperties properties)
    {
        GovernanceActionStatus governanceActionStatus = GovernanceActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> governanceActionStatus = GovernanceActionStatus.REQUESTED;
                        case 1 -> governanceActionStatus = GovernanceActionStatus.APPROVED;
                        case 2 -> governanceActionStatus = GovernanceActionStatus.WAITING;
                        case 3 -> governanceActionStatus = GovernanceActionStatus.ACTIVATING;
                        case 4 -> governanceActionStatus = GovernanceActionStatus.IN_PROGRESS;
                        case 10 -> governanceActionStatus = GovernanceActionStatus.ACTIONED;
                        case 11 -> governanceActionStatus = GovernanceActionStatus.INVALID;
                        case 12 -> governanceActionStatus = GovernanceActionStatus.IGNORED;
                        case 13 -> governanceActionStatus = GovernanceActionStatus.FAILED;
                    }
                }
            }
        }

        return governanceActionStatus;
    }
}
