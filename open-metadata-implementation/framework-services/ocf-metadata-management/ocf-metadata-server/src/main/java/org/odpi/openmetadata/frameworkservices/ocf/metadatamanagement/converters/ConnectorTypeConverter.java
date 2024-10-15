/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ConnectorTypeConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an ConnectorType bean.
 */
public class ConnectorTypeConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ConnectorTypeConverter(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ConnectorType connectorType)
            {
                this.setUpElementHeader(connectorType, entity, OpenMetadataType.CONNECTOR_TYPE.typeName, methodName);

                /*
                 * The initial set of values come from the entity.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                connectorType.setQualifiedName(this.removeQualifiedName(instanceProperties));
                connectorType.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                connectorType.setDisplayName(this.removeDisplayName(instanceProperties));
                connectorType.setDescription(this.removeDescription(instanceProperties));
                connectorType.setSupportedAssetTypeName(this.removeSupportedAssetTypeName(instanceProperties));
                connectorType.setSupportedDeployedImplementationType(this.removeSupportedDeployedImplementationType(instanceProperties));
                connectorType.setExpectedDataFormat(this.removeExpectedDataFormat(instanceProperties));
                connectorType.setConnectorProviderClassName(this.removeConnectorProviderClassName(instanceProperties));
                connectorType.setConnectorFrameworkName(this.removeConnectorFrameworkName(instanceProperties));
                connectorType.setConnectorInterfaceLanguage(this.removeConnectorInterfaceLanguage(instanceProperties));
                connectorType.setConnectorInterfaces(this.removeConnectorInterfaces(instanceProperties));
                connectorType.setTargetTechnologySource(this.removeTargetTechnologySource(instanceProperties));
                connectorType.setTargetTechnologyName(this.removeTargetTechnologyName(instanceProperties));
                connectorType.setTargetTechnologyInterfaces(this.removeTargetTechnologyInterfaces(instanceProperties));
                connectorType.setTargetTechnologyVersions(this.removeTargetTechnologyVersions(instanceProperties));
                connectorType.setRecognizedAdditionalProperties(this.removeRecognizedAdditionalProperties(instanceProperties));
                connectorType.setRecognizedSecuredProperties(this.removeRecognizedSecuredProperties(instanceProperties));
                connectorType.setRecognizedConfigurationProperties(this.removeRecognizedConfigurationProperties(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                connectorType.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, methodName);
    }
}
