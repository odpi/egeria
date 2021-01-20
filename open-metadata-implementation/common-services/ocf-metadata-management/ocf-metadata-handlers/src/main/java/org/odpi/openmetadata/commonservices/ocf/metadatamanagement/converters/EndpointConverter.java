/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.EndpointMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * EndpointConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Endpoint bean.
 */
public class EndpointConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param endpointEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public EndpointConverter(EntityDetail             endpointEntity,
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
    public Endpoint getBean()
    {
        final String  methodName = "getBean";

        Endpoint  bean = null;

        if (entity != null)
        {
            bean = new Endpoint();

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
                                                                          EndpointMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          EndpointMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setAddress(repositoryHelper.removeStringProperty(serviceName,
                                                                     EndpointMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
                bean.setProtocol(repositoryHelper.removeStringProperty(serviceName,
                                                                            EndpointMapper.PROTOCOL_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setEncryptionMethod(repositoryHelper.removeStringProperty(serviceName,
                                                                                EndpointMapper.ENCRYPTION_METHOD_PROPERTY_NAME,
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
