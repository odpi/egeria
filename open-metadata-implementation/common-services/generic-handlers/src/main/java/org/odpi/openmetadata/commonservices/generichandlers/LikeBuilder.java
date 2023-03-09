/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * LikeBuilder is able to build the properties for a Like entity.
 */
public class LikeBuilder extends OpenMetadataAPIGenericBuilder
{
    private final boolean    isPublic;

    /**
     * Constructor.
     *
     * @param isPublic should this feedback be shareable?
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public   LikeBuilder(boolean              isPublic,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(OpenMetadataAPIMapper.LIKE_TYPE_GUID,
              OpenMetadataAPIMapper.LIKE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.isPublic = isPublic;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getRelationshipInstanceProperties(String  methodName)
    {
        return repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                             null,
                                                             OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                             isPublic,
                                                             methodName);
    }
}
