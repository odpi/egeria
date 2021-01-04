/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectorTypeMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * ConnectorTypeConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an ConnectorType bean.
 */
public class ConnectorTypeConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the repository content needed to create the connector type object.
     *
     * @param endpointEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public ConnectorTypeConverter(EntityDetail             endpointEntity,
                                  OMRSRepositoryHelper     repositoryHelper,
                                  String                   serviceName,
                                  String                   serverName)
    {
        super(endpointEntity,
              repositoryHelper,
              serviceName,
              serverName);
    }



    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    @Override
    public ConnectorType getBean()
    {
        final String  methodName = "getBean";

        ConnectorType  bean = null;

        if (entity != null)
        {
            bean = new ConnectorType();

            super.updateBean(bean);
            
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          ConnectorTypeMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          ConnectorTypeMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setConnectorProviderClassName(repositoryHelper.removeStringProperty(serviceName,
                                                                                         ConnectorTypeMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
                                                                                         instanceProperties,
                                                                                         methodName));
                bean.setRecognizedAdditionalProperties(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                                                  ConnectorTypeMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
                                                                                                  instanceProperties,
                                                                                                  methodName));
                bean.setRecognizedConfigurationProperties(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                                                  ConnectorTypeMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
                                                                                                  instanceProperties,
                                                                                                  methodName));
                bean.setRecognizedSecuredProperties(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                                                  ConnectorTypeMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
                                                                                                  instanceProperties,
                                                                                                  methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }
}
