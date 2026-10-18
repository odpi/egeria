/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
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
     * @param auditLog destination for audit log events.
     */
    public OpenMetadataAPITemplateHandler(OpenMetadataAPIGenericConverter<B> converter,
                                         Class<B>                            beanClass,
                                         String                              serviceName,
                                         String                              serverName,
                                         InvalidParameterHandler             invalidParameterHandler,
                                         RepositoryHandler                   repositoryHandler,
                                         OMRSRepositoryHelper                repositoryHelper,
                                         String                              localServerUserId,
                                         OpenMetadataServerSecurityVerifier  securityVerifier,
                                         AuditLog                            auditLog)
    {
        super(converter, beanClass, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier, auditLog);
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
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param allowRetrieve can an existing element be returned if it exists
     * @param placeholderProperties values to override placeholder variables in the template
     * @param methodName calling method
     * @return unique identifier of the new bean
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException a problem in the repository services
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
                                         boolean                       deepCopy,
                                         boolean                       templateSubstitute,
                                         boolean                       allowRetrieve,
                                         Map<String, String>           placeholderProperties,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        TemplateProgress templateProgress = new TemplateProgress();

        if (log.isDebugEnabled())
        {
            log.debug("Creating new element of type {} using template {}", forLog(entityTypeName), forLog(templateGUID));
        }

        /*
         * One operation: every element and relationship the copy creates carries the same lastOperationId, so the
         * scope of the copy can be checked afterwards.
         */
        boolean startedOperation = this.startOperation();

        try
        {
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
                                                      deepCopy,
                                                      templateSubstitute,
                                                      allowRetrieve,
                                                      placeholderProperties,
                                                      methodName);

            if (templateProgress.newBeanCreated)
            {
                /*
                 * This relationship shows where the property values for the new bean came from.  It enables traceability.  Also, if the template is
                 * updated, there is a possibility of making complementary changes to the entities that were derived from it.
                 */
                if (log.isDebugEnabled())
                {
                    log.debug("Adding SourcedFrom relationship from new element {} of type {} to template {}",
                              templateProgress.newBeanGUID, forLog(entityTypeName), forLog(templateGUID));
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

                if (this.isObservedAnchorType(entityTypeName))
                {
                    auditLog.logMessage(assetActionDescription,
                                        OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_CREATE.getMessageDefinition(userId,
                                                                                                                      entityTypeName,
                                                                                                                      templateProgress.newBeanGUID,
                                                                                                                      methodName,
                                                                                                                      serviceName));
                }

                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.TEMPLATE_MAPPING_SUMMARY.getMessageDefinition(templateGUID,
                                                                                                           entityTypeName,
                                                                                                           templateProgress.newBeanGUID,
                                                                                                           templateProgress.coveredEntityGUIDMap.toString(),
                                                                                                           templateProgress.coveredRelationshipGUIDMap.toString()));
            }
        }
        finally
        {
            this.endOperation(startedOperation);
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
        String              newBeanGUID                = null; /* GUID of last new entity created - ultimately this is returned to the original caller */
        boolean             newBeanCreated             = true; /* Flag to indicate if a new bean was created during the current processing step */
        long                sourceVersionNumber        = 0; /* the version number of the template entity for the new bean */
        String              previousTemplateGUID       = null; /* GUID of last template entity processed - prevents processing a relationship twice */
        Map<String, String> coveredEntityGUIDMap       = new HashMap<>(); /* Map of template entity GUIDs to new bean GUIDs that have been processed - prevents replicating the same entity twice */
        Map<String, String> coveredRelationshipGUIDMap = new HashMap<>(); /* Map of template relationship GUIDs to new relationship GUIDs that have been processed - prevents replicating the same relationship twice */
        String              templateRootAnchorGUID     = null; /* the anchor of the template's anchored graph - the template itself unless the template is a member of another element's graph */
        boolean             memberTemplate             = false; /* the template is anchored to another element, so only its own part of that element's graph is copied */
        Map<String, String> anchorGUIDMap              = new HashMap<>(); /* Map of template anchor GUIDs to the anchor GUIDs for the new beans, for anchors that are not themselves copied */
        Map<String, EntityDetail> newAnchorEntities    = new HashMap<>(); /* The anchors of the new beans, retrieved once each */
        String              beanAnchorGUID             = null; /* the top-level new bean - the anchor for new beans whose template anchor is not mapped */

        /**
         * Standard toString for logging
         *
         * @return string
         */
        @Override
        public String toString()
        {
            return "OpenMetadataAPITemplateHandler{" +
                    "newBeanGUID='" + newBeanGUID + '\'' +
                    ", newBeanCreated=" + newBeanCreated +
                    ", sourceVersionNumber=" + sourceVersionNumber +
                    ", previousTemplateGUID='" + previousTemplateGUID + '\'' +
                    ", coveredGUIDMap=" + coveredEntityGUIDMap +
                    ", templateRootAnchorGUID='" + templateRootAnchorGUID + '\'' +
                    ", memberTemplate=" + memberTemplate +
                    ", anchorGUIDMap=" + anchorGUIDMap +
                    ", beanAnchorGUID='" + beanAnchorGUID + '\'' +
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
     * @param deepCopy should the template creation extend to the anchored element or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param placeholderProperties values to override placeholder variables in the template
     * @param allowRetrieve can an existing element be returned if it exists
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException a problem in the repository services
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
                                                    boolean                       deepCopy,
                                                    boolean                       templateSubstitute,
                                                    boolean                       allowRetrieve,
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
             * If the element is a template substitute, then use the entity that it is sourced from.
             */
            Classification templateSubstituteClassification = this.getExistingClassification(templateEntity, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName);

            if (templateSubstituteClassification != null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Template entity " + templateEntity.getGUID() + " is a template substitute - retrieving real template.");
                }

                String templateSubstituteGUID = templateEntity.getGUID();

                templateEntity = getAttachedEntity(userId,
                                                   templateSubstituteGUID,
                                                   templateGUIDParameterName,
                                                   entityTypeName,
                                                   OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                   entityTypeName,
                                                   2,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);

                if (templateEntity == null)
                {
                    /*
                     * The TemplateSubstitute classification says "use the element I am sourced from instead of
                     * me", so a substitute with no SourcedFrom relationship points nowhere.  Without this check
                     * the null travels on and surfaces much later as a null entityOneGUID on an addRelationship
                     * call - reported as an internal error, which tells the caller nothing about what is
                     * actually wrong with their template.
                     */
                    throw new InvalidParameterException(GenericHandlersErrorCode.NO_SUBSTITUTE_TEMPLATE.getMessageDefinition(templateSubstituteGUID,
                                                                                                                              methodName),
                                                         this.getClass().getName(),
                                                         methodName,
                                                         templateGUIDParameterName);
                }
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
            EntityDetail templateAnchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                                    entityTypeName,
                                                                                    templateEntity,
                                                                                    templateGUIDParameterName,
                                                                                    true,
                                                                                    false,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);
            if (firstIteration)
            {
                /*
                 * A template that is anchored to another element is a member of that element's graph, and only its
                 * own part of the graph is copied - see addAttachmentsFromTemplate.  The copy is not anchored to the
                 * template's anchor: that would make it part of the template's graph.  It is anchored as the caller's
                 * options say, as any other new element is.
                 */
                if ((templateAnchorEntity == null) || (templateGUID.equals(templateAnchorEntity.getGUID())))
                {
                    templateProgress.templateRootAnchorGUID = templateGUID;
                    templateProgress.memberTemplate         = false;
                }
                else
                {
                    templateProgress.templateRootAnchorGUID = templateAnchorEntity.getGUID();
                    templateProgress.memberTemplate         = true;
                }
            }
            else if (! propertyBuilder.isClassificationSet(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
            {
                /*
                 * The new bean mirrors its template's anchor: the copy of whatever the template is anchored to.  An
                 * element anchored to a member of the template's graph - an endpoint anchored to its connection - is
                 * anchored to the copy of that member, not flattened onto the new top-level bean.
                 */
                this.setNewBeanAnchors(userId, templateEntity, templateProgress, propertyBuilder, forLineage, forDuplicateProcessing, effectiveTime, methodName);
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

            InstanceProperties newEntityProperties = propertyBuilder.getInstanceProperties(methodName);
            String             newQualifiedName    = repositoryHelper.getStringProperty(serviceName,
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                        newEntityProperties,
                                                                                        methodName);

            /*
             * If retrievals of matching entities is permitted then check that the qualified name is unique.
             */
            if ((allowRetrieve) && (newQualifiedName != null))
            {
                String newEntityGUID = getBeanGUIDByUniqueName(userId,
                                                               newQualifiedName,
                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                               entityTypeGUID,
                                                               entityTypeName,
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

                if (newEntityGUID != null)
                {
                    templateProgress.newBeanGUID = newEntityGUID;
                    templateProgress.newBeanCreated = false;

                    return templateProgress;
                }
            }

            /*
             * Verify that the user is permitted to create a new bean.
             */
            validateNewEntityRequest(userId,
                                     templateEntity.getType().getTypeDefGUID(),
                                     templateEntity.getType().getTypeDefName(),
                                     newEntityProperties,
                                     propertyBuilder.getEntityClassifications(),
                                     InstanceStatus.ACTIVE,
                                     effectiveTime,
                                     methodName);

            /*
             * All OK to create the new bean, now work out the classifications.
             * Start with the classifications from the template (ignoring Anchors and Template)
             * and then overlay the classifications set up in the builder and the appropriate anchor.
             */
            Map<String, Classification> newClassificationMap = new HashMap<>();

            List<Classification> templateClassifications = propertyBuilder.getTemplateClassifications();
            if (templateClassifications != null)
            {
                for (Classification templateClassification : templateClassifications)
                {
                    if (templateClassification != null)
                    {
                        if ((!OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(templateClassification.getName())) &&
                                (!OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(templateClassification.getName())))
                        {
                            newClassificationMap.put(templateClassification.getName(), templateClassification);
                        }
                    }
                }
            }

            /*
             * These classifications are from the caller (replacement classifications).
             */
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

            if (!newClassificationMap.isEmpty())
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
                                                                  InstanceStatus.ACTIVE,
                                                                  methodName);


            /*
             * This is the first time through the iteration, so we need to capture the top level bean's guid to act as the anchor for all other
             * beans that are created as a result of this templated creation.
             */
            if (firstIteration)
            {
                templateProgress.beanAnchorGUID = newEntityGUID;

                if (templateProgress.memberTemplate)
                {
                    /*
                     * The template's members share its anchor, which is not copied.  Their copies share the new
                     * bean's anchor instead: the anchor the caller gave it, or the new bean itself if it has none.
                     */
                    templateProgress.anchorGUIDMap.put(templateProgress.templateRootAnchorGUID,
                                                       this.getNewTopLevelAnchorGUID(userId, newEntityGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName));
                }
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
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param placeholderProperties values to override placeholder variables in the template
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException the guids or something related are invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
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
        templateProgress.coveredEntityGUIDMap.put(templateGUID, startingGUID);

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
             * Skip "SourcedFrom", "CatalogTemplate" and "SpecificationPropertyAssignment" relationships since they are part of the template
             * description rather than the template itself.
             */
            if ((! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName)) &&
                (! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName)) &&
                (! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName)))
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
                                                                              null,
                                                                              null,
                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                              null,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
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
                 * Is this a new entity at the other end of the relationship?
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

                        EntityDetail nextTemplateEntityAnchor = this.validateEntityAndAnchorForRead(userId,
                                                                                                    null,
                                                                                                    nextTemplateEntity,
                                                                                                    nextTemplateEntityGUIDParameterName,
                                                                                                    true,
                                                                                                    false,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing,
                                                                                                    effectiveTime,
                                                                                                    methodName);

                        String nextTemplateAnchorGUID = null;
                        if (nextTemplateEntityAnchor != null)
                        {
                            nextTemplateAnchorGUID = nextTemplateEntityAnchor.getGUID();
                        }
                        String nextBeanEntityGUID;

                        /*
                         * An element is copied when it is anchored to an element that has already been copied - the
                         * template itself, or a member of its graph such as a connection with its endpoint - whichever
                         * end of the relationship it is at.  When the template is a member of another element's graph,
                         * an element sharing that anchor is copied only if it lies downstream of the element being
                         * copied: at end 2 of a relationship whose purpose is STRUCTURAL or ATTACHMENT.  Its parent,
                         * its siblings and the elements it is linked to for any other purpose belong to the template's
                         * graph, not to the copy.
                         *
                         * When the template is the anchor of its graph, the whole graph is copied, so an element
                         * anchored to a member is copied even if it is reached before that member has been.
                         */
                        boolean anchoredToCopiedEntity = (nextTemplateAnchorGUID != null)
                                                                 && (! nextTemplateAnchorGUID.equals(nextTemplateEntity.getGUID()))
                                                                 && ((templateProgress.coveredEntityGUIDMap.containsKey(nextTemplateAnchorGUID))
                                                                         || ((! templateProgress.memberTemplate)
                                                                                 && (this.isInTemplateGraph(userId, nextTemplateEntity, templateProgress, forLineage, forDuplicateProcessing, effectiveTime, methodName))));
                        boolean downstreamMember = (templateProgress.memberTemplate)
                                                           && (templateProgress.templateRootAnchorGUID.equals(nextTemplateAnchorGUID))
                                                           && (! nextTemplateEntity.getGUID().equals(nextTemplateAnchorGUID))
                                                           && (relationshipOneToTwo)
                                                           && (this.isPartOfAnchoredGraph(relationship));

                        if (templateProgress.coveredEntityGUIDMap.containsKey(nextTemplateEntity.getGUID()))
                        {
                            /*
                             * The template entity has already been replicated, so we just need to create the
                             * relationship from the equivalent new bean to the start bean.
                             */
                            nextBeanEntityGUID = templateProgress.coveredEntityGUIDMap.get(nextTemplateEntity.getGUID());
                        }
                        else if ((! anchoredToCopiedEntity) && (! downstreamMember)
                                && (templateProgress.memberTemplate)
                                && (this.isInTemplateGraph(userId, nextTemplateEntity, templateProgress, forLineage, forDuplicateProcessing, effectiveTime, methodName)))
                        {
                            /*
                             * Part of the template's own graph that is not being copied - the template's parent, a
                             * sibling, the target of a non-structural link.  Linking the copy to it would join the copy to the
                             * template's graph, so the relationship is not reproduced.  The caller attaches the copy
                             * where it belongs with the parent options.
                             */
                            continue;
                        }
                        else if ((! anchoredToCopiedEntity) && (! downstreamMember))
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
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   serverName);
                            }
                            else
                            {
                                builder = new OpenMetadataAPIGenericBuilder(nextTemplateEntityTypeGUID,
                                                                            nextTemplateEntityTypeName,
                                                                            null,
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
                                                                           true,
                                                                           false,
                                                                           false,
                                                                           placeholderProperties,
                                                                           methodName);

                            nextBeanEntityGUID = templateProgress.newBeanGUID;
                        }

                        /*
                         * As this process iterates, additional entities/relationships are processed.  It may be that
                         * this relationship has already been processed by one of the earlier iterations.  Therefore,
                         * before creating the new relationship, we check it has not been processed.
                         */
                        if (! templateProgress.coveredRelationshipGUIDMap.containsKey(relationship.getGUID()))
                        {
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

                                relationshipProperties = builder.replacePropertiesWithPlaceholders(relationshipProperties, placeholderProperties);
                            }

                            String beanRelationshipGUID;
                            if (relationshipOneToTwo)
                            {
                                final String startingGUIDParameterName = "templateRelationshipEnd1.getGUID()";

                                beanRelationshipGUID = this.uncheckedLinkElementToElement(userId,
                                                                                          externalSourceGUID,
                                                                                          externalSourceName,
                                                                                          startingGUID,
                                                                                          startingGUIDParameterName,
                                                                                          nextBeanEntityGUID,
                                                                                          nextBeanEntityGUIDParameterName,
                                                                                          relationship.getType().getTypeDefGUID(),
                                                                                          relationshipProperties,
                                                                                          methodName);
                            }
                            else
                            {
                                final String startingGUIDParameterName = "templateRelationshipEnd2.getGUID()";

                                beanRelationshipGUID = this.uncheckedLinkElementToElement(userId,
                                                                                          externalSourceGUID,
                                                                                          externalSourceName,
                                                                                          nextBeanEntityGUID,
                                                                                          nextBeanEntityGUIDParameterName,
                                                                                          startingGUID,
                                                                                          startingGUIDParameterName,
                                                                                          relationship.getType().getTypeDefGUID(),
                                                                                          relationshipProperties,
                                                                                          methodName);
                            }

                            templateProgress.coveredRelationshipGUIDMap.put(relationship.getGUID(), beanRelationshipGUID);
                        }
                    }
                }
            }
        }

        return templateProgress;
    }

    /**
     * Set the anchor for a new bean created from a template element that is not the top-level template.  The
     * new bean is anchored to the copy of whatever the template element is anchored to.  Where that anchor is
     * not copied - the anchor of a template that is itself a member of a graph - it is anchored to the new
     * top-level bean's anchor instead, and where the template element records no anchor, to the new top-level bean.
     *
     * @param userId calling user
     * @param templateEntity template element being copied
     * @param templateProgress progress of the copy
     * @param propertyBuilder builder for the new bean
     * @param forLineage the request is to support lineage retrieval
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorized to read the anchor
     */
    private void setNewBeanAnchors(String                        userId,
                                   EntityDetail                  templateEntity,
                                   TemplateProgress              templateProgress,
                                   OpenMetadataAPIGenericBuilder propertyBuilder,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String anchorGUIDParameterName = "newAnchorGUID";

        AnchorIdentifiers templateAnchors     = this.getAnchorsFromAnchorsClassification(templateEntity, methodName);
        String            templateAnchorGUID  = (templateAnchors == null) ? null : templateAnchors.anchorGUID;
        String            newAnchorGUID       = null;

        if (templateAnchorGUID != null)
        {
            newAnchorGUID = templateProgress.anchorGUIDMap.get(templateAnchorGUID);

            if (newAnchorGUID == null)
            {
                newAnchorGUID = templateProgress.coveredEntityGUIDMap.get(templateAnchorGUID);
            }
        }

        if (newAnchorGUID == null)
        {
            newAnchorGUID = templateProgress.beanAnchorGUID;
        }

        if (newAnchorGUID != null)
        {
            EntityDetail newAnchorEntity = templateProgress.newAnchorEntities.get(newAnchorGUID);

            if (newAnchorEntity == null)
            {
                newAnchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                    newAnchorGUID,
                                                                    anchorGUIDParameterName,
                                                                    OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

                templateProgress.newAnchorEntities.put(newAnchorGUID, newAnchorEntity);
            }

            if (newAnchorEntity != null)
            {
                propertyBuilder.setAnchors(userId,
                                           newAnchorGUID,
                                           newAnchorEntity.getType().getTypeDefName(),
                                           this.getDomainName(newAnchorEntity),
                                           this.getAnchorScopeGUIDFromAnchorsClassification(newAnchorEntity, methodName),
                                           this.getZoneMembershipFromClassification(newAnchorEntity, methodName),
                                           methodName);
            }
        }
    }


    /**
     * Return the anchor of a newly created top-level bean: the element named in its Anchors classification, or
     * the bean itself when it is its own anchor or has none.
     *
     * @param userId calling user
     * @param newEntityGUID the new top-level bean
     * @param forLineage the request is to support lineage retrieval
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return anchor GUID
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorized to read the new bean
     */
    private String getNewTopLevelAnchorGUID(String  userId,
                                            String  newEntityGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String newEntityGUIDParameterName = "newEntityGUID";

        EntityDetail newEntity = repositoryHandler.getEntityByGUID(userId,
                                                                   newEntityGUID,
                                                                   newEntityGUIDParameterName,
                                                                   OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        AnchorIdentifiers anchors = this.getAnchorsFromAnchorsClassification(newEntity, methodName);

        if ((anchors != null) && (anchors.anchorGUID != null))
        {
            return anchors.anchorGUID;
        }

        return newEntityGUID;
    }


    /**
     * Is this element part of the template's anchored graph?  It is if it is the template's anchor, or if
     * following the anchors from it - an endpoint to its connection, the connection to the database schema -
     * reaches the template's anchor.
     *
     * @param userId calling user
     * @param entity element to test
     * @param templateProgress progress of the copy, which names the template's anchor
     * @param forLineage the request is to support lineage retrieval
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return true if the element belongs to the template's graph
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorized to read an anchor
     */
    private boolean isInTemplateGraph(String           userId,
                                      EntityDetail     entity,
                                      TemplateProgress templateProgress,
                                      boolean          forLineage,
                                      boolean          forDuplicateProcessing,
                                      Date             effectiveTime,
                                      String           methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String anchorGUIDParameterName = "anchorGUID";

        Set<String>  visitedGUIDs = new HashSet<>();
        EntityDetail current      = entity;

        while ((current != null) && (visitedGUIDs.add(current.getGUID())))
        {
            if (current.getGUID().equals(templateProgress.templateRootAnchorGUID))
            {
                return true;
            }

            AnchorIdentifiers anchors = this.getAnchorsFromAnchorsClassification(current, methodName);

            if ((anchors == null) || (anchors.anchorGUID == null) || (anchors.anchorGUID.equals(current.getGUID())))
            {
                return false;
            }

            if (anchors.anchorGUID.equals(templateProgress.templateRootAnchorGUID))
            {
                return true;
            }

            current = repositoryHandler.getEntityByGUID(userId,
                                                        anchors.anchorGUID,
                                                        anchorGUIDParameterName,
                                                        OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
        }

        return false;
    }
}
