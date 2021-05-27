/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * UserIdentityBuilder creates the parts for an entity that represents a user identity.
 */
public class UserIdentityBuilder extends ReferenceableBuilder
{
    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the user identity
     * @param additionalProperties additional properties for a user identity
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a user identity subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    UserIdentityBuilder(String               qualifiedName,
                        Map<String, String>  additionalProperties,
                        String               typeGUID,
                        String               typeName,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the user identity
     * @param additionalProperties additional properties for a user identity
     * @param extendedProperties  properties for a user identity subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    UserIdentityBuilder(String               qualifiedName,
                        Map<String, String>  additionalProperties,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
              OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);
    }
}
