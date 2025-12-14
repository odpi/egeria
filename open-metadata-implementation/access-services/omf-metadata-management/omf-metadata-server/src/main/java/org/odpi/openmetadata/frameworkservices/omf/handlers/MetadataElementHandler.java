/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.converters.RelatedElementConverter;
import org.odpi.openmetadata.frameworkservices.omf.converters.OpenMetadataRelationshipConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * MetadataElementHandler manages MetadataElement objects from the Open Metadata Framework (OMF).
 * These objects are 1-1 with an open metadata entity.
 */
public class MetadataElementHandler<B> extends ReferenceableHandler<B>
{
    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final OpenMetadataRelationshipConverter<OpenMetadataRelationship> openMetadataRelationshipsConverter;
    private final RelatedElementConverter<RelatedMetadataElement>             relatedElementConverter;

    private final FilesAndFoldersHandler<Object, Object, Object>     filesAndFoldersHandler;
    private static final Logger log = LoggerFactory.getLogger(MetadataElementHandler.class);

    /**
     * Construct the handler for metadata elements.
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
    public MetadataElementHandler(OpenMetadataAPIGenericConverter<B> converter,
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

        openMetadataRelationshipsConverter = new OpenMetadataRelationshipConverter<>(repositoryHelper, serviceName, serverName);
        relatedElementConverter            = new RelatedElementConverter<>(repositoryHelper, serviceName, serverName);

        filesAndFoldersHandler = new FilesAndFoldersHandler<>(null,
                                                              Object.class,
                                                              null,
                                                              Object.class,
                                                              null,
                                                              Object.class,
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
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param methodName calling method
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public B getMetadataElementByGUID(String       userId,
                                      String       elementGUID,
                                      String       requestedTypeName,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         asOfTime,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        /*
         * This validates the type name before calling the repository.
         */
        this.getEntityTypeGUID(requestedTypeName);

