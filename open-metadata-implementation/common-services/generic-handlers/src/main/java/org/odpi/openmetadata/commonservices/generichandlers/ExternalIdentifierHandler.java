/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryIteratorForEntities;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelatedEntitiesIterator;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExternalIdentifierHandler manages ExternalIdentifier objects.  These entities represent the identifiers used for metadata
 * in third party technology. It runs server-side in the OMAG Server Platform and manages ExternalId entities through the OMRSRepositoryConnector
 * via the repository handler.
 * The ExternalIdentifier is linked to the SoftwareCapability that represents the third party technology
 * that generated the external identifier.  This is referred to as the scope. It is also linked to the element
 * (or elements) in open metadata that are equivalent to the metadata element(s) in the third party technology.
 * The correlation may be many-to-many.
 *
 * @param <EXTERNAL_ID> bean that returns an external identifier
 * @param <OPEN_METADATA_ELEMENT_HEADER> bean that returns the elements tied to this external identifier
 */
public class ExternalIdentifierHandler<EXTERNAL_ID, OPEN_METADATA_ELEMENT_HEADER> extends ReferenceableHandler<EXTERNAL_ID>
{
    private final OpenMetadataAPIGenericConverter<OPEN_METADATA_ELEMENT_HEADER> elementConverter;
    private final Class<OPEN_METADATA_ELEMENT_HEADER>                           elementBeanClass;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for the EXTERNAL_ID bean class
     * @param beanClass name of bean class that is represented by the generic class EXTERNAL_ID
     * @param elementConverter specific converter for the OPEN_METADATA_ELEMENT_HEADER bean class
     * @param elementBeanClass name of bean class that is represented by the generic class OPEN_METADATA_ELEMENT_HEADER
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ExternalIdentifierHandler(OpenMetadataAPIGenericConverter<EXTERNAL_ID>                  converter,
                                     Class<EXTERNAL_ID>                                            beanClass,
                                     OpenMetadataAPIGenericConverter<OPEN_METADATA_ELEMENT_HEADER> elementConverter,
                                     Class<OPEN_METADATA_ELEMENT_HEADER>                           elementBeanClass,
                                     String                                                        serviceName,
                                     String                                                        serverName,
                                     InvalidParameterHandler                                       invalidParameterHandler,
                                     RepositoryHandler                                             repositoryHandler,
                                     OMRSRepositoryHelper                                          repositoryHelper,
                                     String                                                        localServerUserId,
                                     OpenMetadataServerSecurityVerifier                            securityVerifier,
                                     List<String>                                                  supportedZones,
                                     List<String>                                                  defaultZones,
                                     List<String>                                                  publishZones,
                                     AuditLog                                                      auditLog)
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

