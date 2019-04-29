/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;

import org.odpi.openmetadata.accessservices.discoveryengine.handlers.ErrorHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ReferenceableBuilder creates Open Metadata Repository Services (OMRS) objects based on the
 * bean properties supplied in the constructor.
 */
public class ReferenceableBuilder
{
    protected  OMRSRepositoryHelper repositoryHelper;
    protected  String               serviceName;

    private String               qualifiedName;
    private Map<String, String>  additionalProperties = null;
    private Map<String, Object>  extendedProperties = null;

    private ErrorHandler errorHandler;


    /**
     * Constructor for simple creates.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ReferenceableBuilder(String               qualifiedName,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        this.qualifiedName = qualifiedName;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;

        this.errorHandler = new ErrorHandler(serviceName, serverName);
    }


    /**
     * Constructor for updates.
     *
     * @param qualifiedName unique name
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ReferenceableBuilder(String               qualifiedName,
                         Map<String, String>  additionalProperties,
                         Map<String, Object>  extendedProperties,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        this(qualifiedName, repositoryHelper, serviceName, serverName);
        this.additionalProperties = additionalProperties;
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        if (extendedProperties != null)
        {
            try
            {
                properties = repositoryHelper.addPropertyMapToInstance(serviceName,
                                                                       null,
                                                                       extendedProperties,
                                                                       methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
            {
                final String  propertyName = "extendedProperties";

                errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
            }
        }

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         null,
                                                                         ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return getNameInstanceProperties(methodName);
    }
}
