/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ByteOrdering;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;

/**
 * OperatingPlatformBuilder creates the parts for an entity that represents an operating platform (model 0030).
 */
public class OperatingPlatformBuilder extends ReferenceableBuilder
{
    private final String name;
    private final String description;
    private final String operatingSystem;
    private final int    byteOrdering;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the operating platform - used in other configuration
     * @param name short display name for the operating platform
     * @param description description of the operating platform
     * @param operatingSystem the operating system running on this platform
     * @param byteOrdering the identifier of the endianness
     * @param additionalProperties additional properties for a operating platform
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a operating platform subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    OperatingPlatformBuilder(String               qualifiedName,
                             String               name,
                             String               description,
                             String               operatingSystem,
                             int                  byteOrdering,
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

        this.name = name;
        this.description = description;
        this.operatingSystem = operatingSystem;
        this.byteOrdering = byteOrdering;
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

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NAME.name,
                                                                  name,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.OPERATING_SYSTEM.name,
                                                                  operatingSystem,
                                                                  methodName);

        try
        {
            return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                              properties,
                                                              OpenMetadataProperty.BYTE_ORDERING.name,
                                                              ByteOrdering.getOpenTypeGUID(),
                                                              ByteOrdering.getOpenTypeName(),
                                                              byteOrdering,
                                                              methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.BYTE_ORDERING.name);
        }
    }
}
