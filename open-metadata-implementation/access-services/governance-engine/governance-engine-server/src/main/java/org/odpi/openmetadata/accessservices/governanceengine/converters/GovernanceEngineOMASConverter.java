/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.commonservices.gaf.converters.OpenMetadataStoreConverter;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementControlHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.EnumTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.StructTypePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineOMASConverter provides the generic methods for the Governance Engine beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
public class GovernanceEngineOMASConverter<B> extends OpenMetadataStoreConverter<B>
{
    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineOMASConverter.class);

    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public GovernanceEngineOMASConverter(OMRSRepositoryHelper repositoryHelper,
                                         String serviceName,
                                         String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Retrieve and delete the GovernanceActionStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    GovernanceActionStatus removeActionStatus(String               propertyName,
                                              InstanceProperties   properties)
    {
        GovernanceActionStatus ownerCategory = this.getActionStatus(propertyName, properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(propertyName);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerCategory;
    }


    /**
     * Retrieve the ActionStatus enum property from the instance properties of a Governance Action.
     *
     * @param propertyName name ot property to extract the enum from
     * @param properties  entity properties
     * @return ActionStatus  enum value
     */
    private GovernanceActionStatus getActionStatus(String               propertyName,
                                                   InstanceProperties   properties)
    {
        GovernanceActionStatus governanceActionStatus = GovernanceActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            governanceActionStatus = GovernanceActionStatus.REQUESTED;
                            break;

                        case 1:
                            governanceActionStatus = GovernanceActionStatus.APPROVED;
                            break;

                        case 2:
                            governanceActionStatus = GovernanceActionStatus.WAITING;
                            break;

                        case 3:
                            governanceActionStatus = GovernanceActionStatus.ACTIVATING;
                            break;

                        case 4:
                            governanceActionStatus = GovernanceActionStatus.IN_PROGRESS;
                            break;

                        case 10:
                            governanceActionStatus = GovernanceActionStatus.ACTIONED;
                            break;

                        case 11:
                            governanceActionStatus = GovernanceActionStatus.INVALID;
                            break;

                        case 12:
                            governanceActionStatus = GovernanceActionStatus.IGNORED;
                            break;

                        case 13:
                            governanceActionStatus = GovernanceActionStatus.FAILED;
                            break;
                    }
                }
            }
        }

        return governanceActionStatus;
    }



    /**
     * Build an open metadata element bean from a matching entity.
     *
     * @param entityGUID unique identifier of desired entity
     * @param entities list of retrieved entities
     * @return new bean or null if guid is not in list
     */
    OpenMetadataElement getOpenMetadataElement(String              entityGUID,
                                               List<EntityDetail>  entities)
    {
        OpenMetadataElement metadataElement = new OpenMetadataElement();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entityGUID.equals(entity.getGUID()))
                    {
                        this.fillOpenMetadataElement(metadataElement, entity);

                        return metadataElement;
                    }
                }
            }
        }

        /*
         * The entity was not retrieved - however, returning the guid allows the caller to retrieve the element separately.
         */
        metadataElement.setElementGUID(entityGUID);

        return metadataElement;
    }
}
