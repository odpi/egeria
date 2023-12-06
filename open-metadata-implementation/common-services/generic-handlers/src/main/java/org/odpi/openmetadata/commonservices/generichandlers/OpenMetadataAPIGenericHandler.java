/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.*;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OpenMetadataAPIGenericHandler manages the exchange of Open Metadata API Bean content with the repository services
 * (via the repository handler).
 */
public class OpenMetadataAPIGenericHandler<B>
{
    protected OpenMetadataAPIGenericConverter<B> converter;
    protected Class<B>                           beanClass;

    protected String                             serviceName;
    protected String                             serverName;
    protected OMRSRepositoryHelper               repositoryHelper;
    protected RepositoryHandler                  repositoryHandler;
    protected InvalidParameterHandler            invalidParameterHandler;

    protected String                             localServerUserId;
    protected OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();


    protected List<String>                       supportedZones;
    protected List<String>                       publishZones;
    protected List<String>                       defaultZones;

    protected RepositoryErrorHandler             errorHandler;

    private static final Logger log = LoggerFactory.getLogger(OpenMetadataAPIGenericHandler.class);
    private static final String assetActionDescription = "userAssetMonitoring";

    protected AuditLog                           auditLog;

    private final static String supplementaryPropertiesQualifiedNamePostFix = " Supplementary Properties";
    private final static String supplementaryPropertiesGlossaryName = "Supplementary Properties Glossary";
    private final static String supplementaryPropertiesGlossaryParameterName = "supplementaryPropertiesGlossaryName";
    private final static String supplementaryPropertiesGlossaryDescription =
            "This glossary contains glossary terms containing the business-oriented descriptive names and related properties for " +
                    "open metadata assets.";

    private final List<String> qualifiedNamePropertyNamesList;


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
    public OpenMetadataAPIGenericHandler(OpenMetadataAPIGenericConverter<B> converter,
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
        this.converter               = converter;
        this.beanClass               = beanClass;

        this.serviceName             = serviceName;
        this.serverName              = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler       = repositoryHandler;
        this.repositoryHelper        = repositoryHelper;
        this.localServerUserId       = localServerUserId;

        if (securityVerifier != null)
        {
            this.securityVerifier    = securityVerifier;
        }

        this.supportedZones          = supportedZones;
        this.defaultZones            = defaultZones;
        this.publishZones            = publishZones;

        this.auditLog                = auditLog;

        this.errorHandler            = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);

