/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.ConnectorType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * ConnectorTypeConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ConnectorTypeElement bean.
 */
public class ConnectorTypeConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    public ConnectorTypeConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B> beanClass, EntityDetail entity, String methodName) throws PropertyServerException {
        try {
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ConnectorType) {
                ConnectorType bean = (ConnectorType) returnBean;

                if (entity != null) {

                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    bean.setDisplayName(this.removeDisplayName(instanceProperties));
                    bean.setDescription(this.removeDescription(instanceProperties));
                    bean.setSupportedAssetTypeName(this.removeSupportedAssetTypeName(instanceProperties));
                    bean.setExpectedDataFormat(this.removeExpectedDataFormat(instanceProperties));
                    bean.setConnectorProviderClassName(this.removeConnectorProviderClassName(instanceProperties));
                    bean.setConnectorFrameworkName(this.removeConnectorFrameworkName(instanceProperties));
                    bean.setConnectorInterfaceLanguage(this.removeConnectorInterfaceLanguage(instanceProperties));
                    bean.setConnectorInterfaces(this.removeConnectorInterfaces(instanceProperties));
                    bean.setTargetTechnologySource(this.removeTargetTechnologySource(instanceProperties));
                    bean.setTargetTechnologyName(this.removeTargetTechnologyName(instanceProperties));
                    bean.setTargetTechnologyInterfaces(this.removeTargetTechnologyInterfaces(instanceProperties));
                    bean.setTargetTechnologyVersions(this.removeTargetTechnologyVersions(instanceProperties));
                    bean.setRecognizedAdditionalProperties(this.removeRecognizedAdditionalProperties(instanceProperties));
                    bean.setRecognizedSecuredProperties(this.removeRecognizedSecuredProperties(instanceProperties));
                    bean.setRecognizedConfigurationProperties(this.removeRecognizedConfigurationProperties(instanceProperties));
                    bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                }
                else {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error) {
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
    @Override
    public B getNewBean(Class<B> beanClass, EntityDetail entity, Relationship relationship, String methodName)
            throws PropertyServerException {
        return getNewBean(beanClass, entity, methodName);
    }
}