        this.elementConverter = elementConverter;
        this.elementBeanClass = elementBeanClass;
    }


    /**
     * Set up the ExternalIdentifier for the supplied element.  This external identifier may already exist for the requested
     * scope if multiple open metadata entities are needed to represent the metadata element(s) in the third party metadata source
     * that is identified by this ExternalIdentifier.
     *
     * @param userId calling userId
     * @param elementGUID unique identifier of the open metadata element to link to the external identifier
     * @param elementGUIDParameterName parameter supplying elementGUID
     * @param elementTypeName type of the element
     * @param identifier identifier from the third party technology (scope)
     * @param identifierParameterName name of parameter supplying identifier
     * @param identifierKeyPattern type of key pattern used in the third party technology (typically local key)
     * @param identifierDescription name of the identifier in the third party technology
     * @param identifierUsage usage information from the connector/client supplying the identifier
     * @param identifierSource name of the connector/client supplying the identifier
     * @param identifierMappingProperties additional properties to help with the synchronization
     * @param externalInstanceCreatedBy the username of the person or process that created the instance in the external system
     * @param externalInstanceCreationTime the date/time when the instance in the external system was created
     * @param externalInstanceLastUpdatedBy the username of the person or process that last updated the instance in the external system
     * @param externalInstanceLastUpdateTime the date/time that the instance in the external system was last updated
     * @param externalInstanceVersion the latest version of the element in the external system
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeGUIDParameterName parameter supplying scopeGUID
     * @param scopeQualifiedName qualified name from the entity that
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param scopeDomainName specific domain name of the software capability that represents the third party metadata source
     * @param permittedSynchronization direction of synchronization
     * @param synchronizationDescription optional description of the synchronization in progress (augments the description in the
     *                                   permitted synchronization enum)
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setUpExternalIdentifier(String              userId,
                                        String              elementGUID,
                                        String              elementGUIDParameterName,
                                        String              elementTypeName,
                                        String              identifier,
                                        String              identifierParameterName,
                                        int                 identifierKeyPattern,
                                        String              identifierDescription,
                                        String              identifierUsage,
                                        String              identifierSource,
                                        Map<String, String> identifierMappingProperties,
                                        String              externalInstanceCreatedBy,
                                        Date                externalInstanceCreationTime,
                                        String              externalInstanceLastUpdatedBy,
                                        Date                externalInstanceLastUpdateTime,
                                        long                externalInstanceVersion,
                                        String              scopeGUID,
                                        String              scopeGUIDParameterName,
                                        String              scopeQualifiedName,
                                        String              scopeTypeName,
                                        String              scopeDomainName,
                                        int                 permittedSynchronization,
                                        String              synchronizationDescription,
                                        List<String>        serviceSupportedZones,
                                        Date                effectiveFrom,
                                        Date                effectiveTo,
                                        boolean             forLineage,
                                        boolean             forDuplicateProcessing,
                                        Date                effectiveTime,
                                        String              methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String externalIdGUIDParameterName = "externalIdGUID";

        EntityDetail externalIdEntity = this.getExternalIdEntity(userId,
                                                                 identifier,
                                                                 identifierParameterName,
                                                                 scopeGUID,
                                                                 scopeGUIDParameterName,
                                                                 scopeQualifiedName,
                                                                 scopeTypeName,
                                                                 serviceSupportedZones,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        String externalIdGUID;

        if (externalIdEntity == null)
        {
            /*
             * There is no external identifier and so it needs to be created and connected to the
             * scope and the element.
             */
            externalIdGUID = createExternalIdentifier(userId,
                                                      identifier,
                                                      identifierKeyPattern,
                                                      externalInstanceCreatedBy,
                                                      externalInstanceCreationTime,
                                                      externalInstanceLastUpdatedBy,
                                                      externalInstanceLastUpdateTime,
                                                      externalInstanceVersion,
                                                      scopeGUID,
                                                      scopeGUIDParameterName,
                                                      scopeTypeName,
                                                      scopeDomainName,
                                                      permittedSynchronization,
                                                      synchronizationDescription,
                                                      serviceSupportedZones,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);


            auditLog.logMessage(methodName,
                                GenericHandlersAuditCode.SETTING_UP_EXTERNAL_ID.getMessageDefinition(serviceName,
                                                                                                     elementTypeName,
                                                                                                     elementGUID,
                                                                                                     identifier,
                                                                                                     scopeQualifiedName,
                                                                                                     scopeGUID,
                                                                                                     methodName));
        }
        else
        {
            externalIdGUID = externalIdEntity.getGUID();

            updateExternalIdentifier(userId,
                                     externalIdGUID,
                                     externalIdGUIDParameterName,
                                     identifier,
                                     identifierKeyPattern,
                                     externalInstanceCreatedBy,
                                     externalInstanceCreationTime,
                                     externalInstanceLastUpdatedBy,
                                     externalInstanceLastUpdateTime,
                                     externalInstanceVersion,
                                     serviceSupportedZones,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
        }

        /*
         * Now check if the relationship currently exists between the element and the external id entity.
         */
        Relationship resourceLink = this.getResourceLinkRelationship(userId,
                                                                     elementGUID,
                                                                     elementGUIDParameterName,
                                                                     elementTypeName,
                                                                     externalIdGUID,
                                                                     serviceSupportedZones,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        if (resourceLink == null)
        {
            /*
             * At this point the link between the element and the external id entity is missing and needs to be created
             */
            createExternalIdLink(userId,
                                 elementGUID,
                                 elementGUIDParameterName,
                                 elementTypeName,
                                 externalIdGUID,
                                 externalIdGUIDParameterName,
                                 identifierDescription,
                                 identifierUsage,
                                 identifierSource,
                                 identifierMappingProperties,
                                 serviceSupportedZones,
                                 effectiveFrom,
                                 effectiveTo,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);
        }
        else
        {
            updateExternalIdLink(userId,
                                 resourceLink,
                                 identifierDescription,
                                 identifierUsage,
                                 identifierSource,
                                 identifierMappingProperties,
                                 methodName);
        }
    }


    /**
     * Create an audit log record to document that an external metadata source has created a relationship.
     *
     * @param scopeGUID unique identifier of the element representing the scope
     * @param scopeQualifiedName unique name of the element representing the scope
     * @param relationshipType type of relationship
     * @param end1GUID unique identifier for the entity at end 1 of the relationship
     * @param end1TypeName type of the entity at end 1 of the relationship
     * @param end2GUID unique identifier for the entity at end 2 of the relationship
     * @param end2TypeName type of the entity at end 2 of the relationship
     * @param methodName calling method
     */
    public  void logRelationshipCreation(String scopeGUID,
                                         String scopeQualifiedName,
                                         String relationshipType,
                                         String end1GUID,
                                         String end1TypeName,
                                         String end2GUID,
                                         String end2TypeName,
                                         String methodName)
    {
        if (scopeGUID != null)
        {
            auditLog.logMessage(methodName,
                                GenericHandlersAuditCode.NEW_EXTERNAL_RELATIONSHIP.getMessageDefinition(serviceName,
                                                                                                        relationshipType,
                                                                                                        end1TypeName,
                                                                                                        end1GUID,
                                                                                                        end2TypeName,
                                                                                                        end2GUID,
                                                                                                        methodName,
                                                                                                        scopeGUID,
                                                                                                        scopeQualifiedName));
        }
    }


    /**
     * Create an audit log record to document that an external metadata source has created a relationship.
     *
     * @param scopeGUID unique identifier of the element representing the scope
     * @param scopeQualifiedName unique name of the element representing the scope
     * @param relationshipType type of relationship
     * @param relationshipGUID unique identifier for the relationship
     * @param methodName calling method
     */
    public  void logRelationshipUpdate(String scopeGUID,
                                       String scopeQualifiedName,
                                       String relationshipType,
                                       String relationshipGUID,
                                       String methodName)
    {
        if (scopeGUID != null)
        {
            auditLog.logMessage(methodName,
                                GenericHandlersAuditCode.EXTERNAL_RELATIONSHIP_UPDATED.getMessageDefinition(serviceName,
                                                                                                            relationshipType,
                                                                                                            relationshipGUID,
                                                                                                            methodName,
                                                                                                            scopeGUID,
                                                                                                            scopeQualifiedName));
        }
    }


    /**
     * Create an audit log record to document that an external metadata source has removed a relationship.
     *
     * @param scopeGUID unique identifier of the element representing the scope
     * @param scopeQualifiedName unique name of the element representing the scope
     * @param relationshipType type of relationship
     * @param end1GUID unique identifier for the entity at end 1 of the relationship
     * @param end1TypeName type of the entity at end 1 of the relationship
     * @param end2GUID unique identifier for the entity at end 2 of the relationship
     * @param end2TypeName type of the entity at end 2 of the relationship
     * @param methodName calling method
     */
    public  void logRelationshipRemoval(String scopeGUID,
                                        String scopeQualifiedName,
                                        String relationshipType,
                                        String end1GUID,
                                        String end1TypeName,
                                        String end2GUID,
                                        String end2TypeName,
                                        String methodName)
    {
        if (scopeGUID != null)
        {
            auditLog.logMessage(methodName,
                                GenericHandlersAuditCode.EXTERNAL_RELATIONSHIP_REMOVED.getMessageDefinition(serviceName,
                                                                                                            relationshipType,
                                                                                                            end1TypeName,
                                                                                                            end1GUID,
                                                                                                            end2TypeName,
                                                                                                            end2GUID,
                                                                                                            methodName,
                                                                                                            scopeGUID,
                                                                                                            scopeQualifiedName));
        }
    }


    /**
     * Remove the ExternalIdentifier for the supplied element. The open metadata element is not affected.
     *
     * @param userId calling userId
     * @param elementGUID unique identifier of the open metadata element to link to the external identifier
     * @param elementGUIDParameterName parameter supplying elementGUID
     * @param elementTypeName type of the element
     * @param identifier identifier from the third party technology (scope)
     * @param identifierParameterName name of parameter supplying identifier
     * @param identifierKeyPattern type of key pattern used in the third party technology (typically local key)
     * @param identifierDescription name of the identifier in the third party technology
     * @param identifierUsage usage information from the connector/client supplying the identifier
     * @param identifierSource name of the connector/client supplying the identifier
     * @param identifierMappingProperties additional properties to help with the synchronization
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeGUIDParameterName parameter supplying scopeGUID
     * @param scopeQualifiedName qualified name from the entity that
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param permittedSynchronization direction of synchronization
     * @param synchronizationDescription optional description of the synchronization in progress (augments the description in the
     *                                   permitted synchronization enum)
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeExternalIdentifier(String              userId,
                                         String              elementGUID,
                                         String              elementGUIDParameterName,
                                         String              elementTypeName,
                                         String              identifier,
                                         String              identifierParameterName,
                                         int                 identifierKeyPattern,
                                         String              identifierDescription,
                                         String              identifierUsage,
                                         String              identifierSource,
                                         Map<String, String> identifierMappingProperties,
                                         List<String>        serviceSupportedZones,
                                         String              scopeGUID,
                                         String              scopeGUIDParameterName,
                                         String              scopeQualifiedName,
                                         String              scopeTypeName,
                                         int                 permittedSynchronization,
                                         String              synchronizationDescription,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        // todo
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param elementGUID unique identifier (GUID) of this element in open metadata
     * @param elementGUIDParameterName parameter supplying elementGUID
     * @param elementTypeName type of element being mapped
     * @param identifier unique identifier of this element in the external asset manager
     * @param identifierParameterName parameter supplying identifier
     * @param scopeGUID unique identifier of software capability representing the caller
     * @param scopeGUIDParameterName parameter name supplying scopeGUID
     * @param scopeQualifiedName unique name of the scope
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return the identifier's entity
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public EntityDetail confirmSynchronization(String       userId,
                                               String       elementGUID,
                                               String       elementGUIDParameterName,
                                               String       elementTypeName,
                                               String       identifier,
                                               String       identifierParameterName,
                                               String       scopeGUID,
                                               String       scopeGUIDParameterName,
                                               String       scopeQualifiedName,
                                               String       scopeTypeName,
                                               List<String> serviceSupportedZones,
                                               boolean      forLineage,
                                               boolean      forDuplicateProcessing,
                                               Date         effectiveTime,
                                               String       methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(scopeGUID, scopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(identifier, identifierParameterName, methodName);

        EntityDetail externalIdEntity = this.getExternalIdEntity(userId,
                                                                 identifier,
                                                                 identifierParameterName,
                                                                 scopeGUID,
                                                                 scopeGUIDParameterName,
                                                                 scopeQualifiedName,
                                                                 scopeTypeName,
                                                                 serviceSupportedZones,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);


        if (externalIdEntity == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_EXTERNAL_IDENTITY.getMessageDefinition(serviceName,
                                                                                                                        identifier,
                                                                                                                        scopeQualifiedName,
                                                                                                                        scopeGUID,
                                                                                                                        elementTypeName,
                                                                                                                        elementGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                identifierParameterName);
        }

        /*
         * Now check if the relationship currently exists between the element and the external id entity.
         */
        Relationship resourceLink = this.getResourceLinkRelationship(userId,
                                                                     elementGUID,
                                                                     elementGUIDParameterName,
                                                                     elementTypeName,
                                                                     externalIdEntity.getGUID(),
                                                                     serviceSupportedZones,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        if (resourceLink == null)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.UNKNOWN_RESOURCE_LINK.getMessageDefinition(serviceName,
                                                                                                                    identifier,
                                                                                                                    scopeQualifiedName,
                                                                                                                    scopeGUID,
                                                                                                                    elementTypeName,
                                                                                                                    elementGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                identifierParameterName);
        }
        else
        {
            InstanceProperties newProperties;

            if (resourceLink.getProperties() == null)
            {
                newProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                           null,
                                                                           OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                                           new Date(),
                                                                           methodName);
            }
            else
            {
                newProperties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                           new InstanceProperties(resourceLink.getProperties()),
                                                                           OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                                           new Date(),
                                                                           methodName);
            }

            repositoryHandler.updateRelationshipProperties(userId,
                                                           null,
                                                           null,
                                                           resourceLink,
                                                           newProperties,
                                                           methodName);
        }

        return externalIdEntity;
    }


    /**
     * Retrieve the ExternalIdentifier for the supplied element.  This external identifier needs to cone form the correct scope.
     *
     * @param userId calling userId
     * @param identifier identifier from the third party technology (scope)
     * @param identifierParameterName name of parameter supplying the identifier
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeGUIDParameterName parameter supplying scopeGUID
     * @param scopeQualifiedName unique name of the software capability that represents the third metadata source
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return ExternalId entity for the supplied identifier and scope
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private EntityDetail getExternalIdEntity(String       userId,
                                             String       identifier,
                                             String       identifierParameterName,
                                             String       scopeGUID,
                                             String       scopeGUIDParameterName,
                                             String       scopeQualifiedName,
                                             String       scopeTypeName,
                                             List<String> serviceSupportedZones,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        invalidParameterHandler.validateGUID(scopeGUID, scopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(identifier, identifierParameterName, methodName);

        /*
         * Since the external identifier is not necessarily unique and is linked many-to-many, begin with
         * retrieving all the ExternalId entities with the same identifier.
         */
        List<String> propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.IDENTIFIER.name);

        int queryPageSize = invalidParameterHandler.getMaxPagingSize();

        RepositoryIteratorForEntities identifierIterator = getEntitySearchIterator(userId,
                                                                                   identifier,
                                                                                   OpenMetadataType.EXTERNAL_ID.typeGUID,
                                                                                   OpenMetadataType.EXTERNAL_ID.typeName,
                                                                                   propertyNames,
                                                                                   true,
                                                                                   false,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   0,
                                                                                   queryPageSize,
                                                                                   effectiveTime,
                                                                                   methodName);


        while (identifierIterator.moreToReceive())
        {
            /*
             * For each of the matching external identifiers validate the scope
             */
            EntityDetail externalIdEntity = identifierIterator.getNext();

            if (this.validateExternalIdentifierScope(userId,
                                                     identifier,
                                                     externalIdEntity,
                                                     scopeGUID,
                                                     scopeQualifiedName,
                                                     scopeTypeName,
                                                     serviceSupportedZones,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName))
            {
                return externalIdEntity;
            }
        }

        return null;
    }


    /**
     * Retrieve the ExternalIdLink relationship between the open metadata element and the external identifier.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the open metadata element to link to the external identifier
     * @param elementGUIDParameterName parameter supplying elementGUID
     * @param elementTypeName type of the element
     * @param externalIdGUID unique identifier of the ExternalId entity
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return ExternalIdLink relationship between the requested elements - or null
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Relationship getResourceLinkRelationship(String  userId,
                                                     String  elementGUID,
                                                     String  elementGUIDParameterName,
                                                     String  elementTypeName,
                                                     String  externalIdGUID,
                                                     List<String> serviceSupportedZones,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        /*
         * Now check if the relationship currently exists between the element and the external id entity.
         */
        List<Relationship> resourceLinks = this.getAttachmentLinks(userId,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   elementTypeName,
                                                                   OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeGUID,
                                                                   OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                   externalIdGUID,
                                                                   OpenMetadataType.EXTERNAL_ID.typeName,
                                                                   0,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   effectiveTime,
                                                                   methodName);

        if (resourceLinks != null)
        {
            for (Relationship relationship : resourceLinks)
            {
                if ((relationship != null) && (relationship.getEntityOneProxy() != null))
                {
                    if (elementGUID.equals(relationship.getEntityOneProxy().getGUID()))
                    {
                        return relationship;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Determine if the scope of an external identifier matches the requester's scope.  This test is needed
     * because it is possible that different third party technologies are using the same external identifier
     * for completely different elements.  This is why the external identifiers are always tied to a scope
     * to show where it is valid.
     *
     * @param userId calling userId
     * @param identifier identifier from the third party technology (scope)
     * @param externalIdEntity entity for the external identifier
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeQualifiedName unique name of the software capability that represents the third metadata source
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param serviceSupportedZones supported zones for calling service
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private boolean validateExternalIdentifierScope(String       userId,
                                                    String       identifier,
                                                    EntityDetail externalIdEntity,
                                                    String       scopeGUID,
                                                    String       scopeQualifiedName,
                                                    String       scopeTypeName,
                                                    List<String> serviceSupportedZones,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String externalIdGUIDParameterName = "externalIdEntity.getGUID()";

        if ((externalIdEntity != null) && (externalIdEntity.getType() != null))
        {
            /*
             * An entity with the same identifier already exists - retrieve its relationships
             * to determine if connected to the same scope. An ordinary retrieve, rather than using an iterator,
             * is used because this number is expected to be small (number of external systems exchanging
             * metadata in the open metadata ecosystem that happens to use the same external identifier).
             */
            List<Relationship> externalIdScopes = this.getAttachmentLinks(userId,
                                                                          externalIdEntity.getGUID(),
                                                                          externalIdGUIDParameterName,
                                                                          OpenMetadataType.EXTERNAL_ID.typeName,
                                                                          OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeGUID,
                                                                          OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                                                          scopeGUID,
                                                                          scopeTypeName,
                                                                          0,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          serviceSupportedZones,
                                                                          0,
                                                                          invalidParameterHandler.getMaxPagingSize(),
                                                                          effectiveTime,
                                                                          methodName);

            return externalIdScopes != null;
        }
        else
        {
            /*
             * Throw logic error exception
             */
            throw new PropertyServerException(GenericHandlersErrorCode.NULL_EXTERNAL_ID_ENTITY.getMessageDefinition(identifier,
                                                                                                                    scopeTypeName,
                                                                                                                    scopeQualifiedName,
                                                                                                                    scopeGUID),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Create a new external Identifier and attach it to its valid scope.
     *
     * @param userId calling user
     * @param identifier identifier from the third party technology
     * @param identifierKeyPattern key pattern that defines the logic used to maintain the identifier
     * @param externalInstanceCreatedBy the username of the person or process that created the instance in the external system
     * @param externalInstanceCreationTime the date/time when the instance in the external system was created
     * @param externalInstanceLastUpdatedBy the username of the person or process that last updated the instance in the external system
     * @param externalInstanceLastUpdateTime the date/time that the instance in the external system was last updated
     * @param externalInstanceVersion the latest version of the element in the external system
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeGUIDParameterName parameter supplying scopeGUID
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param scopeDomainName specific type name of the software capability that represents the third party metadata source
     * @param permittedSynchronization direction of synchronization
     * @param synchronizationDescription optional description of the synchronization in progress (augments the description in the
     *                                   permitted synchronization enum)
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @return unique identifier of the new external id entity
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String createExternalIdentifier(String       userId,
                                            String       identifier,
                                            int          identifierKeyPattern,
                                            String       externalInstanceCreatedBy,
                                            Date         externalInstanceCreationTime,
                                            String       externalInstanceLastUpdatedBy,
                                            Date         externalInstanceLastUpdateTime,
                                            long         externalInstanceVersion,
                                            String       scopeGUID,
                                            String       scopeGUIDParameterName,
                                            String       scopeTypeName,
                                            String       scopeDomainName,
                                            int          permittedSynchronization,
                                            String       synchronizationDescription,
                                            List<String> serviceSupportedZones,
                                            Date         effectiveFrom,
                                            Date         effectiveTo,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String externalIdGUIDParameterName = "externalIdentifierGUID";


        ExternalIdentifierBuilder builder = new ExternalIdentifierBuilder(identifier,
                                                                          identifierKeyPattern,
                                                                          externalInstanceCreatedBy,
                                                                          externalInstanceCreationTime,
                                                                          externalInstanceLastUpdatedBy,
                                                                          externalInstanceLastUpdateTime,
                                                                          externalInstanceVersion,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        builder.setAnchors(userId,
                           scopeGUID,
                           scopeTypeName,
                           scopeDomainName,
                           methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String externalIdGUID = this.createBeanInRepository(userId,
                                                            null,
                                                            null,
                                                            OpenMetadataType.EXTERNAL_ID.typeGUID,
                                                            OpenMetadataType.EXTERNAL_ID.typeName,
                                                            builder,
                                                            effectiveTime,
                                                            methodName);

        if (externalIdGUID != null)
        {
            InstanceProperties scopeProperties = builder.getExternalIdScopeProperties(synchronizationDescription,
                                                                                      permittedSynchronization,
                                                                                      methodName);
            this.uncheckedLinkElementToElement(userId,
                                               null,
                                               null,
                                               scopeGUID,
                                               scopeGUIDParameterName,
                                               scopeTypeName,
                                               externalIdGUID,
                                               externalIdGUIDParameterName,
                                               OpenMetadataType.EXTERNAL_ID.typeName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               serviceSupportedZones,
                                               OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeGUID,
                                               OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                               scopeProperties,
                                               effectiveTime,
                                               methodName);

        }

        return externalIdGUID;
    }


    /**
     * Create a new external Identifier and attach it to its valid scope.
     *
     * @param userId calling user
     * @param externalIdGUID unique identifier of the
     * @param identifier identifier from the third party technology
     * @param identifierKeyPattern key pattern that defines the logic used to maintain the identifier
     * @param externalInstanceCreatedBy the username of the person or process that created the instance in the external system
     * @param externalInstanceCreationTime the date/time when the instance in the external system was created
     * @param externalInstanceLastUpdatedBy the username of the person or process that last updated the instance in the external system
     * @param externalInstanceLastUpdateTime the date/time that the instance in the external system was last updated
     * @param externalInstanceVersion the latest version of the element in the external system
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void updateExternalIdentifier(String       userId,
                                          String       externalIdGUID,
                                          String       externalIdGUIDParameterName,
                                          String       identifier,
                                          int          identifierKeyPattern,
                                          String       externalInstanceCreatedBy,
                                          Date         externalInstanceCreationTime,
                                          String       externalInstanceLastUpdatedBy,
                                          Date         externalInstanceLastUpdateTime,
                                          long         externalInstanceVersion,
                                          List<String> serviceSupportedZones,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        ExternalIdentifierBuilder builder = new ExternalIdentifierBuilder(identifier,
                                                                          identifierKeyPattern,
                                                                          externalInstanceCreatedBy,
                                                                          externalInstanceCreationTime,
                                                                          externalInstanceLastUpdatedBy,
                                                                          externalInstanceLastUpdateTime,
                                                                          externalInstanceVersion,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    externalIdGUID,
                                    externalIdGUIDParameterName,
                                    OpenMetadataType.EXTERNAL_ID.typeGUID,
                                    OpenMetadataType.EXTERNAL_ID.typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    builder.getInstanceProperties(methodName),
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Create the relationship between an open metadata element and an external id.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the open metadata element to link to the external identifier
     * @param elementGUIDParameterName parameter supplying elementGUID
     * @param elementTypeName type of the element
     * @param identifierDescription name of the identifier in the third party technology
     * @param identifierUsage usage information from the connector/client supplying the identifier
     * @param identifierSource name of the connector/client supplying the identifier
     * @param externalIdGUID unique identifier of the external id entity
     * @param externalIdGUIDParameterName parameter supplying externalIdGUID
     * @param identifierMappingProperties additional properties used to manage the mapping to the elements in the third party technology
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void createExternalIdLink(String              userId,
                                      String              elementGUID,
                                      String              elementGUIDParameterName,
                                      String              elementTypeName,
                                      String              externalIdGUID,
                                      String              externalIdGUIDParameterName,
                                      String              identifierDescription,
                                      String              identifierUsage,
                                      String              identifierSource,
                                      Map<String, String> identifierMappingProperties,
                                      List<String>        serviceSupportedZones,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        ExternalIdentifierBuilder builder = new ExternalIdentifierBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties resourceLinkProperties = builder.getExternalIdResourceLinkProperties(identifierDescription,
                                                                                                identifierUsage,
                                                                                                identifierSource,
                                                                                                identifierMappingProperties,
                                                                                                methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  elementGUID,
                                  elementGUIDParameterName,
                                  elementTypeName,
                                  externalIdGUID,
                                  externalIdGUIDParameterName,
                                  OpenMetadataType.EXTERNAL_ID.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                  resourceLinkProperties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Create the relationship between an open metadata element and an external id.
     *
     * @param userId calling user
     * @param externalIdLink existing relationship
     * @param identifierDescription name of the identifier in the third party technology
     * @param identifierUsage usage information from the connector/client supplying the identifier
     * @param identifierSource name of the connector/client supplying the identifier
     * @param identifierMappingProperties additional properties used to manage the mapping to the elements in the third party technology
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void updateExternalIdLink(String              userId,
                                      Relationship        externalIdLink,
                                      String              identifierDescription,
                                      String              identifierUsage,
                                      String              identifierSource,
                                      Map<String, String> identifierMappingProperties,
                                      String              methodName) throws UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        ExternalIdentifierBuilder builder = new ExternalIdentifierBuilder(repositoryHelper, serviceName, serverName);

        if (externalIdLink != null)
        {
            /*
             * Only update the relationship if things have changed (this rarely happens).
             */
            InstanceProperties existingProperties = externalIdLink.getProperties();

            if ((propertyUpdateNeeded(identifierDescription, OpenMetadataProperty.DESCRIPTION.name, existingProperties, methodName)) ||
                (propertyUpdateNeeded(identifierUsage, OpenMetadataProperty.USAGE.name, existingProperties, methodName)) ||
                (propertyUpdateNeeded(identifierSource, OpenMetadataProperty.SOURCE.name, existingProperties, methodName)))
            {
                InstanceProperties properties = builder.getExternalIdResourceLinkProperties(identifierDescription,
                                                                                            identifierUsage,
                                                                                            identifierSource,
                                                                                            identifierMappingProperties,
                                                                                            methodName);

                repositoryHandler.updateRelationshipProperties(userId,
                                                               null,
                                                               null,
                                                               externalIdLink,
                                                               properties,
                                                               methodName);
            }
        }
    }


    /**
     * Test if a string property needs updating.
     *
     * @param newValue new value
     * @param propertyName name of the property
     * @param existingProperties properties currently stored
     * @param methodName calling method
     *
     * @return boolean flag - true if the property needs updating
     */
    private boolean propertyUpdateNeeded(String             newValue,
                                         String             propertyName,
                                         InstanceProperties existingProperties,
                                         String             methodName)
    {
        String existingValue = repositoryHelper.getStringProperty(serviceName,
                                                                  propertyName,
                                                                  existingProperties,
                                                                  methodName);

        if ((existingValue == null) && (newValue == null))
        {
            return false;
        }

        if (existingValue == null)
        {
            return true;
        }

        return  (! existingValue.equals(newValue));
    }


    /**
     * Update and validate the properties associated with the ExternalIdScope relationship.
     *
     * @param userId calling userId
     * @param externalIdScope scope relationship
     * @param permittedSynchronization direction of synchronization
     * @param synchronizationDescription optional description of the synchronization in progress (augments the description in the permitted synchronization enum)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void updateScopeProperties(String       userId,
                                       Relationship externalIdScope,
                                       int          permittedSynchronization,
                                       String       synchronizationDescription,
                                       String       methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        ExternalIdentifierBuilder builder = new ExternalIdentifierBuilder(repositoryHelper, serviceName, serverName);

        /*
         * The properties for this synchronization are not set up - make the changes
         */
        InstanceProperties properties = builder.getExternalIdScopeProperties(synchronizationDescription,
                                                                             permittedSynchronization,
                                                                             methodName);

        repositoryHandler.updateRelationshipProperties(userId,
                                                       null,
                                                       null,
                                                       externalIdScope,
                                                       properties,
                                                       methodName);
    }


    /**
     * Return the external identifiers attached to a referenceable by the ExternalIdLink.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<EXTERNAL_ID> getExternalIdentifiersForElement(String       userId,
                                                              String       elementGUID,
                                                              String       elementGUIDParameterName,
                                                              String       elementTypeName,
                                                              List<String> serviceSupportedZones,
                                                              int          startingFrom,
                                                              int          pageSize,
                                                              boolean      forLineage,
                                                              boolean      forDuplicateProcessing,
                                                              Date         effectiveTime,
                                                              String       methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return this.getExternalIdentifiersForScope(userId,
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   elementTypeName,
                                                   serviceSupportedZones,
                                                   null,
                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                   null,
                                                   startingFrom,
                                                   pageSize,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Return the external identifiers attached to a referenceable by the ExternalIdLink.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param scopeQualifiedName unique name of the software capability that represents the third metadata source
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<EXTERNAL_ID> getExternalIdentifiersForScope(String  userId,
                                                            String  elementGUID,
                                                            String  elementGUIDParameterName,
                                                            String  elementTypeName,
                                                            String  scopeGUID,
                                                            String  scopeTypeName,
                                                            String  scopeQualifiedName,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            int     startingFrom,
                                                            int     pageSize,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return getExternalIdentifiersForScope(userId,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              elementTypeName,
                                              supportedZones,
                                              scopeGUID,
                                              scopeTypeName,
                                              scopeQualifiedName,
                                              startingFrom,
                                              pageSize,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Return the external identifiers attached to a referenceable by the ExternalIdLink.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param scopeGUID unique identifier of the software capability that represents the third metadata source
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param scopeQualifiedName unique name of the software capability that represents the third party metadata source
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<EXTERNAL_ID> getExternalIdentifiersForScope(String       userId,
                                                            String       elementGUID,
                                                            String       elementGUIDParameterName,
                                                            String       elementTypeName,
                                                            List<String> serviceSupportedZones,
                                                            String       scopeGUID,
                                                            String       scopeTypeName,
                                                            String       scopeQualifiedName,
                                                            int          startingFrom,
                                                            int          pageSize,
                                                            boolean      forLineage,
                                                            boolean      forDuplicateProcessing,
                                                            Date         effectiveTime,
                                                            String       methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        List<EXTERNAL_ID> results = new ArrayList<>();

        List<Relationship> externalIdLinks = this.getAttachmentLinks(userId,
                                                                     elementGUID,
                                                                     elementGUIDParameterName,
                                                                     elementTypeName,
                                                                     OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                     null,
                                                                     OpenMetadataType.EXTERNAL_ID.typeName,
                                                                     0,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     serviceSupportedZones,
                                                                     startingFrom,
                                                                     pageSize,
                                                                     effectiveTime,
                                                                     methodName);

        if (externalIdLinks != null)
        {
            for (Relationship externalIdLink : externalIdLinks)
            {
                if ((externalIdLink != null) && (externalIdLink.getEntityTwoProxy()!= null))
                {
                    final String externalIdGUIDParameterName = "externalIdLink.getEntityTwoProxy().getGUID()";

                    String externalIdGUID = externalIdLink.getEntityTwoProxy().getGUID();

                    EntityDetail externalIdEntity = this.getEntityFromRepository(userId,
                                                                                 externalIdGUID,
                                                                                 externalIdGUIDParameterName,
                                                                                 OpenMetadataType.EXTERNAL_ID.typeName,
                                                                                 null,
                                                                                 null,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 serviceSupportedZones,
                                                                                 effectiveTime,
                                                                                 methodName);

                    if ((externalIdEntity != null) && (externalIdEntity.getType() != null))
                    {
                        List<Relationship> externalIdScopes = this.getAttachmentLinks(userId,
                                                                                      externalIdEntity,
                                                                                      externalIdGUIDParameterName,
                                                                                      OpenMetadataType.EXTERNAL_ID.typeName,
                                                                                      OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeGUID,
                                                                                      OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                                                                      null,
                                                                                      scopeTypeName,
                                                                                      0,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      serviceSupportedZones,
                                                                                      startingFrom,
                                                                                      pageSize,
                                                                                      effectiveTime,
                                                                                      methodName);

                        if (externalIdScopes != null)
                        {
                            for (Relationship externalIdScope : externalIdScopes)
                            {
                                if ((externalIdScope != null) && (externalIdScope.getEntityOneProxy() != null))
                                {
                                    if ((scopeGUID == null) ||
                                        (scopeGUID.equals(externalIdScope.getEntityOneProxy().getGUID())))
                                    {
                                        List<Relationship> externalIdRelationships = new ArrayList<>();

                                        externalIdRelationships.add(externalIdLink);
                                        externalIdRelationships.add(externalIdScope);

                                        EXTERNAL_ID bean = converter.getNewComplexBean(beanClass,
                                                                                       externalIdEntity,
                                                                                       externalIdRelationships,
                                                                                       methodName);

                                        if (bean != null)
                                        {
                                            results.add(bean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Return the list of entities for open metadata elements of an open metadata type that are associated with an
     * external identifier in a particular scope.
     *
     * @param userId calling user
     * @param scopeGUID unique identifier of software capability representing the caller
     * @param scopeParameterName unique name of software capability representing the caller
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param requestedTypeName unique type name of the elements in the external asset manager
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<EntityDetail> getElementEntitiesForScope(String  userId,
                                                         String  scopeGUID,
                                                         String  scopeParameterName,
                                                         String  scopeTypeName,
                                                         String  requestedTypeName,
                                                         int     startingFrom,
                                                         int     pageSize,
                                                         Date    effectiveTime,
                                                         boolean      forLineage,
                                                         boolean      forDuplicateProcessing,
                                                         String       methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String requestedTypeNameParameterName = "requestedTypeName";

        invalidParameterHandler.validateGUID(scopeGUID, scopeParameterName, methodName);
        invalidParameterHandler.validateName(requestedTypeName, requestedTypeNameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        RepositoryRelatedEntitiesIterator externalIdIterator = new RepositoryRelatedEntitiesIterator(repositoryHandler,
                                                                                                     invalidParameterHandler,
                                                                                                     userId,
                                                                                                     scopeGUID,
                                                                                                     scopeTypeName,
                                                                                                     OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeGUID,
                                                                                                     OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                                                                                     null,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     0,
                                                                                                     0,
                                                                                                     effectiveTime,
                                                                                                     methodName);

        int skippedResults = 0;
        List<EntityDetail> results = new ArrayList<>();

        while ((externalIdIterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
        {
            EntityDetail externalIdEntity = externalIdIterator.getNext();

            if (externalIdEntity != null)
            {
                RepositoryRelatedEntitiesIterator elementIterator = new RepositoryRelatedEntitiesIterator(repositoryHandler,
                                                                                                          invalidParameterHandler,
                                                                                                          userId,
                                                                                                          externalIdEntity.getGUID(),
                                                                                                          OpenMetadataType.EXTERNAL_ID.typeName,
                                                                                                          OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeGUID,
                                                                                                          OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                                                          null,
                                                                                                          forLineage,
                                                                                                          forDuplicateProcessing,
                                                                                                          0,
                                                                                                          0,
                                                                                                          effectiveTime,
                                                                                                          methodName);

                while ((externalIdIterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
                {
                    EntityDetail elementEntity = elementIterator.getNext();

                    if ((elementEntity != null) && (elementEntity.getType() != null))
                    {
                        if (repositoryHelper.isTypeOf(serviceName, elementEntity.getType().getTypeDefName(), requestedTypeName))
                        {
                            if (skippedResults < startingFrom)
                            {
                                skippedResults ++;
                            }
                            else
                            {
                                results.add(elementEntity);
                            }
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.  It is necessary to navigate to the externalIdentifier from the scope.
     *
     * @param userId calling user
     * @param scopeGUID unique identifier of software capability representing the caller
     * @param scopeParameterName unique name of software capability representing the caller
     * @param scopeTypeName specific type name of the software capability that represents the third party metadata source
     * @param scopeQualifiedName unique name of the software capability that represents the third party metadata source
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param serviceSupportedZones zones from the calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<OPEN_METADATA_ELEMENT_HEADER> getElementsForExternalIdentifier(String       userId,
                                                                               String       scopeGUID,
                                                                               String       scopeParameterName,
                                                                               String       scopeTypeName,
                                                                               String       scopeQualifiedName,
                                                                               String       externalIdentifier,
                                                                               List<String> serviceSupportedZones,
                                                                               int          startingFrom,
                                                                               int          pageSize,
                                                                               boolean      forLineage,
                                                                               boolean      forDuplicateProcessing,
                                                                               Date         effectiveTime,
                                                                               String       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateGUID(scopeGUID, scopeParameterName, methodName);
        invalidParameterHandler.validateName(externalIdentifier, externalIdentifierParameterName, methodName);

        List<String> propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.IDENTIFIER.name);

        List<EntityDetail> matchingExternalIds = this.getEntitiesByValue(userId,
                                                                         externalIdentifier,
                                                                         externalIdentifierParameterName,
                                                                         OpenMetadataType.EXTERNAL_ID.typeGUID,
                                                                         OpenMetadataType.EXTERNAL_ID.typeName,
                                                                         propertyNames,
                                                                         true,
                                                                         false,
                                                                         null,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         serviceSupportedZones,
                                                                         null,
                                                                         0,
                                                                         invalidParameterHandler.getMaxPagingSize(),
                                                                         effectiveTime,
                                                                         methodName);


        if (matchingExternalIds != null)
        {
            final String matchingEntityGUIDParameterName = "matchingEntity.getGUID()";

            for (EntityDetail matchingExternalId : matchingExternalIds)
            {
                if (matchingExternalId != null)
                {
                    /*
                     * Is this externalId connected to the right scope
                     */
                    List<Relationship> externalIdRelationships = this.getAttachmentLinks(userId,
                                                                                         matchingExternalId,
                                                                                         matchingEntityGUIDParameterName,
                                                                                         OpenMetadataType.EXTERNAL_ID.typeName,
                                                                                         OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeGUID,
                                                                                         OpenMetadataType.EXTERNAL_ID_SCOPE_RELATIONSHIP.typeName,
                                                                                         scopeGUID,
                                                                                         scopeTypeName,
                                                                                         1,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         serviceSupportedZones,
                                                                                         0,
                                                                                         invalidParameterHandler.getMaxPagingSize(),
                                                                                         effectiveTime,
                                                                                         methodName);

                    if ((externalIdRelationships != null) && (externalIdRelationships.isEmpty()))
                    {
                        /*
                         * ExternalId is connected to the right scope
                         */
                        return this.getElementHeaders(userId,
                                                      matchingExternalId,
                                                      matchingEntityGUIDParameterName,
                                                      serviceSupportedZones,
                                                      startingFrom,
                                                      pageSize,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.  It is necessary to navigate to the externalIdentifier.
     *
     * @param userId calling user
     * @param externalId unique identifier of software capability representing the caller
     * @param externalIdGUIDParameterName unique name of software capability representing the caller
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private List<OPEN_METADATA_ELEMENT_HEADER> getElementHeaders(String       userId,
                                                                 EntityDetail externalId,
                                                                 String       externalIdGUIDParameterName,
                                                                 List<String> serviceSupportedZones,
                                                                 int          startingFrom,
                                                                 int          pageSize,
                                                                 boolean      forLineage,
                                                                 boolean      forDuplicateProcessing,
                                                                 Date         effectiveTime,
                                                                 String       methodName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        List<EntityDetail> elementEntities = this.getAttachedEntities(userId,
                                                                      externalId,
                                                                      externalIdGUIDParameterName,
                                                                      OpenMetadataType.EXTERNAL_ID.typeName,
                                                                      OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeGUID,
                                                                      OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                      OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                      null,
                                                                      null,
                                                                      1,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      serviceSupportedZones,
                                                                      startingFrom,
                                                                      pageSize,
                                                                      effectiveTime,
                                                                      methodName);

        List<OPEN_METADATA_ELEMENT_HEADER> results = new ArrayList<>();

        if (elementEntities != null)
        {
            for (EntityDetail elementEntity : elementEntities)
            {
                if (elementEntity != null)
                {
                    OPEN_METADATA_ELEMENT_HEADER bean = elementConverter.getNewBean(elementBeanClass,
                                                                                    elementEntity,
                                                                                    methodName);

                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }
}