        return this.getBeanFromRepository(userId,
                                          elementGUID,
                                          guidParameterName,
                                          this.getEntityTypeName(requestedTypeName),
                                          forLineage,
                                          forDuplicateProcessing,
                                          asOfTime,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Convert the entity into a  metadata element.
     *
     * @param entityDetail properties for the metadata element
     * @param methodName calling method
     *
     * @return metadata element properties
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public B getMetadataElementFromEntity(EntityDetail entityDetail,
                                          String       methodName) throws PropertyServerException
    {
        return this.converter.getNewBean(beanClass, entityDetail, methodName);
    }


    /**
     * Set up a type name to use on the call.  If the caller passes a type name, it is used, otherwise
     * OpenMetadataRoot is used.
     *
     * @param requestedTypeName type name from the caller
     * @return type name to use with the repository
     */
    private String getEntityTypeName(String requestedTypeName)
    {
        String typeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

        if ((requestedTypeName != null) && (! requestedTypeName.isBlank()))
        {
            typeName = requestedTypeName;
        }

        return typeName;
    }


    /**
     * Set up a type GUID to use on the call.  If the caller passes a type name, it is used, otherwise
     * OpenMetadataRoot is used.  If the type name is invalid an exception is thrown.
     *
     * @param requestedTypeName type name from the caller
     * @throws InvalidParameterException bad type name
     * @return type name to use with the repository
     */
    private String getEntityTypeGUID(String requestedTypeName) throws InvalidParameterException
    {
        final String methodName = "getEntityTypeGUID";

        String typeName = this.getEntityTypeName(requestedTypeName);

        return invalidParameterHandler.validateTypeName(typeName,
                                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                        serviceName,
                                                        methodName,
                                                        repositoryHelper);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniqueNameParameterName name of the parameter that passed the unique name (optional)
     * @param uniqueNamePropertyName name of the property from the open types to use in the lookup
     * @param requestedTypeName typeName from caller
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param methodName calling method
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public B getMetadataElementByUniqueName(String              userId,
                                            String              uniqueName,
                                            String              uniqueNameParameterName,
                                            String              uniqueNamePropertyName,
                                            String              requestedTypeName,
                                            List<ElementStatus> limitResultsByStatus,
                                            Date                asOfTime,
                                            SequencingOrder     sequencingOrder,
                                            String              sequencingPropertyName,
                                            boolean             forLineage,
                                            boolean             forDuplicateProcessing,
                                            Date                effectiveTime,
                                            String              methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String nameParameterName = "uniqueName";
        final String namePropertyName  = "uniqueNamePropertyName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueNamePropertyName, namePropertyName, methodName);

        if (uniqueNameParameterName != null)
        {
            invalidParameterHandler.validateName(uniqueName, uniqueNameParameterName, methodName);

            return this.getBeanByUniqueName(userId,
                                            uniqueName,
                                            uniqueNameParameterName,
                                            uniqueNamePropertyName,
                                            this.getEntityTypeGUID(requestedTypeName),
                                            this.getEntityTypeName(requestedTypeName),
                                            getInstanceStatuses(limitResultsByStatus),
                                            asOfTime,
                                            getSequencingOrder(sequencingOrder),
                                            sequencingPropertyName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
        }
        else
        {
            invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

            return this.getBeanByUniqueName(userId,
                                            uniqueName,
                                            nameParameterName,
                                            uniqueNamePropertyName,
                                            this.getEntityTypeGUID(requestedTypeName),
                                            this.getEntityTypeName(requestedTypeName),
                                            getInstanceStatuses(limitResultsByStatus),
                                            asOfTime,
                                            getSequencingOrder(sequencingOrder),
                                            sequencingPropertyName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
        }
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniqueNameParameterName name of the parameter that passed the unique name (optional)
     * @param uniqueNamePropertyName name of the property from the open types to use in the lookup
     * @param requestedTypeName typeName from caller
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param methodName calling method
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String              userId,
                                                     String              uniqueName,
                                                     String              uniqueNameParameterName,
                                                     String              uniqueNamePropertyName,
                                                     String              requestedTypeName,
                                                     List<ElementStatus> limitResultsByStatus,
                                                     Date                asOfTime,
                                                     SequencingOrder     sequencingOrder,
                                                     String              sequencingPropertyName,
                                                     boolean             forLineage,
                                                     boolean             forDuplicateProcessing,
                                                     Date                effectiveTime,
                                                     String              methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String nameParameterName = "uniqueName";

        invalidParameterHandler.validateUserId(userId, methodName);

        if (uniqueNameParameterName != null)
        {
            invalidParameterHandler.validateName(uniqueName, uniqueNameParameterName, methodName);
        }
        else
        {
            invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);
        }

        return this.getBeanGUIDByUniqueName(userId,
                                            uniqueName,
                                            uniqueNameParameterName,
                                            uniqueNamePropertyName,
                                            this.getEntityTypeGUID(requestedTypeName),
                                            this.getEntityTypeName(requestedTypeName),
                                            getInstanceStatuses(limitResultsByStatus),
                                            asOfTime,
                                            getSequencingOrder(sequencingOrder),
                                            sequencingPropertyName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param requestedTypeName optional type name to restrict search to a specific type of element (and their subtypes)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findMetadataElementsWithString(String              userId,
                                                  String              searchString,
                                                  String              requestedTypeName,
                                                  List<ElementStatus> limitResultsByStatus,
                                                  Date                asOfTime,
                                                  String              sequencingProperty,
                                                  SequencingOrder     sequencingOrder,
                                                  boolean             forLineage,
                                                  boolean             forDuplicateProcessing,
                                                  Date                effectiveTime,
                                                  int                 startFrom,
                                                  int                 pageSize,
                                                  String              methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);

        if (requestedTypeName == null)
        {
            invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        }
        else if ((searchString == null) || (searchString.isBlank()) || ("*".equals(searchString)))
        {
            searchString = ".*";
        }

        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              this.getEntityTypeGUID(requestedTypeName),
                              this.getEntityTypeName(requestedTypeName),
                              getInstanceStatuses(limitResultsByStatus),
                              asOfTime,
                              getSequencingOrder(sequencingOrder),
                              sequencingProperty,
                              forLineage,
                              forDuplicateProcessing,
                              startFrom,
                              pageSize,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the metadata elements that are of the correct type.
     *
     * @param userId caller's userId
     * @param requestedTypeName   specific type of element (and their subtypes) to retrieve
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> getMetadataElementsByType(String              userId,
                                             String              requestedTypeName,
                                             boolean             forLineage,
                                             boolean             forDuplicateProcessing,
                                             List<ElementStatus> limitResultsByStatus,
                                             Date                asOfTime,
                                             String              sequencingProperty,
                                             SequencingOrder     sequencingOrder,
                                             Date                effectiveTime,
                                             int                 startFrom,
                                             int                 pageSize,
                                             String              methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String typeParameterName = "metadataElementTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(requestedTypeName, typeParameterName, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesByType(userId,
                                                                          this.getEntityTypeGUID(requestedTypeName),
                                                                          this.getInstanceStatuses(limitResultsByStatus),
                                                                          asOfTime,
                                                                          sequencingProperty,
                                                                          getSequencingOrder(sequencingOrder),
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          startFrom,
                                                                          pageSize,
                                                                          effectiveTime,
                                                                          methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       methodName,
                                       entities);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId caller's userId
     * @param startingEntity the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param attachmentEntityTypeName requested type name for retrieved entities
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElement> getRelatedMetadataElements(String              userId,
                                                                   EntityDetail        startingEntity,
                                                                   int                 startingAtEnd,
                                                                   String              relationshipTypeName,
                                                                   String              attachmentEntityTypeName,
                                                                   List<ElementStatus> limitResultsByStatus,
                                                                   Date                asOfTime,
                                                                   String              sequencingProperty,
                                                                   SequencingOrder     sequencingOrder,
                                                                   boolean             forLineage,
                                                                   boolean             forDuplicateProcessing,
                                                                   Date                effectiveTime,
                                                                   int                 startFrom,
                                                                   int                 pageSize,
                                                                   String              methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(startingEntity, guidParameterName, methodName);

        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            null,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }

        int selectionEnd = 0;

        if (startingAtEnd == 1)
        {
            selectionEnd = 2;
        }
        else if (startingAtEnd == 2)
        {
            selectionEnd = 1;
        }

        /*
         * Retrieve the relationships (no security calls)
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity,
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    relationshipTypeGUID,
                                                                                    relationshipTypeName,
                                                                                    selectionEnd,
                                                                                    getInstanceStatuses(limitResultsByStatus),
                                                                                    asOfTime,
                                                                                    getSequencingOrder(sequencingOrder),
                                                                                    sequencingProperty,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships == null)
        {
            return null;
        }

        /*
         * Retrieve all of the entities linked to the relationships.  This is done as a single retrieve
         * to minimise the calls to the repositories.  It also performs security checks
         */
        Map<String, EntityDetail> retrievedEntities = this.getValidatedEntities(userId,
                                                                                startingEntity.getGUID(),
                                                                                startingEntity.getType().getTypeDefName(),
                                                                                relationships,
                                                                                this.getEntityTypeName(attachmentEntityTypeName),
                                                                                null,
                                                                                null,
                                                                                selectionEnd,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        List<RelatedMetadataElement> results = new ArrayList<>();

        if (retrievedEntities != null)
        {
            retrievedEntities.put(startingEntity.getGUID(), startingEntity);

            /*
             * Only return relationships that link to approved entities.
             */
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy otherEnd = repositoryHandler.getOtherEnd(startingEntity.getGUID(), relationship);

                    if (otherEnd != null)
                    {
                        /*
                         * Do not return the same entity twice (may occur if there are duplicates).
                         */
                        EntityDetail retrievedEntity = retrievedEntities.get(otherEnd.getGUID());

                        if (retrievedEntity != null)
                        {
                            results.add(relatedElementConverter.getNewBean(RelatedMetadataElement.class,
                                                                           retrievedEntity,
                                                                           relationship,
                                                                           methodName));
                        }
                    }
                }
            }
        }

        return results;
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataRelationship> getMetadataElementRelationships(String              userId,
                                                                          String              metadataElementAtEnd1GUID,
                                                                          String              relationshipTypeName,
                                                                          String              metadataElementAtEnd2GUID,
                                                                          List<ElementStatus> limitResultsByStatus,
                                                                          Date                asOfTime,
                                                                          String              sequencingProperty,
                                                                          SequencingOrder     sequencingOrder,
                                                                          boolean             forLineage,
                                                                          boolean             forDuplicateProcessing,
                                                                          Date                effectiveTime,
                                                                          int                 startFrom,
                                                                          int                 pageSize,
                                                                          String              methodName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String end1ParameterName = "metadataElementAtEnd1GUID";

        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            null,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }

        List<Relationship> relationships = super.getAttachmentLinks(userId,
                                                                    metadataElementAtEnd1GUID,
                                                                    end1ParameterName,
                                                                    OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                    relationshipTypeGUID,
                                                                    relationshipTypeName,
                                                                    metadataElementAtEnd2GUID,
                                                                    OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                    0,
                                                                    getInstanceStatuses(limitResultsByStatus),
                                                                    asOfTime,
                                                                    getSequencingOrder(sequencingOrder),
                                                                    sequencingProperty,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    startFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if (relationships != null)
        {
            List<OpenMetadataRelationship> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                results.add(openMetadataRelationshipsConverter.getNewRelationshipBean(OpenMetadataRelationship.class,
                                                                                      relationship,
                                                                                      methodName));
            }

            return results;
        }

        return null;
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findMetadataElements(String                userId,
                                        String                metadataElementTypeName,
                                        List<String>          metadataElementSubtypeName,
                                        SearchProperties      searchProperties,
                                        List<ElementStatus>   limitResultsByStatus,
                                        SearchClassifications searchClassifications,
                                        Date                  asOfTime,
                                        String                sequencingProperty,
                                        SequencingOrder       sequencingOrder,
                                        boolean               forLineage,
                                        boolean               forDuplicateProcessing,
                                        Date                  effectiveTime,
                                        int                   startingFrom,
                                        int                   pageSize,
                                        String                methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return super.findBeans(userId,
                               metadataElementTypeName,
                               metadataElementSubtypeName,
                               this.getSearchProperties(searchProperties),
                               this.getInstanceStatuses(limitResultsByStatus),
                               this.getSearchClassifications(searchClassifications),
                               asOfTime,
                               sequencingProperty,
                               this.getSequencingOrder(sequencingOrder),
                               forLineage,
                               forDuplicateProcessing,
                               startingFrom,
                               pageSize,
                               effectiveTime,
                               methodName);
    }


    /**
     * Convert the omf searchProperties to OMRS searchProperties.
     *
     * @param omfSearchProperties omf searchProperties
     * @return OMRS searchProperties
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties
                   getSearchProperties(SearchProperties omfSearchProperties) throws InvalidParameterException
    {
        if (omfSearchProperties != null)
        {
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties
                    omrsSearchProperties = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties();

            omrsSearchProperties.setConditions(getPropertyConditions(omfSearchProperties.getConditions()));
            omrsSearchProperties.setMatchCriteria(getMatchCriteria(omfSearchProperties.getMatchCriteria()));

            return omrsSearchProperties;
        }

        return null;
    }


    /**
     * Convert the omf searchClassifications to OMRS searchClassifications.
     *
     * @param omfSearchClassifications omf searchClassifications
     * @return OMRS searchClassifications
     * @throws InvalidParameterException invalid property specification
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications
                 getSearchClassifications(SearchClassifications omfSearchClassifications) throws InvalidParameterException
    {
        if (omfSearchClassifications != null)
        {
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications
                    omrsSearchClassifications = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications();

            if ((omfSearchClassifications.getConditions() != null) && (! omfSearchClassifications.getConditions().isEmpty()))
            {
                List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition>
                        omrsClassificationConditions = new ArrayList<>();

                for (ClassificationCondition omfClassificationCondition : omfSearchClassifications.getConditions())
                {
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition
                            omrsClassificationCondition = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.ClassificationCondition();

                    omrsClassificationCondition.setName(omfClassificationCondition.getName());
                    omrsClassificationCondition.setMatchProperties(this.getSearchProperties(omfClassificationCondition.getSearchProperties()));

                    omrsClassificationConditions.add(omrsClassificationCondition);
                }

                if (! omrsClassificationConditions.isEmpty())
                {
                    omrsSearchClassifications.setConditions(omrsClassificationConditions);
                }
            }

            omrsSearchClassifications.setMatchCriteria(this.getMatchCriteria(omfSearchClassifications.getMatchCriteria()));

            return  omrsSearchClassifications;
        }

        return null;
    }



    /**
     * Convert the omf propertyConditions to OMRS propertyConditions.
     *
     * @param omfPropertyConditions omf propertyConditions
     * @return OMRS propertyConditions
     * @throws InvalidParameterException invalid property specification
     */
    private List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition>
           getPropertyConditions(List<PropertyCondition> omfPropertyConditions) throws InvalidParameterException
    {
        if ((omfPropertyConditions != null) && (! omfPropertyConditions.isEmpty()))
        {
            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition>
                    omrsPropertyConditions = new ArrayList<>();
            for (PropertyCondition propertyCondition : omfPropertyConditions)
            {
                if (propertyCondition != null)
                {
                    org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition
                            omrsPropertyCondition = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition();

                    omrsPropertyCondition.setProperty(propertyCondition.getProperty());
                    omrsPropertyCondition.setOperator(this.getPropertyComparisonOperator(propertyCondition.getOperator()));
                    omrsPropertyCondition.setValue(this.getInstancePropertyValue(propertyCondition.getValue()));
                    omrsPropertyCondition.setNestedConditions(this.getSearchProperties(propertyCondition.getNestedConditions()));
                    omrsPropertyConditions.add(omrsPropertyCondition);
                }
            }
            return omrsPropertyConditions;
        }

        return null;
    }


    /**
     * Convert the omf property value to the OMRS InstancePropertyValue.
     *
     * @param propertyValue omf property value
     * @return OMRS InstancePropertyValue
     * @throws InvalidParameterException invalid property specification
     */
    private InstancePropertyValue getInstancePropertyValue(PropertyValue propertyValue) throws InvalidParameterException
    {
        if (propertyValue != null)
        {
            final String methodName = "getInstancePropertyValue";
            final String typeParameterName = "propertyValue.getTypeName()";

            AttributeTypeDef typeDef = repositoryHelper.getAttributeTypeDefByName(serviceName, propertyValue.getTypeName());

            invalidParameterHandler.validateObject(typeDef, typeParameterName, methodName);

            if (propertyValue instanceof ArrayTypePropertyValue omfPropertyValue)
            {
                return this.getArrayPropertyValue(typeDef,
                                                  omfPropertyValue.getArrayCount(),
                                                  propertyHelper.getElementPropertiesAsMap(omfPropertyValue.getArrayValues()));
            }
            else if (propertyValue instanceof EnumTypePropertyValue omfPropertyValue)
            {
                return this.getEnumPropertyValue(typeDef, omfPropertyValue.getSymbolicName());
            }
            else if (propertyValue instanceof MapTypePropertyValue omfPropertyValue)
            {
                return this.getMapPropertyValue(typeDef, propertyHelper.getElementPropertiesAsMap(omfPropertyValue.getMapValues()));
            }
            else if (propertyValue instanceof PrimitiveTypePropertyValue omfPropertyValue)
            {
                return this.getPrimitivePropertyValue(typeDef, omfPropertyValue.getPrimitiveTypeCategory(), omfPropertyValue.getPrimitiveValue());
            }
            else if (propertyValue instanceof StructTypePropertyValue omfPropertyValue)
            {
                return this.getStructPropertyValue(typeDef, propertyHelper.getElementPropertiesAsMap(omfPropertyValue.getAttributes()));
            }
        }

        return null;
    }


    /**
     * Create an OMRS instance property value from an omf property value.
     *
     * @param typeDef property's type definition
     * @param arrayCount number of elements in the array
     * @param arrayValues values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private ArrayPropertyValue getArrayPropertyValue(AttributeTypeDef     typeDef,
                                                     int                  arrayCount,
                                                     Map<String, Object>  arrayValues) throws InvalidParameterException
    {
        final String methodName = "getArrayPropertyValue";

        ArrayPropertyValue
                omrsPropertyValue = new ArrayPropertyValue();

        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ARRAY);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());
        omrsPropertyValue.setArrayCount(arrayCount);

        try
        {
            omrsPropertyValue.setArrayValues(repositoryHelper.addPropertyMapToInstance(serviceName, null, arrayValues, methodName));
        }
        catch (OMFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from an omf property value.
     *
     * @param typeDef  property's type definition
     * @param symbolicName enum value
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private EnumPropertyValue getEnumPropertyValue(AttributeTypeDef typeDef,
                                                   String           symbolicName) throws InvalidParameterException
    {
        final String methodName                = "getEnumPropertyValue";
        final String symbolicNameParameterName = "symbolicName";
        final String propertyParameterName     = "omrsPropertyValue";

        invalidParameterHandler.validateName(symbolicName, symbolicNameParameterName, methodName);
        EnumPropertyValue omrsPropertyValue = null;

        if (typeDef instanceof EnumDef enumDef)
        {
            List<EnumElementDef> enumElementDefs = enumDef.getElementDefs();

            if ((enumElementDefs != null) && (! enumElementDefs.isEmpty()))
            {
                for (EnumElementDef enumElementDef : enumElementDefs)
                {
                    if ((enumElementDef != null) &&
                            (enumElementDef.getValue() != null) &&
                            (symbolicName.equalsIgnoreCase(enumElementDef.getValue())))
                    {
                        omrsPropertyValue = new EnumPropertyValue();

                        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
                        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.ENUM);
                        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
                        omrsPropertyValue.setTypeName(typeDef.getName());
                        omrsPropertyValue.setSymbolicName(enumElementDef.getValue());
                        omrsPropertyValue.setOrdinal(enumElementDef.getOrdinal());
                        omrsPropertyValue.setDescription(enumElementDef.getDescription());
                    }
                }
            }
        }

        invalidParameterHandler.validateObject(omrsPropertyValue, propertyParameterName, methodName);
        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from an omf property value.
     *
     * @param typeDef property's type definition
     * @param mapValues values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private MapPropertyValue getMapPropertyValue(AttributeTypeDef    typeDef,
                                                 Map<String, Object> mapValues) throws InvalidParameterException
    {
        final String methodName = "getMapPropertyValue";

        MapPropertyValue omrsPropertyValue = new MapPropertyValue();

        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.MAP);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());

        try
        {
            omrsPropertyValue.setMapValues(repositoryHelper.addPropertyMapToInstance(serviceName, null, mapValues, methodName));
        }
        catch (OMFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Create an OMRS instance property value from an omf property value.
     *
     * @param typeDef property's type definition
     * @param primitiveTypeCategory value type
     * @param primitiveValue value
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(AttributeTypeDef      typeDef,
                                                             PrimitiveTypeCategory primitiveTypeCategory,
                                                             Object                primitiveValue) throws InvalidParameterException
    {
        final String methodName = "getPrimitivePropertyValue";
        final String valueParameterName = "primitiveValue";
        final String categoryParameterName = "primitiveTypeCategory";

        invalidParameterHandler.validateObject(primitiveTypeCategory, categoryParameterName, methodName);
        invalidParameterHandler.validateObject(primitiveValue, valueParameterName, methodName);

        PrimitivePropertyValue
                omrsPropertyValue = new PrimitivePropertyValue();

        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.PRIMITIVE);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());
        omrsPropertyValue.setPrimitiveDefCategory(this.getPrimitiveDefCategory(primitiveTypeCategory));
        omrsPropertyValue.setPrimitiveValue(primitiveValue);

        return omrsPropertyValue;
    }


    /**
     * Convert the omf primitive def category to the OMRS version
     *
     * @param omfPrimitiveTypeCategory omf version
     * @return OMRS version
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory
            getPrimitiveDefCategory(PrimitiveTypeCategory omfPrimitiveTypeCategory)
    {
        if (omfPrimitiveTypeCategory != null)
        {
            return switch (omfPrimitiveTypeCategory)
                           {
                               case OM_PRIMITIVE_TYPE_UNKNOWN ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
                               case OM_PRIMITIVE_TYPE_BOOLEAN ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN;
                               case OM_PRIMITIVE_TYPE_BYTE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE;
                               case OM_PRIMITIVE_TYPE_CHAR ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR;
                               case OM_PRIMITIVE_TYPE_SHORT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT;
                               case OM_PRIMITIVE_TYPE_INT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT;
                               case OM_PRIMITIVE_TYPE_LONG ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG;
                               case OM_PRIMITIVE_TYPE_FLOAT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT;
                               case OM_PRIMITIVE_TYPE_DOUBLE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE;
                               case OM_PRIMITIVE_TYPE_BIGINTEGER ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER;
                               case OM_PRIMITIVE_TYPE_BIGDECIMAL ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;
                               case OM_PRIMITIVE_TYPE_STRING ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;
                               case OM_PRIMITIVE_TYPE_DATE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE;
                           };
        }

        return null;
    }


    /**
     * Create an OMRS instance property value from an omf property value.
     *
     * @param typeDef property's type definition
     * @param attributes values in the array
     * @return OMRS property value
     * @throws InvalidParameterException invalid property specification
     */
    private StructPropertyValue getStructPropertyValue(AttributeTypeDef    typeDef,
                                                       Map<String, Object> attributes) throws InvalidParameterException
    {
        final String methodName = "getStructPropertyValue";

        StructPropertyValue
                omrsPropertyValue = new StructPropertyValue();

        omrsPropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        omrsPropertyValue.setInstancePropertyCategory(InstancePropertyCategory.STRUCT);
        omrsPropertyValue.setTypeGUID(typeDef.getGUID());
        omrsPropertyValue.setTypeName(typeDef.getName());

        try
        {
            omrsPropertyValue.setAttributes(repositoryHelper.addPropertyMapToInstance(serviceName, null, attributes, methodName));
        }
        catch (OMFCheckedExceptionBase error)
        {
            final String parameterName = "searchProperties";

            throw new InvalidParameterException(error, parameterName);
        }

        return omrsPropertyValue;
    }


    /**
     * Convert the omf matchCriteria to OMRS matchCriteria.
     *
     * @param omfMatchCriteria omf matchCriteria
     * @return OMRS matchCriteria
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria
                  getMatchCriteria(MatchCriteria omfMatchCriteria)
    {
        if (omfMatchCriteria != null)
        {
            return switch (omfMatchCriteria)
                           {
                               case ALL ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ALL;
                               case ANY ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.ANY;
                               case NONE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria.NONE;
                           };
        }

        return null;
    }


    /**
     * Convert the omf propertyComparisonOperator to the OMRS propertyComparisonOperator.
     *
     * @param omfPropertyComparisonOperator omf propertyComparisonOperator
     * @return OMRS propertyComparisonOperator
     */
    private org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator
                 getPropertyComparisonOperator(PropertyComparisonOperator omfPropertyComparisonOperator)
    {
        if (omfPropertyComparisonOperator != null)
        {
            return switch (omfPropertyComparisonOperator)
                           {
                               case EQ ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.EQ;
                               case NEQ ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.NEQ;
                               case LT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LT;
                               case LTE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LTE;
                               case GT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.GT;
                               case GTE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.GTE;
                               case IN ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.IN;
                               case IS_NULL ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.IS_NULL;
                               case NOT_NULL ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.NOT_NULL;
                               case LIKE ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator.LIKE;
                           };
        }

        return null;
    }


    /**
     * Convert the omf sequencingOrder to the OMRS sequencingOrder.
     *
     * @param omfSequencingOrder omf sequencingOrder
     * @return OMRS sequencingOrder
     */
    public org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder
                getSequencingOrder(SequencingOrder omfSequencingOrder)
    {
        if (omfSequencingOrder != null)
        {
            return switch (omfSequencingOrder)
                           {
                               case ANY ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.ANY;
                               case GUID ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.GUID;
                               case CREATION_DATE_RECENT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.CREATION_DATE_RECENT;
                               case CREATION_DATE_OLDEST ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.CREATION_DATE_OLDEST;
                               case LAST_UPDATE_RECENT ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.LAST_UPDATE_RECENT;
                               case LAST_UPDATE_OLDEST ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.LAST_UPDATE_OLDEST;
                               case PROPERTY_ASCENDING ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.PROPERTY_ASCENDING;
                               case PROPERTY_DESCENDING ->
                                       org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.PROPERTY_DESCENDING;
                           };
        }

        return null;
    }

    /**
     * Convert a list of omf ElementStatuses into a list of OMRS InstanceStatuses.
     *
     * @param elementStatuses omf ElementStatuses
     * @return OMRS InstanceStatuses
     */
    public List<InstanceStatus> getInstanceStatuses(List<ElementStatus> elementStatuses)
    {
        if (elementStatuses != null)
        {
            List<InstanceStatus> instanceStatuses = new ArrayList<>();

            for(ElementStatus elementStatus : elementStatuses)
            {
                if (elementStatus != null)
                {
                    instanceStatuses.add(getInstanceStatus(elementStatus));
                }
            }

            if (! instanceStatuses.isEmpty())
            {
                return instanceStatuses;
            }
        }

        return null;
    }


    /**
     * Convert an omf ElementStatus into an OMRS InstanceStatus.
     *
     * @param elementStatus omf ElementStatus
     * @return OMRS InstanceStatus
     */
    private InstanceStatus getInstanceStatus(ElementStatus elementStatus)
    {
        if (elementStatus != null)
        {
            return switch (elementStatus)
                           {
                               case UNKNOWN -> InstanceStatus.UNKNOWN;
                               case ACTIVE -> InstanceStatus.ACTIVE;
                               case INVALID -> InstanceStatus.INVALID;
                               case DELETED -> InstanceStatus.DELETED;
                               case OTHER -> InstanceStatus.OTHER;
                           };
        }

        return null;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId caller's userId
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public  List<OpenMetadataRelationship> findRelationshipsBetweenMetadataElements(String              userId,
                                                                                    String              relationshipTypeName,
                                                                                    SearchProperties    searchProperties,
                                                                                    List<ElementStatus> limitResultsByStatus,
                                                                                    Date                asOfTime,
                                                                                    String              sequencingProperty,
                                                                                    SequencingOrder     sequencingOrder,
                                                                                    boolean             forLineage,
                                                                                    boolean             forDuplicateProcessing,
                                                                                    Date                effectiveTime,
                                                                                    int                 startFrom,
                                                                                    int                 pageSize,
                                                                                    String              methodName) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        List<Relationship> relationships = this.findAttachmentLinks(userId,
                                                                    relationshipTypeName,
                                                                    this.getSearchProperties(searchProperties),
                                                                    this.getInstanceStatuses(limitResultsByStatus),
                                                                    asOfTime,
                                                                    sequencingProperty,
                                                                    this.getSequencingOrder(sequencingOrder),
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    startFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if (relationships != null)
        {
            List<OpenMetadataRelationship> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    results.add(openMetadataRelationshipsConverter.getNewRelationshipBean(OpenMetadataRelationship.class,
                                                                                          relationship,
                                                                                          methodName));
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
     * Retrieve the relationships that are of the correct type.
     *
     * @param userId caller's userId
     * @param typeName   specific type of element (and their subtypes) to retrieve
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataRelationship> getRelationshipsByType(String              userId,
                                                                 String              typeName,
                                                                 boolean             forLineage,
                                                                 boolean             forDuplicateProcessing,
                                                                 List<ElementStatus> limitResultsByStatus,
                                                                 Date                asOfTime,
                                                                 String              sequencingProperty,
                                                                 SequencingOrder     sequencingOrder,
                                                                 Date                effectiveTime,
                                                                 int                 startFrom,
                                                                 int                 pageSize,
                                                                 String              methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String typeParameterName = "typeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(typeName, typeParameterName, methodName);

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   null,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);


        List<Relationship> relationships = repositoryHandler.getRelationshipsForType(userId,
                                                                                     typeGUID,
                                                                                     typeName,
                                                                                     this.getInstanceStatuses(limitResultsByStatus),
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     asOfTime,
                                                                                     sequencingProperty,
                                                                                     getSequencingOrder(sequencingOrder),
                                                                                     effectiveTime,
                                                                                     methodName);

        if (relationships != null)
        {
            List<OpenMetadataRelationship> results = new ArrayList<>();
            List<String>                   validatedAnchorGUIDs = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    try
                    {
                        validateRelationship(userId,
                                             relationship,
                                             validatedAnchorGUIDs,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);

                        /*
                         * Entity is added if validate anchor relationship does not throw an exception.
                         */
                        results.add(openMetadataRelationshipsConverter.getNewRelationshipBean(OpenMetadataRelationship.class,
                                                                                              relationship,
                                                                                              methodName));
                    }
                    catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException notVisible)
                    {
                        log.debug("Skip relationship " + relationship.getGUID());
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
     * Convert OMRS relationships into OpenMetadataRelationships
     *
     * @param relationships relationships retrieved from the repository
     * @param methodName calling method
     * @return list of relationships or null
     * @throws PropertyServerException unable to convert relationship
     */
    public List<OpenMetadataRelationship> convertOpenMetadataRelationships(List<Relationship> relationships,
                                                                           String             methodName) throws PropertyServerException
    {
        if (relationships != null)
        {
            List<OpenMetadataRelationship> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    results.add(openMetadataRelationshipsConverter.getNewRelationshipBean(OpenMetadataRelationship.class,
                                                                                          relationship,
                                                                                          methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor flag to indicate if the new entity should be anchored to itself
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                            userId,
                                               String                            externalSourceGUID,
                                               String                            externalSourceName,
                                               String                            metadataElementTypeName,
                                               ElementStatus                     initialStatus,
                                               Map<String, NewElementProperties> initialClassifications,
                                               String                            anchorGUID,
                                               boolean                           isOwnAnchor,
                                               String                            anchorScopeGUID,
                                               NewElementProperties              properties,
                                               String                            parentGUID,
                                               String                            parentRelationshipTypeName,
                                               NewElementProperties              parentRelationshipProperties,
                                               boolean                           parentAtEnd1,
                                               boolean                           forLineage,
                                               boolean                           forDuplicateProcessing,
                                               Date                              effectiveTime,
                                               String                            methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String elementTypeParameterName = "metadataElementTypeName";
        final String anchorGUIDParameterName  = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(metadataElementTypeName, elementTypeParameterName, methodName);

        String metadataElementTypeGUID = invalidParameterHandler.validateTypeName(metadataElementTypeName,
                                                                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

        if (repositoryHelper.isTypeOf(serviceName, metadataElementTypeName, OpenMetadataType.REFERENCEABLE.typeName))
        {
            String qualifiedName = propertyHelper.getStringProperty(serviceName,
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                    properties,
                                                                    methodName);

            invalidParameterHandler.validateName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        }

        String parentRelationshipTypeGUID = null;
        if (parentGUID != null)
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(parentRelationshipTypeName, parentRelationshipTypeNameParameterName, methodName);

            parentRelationshipTypeGUID = invalidParameterHandler.validateTypeName(parentRelationshipTypeName,
                                                                                  null,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);
        }

        MetadataElementBuilder builder;

        if (properties != null)
        {
            builder = new MetadataElementBuilder(metadataElementTypeGUID,
                                                 metadataElementTypeName,
                                                 getElementPropertiesAsOMRSMap(properties),
                                                 this.getInstanceStatus(initialStatus),
                                                 properties.getEffectiveFrom(),
                                                 properties.getEffectiveTo(),
                                                 repositoryHelper,
                                                 serviceName,
                                                 serverName);
        }
        else
        {
            builder = new MetadataElementBuilder(metadataElementTypeGUID,
                                                 metadataElementTypeName,
                                                 null,
                                                 this.getInstanceStatus(initialStatus),
                                                 null,
                                                 null,
                                                 repositoryHelper,
                                                 serviceName,
                                                 serverName);
        }

        if (initialClassifications != null)
        {
            for (String classificationName : initialClassifications.keySet())
            {
                ElementProperties classificationProperties = initialClassifications.get(classificationName);

                Map<String,InstancePropertyValue> instancePropertyValueMap = null;
                if (classificationProperties != null)
                {
                    instancePropertyValueMap = this.getElementPropertiesAsOMRSMap(classificationProperties);
                }

                try
                {
                    Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                          null,
                                                                                          null,
                                                                                          InstanceProvenanceType.LOCAL_COHORT,
                                                                                          userId,
                                                                                          classificationName,
                                                                                          metadataElementTypeName,
                                                                                          ClassificationOrigin.ASSIGNED,
                                                                                          null,
                                                                                          builder.getInstanceProperties(instancePropertyValueMap, null, null));
                    builder.setClassification(classification);
                }
                catch (TypeErrorException error)
                {
                    errorHandler.handleUnsupportedType(error, methodName, classificationName);
                }
            }
        }

        /*
         * If an anchor entity is supplied, make sure it is saved in the builder
         */
        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = setUpAnchorsClassificationFromAnchor(userId,
                                                                             anchorGUID,
                                                                             anchorGUIDParameterName,
                                                                             anchorScopeGUID,
                                                                             builder,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);
            if (anchorEntity != null)
            {
                /*
                 * This method will throw an exception if the asset is not in the supported zones - it will look like
                 * the element is not known.
                 */
                invalidParameterHandler.validateElementInSupportedZone(anchorEntity.getGUID(),
                                                                       anchorGUIDParameterName,
                                                                       this.getEntityZones(anchorEntity),
                                                                       securityVerifier.getSupportedZones(userId, anchorEntity.getType().getTypeDefName(), methodName),
                                                                       serviceName,
                                                                       methodName);
            }
        }


        String metadataElementGUID = this.createBeanInRepository(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 metadataElementTypeGUID,
                                                                 metadataElementTypeName,
                                                                 this.getDomainName(metadataElementTypeName),
                                                                 anchorScopeGUID,
                                                                 builder,
                                                                 isOwnAnchor,
                                                                 effectiveTime,
                                                                 methodName);

        String pathName = propertyHelper.getStringProperty(serviceName,
                                                           OpenMetadataProperty.PATH_NAME.name,
                                                           properties,
                                                           methodName);
        createParentRelationships(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  metadataElementGUID,
                                  metadataElementTypeName,
                                  pathName,
                                  parentGUID,
                                  parentRelationshipTypeGUID,
                                  parentRelationshipProperties,
                                  parentAtEnd1,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);

        return metadataElementGUID;
    }


    /**
     * Add parent relationships.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementTypeName type name of the new metadata element
     * @param metadataElementGUID     newly created element
     * @param pathName                path name property of element (if available)
     * @param parentGUID              requested parent
     * @param parentRelationshipTypeGUID requested parent relationship type GUID
     * @param parentRelationshipProperties  requested parent relationship properties
     * @param parentAtEnd1            which end to attach the parent
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void createParentRelationships(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               metadataElementGUID,
                                           String               metadataElementTypeName,
                                           String               pathName,
                                           String               parentGUID,
                                           String               parentRelationshipTypeGUID,
                                           NewElementProperties parentRelationshipProperties,
                                           boolean              parentAtEnd1,
                                           boolean              forLineage,
                                           boolean              forDuplicateProcessing,
                                           Date                 effectiveTime,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String parentGUIDParameterName = "parentGUID";
        final String metadataElementGUIDParameterName = "metadataElementGUID";

        if (metadataElementGUID != null)
        {
            if (parentGUID != null)
            {
                MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

                InstanceProperties relationshipProperties = null;

                if (parentRelationshipProperties != null)
                {
                    relationshipProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(parentRelationshipProperties),
                                                                           parentRelationshipProperties.getEffectiveFrom(),
                                                                           parentRelationshipProperties.getEffectiveTo());
                }

                if (parentAtEnd1)
                {
                    uncheckedLinkElementToElement(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  parentGUID,
                                                  parentGUIDParameterName,
                                                  metadataElementGUID,
                                                  metadataElementGUIDParameterName,
                                                  parentRelationshipTypeGUID,
                                                  relationshipProperties,
                                                  methodName);
                }
                else
                {
                    uncheckedLinkElementToElement(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  metadataElementGUID,
                                                  metadataElementGUIDParameterName,
                                                  parentGUID,
                                                  parentGUIDParameterName,
                                                  parentRelationshipTypeGUID,
                                                  relationshipProperties,
                                                  methodName);
                }
            }

            if ((repositoryHelper.isTypeOf(serviceName, metadataElementTypeName, OpenMetadataType.DATA_FILE.typeName)) ||
                    (repositoryHelper.isTypeOf(serviceName, metadataElementTypeName, OpenMetadataType.FILE_FOLDER.typeName)))
            {
                if (pathName != null)
                {
                    filesAndFoldersHandler.addFileAssetPath(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            metadataElementGUID,
                                                            metadataElementGUIDParameterName,
                                                            metadataElementTypeName,
                                                            pathName,
                                                            OpenMetadataProperty.PATH_NAME.name,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime,
                                                            methodName);
                }
            }
        }
    }


    /**
     * Create a new metadata element in the metadata store from a template.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param suppliedMetadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor flag to indicate if the new entity should be anchored to itself
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param allowRetrieve can an existing element be returned if it exists
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc.)
     * @param templateProperties properties of the new metadata element
     * @param placeholderPropertyValues values to override placeholder variables in the template
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String                         userId,
                                                    String                         externalSourceGUID,
                                                    String                         externalSourceName,
                                                    String                         suppliedMetadataElementTypeName,
                                                    String                         anchorGUID,
                                                    boolean                        isOwnAnchor,
                                                    String                         anchorScopeGUID,
                                                    boolean                        allowRetrieve,
                                                    Date                           effectiveFrom,
                                                    Date                           effectiveTo,
                                                    String                         templateGUID,
                                                    ElementProperties              templateProperties,
                                                    Map<String, String>            placeholderPropertyValues,
                                                    String                         parentGUID,
                                                    String                         parentRelationshipTypeName,
                                                    NewElementProperties           parentRelationshipProperties,
                                                    boolean                        parentAtEnd1,
                                                    boolean                        forLineage,
                                                    boolean                        forDuplicateProcessing,
                                                    Date                           effectiveTime,
                                                    String                         methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String templateGUIDParameterName = "templateGUID";
        final String anchorGUIDParameterName = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        String parentRelationshipTypeGUID = null;
        if (parentGUID != null)
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(parentRelationshipTypeName, parentRelationshipTypeNameParameterName, methodName);

            parentRelationshipTypeGUID = invalidParameterHandler.validateTypeName(parentRelationshipTypeName,
                                                                                  null,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);
        }

        String metadataElementTypeName = suppliedMetadataElementTypeName;

        if (metadataElementTypeName == null)
        {
            EntityDetail templateEntity = this.getEntityFromRepository(userId,
                                                                       templateGUID,
                                                                       templateGUIDParameterName,
                                                                       OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                       null,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

            if (templateEntity != null)
            {
                metadataElementTypeName = templateEntity.getType().getTypeDefName();
            }
        }


        String metadataElementTypeGUID = invalidParameterHandler.validateTypeName(metadataElementTypeName,
                                                                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(metadataElementTypeGUID,
                                                                    metadataElementTypeName,
                                                                    getElementPropertiesAsOMRSMap(templateProperties),
                                                                    null,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        /*
         * If an anchor entity is supplied, make sure it is saved in the builder
         */
        if (anchorGUID != null)
        {
            setUpAnchorsClassificationFromAnchor(userId,
                                                 anchorGUID,
                                                 anchorGUIDParameterName,
                                                 anchorScopeGUID,
                                                 builder,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
        }
        else if (isOwnAnchor)
        {
            /*
             * Set the anchorGUID
             */
            builder.setAnchors(userId,
                               null,
                               metadataElementTypeName,
                               this.getDomainName(metadataElementTypeName),
                               anchorScopeGUID,
                               methodName);
        }

        /*
         * This creates the new bean and all the elements anchored to it
         */
        String metadataElementGUID = this.createBeanFromTemplate(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 templateGUID,
                                                                 templateGUIDParameterName,
                                                                 metadataElementTypeGUID,
                                                                 metadataElementTypeName,
                                                                 null,
                                                                 null,
                                                                 builder,
                                                                 true,
                                                                 false,
                                                                 allowRetrieve,
                                                                 placeholderPropertyValues,
                                                                 methodName);

        /*
         * This is where we add the parent relationship.  However, due to the "allowRetrieve" flag,
         * it is possible that the entity GUID returned is for an entity that has been around for a while,
         * and already has the parent attached.  So the code below checks that the parent is not already there.
         */
        if (metadataElementGUID != null)
        {
            final String metadataElementGUIDParameterName = "metadataElementGUID";

            EntityDetail entityDetail = this.getEntityFromRepository(userId,
                                                                     metadataElementGUID,
                                                                     metadataElementGUIDParameterName,
                                                                     metadataElementTypeName,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

            String pathName = repositoryHelper.getStringProperty(serviceName,
                                                                 OpenMetadataProperty.PATH_NAME.name,
                                                                 entityDetail.getProperties(),
                                                                 methodName);
            List<Relationship> existingRelationships = null;

            if (parentGUID != null)
            {
                if (allowRetrieve)
                {
                    int attachmentEnd = 2;
                    if (parentAtEnd1)
                    {
                        attachmentEnd = 1;
                    }

                    existingRelationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                              entityDetail,
                                                                                              metadataElementTypeName,
                                                                                              parentGUID,
                                                                                              parentRelationshipTypeGUID,
                                                                                              parentRelationshipTypeName,
                                                                                              attachmentEnd,
                                                                                              null,
                                                                                              null,
                                                                                              org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder.CREATION_DATE_RECENT,
                                                                                              null,
                                                                                              false,
                                                                                              false,
                                                                                              effectiveFrom,
                                                                                              effectiveTo,
                                                                                              true,
                                                                                              methodName);
                }
            }

            if ((existingRelationships == null) || (existingRelationships.isEmpty()))
            {
                /*
                 * This is the first time through - use the caller's values
                 */
                createParentRelationships(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          metadataElementGUID,
                                          metadataElementTypeName,
                                          pathName,
                                          parentGUID,
                                          parentRelationshipTypeGUID,
                                          parentRelationshipProperties,
                                          parentAtEnd1,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }
            else
            {
                /*
                 * Ignore the creation of the parent.
                 */
                createParentRelationships(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          metadataElementGUID,
                                          metadataElementTypeName,
                                          pathName,
                                          null,
                                          null,
                                          null,
                                          true,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }
        }

        return metadataElementGUID;
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the isMergeUpdate flag.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param isMergeUpdate flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param properties new properties for the metadata element
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param methodName calling method
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public boolean updateMetadataElementInStore(String            userId,
                                                String            externalSourceGUID,
                                                String            externalSourceName,
                                                String            metadataElementGUID,
                                                boolean           isMergeUpdate,
                                                boolean           forLineage,
                                                boolean           forDuplicateProcessing,
                                                ElementProperties properties,
                                                Date              effectiveTime,
                                                String            methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        MetadataElementBuilder builder = new MetadataElementBuilder(getElementPropertiesAsOMRSMap(properties),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        return this.updateBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                           OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           builder.getInstanceProperties(methodName),
                                           isMergeUpdate,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        userId,
                                                   String        externalSourceGUID,
                                                   String        externalSourceName,
                                                   String        metadataElementGUID,
                                                   ElementStatus newElementStatus,
                                                   boolean       forLineage,
                                                   boolean       forDuplicateProcessing,
                                                   Date          effectiveTime,
                                                   String        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String statusParameterName = "newElementStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.updateBeanStatusInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                           OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           this.getInstanceStatus(newElementStatus),
                                           statusParameterName,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the zone membership to increase its visibility.  The publishZones  are defined in the user directory.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void publishMetadataElement(String        userId,
                                       String        externalSourceGUID,
                                       String        externalSourceName,
                                       String        metadataElementGUID,
                                       boolean       forLineage,
                                       boolean       forDuplicateProcessing,
                                       Date          effectiveTime,
                                       String        methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.publishElement(userId,
                             externalSourceGUID,
                             externalSourceName,
                             metadataElementGUID,
                             guidParameterName,
                             OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                             forLineage,
                             forDuplicateProcessing,
                             effectiveTime,
                             methodName);
    }


    /**
     * Update the zone membership to reduce its visibility.  The defaultZones are defined in the user directory.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void withdrawMetadataElement(String        userId,
                                        String        externalSourceGUID,
                                        String        externalSourceName,
                                        String        metadataElementGUID,
                                        boolean       forLineage,
                                        boolean       forDuplicateProcessing,
                                        Date          effectiveTime,
                                        String        methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.withdrawElement(userId,
                              externalSourceGUID,
                              externalSourceName,
                              metadataElementGUID,
                              guidParameterName,
                              OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        userId,
                                                        String        externalSourceGUID,
                                                        String        externalSourceName,
                                                        String        metadataElementGUID,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo,
                                                        Date          effectiveTime,
                                                        String        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.updateBeanEffectivityDates(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         metadataElementGUID,
                                         guidParameterName,
                                         OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                         OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void deleteMetadataElementInStore(String       userId,
                                              String       externalSourceGUID,
                                              String       externalSourceName,
                                              String       metadataElementGUID,
                                              boolean      cascadedDelete,
                                              boolean      forLineage,
                                              boolean      forDuplicateProcessing,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        super.deleteBeanInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     metadataElementGUID,
                                     guidParameterName,
                                     OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                     OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                     cascadedDelete,
                                     null,
                                     null,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to archive this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void archiveMetadataElementInStore(String           userId,
                                              String            externalSourceGUID,
                                              String            externalSourceName,
                                              String            metadataElementGUID,
                                              DeleteOptions archiveProperties,
                                              boolean           forLineage,
                                              boolean           forDuplicateProcessing,
                                              Date              effectiveTime,
                                              String            methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        ReferenceableBuilder builder = new ReferenceableBuilder(repositoryHelper, serviceName, serverName);

        if (archiveProperties != null)
        {
            super.archiveBeanInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          metadataElementGUID,
                                          guidParameterName,
                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                          builder.getMementoProperties(archiveProperties.getArchiveDate(),
                                                                       userId,
                                                                       archiveProperties.getArchiveProcess(),
                                                                       archiveProperties.getArchiveProperties(),
                                                                       methodName),
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }
        else
        {
            super.archiveBeanInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          metadataElementGUID,
                                          guidParameterName,
                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                          builder.getMementoProperties(null,
                                                                       userId,
                                                                       null,
                                                                       null,
                                                                       methodName),
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String               userId,
                                               String               externalSourceGUID,
                                               String               externalSourceName,
                                               String               metadataElementGUID,
                                               String               classificationName,
                                               boolean              forLineage,
                                               boolean              forDuplicateProcessing,
                                               NewElementProperties properties,
                                               Date                 effectiveTime,
                                               String               methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 null,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        if (properties != null)
        {
            MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

            InstanceProperties classificationProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                        properties.getEffectiveFrom(),
                                                                                        properties.getEffectiveTo());

            this.setClassificationInRepository(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               metadataElementGUID,
                                               guidParameterName,
                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                               classificationTypeGUID,
                                               classificationName,
                                               classificationProperties,
                                               false,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
        }
        else
        {
            this.setClassificationInRepository(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               metadataElementGUID,
                                               guidParameterName,
                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                               classificationTypeGUID,
                                               classificationName,
                                               null,
                                               false,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
        }
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param isMergeUpdate flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void reclassifyMetadataElementInStore(String            userId,
                                                  String            externalSourceGUID,
                                                  String            externalSourceName,
                                                  String            metadataElementGUID,
                                                  String            classificationName,
                                                  boolean           isMergeUpdate,
                                                  boolean           forLineage,
                                                  boolean           forDuplicateProcessing,
                                                  ElementProperties properties,
                                                  Date              effectiveTime,
                                                  String            methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 null,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties classificationProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                    null,
                                                                                    null);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           metadataElementGUID,
                                           guidParameterName,
                                           OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                           classificationTypeGUID,
                                           classificationName,
                                           classificationProperties,
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationStatusInStore(String       userId,
                                                  String       externalSourceGUID,
                                                  String       externalSourceName,
                                                  String       metadataElementGUID,
                                                  String       classificationName,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  Date         effectiveFrom,
                                                  Date         effectiveTo,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "metadataElementGUID";
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 null,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);

        super.updateClassificationEffectivityDates(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   metadataElementGUID,
                                                   guidParameterName,
                                                   OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                   classificationTypeGUID,
                                                   classificationName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataElementGUIDParameterName name of parameter for GUID
     * @param metadataElementTypeName type of the metadata element
     * @param classificationName unique name of the classification to remove
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void declassifyMetadataElementInStore(String       userId,
                                                  String       externalSourceGUID,
                                                  String       externalSourceName,
                                                  String       metadataElementGUID,
                                                  String       metadataElementGUIDParameterName,
                                                  String       metadataElementTypeName,
                                                  String       classificationName,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, metadataElementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        String classificationTypeGUID = invalidParameterHandler.validateTypeName(classificationName,
                                                                                 null,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);
        super.removeClassificationFromRepository(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 metadataElementGUID,
                                                 metadataElementGUIDParameterName,
                                                 metadataElementTypeName,
                                                 classificationTypeGUID,
                                                 classificationName,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }



    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param makeAnchor should metadata element 2 become anchored to metadata element 1?
     * @param anchorScopeGUID if makeAnchor=true then use this value in anchorScopeGUID
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties the properties of the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String               userId,
                                               String               externalSourceGUID,
                                               String               externalSourceName,
                                               String               relationshipTypeName,
                                               String               metadataElement1GUID,
                                               String               metadataElement2GUID,
                                               boolean              makeAnchor,
                                               String               anchorScopeGUID,
                                               boolean              forLineage,
                                               boolean              forDuplicateProcessing,
                                               NewElementProperties properties,
                                               Date                 effectiveTime,
                                               String               methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String elementTypeParameterName = "relationshipTypeName";
        final String end1ParameterName = "metadataElement1GUID";
        final String end2ParameterName = "metadataElement2GUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, elementTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               null,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        String relationshipGUID;
        if (properties != null)
        {
            MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

            InstanceProperties relationshipProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                      properties.getEffectiveFrom(),
                                                                                      properties.getEffectiveTo());
            relationshipGUID = super.linkElementToElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              metadataElement1GUID,
                                              end1ParameterName,
                                              OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                              metadataElement2GUID,
                                              end2ParameterName,
                                              OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              relationshipTypeGUID,
                                              relationshipTypeName,
                                              relationshipProperties,
                                              properties.getEffectiveFrom(),
                                              properties.getEffectiveTo(),
                                              effectiveTime,
                                              methodName);
        }
        else
        {
            relationshipGUID = super.linkElementToElement(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          metadataElement1GUID,
                                                          end1ParameterName,
                                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                          metadataElement2GUID,
                                                          end2ParameterName,
                                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          relationshipTypeGUID,
                                                          relationshipTypeName,
                                                          null,
                                                          null,
                                                          null,
                                                          effectiveTime,
                                                          methodName);
        }

        if (makeAnchor)
        {
            EntityDetail parent = super.getEntityFromRepository(userId,
                                                                metadataElement1GUID,
                                                                end1ParameterName,
                                                                OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                null,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                null,
                                                                effectiveTime,
                                                                methodName);

            EntityDetail child = super.getEntityFromRepository(userId,
                                                                metadataElement2GUID,
                                                                end2ParameterName,
                                                                OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                null,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                null,
                                                                effectiveTime,
                                                                methodName);

            if ((parent != null) && (child != null))
            {
                super.addAnchorsClassification(userId,
                                               child,
                                               end2ParameterName,
                                               parent.getGUID(),
                                               parent.getType().getTypeDefName(),
                                               this.getDomainName(child),
                                               anchorScopeGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
            }
        }

        return relationshipGUID;
    }


    /**
     * Convert an element properties object into a map.
     *
     * @param properties packed properties
     * @return properties stored in Java map
     * @throws InvalidParameterException the properties are invalid in some way
     */
    public Map<String, InstancePropertyValue> getElementPropertiesAsOMRSMap(ElementProperties    properties) throws InvalidParameterException
    {
        if (properties != null)
        {
            Map<String, PropertyValue>         propertyValues = properties.getPropertyValueMap();
            Map<String, InstancePropertyValue> resultingMap   = new HashMap<>();

            if (propertyValues != null)
            {
                for (String mapPropertyName : propertyValues.keySet())
                {
                    PropertyValue         actualPropertyValue = properties.getPropertyValue(mapPropertyName);
                    InstancePropertyValue instancePropertyValue = this.getInstancePropertyValue(actualPropertyValue);

                    resultingMap.put(mapPropertyName, instancePropertyValue);
                }
            }

            return resultingMap;
        }

        return null;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param relationshipGUID unique identifier of the relationship to update
     * @param isMergeUpdate flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            userId,
                                             String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            relationshipGUID,
                                             boolean           isMergeUpdate,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime,
                                             String            methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        MetadataElementBuilder builder = new MetadataElementBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties relationshipProperties = builder.getInstanceProperties(this.getElementPropertiesAsOMRSMap(properties),
                                                                                  null,
                                                                                  null);
        this.updateRelationshipProperties(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          relationshipGUID,
                                          guidParameterName,
                                          null,
                                          isMergeUpdate,
                                          relationshipProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void updateRelatedElementsStatusInStore(String       userId,
                                                    String       externalSourceGUID,
                                                    String       externalSourceName,
                                                    String       relationshipGUID,
                                                    Date         effectiveFrom,
                                                    Date         effectiveTo,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        super.updateRelationshipEffectivityDates(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationshipGUID,
                                                 guidParameterName,
                                                 null,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @SuppressWarnings(value = "unused")
    public void deleteRelationshipInStore(String       userId,
                                          String       externalSourceGUID,
                                          String       externalSourceName,
                                          String       relationshipGUID,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String guidParameterName = "relationshipGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        repositoryHandler.removeRelationship(userId,
                                             externalSourceGUID,
                                             externalSourceName,
                                             null,
                                             relationshipGUID,
                                             methodName);
    }
}
