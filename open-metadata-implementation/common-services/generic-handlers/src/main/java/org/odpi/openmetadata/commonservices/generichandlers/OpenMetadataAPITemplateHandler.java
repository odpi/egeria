/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OpenMetadataAPITemplateHandler provides the methods for managing the creation of elements using templates.
 *
 * @param <B>
 */
public class OpenMetadataAPITemplateHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataAPITemplateHandler.class);

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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public OpenMetadataAPITemplateHandler(OpenMetadataAPIGenericConverter<B> converter,
                                         Class<B>                           beanClass,
                                         String                             serviceName,
                                         String                             serverName,
                                         InvalidParameterHandler invalidParameterHandler,
                                         RepositoryHandler repositoryHandler,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String                             localServerUserId,
                                         OpenMetadataServerSecurityVerifier securityVerifier,
                                         List<String> supportedZones,
                                         List<String>                       defaultZones,
                                         List<String>                       publishZones,
                                         AuditLog auditLog)
    {
        super(converter, beanClass, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is preloaded with
     * properties that should override the properties from the template.  This is the top-level method to call from the specific handlers.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of existing entity to use
     * @param templateGUIDParameterName name of parameter passing the templateGUID
     * @param entityTypeGUID unique identifier of the type for the entity
     * @param entityTypeName unique name of the type for the entity
     * @param uniqueParameterValue the value of a unique property (typically qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
     * @param serviceSupportedZones list of supported zones for this service
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param placeholderProperties values to override placeholder variables in the template
     * @param methodName calling method
     * @return unique identifier of the new bean
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException there is a problem in the repository services
     * @throws UserNotAuthorizedException the user is not authorized to access one of the elements.
     */
    public String createBeanFromTemplate(String                        userId,
                                         String                        externalSourceGUID,
                                         String                        externalSourceName,
                                         String                        templateGUID,
                                         String                        templateGUIDParameterName,
                                         String                        entityTypeGUID,
                                         String                        entityTypeName,
                                         String                        uniqueParameterValue,
                                         String                        uniqueParameterName,
                                         OpenMetadataAPIGenericBuilder propertyBuilder,
                                         List<String>                  serviceSupportedZones,
                                         boolean                       deepCopy,
                                         boolean                       templateSubstitute,
                                         Map<String, String>           placeholderProperties,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        TemplateProgress templateProgress = new TemplateProgress();

        if (log.isDebugEnabled())
        {
            log.debug("Creating new element of type " + entityTypeName + " using template " + templateGUID);
        }

        templateProgress = createBeanFromTemplate(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  true,
                                                  templateProgress,
                                                  templateGUID,
                                                  templateGUIDParameterName,
                                                  entityTypeGUID,
                                                  entityTypeName,
                                                  uniqueParameterValue,
                                                  uniqueParameterName,
                                                  propertyBuilder,
                                                  serviceSupportedZones,
                                                  deepCopy,
                                                  templateSubstitute,
                                                  placeholderProperties,
                                                  methodName);

        /*
         * This relationship shows where the property values for the new bean came from.  It enables traceability.  Also, if the template is
         * updated, there is a possibility of making complementary changes to the entities that were derived from it.
         */
        if (log.isDebugEnabled())
        {
            log.debug("Adding SourcedFrom relationship from new element " + templateProgress.newBeanGUID + " of type " + entityTypeName + " to template " + templateGUID);
        }

        InstanceProperties relationshipProperties = null;

        if (templateProgress.sourceVersionNumber != 0)
        {
            relationshipProperties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                                null,
                                                                                OpenMetadataProperty.SOURCE_VERSION_NUMBER.name,
                                                                                templateProgress.sourceVersionNumber,
                                                                                methodName);
        }

        repositoryHandler.createRelationship(localServerUserId,
                                             OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                             externalSourceGUID,
                                             externalSourceName,
                                             templateProgress.newBeanGUID,
                                             templateGUID,
                                             relationshipProperties,
                                             methodName);

        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.ASSET.typeName))
        {
            auditLog.logMessage(assetActionDescription,
                                GenericHandlersAuditCode.ASSET_ACTIVITY_CREATE.getMessageDefinition(userId,
                                                                                                    entityTypeName,
                                                                                                    templateProgress.newBeanGUID,
                                                                                                    methodName,
                                                                                                    serviceName));
        }

        return templateProgress.newBeanGUID;
    }


    /**
     * Template process is used to pass around the status of the template replicating process.
     * The purpose of taking note of the parts of the template graph processed is to prevent
     * situations where elements are processed more than once - creating distorted or "infinite" results.
     */
    static class TemplateProgress
    {
        String              newBeanGUID          = null; /* GUID of last new entity created - ultimately this is returned to the original caller */
        long                sourceVersionNumber  = 0; /* the version number of the template entity for the new bean */
        String              previousTemplateGUID = null; /* GUID of last template entity processed - prevents processing a relationship twice */
        Map<String, String> coveredGUIDMap       = new HashMap<>(); /* Map of template GUIDs to new bean GUIDs that have been processed - prevents replicating the same entity twice */
        List<String>        templateAnchorGUIDs  = new ArrayList<>(); /* List of anchor GUIDs associated with the template - to allow nested anchors to be handled */
        String              beanAnchorGUID       = null; /* value of the anchor to set into the new beans */
        String              beanAnchorTypeName   = null; /* value of the anchor to set into the new beans */

        /**
         * Standard toString for logging
         *
         * @return string
         */
        @Override
        public String toString()
        {
            return "TemplateProgress{" +
                    "newBeanGUID='" + newBeanGUID + '\'' +
                    ", sourceVersionNumber=" + sourceVersionNumber +
                    ", previousTemplateGUID='" + previousTemplateGUID + '\'' +
                    ", coveredGUIDMap=" + coveredGUIDMap +
                    ", templateAnchorGUIDs=" + templateAnchorGUIDs +
                    ", beanAnchorGUID='" + beanAnchorGUID + '\'' +
                    ", beanAnchorTypeName='" + beanAnchorTypeName + '\'' +
                    '}';
        }
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is preloaded with
     * properties that should override the properties from the template.  This method is called iterative for each entity anchored to the
     * original template.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param firstIteration is this the first call to this method?
     * @param templateProgress current new bean, previous GUID and list of entities from the template that have been processed (so we only create new elements one-to-one when there are cyclic relationships)
     * @param templateGUID unique identifier of existing entity to use
     * @param templateGUIDParameterName name of parameter passing the templateGUID
     * @param entityTypeGUID unique identifier of the type for the entity
     * @param entityTypeName unique name of the type for the entity
     * @param uniqueParameterValue the value of a unique property (typically qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
     * @param serviceSupportedZones list of supported zones for this service
     * @param deepCopy should the template creation extend to the anchored element or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param placeholderProperties values to override placeholder variables in the template
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException there is a problem in the repository services
     * @throws UserNotAuthorizedException the user is not authorized to access one of the elements.
     */
    @SuppressWarnings(value = "unused")
    private TemplateProgress createBeanFromTemplate(String                        userId,
                                                    String                        externalSourceGUID,
                                                    String                        externalSourceName,
                                                    boolean                       firstIteration,
                                                    TemplateProgress              templateProgress,
                                                    String                        templateGUID,
                                                    String                        templateGUIDParameterName,
                                                    String                        entityTypeGUID,
                                                    String                        entityTypeName,
                                                    String                        uniqueParameterValue,
                                                    String                        uniqueParameterName,
                                                    OpenMetadataAPIGenericBuilder propertyBuilder,
                                                    List<String>                  serviceSupportedZones,
                                                    boolean                       deepCopy,
                                                    boolean                       templateSubstitute,
                                                    Map<String, String>           placeholderProperties,
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String newEntityParameterName = "newEntityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        boolean forLineage = true;
        boolean forDuplicateProcessing = false;
        Date    effectiveTime          = new Date();

        if (log.isDebugEnabled())
        {
            log.debug(templateProgress.toString());
        }

        /*
         * This call ensures the template exists and is the correct type. An exception will be thrown if there are any problems.
         */
        EntityDetail templateEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        templateGUID,
                                                                        templateGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        if ((firstIteration) && (templateEntity != null))
        {
            /*
             * If the element is a template substitute then use the entity that it is sourced from.
             */
            Classification templateSubstituteClassification = this.getExistingClassification(templateEntity, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName);

            if (templateSubstituteClassification != null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Template entity " + templateEntity.getGUID() + " is a template substitute - retrieving real template.");
                }

                templateEntity = getAttachedEntity(userId,
                                                   templateEntity.getGUID(),
                                                   templateGUIDParameterName,
                                                   entityTypeName,
                                                   OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                   entityTypeName,
                                                   2,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   serviceSupportedZones,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        if (templateEntity != null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Using template entity " + templateEntity.getGUID());
            }

            /*
             * Save the version number of the template entity.
             */
            templateProgress.sourceVersionNumber = templateEntity.getVersion();

            /*
             * Check that the template is visible to the calling user.  If the template is an anchor, its own entity is returned.
             */
            EntityDetail templateAnchorEntity = this.validateAnchorEntity(userId,
                                                                          templateGUID,
                                                                          entityTypeName,
                                                                          templateEntity,
                                                                          templateGUIDParameterName,
                                                                          true,
                                                                          false,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          serviceSupportedZones,
                                                                          effectiveTime,
                                                                          methodName);
            if (templateAnchorEntity != null)
            {
                if ((firstIteration) && (! templateGUID.equals(templateAnchorEntity.getGUID())))
                {
                    /*
                     * Need to use the same anchor as the template because the template is anchored to another bean.
                     * This occurs the first time through the iteration if the initial template object has an anchor.
                     */
                    templateProgress.beanAnchorGUID = templateAnchorEntity.getGUID();
                    templateProgress.beanAnchorTypeName = templateAnchorEntity.getType().getTypeDefName();
                }

                templateProgress.templateAnchorGUIDs.add(templateAnchorEntity.getGUID());
            }

            if ((templateProgress.beanAnchorGUID != null) && (! propertyBuilder.isClassificationSet(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName)))
            {
                /*
                 * A bean anchor has been set up on a previous iteration.  This value is typically set when the top-level bean is created
                 * from the template.  The alternative is that the top-level template bean has an anchor.
                 */
                propertyBuilder.setAnchors(userId, templateProgress.beanAnchorGUID, templateProgress.beanAnchorTypeName, methodName);
            }

            /*
             * If the resulting bean is to be used as a template substitute then add the classification.
             */
            if ((firstIteration) && (templateSubstitute))
            {
                propertyBuilder.setTemplateSubstitute(userId, methodName);
            }

            /*
             * Set the properties and classifications from the template entity as the default properties.
             */
            propertyBuilder.setTemplateProperties(templateEntity.getProperties(), placeholderProperties);
            propertyBuilder.setTemplateClassifications(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       templateEntity.getClassifications(),
                                                       placeholderProperties,
                                                       methodName);

            /*
             * Verify that the user is permitted to create a new bean.
             */
            validateNewEntityRequest(userId,
                                     templateEntity.getType().getTypeDefGUID(),
                                     templateEntity.getType().getTypeDefName(),
                                     propertyBuilder.getInstanceProperties(methodName),
                                     propertyBuilder.getEntityClassifications(),
                                     propertyBuilder.getInstanceStatus(),
                                     effectiveTime,
                                     methodName);

            /*
             * All OK to create the new bean, now work out the classifications.  Start with the classifications from the template (ignoring Anchors
             * and LatestChange) and then overlay the classifications set up in the builder and the appropriate anchor.
             */
            Map<String, Classification> newClassificationMap = new HashMap<>();

            if (templateEntity.getClassifications() != null)
            {
                for (Classification templateClassification : templateEntity.getClassifications())
                {
                    if (templateClassification != null)
                    {
                        if ((! OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName.equals(templateClassification.getName())) &&
                                (! OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(templateClassification.getName())) &&
                                (! OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(templateClassification.getName())))
                        {
                            newClassificationMap.put(templateClassification.getName(), templateClassification);
                        }
                    }
                }
            }

            List<Classification> builderClassifications = propertyBuilder.getEntityClassifications();
            if (builderClassifications != null)
            {
                for (Classification builderClassification : builderClassifications)
                {
                    if (builderClassification != null)
                    {
                        newClassificationMap.put(builderClassification.getName(), builderClassification);
                    }
                }
            }

            List<Classification> newClassifications = null;

            if (! newClassificationMap.isEmpty())
            {
                newClassifications = new ArrayList<>(newClassificationMap.values());
            }

            /*
             * Ready to create the new bean
             */
            String newEntityGUID = repositoryHandler.createEntity(userId,
                                                                  templateEntity.getType().getTypeDefGUID(),
                                                                  templateEntity.getType().getTypeDefName(),
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  propertyBuilder.getInstanceProperties(methodName),
                                                                  newClassifications,
                                                                  propertyBuilder.getInstanceStatus(),
                                                                  methodName);

            /*
             * This is the first time through the iteration, so we need to capture the top level bean's guid to act as the anchor for all other
             * beans that are created as a result of this templated creation.
             */
            if (firstIteration)
            {
                templateProgress.beanAnchorGUID = newEntityGUID;
                templateProgress.beanAnchorTypeName = templateEntity.getType().getTypeDefName();
            }

            if (deepCopy)
            {
                /*
                 * The real value of templates is that they cover the creation of a cluster of metadata instances.  The last step is to explore
                 * the graph of linked elements and replicate the structure for the new bean.
                 */
                templateProgress = this.addAttachmentsFromTemplate(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   templateProgress,
                                                                   newEntityGUID,
                                                                   templateGUID,
                                                                   entityTypeName,
                                                                   uniqueParameterValue,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   effectiveTime,
                                                                   placeholderProperties,
                                                                   methodName);
            }

            templateProgress.newBeanGUID = newEntityGUID;
        }
        else
        {
            templateProgress.newBeanGUID = null;
        }

        return templateProgress;
    }


    /**
     * Create equivalent relationships to attachments for a new element that has been created from a template.
     * The element and template have already been checked to be visible to the calling user.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param templateProgress current new bean, previous GUID and list of entities from the template that have been processed (so we only create new elements one-to-one when there are cyclic relationships)
     * @param startingGUID unique identifier of the newly created element
     * @param templateGUID unique identifier of the template element
     * @param expectedTypeName type name of the new bean
     * @param qualifiedName unique name for this new bean - must not be null
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param placeholderProperties values to override placeholder variables in the template
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException the guids or something related are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the repositories
     */
    private TemplateProgress addAttachmentsFromTemplate(String              userId,
                                                        String              externalSourceGUID,
                                                        String              externalSourceName,
                                                        TemplateProgress    templateProgress,
                                                        String              startingGUID,
                                                        String              templateGUID,
                                                        String              expectedTypeName,
                                                        String              qualifiedName,
                                                        boolean             forLineage,
                                                        boolean             forDuplicateProcessing,
                                                        List<String>        serviceSupportedZones,
                                                        Date                effectiveTime,
                                                        Map<String, String> placeholderProperties,
                                                        String              methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String nextTemplateEntityGUIDParameterName = "nextTemplateEntity.getGUID";
        final String nextQualifiedNameParameterName = "nextQualifiedName";
        final String nextBeanEntityGUIDParameterName = "nextBeanEntityGUID";

        Map<String, Integer> qualifiedNameUsageCount = new HashMap<>();
        boolean              relationshipOneToTwo;

        /*
         * Save the previousTemplateGUID for this round.
         */
        String previousTemplateGUID = templateProgress.previousTemplateGUID;

        /*
         * Record that the templateGUID has already been processed. This is passed to subsequent iterative calls to this method.
         */
        templateProgress.previousTemplateGUID = templateGUID;
        templateProgress.coveredGUIDMap.put(templateGUID, startingGUID);

        /*
         * Begin by retrieving all the relationships attached to the template.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       templateGUID,
                                                                                       expectedTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        /*
         * For each relationship, it is necessary to determine if the attached entity is anchored to the template or not.  If it is
         * anchored, the attached entity needs to be copied and the copy attached to the new bean.  If it is not anchored then it is sufficient to
         * create a new relationship between the new bean and the attached entity.
         */
        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();
            EntityProxy  entityProxy  = relationship.getEntityOneProxy();

            if (templateGUID.equals(entityProxy.getGUID()))
            {
                entityProxy = relationship.getEntityTwoProxy();
                relationshipOneToTwo = true;
            }
            else
            {
                relationshipOneToTwo = false;
            }

            /*
             * Skip "SourcedFrom" and "SpecificationPropertyAssignment" relationships since they are part of the template
             * description rather than the template itself.
             */
            if ((! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName)) &&
                (! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName)))
            {
                /*
                 * If the element is a template substitute then use the entity that it is sourced from.
                 */
                Classification templateSubstituteClassification = this.getExistingClassification(entityProxy, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName);

                if (templateSubstituteClassification != null)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Template entity " + entityProxy.getGUID() + " is a template substitute - retrieving real template.");
                    }

                    List<Relationship> realRelationships = getAttachmentLinks(userId,
                                                                              entityProxy.getGUID(),
                                                                              nextTemplateEntityGUIDParameterName,
                                                                              entityProxy.getType().getTypeDefName(),
                                                                              OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                                                              OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                                              null,
                                                                              entityProxy.getType().getTypeDefName(),
                                                                              2,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              serviceSupportedZones,
                                                                              0, 0,
                                                                              effectiveTime,
                                                                              methodName);
                    if (realRelationships != null)
                    {
                        for (Relationship realRelationship : realRelationships)
                        {
                            if (realRelationship != null)
                            {
                                entityProxy = realRelationship.getEntityTwoProxy();
                            }
                        }
                    }
                }

                /*
                 * Is this a new relationship?
                 */
                if ((entityProxy != null) && (entityProxy.getType() != null) && (! entityProxy.getGUID().equals(previousTemplateGUID)))
                {
                    EntityDetail nextTemplateEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        entityProxy.getGUID(),
                                                                                        nextTemplateEntityGUIDParameterName,
                                                                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

                    if ((nextTemplateEntity != null) && (nextTemplateEntity.getType() != null))
                    {
                        String nextTemplateEntityTypeGUID = nextTemplateEntity.getType().getTypeDefGUID();
                        String nextTemplateEntityTypeName = nextTemplateEntity.getType().getTypeDefName();

                        EntityDetail nextTemplateEntityAnchor = this.validateAnchorEntity(userId,
                                                                                          nextTemplateEntity.getGUID(),
                                                                                          null,
                                                                                          nextTemplateEntity,
                                                                                          nextTemplateEntityGUIDParameterName,
                                                                                          true,
                                                                                          false,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          serviceSupportedZones,
                                                                                          effectiveTime,
                                                                                          methodName);

                        String nextTemplateAnchorGUID = null;
                        if (nextTemplateEntityAnchor != null)
                        {
                            nextTemplateAnchorGUID = nextTemplateEntityAnchor.getGUID();
                        }
                        String nextBeanEntityGUID;

                        if (templateProgress.coveredGUIDMap.containsKey(nextTemplateEntity.getGUID()))
                        {
                            /*
                             * The template entity has already been replicated, so we just need to create the
                             * relationship from the equivalent new bean to the start bean.
                             */
                            nextBeanEntityGUID = templateProgress.coveredGUIDMap.get(nextTemplateEntity.getGUID());
                        }
                        else if ((nextTemplateAnchorGUID == null) || (! templateProgress.templateAnchorGUIDs.contains(nextTemplateAnchorGUID)))
                        {
                            /*
                             * The linked entity is either not got an anchorGUID or has a different anchorGUID.
                             * However, we still need to create the relationship between the start bean and the linked entity.
                             */
                            nextBeanEntityGUID = nextTemplateEntity.getGUID();
                        }
                        else
                        {
                            /*
                             * This linked entity has the same anchorGUID, so it needs to be copied.
                             */
                            OpenMetadataAPIGenericBuilder builder;
                            String                        nextQualifiedName = null;
                            if ((qualifiedName != null) &&
                                    (repositoryHelper.isTypeOf(serviceName,
                                                               nextTemplateEntityTypeName,
                                                               OpenMetadataType.REFERENCEABLE.typeName)))
                            {
                                /*
                                 * This entity may be a nested anchor itself.  We can not tell until processing attachments to it later in the
                                 * process so the GUID is stored as a potential anchor.
                                 */
                                templateProgress.templateAnchorGUIDs.add(nextTemplateEntity.getGUID());

                                nextQualifiedName = qualifiedName + "::" + nextTemplateEntityTypeName;
                                int nextQualifiedNameCount = 0;

                                if (qualifiedNameUsageCount.get(nextQualifiedName) != null)
                                {
                                    nextQualifiedNameCount = qualifiedNameUsageCount.get(nextQualifiedName);
                                }
                                qualifiedNameUsageCount.put(nextQualifiedName, nextQualifiedNameCount + 1);

                                if (nextQualifiedNameCount > 0)
                                {
                                    nextQualifiedName = nextQualifiedName + "_" + nextQualifiedNameCount;
                                }

                                builder = new ReferenceableBuilder(nextQualifiedName,
                                                                   null,
                                                                   nextTemplateEntityTypeGUID,
                                                                   nextTemplateEntityTypeName,
                                                                   null,
                                                                   nextTemplateEntity.getStatus(),
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   serverName);
                            }
                            else
                            {
                                builder = new OpenMetadataAPIGenericBuilder(nextTemplateEntityTypeGUID,
                                                                            nextTemplateEntityTypeName,
                                                                            null,
                                                                            nextTemplateEntity.getStatus(),
                                                                            null,
                                                                            repositoryHelper,
                                                                            serviceName,
                                                                            serverName);
                            }

                            builder.setTemplateProperties(nextTemplateEntity.getProperties(), placeholderProperties);
                            builder.setTemplateClassifications(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               nextTemplateEntity.getClassifications(),
                                                               placeholderProperties,
                                                               methodName);

                            templateProgress = this.createBeanFromTemplate(userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           false,
                                                                           templateProgress,
                                                                           nextTemplateEntity.getGUID(),
                                                                           nextTemplateEntityGUIDParameterName,
                                                                           nextTemplateEntity.getType().getTypeDefGUID(),
                                                                           nextTemplateEntityTypeName,
                                                                           nextQualifiedName,
                                                                           nextQualifiedNameParameterName,
                                                                           builder,
                                                                           serviceSupportedZones,
                                                                           true,
                                                                           false,
                                                                           placeholderProperties,
                                                                           methodName);

                            nextBeanEntityGUID = templateProgress.newBeanGUID;
                        }

                        /*
                         * Link the previously created bean to the next bean - making sure end one and end two are correctly set up.
                         */
                        InstanceProperties relationshipProperties = relationship.getProperties();

                        if (relationshipProperties != null)
                        {
                            OpenMetadataAPIGenericBuilder builder = new OpenMetadataAPIGenericBuilder(relationship.getType().getTypeDefGUID(),
                                                                                                      relationship.getType().getTypeDefName(),
                                                                                                      repositoryHelper,
                                                                                                      serviceName,
                                                                                                      serverName);

                            relationshipProperties = builder.replaceStringPropertiesWithPlaceholders(relationshipProperties, placeholderProperties);
                        }
                        if (relationshipOneToTwo)
                        {
                            final String startingGUIDParameterName = "templateRelationshipEnd1.getGUID()";

                            this.uncheckedLinkElementToElement(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               startingGUID,
                                                               startingGUIDParameterName,
                                                               expectedTypeName,
                                                               nextBeanEntityGUID,
                                                               nextBeanEntityGUIDParameterName,
                                                               nextTemplateEntityTypeName,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               serviceSupportedZones,
                                                               relationship.getType().getTypeDefGUID(),
                                                               relationship.getType().getTypeDefName(),
                                                               relationshipProperties,
                                                               effectiveTime,
                                                               methodName);
                        }
                        else
                        {
                            final String startingGUIDParameterName = "templateRelationshipEnd2.getGUID()";

                            this.uncheckedLinkElementToElement(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               nextBeanEntityGUID,
                                                               nextBeanEntityGUIDParameterName,
                                                               nextTemplateEntityTypeName,
                                                               startingGUID,
                                                               startingGUIDParameterName,
                                                               expectedTypeName,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               serviceSupportedZones,
                                                               relationship.getType().getTypeDefGUID(),
                                                               relationship.getType().getTypeDefName(),
                                                               relationshipProperties,
                                                               effectiveTime,
                                                               methodName);
                        }
                    }
                }
            }
        }

        return templateProgress;
    }
}
