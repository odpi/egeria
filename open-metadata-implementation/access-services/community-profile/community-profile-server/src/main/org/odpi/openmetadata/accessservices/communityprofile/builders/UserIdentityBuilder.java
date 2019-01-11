/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.builders;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.UserIdentityMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * UserIdentityBuilder creates an UserIdentity entity from a userId
 */
public class UserIdentityBuilder
{
    private String               userId;
    private OMRSRepositoryHelper repositoryHelper;
    private String               serviceName;

    /**
     * Constructor saves the parameters.
     *
     * @param userId userId to build from
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this access service
     */
    public UserIdentityBuilder(String               userId,
                               OMRSRepositoryHelper repositoryHelper,
                               String               serviceName)
    {
        this.userId = userId;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Return the properties for a UserIdentity entity.
     *
     * @return InstanceProperties object
     */
    public InstanceProperties getEntityProperties()
    {
        final String   methodName = "getEntityProperties";

        if (userId != null)
        {
            return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                null,
                                                                UserIdentityMapper.USER_ID_PROPERTY_NAME,
                                                                userId,
                                                                methodName);
        }
        else
        {
            return null;
        }
    }
}
