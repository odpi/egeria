/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworkservices.omf.converters.OpenMetadataStoreConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * OpenMetadataStoreConverter provides the generic methods for the Open Survey Framework (OGF) beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
abstract public class OpenGovernanceConverterBase<B> extends OpenMetadataStoreConverter<B>
{
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
     * Retrieve and delete the ActivityStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return AssetOwnerType  enum value
     */
    protected ActivityStatus removeActivityStatus(String               propertyName,
                                                  InstanceProperties   properties)
    {
        ActivityStatus actionStatus = this.getActivityStatus(propertyName, properties);

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
    private ActivityStatus getActivityStatus(String               propertyName,
                                             InstanceProperties   properties)
    {
        ActivityStatus activityStatus = null;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    activityStatus = switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> ActivityStatus.REQUESTED;
                        case 1 -> ActivityStatus.APPROVED;
                        case 2 -> ActivityStatus.WAITING;
                        case 3 -> ActivityStatus.ACTIVATING;
                        case 4 -> ActivityStatus.IN_PROGRESS;
                        case 5 -> ActivityStatus.PAUSED;
                        case 6 -> ActivityStatus.FOR_INFO;
                        case 10 -> ActivityStatus.COMPLETED;
                        case 11 -> ActivityStatus.INVALID;
                        case 12 -> ActivityStatus.IGNORED;
                        case 13 -> ActivityStatus.FAILED;
                        case 14 -> ActivityStatus.CANCELLED;
                        default -> null;
                    };
                }
            }
        }

        return activityStatus;
    }
}
