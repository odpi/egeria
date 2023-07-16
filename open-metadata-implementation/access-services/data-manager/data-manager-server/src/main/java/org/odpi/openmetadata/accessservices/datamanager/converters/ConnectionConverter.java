/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.EmbeddedConnection;
import org.odpi.openmetadata.accessservices.datamanager.properties.ConnectionProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * ConnectionConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail and Relationship objects into a Connection bean (or a VirtualConnection bean).
 */
public class ConnectionConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ConnectionConverter(OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have an properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ConnectionElement)
            {
                ConnectionElement bean = (ConnectionElement) returnBean;
                ConnectionProperties connectionProperties = new ConnectionProperties();

                if (primaryEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    connectionProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    connectionProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    connectionProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    connectionProperties.setDescription(this.removeDescription(instanceProperties));
                    connectionProperties.setSecuredProperties(this.removeSecuredProperties(instanceProperties));
                    connectionProperties.setConfigurationProperties(this.removeConfigurationProperties(instanceProperties));
                    connectionProperties.setUserId(this.removeUserId(instanceProperties));
                    connectionProperties.setClearPassword(this.removeClearPassword(instanceProperties));
                    connectionProperties.setEncryptedPassword(this.removeEncryptedPassword(instanceProperties));
                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    connectionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    connectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setConnectionProperties(connectionProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                if (relationships != null)
                {
                    List<EmbeddedConnection> embeddedConnections = new ArrayList<>();

                    for (Relationship relationship : relationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null))
                        {
                            if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME))
                            {
                                EmbeddedConnection embeddedConnection = new EmbeddedConnection();

                                InstanceProperties relationshipProperties = relationship.getProperties();

                                embeddedConnection.setPosition(this.getPosition(relationshipProperties));
                                embeddedConnection.setDisplayName(this.getDisplayName(relationshipProperties));
                                embeddedConnection.setArguments(this.getArguments(relationshipProperties));

                                embeddedConnection.setEmbeddedConnection(getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName));

                                embeddedConnections.add(embeddedConnection);
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                            {
                                bean.setConnectorType(getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName));
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                            {
                                bean.setEndpoint(getElementStub(beanClass, relationship.getEntityOneProxy(), methodName));
                            }
                        }
                    }

                    if (! embeddedConnections.isEmpty())
                    {
                        bean.setEmbeddedConnections(embeddedConnections);
                    }
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
