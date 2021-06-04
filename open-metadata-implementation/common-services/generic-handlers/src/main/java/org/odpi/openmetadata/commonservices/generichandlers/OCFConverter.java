/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DigitalArchitectureOMASConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public abstract class OCFConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    protected OCFConverter(OMRSRepositoryHelper   repositoryHelper,
                           String                 serviceName,
                           String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Extract the properties from the entity.
     *
     * @param elementHeader the header for the bean
     * @param entity entity containing the properties
     * @param expectedTypeName type that the entity must match (or it may be a subtype)
     * @param methodName calling method
     * @throws PropertyServerException the supplied entity is not of the expected type
     */
    protected void setUpElementHeader(ElementHeader elementHeader,
                                      EntityDetail  entity,
                                      String        expectedTypeName,
                                      String        methodName) throws PropertyServerException
    {
        if (entity != null)
        {
            super.validateInstanceType(expectedTypeName,
                                       elementHeader.getClass().getName(),
                                       entity,
                                       methodName);

            elementHeader.setGUID(entity.getGUID());
            elementHeader.setType(this.getElementType(entity));
            elementHeader.setURL(entity.getInstanceURL());
            elementHeader.setClassifications(this.getEntityClassifications(entity));
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
     * @param elementHeader the header for the bean
     * @param instanceHeader header of entity
     * @param classifications classifications from the entity
     * @param methodName calling method
     * @throws PropertyServerException the supplied entity is not of the expected type
     */
    protected void setUpElementHeader(ElementHeader        elementHeader,
                                      InstanceHeader       instanceHeader,
                                      List<Classification> classifications,
                                      String               methodName) throws PropertyServerException
    {
        if (instanceHeader != null)
        {
            elementHeader.setGUID(instanceHeader.getGUID());
            elementHeader.setType(this.getElementType(instanceHeader));
            elementHeader.setURL(instanceHeader.getInstanceURL());
            elementHeader.setClassifications(this.getEntityClassifications(classifications));
        }
        else
        {
            super.handleMissingMetadataInstance(elementHeader.getClass().getName(),
                                                TypeDefCategory.ENTITY_DEF,
                                                methodName);
        }
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entity entity containing the classifications
     * @return list of bean classifications
     */
    private List<ElementClassification> getEntityClassifications(EntityDetail entity)
    {
        if (entity != null)
        {
            return getEntityClassifications(entity.getClassifications());
        }

        return null;
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entityClassifications list of classifications from entity
     * @return list of bean classifications
     */
    protected List<ElementClassification> getEntityClassifications(List<Classification> entityClassifications)
    {
        List<ElementClassification> beanClassifications = null;

        if (entityClassifications != null)
        {
            beanClassifications = new ArrayList<>();

            for (Classification entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    ElementClassification beanClassification = new ElementClassification();

                    beanClassification.setElementSourceServer(serverName);
                    beanClassification.setElementOrigin(this.getElementOrigin(entityClassification.getInstanceProvenanceType()));
                    beanClassification.setElementMetadataCollectionId(entityClassification.getMetadataCollectionId());
                    beanClassification.setElementMetadataCollectionName(entityClassification.getMetadataCollectionName());
                    beanClassification.setElementLicense(entityClassification.getInstanceLicense());
                    beanClassification.setElementCreatedBy(entityClassification.getCreatedBy());
                    beanClassification.setElementUpdatedBy( entityClassification.getUpdatedBy());
                    beanClassification.setElementMaintainedBy(entityClassification.getMaintainedBy());
                    beanClassification.setElementCreateTime(entityClassification.getCreateTime());
                    beanClassification.setElementUpdateTime(entityClassification.getUpdateTime());
                    beanClassification.setElementVersion(entityClassification.getVersion());
                    beanClassification.setStatus(this.getElementStatus(entityClassification.getStatus()));

                    beanClassification.setClassificationName(entityClassification.getName());
                    beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));

                    beanClassifications.add(beanClassification);
                }
            }
        }

        return beanClassifications;
    }


    /**
     * Retrieve a specific named classification.
     *
     * @param classificationName name of classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or the requested classification
     */
    protected ElementClassification getClassification(String                      classificationName,
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
     * @param classificationName name of the classification
     * @param beanClassifications list of classifications retrieved from the repositories
     * @return null or a list of classifications
     */
    protected List<ElementClassification> removeClassification(String                      classificationName,
                                                               List<ElementClassification> beanClassifications)
    {
        if ((classificationName != null) && (beanClassifications != null))
        {
            List<ElementClassification> results = new ArrayList<>();

            for (ElementClassification classification : beanClassifications)
            {
                if (classification != null)
                {
                    if (! classification.getClassificationName().equals(classificationName))
                    {
                        results.add(classification);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    private ElementType getElementType(InstanceHeader instanceHeader)
    {
        ElementType  elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            elementType.setElementSourceServer(serverName);
            elementType.setElementOrigin(this.getElementOrigin(instanceHeader.getInstanceProvenanceType()));
            elementType.setElementMetadataCollectionId(instanceHeader.getMetadataCollectionId());
            elementType.setElementMetadataCollectionName(instanceHeader.getMetadataCollectionName());
            elementType.setElementLicense(instanceHeader.getInstanceLicense());
            elementType.setElementCreatedBy(instanceHeader.getCreatedBy());
            elementType.setElementUpdatedBy(instanceHeader.getUpdatedBy());
            elementType.setElementMaintainedBy(instanceHeader.getMaintainedBy());
            elementType.setElementCreateTime(instanceHeader.getCreateTime());
            elementType.setElementUpdateTime(instanceHeader.getUpdateTime());
            elementType.setElementVersion(instanceHeader.getVersion());
            elementType.setStatus(this.getElementStatus(instanceHeader.getStatus()));
            elementType.setElementTypeId(instanceType.getTypeDefGUID());
            elementType.setElementTypeName(instanceType.getTypeDefName());
            elementType.setElementTypeVersion(instanceType.getTypeDefVersion());
            elementType.setElementTypeDescription(instanceType.getTypeDefDescription());

            List<TypeDefLink> typeDefSuperTypes = instanceType.getTypeDefSuperTypes();

            if ((typeDefSuperTypes != null) && (! typeDefSuperTypes.isEmpty()))
            {
                List<String>   superTypes = new ArrayList<>();

                for (TypeDefLink typeDefLink : typeDefSuperTypes)
                {
                    if (typeDefLink != null)
                    {
                        superTypes.add(typeDefLink.getName());
                    }
                }

                if (! superTypes.isEmpty())
                {
                    elementType.setElementSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    private ElementOrigin getElementOrigin(InstanceProvenanceType instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOrigin.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOrigin.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOrigin.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOrigin.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOrigin.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOrigin.CONFIGURATION;

                case UNKNOWN:
                    return ElementOrigin.UNKNOWN;
            }
        }

        return ElementOrigin.UNKNOWN;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceStatus value from the repository services
     * @return ElementOrigin enum
     */
    private ElementStatus getElementStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            switch (instanceStatus)
            {
                case UNKNOWN:
                    return ElementStatus.UNKNOWN;

                case DRAFT:
                    return ElementStatus.DRAFT;

                case PREPARED:
                    return ElementStatus.PREPARED;

                case PROPOSED:
                    return ElementStatus.PROPOSED;

                case APPROVED:
                    return ElementStatus.APPROVED;

                case REJECTED:
                    return ElementStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return ElementStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return ElementStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return ElementStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return ElementStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return ElementStatus.STANDBY;

                case ACTIVE:
                    return ElementStatus.ACTIVE;

                case FAILED:
                    return ElementStatus.FAILED;

                case DISABLED:
                    return ElementStatus.DISABLED;

                case COMPLETE:
                    return ElementStatus.COMPLETE;

                case DEPRECATED:
                    return ElementStatus.DEPRECATED;

                case OTHER:
                    return ElementStatus.OTHER;
            }
        }

        return ElementStatus.UNKNOWN;
    }


    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    protected DataItemSortOrder removeSortOrder(InstanceProperties instanceProperties)
    {
        final String methodName = "removeSortOrder";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.SORT_ORDER_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (DataItemSortOrder dataItemSortOrder : DataItemSortOrder.values())
            {
                if (dataItemSortOrder.getOpenTypeOrdinal() == ordinal)
                {
                    return dataItemSortOrder;
                }
            }
        }

        return DataItemSortOrder.UNKNOWN;
    }


    /**
     * Retrieve and delete the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    protected OwnerType removeOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerType;
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of a classification
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    protected OwnerType getOwnerTypeFromProperties(InstanceProperties   properties)
    {
        OwnerType ownerType = OwnerType.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            ownerType = OwnerType.USER_ID;
                            break;

                        case 1:
                            ownerType = OwnerType.PROFILE_ID;
                            break;

                        case 99:
                            ownerType = OwnerType.OTHER;
                            break;
                    }
                }
            }
        }

        return ownerType;
    }



    /**
     * Using the supplied instances, return a new instance of the Connection bean. It may be a Connection or a VirtualConnection.
     *
     * @param beanClass class name for the bean
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    protected Connection getEmbeddedConnection(Class<B>           beanClass,
                                               EntityDetail       primaryEntity,
                                               List<EntityDetail> supplementaryEntities,
                                               List<Relationship> relationships,
                                               String             methodName) throws PropertyServerException
    {
        /*
         * The entities and relationships may describe either a Connection or a VirtualConnection.
         * This next piece of logic sorts out which type of bean to create.
         */
        if ((primaryEntity != null) && (primaryEntity.getType() != null))
        {
            String actualTypeName = primaryEntity.getType().getTypeDefName();

            if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.VIRTUAL_CONNECTION_TYPE_NAME))
            {
                return getNewVirtualConnection(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.CONNECTION_TYPE_NAME))
            {
                return getNewConnection(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
            }
            else
            {
                /*
                 * This will throw an exception
                 */
                super.validateInstanceType(OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
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
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Connection getNewVirtualConnection(Class<B>           beanClass,
                                               EntityDetail       primaryEntity,
                                               List<EntityDetail> supplementaryEntities,
                                               List<Relationship> relationships,
                                               String             methodName) throws PropertyServerException
    {
        try
        {
            VirtualConnection connection = new VirtualConnection();

            fillInConnectionProperties(connection, primaryEntity, relationships, methodName);

            /*
             * To fill out the rest of the virtual connection it is necessary to follow the relationships
             * because connections can be embedded inside one another to multiple depths.  Verify that there are
             * relationships to follow.
             */
            if ((relationships == null) || (relationships.isEmpty()))
            {
                handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
                return null;
            }

            /*
             * The next step is to build a map of the entities so that they can be selected as we walk through the relationships.
             */
            Map<String, EntityDetail> entityDetailMap = new HashMap<>();

            if (supplementaryEntities != null)
            {
                for (EntityDetail entity : supplementaryEntities)
                {
                    if ((entity != null) && (entity.getGUID() != null))
                    {
                        entityDetailMap.put(entity.getGUID(), entity);
                    }
                }
            }

            if (entityDetailMap.isEmpty())
            {
                handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                return null;
            }

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
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME))
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
                                    InstanceProperties properties = relationship.getProperties();
                                    EmbeddedConnection embeddedConnection = new EmbeddedConnection();

                                    embeddedConnection.setPosition(getPosition(properties));
                                    embeddedConnection.setDisplayName(getDisplayName(properties));
                                    embeddedConnection.setArguments(getArguments(properties));
                                    embeddedConnection.setEmbeddedConnection(this.getEmbeddedConnection(beanClass,
                                                                                                        primaryEntity,
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

            if (! embeddedConnections.isEmpty())
            {
                connection.setEmbeddedConnections(embeddedConnections);
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
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Connection getNewConnection(Class<B>           beanClass,
                                        EntityDetail       primaryEntity,
                                        List<EntityDetail> supplementaryEntities,
                                        List<Relationship> relationships,
                                        String             methodName) throws PropertyServerException
    {
        try
        {
            Connection connection = new Connection();

            fillInConnectionProperties(connection, primaryEntity, relationships, methodName);

            /*
             * The other entities should include the ConnectorType and Endpoint
             */
            if (supplementaryEntities != null)
            {
                for (EntityDetail entity : supplementaryEntities)
                {
                    if ((entity != null) && (entity.getType() != null))
                    {
                        String actualTypeName = entity.getType().getTypeDefName();

                        if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME))
                        {
                            Endpoint endpoint = getEndpoint(entity, methodName);

                            /*
                             * Add the endpoint to the connection
                             */
                            connection.setEndpoint(endpoint);
                        }
                        else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME))
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
     * @param connection bean to fill
     * @param entity entity to trawl for values
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private void fillInConnectionProperties(Connection         connection,
                                            EntityDetail       entity,
                                            List<Relationship> relationships,
                                            String             methodName) throws PropertyServerException
    {
        this.setUpElementHeader(connection, entity, OpenMetadataAPIMapper.CONNECTION_TYPE_NAME, methodName);

        /*
         * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
         * properties, leaving any subclass properties to be stored in extended properties.
         */
        InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

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
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME))
                    {
                        connection.setAssetSummary(this.getAssetSummary(instanceProperties));
                    }
                }
            }
        }
    }



    /**
     * Retrieve the endpoint from an entity.
     *
     * @param entity entity to trawl for values
     * @param methodName calling method
     * @return new endpoint object
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private Endpoint getEndpoint(EntityDetail entity,
                                 String       methodName) throws PropertyServerException
    {
        Endpoint endpoint = new Endpoint();

        this.setUpElementHeader(endpoint, entity, OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME, methodName);

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
     * @param entity entity to trawl for values
     * @param methodName calling method
     * @return new endpoint object
     * @throws PropertyServerException there was a problem unpacking the entity
     */
    private ConnectorType getConnectorType(EntityDetail entity,
                                           String       methodName) throws PropertyServerException
    {
        ConnectorType connectorType = new ConnectorType();

        this.setUpElementHeader(connectorType, entity, OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME, methodName);

        /*
         * The initial set of values come from the entity.
         */
        InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

        connectorType.setQualifiedName(this.removeQualifiedName(instanceProperties));
        connectorType.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
        connectorType.setDisplayName(this.removeName(instanceProperties));
        connectorType.setDescription(this.removeDescription(instanceProperties));
        connectorType.setConnectorProviderClassName(this.removeConnectorProviderClassName(instanceProperties));
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
}
