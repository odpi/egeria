/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ConnectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.VirtualConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * ConnectionConverter provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean that inherits from ConnectionElement.
 */
public class ConnectionConverter<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ConnectionConverter(PropertyHelper propertyHelper,
                               String         serviceName,
                               String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Uses the type of the entity to determine the type of bean to use for the properties.
     *
     * @param beanClass element bean class
     * @param openMetadataElement element retrieved
     * @param methodName calling method
     * @return properties
     * @throws PropertyServerException problem in conversion
     */
    protected ConnectionProperties getConnectionProperties(Class<B>            beanClass,
                                                           OpenMetadataElement openMetadataElement,
                                                           String              methodName) throws PropertyServerException
    {
        if (openMetadataElement != null)
        {
            ConnectionProperties connectionProperties;

            /*
             * The initial set of values come from the entity.
             */
            ElementProperties elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

            if (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.VIRTUAL_CONNECTION.typeName))
            {
                connectionProperties = new VirtualConnectionProperties();
            }
            else
            {
                connectionProperties = new ConnectionProperties();
            }

            /*
             * These are the standard properties for an actor profile.
             */
            connectionProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            connectionProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            connectionProperties.setDisplayName(this.removeDisplayName(elementProperties));
            connectionProperties.setDescription(this.removeDescription(elementProperties));
            connectionProperties.setSecuredProperties(this.removeSecuredProperties(elementProperties));
            connectionProperties.setConfigurationProperties(this.removeConfigurationProperties(elementProperties));
            connectionProperties.setUserId(this.removeUserId(elementProperties));
            connectionProperties.setClearPassword(this.removeClearPassword(elementProperties));
            connectionProperties.setEncryptedPassword(this.removeEncryptedPassword(elementProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            connectionProperties.setTypeName(openMetadataElement.getType().getTypeName());
            connectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return connectionProperties;
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ConnectionElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));
                bean.setProperties(getConnectionProperties(beanClass, openMetadataElement, methodName));
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
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof ConnectionElement bean)
        {
            if (relationships != null)
            {
                List<EmbeddedConnection> embeddedConnections = new ArrayList<>();

                for (RelatedMetadataElement relationship : relationships)
                {
                    if ((relationship != null) && (relationship.getType() != null))
                    {
                        if (propertyHelper.isTypeOf(relationship, OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
                        {
                            EmbeddedConnection embeddedConnection = new EmbeddedConnection();

                            ElementProperties relationshipProperties = relationship.getRelationshipProperties();

                            embeddedConnection.setPosition(this.getPosition(relationshipProperties));
                            embeddedConnection.setDisplayName(this.getDisplayName(relationshipProperties));
                            embeddedConnection.setArguments(this.getArguments(relationshipProperties));

                            embeddedConnection.setEmbeddedConnection(getElementStub(beanClass, relationship.getElement(), methodName));

                            embeddedConnections.add(embeddedConnection);
                        }
                        else if (propertyHelper.isTypeOf(relationship, OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName))
                        {
                            bean.setConnectorType(getElementStub(beanClass, relationship.getElement(), methodName));
                        }
                        else if ((bean.getEndpoint() == null) && (propertyHelper.isTypeOf(relationship, OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName)))
                        {
                            bean.setEndpoint(getElementStub(beanClass, relationship.getElement(), methodName));
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

}
