/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectionMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * ConnectionConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail and Relationship objects into a Connection bean.
 */
public class ConnectionConverter extends ReferenceableConverter
{
    private List<EmbeddedConnection> embeddedConnections;
    private Endpoint                 endpoint;
    private ConnectorType            connectorType;



    /**
     * Constructor captures the repository content needed to create the connection object.
     * This may ba a Connection or a VirtualConnection.  EmbeddedConnections determines
     * which.
     *
     * @param connectionEntity properties to convert
     * @param endpoint endpoint for the connection
     * @param connectorType connector type for the connection
     * @param embeddedConnections list of embedded connections (optional)
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public ConnectionConverter(EntityDetail             connectionEntity,
                               Endpoint                 endpoint,
                               ConnectorType            connectorType,
                               List<EmbeddedConnection> embeddedConnections,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(connectionEntity,
              repositoryHelper,
              serviceName,
              serverName);

        this.endpoint = endpoint;
        this.connectorType = connectorType;
        this.embeddedConnections = embeddedConnections;
    }



    /**
     * Request the bean is extracted from the repository objects.  This bean has multiple parts
     * to it.  At the root is a connection object.  It typically has a connector type and an endpoint
     * linked to it.  Optionally it has embedded connections in it.
     *
     * @return output bean
     */
    @Override
    public Connection getBean()
    {
        final String  methodName = "getBean";

        Connection  bean = null;

        if (entity != null)
        {
            if (embeddedConnections == null)
            {
                bean = new Connection();
            }
            else
            {
                VirtualConnection virtualConnection = new VirtualConnection();

                virtualConnection.setEmbeddedConnections(embeddedConnections);
                bean = virtualConnection;
            }

            super.updateBean(bean);
            bean.setEndpoint(endpoint);
            bean.setConnectorType(connectorType);

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
                                                                          ConnectionMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          ConnectionMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setUserId(repositoryHelper.removeStringProperty(serviceName,
                                                                     ConnectionMapper.USER_ID_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
                bean.setClearPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                            ConnectionMapper.CLEAR_PASSWORD_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setEncryptedPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                                ConnectionMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));

                bean.setSecuredProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                       ConnectionMapper.SECURED_PROPERTIES_PROPERTY_NAME,
                                                                                       instanceProperties,
                                                                                       methodName));
                bean.setConfigurationProperties(repositoryHelper.removeMapFromProperty(serviceName,
                                                                                       ConnectionMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
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
