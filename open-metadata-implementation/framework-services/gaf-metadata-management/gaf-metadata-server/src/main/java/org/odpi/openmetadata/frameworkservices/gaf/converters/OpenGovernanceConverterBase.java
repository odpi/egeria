/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworkservices.omf.converters.OpenMetadataStoreConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * OpenMetadataStoreConverter provides the generic methods for the Governance Action Framework (GAF) beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
abstract public class OpenGovernanceConverterBase<B> extends OpenMetadataStoreConverter<B>
{
    private static final Logger log = LoggerFactory.getLogger(OpenGovernanceConverterBase.class);


    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    protected OpenGovernanceConverterBase(OMRSRepositoryHelper repositoryHelper,
                                          String serviceName,
                                          String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Retrieve and delete the EngineActionStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return AssetOwnerType  enum value
     */
    protected EngineActionStatus removeActionStatus(String               propertyName,
                                                    InstanceProperties   properties)
    {
        EngineActionStatus actionStatus = this.getActionStatus(propertyName, properties);

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
     * @param properties  entity properties
     * @return ActionStatus  enum value
     */
    private EngineActionStatus getActionStatus(String               propertyName,
                                               InstanceProperties   properties)
    {
        EngineActionStatus engineActionStatus = EngineActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    engineActionStatus = switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> EngineActionStatus.REQUESTED;
                        case 1 -> EngineActionStatus.APPROVED;
                        case 2 -> EngineActionStatus.WAITING;
                        case 3 -> EngineActionStatus.ACTIVATING;
                        case 4 -> EngineActionStatus.IN_PROGRESS;
                        case 10 -> EngineActionStatus.ACTIONED;
                        case 11 -> EngineActionStatus.INVALID;
                        case 12 -> EngineActionStatus.IGNORED;
                        case 13 -> EngineActionStatus.FAILED;
                        case 14 -> EngineActionStatus.CANCELLED;
                        default -> engineActionStatus;
                    };
                }
            }
        }

        return engineActionStatus;
    }
}
