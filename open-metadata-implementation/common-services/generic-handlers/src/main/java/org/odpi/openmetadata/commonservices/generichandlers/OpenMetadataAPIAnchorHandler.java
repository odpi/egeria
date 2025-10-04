/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;

import java.util.Date;
import java.util.List;

/**
 * OpenMetadataAPIRootHandler provides the common properties for the generic handlers.
 *
 * @param <B> bean class
 */
public class OpenMetadataAPIAnchorHandler<B> extends OpenMetadataAPIRootHandler<B>
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
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public OpenMetadataAPIAnchorHandler(OpenMetadataAPIGenericConverter<B> converter,
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
        super(converter, beanClass, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
    }



    /**
     * The properties for an Anchors classification.
     */
    public static class AnchorIdentifiers
    {
        public String anchorGUID = null;
        String anchorTypeName    = null;
        String anchorDomainName  = null;
        String anchorScopeGUID   = null;
    }

    /**
     * Retrieve the anchorGUID property from the Anchors classification if present.  A null is returned if the Anchors classification
     * is missing, or the property is missing from the classification or is null.
     *
     * @param connectToEntity entity containing the classifications
     * @param methodName calling method
     * @return anchorGUID or null
     */
    public AnchorIdentifiers getAnchorsFromAnchorsClassification(EntitySummary connectToEntity,
                                                                 String        methodName)
    {
        /*
         * Metadata maintained by Egeria Access Service modules should have the Anchors classification.
         */
        Classification    anchorsClassification;
        AnchorIdentifiers anchorIdentifiers = null;

        try
        {
            anchorsClassification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                 connectToEntity,
                                                                                 OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                                 methodName);

            if (anchorsClassification != null)
            {
                /*
                 * If the connectTo entity has the Anchors classification then this should contain the anchor guid
                 */
                if (anchorsClassification.getProperties() != null)
                {
                    anchorIdentifiers = new AnchorIdentifiers();

                    anchorIdentifiers.anchorGUID = repositoryHelper.getStringProperty(serviceName,
                                                                                      OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                      anchorsClassification.getProperties(),
                                                                                      methodName);

                    anchorIdentifiers.anchorTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                                          OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                                          anchorsClassification.getProperties(),
                                                                                          methodName);

                    anchorIdentifiers.anchorDomainName = repositoryHelper.getStringProperty(serviceName,
                                                                                            OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                                            anchorsClassification.getProperties(),
                                                                                            methodName);

                    anchorIdentifiers.anchorScopeGUID = repositoryHelper.getStringProperty(serviceName,
                                                                                            OpenMetadataProperty.ANCHOR_SCOPE_GUID.name,
                                                                                            anchorsClassification.getProperties(),
                                                                                            methodName);

                    if (anchorIdentifiers.anchorGUID == null)
                    {
                        /*
                         * If the anchorGUID is not filled out then the entity is its own anchor.
                         * This approach allows us to add the Anchors classification to be added
                         * when an element is created, rather than once we have its GUID.
                         * This saves a lot of unnecessary processing of Anchors classification by the
                         * event handlers that receive the createEntity event, since they try to
                         * derive the Anchors classification if it is missing.
                         */
                        anchorIdentifiers.anchorGUID = connectToEntity.getGUID();
                    }

                    /*
                     * This is an attempt to trap an intermittent error recorded in issue #4680.
                     */
                    if ("<unknown>".equals(anchorIdentifiers.anchorGUID))
                    {
                        final String localMethodName = "getAnchorGUIDFromAnchorsClassification";

                        throw new PropertyServerException(GenericHandlersErrorCode.UNKNOWN_ANCHOR_GUID.getMessageDefinition(localMethodName,
                                                                                                                            serviceName,
                                                                                                                            methodName),
                                                          this.getClass().getName(),
                                                          localMethodName);
                    }
                }
            }
        }
        catch (ClassificationErrorException noAnchorsClassification)
        {
            /*
             * No Anchors Classification - this is not an error - it means the connectTo entity is either an anchor itself, or was not created by an
             * Egeria component.
             */
        }
        catch (PropertyServerException error)
        {
            try
            {
                errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
            }
            catch (PropertyServerException secondError)
            {
                // Not able to log exception
            }
        }

        return anchorIdentifiers;
    }


    /**
     * Provide a standard approach to setting the anchors for a new element based on the anchor of the parent.
     * If there is no parentGUID, no anchor is added.  An invalid parentGUID results in an exception.
     * If the parent entity has no Anchors classification, no anchors classification is constructed.
     * Otherwise, the values from the retrieved parents Anchors classification are used.
     *
     * @param userId calling user
     * @param parentGUID required anchor
     * @param parentGUIDParameterName parameter passing the parentGUID
     * @param builder builder to set up the anchor
     * @param forLineage is this a lineage request?
     * @param forDuplicateProcessing is this part of de-duplicate processing?
     * @param effectiveTime what effect time to use for the retrieve?
     * @param methodName calling method
     * @throws InvalidParameterException bad parameter (probably parentGUID)
     * @throws PropertyServerException repository not working
     * @throws UserNotAuthorizedException security error
     */
    public void setUpAnchorsClassificationFromParent(String                        userId,
                                                     String                        parentGUID,
                                                     String                        parentGUIDParameterName,
                                                     OpenMetadataAPIGenericBuilder builder,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     Date                          effectiveTime,
                                                     String                        methodName) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        if (parentGUID != null)
        {
            EntityDetail parentEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          parentGUID,
                                                                          parentGUIDParameterName,
                                                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);
            if (parentEntity != null)
            {
                OpenMetadataAPIAnchorHandler.AnchorIdentifiers anchors = this.getAnchorsFromAnchorsClassification(parentEntity, methodName);

                if (anchors != null)
                {
                    builder.setAnchors(userId, anchors, methodName);
                }
                else
                {
                    builder.setAnchors(userId,
                                       parentEntity.getGUID(),
                                       parentEntity.getType().getTypeDefName(),
                                       this.getDomainName(parentEntity),
                                       null,
                                       methodName);
                }
            }
        }
    }


    /**
     * Provide a standard approach to setting the anchors for a new element based on the supplied anchor.
     * If there is no anchorGUID, no anchor is added.  An invalid anchorGUID results in an exception.
     * If the anchor entity has no Anchors classification, a default anchors classification is constructed.
     * Otherwise the default anchors classification is supplemented with the anchorScopeGUID from
     * the retrieved Anchors classification is used.
     *
     * @param userId calling user
     * @param anchorGUID required anchor
     * @param anchorGUIDParameterName parameter passing the anchorGUID
     * @param anchorScopeGUID optional scope of the anchor
     * @param builder builder to set up the anchor
     * @param forLineage is this a lineage request?
     * @param forDuplicateProcessing is this part of de-duplicate processing?
     * @param effectiveTime what effect time to use for the retrieve?
     * @param methodName calling method
     * @return anchor entity
     * @throws InvalidParameterException bad parameter (probably anchorGUID)
     * @throws PropertyServerException repository not working
     * @throws UserNotAuthorizedException security error
     */
    public EntityDetail setUpAnchorsClassificationFromAnchor(String                        userId,
                                                             String                        anchorGUID,
                                                             String                        anchorGUIDParameterName,
                                                             String                        anchorScopeGUID,
                                                             OpenMetadataAPIGenericBuilder builder,
                                                             boolean                       forLineage,
                                                             boolean                       forDuplicateProcessing,
                                                             Date                          effectiveTime,
                                                             String                        methodName) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          anchorGUID,
                                                                          anchorGUIDParameterName,
                                                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);
            if (anchorEntity != null)
            {
                OpenMetadataAPIAnchorHandler.AnchorIdentifiers anchors = this.getAnchorsFromAnchorsClassification(anchorEntity, methodName);

                if ((anchors == null) || (anchorScopeGUID != null))
                {
                    builder.setAnchors(userId,
                                       anchorGUID,
                                       anchorEntity.getType().getTypeDefName(),
                                       this.getDomainName(anchorEntity),
                                       anchorScopeGUID,
                                       methodName);
                }
                else
                {
                    builder.setAnchors(userId,
                                       anchorGUID,
                                       anchorEntity.getType().getTypeDefName(),
                                       this.getDomainName(anchorEntity),
                                       anchors.anchorScopeGUID,
                                       methodName);
                }
            }
        }

        return null;
    }



    /**
     * Retrieve the anchorScopeGUID from the anchors classification.
     *
     * @param anchorEntity required anchor
     * @param methodName calling method
     */
    public String getAnchorScopeGUIDFromAnchorsClassification(EntitySummary anchorEntity,
                                                              String        methodName)
    {
        if (anchorEntity != null)
        {
            OpenMetadataAPIAnchorHandler.AnchorIdentifiers anchors = this.getAnchorsFromAnchorsClassification(anchorEntity, methodName);

            if (anchors != null)
            {
                return anchors.anchorScopeGUID;
            }
        }

        return null;
    }


    /**
     * Set up the Anchors classification in an entity (and any child anchored entity connected to it).  This is done using the local server's user id
     * and assumes these classifications are maintained in the local cohort.
     *
     * @param targetGUID unique identifier for the entity
     * @param targetGUIDParameterName parameter name supplying targetGUID
     * @param targetTypeName type of entity
     * @param targetEntity contents of the entity from the repository
     * @param anchorIdentifiers unique identifier/typeName of the anchor
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws PropertyServerException problem within the repository services
     */
    private void maintainAnchorGUIDInClassification(String            targetGUID,
                                                    String            targetGUIDParameterName,
                                                    String            targetTypeName,
                                                    EntitySummary     targetEntity,
                                                    AnchorIdentifiers anchorIdentifiers,
                                                    boolean           forLineage,
                                                    boolean           forDuplicateProcessing,
                                                    Date effectiveTime,
                                                    String            methodName) throws PropertyServerException
    {
        Classification     anchorsClassification;
        InstanceProperties anchorsProperties = null;
        String             currentAnchorGUID = null;
        String             newAnchorGUID = null;

        if (anchorIdentifiers != null)
        {
            newAnchorGUID = anchorIdentifiers.anchorGUID;
        }

        /*
         * This is an attempt to trap an intermittent error recorded in issue #4680.
         */
        if ("<unknown>".equals(newAnchorGUID))
        {
            final String localMethodName = "maintainAnchorGUIDInClassification";

            throw new PropertyServerException(GenericHandlersErrorCode.UNKNOWN_ANCHOR_GUID.getMessageDefinition(localMethodName,
                                                                                                                serviceName,
                                                                                                                methodName),
                                              this.getClass().getName(),
                                              localMethodName);
        }

        /*
         * It is necessary to retrieve any existing classification to know whether the classify or reclassify method required.
         */
        try
        {
            anchorsClassification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                 targetEntity,
                                                                                 OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                                 methodName);
            if (anchorsClassification != null)
            {
                /*
                 * If the target entity has the Anchors classification then this should contain the anchor guid
                 */
                anchorsProperties = anchorsClassification.getProperties();
                if (anchorsProperties != null)
                {
                    currentAnchorGUID = repositoryHelper.getStringProperty(serviceName,
                                                                           OpenMetadataProperty.ANCHOR_GUID.name,
                                                                           anchorsClassification.getProperties(),
                                                                           methodName);
                }
            }
        }
        catch (ClassificationErrorException noAnchorsClassification)
        {
            /*
             * No Anchors Classification - this is not an error - it means the target entity is either an anchor itself, or was not created by an
             * Egeria component.
             */
            anchorsClassification = null;
        }

        /*
         * This is an attempt to trap an intermittent error recorded in issue #4680.
         */
        if ("<unknown>".equals(currentAnchorGUID))
        {
            final String localMethodName = "maintainAnchorGUIDInClassification";

            throw new PropertyServerException(GenericHandlersErrorCode.UNKNOWN_ANCHOR_GUID.getMessageDefinition(localMethodName,
                                                                                                                serviceName,
                                                                                                                methodName),
                                              this.getClass().getName(),
                                              localMethodName);
        }

        /*
         * If the anchor guid has changed then update the value in the classification
         */
        if (((currentAnchorGUID == null) && (newAnchorGUID != null)) ||
                ((currentAnchorGUID != null) && (! currentAnchorGUID.equals(newAnchorGUID))))
        {
            try
            {
                if (anchorIdentifiers != null)
                {
                    anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     anchorsProperties,
                                                                                     OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                     anchorIdentifiers.anchorGUID,
                                                                                     methodName);

                    anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     anchorsProperties,
                                                                                     OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                                     anchorIdentifiers.anchorTypeName,
                                                                                     methodName);

                    anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     anchorsProperties,
                                                                                     OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                                     anchorIdentifiers.anchorDomainName,
                                                                                     methodName);

                    anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     anchorsProperties,
                                                                                     OpenMetadataProperty.ANCHOR_SCOPE_GUID.name,
                                                                                     anchorIdentifiers.anchorScopeGUID,
                                                                                     methodName);
                }

                if (anchorsClassification == null)
                {
                    repositoryHandler.classifyEntity(localServerUserId,
                                                     null,
                                                     null,
                                                     targetGUID,
                                                     null,
                                                     targetGUIDParameterName,
                                                     targetEntity.getType().getTypeDefName(),
                                                     OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                                     OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                     ClassificationOrigin.ASSIGNED,
                                                     null,
                                                     anchorsProperties,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
                }
                else
                {
                    if (newAnchorGUID != null)
                    {
                        repositoryHandler.reclassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           targetGUID,
                                                           targetGUIDParameterName,
                                                           targetTypeName,
                                                           OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                                           OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                           anchorsClassification,
                                                           anchorsProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
                    }
                    else
                    {
                        if (targetEntity instanceof EntityDetail)
                        {
                            repositoryHandler.declassifyEntity(localServerUserId,
                                                               null,
                                                               null,
                                                               targetGUID,
                                                               (EntityDetail) targetEntity,
                                                               targetGUIDParameterName,
                                                               targetEntity.getType().getTypeDefName(),
                                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                               anchorsClassification,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
                        }
                        else
                        {
                            repositoryHandler.declassifyEntity(localServerUserId,
                                                               null,
                                                               null,
                                                               targetGUID,
                                                               null,
                                                               targetGUIDParameterName,
                                                               targetEntity.getType().getTypeDefName(),
                                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                               anchorsClassification,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
                        }
                    }
                }
            }
            catch (Exception   error)
            {
                String typeName = "<null>";
                String typeGUID = "<null>";

                if (targetEntity.getType() != null)
                {
                    typeName = targetEntity.getType().getTypeDefName();
                    typeGUID = targetEntity.getType().getTypeDefGUID();
                }
                auditLog.logException(methodName,
                                      GenericHandlersAuditCode.UNABLE_TO_SET_ANCHORS.getMessageDefinition(serviceName,
                                                                                                          targetGUID,
                                                                                                          typeName,
                                                                                                          typeGUID,
                                                                                                          methodName,
                                                                                                          error.getClass().getName(),
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Walk the graph to locate the anchor for a schema type.  Schemas are attached to each other through various levels of nesting, ports (for
     * process assets) and asset through the schema type.  It is also possible that the schema is not attached to anything.  This is common if the
     * schema is a template.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of schema type (it is assumed that the anchorGUID property of this type is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException  the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException  there is a problem retrieving the instances from the property server or
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForSchemaType(String  userId,
                                                         String  schemaTypeGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        /*
         * The most obvious test is that this schema type is attached directly to the asset.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  schemaTypeGUID,
                                                                                  OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

            anchorIdentifiers.anchorGUID = proxy.getGUID();
            anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();
            anchorIdentifiers.anchorDomainName = OpenMetadataType.ASSET.typeName;

            return anchorIdentifiers;
        }

        /*
         * Next test to see if the type is connected to a SchemaTypeChoice.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        /*
         * Next test to see if the type is connected to a MapSchemaType.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        /*
         * Next test to see if the type is connected to an API operation or API schema type.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.API_HEADER_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.API_HEADER_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.API_REQUEST_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        /*
         * Finally, test that this schema type is attached directly to a port.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                     false,
                                                                     OpenMetadataType.PORT_SCHEMA_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.PORT_SCHEMA_RELATIONSHIP.typeName,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

            anchorIdentifiers.anchorGUID = proxy.getGUID();
            anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

            return anchorIdentifiers;
        }


        /*
         * If none of these relationships are present, then this schema type has no anchor.
         */
        return null;
    }



    /**
     * Walk the graph to locate the anchor for a schema attribute.  Schemas are attached to each other through various levels of nesting, ports (for
     * process assets) and asset through the schema type.  It is also possible that the schema is not attached to anything.  This is common if the
     * schema is a template.
     *
     * @param userId calling user
     * @param attributeGUID unique identifier of attribute (it is assumed that the anchorGUID property of this attribute is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForSchemaAttribute(String  userId,
                                                              String  attributeGUID,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        /*
         * Is the schema attribute connected to a type.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  attributeGUID,
                                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        /*
         * Is the attribute nested in another attribute?  Note that because schema attributes can be nested through multiple levels,
         * the retrieval of the parent needs to take account of which end the attributeGUID is connected to.
         */

        relationship = repositoryHandler.getUniqueParentRelationshipByType(userId,
                                                                           attributeGUID,
                                                                           OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                           OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeGUID,
                                                                           OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                                           true,
                                                                           null,
                                                                           null,
                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                           null,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaAttribute(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a connection.  Connections are attached to each other through various levels of nesting in
     * a VirtualConnection.  They are also typically attached to an Asset.  The Asset is the anchor.  Because embedded connections
     * can be used in multiple VirtualConnections, they are only anchored to a virtual connection if this is done explicitly.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException one of the guids is no longer available
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForConnection(String  userId,
                                                         String  connectionGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        /*
         * Is the connection connected to an asset?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  connectionGUID,
                                                                                  OpenMetadataType.CONNECTION.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            if ((proxy != null) && (proxy.getGUID() != null))
            {
                AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                anchorIdentifiers.anchorGUID = proxy.getGUID();
                anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                return anchorIdentifiers;
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for an endpoint.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the endpoint
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForEndpoint(String  userId,
                                                       String  endpointGUID,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        /*
         * Is the endpoint connected to some infrastructure?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    endpointGUID,
                                                                                    OpenMetadataType.ENDPOINT.typeName,
                                                                                    OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0,
                                                                                    0,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships != null)
        {
            /*
             * The infrastructure can be the anchor.
             */
            if (relationships.size() == 1)
            {
                EntityProxy proxy = relationships.get(0).getEntityOneProxy();

                if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null))
                {
                    AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                    anchorIdentifiers.anchorGUID     = proxy.getGUID();
                    anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                    return anchorIdentifiers;
                }
            }
            else
            {
                return null;
            }
        }

        /*
         * The endpoint is not connected to infrastructure, so try the connection.
         */
        relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                 endpointGUID,
                                                                 OpenMetadataType.ENDPOINT.typeName,
                                                                 OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeGUID,
                                                                 OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                                 1,
                                                                 null,
                                                                 null,
                                                                 SequencingOrder.CREATION_DATE_RECENT,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 0, 0,
                                                                 effectiveTime,
                                                                 methodName);

        if (relationships != null)
        {
            /*
             * Ignore the connection if the endpoint is used in many places.
             */
            if (relationships.size() == 1)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        EntityProxy proxy = relationship.getEntityOneProxy();
                        if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null))
                        {
                            AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                            anchorIdentifiers.anchorGUID     = proxy.getGUID();
                            anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                            return anchorIdentifiers;
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Like.  Likes are attached directly to a referenceable.
     *
     * @param userId calling user
     * @param likeGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException one of the guids is no longer available
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForLike(String  userId,
                                                   String  likeGUID,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        /*
         * Is the like connected to a Referenceable?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  likeGUID,
                                                                                  OpenMetadataType.LIKE.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                anchorIdentifiers.anchorGUID = proxy.getGUID();
                anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                return anchorIdentifiers;
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Rating.  Ratings are attached directly to a referenceable.
     *
     * @param userId calling user
     * @param ratingGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException one of the guids is no longer available
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForRating(String  userId,
                                                     String  ratingGUID,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        /*
         * Is the rating connected to a Referenceable?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  ratingGUID,
                                                                                  OpenMetadataType.RATING.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                anchorIdentifiers.anchorGUID = proxy.getGUID();
                anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                return anchorIdentifiers;
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Survey report.  Survey reports are attached directly to an asset.
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException one of the guids is no longer available
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForSurveyReport(String  userId,
                                                           String  reportGUID,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        /*
         * Is the report connected to an Asset?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  reportGUID,
                                                                                  OpenMetadataType.SURVEY_REPORT.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.REPORT_SUBJECT.typeGUID,
                                                                                  OpenMetadataType.REPORT_SUBJECT.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                anchorIdentifiers.anchorGUID = proxy.getGUID();
                anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                return anchorIdentifiers;
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for an annotation.  Annotations are attached to each other through various levels of nesting
     * and eventually anchored to an asset via an OpenDiscoveryAnalysisReport.   The asset is the anchor.
     *
     * @param userId calling user
     * @param annotationGUID unique identifier of the comment (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForAnnotation(String  userId,
                                                         String  annotationGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        /*
         * Is the annotation connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    annotationGUID,
                                                                                    OpenMetadataType.ANNOTATION.typeName,
                                                                                    OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy proxy = relationship.getEntityOneProxy();
                    if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null) && (! annotationGUID.equals(proxy.getGUID())))
                    {
                        if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataType.SURVEY_REPORT.typeName))
                        {
                            return this.getAnchorGUIDForSurveyReport(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
                        }
                    }
                }
            }
        }

        return null;
    }





    /**
     * Walk the graph to locate the anchor for an annotation review.  AnnotationReviews are attached to annotations which have levels of nesting
     * and eventually anchored to an asset via an OpenDiscoveryAnalysisReport.   The asset is the anchor.
     *
     * @param userId calling user
     * @param annotationReviewGUID unique identifier of the annotation review (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForAnnotationReview(String  userId,
                                                               String  annotationReviewGUID,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing,
                                                               Date    effectiveTime,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        /*
         * Is the annotation review connected to an annotation?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  annotationReviewGUID,
                                                                                  OpenMetadataType.ANNOTATION_REVIEW.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return this.getAnchorGUIDForAnnotation(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for an annotation.  AnnotationReviews are attached to annotations which have various levels of nesting
     * and eventually anchored to an asset via an OpenDiscoveryAnalysisReport.   The asset is the anchor.
     *
     * @param userId calling user
     * @param dataFieldGUID unique identifier of the data field (it is assumed that the anchorGUID property of this instance is null)
     * @param typeName type name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the repositories or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private AnchorIdentifiers getAnchorGUIDForDataField(String  userId,
                                                        String  dataFieldGUID,
                                                        String  typeName,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime,
                                                        String  methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        /*
         * Is the data field connected to a collection?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  dataFieldGUID,
                                                                                  OpenMetadataType.DATA_FIELD.typeName,
                                                                                  false,
                                                                                  OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return this.getAnchorGUIDForDataField(userId, proxy.getGUID(), proxy.getType().getTypeDefName(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
            }
        }

        /*
         * Assume this type of Referenceable is its own anchor.
         */
        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

        anchorIdentifiers.anchorGUID = dataFieldGUID;
        anchorIdentifiers.anchorTypeName = typeName;
        anchorIdentifiers.anchorDomainName = OpenMetadataType.DATA_FIELD.typeName;

        return anchorIdentifiers;
    }


    /**
     * Walk the graph to locate the anchor for a comment.  Comments are attached to each other through various levels of nesting
     * and eventually anchored to a referenceable.   The referenceable is the anchor.  Care has to be taken because a
     * comment is a referenceable too
     *
     * @param userId calling user
     * @param commentGUID unique identifier of the comment (it is assumed that the anchorGUID property of this instance is null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException  there is a problem retrieving the properties from the repositories
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request
     */
    private AnchorIdentifiers getAnchorGUIDForComment(String  userId,
                                                      String  commentGUID,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        /*
         * Is the comment connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    commentGUID,
                                                                                    OpenMetadataType.COMMENT.typeName,
                                                                                    OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy proxy = relationship.getEntityOneProxy();
                    if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null))
                    {
                        if ((! commentGUID.equals(proxy.getGUID())) &&
                                (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataType.COMMENT.typeName)))
                        {
                            AnchorIdentifiers parentAnchorIdentifiers = this.getAnchorGUIDForComment(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);

                            /*
                             * If the parent has no anchor then it is a detached chain of comments and the parent is the anchor.
                             */
                            if (parentAnchorIdentifiers != null)
                            {
                                return parentAnchorIdentifiers;
                            }
                        }

                        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                        anchorIdentifiers.anchorGUID = proxy.getGUID();
                        anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                        return anchorIdentifiers;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Glossary Term.  Glossary Terms are typically their own anchors but
     * may be connected directly to their anchor via a SupplementaryPropertiesProperties relationship.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the Glossary Term (it is assumed that the anchorGUID property of this instance is null)
     * @param typeName type name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException  there is a problem retrieving the properties from the repositories
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request
     */
    private AnchorIdentifiers getAnchorGUIDForGlossaryTerm(String  userId,
                                                           String  glossaryTermGUID,
                                                           String  typeName,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        /*
         * Is the Glossary Term connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    glossaryTermGUID,
                                                                                    OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                    OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy proxy = relationship.getEntityOneProxy();
                    if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null))
                    {
                        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                        anchorIdentifiers.anchorGUID = proxy.getGUID();
                        anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                        return anchorIdentifiers;
                    }
                }
            }
        }

        /*
         * Assume this type of Referenceable is its own anchor.
         */
        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

        anchorIdentifiers.anchorGUID = glossaryTermGUID;
        anchorIdentifiers.anchorTypeName = typeName;
        anchorIdentifiers.anchorDomainName = OpenMetadataType.GLOSSARY_TERM.typeName;

        return anchorIdentifiers;
    }


    /**
     * Walk the graph to locate the anchor for a Glossary Term.  Glossary Terms are typically their own anchors but
     * may be connected directly to their anchor via a SupplementaryPropertiesProperties relationship.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the Glossary Term (it is assumed that the anchorGUID property of this instance is null)
     * @param typeName type name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifiers of attached anchor or null if there is no attached anchor
     *
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException  there is a problem retrieving the properties from the repositories
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request
     */
    private AnchorIdentifiers getAnchorGUIDForISCSegment(String  userId,
                                                         String  glossaryTermGUID,
                                                         String  typeName,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        /*
         * Is the Glossary Term connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    glossaryTermGUID,
                                                                                    OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                    OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy proxy = relationship.getEntityOneProxy();
                    if ((proxy != null) && (proxy.getGUID() != null) && (proxy.getType() != null))
                    {
                        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

                        anchorIdentifiers.anchorGUID = proxy.getGUID();
                        anchorIdentifiers.anchorTypeName = proxy.getType().getTypeDefName();

                        return anchorIdentifiers;
                    }
                }
            }
        }

        /*
         * Assume this type of Referenceable is its own anchor.
         */
        AnchorIdentifiers anchorIdentifiers = new AnchorIdentifiers();

        anchorIdentifiers.anchorGUID = glossaryTermGUID;
        anchorIdentifiers.anchorTypeName = typeName;
        anchorIdentifiers.anchorDomainName = OpenMetadataType.GLOSSARY_TERM.typeName;

        return anchorIdentifiers;
    }


    /**
     * This method walks the relationships to determine if the entity identified by the targetGUID has an anchor.  It returns the GUID of
     * this anchor if it exists or null if it does not.  This method is used both as part of setting up the anchorGUID in a newly linked entity,
     * or to verify that the existing anchorGUID value is still valid.
     *
     * @param targetGUID unique identifier for the entity to test
     * @param targetTypeName type of the entity to test
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return anchorGUID/anchorTypeName or null
     * @throws InvalidParameterException on of the properties is invalid which is suspicious since they have been validated earlier
     * @throws UserNotAuthorizedException the local server userId does not have access to the repository instances
     * @throws PropertyServerException something is wrong with the repository
     */
    AnchorIdentifiers deriveAnchorGUID(String targetGUID,
                                       String targetTypeName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date effectiveTime,
                                       String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        AnchorIdentifiers anchorIdentifiers = null;

        /*
         * This group of calls walks the chain of entities to detect the anchorIdentifiers for specific types of entities.  There is scope for more
         * method calls added here, for example, for comments, note logs, connections etc.
         */
        if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.SCHEMA_TYPE.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForSchemaType(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.SCHEMA_ATTRIBUTE.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForSchemaAttribute(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.CONNECTION.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForConnection(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.ENDPOINT.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForEndpoint(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.COMMENT.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForComment(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.RATING.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForRating(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.LIKE.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForLike(localServerUserId, targetGUID, forLineage,forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.SURVEY_REPORT.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForSurveyReport(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.ANNOTATION.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForAnnotation(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.ANNOTATION_REVIEW.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForAnnotationReview(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.DATA_FIELD.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForDataField(localServerUserId, targetGUID, targetTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.GLOSSARY_TERM.typeName))
        {
            anchorIdentifiers = this.getAnchorGUIDForGlossaryTerm(localServerUserId, targetGUID, targetTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if ((repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.COLLECTION.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.SOLUTION_COMPONENT.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.SOLUTION_BLUEPRINT.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.DATA_STRUCTURE.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.ASSET.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataType.ACTOR.typeName)))
        {
            /*
             * Assume this type of Referenceable is its own anchor.
             */
            anchorIdentifiers = new AnchorIdentifiers();

            anchorIdentifiers.anchorGUID = targetGUID;
            anchorIdentifiers.anchorTypeName = targetTypeName;
        }

        if ((anchorIdentifiers != null) &&
                (anchorIdentifiers.anchorGUID != null) &&
                (anchorIdentifiers.anchorTypeName != null) &&
                (anchorIdentifiers.anchorDomainName == null))
        {
            anchorIdentifiers.anchorDomainName = this.getDomainName(anchorIdentifiers.anchorTypeName);
        }

        return anchorIdentifiers;
    }



    /**
     * Validates that the current anchorGUID is correct and updates it if it is not.  Notice that
     *
     * @param targetGUID unique identifier of the element to validate
     * @param targetGUIDParameterName parameter that provided the guid
     * @param targetTypeName type of entity to validate
     * @param originalAnchorGUID the original anchor guid - may be null
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return newAnchorGUID we derive the anchor from the target element and then return it. Note this value can be null if there is no anchor.
     *
     * @throws InvalidParameterException probably the type of the entity is not correct
     * @throws PropertyServerException there is a problem with the repository
     * @throws UserNotAuthorizedException the local server user id is not able to update the entity
     */
    String reEvaluateAnchorGUID(String targetGUID,
                                String targetGUIDParameterName,
                                String targetTypeName,
                                String originalAnchorGUID,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date effectiveTime,
                                String methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        /*
         * Find out the anchorGUID by following the relationships
         */
        AnchorIdentifiers newAnchorIdentifiers = this.deriveAnchorGUID(targetGUID, targetTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        /*
         * The anchorGUID has changed
         */
        if (((newAnchorIdentifiers == null) && (originalAnchorGUID != null)) ||
                ((newAnchorIdentifiers != null) && (! newAnchorIdentifiers.anchorGUID.equals(originalAnchorGUID))))
        {
            EntityDetail targetElement = repositoryHandler.getEntityByGUID(localServerUserId,
                                                                           targetGUID,
                                                                           targetGUIDParameterName,
                                                                           targetTypeName,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName);

            if (targetElement != null)
            {
                this.maintainAnchorGUIDInClassification(targetElement.getGUID(),
                                                        targetGUIDParameterName,
                                                        targetTypeName,
                                                        targetElement,
                                                        newAnchorIdentifiers,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
            }
        }

        if (newAnchorIdentifiers != null)
        {
            return newAnchorIdentifiers.anchorGUID;
        }

        return null;
    }




    /**
     * Return the domain name of an entity.
     *
     * @param instance entity
     * @return domain name
     */
    public String getDomainName(EntitySummary  instance)
    {
        return getDomainName(instance.getType().getTypeDefName());
    }


    /**
     * Return the domain name of a type.
     *
     * @param typeName type to check
     * @return domain name
     */
    public String getDomainName(String  typeName)
    {
        String anchorDomainName = typeName;

        if (typeName != null)
        {
            List<TypeDefLink> superTypes = repositoryHelper.getSuperTypes(serviceName, typeName);

            if (superTypes != null)
            {
                /*
                 * The super types are listed in increasing levels of abstraction.
                 * Eg [DataSet, Asset, Referenceable, OpenMetadataRoot].
                 * In this example, the domain is Asset (one below Referenceable).
                 */
                for (TypeDefLink typeDefLink : superTypes)
                {
                    if ((!OpenMetadataType.OPEN_METADATA_ROOT.typeName.equals(typeDefLink.getName())) &&
                            (!OpenMetadataType.REFERENCEABLE.typeName.equals(typeDefLink.getName())))
                    {
                        anchorDomainName = typeDefLink.getName();
                    }
                }
            }
        }

        return anchorDomainName;
    }



    /**
     * Validates that the current anchorGUID is correct and updates it if it is not.
     *
     * @param targetGUID unique identifier of the element to validate
     * @param targetGUIDParameterName parameter name for target GUID
     * @param targetElement target entity already retrieved
     * @param targetTypeName type of entity to validate
     * @param originalAnchorGUID the original anchor guid - may be null
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return the new anchor GUID
     *
     * @throws InvalidParameterException probably the type of the entity is not correct
     * @throws PropertyServerException there is a problem with the repository
     * @throws UserNotAuthorizedException the local server user id is not able to update the entity
     */
    String reEvaluateAnchorGUID(String       targetGUID,
                                String       targetGUIDParameterName,
                                String       targetTypeName,
                                EntityDetail targetElement,
                                String       originalAnchorGUID,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        /*
         * Find out the anchorGUID by following the relationships
         */
        AnchorIdentifiers newAnchorIdentifiers = this.deriveAnchorGUID(targetGUID, targetTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        /*
         * Has the anchorGUID changed
         */
        if (((newAnchorIdentifiers == null) && (originalAnchorGUID != null)) ||
                ((newAnchorIdentifiers != null) && (! newAnchorIdentifiers.anchorGUID.equals(originalAnchorGUID))))
        {
            if (targetElement != null)
            {
                this.maintainAnchorGUIDInClassification(targetElement.getGUID(),
                                                        targetGUIDParameterName,
                                                        targetTypeName,
                                                        targetElement,
                                                        newAnchorIdentifiers,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
            }
        }

        if (newAnchorIdentifiers != null)
        {
            return newAnchorIdentifiers.anchorGUID;
        }

        return null;
    }


    /**
     * Retrieve the anchor guid for an entity.  Ths is in two phases.  If the connectToEntity has an Anchors
     * classification then the anchor information is extracted from that classification.  If there is on
     * Anchors classification then a new one is derived and added to the connectToEntity for the next time
     * it is retrieved.
     *
     * @param connectToEntity  entity retrieved from the repository
     * @param connectToGUIDParameterName  name of the parameter that passed the connectTo guid
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     * @return anchor identifiers
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AnchorIdentifiers getAnchorGUIDForEntity(EntityDetail connectToEntity,
                                                    String       connectToGUIDParameterName,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    Date         effectiveTime,
                                                    String       methodName) throws PropertyServerException,
                                                                                    InvalidParameterException,
                                                                                    UserNotAuthorizedException
    {
        invalidParameterHandler.validateObject(connectToEntity, connectToGUIDParameterName, methodName);

        /*
         * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
         * The exception occurs where the entity is not being managed by this handler, or something equivalent that maintains the Anchors
         * classification.
         */
        AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(connectToEntity, methodName);

        if (anchorIdentifiers == null)
        {
            /*
             * The classification is missing - so walk the relationships to find the anchor if it exists.
             */
            anchorIdentifiers = deriveAnchorGUID(connectToEntity.getGUID(), connectToEntity.getType().getTypeDefName(), forLineage, forDuplicateProcessing, effectiveTime, methodName);

            if (anchorIdentifiers != null)
            {
                /*
                 * The anchor has been found so store it in the classification, so it is easy to find next time.
                 */
                maintainAnchorGUIDInClassification(connectToEntity.getGUID(),
                                                   connectToGUIDParameterName,
                                                   connectToEntity.getType().getTypeDefName(),
                                                   connectToEntity,
                                                   anchorIdentifiers,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        return anchorIdentifiers;
    }


    /**
     * Classify an element with the Anchors classification.
     *
     * @param userId calling user
     * @param anchoredElement element to add the classification to
     * @param anchoredElementGUIDParameterName name of parameter
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName unique name of the type of the anchor
     * @param anchorDomainName unique name of the type of the anchor
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addAnchorsClassification(String       userId,
                                         EntityDetail anchoredElement,
                                         String       anchoredElementGUIDParameterName,
                                         String       anchorGUID,
                                         String       anchorTypeName,
                                         String       anchorDomainName,
                                         String       anchorScopeGUID,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException,
                                                                         InvalidParameterException
    {
        AnchorIdentifiers anchorIdentifiers = getAnchorsFromAnchorsClassification(anchoredElement, methodName);

        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        if (anchorIdentifiers == null)
        {
            repositoryHandler.classifyEntity(userId,
                                             null,
                                             null,
                                             anchoredElement.getGUID(),
                                             anchoredElement,
                                             anchoredElementGUIDParameterName,
                                             anchoredElement.getType().getTypeDefName(),
                                             OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                             OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                             ClassificationOrigin.ASSIGNED,
                                             null,
                                             builder.getAnchorsProperties(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName),
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
        }
        else
        {
            repositoryHandler.reclassifyEntity(userId,
                                               null,
                                               null,
                                               anchoredElement.getGUID(),
                                               anchoredElementGUIDParameterName,
                                               anchoredElement.getType().getTypeDefName(),
                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                               OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                               null,
                                               builder.getAnchorsProperties(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
        }
    }
}
