/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;


/**
 * PortBuilder is able to build the properties for a Port entity.
 */
public class PortBuilder extends ReferenceableBuilder
{
    private final String displayName;
    private final int    portType;


    /**
     * Constructor used for constructing search arguments.
     *
     * @param qualifiedName unique name
     * @param displayName short name of the port
     * @param portType direction of data flow
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier of the specific type for the port (should always be set)
     * @param typeName unique name of the specific type for the port (should always be set)
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public PortBuilder(String               qualifiedName,
                       String               displayName,
                       int                  portType,
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

        this.displayName = displayName;
        this.portType = portType;
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
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);


        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataType.PORT_TYPE_PROPERTY_NAME,
                                                                    OpenMetadataType.PORT_TYPE_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.PORT_TYPE_ENUM_TYPE_NAME,
                                                                    portType,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.PORT_TYPE_ENUM_TYPE_NAME);
        }

        return properties;
    }
}
