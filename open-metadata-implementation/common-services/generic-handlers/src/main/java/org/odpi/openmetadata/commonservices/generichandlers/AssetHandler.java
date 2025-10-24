/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 *
 * @param <B> class that represents the asset
 */
public class AssetHandler<B> extends ReferenceableHandler<B>
{
    private final ConnectionHandler<OpenMetadataAPIDummyBean> connectionHandler;


    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public AssetHandler(OpenMetadataAPIGenericConverter<B> converter,
                        Class<B>                           beanClass,
                        String                             serviceName,
                        String                             serverName,
                        InvalidParameterHandler            invalidParameterHandler,
                        RepositoryHandler                  repositoryHandler,
                        OMRSRepositoryHelper               repositoryHelper,
                        String                             localServerUserId,
                        OpenMetadataServerSecurityVerifier securityVerifier,
                        List<String>                       supportedZones,
                        List<String>                       defaultZones,
                        List<String>                       publishZones,
                        AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        this.connectionHandler = new ConnectionHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                         OpenMetadataAPIDummyBean.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param name the stored name property for the asset
     * @param resourceName the full name from the resource
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param resourceDescription the stored description property associated with the asset
     * @param deployedImplementationType type of technology
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetInRepository(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               qualifiedName,
                                           String               name,
                                           String               resourceName,
                                           String               versionIdentifier,
                                           String               resourceDescription,
                                           String               deployedImplementationType,
                                           Map<String, String>  additionalProperties,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           InstanceStatus       instanceStatus,
                                           Date                 effectiveFrom,
                                           Date                 effectiveTo,
                                           Date                 effectiveTime,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataType.ASSET.typeName;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        String assetTypeId = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                      OpenMetadataType.ASSET.typeName,
                                                                      serviceName,
                                                                      methodName,
                                                                      repositoryHelper);

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                name,
                                                resourceName,
                                                versionIdentifier,
                                                resourceDescription,
                                                deployedImplementationType,
                                                additionalProperties,
                                                assetTypeId,
                                                assetTypeName,
                                                extendedProperties,
                                                instanceStatus,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (defaultZones != null)
        {
            builder.setAssetZones(userId, defaultZones, methodName);
        }

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           assetTypeId,
                                           assetTypeName,
                                           OpenMetadataType.ASSET.typeName,
                                           null,
                                           builder,
                                           true,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUIDParameterName parameter name of the resulting asset's GUID
     * @param assetQualifiedName unique name for this asset
     * @param name the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param resourceDescription the stored description property associated with the asset
     * @param deployedImplementationType type of technology
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param assetTypeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param anchorEndpointToAsset set to true if the network address is unique for the asset and should not be reused. False if this is an endpoint
     *                              that is relevant for multiple assets.
     * @param configurationProperties configuration properties for the connection
     * @param connectorProviderClassName Java class name for the connector provider
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetWithConnection(String              userId,
                                             String              externalSourceGUID,
                                             String              externalSourceName,
                                             String              assetGUIDParameterName,
                                             String              assetQualifiedName,
                                             String              name,
                                             String              resourceName,
                                             String              versionIdentifier,
                                             String              resourceDescription,
                                             String              deployedImplementationType,
                                             Map<String, String> additionalProperties,
                                             String              assetTypeName,
                                             Map<String, Object> extendedProperties,
                                             InstanceStatus      instanceStatus,
                                             boolean             anchorEndpointToAsset,
                                             Map<String, Object> configurationProperties,
                                             String              connectorProviderClassName,
                                             String              networkAddress,
                                             String              protocol,
                                             String              encryptionMethod,
                                             Date                effectiveFrom,
                                             Date                effectiveTo,
                                             boolean             forLineage,
                                             boolean             forDuplicateProcessing,
                                             List<String>        serviceSupportedZones,
                                             Date                effectiveTime,
                                             String              methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String assetGUID = this.createAssetInRepository(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        assetQualifiedName,
                                                        name,
                                                        resourceName,
                                                        versionIdentifier,
                                                        resourceDescription,
                                                        deployedImplementationType,
                                                        additionalProperties,
                                                        assetTypeName,
                                                        extendedProperties,
                                                        instanceStatus,
                                                        effectiveFrom,
                                                        effectiveTo,
                                                        effectiveTime,
                                                        methodName);

        if (assetGUID != null)
        {
            connectionHandler.addAssetConnection(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 assetTypeName,
                                                 assetQualifiedName,
                                                 anchorEndpointToAsset,
                                                 configurationProperties,
                                                 connectorProviderClassName,
                                                 networkAddress,
                                                 protocol,
                                                 encryptionMethod,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 serviceSupportedZones,
                                                 effectiveTime,
                                                 methodName);
        }

        return assetGUID;
    }


    /**
     * Return an asset along with any associated connection.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying assetGUID
     * @param assetTypeName type name of asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetWithConnection(String       userId,
                                    String       assetGUID,
                                    String       assetGUIDParameterName,
                                    String       assetTypeName,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        EntityDetail assetEntity = this.getEntityFromRepository(userId,
                                                                assetGUID,
                                                                assetGUIDParameterName,
                                                                assetTypeName,
                                                                null,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                serviceSupportedZones,
                                                                effectiveTime,
                                                                methodName);

        if (assetEntity != null)
        {
            return this.getAssetWithConnectionBean(userId,
                                                   assetEntity,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   serviceSupportedZones,
                                                   effectiveTime,
                                                   methodName);
        }

        return null;
    }



    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param assetEntity entity for root connection object
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getAssetWithConnectionBean(String       userId,
                                         EntityDetail assetEntity,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> serviceSupportedZones,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        if (assetEntity != null)
        {
            EntityDetail connectionEntity = null;
            EntityProxy connectionProxy = null;

            Relationship relationshipToConnection = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  assetEntity.getGUID(),
                                                                                                  assetEntity.getType().getTypeDefName(),
                                                                                                  OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeGUID,
                                                                                                  OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                                                                  2,
                                                                                                  null,
                                                                                                  null,
                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                  null,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  effectiveTime,
                                                                                                  methodName);

            if (relationshipToConnection != null)
            {
                connectionProxy = relationshipToConnection.getEntityTwoProxy();
            }

            if (connectionProxy != null)
            {
                final String connectionGUIDParameterName = "relationshipToConnection.getEntityProxy().getGUID()";

                connectionEntity = this.getEntityFromRepository(userId,
                                                                connectionProxy.getGUID(),
                                                                connectionGUIDParameterName,
                                                                OpenMetadataType.CONNECTION.typeName,
                                                                null,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                serviceSupportedZones,
                                                                effectiveTime,
                                                                methodName);
            }

            List<Relationship> supplementaryRelationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities      = new ArrayList<>();

            if ((connectionEntity != null) && (connectionEntity.getType() != null))
            {
                /*
                 * The relationships are retrieved first.  It is not possible to follow the same pattern as SchemaType where
                 * embedded instances are retrieve as beans and then assembled in the final bean at the end because of the problem of
                 * matching the properties in the EmbeddedConnection relationship with the Connection bean it links to.
                 * So the entire graph of instances for the connection are retrieved and passed to the converter.
                 */
                supplementaryEntities.add(connectionEntity);
                supplementaryRelationships.add(relationshipToConnection);

                List<Relationship> connectionRelationships = this.getEmbeddedConnectionRelationships(userId,
                                                                                                     connectionEntity,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveTime,
                                                                                                     methodName);

                if (connectionRelationships != null)
                {
                    supplementaryRelationships.addAll(connectionRelationships);

                    for (Relationship relationship : connectionRelationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null))
                        {
                            EntityProxy entityProxy = null;

                            if ((repositoryHelper.isTypeOf(serviceName,
                                                           relationship.getType().getTypeDefName(),
                                                           OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName)))
                            {
                                entityProxy = relationship.getEntityTwoProxy();
                            }
                            if ((entityProxy != null) && (entityProxy.getGUID() != null) && (entityProxy.getType() != null))
                            {
                                final String entityGUIDParameterName = "embeddedRelationship proxy";
                                EntityDetail supplementaryEntity = this.getEntityFromRepository(userId,
                                                                                                entityProxy.getGUID(),
                                                                                                entityGUIDParameterName,
                                                                                                entityProxy.getType().getTypeDefName(),
                                                                                                null,
                                                                                                null,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                serviceSupportedZones,
                                                                                                effectiveTime,
                                                                                                methodName);
                                if (supplementaryEntity != null)
                                {
                                    supplementaryEntities.add(supplementaryEntity);
                                }
                            }
                        }
                    }
                }
            }

            if (supplementaryEntities.isEmpty())
            {
                supplementaryEntities = null;
            }

            if (supplementaryRelationships.isEmpty())
            {
                supplementaryEntities = null;
            }

            return converter.getNewComplexGraphBean(beanClass, assetEntity, supplementaryEntities, supplementaryRelationships, methodName);
        }

        return null;
    }


    /**
     * Recursively retrieve the list of relationships within a connection object.  The recursion occurs in VirtualConnections
     * that embed connections within themselves.
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedConnectionRelationships(String        userId,
                                                                  EntitySummary connectionEntity,
                                                                  boolean       forLineage,
                                                                  boolean       forDuplicateProcessing,
                                                                  Date          effectiveTime,
                                                                  String        methodName) throws PropertyServerException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   InvalidParameterException
    {
        List<Relationship> supplementaryRelationships = new ArrayList<>();

        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           connectionEntity.getGUID(),
                                                                                           connectionEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           null,
                                                                                           null,
                                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                                           null,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);


            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    if ((repositoryHelper.isTypeOf(serviceName,
                                                   relationship.getType().getTypeDefName(),
                                                   OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
                    {
                        supplementaryRelationships.add(relationship);

                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedConnectionRelationships(userId,
                                                                                                                         embeddedConnectionEnd,
                                                                                                                         forLineage,
                                                                                                                         forDuplicateProcessing,
                                                                                                                         effectiveTime,
                                                                                                                         methodName);

                            if (embeddedConnectionRelationships != null)
                            {
                                supplementaryRelationships.addAll(embeddedConnectionRelationships);
                            }
                        }
                    }
                }
            }
        }

        if (supplementaryRelationships.isEmpty())
        {
            return null;
        }

        return supplementaryRelationships;
    }
}
