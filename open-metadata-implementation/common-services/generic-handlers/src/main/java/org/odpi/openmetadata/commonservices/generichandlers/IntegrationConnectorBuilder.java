/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * IntegrationConnectorBuilder creates the parts of a root repository entity for an IntegrationConnector entity.
 */
public class IntegrationConnectorBuilder extends ProcessBuilder
{
    private boolean usesBlockingCalls = false;


    /**
     * Creation constructor used when working with classifications
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    IntegrationConnectorBuilder(OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(OpenMetadataAPIMapper.INTEGRATION_CONNECTOR_TYPE_GUID,
              OpenMetadataAPIMapper.INTEGRATION_CONNECTOR_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }



    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param versionIdentifier new value for the versionIdentifier
     * @param technicalDescription new description for the process
     * @param formula description of the logic that is implemented by this process
     * @param formulaType description of the language used in the formula
     * @param implementationLanguage language used to implement this process (DeployedSoftwareComponent and subtypes only)
     * @param usesBlockingCalls integration connector needs own thread to execute because it uses blocking calls
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    IntegrationConnectorBuilder(String               qualifiedName,
                                String               technicalName,
                                String               versionIdentifier,
                                String               technicalDescription,
                                String               formula,
                                String               formulaType,
                                String               implementationLanguage,
                                boolean              usesBlockingCalls,
                                Map<String, String>  additionalProperties,
                                String               typeGUID,
                                String               typeName,
                                Map<String, Object>  extendedProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              technicalName,
              versionIdentifier,
              technicalDescription,
              formula,
              formulaType,
              implementationLanguage,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.usesBlockingCalls = usesBlockingCalls;
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

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.USES_BLOCKING_CALLS_PROPERTY_NAME,
                                                                  usesBlockingCalls,
                                                                  methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getRegisteredIntegrationConnectorProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.USES_BLOCKING_CALLS_PROPERTY_NAME,
                                                                   usesBlockingCalls,
                                                                   methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param catalogTargetName name of the catalog target
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getCatalogTargetProperties(String catalogTargetName,
                                                         String methodName) throws InvalidParameterException
    {
        return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                            null,
                                                            OpenMetadataAPIMapper.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                            catalogTargetName,
                                                            methodName);

    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param catalogTargetName name of catalog target
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getRegisteredIntegrationConnectorProperties(String catalogTargetName,
                                                                          String methodName) throws InvalidParameterException
    {
        return repositoryHelper.addStringPropertyToInstance(serviceName,
                                                            null,
                                                            OpenMetadataAPIMapper.CATALOG_TARGET_NAME_PROPERTY_NAME,
                                                            catalogTargetName,
                                                            methodName);

    }
}
