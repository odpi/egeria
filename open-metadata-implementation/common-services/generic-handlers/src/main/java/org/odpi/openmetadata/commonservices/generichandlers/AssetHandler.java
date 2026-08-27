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

/**
 * AssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 *
 * @param <B> class that represents the asset
 */
public class AssetHandler<B> extends ReferenceableHandler<B>
{
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
              auditLog);
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
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetWithConnection(String       userId,
                                    String       assetGUID,
                                    String       assetGUIDParameterName,
                                    String       assetTypeName,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
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
                                                                effectiveTime,
                                                                methodName);

        if (assetEntity != null)
        {
            return this.getAssetWithConnectionBean(userId,
                                                   assetEntity,
                                                   forLineage,
                                                   forDuplicateProcessing,
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
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getAssetWithConnectionBean(String       userId,
                                         EntityDetail assetEntity,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
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
                                                                                                  OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeGUID,
                                                                                                  OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName,
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
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
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
                                                                                           2,
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
                                                          OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName)))
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