        this.qualifiedNamePropertyNamesList = new ArrayList<>();
        this.qualifiedNamePropertyNamesList.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
    }


    /**
     * Return this handler's converter.
     *
     * @return converter
     */
    public OpenMetadataAPIGenericConverter<B> getConverter()
    {
        return converter;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this method is called).
     * <br><br>
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }


    /**
     * Return the repository helper for this server.
     *
     * @return repository helper object
     */
    public OMRSRepositoryHelper getRepositoryHelper()
    {
        return repositoryHelper;
    }


    /**
     * Return the repository handler for this server.
     *
     * @return repository handler object
     */
    public RepositoryHandler getRepositoryHandler()
    {
        return repositoryHandler;
    }


    /**
     * Return the list of zones to use for retrieving assets.
     *
     * @return list of zone names
     */
    public List<String> getSupportedZones()
    {
        return supportedZones;
    }


    /**
     * Return the type definition for the named type.
     *
     * @param suppliedTypeName caller's subtype (or null)
     * @param defaultTypeName common super type
     *
     * @return type definition
     */
    public TypeDef getTypeDefByName(String suppliedTypeName,
                                    String defaultTypeName)
    {
        String resultsTypeName = defaultTypeName;

        if (suppliedTypeName != null)
        {
            resultsTypeName = suppliedTypeName;
        }

        return repositoryHelper.getTypeDefByName(serviceName, resultsTypeName);
    }


    /**
     * Return the name of this service.
     *
     * @return string name
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Return the name of this server.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param classificationProperties properties to save in the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String              userId,
                                              String              externalSourceGUID,
                                              String              externalSourceName,
                                              String              beanGUID,
                                              String              beanGUIDParameterName,
                                              String              beanGUIDTypeName,
                                              String              classificationTypeGUID,
                                              String              classificationTypeName,
                                              Date                effectiveFrom,
                                              Date                effectiveTo,
                                              boolean             forLineage,
                                              boolean             forDuplicateProcessing,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> classificationProperties,
                                              Date                effectiveTime,
                                              String              methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        InstanceProperties instanceProperties = null;

        try
        {
            instanceProperties = repositoryHelper.addPropertyMapToInstance(serviceName, null, classificationProperties, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            final String  propertyName = "relationshipProperties";

            errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
        }

        this.setUpEffectiveDates(instanceProperties, effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           classificationTypeGUID,
                                           classificationTypeName,
                                           instanceProperties,
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              String             beanGUID,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              boolean            isMergeUpdate,
                                              boolean            forLineage,
                                              boolean            forDuplicateProcessing,
                                              Date               effectiveTime,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        setClassificationInRepository(userId, externalSourceGUID, externalSourceName, beanGUID, beanGUIDParameterName, beanGUIDTypeName, classificationTypeGUID, classificationTypeName, classificationProperties, isMergeUpdate, forLineage, forDuplicateProcessing, supportedZones, effectiveTime, methodName);
    }

    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              String             beanGUID,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              boolean            isMergeUpdate,
                                              boolean            forLineage,
                                              boolean            forDuplicateProcessing,
                                              List<String>       serviceSupportedZones,
                                              Date               effectiveTime,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail beanEntity = getEntityFromRepository(userId,
                                                          beanGUID,
                                                          beanGUIDParameterName,
                                                          beanGUIDTypeName,
                                                          null,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          serviceSupportedZones,
                                                          effectiveTime,
                                                          methodName);

        setClassificationInRepository(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      beanEntity,
                                      beanGUIDParameterName,
                                      beanGUIDTypeName,
                                      classificationTypeGUID,
                                      classificationTypeName,
                                      classificationProperties,
                                      isMergeUpdate,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanEntity entity that the classification is for
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param forLineage the query is for lineage so ignore Memento classifications
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              EntityDetail       beanEntity,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              boolean            isMergeUpdate,
                                              boolean            forLineage,
                                              boolean            forDuplicateProcessing,
                                              List<String>       serviceSupportedZones,
                                              Date               effectiveTime,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (beanEntity != null)
        {
            EntityDetail anchorEntity = validateAnchorEntity(userId,
                                                             beanEntity.getGUID(),
                                                             beanGUIDTypeName,
                                                             beanEntity,
                                                             beanGUIDParameterName,
                                                             true,
                                                             true,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             serviceSupportedZones,
                                                             effectiveTime,
                                                             methodName);

            Classification existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            /*
             * Classify the asset
             */
            int latestChangeActionOrdinal;
            if (existingClassification == null)
            {
                latestChangeActionOrdinal = OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL;

                repositoryHandler.classifyEntity(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 beanEntity.getGUID(),
                                                 beanEntity,
                                                 beanGUIDParameterName,
                                                 beanGUIDTypeName,
                                                 classificationTypeGUID,
                                                 classificationTypeName,
                                                 ClassificationOrigin.ASSIGNED,
                                                 null,
                                                 classificationProperties,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
            }
            else
            {
                latestChangeActionOrdinal = OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL;

                InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                      classificationProperties,
                                                                      existingClassification.getProperties());

                /*
                 * If there are no properties to change then nothing more to do
                 */
                if ((newProperties == null) && (existingClassification.getProperties() == null))
                {
                    auditLog.logMessage(methodName,
                                        GenericHandlersAuditCode.IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE.getMessageDefinition(classificationTypeName,
                                                                                                                         beanEntity.getGUID(),
                                                                                                                         methodName,
                                                                                                                         userId));
                    return;
                }

                /*
                 * If nothing has changed in the properties then nothing to do
                 */
                if ((newProperties != null) && (newProperties.equals(existingClassification.getProperties())))
                {
                    auditLog.logMessage(methodName,
                                        GenericHandlersAuditCode.IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE.getMessageDefinition(classificationTypeName,
                                                                                                                                 beanEntity.getGUID(),
                                                                                                                                 methodName,
                                                                                                                                 userId));
                    return;
                }

                repositoryHandler.reclassifyEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   beanEntity.getGUID(),
                                                   beanGUIDParameterName,
                                                   beanGUIDTypeName,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   newProperties,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }

            final String actionDescriptionTemplate = "Adding %s classification to %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             beanEntity.getGUID(),
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityDates(String       userId,
                                                     String       externalSourceGUID,
                                                     String       externalSourceName,
                                                     String       beanGUID,
                                                     String       beanGUIDParameterName,
                                                     String       beanGUIDTypeName,
                                                     String       classificationTypeGUID,
                                                     String       classificationTypeName,
                                                     boolean      forLineage,
                                                     boolean      forDuplicateProcessing,
                                                     Date         effectiveFrom,
                                                     Date         effectiveTo,
                                                     List<String> serviceSupportedZones,
                                                     Date         effectiveTime,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          true,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          serviceSupportedZones,
                                                          effectiveTime,
                                                          methodName);

        if (beanEntity != null)
        {
            Classification existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            invalidParameterHandler.validateObject(existingClassification, classificationTypeName, methodName);

            int latestChangeActionOrdinal = OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL;

            InstanceProperties newProperties = existingClassification.getProperties();

            if (newProperties == null)
            {
                newProperties = new InstanceProperties();
            }

            repositoryHandler.reclassifyEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               beanEntity.getGUID(),
                                               beanGUIDParameterName,
                                               beanGUIDTypeName,
                                               classificationTypeGUID,
                                               classificationTypeName,
                                               existingClassification,
                                               this.setUpEffectiveDates(newProperties, effectiveFrom, effectiveTo),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);

            final String actionDescriptionTemplate = "Updating effectivity dates for %s classification to %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             beanGUID,
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Update the effectivity dates of a specific entity .
     * The effectivity dates control the visibility of the entity through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeGUID type of bean
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateBeanEffectivityDates(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       beanGUID,
                                           String       beanGUIDParameterName,
                                           String       beanGUIDTypeGUID,
                                           String       beanGUIDTypeName,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        updateBeanEffectivityDates(userId, externalSourceGUID, externalSourceName, beanGUID, beanGUIDParameterName, beanGUIDTypeGUID, beanGUIDTypeName, forLineage, forDuplicateProcessing, effectiveFrom, effectiveTo, supportedZones, effectiveTime, methodName);
    }


    /**
     * Update the effectivity dates of a specific entity .
     * The effectivity dates control the visibility of the entity through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeGUID type of bean
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateBeanEffectivityDates(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       beanGUID,
                                           String       beanGUIDParameterName,
                                           String       beanGUIDTypeGUID,
                                           String       beanGUIDTypeName,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           List<String> serviceSupportedZones,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          true,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          serviceSupportedZones,
                                                          effectiveTime,
                                                          methodName);

        if (beanEntity != null)
        {
            InstanceProperties newProperties = this.setUpEffectiveDates(null, effectiveFrom, effectiveTo);

            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     beanEntity.getGUID(),
                                                     beanEntity,
                                                     beanGUIDTypeGUID,
                                                     beanGUIDTypeName,
                                                     newProperties,
                                                     methodName);

            final String actionDescriptionTemplate = "Updating effectivity dates for %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, beanGUIDTypeName, beanGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             beanGUID,
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             OpenMetadataAPIMapper.ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }

    /**
     * Update the effectivity dates of a specific relationship.
     * The effectivity dates control the visibility of the relationship through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the entity in the repositories
     * @param relationshipGUIDParameterName parameter name that passed the relationshipGUID
     * @param relationshipGUIDTypeName type of relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipEffectivityDates(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  relationshipGUID,
                                                   String  relationshipGUIDParameterName,
                                                   String  relationshipGUIDTypeName,
                                                   Date    effectiveFrom,
                                                   Date    effectiveTo,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        updateRelationshipEffectivityDates(userId, externalSourceGUID, externalSourceName, relationshipGUID, relationshipGUIDParameterName, relationshipGUIDTypeName, effectiveFrom, effectiveTo, forLineage, forDuplicateProcessing, supportedZones, effectiveTime, methodName);
    }

    /**
     * Update the effectivity dates of a specific relationship.
     * The effectivity dates control the visibility of the relationship through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the entity in the repositories
     * @param relationshipGUIDParameterName parameter name that passed the relationshipGUID
     * @param relationshipGUIDTypeName type of relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipEffectivityDates(String       userId,
                                                   String       externalSourceGUID,
                                                   String       externalSourceName,
                                                   String       relationshipGUID,
                                                   String       relationshipGUIDParameterName,
                                                   String       relationshipGUIDTypeName,
                                                   Date         effectiveFrom,
                                                   Date         effectiveTo,
                                                   boolean      forLineage,
                                                   boolean      forDuplicateProcessing,
                                                   List<String> serviceSupportedZones,
                                                   Date         effectiveTime,
                                                   String       methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipGUIDTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (relationship != null)
        {
            final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
            final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

            this.validateAnchorEntity(userId,
                                      relationship.getEntityOneProxy().getGUID(),
                                      entityOneParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      effectiveTime,
                                      methodName);

            this.validateAnchorEntity(userId,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      entityTwoParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      effectiveTime,
                                      methodName);

            InstanceProperties newProperties = relationship.getProperties();

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationshipGUID,
                                                           this.setUpEffectiveDates(newProperties, effectiveFrom, effectiveTo),
                                                           methodName);

        }
    }


    /**
     * Create the new properties to hand to the repository helper based on the supplied properties,
     * existing properties and whether this is a merge update or not.
     * The effectivity dates are always preserved unless they are over-written by the caller.  If they need clearing then use the separate call.
     *
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param suppliedProperties properties from the caller
     * @param existingProperties properties from the repository
     * @return properties to store
     */
    private InstanceProperties setUpNewProperties(boolean            isMergeUpdate,
                                                  InstanceProperties suppliedProperties,
                                                  InstanceProperties existingProperties)
    {
        /*
         * Sort out the properties
         */
        InstanceProperties newProperties;

        if (isMergeUpdate)
        {
            newProperties = existingProperties;

            if (newProperties == null)
            {
                newProperties = suppliedProperties;
            }
            else if (suppliedProperties != null)
            {
                Map<String, InstancePropertyValue>  propertyMap = suppliedProperties.getInstanceProperties();

                for (String propertyName : propertyMap.keySet())
                {
                    if (propertyName != null)
                    {
                        newProperties.setProperty(propertyName, propertyMap.get(propertyName));
                    }
                }

                if (suppliedProperties.getEffectiveFromTime() != null)
                {
                    newProperties.setEffectiveFromTime(suppliedProperties.getEffectiveFromTime());
                }

                if (suppliedProperties.getEffectiveToTime() != null)
                {
                    newProperties.setEffectiveToTime(suppliedProperties.getEffectiveToTime());
                }
            }
        }
        else
        {
            newProperties = suppliedProperties;

            /*
             * Preserve the effectivity dates even though the properties are being replaced.
             */
            if (existingProperties != null)
            {
                if (existingProperties.getEffectiveFromTime() != null)
                {
                    newProperties.setEffectiveFromTime(existingProperties.getEffectiveFromTime());
                }
                if (existingProperties.getEffectiveToTime() != null)
                {
                    newProperties.setEffectiveToTime(existingProperties.getEffectiveToTime());
                }
            }
        }

        return newProperties;
    }


    /**
     * Update the properties associated with a relationship.  Effectivity dates are unchanged.
     *
     * @param userId caller's userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the relationship to update
     * @param relationshipGUIDParameterName  name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName type name of relationship if known (null is ok)
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties new properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipProperties(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             relationshipGUID,
                                             String             relationshipGUIDParameterName,
                                             String             relationshipTypeName,
                                             boolean            isMergeUpdate,
                                             InstanceProperties relationshipProperties,
                                             boolean            forLineage,
                                             boolean            forDuplicateProcessing,
                                             Date               effectiveTime,
                                             String             methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        updateRelationshipProperties(userId, externalSourceGUID, externalSourceName, relationshipGUID, relationshipGUIDParameterName, relationshipTypeName, isMergeUpdate, relationshipProperties, forLineage, forDuplicateProcessing, supportedZones, effectiveTime, methodName);
    }

    /**
     * Update the properties associated with a relationship.  Effectivity dates are unchanged.
     *
     * @param userId caller's userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the relationship to update
     * @param relationshipGUIDParameterName  name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName type name of relationship if known (null is ok)
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties new properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipProperties(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             relationshipGUID,
                                             String             relationshipGUIDParameterName,
                                             String             relationshipTypeName,
                                             boolean            isMergeUpdate,
                                             InstanceProperties relationshipProperties,
                                             boolean            forLineage,
                                             boolean            forDuplicateProcessing,
                                             List<String>       serviceSupportedZones,
                                             Date               effectiveTime,
                                             String             methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
        {
            /*
             * Sort out the properties
             */
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  relationshipProperties,
                                                                  relationship.getProperties());

            final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
            final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

            this.validateAnchorEntity(userId,
                                      relationship.getEntityOneProxy().getGUID(),
                                      entityOneParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      effectiveTime,
                                      methodName);

            this.validateAnchorEntity(userId,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      entityTwoParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      serviceSupportedZones,
                                      effectiveTime,
                                      methodName);


            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (relationship.getProperties() == null))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE.getMessageDefinition(relationship.getType().getTypeDefName(),
                                                                                                                           relationship.getGUID(),
                                                                                                                           methodName,
                                                                                                                           userId));
                return;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(relationship.getProperties())))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE.getMessageDefinition(relationship.getType().getTypeDefName(),
                                                                                                                           relationship.getGUID(),
                                                                                                                           methodName,
                                                                                                                           userId));
                return;
            }

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
                                                           methodName);
        }
    }



    /**
     * Update the properties associated with a relationship.  Effectivity dates are unchanged.
     *
     * @param userId caller's userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the relationship to update
     * @param relationshipGUIDParameterName  name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName type name of relationship if known (null is ok)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime time when the relationship is effective
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelationship(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  relationshipGUID,
                                   String  relationshipGUIDParameterName,
                                   String  relationshipTypeName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
        {
            final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
            final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

            this.validateAnchorEntity(userId,
                                      relationship.getEntityOneProxy().getGUID(),
                                      entityOneParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);

            this.validateAnchorEntity(userId,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      entityTwoParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);

            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);
        }
    }


    /**
     * Locate the requested classification in the supplied entity.
     *
     * @param beanEntity entity potentially containing the classification
     * @param classificationTypeName unique name of classification type
     */
    private Classification getExistingClassification(EntityDetail beanEntity,
                                                     String       classificationTypeName)
    {
        Classification  existingClassification  = null;

        if (beanEntity != null)
        {
            /*
             * Look to see if there is an existing classification
             */
            List<Classification> existingClassifications = beanEntity.getClassifications();
            if (existingClassifications != null)
            {
                for (Classification classification : existingClassifications)
                {
                    if (classification != null)
                    {
                        if (classificationTypeName.equals(classification.getName()))
                        {
                            existingClassification = classification;
                        }
                    }
                }
            }
        }

        return existingClassification;
    }


    /**
     * Remove the requested classification from the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void removeClassificationFromRepository(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  beanGUID,
                                                   String  beanGUIDParameterName,
                                                   String  beanGUIDTypeName,
                                                   String  classificationTypeGUID,
                                                   String  classificationTypeName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        removeClassificationFromRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           classificationTypeGUID,
                                           classificationTypeName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the requested classification from the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    public void removeClassificationFromRepository(String       userId,
                                                   String       externalSourceGUID,
                                                   String       externalSourceName,
                                                   String       beanGUID,
                                                   String       beanGUIDParameterName,
                                                   String       beanGUIDTypeName,
                                                   String       classificationTypeGUID,
                                                   String       classificationTypeName,
                                                   boolean      forLineage,
                                                   boolean      forDuplicateProcessing,
                                                   List<String> serviceSupportedZones,
                                                   Date         effectiveTime,
                                                   String       methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          true,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          serviceSupportedZones,
                                                          effectiveTime,
                                                          methodName);

        if (beanEntity != null)
        {
            /*
             * Look to see if there is an existing classification
             */
            InstanceAuditHeader existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            if (existingClassification != null)
            {
                repositoryHandler.declassifyEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   beanEntity.getGUID(),
                                                   beanEntity,
                                                   beanGUIDParameterName,
                                                   beanGUIDTypeName,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);

                final String actionDescriptionTemplate = "Removing %s classification from %s %s";
                String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

                if (anchorEntity != null)
                {
                    this.addLatestChangeToAnchor(anchorEntity,
                                                 OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 classificationTypeName,
                                                 beanGUID,
                                                 beanGUIDTypeName,
                                                 null,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
                else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                {
                    this.addLatestChangeToAnchor(beanEntity,
                                                 OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 classificationTypeName,
                                                 null,
                                                 null,
                                                 null,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
        }
    }


    /**
     * Work out whether the relationship permits the requesting user to traverse along the relationship.  This is determined by the
     * isPublic property on relationships that represent feedback on another object.
     *
     * @param userId calling user
     * @param relationship relationship to the feedback content
     * @param methodName calling method
     * @return boolean - true if allowed
     */
    private boolean visibleToUserThroughRelationship(String        userId,
                                                     Relationship  relationship,
                                                     String        methodName)
    {
        if (relationship == null)
        {
            return false;
        }

        String relationshipTypeName = null;

        if (relationship.getType() != null)
        {
            relationshipTypeName = relationship.getType().getTypeDefName();
        }

        if (relationshipTypeName == null)
        {
            /*
             * Strictly speaking this relationship is in error. Returning false will cause it to be ignored.
             */
            return false;
        }

        /*
         * These are the feedback relationships.  They have a property called "isPrivate".  If it is set to true, only the user
         * that created it can see it (or update or delete it).
         */
        if ((repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME)) ||
            (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME)) ||
            (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_NAME)) ||
            (repositoryHelper.isTypeOf(serviceName, relationshipTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME)))
        {
            if (userId.equals(relationship.getCreatedBy()))
            {
                return true;
            }

            return repositoryHelper.getBooleanProperty(serviceName,
                                                       OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                       relationship.getProperties(),
                                                       methodName);
        }

        return true;
    }



    /**
     * Retrieve the anchorGUID property from the Anchors classification if present.  A null is returned if the Anchors classification
     * is missing, or the property is missing from the classification or is null.
     *
     * @param connectToEntity entity containing the classifications
     * @param methodName calling method
     * @return anchorGUID or null
     */
    public AnchorIdentifiers getAnchorGUIDFromAnchorsClassification(EntityDetail connectToEntity,
                                                                    String       methodName)
    {
        /*
         * Metadata maintained by Egeria Access Service modules should have the Anchors classification.
         */
        Classification     anchorsClassification;
        AnchorIdentifiers  anchorIdentifiers = null;

        try
        {
            anchorsClassification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                 connectToEntity,
                                                                                 OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                                                                    OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                    anchorsClassification.getProperties(),
                                                                    methodName);

                    anchorIdentifiers.anchorTypeName = repositoryHelper.getStringProperty(serviceName,
                                                                                          OpenMetadataAPIMapper.ANCHOR_TYPE_NAME_PROPERTY_NAME,
                                                                                          anchorsClassification.getProperties(),
                                                                                          methodName);

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
                errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME);
            }
            catch (PropertyServerException secondError)
            {
                // Not able to log exception
            }
        }

        return anchorIdentifiers;
    }


    /**
     * Set up the Anchors classification in an entity (and any child anchored entity connected to it).  This is done using the local server's user id
     * and assumes these classifications are maintained in the local cohort.
     *
     * @param targetGUID unique identifier for the entity
     * @param targetGUIDParameterName parameter name supplying targetGUID
     * @param targetTypeName type of entity
     * @param targetEntity contents of the entity from the repository
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName unique identifier of the anchor entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException problem with one of the properties
     * @throws PropertyServerException problem within the repository services
     * @throws UserNotAuthorizedException local server user not allowed to issue the request.
     */
    private void maintainAnchorGUIDInClassification(String        targetGUID,
                                                    String        targetGUIDParameterName,
                                                    String        targetTypeName,
                                                    EntitySummary targetEntity,
                                                    String        anchorGUID,
                                                    String        anchorTypeName,
                                                    boolean       forLineage,
                                                    boolean       forDuplicateProcessing,
                                                    Date          effectiveTime,
                                                    String        methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        Classification     anchorsClassification;
        InstanceProperties anchorsProperties = null;
        String             currentAnchorGUID = null;

        /*
         * This is an attempt to trap an intermittent error recorded in issue #4680.
         */
        if ("<unknown>".equals(anchorGUID))
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
                                                                                 OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                                                                           OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
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
        if (((currentAnchorGUID == null) && (anchorGUID != null)) ||
            ((currentAnchorGUID != null) && (! currentAnchorGUID.equals(anchorGUID))))
        {
            try
            {
                anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                 anchorsProperties,
                                                                                 OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                                 anchorGUID,
                                                                                 methodName);

                anchorsProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                 anchorsProperties,
                                                                                 OpenMetadataAPIMapper.ANCHOR_TYPE_NAME_PROPERTY_NAME,
                                                                                 anchorTypeName,
                                                                                 methodName);

                if (anchorsClassification == null)
                {
                    repositoryHandler.classifyEntity(localServerUserId,
                                                     null,
                                                     null,
                                                     targetGUID,
                                                     null,
                                                     targetGUIDParameterName,
                                                     targetEntity.getType().getTypeDefName(),
                                                     OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                     OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                    if (anchorGUID != null)
                    {
                        repositoryHandler.reclassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           targetGUID,
                                                           targetGUIDParameterName,
                                                           targetTypeName,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                                                               OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                               OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                                                               OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                               OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
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
                                                                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
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
         * Next test to see if the type is connected to an attribute.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaAttribute(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        /*
         * Next test to see if the type is connected to a SchemaTypeChoice.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_NAME,
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
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     false,
                                                                     OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
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
                                                                                  OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
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
                                                                           OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                           true,
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
                                                                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                                  true,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
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
                                                                                  OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
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
                                                                                  OpenMetadataAPIMapper.RATING_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
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
    private AnchorIdentifiers getAnchorGUIDForOpenDiscoveryAnalysisReport(String  userId,
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
                                                                                  OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_NAME,
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
                                                                                    OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME,
                                                                                    null,
                                                                                    null,
                                                                                    1,
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
                        if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME))
                        {
                            return this.getAnchorGUIDForOpenDiscoveryAnalysisReport(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
                        }
                        else if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME))
                        {
                            AnchorIdentifiers parentAnchorIdentifiers = this.getAnchorGUIDForDataField(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);

                            if (parentAnchorIdentifiers != null)
                            {
                                return parentAnchorIdentifiers;
                            }
                        }
                        else if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME))
                        {
                            return this.getAnchorGUIDForAnnotation(userId, proxy.getGUID(), forLineage, forDuplicateProcessing, effectiveTime, methodName);
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
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_LINK_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_LINK_TYPE_NAME,
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
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime,
                                                        String  methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        /*
         * Is the data field connected to an annotation?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  dataFieldGUID,
                                                                                  OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                                  false,
                                                                                  OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_NAME,
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
                                                                                    OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                                                                    1,
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
                            (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.COMMENT_TYPE_NAME)))
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
     * Walk the graph to locate the anchor for a Glossary Term.  Glossary Terms are connected directly to their anchor
     * Glossary via a TermAnchor relationship.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the Glossary Term (it is assumed that the anchorGUID property of this instance is null)
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
                                                                                    OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.TERM_ANCHOR_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.TERM_ANCHOR_TYPE_NAME,
                                                                                    1,
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

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Glossary Category.  Glossary Categories are connected directly to their anchor Glossary
     * via a CategoryAnchor relationship.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the Glossary Category (it is assumed that the anchorGUID property of this instance is null)
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
    private AnchorIdentifiers getAnchorGUIDForGlossaryCategory(String  userId,
                                                               String  glossaryCategoryGUID,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing,
                                                               Date    effectiveTime,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        /*
         * Is the Glossary Category connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    glossaryCategoryGUID,
                                                                                    OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                                    OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_NAME,
                                                                                    1,
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

        return null;
    }


    /**
     * The properties for an Anchors classification.
     */
    static class AnchorIdentifiers
    {
        String anchorGUID = null;
        String anchorTypeName = null;
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
    private AnchorIdentifiers deriveAnchorGUID(String  targetGUID,
                                               String  targetTypeName,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        AnchorIdentifiers anchorIdentifiers = null;

        /*
         * This group of calls walks the chain of entities to detect the anchorIdentifiers for specific types of entities.  There is scope for more
         * method calls added here, for example, for comments, note logs, connections etc.
         */
        if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForSchemaType(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForSchemaAttribute(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.CONNECTION_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForConnection(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.COMMENT_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForComment(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.RATING_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForRating(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.LIKE_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForLike(localServerUserId, targetGUID, forLineage,forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForOpenDiscoveryAnalysisReport(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForAnnotation(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForAnnotationReview(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForDataField(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForGlossaryTerm(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME))
        {
            anchorIdentifiers = this.getAnchorGUIDForGlossaryCategory(localServerUserId, targetGUID, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        return anchorIdentifiers;
    }


    /**
     * Validates that the current anchorGUID is correct and updates it if it is not.
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
    private String reEvaluateAnchorGUID(String  targetGUID,
                                        String  targetGUIDParameterName,
                                        String  targetTypeName,
                                        String  originalAnchorGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
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
            ((newAnchorIdentifiers != null) && (! newAnchorIdentifiers.equals(originalAnchorGUID))))
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
                                                        newAnchorIdentifiers.anchorGUID,
                                                        newAnchorIdentifiers.anchorTypeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
            }
        }

        return newAnchorIdentifiers.anchorGUID;
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
    private String reEvaluateAnchorGUID(String       targetGUID,
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
         * The anchorGUID has changed
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
                                                        newAnchorIdentifiers.anchorGUID,
                                                        newAnchorIdentifiers.anchorTypeName,
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
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an update.
     *
     * @param userId           userId of user making request.
     * @param connectToGUID       unique id for the object to connect the attachment to.
     * @param connectToGUIDParameterName  name of the parameter that passed the connectTo guid
     * @param connectToType       type of the connectToElement.
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones supported zone list from calling service
     * @param effectiveTime    the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     * @return anchor entity or null.  The anchor entity is used by the caller to set the LatestChange classification
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail validateAnchorEntity(String       userId,
                                             String       connectToGUID,
                                             String       connectToGUIDParameterName,
                                             String       connectToType,
                                             boolean      isExplicitGetRequest,
                                             boolean      isUpdate,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             List<String> suppliedSupportedZones,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectToGUID, connectToGUIDParameterName, methodName);

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  connectToEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          connectToGUID,
                                                                          connectToGUIDParameterName,
                                                                          connectToType,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        return this.validateAnchorEntity(userId,
                                         connectToGUID,
                                         connectToType,
                                         connectToEntity,
                                         connectToGUIDParameterName,
                                         isExplicitGetRequest,
                                         isUpdate,
                                         forLineage,
                                         forDuplicateProcessing,
                                         suppliedSupportedZones,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an
     * update.
     * <br><br>
     * The first part of this method is looking to see if the connectToEntity is an anchor entity. In which case it calls any specific validation
     * for that entity and returns the connectToEntity, assuming all is ok - exceptions are thrown if the entity is not valid or the user does not
     * have access to it.
     * <br><br>
     * If the connectToEntity is of a type that has a lifecycle that is linked to the lifecycle of another entity - typically a referenceable -
     * then that other entity is its anchor (examples are schema elements, comments, connections).  The anchor entity needs to be retrieved and
     * validated.
     * <br><br>
     * Some anchor entities have specific validation to perform.
     *
     * @param userId           userId of user making request.
     * @param connectToGUID    unique id for the object to connect the attachment to
     * @param connectToType    name of type of connectToEntity
     * @param connectToEntity  entity retrieved from the repository
     * @param connectToGUIDParameterName  name of the parameter that passed the connectTo guid
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones list of supported zones from the caller.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @return anchor entity or null if this entity is an anchor or does not have an anchor.  The anchor entity is used by the
     * caller to set the LatestChange classification
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail validateAnchorEntity(String        userId,
                                             String        connectToGUID,
                                             String        connectToType,
                                             EntityDetail  connectToEntity,
                                             String        connectToGUIDParameterName,
                                             boolean       isExplicitGetRequest,
                                             boolean       isUpdate,
                                             boolean       forLineage,
                                             boolean       forDuplicateProcessing,
                                             List<String>  suppliedSupportedZones,
                                             Date          effectiveTime,
                                             String        methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateObject(connectToEntity, connectToGUIDParameterName, methodName);

        /*
         * This first processing looks at the retrieved entity itself to ensure it is visible.
         */
        if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME))
        {
            /*
             * InformalTags have a property that says whether they are public or private
             */
            if (! repositoryHelper.getBooleanProperty(serviceName,
                                                      OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                      connectToEntity.getProperties(),
                                                      methodName))
            {
                /*
                 * This is a private tag - if the user did not create this we pretend it is not known.
                 */
                if (!userId.equals(connectToEntity.getCreatedBy()))
                {
                    invalidParameterHandler.throwUnknownElement(userId,
                                                                connectToGUID,
                                                                connectToType,
                                                                serviceName,
                                                                serverName,
                                                                methodName);
                }
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.CONNECTION_TYPE_NAME))
        {
            securityVerifier.validateUserForConnection(userId, connectToEntity, repositoryHelper, serviceName, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            /*
             * Even if the request is an update request, the security module is first called for read - the update
             * is validated once the properties have been updated.
             */
            securityVerifier.validateUserForAssetRead(userId,
                                                      connectToGUID,
                                                      connectToGUIDParameterName,
                                                      connectToEntity,
                                                      isExplicitGetRequest,
                                                      suppliedSupportedZones,
                                                      repositoryHelper,
                                                      serviceName,
                                                      methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME))
        {
            /*
             * Even if the request is an update request, the security module is first called for read - the update
             * is validated once the properties have been updated.
             */
            securityVerifier.validateUserForGlossaryRead(userId,
                                                         connectToEntity,
                                                         repositoryHelper,
                                                         serviceName,
                                                         methodName);
        }

        /*
         * Most referenceables have an independent lifecycle.  They are their own anchor.  This method is handling the special cases.
         */
        EntityDetail anchorEntity = null;

        /*
         * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
         * The exception occurs where the entity is not being managed by this handler, or something equivalent that maintains the Anchors
         * classification.
         */
        AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(connectToEntity, methodName);

        if (anchorIdentifiers == null)
        {
            /*
             * The classification is missing - so walk the relationships to find the anchor if it exists.
             */
            anchorIdentifiers = deriveAnchorGUID(connectToGUID, connectToEntity.getType().getTypeDefName(), forLineage, forDuplicateProcessing, effectiveTime, methodName);

            if (anchorIdentifiers != null)
            {
                /*
                 * The anchor has been found so store it in the classification, so it is easy to find next time.
                 */
                maintainAnchorGUIDInClassification(connectToGUID,
                                                   connectToGUIDParameterName,
                                                   connectToType,
                                                   connectToEntity,
                                                   anchorIdentifiers.anchorGUID,
                                                   anchorIdentifiers.anchorTypeName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        /*
         * If an anchor GUID has been found then validate it by retrieving the identified entity.  Note - anchorIdentifiers may be null if the connectToEntity
         * is actually an anchor.
         */
        if (anchorIdentifiers != null)
        {
            final String anchorGUIDParameterName = "anchorIdentifiers";

            if (! anchorIdentifiers.equals(connectToEntity.getGUID()))
            {
                anchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                 anchorIdentifiers.anchorGUID,
                                                                 anchorGUIDParameterName,
                                                                 OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
            }
        }

        /*
         * Perform any special processing on the anchor entity
         */
        if (anchorEntity != null)
        {
            InstanceType anchorEntityType = anchorEntity.getType();

            if (anchorEntityType != null)
            {
                boolean isFeedbackEntity = (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME)) ||
                                           (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.COMMENT_TYPE_NAME)) ||
                                           (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.RATING_TYPE_NAME)) ||
                                           (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.LIKE_TYPE_NAME));

                /*
                 * Determine if the element is attached directly or indirectly to an asset (or is an asset) so it is possible to determine
                 * if this asset is in a supported zone or if the user is allowed to change its attachments.
                 */
                if (OpenMetadataAPIMapper.ASSET_TYPE_NAME.equals(anchorEntityType.getTypeDefName()))
                {
                    securityVerifier.validateUserForAssetAttachment(userId,
                                                                    connectToGUID,
                                                                    connectToGUIDParameterName,
                                                                    anchorEntity,
                                                                    isFeedbackEntity,
                                                                    isUpdate,
                                                                    suppliedSupportedZones,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    methodName);
                }
                else if (OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME.equals(anchorEntityType.getTypeDefName()))
                {
                    if (isFeedbackEntity)
                    {
                        securityVerifier.validateUserForGlossaryFeedback(userId,
                                                                         anchorEntity,
                                                                         repositoryHelper,
                                                                         serviceName,
                                                                         methodName);
                    }
                    else if (isUpdate)
                    {
                        securityVerifier.validateUserForGlossaryMemberUpdate(userId,
                                                                             anchorEntity,
                                                                             repositoryHelper,
                                                                             serviceName,
                                                                             methodName);
                    }
                    else
                    {
                        securityVerifier.validateUserForGlossaryRead(userId,
                                                                     anchorEntity,
                                                                     repositoryHelper,
                                                                     serviceName,
                                                                     methodName);
                    }
                }

                /*
                 * This list is likely to expand as more anchor types get specialized visibility/security mechanisms.
                 */
            }
        }

        return anchorEntity;
    }


    /**
     * Validates that the unique property is not already in use across all types that contain the unique property.
     *
     * @param entityGUID existing entity (or null if this is a create)
     * @param entityTypeName the unique name of the type of the entity
     * @param uniqueParameterValue the value of the unique parameter
     * @param uniqueParameterName the name of the unique parameter
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method for exceptions and error messages
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void validateUniqueProperty(String entityGUID,
                                       String entityTypeName,
                                       String uniqueParameterValue,
                                       String uniqueParameterName,
                                       Date   effectiveTime,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        List<String> propertyNames = new ArrayList<>();
        propertyNames.add(uniqueParameterName);

        String owningEntityTypeGUID = null;
        String owningEntityTypeName = null;

        String entityTypeNameToCheck = entityTypeName;
        while (owningEntityTypeGUID == null)
        {
            TypeDef potentialOwningEntityTypeDef = repositoryHelper.getTypeDefByName(methodName, entityTypeNameToCheck);
            List<TypeDefAttribute> typeDefAttributes = potentialOwningEntityTypeDef.getPropertiesDefinition();
            if (typeDefAttributes != null && !typeDefAttributes.isEmpty())
            {
                for (TypeDefAttribute typeDefAttribute:typeDefAttributes)
                {
                    if (typeDefAttribute.isUnique() && typeDefAttribute.getAttributeName().equals(uniqueParameterName))
                    {
                        owningEntityTypeGUID = potentialOwningEntityTypeDef.getGUID();
                        owningEntityTypeName = potentialOwningEntityTypeDef.getName();
                    }
                }
            }

            TypeDefLink superTypeDefLink = potentialOwningEntityTypeDef.getSuperType();

            if (superTypeDefLink != null)
            {
                entityTypeNameToCheck = superTypeDefLink.getName();
            }
            else
            {
                // should not happen. Log?
                break;
            }
        }

        /*
         * An entity with the Memento classification set is ignored
         */
        List<EntityDetail> existingEntities = this.getEntitiesByValue(localServerUserId,
                                                                      uniqueParameterValue,
                                                                      uniqueParameterName,
                                                                      owningEntityTypeGUID,
                                                                      owningEntityTypeName,
                                                                      propertyNames,
                                                                      true,
                                                                      false,
                                                                      null,
                                                                      OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                                                      false,
                                                                      false,
                                                                      supportedZones,
                                                                      null,
                                                                      0,
                                                                      invalidParameterHandler.getMaxPagingSize(),
                                                                      effectiveTime,
                                                                      methodName);

        if ((existingEntities != null) && (! existingEntities.isEmpty()))
        {
            if (entityGUID != null)
            {
                for (EntityDetail existingEntity : existingEntities)
                {
                    if ((existingEntity != null) && (! entityGUID.equals(existingEntity.getGUID())))
                    {
                        invalidParameterHandler.throwUniqueNameInUse(uniqueParameterValue,
                                                                     uniqueParameterName,
                                                                     entityTypeName,
                                                                     serviceName,
                                                                     methodName);
                    }
                }
            }
            else
            {
                invalidParameterHandler.throwUniqueNameInUse(uniqueParameterValue,
                                                             uniqueParameterName,
                                                             entityTypeName,
                                                             serviceName,
                                                             methodName);
            }
        }
    }


    /**
     * Validate that new properties for an entity do not have unique properties that clash with other instances.
     *
     * @param entityGUID unique identifier of the entity to be updated (or null for a new entity).
     * @param entityTypeName the unique name of the type of the entity
     * @param newProperties properties to test
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method for exceptions and error messages
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void validateUniqueProperties(String             entityGUID,
                                          String             entityTypeName,
                                          InstanceProperties newProperties,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        InstanceProperties uniqueProperties = repositoryHelper.getUniqueProperties(serviceName,
                                                                                   entityTypeName,
                                                                                   newProperties);

        if ((uniqueProperties != null) && (uniqueProperties.getPropertyCount() > 0))
        {
            Iterator<String> uniquePropertyNames = uniqueProperties.getPropertyNames();

            if (uniquePropertyNames != null)
            {
                while (uniquePropertyNames.hasNext())
                {
                    String uniquePropertyName = uniquePropertyNames.next();

                    if (uniquePropertyName != null)
                    {
                        /*
                         * This code assumes that the unique property is a string - which is fine for all current open metadata types.
                         */
                        InstancePropertyValue uniquePropertyValue = uniqueProperties.getPropertyValue(uniquePropertyName);

                        validateUniqueProperty(entityGUID,
                                               entityTypeName,
                                               uniquePropertyValue.valueAsString(),
                                               uniquePropertyName,
                                               effectiveTime,
                                               methodName);
                    }
                }
            }
        }
    }


    /**
     * Validate that the user has permission to create a new entity
     *
     * @param userId           userId of user making request.
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void validateNewEntityRequest(String                         userId,
                                          String                         entityTypeGUID,
                                          String                         entityTypeName,
                                          InstanceProperties             newProperties,
                                          List<Classification>           classifications,
                                          InstanceStatus                 instanceStatus,
                                          Date                           effectiveTime,
                                          String                         methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        validateUniqueProperties(null, entityTypeName, newProperties, effectiveTime, methodName);

        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            securityVerifier.validateUserForAssetCreate(userId,
                                                        entityTypeGUID,
                                                        entityTypeName,
                                                        newProperties,
                                                        classifications,
                                                        instanceStatus,
                                                        defaultZones,
                                                        repositoryHelper,
                                                        serviceName,
                                                        methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME))
        {
            securityVerifier.validateUserForGlossaryCreate(userId,
                                                           entityTypeGUID,
                                                           entityTypeName,
                                                           newProperties,
                                                           classifications,
                                                           instanceStatus,
                                                           repositoryHelper,
                                                           serviceName,
                                                           methodName);
        }
    }


    /**
     * Create the instance properties object for the LatestChange classification.
     *
     * @param latestChangeTargetOrdinal The relationship of element that has been changed to the anchor
     * @param latestChangeActionOrdinal The type of change
     * @param classificationName If a classification name changed, this is its name
     * @param attachmentGUID If an attached entity or relationship changed, this is its unique identifier
     * @param attachmentTypeName If an attached entity or relationship changed, this is its unique type name
     * @param relationshipTypeName if a new relationship has been established, what is the type name of the relationship
     * @param userId The user identifier for the person/system making the change
     * @param actionDescription Description of the change
     * @param methodName calling method
     * @return instance properties object to null
     * @throws TypeErrorException there is a problem with one of the types used in this method - probably a logic error.
     */
    private InstanceProperties getLatestChangeClassificationProperties(int    latestChangeTargetOrdinal,
                                                                       int    latestChangeActionOrdinal,
                                                                       String classificationName,
                                                                       String attachmentGUID,
                                                                       String attachmentTypeName,
                                                                       String relationshipTypeName,
                                                                       String userId,
                                                                       String actionDescription,
                                                                       String methodName) throws TypeErrorException
    {
        InstanceProperties properties =  repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                    null,
                                                                                    OpenMetadataAPIMapper.CHANGE_TARGET_PROPERTY_NAME,
                                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_TARGET_ENUM_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_TARGET_ENUM_TYPE_NAME,
                                                                                    latestChangeTargetOrdinal,
                                                                                    methodName);

        properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataAPIMapper.CHANGE_ACTION_PROPERTY_NAME,
                                                                OpenMetadataAPIMapper.LATEST_CHANGE_ACTION_ENUM_TYPE_GUID,
                                                                OpenMetadataAPIMapper.LATEST_CHANGE_ACTION_ENUM_TYPE_NAME,
                                                                latestChangeActionOrdinal,
                                                                methodName);

        if (classificationName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.CLASSIFICATION_NAME_PROPERTY_NAME,
                                                                      classificationName,
                                                                      methodName);
        }

        if (attachmentGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                                      attachmentGUID,
                                                                      methodName);
        }

        if (attachmentTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ATTACHMENT_TYPE_PROPERTY_NAME,
                                                                      attachmentTypeName,
                                                                      methodName);
        }

        if (attachmentTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.RELATIONSHIP_TYPE_PROPERTY_NAME,
                                                                      relationshipTypeName,
                                                                      methodName);
        }

        if (userId != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.USER_PROPERTY_NAME,
                                                                      userId,
                                                                      methodName);
        }

        if (actionDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ACTION_DESCRIPTION_PROPERTY_NAME,
                                                                      actionDescription,
                                                                      methodName);
        }


        return properties;
    }


    /**
     * Validate the anchor guid and add its type and identifier to the builder.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the anchor
     * @param anchorGUIDParameterName parameter used to pass anchorGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param serviceSuppliedSupportedZones suported zones for this call
     * @param builder builder to receive the anchor (if appropriate).
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addAnchorGUIDToBuilder(String                        userId,
                                       String                        anchorGUID,
                                       String                        anchorGUIDParameterName,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       List<String>                  serviceSuppliedSupportedZones,
                                       OpenMetadataAPIGenericBuilder builder,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = this.getEntityFromRepository(userId,
                                                                     anchorGUID,
                                                                     anchorGUIDParameterName,
                                                                     OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     serviceSuppliedSupportedZones,
                                                                     effectiveTime,
                                                                     methodName);

            if (anchorEntity != null)
            {
                builder.setAnchors(userId, anchorEntity.getGUID(), anchorEntity.getType().getTypeDefName(), methodName);
            }
        }
    }


    /**
     * Classify an element with the Anchors classification.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName anchorGUID of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName unique name of the type of the anchor
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addAnchorsClassification(String  userId,
                                         String  beanGUID,
                                         String  beanGUIDParameterName,
                                         String  beanGUIDTypeName,
                                         String  anchorGUID,
                                         String  anchorTypeName,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                           builder.getAnchorsProperties(anchorGUID, anchorTypeName, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Add details of an update to the anchor entity in the latest change classification.
     *
     * @param anchorEntity the entity to update
     * @param latestChangeTargetOrdinal the type of instance
     * @param latestChangeActionOrdinal the type of change
     * @param classificationName if a classification has changed, what is its name
     * @param attachmentGUID if a new relationship has been established, what is the unique identifier of the entity it is connecting to
     * @param attachmentTypeName if a new relationship has been established, what is the type name of the entity it is connecting to
     * @param relationshipTypeName if a new relationship has been established, what is the type name of the relationship
     * @param userId who is the calling user?
     * @param actionDescription what is the description of the activity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws UserNotAuthorizedException local server user id not authorized to update LatestChange
     * @throws PropertyServerException logic error because classification type not recognized
     */
    private void addLatestChangeToAnchor(EntityDetail anchorEntity,
                                         int          latestChangeTargetOrdinal,
                                         int          latestChangeActionOrdinal,
                                         String       classificationName,
                                         String       attachmentGUID,
                                         String       attachmentTypeName,
                                         String       relationshipTypeName,
                                         String       userId,
                                         String       actionDescription,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String  guidParameterName = "anchorEntity.getGUID()";

        InstanceProperties newProperties = null;

        String anchorTypeName = anchorEntity.getType().getTypeDefName();

        /*
         * Only adding LatestChange classification to anchors that are Assets or Glossaries.
         */
        if ((repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME)) ||
            (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME)))
        {
            if (! OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME.equals(classificationName))
            {
                /*
                 * Do not log LatestChange for anchor classification updates
                 */
                try
                {
                    invalidParameterHandler.validateObject(anchorEntity, guidParameterName, methodName);

                    newProperties = this.getLatestChangeClassificationProperties(latestChangeTargetOrdinal,
                                                                                 latestChangeActionOrdinal,
                                                                                 classificationName,
                                                                                 attachmentGUID,
                                                                                 attachmentTypeName,
                                                                                 relationshipTypeName,
                                                                                 userId,
                                                                                 actionDescription,
                                                                                 methodName);

                    Classification classification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                 anchorEntity,
                                                                                                 OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                                                                                 methodName);
                    if (classification != null)
                    {
                        repositoryHandler.reclassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           anchorEntity.getGUID(),
                                                           guidParameterName,
                                                           anchorEntity.getType().getTypeDefName(),
                                                           OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                                           classification,
                                                           newProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
                    }
                }
                catch (ClassificationErrorException newClassificationNeeded)
                {
                    /*
                     * This is not an error - it just means that the classification is not present on the anchor entity.
                     */
                    try
                    {
                        repositoryHandler.classifyEntity(localServerUserId,
                                                         null,
                                                         null,
                                                         anchorEntity.getGUID(),
                                                         anchorEntity,
                                                         guidParameterName,
                                                         anchorEntity.getType().getTypeDefName(),
                                                         OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_GUID,
                                                         OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                                         ClassificationOrigin.ASSIGNED,
                                                         null,
                                                         newProperties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
                    }
                    catch (PropertyServerException e)
                    {
                        // Ignore exception, possibly a race condition - the entity is already classified
                    }
                }
                catch (InvalidParameterException | TypeErrorException error)
                {
                    throw new PropertyServerException(error);
                }
            }
        }


        /*
         * Check whether this anchor is nested in another anchor.
         */
        AnchorIdentifiers parentAnchorIdentifiers = getAnchorGUIDFromAnchorsClassification(anchorEntity, methodName);

        if (parentAnchorIdentifiers != null)
        {
            try
            {
                final String parentAnchorGUIDParameterName = "parentAnchorGUID";
                EntityDetail parentAnchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                    parentAnchorIdentifiers.anchorGUID,
                                                                                    parentAnchorGUIDParameterName,
                                                                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

                this.addLatestChangeToAnchor(parentAnchorEntity,
                                             latestChangeTargetOrdinal,
                                             latestChangeActionOrdinal,
                                             classificationName,
                                             attachmentGUID,
                                             attachmentTypeName,
                                             relationshipTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            catch (InvalidParameterException  error)
            {
                throw new PropertyServerException(error);
            }
        }
    }


    /**
     * Retrieve the supplementaryProperties glossary object.  This is the anchor of all the supplementaryProperties
     * glossary terms.
     *
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the supplementary properties glossary
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    private String getSupplementaryPropertiesGlossary(boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        String glossaryGUID = this.getEntityGUIDByValue(localServerUserId,
                                                        supplementaryPropertiesGlossaryName,
                                                        supplementaryPropertiesGlossaryParameterName,
                                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_GUID,
                                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                                        qualifiedNamePropertyNamesList,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

        if (glossaryGUID == null)
        {
            InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                         null,
                                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                         supplementaryPropertiesGlossaryName,
                                                                                         methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      supplementaryPropertiesGlossaryName,
                                                                      methodName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      supplementaryPropertiesGlossaryDescription,
                                                                      methodName);

            glossaryGUID = repositoryHandler.createEntity(localServerUserId,
                                                          OpenMetadataAPIMapper.GLOSSARY_TYPE_GUID,
                                                          OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                                          null,
                                                          null,
                                                          properties,
                                                          null,
                                                          InstanceStatus.ACTIVE,
                                                          methodName);
        }

        return glossaryGUID;
    }


    /**
     * Set up the instance properties for a supplementaryProperties glossary term.
     *
     * @param existingProperties properties to add the new properties to
     * @param qualifiedName qualified name of the linked element
     * @param displayName  display name for the term
     * @param summary short description
     * @param description description of the term
     * @param abbreviation abbreviation used for the term
     * @param usage illustrations of how the term is used
     * @param methodName calling method
     * @return properties object or null
     */
    private InstanceProperties getSupplementaryInstanceProperties(InstanceProperties existingProperties,
                                                                  String             qualifiedName,
                                                                  String             displayName,
                                                                  String             summary,
                                                                  String             description,
                                                                  String             abbreviation,
                                                                  String             usage,
                                                                  String             methodName)
    {
        InstanceProperties properties = existingProperties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                  qualifiedName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
                                                                  summary,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
                                                                  abbreviation,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                  usage,
                                                                  methodName);
        return properties;
    }


    /**
     * Maintain the supplementary properties of a technical metadata element in a glossary term linked to the supplied element.
     * The glossary term needs to be connected to a glossary which may need to be created.  There is no use of effective time
     * on these elements because they are effective all the time that the asset is effective
     *
     * @param userId calling user
     * @param elementGUID element for the
     * @param elementGUIDParameterName name of guid parameter
     * @param elementTypeName type of element
     * @param elementQualifiedName qualified name of the linked element
     * @param displayName  display name for the term
     * @param summary short description
     * @param description description of the term
     * @param abbreviation abbreviation used for the term
     * @param usage illustrations of how the term is used
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    public void maintainSupplementaryProperties(String  userId,
                                                String  elementGUID,
                                                String  elementGUIDParameterName,
                                                String  elementTypeName,
                                                String  elementQualifiedName,
                                                String  displayName,
                                                String  summary,
                                                String  description,
                                                String  abbreviation,
                                                String  usage,
                                                boolean isMergeUpdate,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        EntityDetail glossaryTerm = this.getAttachedEntity(localServerUserId,
                                                           elementGUID,
                                                           elementGUIDParameterName,
                                                           elementTypeName,
                                                           OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_GUID,
                                                           OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_NAME,
                                                           OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                           2,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (glossaryTerm == null)
        {
            if (displayName != null || summary != null || description != null || abbreviation != null || usage != null)
            {
                InstanceProperties glossaryTermProperties = this.getSupplementaryInstanceProperties(null,
                                                                                                    elementQualifiedName + supplementaryPropertiesQualifiedNamePostFix,
                                                                                                    displayName,
                                                                                                    summary,
                                                                                                    description,
                                                                                                    abbreviation,
                                                                                                    usage,
                                                                                                    methodName);

                /*
                 * Only create the glossary term if it is needed.
                 */
                if (glossaryTermProperties != null)
                {
                    String glossaryGUID = this.getSupplementaryPropertiesGlossary(forLineage, forDuplicateProcessing, effectiveTime, methodName);

                    if (glossaryGUID != null)
                    {
                        /*
                         * The glossary term is anchored to the element rather than the glossary.  This means that it deleted if/when
                         * the element is deleted.
                         */
                        List<Classification> initialClassifications = new ArrayList<>();
                        try
                        {
                            InstanceProperties classificationProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                                       null,
                                                                                                                       OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                                                                       elementGUID,
                                                                                                                       methodName);
                            classificationProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                    classificationProperties,
                                                                                                    OpenMetadataAPIMapper.ANCHOR_TYPE_NAME_PROPERTY_NAME,
                                                                                                    elementTypeName,
                                                                                                    methodName);
                            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                                  null,
                                                                                                  null,
                                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                                  userId,
                                                                                                  OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                                                                  OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                                  null,
                                                                                                  classificationProperties);
                            initialClassifications.add(classification);

                            classification = repositoryHelper.getNewClassification(serviceName,
                                                                                   null,
                                                                                   null,
                                                                                   InstanceProvenanceType.LOCAL_COHORT,
                                                                                   userId,
                                                                                   OpenMetadataAPIMapper.ELEMENT_SUPPLEMENT_CLASSIFICATION_TYPE_NAME,
                                                                                   OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                   ClassificationOrigin.ASSIGNED,
                                                                                   null,
                                                                                   null);
                            initialClassifications.add(classification);
                        }
                        catch (TypeErrorException error)
                        {
                            throw new PropertyServerException(error);
                        }

                        String glossaryTermGUID = repositoryHandler.createEntity(localServerUserId,
                                                                                 OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                                                 OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                 null,
                                                                                 null,
                                                                                 glossaryTermProperties,
                                                                                 initialClassifications,
                                                                                 InstanceStatus.ACTIVE,
                                                                                 methodName);

                        repositoryHandler.createRelationship(userId,
                                                             OpenMetadataAPIMapper.TERM_ANCHOR_TYPE_GUID,
                                                             null,
                                                             null,
                                                             glossaryGUID,
                                                             glossaryTermGUID,
                                                             null,
                                                             methodName);

                        repositoryHandler.createRelationship(userId,
                                                             OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_GUID,
                                                             null,
                                                             null,
                                                             elementGUID,
                                                             glossaryTermGUID,
                                                             null,
                                                             methodName);
                    }
                }
            }
        }
        else
        {
            InstanceProperties glossaryTermProperties;

            if (isMergeUpdate)
            {
                glossaryTermProperties = this.getSupplementaryInstanceProperties(glossaryTerm.getProperties(),
                                                                                 elementQualifiedName + supplementaryPropertiesQualifiedNamePostFix,
                                                                                 displayName,
                                                                                 summary,
                                                                                 description,
                                                                                 abbreviation,
                                                                                 usage,
                                                                                 methodName);
            }
            else
            {
                glossaryTermProperties = this.getSupplementaryInstanceProperties(null,
                                                                                 elementQualifiedName + supplementaryPropertiesQualifiedNamePostFix,
                                                                                 displayName,
                                                                                 summary,
                                                                                 description,
                                                                                 abbreviation,
                                                                                 usage,
                                                                                 methodName);
            }

            repositoryHandler.updateEntityProperties(userId,
                                                     null,
                                                     null,
                                                     glossaryTerm.getGUID(),
                                                     glossaryTerm,
                                                     OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                     OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                     glossaryTermProperties,
                                                     methodName);
        }
    }


    /**
     * Retrieve the supplementary properties of a technical metadata element in a glossary term linked to the supplied element.
     * The glossary term needs to be connected to a glossary which may need to be created.  There is no use of effective time
     * on these elements because they are effective all the time that the asset is effective
     *
     * @param elementGUID unique identifier of the linked element
     * @param elementGUIDParameterName name of guid parameter
     * @param elementTypeName type of element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return entity containing the supplementary properties
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    public EntityDetail getSupplementaryProperties(String  elementGUID,
                                                   String  elementGUIDParameterName,
                                                   String  elementTypeName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return this.getAttachedEntity(localServerUserId,
                                      elementGUID,
                                      elementGUIDParameterName,
                                      elementTypeName,
                                      OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_GUID,
                                      OpenMetadataAPIMapper.SUPPLEMENTARY_PROPERTIES_TYPE_NAME,
                                      OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Count up the number of elements of a certain type that are attached to a specific entity.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the entity that the object is attached to (anchor entity)
     * @param elementTypeName type of the anchor entity
     * @param attachmentTypeGUID unique identifier of the attachment relationship's type
     * @param attachmentTypeName unique name of the attachment's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return count of attached objects that are effective now
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public int countAttachments(String  userId,
                                String  elementGUID,
                                String  elementTypeName,
                                String  attachmentTypeGUID,
                                String  attachmentTypeName,
                                int     attachmentEntityEnd,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String guidParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        int count = 0;

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       elementGUID,
                                                                                       elementTypeName,
                                                                                       attachmentTypeGUID,
                                                                                       attachmentTypeName,
                                                                                       attachmentEntityEnd,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (visibleToUserThroughRelationship(userId, relationship, methodName))
            {
                count ++;
            }
        }

        return count;
    }


    /**
     * Return the entity for the required relationship attached to a specific entity.  This method assumes the starting entity has
     * a validated anchor
     *
     * @param userId     calling user
     * @param startingElementGUID identifier for the entity that the identifier is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public EntityDetail getAttachedEntity(String  userId,
                                          String  startingElementGUID,
                                          String  startingElementGUIDParameterName,
                                          String  startingElementTypeName,
                                          String  relationshipTypeGUID,
                                          String  relationshipTypeName,
                                          String  resultingElementTypeName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return this.getAttachedEntity(userId,
                                      startingElementGUID,
                                      startingElementGUIDParameterName,
                                      startingElementTypeName,
                                      relationshipTypeGUID,
                                      relationshipTypeName,
                                      resultingElementTypeName,
                                      0,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the entity for the required relationship attached to a specific entity.  This method assumes the starting entity has
     * a validated anchor
     *
     * @param userId     calling user
     * @param startingElementGUID identifier for the entity that the identifier is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public EntityDetail getAttachedEntity(String       userId,
                                          String       startingElementGUID,
                                          String       startingElementGUIDParameterName,
                                          String       startingElementTypeName,
                                          String       relationshipTypeGUID,
                                          String       relationshipTypeName,
                                          String       resultingElementTypeName,
                                          int          attachmentEntityEnd,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          List<String> serviceSupportedZones,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingElementGUID,
                                                                   startingElementGUIDParameterName,
                                                                   startingElementTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   effectiveTime,
                                                                   methodName);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                             startingEntity,
                                                                             startingElementTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             null,
                                                                             0,
                                                                             resultingElementTypeName,
                                                                             attachmentEntityEnd,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);

        if (entity != null)
        {
            validateAnchorEntity(userId,
                                 entity.getGUID(),
                                 resultingElementTypeName,
                                 entity,
                                 startingElementGUIDParameterName,
                                 true,
                                 false,
                                 forLineage,
                                 forDuplicateProcessing,
                                 serviceSupportedZones,
                                 effectiveTime,
                                 methodName);
        }

        return entity;
    }



    /**
     * Return the list of entities at the other end of the requested relationship type that were created or edited by
     * the requesting user.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachedEntityTypeGUID  identifier for the relationship to follow
     * @param attachedEntityTypeName  type name for the relationship to follow
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws InvalidParameterException the entity at the other end is not of the expected type
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public EntityDetail getAttachedEntityFromUser(String  userId,
                                                  String  startingEntityGUID,
                                                  String  startingEntityTypeName,
                                                  String  relationshipTypeGUID,
                                                  String  relationshipTypeName,
                                                  String  attachedEntityTypeGUID,
                                                  String  attachedEntityTypeName,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String localMethodName = "getAttachedEntityFromUser";

        RepositoryRelatedEntitiesIterator iterator = new RepositoryRelatedEntitiesIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           startingEntityGUID,
                                                                                           startingEntityTypeName,
                                                                                           relationshipTypeGUID,
                                                                                           relationshipTypeName,
                                                                                           null,
                                                                                           false,
                                                                                           false,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);

        while (iterator.moreToReceive())
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                if ((userId.equals(entity.getCreatedBy()) || (userId.equals(entity.getUpdatedBy())) || ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                {
                    errorHandler.validateInstanceType(entity, attachedEntityTypeName, methodName, localMethodName);
                    return entity;
                }
            }
        }

        return null;
    }


    /**
     * Return the list of entities at the other end of the requested relationship type that were created or
     * edited by the requesting user.
     *
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param attachedEntityTypeGUID  identifier for the relationship to follow
     * @param attachedEntityTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - needed for paging
     * @param startingFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @return retrieved entities or null
     * @throws PropertyServerException problem accessing the property server
     * @throws UserNotAuthorizedException security access problem
     * @throws InvalidParameterException one of the parameters is in error
     */
    @SuppressWarnings(value = "unused")
    public List<EntityDetail> getAttachedEntitiesFromUser(String  userId,
                                                          String  startingEntityGUID,
                                                          String  startingEntityTypeName,
                                                          String  relationshipTypeGUID,
                                                          String  relationshipTypeName,
                                                          String  attachedEntityTypeGUID,
                                                          String  attachedEntityTypeName,
                                                          String  sequencingPropertyName,
                                                          int     startingFrom,
                                                          int     pageSize,
                                                          Date    effectiveTime,
                                                          String  methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String localMethodName = "getAttachedEntitiesFromUser";

        List<EntityDetail> results = new ArrayList<>();

        RepositoryRelatedEntitiesIterator iterator = new RepositoryRelatedEntitiesIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           startingEntityGUID,
                                                                                           startingEntityTypeName,
                                                                                           relationshipTypeGUID,
                                                                                           relationshipTypeName,
                                                                                           sequencingPropertyName,
                                                                                           false,
                                                                                           false,
                                                                                           startingFrom,
                                                                                           pageSize,
                                                                                           effectiveTime,
                                                                                           methodName);

        while ((iterator.moreToReceive() && ((pageSize == 0) || (results.size() < pageSize))))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                if ((userId.equals(entity.getCreatedBy()) ||
                             (userId.equals(entity.getUpdatedBy())) ||
                             ((entity.getMaintainedBy() != null) && (entity.getMaintainedBy().contains(userId)))))
                {
                    errorHandler.validateInstanceType(entity, attachedEntityTypeName, methodName, localMethodName);
                    results.add(entity);
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
     * Return the entities for the required relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param requiredClassificationName name of a classification that must be on the entity for a match
     * @param omittedClassificationName name of a classification that must NOT be on the entity for a match
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedEntities(String       userId,
                                                  String       startingGUID,
                                                  String       startingGUIDParameterName,
                                                  String       startingTypeName,
                                                  String       relationshipTypeGUID,
                                                  String       relationshipTypeName,
                                                  String       resultingElementTypeName,
                                                  String       requiredClassificationName,
                                                  String       omittedClassificationName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        return this.getAttachedEntities(userId,
                                        startingEntity,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        relationshipTypeGUID,
                                        relationshipTypeName,
                                        resultingElementTypeName,
                                        requiredClassificationName,
                                        omittedClassificationName,
                                        attachmentEntityEnd,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the entities for the required relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingElement  the entity that the required element is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param requiredClassificationName name of a classification that must be on the entity for a match
     * @param omittedClassificationName name of a classification that must NOT be on the entity for a match
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedEntities(String       userId,
                                                  EntityDetail startingElement,
                                                  String       startingElementGUIDParameterName,
                                                  String       startingElementTypeName,
                                                  String       relationshipTypeGUID,
                                                  String       relationshipTypeName,
                                                  String       resultingElementTypeName,
                                                  String       requiredClassificationName,
                                                  String       omittedClassificationName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(startingElement, startingElementGUIDParameterName, methodName);

        List<Relationship> visibleRelationships = this.getAttachmentLinks(userId,
                                                                          startingElement,
                                                                          startingElementGUIDParameterName,
                                                                          startingElementTypeName,
                                                                          relationshipTypeGUID,
                                                                          relationshipTypeName,
                                                                          null,
                                                                          resultingElementTypeName,
                                                                          attachmentEntityEnd,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          serviceSupportedZones,
                                                                          startingFrom,
                                                                          pageSize,
                                                                          effectiveTime,
                                                                          methodName);

        if (visibleRelationships != null)
        {
            List<EntityDetail> visibleEntities = new ArrayList<>();

            for (Relationship  relationship : visibleRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingElement.getGUID(), startingElementTypeName, relationship, attachmentEntityEnd, methodName);

                    if ((entityProxy != null) && (entityProxy.getType() != null)  &&
                                (repositoryHelper.isTypeOf(serviceName, entityProxy.getType().getTypeDefName(), resultingElementTypeName)))
                    {
                        try
                        {
                            final String guidParameterName = "entityProxy.getGUID()";

                            visibleEntities.add(this.getEntityFromRepository(userId,
                                                                             entityProxy.getGUID(),
                                                                             guidParameterName,
                                                                             resultingElementTypeName,
                                                                             requiredClassificationName,
                                                                             omittedClassificationName,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             supportedZones,
                                                                             effectiveTime,
                                                                             methodName));
                        }
                        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                        {
                            // skip entities that are not visible to this user
                            if (log.isDebugEnabled())
                            {
                                log.debug("Skipping inaccessible entity: " + inaccessibleEntity);
                            }
                        }
                    }
                }
            }

            if (! visibleEntities.isEmpty())
            {
                return visibleEntities;
            }
        }

        return null;
    }


    /**
     * Return the entities for the required relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingElementGUID identifier for the entity that the identifier is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param requiredClassificationName name of a classification that must be on the entity for a match
     * @param omittedClassificationName name of a classification that must NOT be on the entity for a match
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedEntities(String       userId,
                                                  String       startingElementGUID,
                                                  String       startingElementGUIDParameterName,
                                                  String       startingElementTypeName,
                                                  String       relationshipTypeGUID,
                                                  String       relationshipTypeName,
                                                  String       resultingElementTypeName,
                                                  String       requiredClassificationName,
                                                  String       omittedClassificationName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return getAttachedEntities(userId,
                                   startingElementGUID,
                                   startingElementGUIDParameterName,
                                   startingElementTypeName,
                                   relationshipTypeGUID,
                                   relationshipTypeName,
                                   resultingElementTypeName,
                                   requiredClassificationName,
                                   omittedClassificationName,
                                   attachmentEntityEnd,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startingFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return a visible relationship retrieved by its GUID.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier
     * @param relationshipGUIDParameterName parameter passing the unique identifier
     * @param relationshipTypeName type of relationship to be retrieved
     * @param effectiveTime effective time for the retrieval
     * @param methodName calling method
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public Relationship getAttachmentLink(String userId,
                                          String relationshipGUID,
                                          String relationshipGUIDParameterName,
                                          String relationshipTypeName,
                                          Date   effectiveTime,
                                          String methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException

    {
        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
        {
            return relationship;
        }

        return null;
    }


    /**
     * Return the relationship between the requested elements - there should be only one.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public Relationship  getUniqueAttachmentLink(String   userId,
                                                 String   startingGUID,
                                                 String   startingGUIDParameterName,
                                                 String   startingTypeName,
                                                 String   attachmentRelationshipTypeGUID,
                                                 String   attachmentRelationshipTypeName,
                                                 String   attachmentEntityGUID,
                                                 String   attachmentEntityTypeName,
                                                 int      attachmentEntityEnd,
                                                 boolean  forLineage,
                                                 boolean  forDuplicateProcessing,
                                                 Date     effectiveTime,
                                                 String   methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return getUniqueAttachmentLink(userId, startingGUID, startingGUIDParameterName, startingTypeName, attachmentRelationshipTypeGUID, attachmentRelationshipTypeName, attachmentEntityGUID, attachmentEntityTypeName, attachmentEntityEnd, forLineage, forDuplicateProcessing, supportedZones, effectiveTime, methodName);
    }


    /**
     * Return the relationship between the requested elements - there should be only one.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public Relationship  getUniqueAttachmentLink(String       userId,
                                                 String       startingGUID,
                                                 String       startingGUIDParameterName,
                                                 String       startingTypeName,
                                                 String       attachmentRelationshipTypeGUID,
                                                 String       attachmentRelationshipTypeName,
                                                 String       attachmentEntityGUID,
                                                 String       attachmentEntityTypeName,
                                                 int          attachmentEntityEnd,
                                                 boolean      forLineage,
                                                 boolean      forDuplicateProcessing,
                                                 List<String> serviceSupportedZones,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   attachmentRelationshipTypeGUID,
                                                                   attachmentRelationshipTypeName,
                                                                   attachmentEntityGUID,
                                                                   attachmentEntityTypeName,
                                                                   attachmentEntityEnd,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   effectiveTime,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }
        else if (relationships.size() == 1)
        {
            return relationships.get(0);
        }
        else
        {
            errorHandler.handleAmbiguousRelationships(startingGUID,
                                                      startingTypeName,
                                                      attachmentRelationshipTypeName,
                                                      relationships,
                                                      methodName);

            return null;
        }
    }


    /**
     * Return all relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAllAttachmentLinks(String   userId,
                                                     String   startingGUID,
                                                     String   startingGUIDParameterName,
                                                     String   startingTypeName,
                                                     boolean  forLineage,
                                                     boolean  forDuplicateProcessing,
                                                     Date     effectiveTime,
                                                     String   methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return this.getAttachmentLinks(userId,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       startingTypeName,
                                       null,
                                       null,
                                       null,
                                       null,
                                       0,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       0,
                                       invalidParameterHandler.getMaxPagingSize(),
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Return the relationships to required elements attached to a specific entity.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String       userId,
                                                  String       startingGUID,
                                                  String       startingGUIDParameterName,
                                                  String       startingTypeName,
                                                  String       attachmentRelationshipTypeGUID,
                                                  String       attachmentRelationshipTypeName,
                                                  String       attachmentEntityGUID,
                                                  String       attachmentEntityTypeName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return getAttachmentLinks(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  attachmentRelationshipTypeGUID,
                                  attachmentRelationshipTypeName,
                                  attachmentEntityGUID,
                                  attachmentEntityTypeName,
                                  attachmentEntityEnd,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  startingFrom,
                                  pageSize,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the relationships to required elements attached to a specific entity.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String       userId,
                                                  String       startingGUID,
                                                  String       startingGUIDParameterName,
                                                  String       startingTypeName,
                                                  String       attachmentRelationshipTypeGUID,
                                                  String       attachmentRelationshipTypeName,
                                                  String       attachmentEntityGUID,
                                                  String       attachmentEntityTypeName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        return getAttachmentLinks(userId,
                                  startingEntity,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  attachmentRelationshipTypeGUID,
                                  attachmentRelationshipTypeName,
                                  attachmentEntityGUID,
                                  attachmentEntityTypeName,
                                  attachmentEntityEnd,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  startingFrom,
                                  pageSize,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the relationships to required elements attached to a specific entity.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingEntity  the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String       userId,
                                                  EntityDetail startingEntity,
                                                  String       startingGUIDParameterName,
                                                  String       startingTypeName,
                                                  String       attachmentRelationshipTypeGUID,
                                                  String       attachmentRelationshipTypeName,
                                                  String       attachmentEntityGUID,
                                                  String       attachmentEntityTypeName,
                                                  int          attachmentEntityEnd,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(startingEntity, startingGUIDParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        this.validateAnchorEntity(userId,
                                  startingEntity.getGUID(),
                                  startingTypeName,
                                  startingEntity,
                                  startingGUIDParameterName,
                                  true,
                                  false,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  effectiveTime,
                                  methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntity,
                                                                                       startingTypeName,
                                                                                       attachmentRelationshipTypeGUID,
                                                                                       attachmentRelationshipTypeName,
                                                                                       attachmentEntityEnd,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       startingFrom,
                                                                                       queryPageSize,
                                                                                       effectiveTime,
                                                                                       methodName);


        List<Relationship> visibleRelationships = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (visibleRelationships.size() < queryPageSize)))
        {
            Relationship relationship = iterator.getNext();

            if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
            {
                EntityProxy otherEnd = repositoryHandler.getOtherEnd(startingEntity.getGUID(), startingTypeName, relationship, attachmentEntityEnd, methodName);

                /*
                 * Does the relationship point to an appropriate type of entity?
                 */
                if (repositoryHelper.isTypeOf(serviceName, otherEnd.getType().getTypeDefName(), attachmentEntityTypeName))
                {
                    if ((attachmentEntityGUID == null) || (attachmentEntityGUID.equals(otherEnd.getGUID())))
                    {
                        log.debug("Accepting relationship: " + relationship.getGUID());
                        visibleRelationships.add(relationship);
                    }
                    else
                    {
                        log.debug("Ignoring relationship based on entity instance: " + relationship.getGUID());
                    }
                }
                else
                {
                    log.debug("Ignoring relationship based on type of attachment: " + relationship.getGUID());
                }
            }
        }

        if (! visibleRelationships.isEmpty())
        {
            return visibleRelationships;
        }

        return null;
    }


    /**
     * Return a list of relationships that match the supplied criteria.  The results can be returned over many pages.
     * Note: the visibility of the relationship and both entities is checked before returning a relationship to the caller.
     *
     * @param userId caller's userId
     * @param relationshipTypeName type of interest (null means any element type)
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<Relationship> findAttachmentLinks(String                userId,
                                                  String                relationshipTypeName,
                                                  SearchProperties      searchProperties,
                                                  List<InstanceStatus>  limitResultsByStatus,
                                                  Date                  asOfTime,
                                                  String                sequencingProperty,
                                                  SequencingOrder       sequencingOrder,
                                                  boolean               forLineage,
                                                  boolean               forDuplicateProcessing,
                                                  int                   startingFrom,
                                                  int                   pageSize,
                                                  List<String>          serviceSupportedZones,
                                                  Date                  effectiveTime,
                                                  String                methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            null,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }


        RepositoryFindRelationshipsIterator iterator = new RepositoryFindRelationshipsIterator(repositoryHandler,
                                                                                               invalidParameterHandler,
                                                                                               userId,
                                                                                               relationshipTypeGUID,
                                                                                               null,
                                                                                               searchProperties,
                                                                                               limitResultsByStatus,
                                                                                               asOfTime,
                                                                                               sequencingProperty,
                                                                                               sequencingOrder,
                                                                                               startingFrom,
                                                                                               queryPageSize,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               effectiveTime,
                                                                                               methodName);

        List<Relationship> results = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            Relationship relationship = iterator.getNext();

            if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
            {
                try
                {
                    final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
                    final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

                    this.validateAnchorEntity(userId,
                                              relationship.getEntityOneProxy().getGUID(),
                                              entityOneParameterName,
                                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                              false,
                                              false,
                                              forLineage,
                                              forDuplicateProcessing,
                                              serviceSupportedZones,
                                              effectiveTime,
                                              methodName);

                    this.validateAnchorEntity(userId,
                                              relationship.getEntityTwoProxy().getGUID(),
                                              entityTwoParameterName,
                                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                              false,
                                              false,
                                              forLineage,
                                              forDuplicateProcessing,
                                              serviceSupportedZones,
                                              effectiveTime,
                                              methodName);

                    results.add(relationship);
                }
                catch (Exception error)
                {
                    // ignore an element that is not visible to the caller
                }
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
    }


    /**
     * Create a new entity in the repository assuming all parameters are ok.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param propertyBuilder builder pre-populated with the properties and classifications of the new entity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return unique identifier of new entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createBeanInRepository(String                        userId,
                                         String                        externalSourceGUID,
                                         String                        externalSourceName,
                                         String                        entityTypeGUID,
                                         String                        entityTypeName,
                                         OpenMetadataAPIGenericBuilder propertyBuilder,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        validateNewEntityRequest(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 propertyBuilder.getInstanceProperties(methodName),
                                 propertyBuilder.getEntityClassifications(),
                                 propertyBuilder.getInstanceStatus(),
                                 effectiveTime,
                                 methodName);

        String entityGUID = repositoryHandler.createEntity(userId,
                                                           entityTypeGUID,
                                                           entityTypeName,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           propertyBuilder.getInstanceProperties(methodName),
                                                           propertyBuilder.getEntityClassifications(),
                                                           propertyBuilder.getInstanceStatus(),
                                                           methodName);


        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            auditLog.logMessage(assetActionDescription,
                                GenericHandlersAuditCode.ASSET_ACTIVITY_CREATE.getMessageDefinition(userId,
                                                                                                    entityTypeName,
                                                                                                    entityGUID,
                                                                                                    methodName,
                                                                                                    serviceName));
        }

        return entityGUID;
    }



    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is preloaded with
     * properties that should override the properties from the template.  This is the method to call from the specific handlers.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of existing entity to use
     * @param templateGUIDParameterName name of parameter passing the templateGUID
     * @param entityTypeGUID unique identifier of the type for the entity
     * @param entityTypeName unique name of the type for the entity
     * @param uniqueParameterValue the value of a unique property (eg qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
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
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        return createBeanFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateGUIDParameterName, entityTypeGUID, entityTypeName, uniqueParameterValue, uniqueParameterName, propertyBuilder, supportedZones, true, false, methodName);
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is preloaded with
     * properties that should override the properties from the template.  This is the method to call from the specific handlers.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of existing entity to use
     * @param templateGUIDParameterName name of parameter passing the templateGUID
     * @param entityTypeGUID unique identifier of the type for the entity
     * @param entityTypeName unique name of the type for the entity
     * @param uniqueParameterValue the value of a unique property (eg qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
     * @param serviceSupportedZones list of supported zones for this service
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
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        return createBeanFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateGUIDParameterName, entityTypeGUID, entityTypeName, uniqueParameterValue, uniqueParameterName, propertyBuilder, serviceSupportedZones, true, false, methodName);
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is preloaded with
     * properties that should override the properties from the template.  This is the method to call from the specific handlers.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of existing entity to use
     * @param templateGUIDParameterName name of parameter passing the templateGUID
     * @param entityTypeGUID unique identifier of the type for the entity
     * @param entityTypeName unique name of the type for the entity
     * @param uniqueParameterValue the value of a unique property (eg qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
     * @param serviceSupportedZones list of supported zones for this service
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
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
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        TemplateProgress templateProgress = createBeanFromTemplate(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   true,
                                                                   new TemplateProgress(),
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
                                                                   methodName);

        if (templateProgress != null)
        {
            /*
             * This relationship shows where the property values for the new bean came from.  It enables traceability.  Also, if the template is
             * updated, there is a possibility of making complementary changes to the entities that were derived from it.
             */
            InstanceProperties relationshipProperties = null;

            if (templateProgress.sourceVersionNumber != 0)
            {
                relationshipProperties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                                    null,
                                                                                    OpenMetadataAPIMapper.SOURCE_VERSION_NUMBER_PROPERTY_NAME,
                                                                                    templateProgress.sourceVersionNumber,
                                                                                    methodName);
            }

            repositoryHandler.createRelationship(localServerUserId,
                                                 OpenMetadataAPIMapper.SOURCED_FROM_RELATIONSHIP_TYPE_GUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 templateProgress.newBeanGUID,
                                                 templateGUID,
                                                 relationshipProperties,
                                                 methodName);

            if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
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

        return null;
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
     * @param uniqueParameterValue the value of a unique property (eg qualifiedName) in the new entity - this is used to create unique names in the
     *                             attachments.
     * @param uniqueParameterName name of the property where the unique value is stored.
     * @param propertyBuilder this property builder has the new properties supplied by the caller.  They will be augmented by the template
     *                        properties and classification.
     * @param serviceSupportedZones list of supported zones for this service
     * @param deepCopy should the template creation extend to the anchored element or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
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
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String newEntityParameterName = "newEntityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        boolean forLineage = true;
        boolean forDuplicateProcessing = false;
        Date    effectiveTime = new Date();

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

        if (templateEntity != null)
        {
            Classification classification = this.getExistingClassification(templateEntity,
                                                                           OpenMetadataAPIMapper.TEMPLATE_SUBSTITUTE_CLASSIFICATION_TYPE_NAME);

            if (classification != null)
            {
                /*
                 * The template element is a substitute for the real entity to connect.
                 */
                templateEntity = getAttachedEntity(userId,
                                                   templateEntity.getGUID(),
                                                   templateGUIDParameterName,
                                                   entityTypeName,
                                                   OpenMetadataAPIMapper.SOURCED_FROM_RELATIONSHIP_TYPE_GUID,
                                                   OpenMetadataAPIMapper.SOURCED_FROM_RELATIONSHIP_TYPE_NAME,
                                                   entityTypeName,
                                                   1,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   serviceSupportedZones,
                                                   effectiveTime,
                                                   methodName);
            }
        }

        if (templateEntity != null)
        {
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

            if ((templateProgress.beanAnchorGUID != null) && (! propertyBuilder.isClassificationSet(OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME)))
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
            propertyBuilder.setTemplateProperties(templateEntity.getProperties());
            propertyBuilder.setTemplateClassifications(userId, externalSourceGUID, externalSourceName, templateEntity.getClassifications(), methodName);

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
                        if ((! OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME.equals(templateClassification.getName())) &&
                            (! OpenMetadataAPIMapper.TEMPLATE_CLASSIFICATION_TYPE_NAME.equals(templateClassification.getName())) &&
                            (! OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME.equals(templateClassification.getName())))
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
                                                                   newEntityParameterName,
                                                                   templateGUID,
                                                                   entityTypeName,
                                                                   uniqueParameterValue,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   effectiveTime,
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
     * @param startingGUIDParameterName parameter providing the startingGUID value
     * @param templateGUID unique identifier of the template element
     * @param expectedTypeName type name of the new bean
     * @param qualifiedName unique name for this new bean - must not be null
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException the guids or something related are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the repositories
     */
    private TemplateProgress addAttachmentsFromTemplate(String           userId,
                                                        String           externalSourceGUID,
                                                        String           externalSourceName,
                                                        TemplateProgress templateProgress,
                                                        String           startingGUID,
                                                        String           startingGUIDParameterName,
                                                        String           templateGUID,
                                                        String           expectedTypeName,
                                                        String           qualifiedName,
                                                        boolean          forLineage,
                                                        boolean          forDuplicateProcessing,
                                                        List<String>     serviceSupportedZones,
                                                        Date             effectiveTime,
                                                        String           methodName) throws InvalidParameterException,
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
            Relationship relationship  = iterator.getNext();
            EntityProxy  entityProxy   = relationship.getEntityOneProxy();
            Date         effectiveFrom = null;
            Date         effectiveTo   = null;

            if (relationship.getProperties() != null)
            {
                effectiveFrom = relationship.getProperties().getEffectiveFromTime();
                effectiveTo   = relationship.getProperties().getEffectiveToTime();
            }

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
             * Skip "SourcedFrom" relationships since they are not part of the template.
             */
            if (! repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.SOURCED_FROM_RELATIONSHIP_TYPE_NAME))
            {
                /*
                 * Is this a new relationship?
                 */
                if ((entityProxy != null) && (entityProxy.getType() != null) && (! entityProxy.getGUID().equals(previousTemplateGUID)))
                {
                    EntityDetail nextTemplateEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                        entityProxy.getGUID(),
                                                                                        nextTemplateEntityGUIDParameterName,
                                                                                        OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
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
                            if (repositoryHelper.isTypeOf(serviceName, nextTemplateEntityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
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

                            builder.setTemplateProperties(nextTemplateEntity.getProperties());
                            builder.setTemplateClassifications(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               nextTemplateEntity.getClassifications(),
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
                                                                           methodName);

                            nextBeanEntityGUID = templateProgress.newBeanGUID;
                        }

                        /*
                         * Link the previously created bean to the next bean - making sure end one and end two are correctly set up.
                         */
                        if (relationshipOneToTwo)
                        {
                            this.linkElementToElement(userId,
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
                                                      relationship.getProperties(),
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      effectiveTime,
                                                      methodName);
                        }
                        else
                        {
                            this.linkElementToElement(userId,
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
                                                      relationship.getProperties(),
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      effectiveTime,
                                                      methodName);
                        }
                    }
                }
            }
        }

        return templateProgress;
    }



    /**
     * Update one or more properties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param propertyName name of bean property to update
     * @param propertyValue new value for bean property
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the properties tin the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanPropertyInRepository(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  entityGUID,
                                               String  entityGUIDParameterName,
                                               String  entityTypeGUID,
                                               String  entityTypeName,
                                               String  propertyName,
                                               String  propertyValue,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String propertyParameterName = "propertyValue";
        final String propertyNameParameterName = "propertyName";

        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, propertyName, propertyValue, methodName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    properties,
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Update one or more properties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param properties object containing the properties for the repository instances based on the properties of the bean
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanInRepository(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             entityGUID,
                                       String             entityGUIDParameterName,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       InstanceProperties properties,
                                       boolean            isMergeUpdate,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    false,
                                    false,
                                    supportedZones,
                                    properties,
                                    isMergeUpdate,
                                    new Date(),
                                    methodName);
    }



    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanInRepository(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             entityGUID,
                                       String             entityGUIDParameterName,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       InstanceProperties updateProperties,
                                       boolean            isMergeUpdate,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    updateProperties,
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }



    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanInRepository(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             entityGUID,
                                       String             entityGUIDParameterName,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       List<String>       serviceSupportedZones,
                                       InstanceProperties updateProperties,
                                       boolean            isMergeUpdate,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        updateBeanInRepository(userId,
                               externalSourceGUID,
                               externalSourceName,
                               startingEntity,
                               entityGUIDParameterName,
                               entityTypeGUID,
                               entityTypeName,
                               forLineage,
                               forDuplicateProcessing,
                               serviceSupportedZones,
                               updateProperties,
                               isMergeUpdate,
                               effectiveTime,
                               methodName);
    }


    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param originalEntity unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanInRepository(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       EntityDetail       originalEntity,
                                       String             entityGUIDParameterName,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       List<String>       serviceSupportedZones,
                                       InstanceProperties updateProperties,
                                       boolean            isMergeUpdate,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                                  originalEntity.getGUID(),
                                                                  entityTypeName,
                                                                  originalEntity,
                                                                  entityGUIDParameterName,
                                                                  true,
                                                                  true,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  serviceSupportedZones,
                                                                  effectiveTime,
                                                                  methodName);

            /*
             * Sort out the properties
             */
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  updateProperties,
                                                                  originalEntity.getProperties());

            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (originalEntity.getProperties() == null))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_ENTITY_UPDATE.getMessageDefinition(originalEntity.getType().getTypeDefName(),
                                                                                                                     originalEntity.getGUID(),
                                                                                                                     methodName,
                                                                                                                     userId));
                return;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(originalEntity.getProperties())))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_ENTITY_UPDATE.getMessageDefinition(originalEntity.getType().getTypeDefName(),
                                                                                                                     originalEntity.getGUID(),
                                                                                                                     methodName,
                                                                                                                     userId));

                return;
            }

            /*
             * Validate that any changes to the unique properties do not clash with other entities.
             */
            validateUniqueProperties(originalEntity.getGUID(),
                                     entityTypeName,
                                     newProperties,
                                     effectiveTime,
                                     methodName);

            /*
             * There is an extra security check if the update is for an asset.
             */
            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
            {
                securityVerifier.validateUserForAssetUpdate(userId,
                                                            originalEntity,
                                                            newProperties,
                                                            originalEntity.getStatus(),
                                                            repositoryHelper,
                                                            serviceName,
                                                            methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME))
            {
                securityVerifier.validateUserForGlossaryDetailUpdate(userId,
                                                                     originalEntity,
                                                                     newProperties,
                                                                     repositoryHelper,
                                                                     serviceName,
                                                                     methodName);
            }

            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     originalEntity.getGUID(),
                                                     originalEntity,
                                                     entityTypeGUID,
                                                     entityTypeName,
                                                     newProperties,
                                                     methodName);

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Updating properties in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, originalEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             originalEntity.getGUID(),
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             OpenMetadataAPIMapper.ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            String entityGUID = "<null>";

            if (originalEntity != null)
            {
                entityGUID = originalEntity.getGUID();
            }
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Update the instance status in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param newStatus new status value
     * @param newStatusParameterName parameter providing the new status value
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanStatusInRepository(String         userId,
                                             String         externalSourceGUID,
                                             String         externalSourceName,
                                             String         entityGUID,
                                             String         entityGUIDParameterName,
                                             String         entityTypeGUID,
                                             String         entityTypeName,
                                             boolean        forLineage,
                                             boolean        forDuplicateProcessing,
                                             InstanceStatus newStatus,
                                             String         newStatusParameterName,
                                             Date           effectiveTime,
                                             String         methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        updateBeanStatusInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     entityGUID,
                                     entityGUIDParameterName,
                                     entityTypeGUID,
                                     entityTypeName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     newStatus,
                                     newStatusParameterName,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Update the instance status in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param newStatus new status value
     * @param newStatusParameterName parameter providing the new status value
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanStatusInRepository(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             entityGUID,
                                             String             entityGUIDParameterName,
                                             String             entityTypeGUID,
                                             String             entityTypeName,
                                             boolean            forLineage,
                                             boolean            forDuplicateProcessing,
                                             List<String>       serviceSupportedZones,
                                             InstanceStatus     newStatus,
                                             String             newStatusParameterName,
                                             Date               effectiveTime,
                                             String             methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(newStatus, newStatusParameterName, methodName);

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                                  entityGUID,
                                                                  entityTypeName,
                                                                  originalEntity,
                                                                  entityGUIDParameterName,
                                                                  true,
                                                                  true,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  serviceSupportedZones,
                                                                  effectiveTime,
                                                                  methodName);

            /*
             * There is an extra security check if the update is for an asset.
             */
            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
            {
                securityVerifier.validateUserForAssetUpdate(userId,
                                                            originalEntity,
                                                            originalEntity.getProperties(),
                                                            newStatus,
                                                            repositoryHelper,
                                                            serviceName,
                                                            methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME))
            {
                securityVerifier.validateUserForGlossaryMemberStatusUpdate(userId,
                                                                           anchorEntity,
                                                                           repositoryHelper,
                                                                           serviceName,
                                                                           methodName);
            }

            repositoryHandler.updateEntityStatus(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 originalEntity.getGUID(),
                                                 originalEntity,
                                                 entityTypeGUID,
                                                 entityTypeName,
                                                 newStatus,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Updating instance status in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_STATUS_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             OpenMetadataAPIMapper.ENTITY_STATUS_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }


    /**
     * Undo the last update to the entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return recovered bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B    undoBeanUpdateInRepository(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             entityGUID,
                                           String             entityGUIDParameterName,
                                           String             entityTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return this.undoBeanUpdateInRepository(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               entityGUID,
                                               entityGUIDParameterName,
                                               entityTypeName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Undo the last update to the entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return recovered bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B    undoBeanUpdateInRepository(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             entityGUID,
                                           String             entityGUIDParameterName,
                                           String             entityTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           List<String>       serviceSupportedZones,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        return undoBeanUpdateInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          startingEntity,
                                          entityGUIDParameterName,
                                          entityTypeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Undo the last update to the entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param originalEntity unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return recovered bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B    undoBeanUpdateInRepository(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           EntityDetail       originalEntity,
                                           String             entityGUIDParameterName,
                                           String             entityTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           List<String>       serviceSupportedZones,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String originalEntityParameterName = "originalEntity";

        invalidParameterHandler.validateUserId(userId, methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                                  originalEntity.getGUID(),
                                                                  entityTypeName,
                                                                  originalEntity,
                                                                  entityGUIDParameterName,
                                                                  true,
                                                                  true,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  serviceSupportedZones,
                                                                  effectiveTime,
                                                                  methodName);

            EntityDetail recoveredEntity = repositoryHandler.undoEntityUpdate(userId,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              originalEntity.getGUID(),
                                                                              methodName);

            /*
             * There is an extra security check if the update is for an asset or glossary.
             */
            try
            {
                if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
                {
                    securityVerifier.validateUserForAssetUpdate(userId,
                                                                originalEntity,
                                                                recoveredEntity.getProperties(),
                                                                recoveredEntity.getStatus(),
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
                }
                else if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME))
                {
                    securityVerifier.validateUserForGlossaryDetailUpdate(userId,
                                                                         originalEntity,
                                                                         recoveredEntity.getProperties(),
                                                                         repositoryHelper,
                                                                         serviceName,
                                                                         methodName);
                }
            }
            catch (UserNotAuthorizedException notAuth)
            {
                /*
                 * Put the original entity back.
                 */
                repositoryHandler.updateEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               originalEntity.getGUID(),
                                               originalEntityParameterName,
                                               originalEntity.getType().getTypeDefGUID(),
                                               originalEntity.getType().getTypeDefName(),
                                               originalEntity.getProperties(),
                                               originalEntity.getClassifications(),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
                throw notAuth;
            }

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Undo last update of properties in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, originalEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             originalEntity.getGUID(),
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             OpenMetadataAPIMapper.ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            if (recoveredEntity != null)
            {
                return converter.getNewBean(beanClass, recoveredEntity, methodName);
            }
        }
        else
        {
            String entityGUID = "<null>";

            if (originalEntity != null)
            {
                entityGUID = originalEntity.getGUID();
            }
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }

        return null;
    }


    /**
     * Classify as a Memento any entity if it is anchored to the anchor entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorEntity entity anchor to match against
     * @param potentialAnchoredEntity entity to validate
     * @param classificationOriginGUID original entity that the Memento classification  was attached to
     * @param classificationProperties properties for the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorize to issue this request
     */
    private void archiveAnchoredEntity(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       EntityDetail       anchorEntity,
                                       EntityProxy        potentialAnchoredEntity,
                                       String             classificationOriginGUID,
                                       InstanceProperties classificationProperties,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        /*
         * Only need to progress if anchor entity exists.
         */
        if (anchorEntity != null)
        {
            final String guidParameterName = "potentialAnchoredEntity";

            if ((potentialAnchoredEntity != null) && (potentialAnchoredEntity.getType() != null))
            {
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        potentialAnchoredEntity.getGUID(),
                                                                        guidParameterName,
                                                                        potentialAnchoredEntity.getType().getTypeDefName(),
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                if ((anchorIdentifiers != null) && (anchorIdentifiers.equals(anchorEntity.getGUID())))
                {
                    this.archiveBeanInRepository(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 entity.getGUID(),
                                                 guidParameterName,
                                                 potentialAnchoredEntity.getType().getTypeDefName(),
                                                 ClassificationOrigin.PROPAGATED,
                                                 classificationOriginGUID,
                                                 classificationProperties,
                                                 anchorEntity,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either been deleted or archived.
     * The entity is preserved because it is needed for lineage.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationProperties properties for the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void archiveBeanInRepository(String             userId,
                                        String             externalSourceGUID,
                                        String             externalSourceName,
                                        String             entityGUID,
                                        String             entityGUIDParameterName,
                                        String             entityTypeName,
                                        InstanceProperties classificationProperties,
                                        boolean            forLineage,
                                        boolean            forDuplicateProcessing,
                                        Date               effectiveTime,
                                        String             methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        this.archiveBeanInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     entityGUID,
                                     entityGUIDParameterName,
                                     entityTypeName,
                                     classificationProperties,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived. Note, this method is designed to work only on anchor entities or entities with no anchor.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationProperties properties for the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void archiveBeanInRepository(String             userId,
                                        String             externalSourceGUID,
                                        String             externalSourceName,
                                        String             entityGUID,
                                        String             entityGUIDParameterName,
                                        String             entityTypeName,
                                        InstanceProperties classificationProperties,
                                        boolean            forLineage,
                                        boolean            forDuplicateProcessing,
                                        List<String>       serviceSupportedZones,
                                        Date               effectiveTime,
                                        String             methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              entityGUID,
                                                              entityGUIDParameterName,
                                                              entityTypeName,
                                                              true,
                                                              true,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              effectiveTime,
                                                              methodName);

        /*
         * At this point, archiving is only supported on the anchor entity.  This needs to change (eg to be able to archive schema elements)
         * by adding logic very similar to the templating logic that makes sure the archive processing travels down the hierarchy and does not
         * cover the whole anchored entity.
         */
        invalidParameterHandler.validateAnchorGUID(entityGUID,
                                                   entityGUIDParameterName,
                                                   anchorEntity,
                                                   entityGUID,
                                                   entityTypeName,
                                                   methodName);

        EntityDetail entity = this.archiveBeanInRepository(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           entityGUID,
                                                           entityGUIDParameterName,
                                                           entityTypeName,
                                                           ClassificationOrigin.ASSIGNED,
                                                           entityGUID,
                                                           classificationProperties,
                                                           anchorEntity,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        /*
         * Update the LatestChange in the archived entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
        {
            final String actionDescriptionTemplate = "Classifying as Memento %s %s";

            String actionDescription  = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (entity != null)
            {
                this.addLatestChangeToAnchor(entity,
                                             OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived.  Note that this classification is propagated to all elements with the same
     * AnchorGUID.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName parameter name supplying entityGUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationOrigin is this classification assigned or propagated?
     * @param classificationOriginGUID which entity did a propagated classification originate from?
     * @param classificationProperties properties for the classification
     * @param anchorEntity anchor entity for the bean (can be null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return GUID of archived entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private EntityDetail archiveBeanInRepository(String               userId,
                                                 String               externalSourceGUID,
                                                 String               externalSourceName,
                                                 String               entityGUID,
                                                 String               entityGUIDParameterName,
                                                 String               entityTypeName,
                                                 ClassificationOrigin classificationOrigin,
                                                 String               classificationOriginGUID,
                                                 InstanceProperties   classificationProperties,
                                                 EntityDetail         anchorEntity,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        /*
         * This is to pick up any errors in the iteration through the anchored elements.
         */
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail targetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                      entityGUID,
                                                                      entityGUIDParameterName,
                                                                      entityTypeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (targetEntity != null)
        {
            /*
             * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is archived.
             */
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           targetEntity,
                                                                                           entityTypeName,
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if (anchorEntity == null)
                {
                    this.archiveAnchoredEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               targetEntity,
                                               repositoryHandler.getOtherEnd(targetEntity.getGUID(), entityTypeName, relationship, 0, methodName),
                                               classificationOriginGUID,
                                               classificationProperties,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
                }
                else
                {
                    this.archiveAnchoredEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               anchorEntity,
                                               repositoryHandler.getOtherEnd(targetEntity.getGUID(), entityTypeName, relationship, 0, methodName),
                                               classificationOriginGUID,
                                               classificationProperties,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
                }
            }

            repositoryHandler.classifyEntity(userId,
                                             null,
                                             null,
                                             targetEntity.getGUID(),
                                             targetEntity,
                                             entityGUIDParameterName,
                                             entityTypeName,
                                             OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_GUID,
                                             OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                             classificationOrigin,
                                             classificationOriginGUID,
                                             classificationProperties,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);


            /*
             * Update the qualified name in the archived entity.
             */
            if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                String qualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                          targetEntity.getProperties(),
                                                                          methodName) + "_archivedOn_" + new Date();

                String entityTypeGUID = invalidParameterHandler.validateTypeName(entityTypeName,
                                                                                 OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);
                this.updateBeanPropertyInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    entityGUID,
                                                    entityGUIDParameterName,
                                                    entityTypeGUID,
                                                    entityTypeName,
                                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                    qualifiedName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
            }
        }

        return targetEntity;
    }


    /**
     * Remove an entity if it is anchored to the anchor entity
     *
     * @param anchoredEntityGUIDs entity anchors to match against
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param potentialAnchoredEntity entity to validate
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorize to issue this request
     */
    public void deleteAnchoredEntity(List<String> anchoredEntityGUIDs,
                                     String       externalSourceGUID,
                                     String       externalSourceName,
                                     EntityProxy  potentialAnchoredEntity,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        /*
         * Only need to progress if anchor entity exists.
         */
        if ((anchoredEntityGUIDs != null) && (! anchoredEntityGUIDs.isEmpty()))
        {
            final String guidParameterName = "potentialAnchoredEntity";

            if ((potentialAnchoredEntity != null) && (potentialAnchoredEntity.getType() != null))
            {
                String entityGUID = potentialAnchoredEntity.getGUID();
                String entityTypeName = potentialAnchoredEntity.getType().getTypeDefName();

                EntityDetail entity = repositoryHandler.getEntityByGUID(localServerUserId,
                                                                        entityGUID,
                                                                        guidParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                if ((anchorIdentifiers != null) && ((anchoredEntityGUIDs.contains(anchorIdentifiers.anchorGUID))))
                {
                    /*
                     * The element is part of the same set of elements for the anchorIdentifiers.
                     * If the element is still connected to the anchor then it should remain
                     * because it is a parent object.   If it now has no anchor then it can be
                     * deleted because it is a child object.
                     */
                    AnchorIdentifiers derivedAnchorGUID = this.deriveAnchorGUID(entity.getGUID(), entityTypeName, forLineage, forDuplicateProcessing, effectiveTime, methodName);

                    if (derivedAnchorGUID == null)
                    {
                        this.deleteBeanInRepository(localServerUserId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    entity.getGUID(),
                                                    guidParameterName,
                                                    potentialAnchoredEntity.getType().getTypeDefGUID(),
                                                    potentialAnchoredEntity.getType().getTypeDefName(),
                                                    null,
                                                    null,
                                                    anchoredEntityGUIDs,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
                    }
                }
            }
        }
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - or null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteBeanInRepository(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  entityGUID,
                                       String  entityGUIDParameterName,
                                       String  entityTypeGUID,
                                       String  entityTypeName,
                                       String  validatingPropertyName,
                                       String  validatingPropertyValue,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    validatingPropertyName,
                                    validatingPropertyValue,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - or null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteBeanInRepository(String       userId,
                                       String       externalSourceGUID,
                                       String       externalSourceName,
                                       String       entityGUID,
                                       String       entityGUIDParameterName,
                                       String       entityTypeGUID,
                                       String       entityTypeName,
                                       String       validatingPropertyName,
                                       String       validatingPropertyValue,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       List<String> serviceSupportedZones,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail  entity = repositoryHandler.getEntityByGUID(userId,
                                                                 entityGUID,
                                                                 entityGUIDParameterName,
                                                                 entityTypeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              entityGUID,
                                                              entityTypeName,
                                                              entity,
                                                              entityGUIDParameterName,
                                                              true,
                                                              false,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              effectiveTime,
                                                              methodName);

        List<String> anchorEntityGUIDs = new ArrayList<>();

        /*
         * The call above has validated that the entity to delete exists.
         * The anchorEntity is only set up if the entity to delete has an anchor entity.
         * This means it is not an anchor entity itself or without an anchor.
         */
        if (anchorEntity != null)
        {
            anchorEntityGUIDs.add(anchorEntity.getGUID());

            this.deleteBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        entityGUID,
                                        entityGUIDParameterName,
                                        entityTypeGUID,
                                        entityTypeName,
                                        validatingPropertyName,
                                        validatingPropertyValue,
                                        anchorEntityGUIDs,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

            /*
             * Update the LatestChange in the anchor entity if it is not the instance we have just deleted.
             */
            if (! entityGUID.equals(anchorEntity.getGUID()))
            {
                final String actionDescriptionTemplate = "Deleting %s %s";

                String actionDescription  = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);
                int    latestChangeTarget = OpenMetadataAPIMapper.ATTACHMENT_LATEST_CHANGE_TARGET_ORDINAL;

                this.addLatestChangeToAnchor(anchorEntity,
                                             latestChangeTarget,
                                             OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            anchorEntityGUIDs.add(entity.getGUID());

            this.deleteBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        entityGUID,
                                        entityGUIDParameterName,
                                        entityTypeGUID,
                                        entityTypeName,
                                        validatingPropertyName,
                                        validatingPropertyValue,
                                        anchorEntityGUIDs,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param anchorEntityGUIDs list of anchor entities for the bean (can be null)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteBeanInRepository(String       userId,
                                       String       externalSourceGUID,
                                       String       externalSourceName,
                                       String       entityGUID,
                                       String       entityGUIDParameterName,
                                       String       entityTypeGUID,
                                       String       entityTypeName,
                                       String       validatingPropertyName,
                                       String       validatingPropertyValue,
                                       List<String> anchorEntityGUIDs,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        /*
         * Initialize the lists if this is the first iteration
         */
        if (anchorEntityGUIDs == null)
        {
            anchorEntityGUIDs = new ArrayList<>();
        }

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            securityVerifier.validateUserForAssetDelete(userId,
                                                        startingEntity,
                                                        repositoryHelper,
                                                        serviceName,
                                                        methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME))
        {
            securityVerifier.validateUserForGlossaryDelete(userId,
                                                           startingEntity,
                                                           repositoryHelper,
                                                           serviceName,
                                                           methodName);
        }
        else if ((repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME)) ||
                 (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME)))
        {
            securityVerifier.validateUserForGlossaryMemberUpdate(userId,
                                                                 startingEntity,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);
        }

        /*
         * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is deleted.
         * (This is why we explicitly delete the relationship to the parent element before calling this method).
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntity,
                                                                                       entityTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);

            this.deleteAnchoredEntity(anchorEntityGUIDs,
                                      externalSourceGUID,
                                      externalSourceName,
                                      repositoryHandler.getOtherEnd(startingEntity.getGUID(),
                                                                    entityTypeName,
                                                                    relationship,
                                                                    0,
                                                                    methodName),
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }

        /*
         * This method explicitly removes all relationships attached to the entity before it deleted the entity.  This ensures that repository
         * events are created for all the relationships.  This is why the code above needs to deal with the nested entities first.
         */
        repositoryHandler.removeEntity(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       entityGUID,
                                       entityGUIDParameterName,
                                       entityTypeGUID,
                                       entityTypeName,
                                       validatingPropertyName,
                                       validatingPropertyValue,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Is the bean isolated - i.e. has no relationships.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of object to update
     * @param entityTypeName unique name of the entity's type
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return test results
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean isBeanIsolated(String       userId,
                                  String       entityGUID,
                                  String       entityTypeName,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        /*
         * Retrieve the first relationship, if there is one then we have relationships.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       entityGUID,
                                                                                       entityTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        return ! (iterator.moreToReceive());
    }


    /**
     * Return the elements of the requested type indirectly attached to an entity identified by the starting GUID via the listed relationship
     * types.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationshipPath list of unique identifier of the relationship types to follow to locate the attachment
     * @param relatedEntityTypeName unique name of the attached entity's type
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    List<String> getRelatedEntityGUIDs(String       userId,
                                       String       startingGUID,
                                       String       startingGUIDParameterName,
                                       String       startingTypeName,
                                       List<String> relationshipPath,
                                       String       relatedEntityTypeName,
                                       int          startingFrom,
                                       int          pageSize,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return this.getRelatedEntityGUIDs(userId,
                                          startingGUID,
                                          startingGUIDParameterName,
                                          startingTypeName,
                                          relationshipPath,
                                          relatedEntityTypeName,
                                          supportedZones,
                                          startingFrom,
                                          pageSize,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return the elements of the requested type indirectly attached to an entity identified by the starting GUID via the listed relationship
     * types.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationshipPath list of unique identifier of the relationship types to follow to locate the attachment
     * @param relatedEntityTypeName unique name of the attached entity's type
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    List<String> getRelatedEntityGUIDs(String       userId,
                                       String       startingGUID,
                                       String       startingGUIDParameterName,
                                       String       startingTypeName,
                                       List<String> relationshipPath,
                                       String       relatedEntityTypeName,
                                       List<String> serviceSupportedZones,
                                       int          startingFrom,
                                       int          pageSize,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        // todo
        invalidParameterHandler.throwMethodNotSupported(userId, serviceName, serverName, methodName);
        return null;
    }


    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<String> getAttachedElementGUIDs(String       userId,
                                                String       startingGUID,
                                                String       startingGUIDParameterName,
                                                String       startingTypeName,
                                                String       attachmentRelationshipTypeGUID,
                                                String       attachmentRelationshipTypeName,
                                                String       attachmentEntityTypeName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                int          startingFrom,
                                                int          pageSize,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getAttachedElementGUIDs(userId,
                                            startingGUID,
                                            startingGUIDParameterName,
                                            startingTypeName,
                                            attachmentRelationshipTypeGUID,
                                            attachmentRelationshipTypeName,
                                            attachmentEntityTypeName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            supportedZones,
                                            startingFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<String> getAttachedElementGUIDs(String       userId,
                                                String       startingGUID,
                                                String       startingGUIDParameterName,
                                                String       startingTypeName,
                                                String       attachmentRelationshipTypeGUID,
                                                String       attachmentRelationshipTypeName,
                                                String       attachmentEntityTypeName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                List<String> serviceSupportedZones,
                                                int          startingFrom,
                                                int          pageSize,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    startingEntity,
                                                                    startingGUIDParameterName,
                                                                    startingTypeName,
                                                                    attachmentRelationshipTypeGUID,
                                                                    attachmentRelationshipTypeName,
                                                                    null,
                                                                    attachmentEntityTypeName,
                                                                    0,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<String>  results = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingEntity.getGUID(),
                                                                        startingTypeName,
                                                                        relationship,
                                                                        0,
                                                                        methodName);
                if (entityProxy != null)
                {
                    try
                    {
                        this.validateAnchorEntity(userId,
                                                   entityProxy.getGUID(),
                                                   guidParameterName,
                                                   attachmentEntityTypeName,
                                                   false,
                                                   false,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   serviceSupportedZones,
                                                   effectiveTime,
                                                   methodName);

                        results.add(entityProxy.getGUID());
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException nonAccessibleEntity)
                    {
                        // skip entities that are not visible to this user
                        if (log.isDebugEnabled())
                        {
                            log.debug("Skipping entity: " + nonAccessibleEntity);
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
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public String getAttachedElementGUID(String  userId,
                                         String  startingGUID,
                                         String  startingGUIDParameterName,
                                         String  startingTypeName,
                                         String  attachmentRelationshipTypeGUID,
                                         String  attachmentRelationshipTypeName,
                                         String  attachmentEntityTypeName,
                                         int     selectionEnd,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return getAttachedElementGUID(userId,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingTypeName,
                                      attachmentRelationshipTypeGUID,
                                      attachmentRelationshipTypeName,
                                      attachmentEntityTypeName,
                                      selectionEnd,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public String getAttachedElementGUID(String       userId,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingTypeName,
                                         String       attachmentRelationshipTypeGUID,
                                         String       attachmentRelationshipTypeName,
                                         String       attachmentEntityTypeName,
                                         int          selectionEnd,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> serviceSupportedZones,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    startingEntity,
                                                                    startingGUIDParameterName,
                                                                    startingTypeName,
                                                                    attachmentRelationshipTypeGUID,
                                                                    attachmentRelationshipTypeName,
                                                                    null,
                                                                    attachmentEntityTypeName,
                                                                    0,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    0,
                                                                    invalidParameterHandler.getMaxPagingSize(),
                                                                    effectiveTime,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        String  result = null;

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingEntity.getGUID(),
                                                                        startingTypeName,
                                                                        relationship,
                                                                        selectionEnd,
                                                                        methodName);

                if (entityProxy != null)
                {
                    try
                    {
                        this.validateAnchorEntity(userId,
                                                  entityProxy.getGUID(),
                                                  guidParameterName,
                                                  attachmentEntityTypeName,
                                                  false,
                                                  false,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  serviceSupportedZones,
                                                  effectiveTime,
                                                  methodName);

                        if (result == null)
                        {
                            result = entityProxy.getGUID();
                        }
                        else
                        {
                            errorHandler.handleAmbiguousRelationships(startingGUID,
                                                                      startingTypeName,
                                                                      attachmentRelationshipTypeName,
                                                                      relationships,
                                                                      methodName);
                        }
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException nonAccessibleEntity)
                    {
                        // skip entities that are not visible to this user
                        if (log.isDebugEnabled())
                        {
                            log.debug("Skipping entity: " + nonAccessibleEntity);
                        }
                    }
                }
            }
        }

        return result;
    }


    /**
     * Return the Bean for the required relationship attached to a specific element.  This method assumes the starting element has
     * a validated anchor.
     *
     * @param userId     calling user
     * @param startingElementGUID identifier for the entity that the identifier is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public B getAttachedElement(String       userId,
                                String       startingElementGUID,
                                String       startingElementGUIDParameterName,
                                String       startingElementTypeName,
                                String       relationshipTypeGUID,
                                String       relationshipTypeName,
                                String       resultingElementTypeName,
                                int          attachmentEntityEnd,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                List<String> serviceSupportedZones,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingElementGUIDParameterName, methodName);

        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingElementGUID,
                                                                   startingElementGUIDParameterName,
                                                                   startingElementTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   serviceSupportedZones,
                                                                   effectiveTime,
                                                                   methodName);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                             startingEntity,
                                                                             startingElementTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             null,
                                                                             0,
                                                                             resultingElementTypeName,
                                                                             attachmentEntityEnd,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);

        if (entity != null)
        {
            validateAnchorEntity(userId,
                                 startingElementGUID,
                                 startingElementTypeName,
                                 entity,
                                 startingElementGUIDParameterName,
                                 true,
                                 false,
                                 forLineage,
                                 forDuplicateProcessing,
                                 serviceSupportedZones,
                                 effectiveTime,
                                 methodName);

            return converter.getNewBean(beanClass, entity, methodName);
        }

        return null;
    }

    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param limitResultsByEnumValues  The list of ordinals for the enum value.
     * @param enumPropertyName   String the name of a property in the relationship that is an enum.
     * @param attachmentEntityEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String         userId,
                                         String         startingGUID,
                                         String         startingGUIDParameterName,
                                         String         startingTypeName,
                                         String         attachmentRelationshipTypeGUID,
                                         String         attachmentRelationshipTypeName,
                                         String         attachmentEntityTypeName,
                                         String         requiredClassificationName,
                                         String         omittedClassificationName,
                                         List<Integer>  limitResultsByEnumValues,
                                         String         enumPropertyName,
                                         int            attachmentEntityEnd,
                                         boolean        forLineage,
                                         boolean        forDuplicateProcessing,
                                         int            startingFrom,
                                         int            pageSize,
                                         Date           effectiveTime,
                                         String         methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              startingGUID,
                                                              startingGUIDParameterName,
                                                              startingTypeName,
                                                              true,
                                                              false,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    startingGUID,
                                                                    startingGUIDParameterName,
                                                                    startingTypeName,
                                                                    attachmentRelationshipTypeGUID,
                                                                    attachmentRelationshipTypeName,
                                                                    null,
                                                                    attachmentEntityTypeName,
                                                                    attachmentEntityEnd,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    supportedZones,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<B>  results = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                try
                {
                    Integer relationshipOrdinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                                          enumPropertyName,
                                                                                          relationship.getProperties(),
                                                                                          methodName);

                    if (limitResultsByEnumValues.contains(relationshipOrdinal))
                    {
                        B bean = this.getAttachedElement(userId,
                                                         startingGUID,
                                                         startingGUIDParameterName,
                                                         startingTypeName,
                                                         relationship,
                                                         attachmentEntityTypeName,
                                                         requiredClassificationName,
                                                         omittedClassificationName,
                                                         attachmentEntityEnd,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         supportedZones,
                                                         effectiveTime,
                                                         methodName);
                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                }
                catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                {
                    // skip entities that are not visible to this user
                    if (log.isDebugEnabled())
                    {
                        log.debug("Skipping inaccessible entity: " + inaccessibleEntity);
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
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String  userId,
                                         String  startingGUID,
                                         String  startingGUIDParameterName,
                                         String  startingTypeName,
                                         String  attachmentRelationshipTypeGUID,
                                         String  attachmentRelationshipTypeName,
                                         String  attachmentEntityTypeName,
                                         String  requiredClassificationName,
                                         String  omittedClassificationName,
                                         int     selectionEnd,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         int     startingFrom,
                                         int     pageSize,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        attachmentRelationshipTypeGUID,
                                        attachmentRelationshipTypeName,
                                        attachmentEntityTypeName,
                                        requiredClassificationName,
                                        omittedClassificationName,
                                        selectionEnd,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param anchorGUID expected anchorGUID for this element
     * @param anchorGUIDParameterName parameter supplying anchorGUID
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String       userId,
                                         String       anchorGUID,
                                         String       anchorGUIDParameterName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingTypeName,
                                         String       attachmentRelationshipTypeGUID,
                                         String       attachmentRelationshipTypeName,
                                         String       attachmentEntityTypeName,
                                         String       requiredClassificationName,
                                         String       omittedClassificationName,
                                         int          selectionEnd,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> serviceSupportedZones,
                                         int          startingFrom,
                                         int          pageSize,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              startingGUID,
                                                              startingGUIDParameterName,
                                                              startingTypeName,
                                                              true,
                                                              false,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              effectiveTime,
                                                              methodName);

        invalidParameterHandler.validateAnchorGUID(anchorGUID, anchorGUIDParameterName, anchorEntity, startingGUID, startingTypeName, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    startingGUID,
                                                                    startingGUIDParameterName,
                                                                    startingTypeName,
                                                                    attachmentRelationshipTypeGUID,
                                                                    attachmentRelationshipTypeName,
                                                                    null,
                                                                    attachmentEntityTypeName,
                                                                    selectionEnd,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<B>  results = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                try
                {
                    B bean = this.getAttachedElement(userId,
                                                     startingGUID,
                                                     startingGUIDParameterName,
                                                     startingTypeName,
                                                     relationship,
                                                     attachmentEntityTypeName,
                                                     requiredClassificationName,
                                                     omittedClassificationName,
                                                     selectionEnd,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     serviceSupportedZones,
                                                     effectiveTime,
                                                     methodName);
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
                catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                {
                    // skip entities that are not visible to this user
                    if (log.isDebugEnabled())
                    {
                        log.debug("Skipping inaccessible entity: " + inaccessibleEntity);
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
     * Retrieve the requested element from the supplied relationship.
     *
     * @param userId       calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationship relationship between the requested element and the related keyword
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    private B getAttachedElement(String        userId,
                                 String        startingGUID,
                                 String        startingGUIDParameterName,
                                 String        startingTypeName,
                                 Relationship  relationship,
                                 String        attachmentEntityTypeName,
                                 String        requiredClassificationName,
                                 String        omittedClassificationName,
                                 int           selectionEnd,
                                 boolean       forLineage,
                                 boolean       forDuplicateProcessing,
                                 List<String>  serviceSupportedZones,
                                 Date          effectiveTime,
                                 String        methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        if (relationship != null)
        {
            EntityProxy entityProxy = null;

            if (selectionEnd == 0)
            {
                entityProxy = repositoryHandler.getOtherEnd(startingGUID,
                                                            startingTypeName,
                                                            relationship,
                                                            selectionEnd,
                                                            methodName);
            }
            else if (selectionEnd == 1)
            {
                entityProxy = relationship.getEntityOneProxy();
            }
            else if (selectionEnd == 2)
            {
                entityProxy = relationship.getEntityTwoProxy();
            }

            if (entityProxy != null)
            { 
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityProxy.getGUID(),
                                                                        guidParameterName,
                                                                        attachmentEntityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                this.validateAnchorEntity(userId,
                                          entityProxy.getGUID(),
                                          attachmentEntityTypeName,
                                          entity,
                                          guidParameterName,
                                          true,
                                          false,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);

                boolean beanValid = true;

                if (requiredClassificationName != null)
                {
                    try
                    {
                        if (repositoryHelper.getClassificationFromEntity(serviceName, entity, requiredClassificationName, methodName) == null)
                        {
                            beanValid = false;
                        }
                    }
                    catch (ClassificationErrorException error)
                    {
                        /*
                         * Since this classification is not supported, it can not be attached to the entity
                         */
                        beanValid = false;
                    }
                }

                if (omittedClassificationName != null)
                {
                    try
                    {
                        if (repositoryHelper.getClassificationFromEntity(serviceName, entity, omittedClassificationName, methodName) != null)
                        {
                            beanValid = false;
                        }
                    }
                    catch (ClassificationErrorException error)
                    {
                        /*
                         * Since this classification is not supported, it can not be attached to the entity
                         */
                    }
                }

                if (beanValid)
                {
                    /*
                     * Valid entity to return since no exception occurred.
                     */
                    return converter.getNewBean(beanClass, entity, relationship, methodName);
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the entities that are attached to the entity with startingGUID. The entities are only returned if they match the supplied filtering.
     * To be returned the attached entity needs to be directly attached to the entity with startingGUID:
     * <ul>
     * <li> with the relationship relationshipTypeName</li>
     * <li> the relationship relationshipTypeGUID</li>
     * <li> be at this end of the relationship.</li>
     * <li> it is visible to the calling user </li>
     * </ul>
     * <p>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> match the searchCriteria taking into account the ignoreCase and startsWith flags against the text property fields named in specificMatchPropertyNames.</li>
     *</ul>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> be in the requested page as specified by startFrom and queryPageSize</li>
     * </ul>
     *
     * @param userId       calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationshipTypeName name of the type of relationship attaching the attached entity
     * @param relationshipTypeGUID guid of the type of relationship attaching the attached entity
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param specificMatchPropertyNames list of property names to
     * @param searchCriteria text to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param startsWith if flag set search looking for matches starting with the supplied searchCriteria, otherwise an exact match
     * @param ignoreCase if set ignore case on the match, if not set then case must match
     * @param queryPageSize requested page size
     * @param methodName   calling method
     * @return List of attached entities
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedFilteredEntities(String        userId,
                                                          String        startingGUID,
                                                          String        startingGUIDParameterName,
                                                          String        startingTypeName,
                                                          String        relationshipTypeName,
                                                          String        relationshipTypeGUID,
                                                          int           selectionEnd,
                                                          Set<String>   specificMatchPropertyNames,
                                                          String        searchCriteria,
                                                          int           startFrom,
                                                          boolean       startsWith,
                                                          boolean       ignoreCase,
                                                          int           queryPageSize,
                                                          String        methodName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return getAttachedFilteredEntities(userId,
                                           startingGUID,
                                           startingGUIDParameterName,
                                           startingTypeName,
                                           relationshipTypeName,
                                           relationshipTypeGUID,
                                           selectionEnd,
                                           null,
                                           null,
                                           true,
                                           specificMatchPropertyNames,
                                           searchCriteria,
                                           startFrom,
                                           startsWith,
                                           ignoreCase,
                                           queryPageSize,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
    }


    /**
     * Retrieve the entities that are attached to the entity with startingGUID. The entities are only returned if they match the supplied filtering.
     *
     * To be returned the attached entity needs to be directly attached to the entity with startingGUID:
     * <ul>
     * <li> with the relationship relationshipTypeName</li>
     * <li> the relationship relationshipTypeGUID</li>
     * <li> be at this end of the relationship.</li>
     * <li> it is visible to the calling user </li>
     * </ul>
     * <p>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> match the searchCriteria taking into account the ignoreCase and startsWith flags against the text property fields named in specificMatchPropertyNames.</li>
     *</ul>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> be in the requested page as specified by startFrom and queryPageSize</li>
     * </ul>
     *
     * @param userId       calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationshipTypeName name of the type of relationship attaching the attached entity
     * @param relationshipTypeGUID guid of the type of relationship attaching the attached entity
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param specificMatchPropertyNames list of property names to
     * @param searchCriteria text to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param startsWith if flag set search looking for matches starting with the supplied searchCriteria, otherwise an exact match
     * @param ignoreCase if set ignore case on the match, if not set then case must match
     * @param queryPageSize requested page size
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     * @return List of attached entities
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedFilteredEntities(String        userId,
                                                          String        startingGUID,
                                                          String        startingGUIDParameterName,
                                                          String        startingTypeName,
                                                          String        relationshipTypeName,
                                                          String        relationshipTypeGUID,
                                                          int           selectionEnd,
                                                          Set<String>   specificMatchPropertyNames,
                                                          String        searchCriteria,
                                                          int           startFrom,
                                                          boolean       startsWith,
                                                          boolean       ignoreCase,
                                                          int           queryPageSize,
                                                          Date          effectiveTime,
                                                          String        methodName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return getAttachedFilteredEntities(userId,
                                           startingGUID,
                                           startingGUIDParameterName,
                                           startingTypeName,
                                           relationshipTypeName,
                                           relationshipTypeGUID,
                                           selectionEnd,
                                           null,
                                           null,
                                           true,
                                           specificMatchPropertyNames,
                                           searchCriteria,
                                           startFrom,
                                           startsWith,
                                           ignoreCase,
                                           queryPageSize,
                                           false,
                                           false,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Retrieve the entities that are attached to the entity with startingGUID. The entities are only returned if they match the supplied filtering.
     *
     * To be returned the attached entity needs to be directly attached to the entity with startingGUID:
     * <ul>
     * <li> with the relationship relationshipTypeName</li>
     * <li> the relationship relationshipTypeGUID</li>
     * <li> be at this end of the relationship.</li>
     * <li> it is visible to the calling user </li>
     * </ul>
     * <p>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> match the searchCriteria taking into account the ignoreCase and startsWith flags against the text property fields named in specificMatchPropertyNames.</li>
     *</ul>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> be in the requested page as specified by startFrom and queryPageSize</li>
     * </ul>
     * Optionally if specified, the attached entity needs to
     * <ul>
     * <li> not have a relationship to a unique parent entity via the attachedEntityFilterRelationshipTypeName</li>
     * <li> not have a relationship to a unique parent entity via the attachedEntityFilterRelationshipTypeGUID</li>
     * <li> not have a relationship to a unique parent entity where the parent is at the other end, the parent end is identified using attachedEntityParentAtEnd1</li>
     *</ul>
     *
     * @param userId       calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param relationshipTypeName name of the type of relationship attaching the attached entity
     * @param relationshipTypeGUID guid of the type of relationship attaching the attached entity
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param attachedEntityFilterRelationshipTypeName do not return attached entities that have this parent relationship at attachedEntityParentAtEnd1. If null this has no effect on the match.
     * @param attachedEntityFilterRelationshipTypeGUID do not return attached entities that have this parent relationship at attachedEntityParentAtEnd1. If null this has no effect on the match.
     * @param attachedEntityParentAtEnd1 if the attached entity has a parent the entity will not be returned.
     * @param specificMatchPropertyNames list of property names to
     * @param searchCriteria text to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param startsWith if flag set search looking for matches starting with the supplied searchCriteria, otherwise an exact match
     * @param ignoreCase if set ignore case on the match, if not set then case must match
     * @param queryPageSize requested page size
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     * @return List of attached entities
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedFilteredEntities(String        userId,
                                                          String        startingGUID,
                                                          String        startingGUIDParameterName,
                                                          String        startingTypeName,
                                                          String        relationshipTypeName,
                                                          String        relationshipTypeGUID,
                                                          int           selectionEnd,
                                                          String        attachedEntityFilterRelationshipTypeName,
                                                          String        attachedEntityFilterRelationshipTypeGUID,
                                                          boolean       attachedEntityParentAtEnd1,
                                                          Set<String>   specificMatchPropertyNames,
                                                          String        searchCriteria,
                                                          int           startFrom,
                                                          boolean       startsWith,
                                                          boolean       ignoreCase,
                                                          int           queryPageSize,
                                                          boolean       forLineage,
                                                          boolean       forDuplicateProcessing,
                                                          Date          effectiveTime,
                                                          String        methodName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        int localStartFrom = 0;

        RepositoryRelatedEntitiesIterator relatedEntityIterator = new RepositoryRelatedEntitiesIterator(repositoryHandler,
                                                                                                        invalidParameterHandler,
                                                                                                        userId,
                                                                                                        startingGUID,
                                                                                                        startingTypeName,
                                                                                                        relationshipTypeGUID,
                                                                                                        relationshipTypeName,
                                                                                                        null,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        localStartFrom,
                                                                                                        queryPageSize,
                                                                                                        selectionEnd,
                                                                                                        effectiveTime,
                                                                                                        methodName);
        // resultsToReturn is the subset of the filtered results to meets the requested startFrom
        List<EntityDetail> resultsToReturn = new ArrayList<>();
        // accumulate the total filtered results from 0 - so we can then honour the requested startFrom which is the index into the filtered results.
        List<EntityDetail> totalFilteredResults = new ArrayList<>();

        // iterate through the related entities applying the filter.
        while ((relatedEntityIterator.moreToReceive()) && ((queryPageSize == 0) || resultsToReturn.size() < queryPageSize))
        {
            EntityDetail relatedEntity = relatedEntityIterator.getNext();

            if (relatedEntity != null)
            {
                if (log.isDebugEnabled())
                {
                    String displayName = "";
                    String qualifiedName = "";

                    if (relatedEntity.getProperties() !=null && relatedEntity.getProperties().getInstanceProperties() != null )
                    {
                        if (relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME) != null)
                        {
                            displayName = relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME).toString();
                        }
                        else if (relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.NAME_PROPERTY_NAME) != null)
                        {
                            displayName = relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.NAME_PROPERTY_NAME).toString();
                        }
                        if ( relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME)!=null)
                        {
                            qualifiedName = relatedEntity.getProperties().getInstanceProperties().get(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME).toString();
                        }
                    }

                    log.debug("getAttachedFilteredEntities - while  relatedEntity guid=" + relatedEntity.getGUID() + ",displayName=" + displayName + ",qualifiedName=" + qualifiedName);
                }

                Relationship parentRelationship = null;
                // apply the filter if there is one
                if ((attachedEntityFilterRelationshipTypeGUID != null) && (attachedEntityFilterRelationshipTypeName != null))
                {
                    parentRelationship = repositoryHandler.getUniqueParentRelationshipByType(userId,
                                                                                             relatedEntity.getGUID(),
                                                                                             relatedEntity.getType().getTypeDefName(),
                                                                                             attachedEntityFilterRelationshipTypeGUID,
                                                                                             attachedEntityFilterRelationshipTypeName,
                                                                                             attachedEntityParentAtEnd1,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing,
                                                                                             effectiveTime,
                                                                                             methodName);

                    if (log.isDebugEnabled())
                    {
                        if (parentRelationship != null)
                        {
                            log.debug("getAttachedFilteredEntities - while found parent relationship  parentRelationship" + parentRelationship.getGUID());
                        }
                    }
                }

                // if there is a parentRelationship - this should not be included.
                if ((parentRelationship == null) && entityMatchSearchCriteria(relatedEntity, specificMatchPropertyNames, searchCriteria, !startsWith, ignoreCase))
                {
                    totalFilteredResults.add(relatedEntity);

                    if (totalFilteredResults.size() > startFrom)
                    {
                        resultsToReturn.add(relatedEntity);
                    }
                }
            }
        }

        if (resultsToReturn.isEmpty())
        {
            return null;
        }
        else
        {
            return resultsToReturn;
        }

    }


    /**
     * Check whether the attribute values, associated with the supplied attribute names, in the supplied entity match the search criteria. This text match is influenced by the
     * exactValue and ignoreCase flags.
     *
     * @param entity entity to check
     * @param attributeNames attribute names to check the value of - these are expected to be attributes that hold text values; if they will be ignored
     * @param searchCriteria literal text search criteria
     * @param exactValue when set match exactly otherwise look for matches starting with this text
     * @param ignoreCase when set ignore the case, otherwise do a case-sensitive match.
     * @return true for match otherwise false
     */
    protected boolean entityMatchSearchCriteria(EntityDetail entity,
                                                Set<String>  attributeNames,
                                                String       searchCriteria,
                                                boolean      exactValue,
                                                boolean      ignoreCase)
    {
        if (attributeNames == null)
        {
            return true; // TODO maybe this should be an error
        }

        if (searchCriteria == null)
        {
            return true; // nothing specific to match so it all matches
        }

        String regExedSearchCriteria = regexSearchCriteria(searchCriteria, exactValue, ignoreCase);
        boolean isMatch = false;
        InstanceProperties matchProperties = entity.getProperties();
        Iterator<String> propertyNames = matchProperties.getPropertyNames();

        if (propertyNames != null)
        {
            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();
                if (attributeNames.contains(propertyName))
                {
                    InstancePropertyValue instancePropertyValue = matchProperties.getPropertyValue(propertyName);

                    InstancePropertyCategory ipCat = instancePropertyValue.getInstancePropertyCategory();
                    if (ipCat == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) instancePropertyValue;
                        PrimitiveDefCategory pdCat = ppv.getPrimitiveDefCategory();
                        if (pdCat == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            String currentValue = (String) ppv.getPrimitiveValue();
                            if (currentValue != null && currentValue.matches(regExedSearchCriteria))
                            {
                                isMatch = true;
                            }
                        }

                        // TODO if there are no valid string attributes throw an error?
                    }
                }
            }
        }

        return isMatch;
    }


    /**
     * Take a literal string supplied in searchCriteria and augment it with extra content for the regex engine to process.
     * 2 flags exactValue and ignoreCase are supplied that determine the nature of the regex expression that is created.
     *
     * @param searchCriteria text literal use as the basis of the match, if this empty then match everything ignoring the flags.
     * @param exactValue the exactValue flag when set means to exactly match the string, otherwise it looks for strings starting with the searchCriteria.
     * @param ignoreCase if set ignore the case on the match, if not set then the case must match.
     * @return a regex expression created to match implement the supplied searchCriteria and flags.
     */
    protected String regexSearchCriteria(String searchCriteria, boolean exactValue, boolean ignoreCase)
    {
        if (searchCriteria == null || searchCriteria.trim().length() == 0)
        {
            // ignore the flags for an empty search criteria string - assume we want everything
            searchCriteria = ".*";
        }
        else
        {
            // lose any leading and trailing blanks
            searchCriteria = searchCriteria.trim();
            if (exactValue)
            {
                searchCriteria = repositoryHelper.getExactMatchRegex(searchCriteria, ignoreCase);
            }
            else
            {
                searchCriteria = repositoryHelper.getStartsWithRegex(searchCriteria, ignoreCase);
            }
        }

        return searchCriteria;
    }


    /**
     * Return the keyword for the supplied unique identifier (guid).  The keyword is only returned if
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String  userId,
                                                String  requestedEntityGUID,
                                                String  requestedEntityGUIDParameterName,
                                                String  requestedEntityTypeName,
                                                String  requiredClassificationName,
                                                String  omittedClassificationName,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return this.getEntityFromRepository(userId,
                                            requestedEntityGUID,
                                            requestedEntityGUIDParameterName,
                                            requestedEntityTypeName,
                                            requiredClassificationName,
                                            omittedClassificationName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            supportedZones,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the entity for the supplied unique identifier (guid).  An exception is thrown if the entity does not exist.
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid, the entity does not exist.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String       userId,
                                                String       requestedEntityGUID,
                                                String       requestedEntityGUIDParameterName,
                                                String       requestedEntityTypeName,
                                                String       requiredClassificationName,
                                                String       omittedClassificationName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                List<String> serviceSupportedZones,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(requestedEntityGUID, requestedEntityGUIDParameterName, methodName);

        EntityDetail  retrievedEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          requestedEntityGUID,
                                                                          requestedEntityGUIDParameterName,
                                                                          requestedEntityTypeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        /*
         * This method validates that the entity is visible to the calling user.
         */
        this.validateAnchorEntity(userId,
                                  retrievedEntity.getGUID(),
                                  requestedEntityTypeName,
                                  retrievedEntity,
                                  requestedEntityGUIDParameterName,
                                  true,
                                  false,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  effectiveTime,
                                  methodName);

        boolean beanValid = true;


        if (requiredClassificationName != null)
        {
            try
            {
                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, requiredClassificationName, methodName) == null)
                {
                    beanValid = false;
                }
            }
            catch (ClassificationErrorException error)
            {
                /*
                 * Since this classification is not supported, it can not be attached to the entity
                 */
                beanValid = false;
            }
        }

        if (omittedClassificationName != null)
        {
            try
            {
                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, omittedClassificationName, methodName) != null)
                {
                    beanValid = false;
                }
            }
            catch (ClassificationErrorException error)
            {
                /*
                 * Since this classification is not supported, it can not be attached to the entity
                 */
            }
        }

        if (! beanValid)
        {
            return null;
        }

        return retrievedEntity;
    }


    /**
     * Use the supplied unique identifier (guid) of an entity in the repository to retrieve its contents as a bean.
     * The entity is checked to ensure it is of the required return type.
     *
     * @param userId calling user
     * @param guid unique identifier of the entity to retrieve
     * @param guidParameterName parameter supplying the unique identifier
     * @param resultTypeName type of the retrieve entity
     * @param methodName calling method
     * @return B bean
     * @throws InvalidParameterException one of the properties (probably the GUID) is invalid
     * @throws PropertyServerException the repository services hit an unexpected problem
     * @throws UserNotAuthorizedException the user is not permitted to access this entity
     */
    public B getBeanFromRepository(String userId,
                                   String guid,
                                   String guidParameterName,
                                   String resultTypeName,
                                   String methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          resultTypeName,
                                          false,
                                          false,
                                          supportedZones,
                                          new Date(),
                                          methodName);
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the entity to retrieve from the repository
     * @param guidParameterName name of the parameter supplying the GUID
     * @param entityTypeName name of type of entity to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return new bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanFromRepository(String       userId,
                                   String       guid,
                                   String       guidParameterName,
                                   String       entityTypeName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          entityTypeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the entity to retrieve from the repository
     * @param guidParameterName name of the parameter supplying the GUID
     * @param entityTypeName name of type of entity to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return new bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanFromRepository(String       userId,
                                   String       guid,
                                   String       guidParameterName,
                                   String       entityTypeName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   List<String> serviceSupportedZones,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           entityTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           serviceSupportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            return converter.getNewBean(beanClass, entity, methodName);
        }

        return null;
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param entity entity retrieved from the repository
     * @param entityParameterName name of the parameter supplying the entity
     * @param methodName calling method
     *
     * @return new bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanFromEntity(String       userId,
                               EntityDetail entity,
                               String       entityParameterName,
                               String       methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(entity, entityParameterName, methodName);

        if (entity != null)
        {
            return converter.getNewBean(beanClass, entity, methodName);
        }

        return null;
    }


    /**
     * Return the requested string property for the supplied entity guid.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to retrieve
     * @param entityGUIDParameterName name of parameter providing the entityGUID
     * @param entityTypeName expected type of entity
     * @param propertyName name of property to extract
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String getBeanStringPropertyFromRepository(String       userId,
                                               String       entityGUID,
                                               String       entityGUIDParameterName,
                                               String       entityTypeName,
                                               String       propertyName,
                                               Date         effectiveTime,
                                               String       methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return this.getBeanStringPropertyFromRepository(userId,
                                                        entityGUID,
                                                        entityGUIDParameterName,
                                                        entityTypeName,
                                                        propertyName,
                                                        false,
                                                        false,
                                                        supportedZones,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Return the requested string property for the supplied entity guid.
     *
     * @param userId calling user
     * @param entityGUID unique identifier of entity to retrieve
     * @param entityGUIDParameterName name of parameter providing the entityGUID
     * @param entityTypeName expected type of entity
     * @param propertyName name of property to extract
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String getBeanStringPropertyFromRepository(String             userId,
                                               String             entityGUID,
                                               String             entityGUIDParameterName,
                                               String             entityTypeName,
                                               String             propertyName,
                                               boolean            forLineage,
                                               boolean            forDuplicateProcessing,
                                               List<String>       serviceSupportedZones,
                                               Date               effectiveTime,
                                               String             methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String propertyNameParameter = "propertyName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameter, methodName);

        String property = null;

        EntityDetail entityDetail = this.getEntityFromRepository(userId,
                                                                 entityGUID,
                                                                 entityGUIDParameterName,
                                                                 entityTypeName,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 serviceSupportedZones,
                                                                 effectiveTime,
                                                                 methodName);

        if (entityDetail != null)
        {
            property = repositoryHelper.getStringProperty(serviceName, propertyName, entityDetail.getProperties(), methodName);
        }

        return property;
    }


    /**
     * This method fills an instance properties object with the list of names properties, each with the supplied search value.
     * It is used when searching for a specific value that may be located in multiple properties.
     *
     * @param searchValue property value to store in each named property
     * @param specificMatchPropertyNames list of property names to
     * @param methodName calling method
     * @return instance properties object
     */
    private InstanceProperties getSearchInstanceProperties(String       searchValue,
                                                           List<String> specificMatchPropertyNames,
                                                           String       methodName)
    {
        InstanceProperties properties = new InstanceProperties();

        for (String propertyName : specificMatchPropertyNames)
        {
            if (propertyName != null)
            {
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          properties,
                                                                          propertyName,
                                                                          searchValue,
                                                                          methodName);
            }
        }

        return properties;
    }


    /**
     * Base on the parameters, load an appropriate repository helper iterator.
     *
     * @param userId calling user
     * @param searchString search string (or null to just return entities of a specific type)
     * @param resultTypeGUID type identifier of entities to return
     * @param resultTypeName type name of entities to return
     * @param specificMatchPropertyNames list of property names to look in (or null to search any string property)
     * @param exactValueMatch should the value be treated as a literal or a RegEx?
     * @param caseInsensitive set to true to have a case-insensitive exact match regular expression
     * @param sequencingPropertyName should the results be sequenced?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param queryPageSize maximum number of values to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return configured iterator
     */
    RepositoryIteratorForEntities getEntitySearchIterator(String       userId,
                                                          String       searchString,
                                                          String       resultTypeGUID,
                                                          String       resultTypeName,
                                                          List<String> specificMatchPropertyNames,
                                                          boolean      exactValueMatch,
                                                          boolean      caseInsensitive,
                                                          String       sequencingPropertyName,
                                                          boolean      forLineage,
                                                          boolean      forDuplicateProcessing,
                                                          int          startFrom,
                                                          int          queryPageSize,
                                                          Date         effectiveTime,
                                                          String       methodName) throws InvalidParameterException
    {
        RepositoryIteratorForEntities iterator;

        if (searchString != null)
        {
            String searchValue = searchString;

            if (exactValueMatch)
            {
                searchValue = repositoryHelper.getExactMatchRegex(searchString, caseInsensitive);
            }

            if ((specificMatchPropertyNames == null) || (specificMatchPropertyNames.isEmpty()))
            {
                /*
                 * Search for value in any string property
                 */
                iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                  invalidParameterHandler,
                                                                  userId,
                                                                  resultTypeGUID,
                                                                  searchValue,
                                                                  sequencingPropertyName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  startFrom,
                                                                  queryPageSize,
                                                                  effectiveTime,
                                                                  methodName);
            }
            else
            {
                /*
                 * Search for value in specific properties
                 */
                iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                  invalidParameterHandler,
                                                                  userId,
                                                                  resultTypeGUID,
                                                                  this.getSearchInstanceProperties(searchValue, specificMatchPropertyNames, methodName),
                                                                  MatchCriteria.ANY,
                                                                  sequencingPropertyName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  startFrom,
                                                                  queryPageSize,
                                                                  effectiveTime,
                                                                  methodName);
            }
        }
        else
        {
            /*
             * Get all values of a specific type
             */
            iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                      invalidParameterHandler,
                                                      userId,
                                                      resultTypeGUID,
                                                      resultTypeName,
                                                      sequencingPropertyName,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      startFrom,
                                                      queryPageSize,
                                                      effectiveTime,
                                                      methodName);
        }

        return iterator;
    }


    /**
     * Return the unique identifier of the entity that has the supplied unique name. An exception is thrown if
     * multiple entities are found with this name.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the requested entity/bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByUniqueName(String       userId,
                                          String       name,
                                          String       nameParameterName,
                                          String       namePropertyName,
                                          String       resultTypeGUID,
                                          String       resultTypeName,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return this.getBeanGUIDByUniqueName(userId,
                                            name,
                                            nameParameterName,
                                            namePropertyName,
                                            resultTypeGUID,
                                            resultTypeName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            supportedZones,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the unique identifier of the entity that has the supplied unique name. An exception is thrown if
     * multiple entities are found with this name.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the requested entity/bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByUniqueName(String       userId,
                                          String       name,
                                          String       nameParameterName,
                                          String       namePropertyName,
                                          String       resultTypeGUID,
                                          String       resultTypeName,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          List<String> serviceSupportedZones,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> propertyNames = new ArrayList<>();
        propertyNames.add(namePropertyName);

        int queryPageSize = invalidParameterHandler.getMaxPagingSize();

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         name,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         propertyNames,
                                                                         true,
                                                                         false,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        String        guid = null;
        List<String>  duplicateEntities = new ArrayList<>();
        String        entityParameterName = "Entity from search of value " + name;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (duplicateEntities.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                duplicateEntities.add(entity.getGUID());

                try
                {
                    validateAnchorEntity(userId,
                                         entity.getGUID(),
                                         resultTypeName,
                                         entity,
                                         entityParameterName,
                                         true,
                                         false,
                                         forLineage,
                                         forDuplicateProcessing,
                                         serviceSupportedZones,
                                         effectiveTime,
                                         methodName);

                    /*
                     * Valid entity to return since no exception occurred.
                     */
                    if (guid == null)
                    {
                        guid = entity.getGUID();
                    }
                }
                catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                {
                    /*
                     * Skipping entity
                     */
                }
            }
        }

        if (guid == null)
        {
            return null;
        }
        else if (duplicateEntities.size() == 1)
        {
            return guid;
        }

        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(resultTypeName,
                                                                                                                name,
                                                                                                                duplicateEntities.toString(),
                                                                                                                methodName,
                                                                                                                nameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByUniqueName(String       userId,
                                 String       name,
                                 String       nameParameterName,
                                 String       namePropertyName,
                                 String       resultTypeGUID,
                                 String       resultTypeName,
                                 boolean      forLineage,
                                 boolean      forDuplicateProcessing,
                                 Date         effectiveTime,
                                 String       methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return getBeanByUniqueName(userId,
                                   name,
                                   nameParameterName,
                                   namePropertyName,
                                   resultTypeGUID,
                                   resultTypeName,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByUniqueName(String       userId,
                                 String       name,
                                 String       nameParameterName,
                                 String       namePropertyName,
                                 String       resultTypeGUID,
                                 String       resultTypeName,
                                 boolean      forLineage,
                                 boolean      forDuplicateProcessing,
                                 List<String> serviceSupportedZones,
                                 Date         effectiveTime,
                                 String       methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> propertyNames = new ArrayList<>();
        propertyNames.add(namePropertyName);

        int queryPageSize = invalidParameterHandler.getMaxPagingSize();

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         name,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         propertyNames,
                                                                         true,
                                                                         false,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        B            bean = null;
        List<String> duplicateEntities = new ArrayList<>();
        String       entityParameterName = "Entity from search of value " + name;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (duplicateEntities.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                duplicateEntities.add(entity.getGUID());

                try
                {
                    validateAnchorEntity(userId,
                                         entity.getGUID(),
                                         resultTypeName,
                                         entity,
                                         entityParameterName,
                                         true,
                                         false,
                                         forLineage,
                                         forDuplicateProcessing,
                                         serviceSupportedZones,
                                         effectiveTime,
                                         methodName);

                    /*
                     * Valid entity to return since no exception occurred.
                     */
                    if (bean == null)
                    {
                        bean = converter.getNewBean(beanClass, entity, methodName);
                    }
                }
                catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                {
                    /*
                     * Skipping entity
                     */
                }
            }
        }

        if (bean == null)
        {
            return null;
        }
        else if (duplicateEntities.size() == 1)
        {
            return bean;
        }


        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(resultTypeName,
                                                                                                                name,
                                                                                                                duplicateEntities.toString(),
                                                                                                                methodName,
                                                                                                                nameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived. Note, this method is designed to work only on anchor entities or entities with no anchor.
     *
     * @param userId calling user
     * @param guid unique identifier of object to update
     * @param guidParameterName name of parameter supplying the GUID
     * @param fromTime starting time
     * @param toTime ending time
     * @param entityTypeName unique name of the entity's type
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param sequencingOrder order of the results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones supported zones for calling service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeanHistory(String                 userId,
                                  String                 guid,
                                  String                 guidParameterName,
                                  String                 entityTypeName,
                                  Date                   fromTime,
                                  Date                   toTime,
                                  int                    startingFrom,
                                  int                    pageSize,
                                  HistorySequencingOrder sequencingOrder,
                                  boolean                forLineage,
                                  boolean                forDuplicateProcessing,
                                  List<String>           serviceSupportedZones,
                                  Date                   effectiveTime,
                                  String                 methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  guid,
                                  guidParameterName,
                                  entityTypeName,
                                  true,
                                  true,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  effectiveTime,
                                  methodName);

        List<EntityDetail> entities = repositoryHandler.getEntityDetailHistory(userId,
                                                                               guid,
                                                                               fromTime,
                                                                               toTime,
                                                                               startingFrom,
                                                                               pageSize,
                                                                               sequencingOrder,
                                                                               methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                results.add(converter.getNewBean(beanClass, entity, methodName));
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }

    /**
     * Return the bean that matches the requested value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return matching bean.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the bean definition.
     */
    public  B getBeanByValue(String       userId,
                             String       value,
                             String       valueParameterName,
                             String       resultTypeGUID,
                             String       resultTypeName,
                             List<String> specificMatchPropertyNames,
                             boolean      forLineage,
                             boolean      forDuplicateProcessing,
                             Date         effectiveTime,
                             String       methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             value,
                                                             valueParameterName,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             null,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             effectiveTime,
                                                             methodName);


        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }
        else if (results.size() == 1)
        {
            return converter.getNewBean(beanClass, results.get(0), methodName);
        }
        else
        {
            errorHandler.handleAmbiguousEntityName(value,
                                                   valueParameterName,
                                                   resultTypeName,
                                                   results,
                                                   methodName);
        }

        return null;
    }


    /**
     * Return the list of beans matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findBeans(String       userId,
                             String       searchString,
                             String       searchStringParameterName,
                             String       resultTypeGUID,
                             String       resultTypeName,
                             String       sequencingPropertyName,
                             int          startFrom,
                             int          pageSize,
                             boolean      forLineage,
                             boolean      forDuplicateProcessing,
                             Date         effectiveTime,
                             String       methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getBeansByValue(userId,
                                    searchString,
                                    searchStringParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    null,
                                    false,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    sequencingPropertyName,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the list of beans matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param serviceSupportedZones list of supported zones for this service.
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findBeans(String       userId,
                             String       searchString,
                             String       searchStringParameterName,
                             String       resultTypeGUID,
                             String       resultTypeName,
                             boolean      forLineage,
                             boolean      forDuplicateProcessing,
                             List<String> serviceSupportedZones,
                             String       sequencingPropertyName,
                             int          startFrom,
                             int          pageSize,
                             Date         effectiveTime,
                             String       methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getBeansByValue(userId,
                                    searchString,
                                    searchStringParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    null,
                                    false,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    sequencingPropertyName,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
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
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findBeans(String                userId,
                             String                metadataElementTypeName,
                             List<String>          metadataElementSubtypeName,
                             SearchProperties      searchProperties,
                             List<InstanceStatus>  limitResultsByStatus,
                             SearchClassifications searchClassifications,
                             Date                  asOfTime,
                             String                sequencingProperty,
                             SequencingOrder       sequencingOrder,
                             boolean               forLineage,
                             boolean               forDuplicateProcessing,
                             int                   startingFrom,
                             int                   pageSize,
                             List<String>          serviceSupportedZones,
                             Date                  effectiveTime,
                             String                methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        List<EntityDetail> entities = this.findEntities(userId,
                                                        metadataElementTypeName,
                                                        metadataElementSubtypeName,
                                                        searchProperties,
                                                        limitResultsByStatus,
                                                        searchClassifications,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        startingFrom,
                                                        pageSize,
                                                        serviceSupportedZones,
                                                        effectiveTime,
                                                        methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    results.add(converter.getNewBean(beanClass, entity, methodName));
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
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<EntityDetail> findEntities(String                userId,
                                           String                metadataElementTypeName,
                                           List<String>          metadataElementSubtypeName,
                                           SearchProperties      searchProperties,
                                           List<InstanceStatus>  limitResultsByStatus,
                                           SearchClassifications searchClassifications,
                                           Date                  asOfTime,
                                           String                sequencingProperty,
                                           SequencingOrder       sequencingOrder,
                                           boolean               forLineage,
                                           boolean               forDuplicateProcessing,
                                           int                   startingFrom,
                                           int                   pageSize,
                                           Date                  effectiveTime,
                                           String                methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return findEntities(userId, metadataElementTypeName, metadataElementSubtypeName, searchProperties, limitResultsByStatus, searchClassifications, asOfTime, sequencingProperty, sequencingOrder, forLineage, forDuplicateProcessing, startingFrom, pageSize, supportedZones, effectiveTime, methodName);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<EntityDetail> findEntities(String                userId,
                                           String                metadataElementTypeName,
                                           List<String>          metadataElementSubtypeNames,
                                           SearchProperties      searchProperties,
                                           List<InstanceStatus>  limitResultsByStatus,
                                           SearchClassifications searchClassifications,
                                           Date                  asOfTime,
                                           String                sequencingProperty,
                                           SequencingOrder       sequencingOrder,
                                           boolean               forLineage,
                                           boolean               forDuplicateProcessing,
                                           int                   startingFrom,
                                           int                   pageSize,
                                           List<String>          serviceSupportedZones,
                                           Date                  effectiveTime,
                                           String                methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String entityGUIDParameterName = "foundEntity.GUID";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        String typeName = OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME;

        if (metadataElementTypeName != null)
        {
            typeName = metadataElementTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        List<String> subTypeGUIDs = null;

        if (metadataElementSubtypeNames != null)
        {
            subTypeGUIDs = new ArrayList<>();

            for (String subTypeName : metadataElementSubtypeNames)
            {
                if (subTypeName != null)
                {
                    String subTypeGUID = invalidParameterHandler.validateTypeName(subTypeName,
                                                                                  typeName,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

                    if (subTypeGUID != null)
                    {
                        subTypeGUIDs.add(subTypeGUID);
                    }
                }
            }
        }

        /*
         * Validate that the anchor guid means that the entity is visible to caller.
         */
        RepositoryFindEntitiesIterator iterator = new RepositoryFindEntitiesIterator(repositoryHandler,
                                                                                     invalidParameterHandler,
                                                                                     userId,
                                                                                     typeGUID,
                                                                                     subTypeGUIDs,
                                                                                     searchProperties,
                                                                                     limitResultsByStatus,
                                                                                     searchClassifications,
                                                                                     asOfTime,
                                                                                     sequencingProperty,
                                                                                     sequencingOrder,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     startingFrom,
                                                                                     queryPageSize,
                                                                                     effectiveTime,
                                                                                     methodName);

        List<EntityDetail> results = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                this.validateAnchorEntity(userId,
                                          entity.getGUID(),
                                          entity.getType().getTypeDefName(),
                                          entity,
                                          entityGUIDParameterName,
                                          false,
                                          false,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          effectiveTime,
                                          methodName);
                results.add(entity);
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByValue(String       userId,
                                   String       value,
                                   String       valueParameterName,
                                   String       resultTypeGUID,
                                   String       resultTypeName,
                                   List<String> specificMatchPropertyNames,
                                   boolean      exactValueMatch,
                                   String       requiredClassificationName,
                                   String       omittedClassificationName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   String       sequencingPropertyName,
                                   int          startFrom,
                                   int          pageSize,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        return this.getBeansByValue(userId,
                                    value,
                                    valueParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    exactValueMatch,
                                    requiredClassificationName,
                                    omittedClassificationName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    sequencingPropertyName,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }



    /**
     * Return the list of beans of the requested type that match the supplied integer value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param propertyName property  to look in - if null or empty list then all string properties are checked.
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByIntValue(String       userId,
                                      int          value,
                                      String       resultTypeGUID,
                                      String       resultTypeName,
                                      String       propertyName,
                                      String       requiredClassificationName,
                                      String       omittedClassificationName,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      List<String> serviceSupportedZones,
                                      String       sequencingPropertyName,
                                      int          startFrom,
                                      int          pageSize,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        List<EntityDetail> entities = getEntitiesByIntValue(userId,
                                                            value,
                                                            resultTypeGUID,
                                                            resultTypeName,
                                                            propertyName,
                                                            requiredClassificationName,
                                                            omittedClassificationName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            serviceSupportedZones,
                                                            sequencingPropertyName,
                                                            startFrom,
                                                            pageSize,
                                                            effectiveTime,
                                                            methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = converter.getNewBean(beanClass, entity, methodName);

                    results.add(bean);
                }
            }

            if (! results.isEmpty())
            {
                return  results;
            }
        }

        return null;
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByValue(String       userId,
                                   String       value,
                                   String       valueParameterName,
                                   String       resultTypeGUID,
                                   String       resultTypeName,
                                   List<String> specificMatchPropertyNames,
                                   boolean      exactValueMatch,
                                   String       requiredClassificationName,
                                   String       omittedClassificationName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   List<String> serviceSupportedZones,
                                   String       sequencingPropertyName,
                                   int          startFrom,
                                   int          pageSize,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        List<EntityDetail> entities = getEntitiesByValue(userId,
                                                         value,
                                                         valueParameterName,
                                                         resultTypeGUID,
                                                         resultTypeName,
                                                         specificMatchPropertyNames,
                                                         exactValueMatch,
                                                         false,
                                                         requiredClassificationName,
                                                         omittedClassificationName,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         serviceSupportedZones,
                                                         sequencingPropertyName,
                                                         startFrom,
                                                         pageSize,
                                                         effectiveTime,
                                                         methodName);


        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = converter.getNewBean(beanClass, entity, methodName);

                    results.add(bean);
                }
            }

            if (! results.isEmpty())
            {
                return  results;
            }
        }

        return null;
    }


    /**
     * Return the list of entities of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByType(String       userId,
                                                String       resultTypeGUID,
                                                String       resultTypeName,
                                                String       sequencingPropertyName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                int          startFrom,
                                                int          pageSize,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return getEntitiesByType(userId, resultTypeGUID, resultTypeName, sequencingPropertyName, forLineage, forDuplicateProcessing, supportedZones, startFrom, pageSize, effectiveTime, methodName);
    }


    /**
     * Return the list of entities of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByType(String       userId,
                                                String       resultTypeGUID,
                                                String       resultTypeName,
                                                String       sequencingPropertyName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                List<String> serviceSupportedZones,
                                                int          startFrom,
                                                int          pageSize,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         null,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         sequencingPropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);


        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<EntityDetail>  results = new ArrayList<>();
        String entityParameterName = "Entity of type" + resultTypeName;
        int skippedValues = 0;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                try
                {
                    validateAnchorEntity(userId,
                                         entity.getGUID(),
                                         resultTypeName,
                                         entity,
                                         entityParameterName,
                                         false,
                                         false,
                                         forLineage,
                                         forDuplicateProcessing,
                                         serviceSupportedZones,
                                         effectiveTime,
                                         methodName);

                    boolean beanArchived = false;

                    try
                    {
                        /*
                         * The Memento classification means the entity is archived and should only be returned for lineage requests.
                         * This method is not to be used for lineage requests.
                         */
                        if (repositoryHelper.getClassificationFromEntity(serviceName, entity, OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME, methodName) != null)
                        {
                            beanArchived = true;
                        }
                    }
                    catch (ClassificationErrorException error)
                    {
                        /*
                         * Since this classification is not supported, it can not be attached to the entity.
                         */
                    }

                    if (! beanArchived)
                    {
                        /*
                         * Valid entity to return since no exception occurred and the entity has not been archived.
                         */
                        if (skippedValues < startFrom)
                        {
                            skippedValues ++;
                        }
                        else
                        {
                            results.add(entity);
                        }
                    }
                }
                catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                {
                    /*
                     * Skipping entity
                     */
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
     * Return the list of entities matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> findEntities(String       userId,
                                           String       searchString,
                                           String       searchStringParameterName,
                                           String       resultTypeGUID,
                                           String       resultTypeName,
                                           String       requiredClassificationName,
                                           String       omittedClassificationName,
                                           String       sequencingPropertyName,
                                           int          startFrom,
                                           int          pageSize,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return findEntities(userId, searchString, searchStringParameterName, resultTypeGUID, resultTypeName, requiredClassificationName, omittedClassificationName, sequencingPropertyName, startFrom, pageSize, forLineage, forDuplicateProcessing, supportedZones, effectiveTime, methodName);
    }


    /**
     * Return the list of entities matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> findEntities(String       userId,
                                           String       searchString,
                                           String       searchStringParameterName,
                                           String       resultTypeGUID,
                                           String       resultTypeName,
                                           String       requiredClassificationName,
                                           String       omittedClassificationName,
                                           String       sequencingPropertyName,
                                           int          startFrom,
                                           int          pageSize,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           List<String> serviceSupportedZones,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return this.getEntitiesByValue(userId,
                                       searchString,
                                       searchStringParameterName,
                                       resultTypeGUID,
                                       resultTypeName,
                                       null,
                                       false,
                                       false,
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       serviceSupportedZones,
                                       sequencingPropertyName,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByValue(String       userId,
                                                 String       value,
                                                 String       valueParameterName,
                                                 String       resultTypeGUID,
                                                 String       resultTypeName,
                                                 List<String> specificMatchPropertyNames,
                                                 boolean      exactValueMatch,
                                                 String       requiredClassificationName,
                                                 String       omittedClassificationName,
                                                 boolean      forLineage,
                                                 boolean      forDuplicateProcessing,
                                                 int          startFrom,
                                                 int          pageSize,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return this.getEntitiesByValue(userId,
                                       value,
                                       valueParameterName,
                                       resultTypeGUID,
                                       resultTypeName,
                                       specificMatchPropertyNames,
                                       exactValueMatch,
                                       false,
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       null,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param propertyName  property name to look in.
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage boolean indicating whether the entity is being retrieved for a lineage request or not
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByIntValue(String       userId,
                                                    int          value,
                                                    String       resultTypeGUID,
                                                    String       resultTypeName,
                                                    String       propertyName,
                                                    String       requiredClassificationName,
                                                    String       omittedClassificationName,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    List<String> serviceSupportedZones,
                                                    String       sequencingPropertyName,
                                                    int          startFrom,
                                                    int          pageSize,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String propertyParameterName = "propertyName";

        String entityParameterName = "Entity from search of value " + value;

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RepositoryIteratorForEntities iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                                        invalidParameterHandler,
                                                                                        userId,
                                                                                        resultTypeGUID,
                                                                                        repositoryHelper.addIntPropertyToInstance(serviceName, null, propertyName, value, methodName),
                                                                                        MatchCriteria.ANY,
                                                                                        sequencingPropertyName,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        0,
                                                                                        queryPageSize,
                                                                                        effectiveTime,
                                                                                        methodName);

        return getEntitiesByValue(userId,
                                  iterator,
                                  entityParameterName,
                                  resultTypeName,
                                  requiredClassificationName,
                                  omittedClassificationName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  startFrom,
                                  queryPageSize,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByValue(String       userId,
                                                 String       value,
                                                 String       valueParameterName,
                                                 String       resultTypeGUID,
                                                 String       resultTypeName,
                                                 List<String> specificMatchPropertyNames,
                                                 boolean      exactValueMatch,
                                                 String       requiredClassificationName,
                                                 String       omittedClassificationName,
                                                 boolean      forLineage,
                                                 boolean      forDuplicateProcessing,
                                                 String       sequencingPropertyName,
                                                 int          startFrom,
                                                 int          pageSize,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        return this.getEntitiesByValue(userId,
                                       value,
                                       valueParameterName,
                                       resultTypeGUID,
                                       resultTypeName,
                                       specificMatchPropertyNames,
                                       exactValueMatch,
                                       false,
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       sequencingPropertyName,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }

    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param caseInsensitive set to true to have a case-insensitive exact match regular expression
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByValue(String       userId,
                                                 String       value,
                                                 String       valueParameterName,
                                                 String       resultTypeGUID,
                                                 String       resultTypeName,
                                                 List<String> specificMatchPropertyNames,
                                                 boolean      exactValueMatch,
                                                 boolean      caseInsensitive,
                                                 String       requiredClassificationName,
                                                 String       omittedClassificationName,
                                                 boolean      forLineage,
                                                 boolean      forDuplicateProcessing,
                                                 List<String> serviceSupportedZones,
                                                 String       sequencingPropertyName,
                                                 int          startFrom,
                                                 int          pageSize,
                                                 Date         effectiveTime,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(value, valueParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        /*
         * Notice that the startFrom is 0 - it allows the filtering process to skip over the right number of
         * elements.
         */
        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         value,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         specificMatchPropertyNames,
                                                                         exactValueMatch,
                                                                         caseInsensitive,
                                                                         sequencingPropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        String entityParameterName = "Entity from search of value " + value;

        return getEntitiesByValue(userId,
                                  iterator,
                                  entityParameterName,
                                  resultTypeName,
                                  requiredClassificationName,
                                  omittedClassificationName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  serviceSupportedZones,
                                  startFrom,
                                  queryPageSize,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param iterator mechanism for search
     * @param entityParameterName parameter description
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage                   boolean indicating whether the entity is being retrieved for a lineage request or not
     * @param forDuplicateProcessing       the query is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list to start from (0 for start)
     * @param queryPageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByValue(String                        userId,
                                                 RepositoryIteratorForEntities iterator,
                                                 String                        entityParameterName,
                                                 String                        resultTypeName,
                                                 String                        requiredClassificationName,
                                                 String                        omittedClassificationName,
                                                 boolean                       forLineage,
                                                 boolean                       forDuplicateProcessing,
                                                 List<String>                  serviceSupportedZones,
                                                 int                           startFrom,
                                                 int                           queryPageSize,
                                                 Date                          effectiveTime,
                                                 String                        methodName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<EntityDetail> results = new ArrayList<>();
        int                skippedValues = 0;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                boolean beanValid = true;

                try
                {
                    validateAnchorEntity(userId,
                                         entity.getGUID(),
                                         resultTypeName,
                                         entity,
                                         entityParameterName,
                                         false,
                                         false,
                                         forLineage,
                                         forDuplicateProcessing,
                                         serviceSupportedZones,
                                         effectiveTime,
                                         methodName);


                    if (requiredClassificationName != null)
                    {
                        try
                        {
                            if (repositoryHelper.getClassificationFromEntity(serviceName, entity, requiredClassificationName, methodName) == null)
                            {
                                beanValid = false;
                            }
                        }
                        catch (ClassificationErrorException error)
                        {
                            /*
                             * Since this classification is not supported, it can not be attached to the entity
                             */
                            beanValid = false;
                        }
                    }

                    if (omittedClassificationName != null)
                    {
                        try
                        {
                            if (repositoryHelper.getClassificationFromEntity(serviceName, entity, omittedClassificationName, methodName) != null)
                            {
                                beanValid = false;
                            }
                        }
                        catch (ClassificationErrorException error)
                        {
                            // ok - don't care
                        }
                    }
                }
                catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                {
                    /*
                     * Skipping entity
                     */
                    beanValid = false;
                }

                if (beanValid)
                {
                    /*
                     * Ignore entities until it reaches the start point
                     */
                    if (skippedValues < startFrom)
                    {
                        skippedValues ++;
                    }
                    else
                    {
                        results.add(entity);
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
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param forLineage boolean indicating whether the entity is being retrieved for a lineage request or not.
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forDuplicateProcessing       the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getEntityGUIDsByValue(String       userId,
                                              String       value,
                                              String       valueParameterName,
                                              String       resultTypeGUID,
                                              String       resultTypeName,
                                              List<String> specificMatchPropertyNames,
                                              boolean      exactValueMatch,
                                              String       requiredClassificationName,
                                              String       omittedClassificationName,
                                              boolean      forLineage,
                                              boolean      forDuplicateProcessing,
                                              List<String> serviceSupportedZones,
                                              String       sequencingPropertyName,
                                              int          startFrom,
                                              int          pageSize,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        List<EntityDetail> entities = this.getEntitiesByValue(userId,
                                                              value,
                                                              valueParameterName,
                                                              resultTypeGUID,
                                                              resultTypeName,
                                                              specificMatchPropertyNames,
                                                              exactValueMatch,
                                                              false,
                                                              requiredClassificationName,
                                                              omittedClassificationName,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              serviceSupportedZones,
                                                              sequencingPropertyName,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              methodName);

        if (entities != null)
        {
            List<String> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    results.add(entity.getGUID());
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
     * Return the entity that matches the requested value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return requested entity
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public  EntityDetail getEntityByValue(String       userId,
                                          String       value,
                                          String       valueParameterName,
                                          String       resultTypeGUID,
                                          String       resultTypeName,
                                          List<String> specificMatchPropertyNames,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             value,
                                                             valueParameterName,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             false,
                                                             null,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             null,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             effectiveTime,
                                                             methodName);


        if (results != null)
        {
            if (results.size() == 1)
            {
                return results.get(0);
            }
            else if (results.size() > 1)
            {
                errorHandler.handleAmbiguousEntityName(value,
                                                       valueParameterName,
                                                       resultTypeName,
                                                       results,
                                                       methodName);
            }
        }

        return null;
    }


    /**
     * Return the unique identifier of the entity matching the value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the requested entity.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public  String getEntityGUIDByValue(String       userId,
                                        String       value,
                                        String       valueParameterName,
                                        String       resultTypeGUID,
                                        String       resultTypeName,
                                        List<String> specificMatchPropertyNames,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        EntityDetail result = this.getEntityByValue(userId,
                                                    value,
                                                    valueParameterName,
                                                    resultTypeGUID,
                                                    resultTypeName,
                                                    specificMatchPropertyNames,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);


        if (result != null)
        {
            return result.getGUID();
        }

        return null;
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> findBeanGUIDs(String       userId,
                                      String       searchString,
                                      String       searchStringParameterName,
                                      String       resultTypeGUID,
                                      String       resultTypeName,
                                      String       sequencingPropertyName,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.getEntityGUIDsByValue(userId,
                                          searchString,
                                          searchStringParameterName,
                                          resultTypeGUID,
                                          resultTypeName,
                                          null,
                                          false,
                                          null,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          sequencingPropertyName,
                                          startFrom,
                                          pageSize,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return the list of beans of the requested type that were created by the requesting user.
     *
     * @param userId the name of the calling user
     * @param searchString value that describes what to search for
     * @param searchStringParameterName parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param specificMatchPropertyNames name of properties to visit
     * @param exactValueMatch does the value need to match exactly?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName should the results be sequenced?
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByCreator(String       userId,
                                     String       searchString,
                                     String       searchStringParameterName,
                                     String       resultTypeGUID,
                                     String       resultTypeName,
                                     List<String> specificMatchPropertyNames,
                                     boolean      exactValueMatch,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     List<String> serviceSupportedZones,
                                     String       sequencingPropertyName,
                                     int          startFrom,
                                     int          pageSize,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         searchString,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         specificMatchPropertyNames,
                                                                         exactValueMatch,
                                                                         false,
                                                                         sequencingPropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<B>  results = new ArrayList<>();
        String   entityParameterName = "Entity from createdBy search of " + searchString;
        int      skippedValues = 0;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                /*
                 * Eliminate anything that was not created by this user
                 */
                if (userId.equals(entity.getCreatedBy()))
                {
                    try
                    {
                        validateAnchorEntity(userId,
                                             entity.getGUID(),
                                             resultTypeName,
                                             entity,
                                             entityParameterName,
                                             false,
                                             false,
                                             forLineage,
                                             forDuplicateProcessing,
                                             serviceSupportedZones,
                                             effectiveTime,
                                             methodName);

                        boolean beanArchived = false;

                        try
                        {
                            /*
                             * The Memento classification means the entity is archived and should only be returned for lineage requests.
                             * This method is not to be used for lineage requests.
                             */
                            if (repositoryHelper.getClassificationFromEntity(serviceName, entity, OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME, methodName) != null)
                            {
                                beanArchived = true;
                            }
                        }
                        catch (ClassificationErrorException error)
                        {
                            /*
                             * Since this classification is not supported, it can not be attached to the entity.
                             */
                        }

                        /*
                         * Valid entity to return since no exception occurred.
                         */
                        if (! beanArchived)
                        {
                            B bean = converter.getNewBean(beanClass, entity, methodName);
                            if (bean != null)
                            {
                                if (skippedValues < startFrom)
                                {
                                    skippedValues ++;
                                }
                                else
                                {
                                    results.add(bean);
                                }
                            }
                        }
                    }
                    catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                    {
                        /*
                         * Skipping entity
                         */
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
     * Return the list of beans of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByType(String       userId,
                                  String       resultTypeGUID,
                                  String       resultTypeName,
                                  String       sequencingPropertyName,
                                  int          startFrom,
                                  int          pageSize,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return getBeansByType(userId,
                              resultTypeGUID,
                              resultTypeName,
                              sequencingPropertyName,
                              forLineage,
                              forDuplicateProcessing,
                              supportedZones,
                              startFrom,
                              pageSize,
                              effectiveTime,
                              methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied name.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param serviceSupportedZones list of supported zones for this service
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByType(String       userId,
                                  String       resultTypeGUID,
                                  String       resultTypeName,
                                  String       sequencingPropertyName,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  List<String> serviceSupportedZones,
                                  int          startFrom,
                                  int          pageSize,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             sequencingPropertyName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             serviceSupportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
                                                             methodName);

        if ((entities != null)  && (! entities.isEmpty()))
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = converter.getNewBean(beanClass, entity, methodName);
                    if (bean != null)
                    {
                        results.add(bean);
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
     * Return the list of beans of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param sequencingPropertyName should the results be sequenced?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for matching beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByType(String       userId,
                                           String       resultTypeGUID,
                                           String       resultTypeName,
                                           String       sequencingPropertyName,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           int          startFrom,
                                           int          pageSize,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return this.getBeanGUIDsByType(userId, resultTypeGUID, resultTypeName, sequencingPropertyName, forLineage, forDuplicateProcessing, supportedZones, startFrom, pageSize, effectiveTime, methodName);
    }


    /**
     * Return the list of beans of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param sequencingPropertyName should the results be sequenced?
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for matching beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByType(String       userId,
                                           String       resultTypeGUID,
                                           String       resultTypeName,
                                           String       sequencingPropertyName,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           List<String> serviceSupportedZones,
                                           int          startFrom,
                                           int          pageSize,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         null,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         sequencingPropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<String> results = new ArrayList<>();
        String       entityParameterName = "Entity of type" + resultTypeName;
        int          skippedValues = 0;

        while (iterator.moreToReceive() && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                try
                {
                    validateAnchorEntity(userId,
                                         entity.getGUID(),
                                         resultTypeName,
                                         entity,
                                         entityParameterName,
                                         false,
                                         false,
                                         forLineage,
                                         forDuplicateProcessing,
                                         serviceSupportedZones,
                                         effectiveTime,
                                         methodName);

                    /*
                     * Valid entity to return since no exception occurred.
                     */
                    if (skippedValues < startFrom)
                    {
                        skippedValues ++;
                    }
                    else
                    {
                        results.add(entity.getGUID());
                    }
                }
                catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                {
                    /*
                     * Skipping entity
                     */
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
     * Return the list of beans of the requested type that match the supplied classification.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultClassificationName unique name of the classification that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByClassification(String  userId,
                                            String  resultTypeGUID,
                                            String  resultClassificationName,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         resultTypeGUID,
                                                                                         resultClassificationName,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        if (entities != null)
        {
            List<B>  beans = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    beans.add(converter.getNewBean(beanClass, entity, methodName));
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
    }


    /**
     * Return the list of beans of the requested type that match the supplied classification.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultClassificationName unique name of the classification that the results should match with
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of guids representing beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByClassification(String  userId,
                                                     String  resultTypeGUID,
                                                     String  resultClassificationName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         resultTypeGUID,
                                                                                         resultClassificationName,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        if (entities != null)
        {
            List<String>  softwareServerCapabilityGUIDs = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    softwareServerCapabilityGUIDs.add(entity.getGUID());
                }
            }

            if (! softwareServerCapabilityGUIDs.isEmpty())
            {
                return softwareServerCapabilityGUIDs;
            }
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param relationshipTypeGUID      unique identifier of type of the relationship to create
     * @param relationshipTypeName      unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkElementToElement(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             startingGUID,
                                       String             startingGUIDParameterName,
                                       String             startingElementTypeName,
                                       String             attachingGUID,
                                       String             attachingGUIDParameterName,
                                       String             attachingElementTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       String             relationshipTypeGUID,
                                       String             relationshipTypeName,
                                       InstanceProperties relationshipProperties,
                                       Date               effectiveFrom,
                                       Date               effectiveTo,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return this.linkElementToElement(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         startingGUID,
                                         startingGUIDParameterName,
                                         startingElementTypeName,
                                         attachingGUID,
                                         attachingGUIDParameterName,
                                         attachingElementTypeName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         supportedZones,
                                         relationshipTypeGUID,
                                         relationshipTypeName,
                                         relationshipProperties,
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param relationshipTypeGUID      unique identifier of type of the relationship to create
     * @param relationshipTypeName      unique name of type of the relationship to create
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkElementToElement(String              userId,
                                       String              externalSourceGUID,
                                       String              externalSourceName,
                                       String              startingGUID,
                                       String              startingGUIDParameterName,
                                       String              startingElementTypeName,
                                       String              attachingGUID,
                                       String              attachingGUIDParameterName,
                                       String              attachingElementTypeName,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       String              relationshipTypeGUID,
                                       String              relationshipTypeName,
                                       Date                effectiveFrom,
                                       Date                effectiveTo,
                                       Map<String, Object> relationshipProperties,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        InstanceProperties instanceProperties = null;

        try
        {
            instanceProperties = repositoryHelper.addPropertyMapToInstance(serviceName, null, relationshipProperties, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            final String  propertyName = "relationshipProperties";

            errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
        }

        return this.linkElementToElement(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         startingGUID,
                                         startingGUIDParameterName,
                                         startingElementTypeName,
                                         attachingGUID,
                                         attachingGUIDParameterName,
                                         attachingElementTypeName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         supportedZones,
                                         relationshipTypeGUID,
                                         relationshipTypeName,
                                         this.setUpEffectiveDates(instanceProperties, effectiveFrom, effectiveTo),
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.  If the relationship already exists with matching effectivity dates,
     * the properties are updated.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkElementToElement(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             startingElementGUID,
                                       String             startingGUIDParameterName,
                                       String             startingElementTypeName,
                                       String             attachingElementGUID,
                                       String             attachingGUIDParameterName,
                                       String             attachingElementTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       List<String>       suppliedSupportedZones,
                                       String             attachmentTypeGUID,
                                       String             attachmentTypeName,
                                       InstanceProperties relationshipProperties,
                                       Date               effectiveFrom,
                                       Date               effectiveTo,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingElementGUID, attachingGUIDParameterName, methodName);

        String startingGUID = startingElementGUID;
        String attachingGUID = attachingElementGUID;

        EntityDetail  startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                startingElementGUID,
                                                                                startingGUIDParameterName,
                                                                                startingElementTypeName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        if (startingElementEntity != null)
        {
            /*
             * In case a consolidated entity is returned
             */
            startingGUID = startingElementEntity.getGUID();
        }

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                 attachingGUID,
                                                                                 attachingGUIDParameterName,
                                                                                 attachingElementTypeName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);
        if (attachingElementEntity != null)
        {
            /*
             * In case a consolidated entity is returned
             */
            attachingGUID = attachingElementEntity.getGUID();
        }

        EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                              attachingGUID,
                                                                              attachingElementTypeName,
                                                                              attachingElementEntity,
                                                                              attachingGUIDParameterName,
                                                                              true,
                                                                              false,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              suppliedSupportedZones,
                                                                              effectiveTime,
                                                                              methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         *
         * The next test ensures that the effectivity dates in the new relationship's properties are compatible with the
         * existing relationships.
         */
        List<Relationship> existingRelationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                                     startingElementEntity,
                                                                                                     startingElementEntity.getType().getTypeDefName(),
                                                                                                     attachingGUID,
                                                                                                     attachmentTypeGUID,
                                                                                                     attachmentTypeName,
                                                                                                     2,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveFrom,
                                                                                                     effectiveTo,
                                                                                                     true,
                                                                                                     methodName);

        Relationship newRelationship;
        String actionDescriptionTemplate;
        int    actionOrdinal;

        if (existingRelationships != null)
        {
            if (existingRelationships.size() == 1)
            {
                actionDescriptionTemplate = "Updating link from %s %s to %s %s";
                actionOrdinal = OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL;

                newRelationship = repositoryHandler.updateRelationshipProperties(userId,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 existingRelationships.get(0),
                                                                                 relationshipProperties,
                                                                                 methodName);
            }
            else
            {
                String guids = null;

                for (Relationship relationship : existingRelationships)
                {
                    if (relationship != null)
                    {
                        if (guids == null)
                        {
                            guids = "[";
                        }
                        else
                        {
                            guids = ", ";
                        }

                        guids = guids + relationship.getGUID() + " ";
                    }
                }

                if (guids == null)
                {
                    guids = "[]";
                }
                else
                {
                    guids = guids + "]";
                }

                throw new InvalidParameterException(GenericHandlersErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(attachmentTypeName,
                                                                                                                               startingElementTypeName,
                                                                                                                               startingGUID,
                                                                                                                               attachingElementTypeName,
                                                                                                                               attachingGUID,
                                                                                                                               guids,
                                                                                                                               methodName,
                                                                                                                               serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    attachmentTypeName);
            }
        }
        else
        {
            actionDescriptionTemplate = "Linking %s %s to %s %s";
            actionOrdinal = OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL;

            newRelationship = repositoryHandler.createRelationship(userId,
                                                                   attachmentTypeGUID,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   startingGUID,
                                                                   attachingGUID,
                                                                   relationshipProperties,
                                                                   methodName);
        }

        /*
         * Final stage is to add the latest change classification to the anchor(s).
         * The act of creating the relationship may set up the anchor GUID in either element.
         */
        String startingElementAnchorGUID;

        if (startingElementAnchorEntity == null)
        {
            startingElementAnchorGUID = this.reEvaluateAnchorGUID(startingGUID,
                                                                  startingGUIDParameterName,
                                                                  startingElementTypeName,
                                                                  startingElementEntity,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);

            if (startingElementAnchorGUID != null)
            {
                if ((attachingElementAnchorEntity != null) && (attachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID)))
                {
                    startingElementAnchorEntity = attachingElementAnchorEntity;
                }
                else
                {
                    final String anchorGUIDParameterName = "startingElementAnchorGUID";

                    startingElementAnchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                    startingElementAnchorGUID,
                                                                                    anchorGUIDParameterName,
                                                                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);
                }
            }
            else
            {
                startingElementAnchorGUID = startingGUID;
            }
        }
        else
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        if (attachingElementAnchorEntity == null)
        {
            String attachingElementAnchorGUID = this.reEvaluateAnchorGUID(attachingGUID,
                                                                          attachingGUIDParameterName,
                                                                          attachingElementTypeName,
                                                                          attachingElementEntity,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            if (attachingElementAnchorGUID != null)
            {
                if (attachingElementAnchorGUID.equals(startingElementAnchorGUID))
                {
                    attachingElementAnchorEntity = startingElementAnchorEntity;
                }
                else
                {
                    final String anchorGUIDParameterName = "attachingElementAnchorGUID";

                    attachingElementAnchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                     attachingElementAnchorGUID,
                                                                                     anchorGUIDParameterName,
                                                                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName);
                }
            }
        }


        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 startingGUID,
                                                 attachingElementTypeName,
                                                 attachingGUID);

        if (startingElementAnchorEntity != null)
        {
            this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                         OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                         actionOrdinal,
                                         null,
                                         attachingGUID,
                                         attachingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             actionOrdinal,
                                             null,
                                             attachingGUID,
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (attachingElementAnchorEntity != null)
        {
            /*
             * Only need to add latestChange if the anchor of the attached element is different from the starting element
             */
            if (! attachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
            {
                this.addLatestChangeToAnchor(attachingElementAnchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             actionOrdinal,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else if ((! attachingGUID.equals(startingElementAnchorGUID)) && (attachingElementEntity != null))
        {
            /*
             * The attaching element does not have an anchor and is different from the starting element's anchor
             */
            if (repositoryHelper.isTypeOf(serviceName, attachingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                /*
                 * The attaching element is an anchor in its own right.
                 */
                this.addLatestChangeToAnchor(attachingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             actionOrdinal,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (newRelationship != null)
        {
            return newRelationship.getGUID();
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link. No check is done for the relationship existence before creating it.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String uncheckedLinkElementToElement(String             userId,
                                                String             externalSourceGUID,
                                                String             externalSourceName,
                                                String             startingElementGUID,
                                                String             startingGUIDParameterName,
                                                String             startingElementTypeName,
                                                String             attachingElementGUID,
                                                String             attachingGUIDParameterName,
                                                String             attachingElementTypeName,
                                                boolean            forLineage,
                                                boolean            forDuplicateProcessing,
                                                List<String>       suppliedSupportedZones,
                                                String             attachmentTypeGUID,
                                                String             attachmentTypeName,
                                                InstanceProperties relationshipProperties,
                                                Date               effectiveTime,
                                                String             methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingElementGUID, attachingGUIDParameterName, methodName);

        String startingGUID = startingElementGUID;
        String attachingGUID = attachingElementGUID;

        EntityDetail  startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                startingElementGUID,
                                                                                startingGUIDParameterName,
                                                                                startingElementTypeName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        if (startingElementEntity != null)
        {
            /*
             * In case a consolidated entity is returned
             */
            startingGUID = startingElementEntity.getGUID();
        }

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                 attachingGUID,
                                                                                 attachingGUIDParameterName,
                                                                                 attachingElementTypeName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);
        if (attachingElementEntity != null)
        {
            /*
             * In case a consolidated entity is returned
             */
            attachingGUID = attachingElementEntity.getGUID();
        }

        EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                              attachingGUID,
                                                                              attachingElementTypeName,
                                                                              attachingElementEntity,
                                                                              attachingGUIDParameterName,
                                                                              true,
                                                                              false,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              suppliedSupportedZones,
                                                                              effectiveTime,
                                                                              methodName);

        Relationship newRelationship = repositoryHandler.createRelationship(userId,
                                                                            attachmentTypeGUID,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            startingGUID,
                                                                            attachingGUID,
                                                                            relationshipProperties,
                                                                            methodName);


        /*
         * The act of creating the relationship may set up the anchor GUID in either element.
         */

        if (startingElementAnchorEntity == null)
        {
            this.reEvaluateAnchorGUID(startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      startingElementEntity,
                                      null,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);

        }

        if (attachingElementAnchorEntity == null)
        {
             this.reEvaluateAnchorGUID(attachingGUID,
                                       attachingGUIDParameterName,
                                       attachingElementTypeName,
                                       attachingElementEntity,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);

        }

        if (newRelationship != null)
        {
            return newRelationship.getGUID();
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String multiLinkElementToElement(String             userId,
                                            String             externalSourceGUID,
                                            String             externalSourceName,
                                            String             startingElementGUID,
                                            String             startingGUIDParameterName,
                                            String             startingElementTypeName,
                                            String             attachingElementGUID,
                                            String             attachingGUIDParameterName,
                                            String             attachingElementTypeName,
                                            boolean            forLineage,
                                            boolean            forDuplicateProcessing,
                                            List<String>       suppliedSupportedZones,
                                            String             attachmentTypeGUID,
                                            String             attachmentTypeName,
                                            InstanceProperties relationshipProperties,
                                            Date               effectiveTime,
                                            String             methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return uncheckedLinkElementToElement(userId,
                                             externalSourceGUID,
                                             externalSourceName,
                                             startingElementGUID,
                                             startingGUIDParameterName,
                                             startingElementTypeName,
                                             attachingElementGUID,
                                             attachingGUIDParameterName,
                                             attachingElementTypeName,
                                             forLineage,
                                             forDuplicateProcessing,
                                             suppliedSupportedZones,
                                             attachmentTypeGUID,
                                             attachmentTypeName,
                                             relationshipProperties,
                                             effectiveTime,
                                             methodName);
    }

    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateElementToElementLink(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             startingGUID,
                                           String             startingGUIDParameterName,
                                           String             startingElementTypeName,
                                           String             attachingGUID,
                                           String             attachingGUIDParameterName,
                                           String             attachingElementTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           List<String>       suppliedSupportedZones,
                                           String             attachmentTypeGUID,
                                           String             attachmentTypeName,
                                           boolean            isMergeUpdate,
                                           InstanceProperties relationshipProperties,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingGUID, attachingGUIDParameterName, methodName);

        EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               startingGUID,
                                                                               startingGUIDParameterName,
                                                                               startingElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        String startingElementAnchorGUID = startingGUID;

        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                 attachingGUID,
                                                                                 attachingGUIDParameterName,
                                                                                 attachingElementTypeName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);

        EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                              attachingGUID,
                                                                              attachingElementTypeName,
                                                                              attachingElementEntity,
                                                                              attachingGUIDParameterName,
                                                                              true,
                                                                              false,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              suppliedSupportedZones,
                                                                              effectiveTime,
                                                                              methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         * Next step is to find the relationship to update
         */
        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                     startingGUID,
                                                                                     startingElementTypeName,
                                                                                     attachingGUID,
                                                                                     attachmentTypeGUID,
                                                                                     attachmentTypeName,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName);

        if (relationship != null)
        {
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  relationshipProperties,
                                                                  relationship.getProperties());

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
                                                           methodName);


            /*
             * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
             */
            final String actionDescriptionTemplate = "Updating link from %s %s to %s %s";

            String actionDescription = String.format(actionDescriptionTemplate,
                                                     startingElementTypeName,
                                                     startingGUID,
                                                     attachingElementTypeName,
                                                     attachingGUID);

            if (startingElementAnchorEntity != null)
            {
                this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             attachingGUID,
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                {
                    this.addLatestChangeToAnchor(startingElementEntity,
                                                 OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 attachingGUID,
                                                 attachingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID might be set up
                 */
                this.reEvaluateAnchorGUID(startingGUID,
                                          startingGUIDParameterName,
                                          startingElementTypeName,
                                          startingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }

            if (attachingElementAnchorEntity != null)
            {
                /*
                 * Only need to add latestChange if the anchor of the attached element is different
                 */
                if (! attachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
                {
                    this.addLatestChangeToAnchor(attachingElementAnchorEntity,
                                                 OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                {
                    this.addLatestChangeToAnchor(attachingElementEntity,
                                                 OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID may now be set up
                 */
                this.reEvaluateAnchorGUID(attachingGUID,
                                          attachingGUIDParameterName,
                                          attachingElementTypeName,
                                          attachingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }
        }
    }


    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param relationshipGUID          unique identifier of the relationship
     * @param relationshipGUIDParameterName name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName      name of the relationship type
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateElementToElementLink(String              userId,
                                           String              externalSourceGUID,
                                           String              externalSourceName,
                                           String              relationshipGUID,
                                           String              relationshipGUIDParameterName,
                                           String              relationshipTypeName,
                                           boolean             forLineage,
                                           boolean             forDuplicateProcessing,
                                           boolean             isMergeUpdate,
                                           Date                effectiveFrom,
                                           Date                effectiveTo,
                                           Map<String, Object> relationshipProperties,
                                           Date                effectiveTime,
                                           String              methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        InstanceProperties instanceProperties = null;

        try
        {
            instanceProperties = repositoryHelper.addPropertyMapToInstance(serviceName, null, relationshipProperties, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            final String  propertyName = "relationshipProperties";

            errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
        }

        this.updateElementToElementLink(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        relationshipGUID,
                                        relationshipGUIDParameterName,
                                        relationshipTypeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        isMergeUpdate,
                                        this.setUpEffectiveDates(instanceProperties, effectiveFrom, effectiveTo),
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param relationshipGUID          unique identifier of the relationship
     * @param relationshipGUIDParameterName name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName      name of the relationship type
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateElementToElementLink(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             relationshipGUID,
                                           String             relationshipGUIDParameterName,
                                           String             relationshipTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           List<String>       suppliedSupportedZones,
                                           boolean            isMergeUpdate,
                                           InstanceProperties relationshipProperties,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (relationship != null)
        {
            final String startingGUIDParameterName = "relationship.getEntityOneProxy().getGUID()";
            final String attachingGUIDParameterName = "relationship.getEntityTwoProxy().getGUID()";

            String startingGUID = relationship.getEntityOneProxy().getGUID();
            String attachingGUID = relationship.getEntityTwoProxy().getGUID();

            String startingElementTypeName = relationship.getEntityOneProxy().getType().getTypeDefName();
            String attachingElementTypeName = relationship.getEntityTwoProxy().getType().getTypeDefName();

            EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                   startingGUID,
                                                                                   startingGUIDParameterName,
                                                                                   startingElementTypeName,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   effectiveTime,
                                                                                   methodName);

            EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                                 startingGUID,
                                                                                 startingElementTypeName,
                                                                                 startingElementEntity,
                                                                                 startingGUIDParameterName,
                                                                                 true,
                                                                                 false,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 suppliedSupportedZones,
                                                                                 effectiveTime,
                                                                                 methodName);

            String startingElementAnchorGUID = startingGUID;

            if (startingElementAnchorEntity != null)
            {
                startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
            }

            EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                     attachingGUID,
                                                                                     attachingGUIDParameterName,
                                                                                     attachingElementTypeName,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName);

            EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                                  attachingGUID,
                                                                                  attachingElementTypeName,
                                                                                  attachingElementEntity,
                                                                                  attachingGUIDParameterName,
                                                                                  true,
                                                                                  false,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  suppliedSupportedZones,
                                                                                  effectiveTime,
                                                                                  methodName);

            /*
             * The calls above validate the existence of the two entities and that they are visible to the user.
             * An exception is thrown if there are any problems.
             * The anchor entities are returned if there are anchor entities associated with a specific end.
             * Now ready to update the relationship
             */

            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  relationshipProperties,
                                                                  relationship.getProperties());

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
                                                           methodName);


            /*
             * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
             */
            final String actionDescriptionTemplate = "Updating link from %s %s to %s %s";

            String actionDescription = String.format(actionDescriptionTemplate,
                                                     startingElementTypeName,
                                                     startingGUID,
                                                     attachingElementTypeName,
                                                     attachingGUID);

            if (startingElementAnchorEntity != null)
            {
                this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             attachingGUID,
                                             attachingElementTypeName,
                                             relationshipTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                {
                    this.addLatestChangeToAnchor(startingElementEntity,
                                                 OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 attachingGUID,
                                                 attachingElementTypeName,
                                                 relationshipTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID might be set up
                 */
                this.reEvaluateAnchorGUID(startingGUID,
                                          startingGUIDParameterName,
                                          startingElementTypeName,
                                          startingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }

            if (attachingElementAnchorEntity != null)
            {
                /*
                 * Only need to add latestChange if the anchor of the attached element is different
                 */
                if (! attachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
                {
                    this.addLatestChangeToAnchor(attachingElementAnchorEntity,
                                                 OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 relationshipTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                {
                    this.addLatestChangeToAnchor(attachingElementEntity,
                                                 OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                                 OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 relationshipTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID may now be set up
                 */
                this.reEvaluateAnchorGUID(attachingGUID,
                                          attachingGUIDParameterName,
                                          attachingElementTypeName,
                                          attachingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }
        }
    }


    /**
     * Deleted the existing relationship between the starting element and another element then create a new relationship
     * between the starting element and the new attaching element.
     *
     * If successful this updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the relinking.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param startAtEnd1               is the starting element at end 1 of the relationship
     * @param newAttachingGUID             unique id of the entity for the element that is being attached
     * @param newAttachingGUIDParameterName name of the parameter supplying the newAttachingGUID
     * @param newAttachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid; or the relationship to change is not obvious
     * @throws PropertyServerException there is a problem adding the relationship to the repositories
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void relinkElementToNewElement(String             userId,
                                          String             externalSourceGUID,
                                          String             externalSourceName,
                                          String             startingGUID,
                                          String             startingGUIDParameterName,
                                          String             startingElementTypeName,
                                          boolean            startAtEnd1,
                                          String             newAttachingGUID,
                                          String             newAttachingGUIDParameterName,
                                          String             newAttachingElementTypeName,
                                          boolean            forLineage,
                                          boolean            forDuplicateProcessing,
                                          String             attachmentTypeGUID,
                                          String             attachmentTypeName,
                                          InstanceProperties relationshipProperties,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        relinkElementToNewElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingElementTypeName,
                                  startAtEnd1,
                                  newAttachingGUID,
                                  newAttachingGUIDParameterName,
                                  newAttachingElementTypeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  attachmentTypeGUID,
                                  attachmentTypeName,
                                  relationshipProperties,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Deleted the existing relationship between the starting element and another element then create a new relationship
     * between the starting element and the new attaching element.
     *
     * If successful this updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the relinking.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param startAtEnd1               is the starting element at end 1 of the relationship
     * @param newAttachingGUID             unique id of the entity for the element that is being attached
     * @param newAttachingGUIDParameterName name of the parameter supplying the newAttachingGUID
     * @param newAttachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid; or the relationship to change is not obvious
     * @throws PropertyServerException there is a problem adding the relationship to the repositories
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void relinkElementToNewElement(String             userId,
                                          String             externalSourceGUID,
                                          String             externalSourceName,
                                          String             startingGUID,
                                          String             startingGUIDParameterName,
                                          String             startingElementTypeName,
                                          boolean            startAtEnd1,
                                          String             newAttachingGUID,
                                          String             newAttachingGUIDParameterName,
                                          String             newAttachingElementTypeName,
                                          boolean            forLineage,
                                          boolean            forDuplicateProcessing,
                                          List<String>       suppliedSupportedZones,
                                          String             attachmentTypeGUID,
                                          String             attachmentTypeName,
                                          InstanceProperties relationshipProperties,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(newAttachingGUID, newAttachingGUIDParameterName, methodName);

        EntityDetail  startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                startingGUID,
                                                                                startingGUIDParameterName,
                                                                                startingElementTypeName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        String startingElementAnchorGUID = startingGUID;

        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail  newAttachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                    newAttachingGUID,
                                                                                    newAttachingGUIDParameterName,
                                                                                    newAttachingElementTypeName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

        EntityDetail newAttachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                                 newAttachingGUID,
                                                                                 newAttachingElementTypeName,
                                                                                 newAttachingElementEntity,
                                                                                 newAttachingGUIDParameterName,
                                                                                 true,
                                                                                 false,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 suppliedSupportedZones,
                                                                                 effectiveTime,
                                                                                 methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         *
         * The next step is to remove the relationship if it exists.
         */
        int attachmentEntityEnd = 1;
        if (startAtEnd1)
        {
            attachmentEntityEnd = 2;
        }
        Relationship  relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                   startingGUID,
                                                                                   startingElementTypeName,
                                                                                   attachmentTypeGUID,
                                                                                   attachmentTypeName,
                                                                                   attachmentEntityEnd,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   effectiveTime,
                                                                                   methodName);

        String oldAttachingGUID = "<null>";
        String oldAttachingElementTypeName = "<null";

        if (relationship != null)
        {
            EntityProxy proxy;

            if (startAtEnd1)
            {
                proxy = relationship.getEntityOneProxy();
            }
            else
            {
                proxy = relationship.getEntityTwoProxy();
            }

            if (proxy != null)
            {
                oldAttachingGUID = proxy.getGUID();
                if (proxy.getType() != null)
                {
                    oldAttachingElementTypeName = proxy.getType().getTypeDefName();
                }
            }

            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);
        }


        /*
         * Now add the new relationship.
         */
        if (startAtEnd1)
        {
            repositoryHandler.createRelationship(userId,
                                                 attachmentTypeGUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 startingGUID,
                                                 newAttachingGUID,
                                                 relationshipProperties,
                                                 methodName);
        }
        else
        {
            repositoryHandler.createRelationship(userId,
                                                 attachmentTypeGUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 newAttachingGUID,
                                                 startingGUID,
                                                 relationshipProperties,
                                                 methodName);
        }


        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */
        final String actionDescriptionTemplate = "Relinking %s %s from %s %s to %s %s";

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 startingGUID,
                                                 oldAttachingElementTypeName,
                                                 oldAttachingGUID,
                                                 newAttachingElementTypeName,
                                                 newAttachingGUID);

        if (startingElementAnchorEntity != null)
        {
            this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                         OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                         OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                         null,
                                         newAttachingGUID,
                                         newAttachingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      startingElementEntity,
                                      startingElementAnchorEntity.getGUID(),
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             newAttachingGUID,
                                             newAttachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      startingElementEntity,
                                      null,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }

        if (newAttachingElementAnchorEntity != null)
        {
            /*
             * Only need to add latestChange if the anchor of the attached element is different
             */
            if (! newAttachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
            {
                this.addLatestChangeToAnchor(newAttachingElementAnchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(newAttachingGUID,
                                      newAttachingGUIDParameterName,
                                      newAttachingElementTypeName,
                                      newAttachingElementEntity,
                                      newAttachingElementAnchorEntity.getGUID(),
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, newAttachingElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(newAttachingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(newAttachingGUID,
                                      newAttachingGUIDParameterName,
                                      newAttachingElementTypeName,
                                      newAttachingElementEntity,
                                      null,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeGUID  type identifier of the attaching element's entity
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachingGUID,
                                         String       attachingGUIDParameterName,
                                         String       attachingElementTypeGUID,
                                         String       attachingElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        this.unlinkElementFromElement(userId,
                                      onlyCreatorPermitted,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      attachingGUID,
                                      attachingGUIDParameterName,
                                      attachingElementTypeGUID,
                                      attachingElementTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      attachmentTypeGUID,
                                      attachmentTypeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to remove
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachedGUID,
                                         String       attachedGUIDParameterName,
                                         String       attachedElementTypeGUID,
                                         String       attachedElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachedGUID, attachedGUIDParameterName, methodName);

        Relationship  relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                      startingGUID,
                                                                                      startingElementTypeName,
                                                                                      attachedGUID,
                                                                                      attachmentTypeGUID,
                                                                                      attachmentTypeName,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);

        this.unlinkElementFromElement(userId,
                                      onlyCreatorPermitted,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      attachedGUID,
                                      attachedGUIDParameterName,
                                      attachedElementTypeGUID,
                                      attachedElementTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      suppliedSupportedZones,
                                      attachmentTypeName,
                                      relationship,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param relationship              specific relationship to remove
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachedGUID,
                                         String       attachedGUIDParameterName,
                                         String       attachedElementTypeGUID,
                                         String       attachedElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         String       attachmentTypeName,
                                         Relationship relationship,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        this.unlinkElementFromElement(userId,
                                      onlyCreatorPermitted,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      attachedGUID,
                                      attachedGUIDParameterName,
                                      attachedElementTypeGUID,
                                      attachedElementTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      attachmentTypeName,
                                      relationship,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param relationship              specific relationship to remove
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachedGUID,
                                         String       attachedGUIDParameterName,
                                         String       attachedElementTypeGUID,
                                         String       attachedElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeName,
                                         Relationship relationship,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String relationshipParameterName = "relationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachedGUID, attachedGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(relationship, relationshipParameterName, methodName);

        EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               startingGUID,
                                                                               startingGUIDParameterName,
                                                                               startingElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        String startingElementAnchorGUID = startingGUID;

        String newStartingAnchorGUID = null;
        String newAttachedAnchorGUID = null;


        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail attachedElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               attachedGUID,
                                                                               attachedGUIDParameterName,
                                                                               attachedElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail attachedElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             attachedGUID,
                                                                             attachedElementTypeName,
                                                                             attachedElementEntity,
                                                                             attachedGUIDParameterName,
                                                                             true,
                                                                             false,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             suppliedSupportedZones,
                                                                             effectiveTime,
                                                                             methodName);

        if ((!onlyCreatorPermitted) || (userId.equals(relationship.getCreatedBy())))
        {
            if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
            {
                repositoryHandler.removeRelationship(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     relationship,
                                                     methodName);
            }
        }
        else
        {
            throw new UserNotAuthorizedException(GenericHandlersErrorCode.ONLY_CREATOR_CAN_DELETE.getMessageDefinition(methodName,
                                                                                                                       startingElementTypeName,
                                                                                                                       startingGUID,
                                                                                                                       attachedElementTypeName,
                                                                                                                       attachedGUID,
                                                                                                                       userId),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */
        final String actionDescriptionTemplate = "Unlinking %s %s from %s %s";

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 startingGUID,
                                                 attachedElementTypeName,
                                                 attachedGUID);


        if (startingElementAnchorEntity != null)
        {
            this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                         OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                         OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                         null,
                                         attachedGUID,
                                         attachedElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

            /*
             * Now that this relationship is gone, the anchorGUID may now be wrong
             */
            newStartingAnchorGUID = this.reEvaluateAnchorGUID(startingGUID,
                                                              startingGUIDParameterName,
                                                              startingElementTypeName,
                                                              startingElementAnchorEntity.getGUID(),
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, attachedElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             attachedGUID,
                                             attachedElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (attachedElementAnchorEntity != null)
        {
            /*
             * Only need to add latestChange if the anchor of the attached element is different
             */
            if (! attachedElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
            {
                this.addLatestChangeToAnchor(attachedElementAnchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            /*
             * Now that this relationship is gone, the anchorGUID may now be wrong
             */
            newAttachedAnchorGUID = this.reEvaluateAnchorGUID(attachedGUID,
                                                              attachedGUIDParameterName,
                                                              attachedElementTypeName,
                                                              attachedElementAnchorEntity.getGUID(),
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, attachedElementEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             OpenMetadataAPIMapper.ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }


        /*
         * If the attached element had an anchor before the relationship deletion, but now is without an anchor, then delete the bean.
         */
        if (attachedElementAnchorEntity != null && newAttachedAnchorGUID == null)
        {
            try
            {
                List<String> anchorEntityGUIDs = new ArrayList<>();

                anchorEntityGUIDs.add(attachedElementAnchorEntity.getGUID());

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            attachedGUID,
                                            attachedGUIDParameterName,
                                            attachedElementTypeGUID,
                                            attachedElementTypeName,
                                            null,
                                            null,
                                            anchorEntityGUIDs,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
            catch (InvalidParameterException | PropertyServerException| UserNotAuthorizedException error)
            {
                // This method should succeed, because the relationship has been deleted. Issue an audit log indicating that the bean delete failed
                auditLog.logException(methodName,
                                      GenericHandlersAuditCode.UNABLE_TO_DELETE_UNANCHORED_BEAN.getMessageDefinition(serviceName,
                                                                                                                     attachedGUID,
                                                                                                                     attachedElementTypeName,
                                                                                                                     attachedElementTypeGUID,
                                                                                                                     methodName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                      error);

            }
        }

        /*
         * If the starting element had an anchor before the relationship deletion, but now is without an anchor, then delete the bean.
         */
        if (startingElementAnchorEntity != null && newStartingAnchorGUID == null)
        {
            final String startingElementTypeGUID = repositoryHelper.getTypeDefByName(methodName, startingElementTypeName).getGUID();

            try
            {
                List<String> anchorEntityGUIDs = new ArrayList<>();

                anchorEntityGUIDs.add(startingElementAnchorEntity.getGUID());

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            startingGUID,
                                            startingGUIDParameterName,
                                            startingElementTypeGUID,
                                            startingElementTypeName,
                                            null,
                                            null,
                                            anchorEntityGUIDs,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
            catch (InvalidParameterException | PropertyServerException| UserNotAuthorizedException error)
            {
                // This method should succeed, because the relationship has been deleted. Issue an audit log indicating that the bean delete failed
                auditLog.logException(methodName,
                                      GenericHandlersAuditCode.UNABLE_TO_DELETE_UNANCHORED_BEAN.getMessageDefinition(serviceName,
                                                                                                                     startingGUID,
                                                                                                                     startingElementTypeName,
                                                                                                                     startingElementTypeGUID,
                                                                                                                     methodName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Calls unlinkElementFromElement for all relationships of a certain type emanating from the requested element.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the relationships in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkAllElements(String       userId,
                                  boolean      onlyCreatorPermitted,
                                  String       externalSourceGUID,
                                  String       externalSourceName,
                                  String       startingGUID,
                                  String       startingGUIDParameterName,
                                  String       startingElementTypeName,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  List<String> suppliedSupportedZones,
                                  String       attachmentTypeGUID,
                                  String       attachmentTypeName,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String entityProxyParameterName = "entityProxy.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingElementTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntity,
                                                                                       startingElementTypeName,
                                                                                       attachmentTypeGUID,
                                                                                       attachmentTypeName,
                                                                                       2,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();
            EntityProxy  entityProxy  = repositoryHandler.getOtherEnd(startingEntity.getGUID(), startingElementTypeName, relationship, 2, methodName);

            if ((entityProxy != null) && (entityProxy.getType() != null))
            {
                this.unlinkElementFromElement(userId,
                                              onlyCreatorPermitted,
                                              externalSourceGUID,
                                              externalSourceName,
                                              startingGUID,
                                              startingGUIDParameterName,
                                              startingElementTypeName,
                                              entityProxy.getGUID(),
                                              entityProxyParameterName,
                                              entityProxy.getType().getTypeDefGUID(),
                                              entityProxy.getType().getTypeDefName(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              attachmentTypeGUID,
                                              attachmentTypeName,
                                              effectiveTime,
                                              methodName);
            }
        }
    }



    /**
     * Removes the relationship of a specific type attached to an entity.  If the connected entity is anchored to the starting entity
     * it is deleted (and linked dependent elements). There should be only one relationship. If there are more, an error is thrown.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param detachedElementTypeName   name of type of element that will be detached
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the entity that has been detached
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String unlinkConnectedElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         String       detachedElementTypeName,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String entityProxyParameterName = "entityProxy.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingElementTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                           startingEntity,
                                                           startingGUIDParameterName,
                                                           startingElementTypeName,
                                                           attachmentTypeGUID,
                                                           attachmentTypeName,
                                                           null,
                                                           detachedElementTypeName,
                                                           0,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           suppliedSupportedZones,
                                                           0,
                                                           invalidParameterHandler.getMaxPagingSize(),
                                                           effectiveTime,
                                                           methodName);

        if (relationships == null)
        {
            return null;
        }

        List<Relationship> links;

        /*
         * If this is a relationship that is dedicated to a specific user then the returned links are filtered for the specific user's relationship.
         */
        if (onlyCreatorPermitted)
        {
            links = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (userId.equals(relationship.getCreatedBy()))
                    {
                        links.add(relationship);
                    }
                }
            }

        }
        else
        {
            links = relationships;
        }

        if (links.size() > 1)
        {
            errorHandler.handleAmbiguousRelationships(startingGUID,
                                                      startingElementTypeName,
                                                      attachmentTypeName,
                                                      links,
                                                      methodName);
        }

        EntityProxy  entityProxy  = repositoryHandler.getOtherEnd(startingEntity.getGUID(), startingElementTypeName, links.get(0), 0, methodName);

        String detachedElementGUID = null;

        if ((entityProxy != null) && (entityProxy.getType() != null))
        {
            detachedElementGUID = entityProxy.getGUID();
            this.unlinkElementFromElement(userId,
                                          onlyCreatorPermitted,
                                          externalSourceGUID,
                                          externalSourceName,
                                          startingGUID,
                                          startingGUIDParameterName,
                                          startingElementTypeName,
                                          entityProxy.getGUID(),
                                          entityProxyParameterName,
                                          entityProxy.getType().getTypeDefGUID(),
                                          entityProxy.getType().getTypeDefName(),
                                          forLineage,
                                          forDuplicateProcessing,
                                          suppliedSupportedZones,
                                          attachmentTypeGUID,
                                          attachmentTypeName,
                                          effectiveTime,
                                          methodName);

        }

        return detachedElementGUID;
    }



    /**
     * Set up the effectivity times for an element not using a builder - typically a relationship.
     *
     * @param currentProperties other property values for the element
     * @param effectiveFrom supplied starting time for this element (null for all time)
     * @param effectiveTo supplied ending time for this element
     * @return correctly filled out properties
     */
    InstanceProperties setUpEffectiveDates(InstanceProperties currentProperties,
                                           Date               effectiveFrom,
                                           Date               effectiveTo)
    {
        InstanceProperties properties = currentProperties;

        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            if (properties == null)
            {
                properties = new InstanceProperties();
            }

            properties.setEffectiveFromTime(effectiveFrom);
            properties.setEffectiveToTime(effectiveTo);
        }

        return properties;
    }


    /**
     * Verify that the integrator identities are either null or refer to a valid software capability.
     * These values will be used to set up the
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param forLineage is this request part of a lineage service
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the integrator GUID or name does not match what is in the metadata repository
     * @throws PropertyServerException problem accessing repositories
     * @throws UserNotAuthorizedException security access problem
     */
    public void verifyExternalSourceIdentity(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameterName = "externalSourceGUID";

        if ((externalSourceGUID == null) && (externalSourceName == null))
        {
            return;
        }

        try
        {
            EntityDetail integrator = this.getEntityFromRepository(userId,
                                                                   externalSourceGUID,
                                                                   guidParameterName,
                                                                   OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   effectiveTime,
                                                                   methodName);

            if (integrator == null)
            {
                throw new InvalidParameterException(GenericHandlersErrorCode.INTEGRATOR_NOT_RETURNED.getMessageDefinition(serviceName,
                                                                                                                  methodName,
                                                                                                                  externalSourceGUID,
                                                                                                                  externalSourceName,
                                                                                                                  null),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    externalSourceGUID);
            }
            else
            {
                String qualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                          integrator.getProperties(),
                                                                          methodName);

                if (! externalSourceName.equals(qualifiedName))
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.BAD_INTEGRATOR_NAME.getMessageDefinition(serviceName,
                                                                                                                          methodName,
                                                                                                                          externalSourceName,
                                                                                                                          qualifiedName,
                                                                                                                          externalSourceGUID),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        externalSourceGUID);
                }
            }
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(GenericHandlersErrorCode.INTEGRATOR_NOT_RETURNED.getMessageDefinition(serviceName,
                                                                                                              methodName,
                                                                                                              externalSourceGUID,
                                                                                                              externalSourceName,
                                                                                                              error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                externalSourceGUID);
        }
    }

    /**
     * Test whether an entity is of a particular type or not. Internally it delegates to {@link RepositoryHandler#isEntityATypeOf}
     *
     * @param userId calling user
     * @param guid unique identifier of the entity.
     * @param guidParameterName name of the parameter containing the guid.
     * @param entityTypeName name of the type to test for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public boolean isEntityATypeOf(String  userId,
                                   String  guid,
                                   String  guidParameterName,
                                   String  entityTypeName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, "GUID", methodName);
        invalidParameterHandler.validateObject(entityTypeName, "entityTypeName", methodName);

        EntityDetail entityDetail = this.getEntityFromRepository(userId,
                                                                 guid,
                                                                 guidParameterName,
                                                                 entityTypeName,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        return entityDetail != null && entityDetail.getType().getTypeDefName().equals(entityTypeName);
    }
}
