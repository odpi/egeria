/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;

/**
 * ITInfrastructureBuilder provides the builder functions for classifications and relationships.
 */
public class ITInfrastructureBuilder extends OpenMetadataAPIGenericBuilder
{
    /**
     * Create constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ITInfrastructureBuilder(OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(OpenMetadataType.IT_INFRASTRUCTURE.typeGUID,
              OpenMetadataType.IT_INFRASTRUCTURE.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException
    {
        return super.getInstanceProperties(methodName);
    }



    /**
     * Return the bean properties describing the link between a software server and a software capability in an InstanceProperties object.
     *
     * @param deploymentTime date/time that the capability was deployed
     * @param deployer user who issued the deploy command
     * @param serverCapabilityStatus operational status of the capability
     * @param methodName name of the calling method
     * @throws InvalidParameterException there is a problem with the properties
     * @return InstanceProperties object
     */
    InstanceProperties getSoftwareServerSupportedCapabilitiesProperties(Date   deploymentTime,
                                                                        String deployer,
                                                                        int    serverCapabilityStatus,
                                                                        String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                null,
                                                                OpenMetadataProperty.DEPLOYMENT_TIME.name,
                                                                deploymentTime,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DEPLOYER.name,
                                                                  deployer,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                    OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.OPERATIONAL_STATUS_ENUM_TYPE_NAME,
                                                                    serverCapabilityStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.OPERATIONAL_STATUS.name);
        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the link between a software server and a software capability in an InstanceProperties object.
     *
     * @param description description of the use
     * @param useType server asset use type
     * @param methodName name of the calling method
     * @throws InvalidParameterException there is a problem with the properties
     * @return InstanceProperties object
     */
    InstanceProperties getSoftwareServerCapabilitiesAssetUseProperties(String description,
                                                                       int    useType,
                                                                       String methodName) throws InvalidParameterException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                                     description,
                                                                                     methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.USE_TYPE.name,
                                                                    OpenMetadataType.SERVER_ASSET_USE_TYPE_TYPE_GUID,
                                                                    OpenMetadataType.SERVER_ASSET_USE_TYPE_TYPE_NAME,
                                                                    useType,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.USE_TYPE.name);
        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a cloud provider in an InstanceProperties object.
     *
     * @param providerName name of the cloud provider
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCloudProviderProperties(String providerName,
                                                  String methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.PROVIDER_NAME_PROPERTY_NAME,
                                                                                     providerName,
                                                                                     methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a cloud platform in an InstanceProperties object.
     *
     * @param implementationType type of cloud platform implementation
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCloudPlatformProperties(String implementationType,
                                                  String methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     implementationType,
                                                                                     methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a Cloud Tenant in an InstanceProperties object.
     *
     * @param tenantName name of the tenant
     * @param tenantType type of tenant
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCloudTenantProperties(String tenantName,
                                                String tenantType,
                                                String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataType.TENANT_NAME_PROPERTY_NAME,
                                                                  tenantName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.TENANT_TYPE_PROPERTY_NAME,
                                                                  tenantType,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a Cloud Service in an InstanceProperties object.
     *
     * @param offeringName name of the service
     * @param serviceType type of service
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCloudServiceProperties(String offeringName,
                                                 String serviceType,
                                                 String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataType.OFFERING_NAME_PROPERTY_NAME,
                                                                  offeringName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.SERVICE_TYPE_PROPERTY_NAME,
                                                                  serviceType,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }
}
