/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ConnectionHandler manages Connection objects.  These describe the network addresses where services are running.  They are used by connection
 * objects to describe the service that the connector should call.  They are linked to servers to show their network address where the services that
 * they are hosting are running.
 * <br>
 * Most OMASs that work with Connection objects use the Open Connector Framework (OCF) Bean since this can be passed to the OCF Connector
 * Broker to create an instance of a connector to the attached asset.  Therefore, this handler has a default bean and converter which
 * is the one that works with the OCF bean.  The call can either use these values or override with their own bean/converter implementations.
 * <br>
 * ConnectionHandler runs server-side in the OMAG Server Platform and retrieves Connection entities through the OMRSRepositoryConnector via the
 * generic handler and repository handler.
 */
public class ConnectionHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
    public ConnectionHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Retrieve the list of connection objects attached to the requested asset object.
     * This includes the endpoint and connector type.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset object
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return Connection bean
     *
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public B getConnectionForAsset(String       userId,
                                   String       assetGUID,
                                   String       assetGUIDParameterName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        /*
         * This checks the asset is visible to the user.
         */
        EntityDetail assetEntity = getEntityFromRepository(userId,
                                                           assetGUID,
                                                           assetGUIDParameterName,
                                                           OpenMetadataType.ASSET.typeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        /*
         * Each returned entity has been verified as readable by the user before it is returned.
         */
        List<EntityDetail> connectionEntities = this.getAttachedEntities(userId,
                                                                         assetEntity,
                                                                         assetGUIDParameterName,
                                                                         OpenMetadataType.ASSET.typeName,
                                                                         OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeGUID,
                                                                         OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                                         OpenMetadataType.CONNECTION.typeName,
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


        EntityDetail selectedEntity;

        if (connectionEntities == null)
        {
            return null;
        }
        else
        {
            selectedEntity = securityVerifier.selectConnection(userId, assetEntity, connectionEntities, repositoryHelper, serviceName, methodName);
        }

        return getFullConnection(userId, selectedEntity, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getFullConnection(String       userId,
                                EntityDetail connectionEntity,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            /*
             * The relationships are retrieved first.  It is not possible to follow the same pattern as SchemaType where
             * embedded instances are retrieve as beans and then assembled in the final bean at the end because of the problem of
             * matching the properties in the EmbeddedConnection relationship with the Connection bean it links to.
             * So the entire graph of instances for the connection are retrieved and passed to the converter.
             */
            List<Relationship> supplementaryRelationships = this.getEmbeddedRelationships(userId, connectionEntity, forLineage, forDuplicateProcessing, effectiveTime, methodName);
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            if (supplementaryRelationships != null)
            {
                for (Relationship relationship : supplementaryRelationships)
                {
                    if ((relationship != null) && (relationship.getType() != null))
                    {
                        EntityProxy entityProxy = null;

                        if ((repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
                                || (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName)))
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

            if (supplementaryEntities.isEmpty())
            {
                supplementaryEntities = null;
            }

            return converter.getNewComplexGraphBean(beanClass, connectionEntity, supplementaryEntities, supplementaryRelationships, methodName);
        }

        return null;
    }

    
    /**
     * Recursively retrieve the list of relationships within a connection object.  The recursion occurs in VirtualConnections
     * that embed connections within themselves.
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedRelationships(String        userId,
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
                                                   OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                              relationship.getType().getTypeDefName(),
                                                              OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
                    {
                        supplementaryRelationships.add(relationship);

                        /*
                         * Navigate DOWN to the embedded connections
                         */
                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null) &&
                                (! embeddedConnectionEnd.getGUID().equals(connectionEntity.getGUID())))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedRelationships(userId,
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
