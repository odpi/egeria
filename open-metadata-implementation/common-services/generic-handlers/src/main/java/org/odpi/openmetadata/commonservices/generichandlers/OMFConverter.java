/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMFConverter provides the generic methods for the OCF beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public abstract class OMFConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    protected long karmaPointPlateau = 0;

    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       name of this server
     */
    protected OMFConverter(OMRSRepositoryHelper repositoryHelper,
                           String serviceName,
                           String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor
     *
     * @param repositoryHelper  helper object to parse entity
     * @param serviceName       name of this component
     * @param serverName        local server name
     * @param karmaPointPlateau how many karma points to a plateau
     */
    public OMFConverter(OMRSRepositoryHelper repositoryHelper,
                        String serviceName,
                        String serverName,
                        int karmaPointPlateau)
    {
        this(repositoryHelper, serviceName, serverName);

        this.karmaPointPlateau = karmaPointPlateau;
    }



    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Extract the properties from the entity.
     *
     * @param elementHeader    the header for the bean
     * @param entity           entity containing the properties
     * @param expectedTypeName type that the entity must match (or it may be a subtype)
     * @param methodName       calling method
     * @throws PropertyServerException the supplied entity is not of the expected type
     */
    protected void setUpElementHeader(ElementHeader elementHeader,
                                      EntityDetail entity,
                                      String expectedTypeName,
                                      String methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            super.validateInstanceType(expectedTypeName,
                                       elementHeader.getClass().getName(),
                                       entity,
                                       methodName);

            elementHeader.setGUID(entity.getGUID());
            elementHeader.setType(this.getElementType(entity));
            elementHeader.setClassifications(this.getEntityClassifications(entity));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(entity.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(entity.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(entity.getMetadataCollectionName());
            elementOrigin.setLicense(entity.getInstanceLicense());

            elementHeader.setOrigin(elementOrigin);

            elementHeader.setVersions(this.getElementVersions(entity));
        }
        else
        {
            super.handleMissingMetadataInstance(elementHeader.getClass().getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }
    }


    /**
     * Extract the properties from the entity.
     *
     * @param elementHeader   the header for the bean
     * @param instanceHeader  header of entity
     * @param classifications classifications from the entity
     * @param methodName      calling method
     * @throws PropertyServerException the supplied entity is not of the expected type
     */
    protected void setUpElementHeader(ElementHeader elementHeader,
                                      InstanceHeader instanceHeader,
                                      List<Classification> classifications,
                                      String methodName) throws PropertyServerException
    {
        if (instanceHeader != null)
        {
            elementHeader.setGUID(instanceHeader.getGUID());
            elementHeader.setType(this.getElementType(instanceHeader));
            elementHeader.setClassifications(this.getElementClassifications(classifications));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(instanceHeader.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(instanceHeader.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(instanceHeader.getMetadataCollectionName());
            elementOrigin.setLicense(instanceHeader.getInstanceLicense());

            elementHeader.setOrigin(elementOrigin);

            elementHeader.setVersions(this.getElementVersions(instanceHeader));
        }
        else
        {
            super.handleMissingMetadataInstance(elementHeader.getClass().getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }
    }


    /**
     * Retrieve a specific named classification.
     *
     * @param classificationName  name of classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or the requested classification
     */
    protected ElementClassification getClassification(String classificationName,
                                                      List<ElementClassification> beanClassifications)
    {
        if ((classificationName != null) && (beanClassifications != null))
        {
            for (ElementClassification classification : beanClassifications)
            {
                if (classification != null)
                {
                    if (classification.getClassificationName().equals(classificationName))
                    {
                        return classification;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Remove the requested classification from the bean classifications and return the resulting list.
     *
     * @param classificationName  name of the classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or a list of classifications
     */
    protected List<ElementClassification> removeClassification(String classificationName,
                                                               List<ElementClassification> beanClassifications)
    {
        if ((classificationName != null) && (beanClassifications != null))
        {
            List<ElementClassification> results = new ArrayList<>();

            for (ElementClassification classification : beanClassifications)
            {
                if (classification != null)
                {
                    if (!classification.getClassificationName().equals(classificationName))
                    {
                        results.add(classification);
                    }
                }
            }

            if (!results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass    name of the class to create
     * @param entity       entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName   calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B> beanClass,
                                            EntityDetail entity,
                                            Relationship relationship,
                                            String methodName) throws PropertyServerException
    {
        RelatedElement relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entityProxy and that of a connected relationship.
     *
     * @param beanClass    name of the class to create
     * @param entityProxy  entityProxy containing the properties
     * @param relationship relationship containing the properties
     * @param methodName   calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B> beanClass,
                                            Relationship relationship,
                                            EntityProxy entityProxy,
                                            String methodName) throws PropertyServerException
    {
        RelatedElement relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entityProxy != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entityProxy, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }


    /**
     * Using the supplied instances, return a new instance of the Connection bean. It may be a Connection or a VirtualConnection.
     *
     * @param beanClass             class name for the bean
     * @param primaryEntity         entity that is the root of the collection of entities that make up the
     *                              content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships         relationships linking the entities
     * @param methodName            calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    protected Connection getEmbeddedConnection(Class<B> beanClass,
                                               EntityDetail primaryEntity,
                                               List<EntityDetail> supplementaryEntities,
                                               List<Relationship> relationships,
                                               String methodName) throws PropertyServerException
    {
        /*
         * The entities and relationships may describe either a Connection or a VirtualConnection.
         * This next piece of logic sorts out which type of bean to create.
         */
        if ((primaryEntity != null) && (primaryEntity.getType() != null))
        {
            String actualTypeName = primaryEntity.getType().getTypeDefName();

            if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.VIRTUAL_CONNECTION_TYPE_NAME))
            {
                return getNewVirtualConnection(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.CONNECTION_TYPE_NAME))
            {
                return getNewConnection(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else
            {
                /*
                 * This will throw an exception
                 */
                super.validateInstanceType(OpenMetadataType.CONNECTION_TYPE_NAME,
                                           beanClass.getName(),
                                           primaryEntity,
                                           methodName);
            }
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return null;
    }

    /**
     * Using the supplied instances, return a new instance of the VirtualConnection bean.  Virtual connections nest other connections
     * inside themselves to allow for connectors that use other connectors.  This means the converter needs to walk the
     * relationships to assemble the virtual connection accurately.
     *
     * @param beanClass             name of the class to create
     * @param primaryEntity         entity that is the root of the collection of entities that make up the
     *                              content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships         relationships linking the entities
     * @param methodName            calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Connection getNewVirtualConnection(Class<B> beanClass,
                                               EntityDetail primaryEntity,
                                               List<EntityDetail> supplementaryEntities,
                                               List<Relationship> relationships,
                                               String methodName) throws PropertyServerException
    {
        try
        {
            VirtualConnection connection = new VirtualConnection();

            fillInConnectionProperties(connection, primaryEntity, relationships, supplementaryEntities, methodName);

            /*
             * To fill out the rest of the virtual connection it is necessary to follow the relationships
             * because connections can be embedded inside one another to multiple depths.  Verify that there are
             * relationships to follow.
             */
            if ((relationships == null) || (relationships.isEmpty()))
            {
                return null;
            }

            /*
             * Get the list of entities that are directly connected to this connection.
             */
            List<String> connectedEntities = this.getConnectedEntities(primaryEntity, relationships);

            /*
             * The next step is to build a map of the entities so that they can be selected as we walk through the relationships.
             */
            Map<String, EntityDetail> entityDetailMap = new HashMap<>();

            if (supplementaryEntities != null)
            {
                for (EntityDetail entity : supplementaryEntities)
                {
                    if ((entity != null) && (entity.getGUID() != null) && (connectedEntities.contains(entity.getGUID())))
                    {
                        entityDetailMap.put(entity.getGUID(), entity);
                    }
                }
            }

            if (! entityDetailMap.isEmpty())
            {
                /*
                 * Step through the relationships looking for embedded connection relationships.  If the embedded relationship is for this
                 * connection then create it; otherwise ignore the relationship - other relationships will be picked up by the iterative
                 * processing.
                 */
                List<EmbeddedConnection> embeddedConnections = new ArrayList<>();
                for (Relationship relationship : relationships)
                {
                    if ((relationship != null) && (relationship.getType() != null))
                    {
                        if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.EMBEDDED_CONNECTION_TYPE_NAME))
                        {
                            EntityProxy parentConnectionProxy = relationship.getEntityOneProxy();

                            if ((parentConnectionProxy != null) && (primaryEntity.getGUID().equals(parentConnectionProxy.getGUID())))
                            {
                                EntityProxy embeddedConnectionProxy = relationship.getEntityTwoProxy();

                                if ((embeddedConnectionProxy != null) && (embeddedConnectionProxy.getGUID() != null))
                                {
                                    EntityDetail embeddedConnectionEntity = entityDetailMap.get((embeddedConnectionProxy.getGUID()));

                                    if (embeddedConnectionEntity != null)
                                    {
                                        InstanceProperties properties         = relationship.getProperties();
                                        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

                                        embeddedConnection.setPosition(getPosition(properties));
                                        embeddedConnection.setDisplayName(getDisplayName(properties));
                                        embeddedConnection.setArguments(getArguments(properties));
                                        embeddedConnection.setEmbeddedConnection(this.getEmbeddedConnection(beanClass,
                                                                                                            embeddedConnectionEntity,
                                                                                                            supplementaryEntities,
                                                                                                            relationships,
                                                                                                            methodName));

                                        embeddedConnections.add(embeddedConnection);
                                    }
                                }
                            }
                        }
                    }
                }

                if (!embeddedConnections.isEmpty())
                {
                    connection.setEmbeddedConnections(embeddedConnections);
                }
            }

            return connection;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the Connection bean. The
     * connection bean is made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted as they do not have any properties.
     * The Endpoint is optional.  If the ConnectorType is missing the bean is still returned even
     * though this is technically an error.
     *
     * @param beanClass             name of the class to create
     * @param primaryEntity         entity that is the root of the collection of entities that make up the
     *                              content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships         relationships linking the entities
     * @param methodName            calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Connection getNewConnection(Class<B>           beanClass,
                                        EntityDetail       primaryEntity,
                                        List<EntityDetail> supplementaryEntities,
                                        List<Relationship> relationships,
                                        String methodName) throws PropertyServerException
    {
        try
        {
            Connection connection = new Connection();

            fillInConnectionProperties(connection, primaryEntity, relationships, supplementaryEntities, methodName);

            return connection;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Retrieve the connection properties from an entity and save them in the supplied bean
     *
     * @param connection    bean to fill
     * @param primaryEntity        entity to trawl for values
     * @param relationships relationships linking the entities
     * @param methodName    calling method
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private void fillInConnectionProperties(Connection connection,
                                            EntityDetail primaryEntity,
                                            List<Relationship> relationships,
                                            List<EntityDetail> supplementaryEntities,
                                            String methodName) throws PropertyServerException
    {
        this.setUpElementHeader(connection, primaryEntity, OpenMetadataType.CONNECTION_TYPE_NAME, methodName);

        /*
         * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
         * properties, leaving any subclass properties to be stored in extended properties.
         */
        InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

        connection.setQualifiedName(this.removeQualifiedName(instanceProperties));
        connection.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
        connection.setDisplayName(this.removeDisplayName(instanceProperties));
        connection.setDescription(this.removeDescription(instanceProperties));
        connection.setSecuredProperties(this.removeSecuredProperties(instanceProperties));
        connection.setConfigurationProperties(this.removeConfigurationProperties(instanceProperties));
        connection.setUserId(this.removeUserId(instanceProperties));
        connection.setClearPassword(this.removeClearPassword(instanceProperties));
        connection.setEncryptedPassword(this.removeEncryptedPassword(instanceProperties));

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        connection.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.ASSET_TO_CONNECTION_TYPE_NAME))
                    {
                        connection.setAssetSummary(this.getAssetSummary(instanceProperties));
                    }
                }
            }
        }

        /*
         * Get the list of entities that are directly connected to this connection.
         */
        List<String> connectedEntities = this.getConnectedEntities(primaryEntity, relationships);

        /*
         * The other entities should include the ConnectorType and Endpoint
         */
        if (supplementaryEntities != null)
        {
            for (EntityDetail entity : supplementaryEntities)
            {
                if ((entity != null) && (entity.getType() != null) && (connectedEntities.contains(entity.getGUID())))
                {
                    String actualTypeName = entity.getType().getTypeDefName();

                    if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ENDPOINT.typeName))
                    {
                        Endpoint endpoint = getEndpoint(entity, methodName);

                        /*
                         * Add the endpoint to the connection
                         */
                        connection.setEndpoint(endpoint);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME))
                    {
                        ConnectorType connectorType = getConnectorType(entity, methodName);

                        /*
                         * Add the connector type to the connection
                         */
                        connection.setConnectorType(connectorType);
                    }
                }
            }
        }
    }


    /**
     * Return the list of guids for entities that are connected at this level.
     *
     * @param primaryEntity connection entity
     * @param relationships all relationships in connection
     * @return list of guids or empty list
     */
    private List<String> getConnectedEntities(EntityDetail       primaryEntity,
                                              List<Relationship> relationships)
    {
        List<String> connectedEntities = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship.getEntityOneProxy().getGUID().equals(primaryEntity.getGUID()))
                {
                    connectedEntities.add(relationship.getEntityTwoProxy().getGUID());
                }
                else if (relationship.getEntityTwoProxy().getGUID().equals(primaryEntity.getGUID()))
                {
                    connectedEntities.add(relationship.getEntityOneProxy().getGUID());
                }
            }
        }

        return connectedEntities;
    }



    /**
     * Retrieve the endpoint from an entity.
     *
     * @param entity     entity to trawl for values
     * @param methodName calling method
     * @return new endpoint object
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private Endpoint getEndpoint(EntityDetail entity,
                                 String methodName) throws PropertyServerException
    {
        Endpoint endpoint = new Endpoint();

        this.setUpElementHeader(endpoint, entity, OpenMetadataType.ENDPOINT.typeName, methodName);

        /*
         * The initial set of values come from the entity.
         */
        InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

        endpoint.setQualifiedName(this.removeQualifiedName(instanceProperties));
        endpoint.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
        endpoint.setDisplayName(this.removeName(instanceProperties));
        endpoint.setDescription(this.removeDescription(instanceProperties));
        endpoint.setAddress(this.removeNetworkAddress(instanceProperties));
        endpoint.setProtocol(this.removeProtocol(instanceProperties));
        endpoint.setEncryptionMethod(this.removeEncryptionMethod(instanceProperties));

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        endpoint.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        return endpoint;
    }


    /**
     * Retrieve the connector type from an entity.
     *
     * @param entity     entity to trawl for values
     * @param methodName calling method
     * @return new endpoint object
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private ConnectorType getConnectorType(EntityDetail entity,
                                           String methodName) throws PropertyServerException
    {
        ConnectorType connectorType = new ConnectorType();

        this.setUpElementHeader(connectorType, entity, OpenMetadataType.CONNECTOR_TYPE_TYPE_NAME, methodName);

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

        return connectorType;
    }


    /**
     * Extract and delete the permittedSynchronization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PermittedSynchronization enum
     */
    protected PermittedSynchronization removePermittedSynchronization(InstanceProperties instanceProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (PermittedSynchronization permittedSynchronization : PermittedSynchronization.values())
            {
                if (permittedSynchronization.getOrdinal() == ordinal)
                {
                    return permittedSynchronization;
                }
            }
        }

        return PermittedSynchronization.BOTH_DIRECTIONS;
    }


    /**
     * Extract and delete the portType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PortType enum
     */
    protected PortType removePortType(InstanceProperties instanceProperties)
    {
        final String methodName = "removePortType";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.PORT_TYPE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (PortType portType : PortType.values())
            {
                if (portType.getOpenTypeOrdinal() == ordinal)
                {
                    return portType;
                }
            }
        }

        return PortType.OTHER;
    }

    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties entity properties
     * @return ContactMethodType  enum value
     */
    protected ContactMethodType getContactMethodTypeFromProperties(InstanceProperties properties)
    {
        final String methodName = "getContactMethodTypeFromProperties";

        ContactMethodType contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName, OpenMetadataProperty.CONTACT_METHOD_TYPE.name, properties, methodName);

            switch (ordinal)
            {
                case 0:
                    contactMethodType = ContactMethodType.EMAIL;
                    break;

                case 1:
                    contactMethodType = ContactMethodType.PHONE;
                    break;

                case 2:
                    contactMethodType = ContactMethodType.CHAT;
                    break;

                case 3:
                    contactMethodType = ContactMethodType.PROFILE;
                    break;

                case 4:
                    contactMethodType = ContactMethodType.ACCOUNT;
                    break;

                case 99:
                    break;
            }
        }

        return contactMethodType;
    }

}