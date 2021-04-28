/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.*;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.*;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
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

    protected AuditLog                           auditLog;

    private final static String supplementaryPropertiesQualifiedNamePostFix = " Supplementary Properties";
    private final static String supplementaryPropertiesQualifiedNameParameterName = "elementQualifiedName";
    private final static String supplementaryPropertiesGlossaryName = "Supplementary Properties Glossary";
    private final static String supplementaryPropertiesGlossaryParameterName = "supplementaryPropertiesGlossaryName";
    private final static String supplementaryPropertiesGlossaryDescription =
            "This glossary contains glossary terms containing the business-oriented descriptive names and related properties for " +
                    "open metadata assets.";

    private List<String> qualifiedNamePropertyNamesList;


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
     * Set up a new security verifier (the handler runs with a default verifier until this method is called).
     *
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
     * Return the list of supported zones for this asset.  This originates from the configuration of the access server.
     * but may be changed by the security verifier.
     *
     * @param userId calling user
     * @param suppliedSupportedZones supported zones from caller
     * @param serviceName called service
     * @return list of zone names
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     */
    private List<String> getSupportedZones(String       userId,
                                           List<String> suppliedSupportedZones,
                                           String       serviceName) throws InvalidParameterException, PropertyServerException
    {
        return securityVerifier.setSupportedZonesForUser(suppliedSupportedZones, serviceName, userId);
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
     * Return the name of this service.
     *
     * @return string name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    public void setClassificationInRepository(String             userId,
                                              String             beanGUID,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        setClassificationInRepository(userId,
                                      null,
                                      null,
                                      beanGUID,
                                      beanGUIDParameterName,
                                      beanGUIDTypeName,
                                      classificationTypeGUID,
                                      classificationTypeName,
                                      classificationProperties,
                                      false,
                                      methodName);
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
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
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          supportedZones,
                                                          methodName);

        if (beanEntity != null)
        {
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
                                                 beanGUID,
                                                 classificationTypeGUID,
                                                 classificationTypeName,
                                                 ClassificationOrigin.ASSIGNED,
                                                 null,
                                                 classificationProperties,
                                                 methodName);
            }
            else
            {
                latestChangeActionOrdinal = OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL;

                InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                      classificationProperties,
                                                                      existingClassification.getProperties());

                repositoryHandler.reclassifyEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   beanGUID,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   newProperties,
                                                   methodName);
            }

            if (anchorEntity != null)
            {
                final String actionDescriptionTemplate = "Adding %s classification to %s %s";
                String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

                int latestChangeTargetOrdinal;
                String attachmentGUID = null;
                String attachmentTypeName = null;

                if (beanGUID.equals(anchorEntity.getGUID()))
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                }
                else
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                    attachmentGUID = beanGUID;
                    attachmentTypeName = beanGUIDTypeName;
                }

                this.addLatestChangeToAnchor(anchorEntity,
                                             latestChangeTargetOrdinal,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             attachmentGUID,
                                             attachmentTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             methodName);
            }
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    protected void updateClassificationEffectivityDates(String userId,
                                                        String externalSourceGUID,
                                                        String externalSourceName,
                                                        String beanGUID,
                                                        String beanGUIDParameterName,
                                                        String beanGUIDTypeName,
                                                        String classificationTypeGUID,
                                                        String classificationTypeName,
                                                        Date   effectiveFrom,
                                                        Date   effectiveTo,
                                                        String methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          supportedZones,
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

            newProperties.setEffectiveFromTime(effectiveFrom);
            newProperties.setEffectiveToTime(effectiveTo);

            repositoryHandler.reclassifyEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               beanGUID,
                                               classificationTypeGUID,
                                               classificationTypeName,
                                               existingClassification,
                                               newProperties,
                                               methodName);

            if (anchorEntity != null)
            {
                final String actionDescriptionTemplate = "Updating effectivity dates for %s classification to %s %s";
                String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

                int latestChangeTargetOrdinal;
                String attachmentGUID = null;
                String attachmentTypeName = null;

                if (beanGUID.equals(anchorEntity.getGUID()))
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                }
                else
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                    attachmentGUID = beanGUID;
                    attachmentTypeName = beanGUIDTypeName;
                }

                this.addLatestChangeToAnchor(anchorEntity,
                                             latestChangeTargetOrdinal,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             attachmentGUID,
                                             attachmentTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             methodName);
            }
        }
    }



    /**
     * Update the effectivity dates of a specific entity .
     * The effectivity dates control the visibility of the entity through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeGUID type of bean
     * @param beanGUIDTypeName type of bean
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    protected void updateBeanEffectivityDates(String userId,
                                              String externalSourceGUID,
                                              String externalSourceName,
                                              String beanGUID,
                                              String beanGUIDParameterName,
                                              String beanGUIDTypeGUID,
                                              String beanGUIDTypeName,
                                              Date   effectiveFrom,
                                              Date   effectiveTo,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          supportedZones,
                                                          methodName);

        if (beanEntity != null)
        {
            InstanceProperties newProperties = beanEntity.getProperties();

            if (newProperties == null)
            {
                newProperties = new InstanceProperties();
            }

            newProperties.setEffectiveFromTime(effectiveFrom);
            newProperties.setEffectiveToTime(effectiveTo);

            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     beanGUID,
                                                     beanEntity,
                                                     beanGUIDTypeGUID,
                                                     beanGUIDTypeName,
                                                     newProperties,
                                                     methodName);

            if (anchorEntity != null)
            {
                final String actionDescriptionTemplate = "Updating effectivity dates for %s %s";
                String actionDescription = String.format(actionDescriptionTemplate, beanGUIDTypeName, beanGUID);

                int latestChangeTargetOrdinal;
                String attachmentGUID = null;
                String attachmentTypeName = null;

                if (beanGUID.equals(anchorEntity.getGUID()))
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL;
                }
                else
                {
                    latestChangeTargetOrdinal = OpenMetadataAPIMapper.ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL;
                    attachmentGUID = beanGUID;
                    attachmentTypeName = beanGUIDTypeName;
                }

                this.addLatestChangeToAnchor(anchorEntity,
                                             latestChangeTargetOrdinal,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             attachmentGUID,
                                             attachmentTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             methodName);
            }
        }
    }


    /**
     * Update the effectivity dates of a specific relationship.
     * The effectivity dates control the visibility of the relationship through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param relationshipGUID unique identifier of the entity in the repositories
     * @param relationshipGUIDParameterName parameter name that passed the relationshipGUID
     * @param relationshipGUIDTypeName type of relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    protected void updateRelationshipEffectivityDates(String userId,
                                                      String externalSourceGUID,
                                                      String externalSourceName,
                                                      String relationshipGUID,
                                                      String relationshipGUIDParameterName,
                                                      String relationshipGUIDTypeName,
                                                      Date   effectiveFrom,
                                                      Date   effectiveTo,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipGUIDTypeName,
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
                                      supportedZones,
                                      methodName);

            this.validateAnchorEntity(userId,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      entityTwoParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      supportedZones,
                                      methodName);

            InstanceProperties newProperties = relationship.getProperties();

            if (newProperties == null)
            {
                newProperties = new InstanceProperties();
            }

            newProperties.setEffectiveFromTime(effectiveFrom);
            newProperties.setEffectiveToTime(effectiveTo);

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationshipGUID,
                                                           newProperties,
                                                           methodName);

        }
    }


    /**
     * Create the new properties to hand to the repository helper based on the supplied properties,
     * existing properties and whether this is a merge update or not.
     * The effectivity dates are always preserved.  If they need updating then use the separate call.
     *
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing the just the properties with
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
                newProperties.setEffectiveFromTime(existingProperties.getEffectiveFromTime());
                newProperties.setEffectiveToTime(existingProperties.getEffectiveToTime());
            }
        }

        return newProperties;
    }


    /**
     * Update the properties associated with a relationship.  Effectivity dates are unchanged.
     *
     * @param userId caller's userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param relationshipGUID unique identifier of the relationship to update
     * @param relationshipGUIDParameterName  name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName type name of relationship if known (null is ok)
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing the just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties new properties for the relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
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
                                      supportedZones,
                                      methodName);

            this.validateAnchorEntity(userId,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      entityTwoParameterName,
                                      OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                      false,
                                      supportedZones,
                                      methodName);

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
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
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    void removeClassificationFromRepository(String userId,
                                            String beanGUID,
                                            String beanGUIDParameterName,
                                            String beanGUIDTypeName,
                                            String classificationTypeGUID,
                                            String classificationTypeName,
                                            String methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        removeClassificationFromRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           classificationTypeGUID,
                                           classificationTypeName,
                                           methodName);
    }


    /**
     * Remove the requested classification from the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    public void removeClassificationFromRepository(String userId,
                                                   String externalSourceGUID,
                                                   String externalSourceName,
                                                   String beanGUID,
                                                   String beanGUIDParameterName,
                                                   String beanGUIDTypeName,
                                                   String classificationTypeGUID,
                                                   String classificationTypeName,
                                                   String methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           methodName);

        EntityDetail  anchorEntity = validateAnchorEntity(userId,
                                                          beanGUID,
                                                          beanGUIDTypeName,
                                                          beanEntity,
                                                          beanGUIDParameterName,
                                                          true,
                                                          supportedZones,
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
                                                   beanGUID,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   methodName);

                if (anchorEntity != null)
                {
                    final String actionDescriptionTemplate = "Removing %s classification from %s %s";
                    String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

                    int latestChangeTargetOrdinal;
                    String attachmentGUID = null;
                    String attachmentTypeName = null;

                    if (beanGUID.equals(anchorEntity.getGUID()))
                    {
                        latestChangeTargetOrdinal = OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                    }
                    else
                    {
                        latestChangeTargetOrdinal = OpenMetadataAPIMapper.ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;
                        attachmentGUID = beanGUID;
                        attachmentTypeName = beanGUIDTypeName;
                    }

                    this.addLatestChangeToAnchor(anchorEntity,
                                                 latestChangeTargetOrdinal,
                                                 OpenMetadataAPIMapper.DELETED_LATEST_CHANGE_ACTION_ORDINAL,
                                                 classificationTypeName,
                                                 attachmentGUID,
                                                 attachmentTypeName,
                                                 null,
                                                 userId,
                                                 actionDescription,
                                                 methodName);
                }
            }
        }
    }






    /**
     * Fill in information about an asset from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param entity properties to add to the bean
     * @param methodName calling method
     */
    private Asset getAssetBeanFromEntity(EntityDetail entity,
                                         String       methodName)
    {
        if ((entity != null) && (entity.getType() != null))
        {
            Asset assetBean = new Asset();

            String              typeId                    = entity.getType().getTypeDefGUID();
            String              typeName                  = entity.getType().getTypeDefName();
            InstanceStatus      instanceStatus            = entity.getStatus();
            String              assetGUID                 = entity.getGUID();
            InstanceProperties  entityProperties          = entity.getProperties();
            InstanceProperties  securityTagProperties     = null;
            InstanceProperties  confidentialityProperties = null;
            InstanceProperties  confidenceProperties      = null;
            InstanceProperties  criticalityProperties     = null;
            InstanceProperties  impactProperties          = null;
            InstanceProperties  retentionProperties       = null;
            InstanceProperties  ownershipProperties       = null;
            InstanceProperties  zoneProperties            = null;
            InstanceProperties  originProperties          = null;

            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        if (OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            securityTagProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidentialityProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.CONFIDENCE_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidenceProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.CRITICALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            criticalityProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.IMPACT_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            criticalityProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.RETENTION_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            retentionProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            ownershipProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            zoneProperties = classification.getProperties();
                        }
                        else if (OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            originProperties = classification.getProperties();
                        }
                    }
                }
            }

            setupAssetBeanWithEntityProperties(assetBean,
                                               typeId,
                                               typeName,
                                               instanceStatus,
                                               assetGUID,
                                               entityProperties,
                                               securityTagProperties,
                                               confidentialityProperties,
                                               confidenceProperties,
                                               criticalityProperties,
                                               impactProperties,
                                               retentionProperties,
                                               ownershipProperties,
                                               zoneProperties,
                                               originProperties,
                                               methodName);
            return assetBean;
        }

        return null;
    }


    /**
     * Convert an OMRS InstanceStatus enum into a metadata security Referenceable Status enum.
     *
     * @param instanceStatus value from the entity
     * @return mapped enum (default is ReferenceableStatus.UNKNOWN)
     */
    private ReferenceableStatus getReferenceableStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            for (ReferenceableStatus referenceableStatus : ReferenceableStatus.values())
            {
                if (referenceableStatus.getOMRSOrdinal() == instanceStatus.getOrdinal())
                {
                    return referenceableStatus;
                }
            }
        }

        return ReferenceableStatus.UNKNOWN;
    }



    /**
     * Fill in information about an asset from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param assetBean bean to fill out
     * @param typeId unique identifier for the type of the entity
     * @param typeName unique name for the type of the entity
     * @param instanceStatus status from the entity
     * @param assetGUID unique identifier for the entity
     * @param entityProperties properties from the entity
     * @param securityTagProperties properties from the SecurityTags classification
     * @param confidentialityProperties properties from the Confidentiality classification
     * @param confidenceProperties properties from the Confidence classification
     * @param criticalityProperties properties from the Criticality classification
     * @param impactProperties properties from the Impact classification
     * @param retentionProperties properties from the Retention classification
     * @param ownershipProperties properties from the AssetOwnership classification
     * @param zoneProperties properties from the AssetZoneMembership classification
     * @param originProperties properties from the AssetOrigin classification
     * @param methodName calling method
     */
    private void setupAssetBeanWithEntityProperties(Asset              assetBean,
                                                    String             typeId,
                                                    String             typeName,
                                                    InstanceStatus     instanceStatus,
                                                    String             assetGUID,
                                                    InstanceProperties entityProperties,
                                                    InstanceProperties securityTagProperties,
                                                    InstanceProperties confidentialityProperties,
                                                    InstanceProperties confidenceProperties,
                                                    InstanceProperties criticalityProperties,
                                                    InstanceProperties impactProperties,
                                                    InstanceProperties retentionProperties,
                                                    InstanceProperties ownershipProperties,
                                                    InstanceProperties zoneProperties,
                                                    InstanceProperties originProperties,
                                                    String             methodName)
    {
        assetBean.setTypeGUID(typeId);
        assetBean.setTypeName(typeName);
        assetBean.setStatus(this.getReferenceableStatus(instanceStatus));
        assetBean.setGUID(assetGUID);

        InstanceProperties properties = new InstanceProperties(entityProperties);

        assetBean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                         properties,
                                                                         methodName));
        assetBean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                       OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                       properties,
                                                                                       methodName));
        assetBean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                       properties,
                                                                       methodName));
        assetBean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                       properties,
                                                                       methodName));
        assetBean.setOwner(repositoryHelper.removeStringProperty(serviceName,
                                                                 OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                 properties,
                                                                 methodName));
        assetBean.setOwnerType(repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                          OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                          properties,
                                                                          methodName));
        assetBean.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                               OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                               properties,
                                                                               methodName));

        assetBean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(properties));

        if (securityTagProperties != null)
        {
            assetBean.setSecurityLabels(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                OpenMetadataAPIMapper.SECURITY_LABELS_PROPERTY_NAME,
                                                                                securityTagProperties,
                                                                                methodName));
            assetBean.setSecurityProperties(repositoryHelper.getMapFromProperty(serviceName,
                                                                                OpenMetadataAPIMapper.SECURITY_PROPERTIES_PROPERTY_NAME,
                                                                                securityTagProperties,
                                                                                methodName));
        }

        if (confidentialityProperties != null)
        {
            ConfidentialityGovernanceClassification classification = new ConfidentialityGovernanceClassification();

            classification.setStatus(this.getGovernanceClassificationStatus(confidentialityProperties, methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidentialityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidentialityProperties,
                                                                       methodName));
            classification.setConfidentialityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                                   OpenMetadataAPIMapper.CONFIDENTIALITY_LEVEL_PROPERTY_NAME,
                                                                                   confidentialityProperties,
                                                                                   methodName));
        }

        if (confidenceProperties != null)
        {
            ConfidenceGovernanceClassification classification = new ConfidenceGovernanceClassification();

            classification.setStatus(this.getGovernanceClassificationStatus(confidenceProperties, methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidenceProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidenceProperties,
                                                                       methodName));
            classification.setConfidenceLevel(repositoryHelper.getIntProperty(serviceName,
                                                                              OpenMetadataAPIMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
                                                                              confidenceProperties,
                                                                              methodName));
        }

        if (criticalityProperties != null)
        {
            CriticalityGovernanceClassification classification = new CriticalityGovernanceClassification();

            classification.setStatus(this.getGovernanceClassificationStatus(criticalityProperties, methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        criticalityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       criticalityProperties,
                                                                       methodName));
            classification.setCriticalityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                               OpenMetadataAPIMapper.CRITICALITY_LEVEL_PROPERTY_NAME,
                                                                               criticalityProperties,
                                                                               methodName));
        }

        if (impactProperties != null)
        {
            ImpactGovernanceClassification classification = new ImpactGovernanceClassification();

            classification.setStatus(this.getGovernanceClassificationStatus(impactProperties, methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        impactProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       impactProperties,
                                                                       methodName));
            classification.setImpactLevel(repositoryHelper.getIntProperty(serviceName,
                                                                          OpenMetadataAPIMapper.IMPACT_CLASSIFICATION_TYPE_NAME,
                                                                          impactProperties,
                                                                          methodName));
        }

        if (retentionProperties != null)
        {
            RetentionGovernanceClassification classification = new RetentionGovernanceClassification();

            classification.setStatus(this.getGovernanceClassificationStatus(confidenceProperties, methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        retentionProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       retentionProperties,
                                                                       methodName));
            classification.setRetentionBasis(repositoryHelper.getIntProperty(serviceName,
                                                                             OpenMetadataAPIMapper.RETENTION_BASIS_PROPERTY_NAME,
                                                                             retentionProperties,
                                                                             methodName));
            classification.setAssociatedGUID(repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.RETENTION_ASSOCIATED_GUID_PROPERTY_NAME,
                                                                                retentionProperties,
                                                                                methodName));
            classification.setArchiveAfter(repositoryHelper.getDateProperty(serviceName,
                                                                            OpenMetadataAPIMapper.RETENTION_ARCHIVE_AFTER_PROPERTY_NAME,
                                                                            retentionProperties,
                                                                            methodName));
            classification.setDeleteAfter(repositoryHelper.getDateProperty(serviceName,
                                                                           OpenMetadataAPIMapper.RETENTION_DELETE_AFTER_PROPERTY_NAME,
                                                                           retentionProperties,
                                                                           methodName));
        }

        if (ownershipProperties != null)
        {
            assetBean.setOwner(repositoryHelper.getStringProperty(serviceName,
                                                                  OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                  ownershipProperties,
                                                                  methodName));
            assetBean.setOwnerType(repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                           OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                           ownershipProperties,
                                                                           methodName));
        }

        if (zoneProperties != null)
        {
            assetBean.setZoneMembership(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                zoneProperties,
                                                                                methodName));
        }

        if (originProperties != null)
        {
            Map<String, String> origins = new HashMap<>();
            String               propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                                                    OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                                    originProperties,
                                                                                    methodName);

            if (propertyValue != null)
            {
                origins.put(OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME, propertyValue);
            }

            propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                               OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                               originProperties,
                                                               methodName);

            if (propertyValue != null)
            {
                origins.put(OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME, propertyValue);
            }

            Map<String, String> propertyMap = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                        OpenMetadataAPIMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                                        originProperties,
                                                                                        methodName);

            if (propertyMap != null)
            {
                for (String propertyName : propertyMap.keySet())
                {
                    if (propertyName != null)
                    {
                        origins.put(propertyName, propertyMap.get(propertyName));
                    }
                }
            }

            if (! origins.isEmpty())
            {
                assetBean.setOrigin(origins);
            }
        }
    }


    /**
     * Return the enum value that matches the ordinal from the classification properties.  If the ordinal is not recognized,
     * the enum returned is null.
     *
     * @param governanceClassificationProperties properties from classification (not null)
     * @param methodName calling methodName
     * @return GovernanceClassificationStatus enum
     */
    private GovernanceClassificationStatus getGovernanceClassificationStatus(InstanceProperties governanceClassificationProperties,
                                                                             String             methodName)
    {
        int enumOrdinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_CLASSIFICATION_STATUS_PROPERTY_NAME,
                                                                  governanceClassificationProperties,
                                                                  methodName);

        if (enumOrdinal >= 0)
        {
            GovernanceClassificationStatus[] enums = GovernanceClassificationStatus.values();

            for (GovernanceClassificationStatus status : enums)
            {
                if (status.getOpenTypeOrdinal() == enumOrdinal)
                {
                    return status;
                }
            }
        }

        return null;
    }


    /**
     * Validate that the user is able to perform the requested action on an attachment.  This method should be used by the other
     * handlers to verify whether or not the element they are working with is attached to a visible asset
     * (ie is a member of one of the supported zones) that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying the assetGUID
     * @param assetEntity entity storing the the asset's properties
     * @param suppliedSupportedZones list of supported zones from the caller.
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void validateUserForAssetRead(String       userId,
                                          String       assetGUID,
                                          String       assetGUIDParameterName,
                                          EntityDetail assetEntity,
                                          List<String> suppliedSupportedZones,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        /*
         * This method will throw an exception if the asset is not in the supported zones - it will look like
         * the asset is not known.
         */
        invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                             assetGUIDParameterName,
                                                             suppliedSupportedZones,
                                                             this.getSupportedZones(userId, suppliedSupportedZones, serviceName),
                                                             serviceName,
                                                             methodName);

        /*
         * Create the bean for the security module then call the appropriate security method.
         */
        Asset assetBean = this.getAssetBeanFromEntity(assetEntity, methodName);

        securityVerifier.validateUserForAssetRead(userId, assetBean);
    }


    /**
     * Validate that the user is able to perform the requested action on an attachment.  This method should be used by the other
     * handlers to verify whether or not the element they are working with is attached to a visible asset
     * (ie is a member of one of the supported zones) that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param originalAssetEntity entity storing the current asset
     * @param updatedAssetProperties  properties after the update has completed
     * @param newInstanceStatus status of the entity once the update is complete
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    private void validateUserForAssetUpdate(String             userId,
                                            EntityDetail       originalAssetEntity,
                                            InstanceProperties updatedAssetProperties,
                                            InstanceStatus     newInstanceStatus,
                                            String             methodName) throws UserNotAuthorizedException
    {
        /*
         * Creates the beans for the security module then calls to appropriate security method.
         */
        Asset originalAsset = this.getAssetBeanFromEntity(originalAssetEntity, methodName);

        AssetAuditHeader assetAuditHeader = new AssetAuditHeader();
        assetAuditHeader.setCreatedBy(originalAssetEntity.getCreatedBy());
        assetAuditHeader.setCreateTime(originalAssetEntity.getCreateTime());
        assetAuditHeader.setMaintainedBy(originalAssetEntity.getMaintainedBy());
        assetAuditHeader.setUpdatedBy(originalAssetEntity.getUpdatedBy());
        assetAuditHeader.setUpdateTime(originalAssetEntity.getUpdateTime());
        assetAuditHeader.setVersion(assetAuditHeader.getVersion());

        EntityDetail updatedAssetEntity = new EntityDetail(originalAssetEntity);
        updatedAssetEntity.setProperties(updatedAssetProperties);
        updatedAssetEntity.setStatus(newInstanceStatus);
        Asset updatedAsset = this.getAssetBeanFromEntity(updatedAssetEntity, methodName);

        securityVerifier.validateUserForAssetDetailUpdate(userId, originalAsset, assetAuditHeader, updatedAsset);
    }


    /**
     * Validate that the user is able to perform the requested action on an attachment.  This method should be used by the other
     * handlers to verify whether or not the element they are working with is attached to a visible asset
     * (ie is a member of one of the supported zones) that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying the assetGUID
     * @param assetEntity entity storing the root of the  asset
     * @param isFeedback       is this request related to a feedback element (comment, like, rating) or an attachment
     * @param isUpdate         is this an update request?
     * @param suppliedSupportedZones list of supported zones from the caller.
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void validateUserForAssetAttachment(String       userId,
                                                String       assetGUID,
                                                String       assetGUIDParameterName,
                                                EntityDetail assetEntity,
                                                boolean      isFeedback,
                                                boolean      isUpdate,
                                                List<String> suppliedSupportedZones,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        /*
         * This method will throw an exception if the asset is not in the supported zones - it will look like
         * the asset is not known.
         */
        invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                             assetGUIDParameterName,
                                                             suppliedSupportedZones,
                                                             this.getSupportedZones(userId,
                                                                                    suppliedSupportedZones,
                                                                                    serviceName),
                                                             serviceName,
                                                             methodName);

        /*
         * Creates the entity and checks that it is in at least one of the supporting zones.
         */
        Asset asset = this.getAssetBeanFromEntity(assetEntity, methodName);

        /*
         * Now validate the security.
         */
        if (isUpdate)
        {
            if (isFeedback)
            {
                securityVerifier.validateUserForAssetFeedback(userId, asset);
            }
            else
            {
                securityVerifier.validateUserForAssetAttachmentUpdate(userId, asset);
            }
        }
        else
        {
            securityVerifier.validateUserForAssetRead(userId, asset);
        }
    }


    /**
     * Fill in information about an connection from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param entity properties fill out
     * @param methodName calling method
     */
    Connection getConnectionFromEntity(EntityDetail       entity,
                                       String             methodName)
    {
        if ((entity != null) && (entity.getType() != null))
        {
            Connection connectionBean = new Connection();

            connectionBean.setTypeGUID(entity.getType().getTypeDefGUID());
            connectionBean.setTypeName(entity.getType().getTypeDefName());

            connectionBean.setGUID(entity.getGUID());

            InstanceProperties properties = new InstanceProperties(entity.getProperties());

            connectionBean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                                  OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                  properties,
                                                                                  methodName));

            connectionBean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                                properties,
                                                                                methodName));
            connectionBean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                properties,
                                                                                methodName));

            connectionBean.setUserId(repositoryHelper.removeStringProperty(serviceName,
                                                                           OpenMetadataAPIMapper.USER_ID_PROPERTY_NAME,
                                                                           properties,
                                                                           methodName));

            connectionBean.setClearPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                                  OpenMetadataAPIMapper.CLEAR_PASSWORD_PROPERTY_NAME,
                                                                                  properties,
                                                                                  methodName));

            connectionBean.setEncryptedPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                                      OpenMetadataAPIMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME,
                                                                                      properties,
                                                                                      methodName));

            connectionBean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                                OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                                properties,
                                                                                                methodName));

            connectionBean.setConfigurationProperties(repositoryHelper.removeMapFromProperty(serviceName,
                                                                                             OpenMetadataAPIMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                                             properties,
                                                                                             methodName));

            connectionBean.setSecuredProperties(repositoryHelper.removeMapFromProperty(serviceName,
                                                                                       OpenMetadataAPIMapper.SECURED_PROPERTIES_PROPERTY_NAME,
                                                                                       properties,
                                                                                       methodName));

            connectionBean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(properties));

            return connectionBean;
        }

        return null;
    }


    /**
     * Validate that the user is able to retrieve the requested connection.
     *
     * @param userId calling user
     * @param entity entity storing the connection's properties
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to access this connection
     */
    private void validateUserForConnection(String       userId,
                                           EntityDetail entity,
                                           String       methodName) throws UserNotAuthorizedException
    {
        Connection connectionBean = this.getConnectionFromEntity(entity, methodName);

        securityVerifier.validateUserForConnection(userId, connectionBean);
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
    String getAnchorGUIDFromAnchorsClassification(EntityDetail connectToEntity,
                                                  String       methodName)
    {
        /*
         * Metadata maintained by Egeria Access Service modules should have the Anchors classification.
         */
        Classification anchorsClassification;
        String         anchorGUID = null;

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
                    anchorGUID = repositoryHelper.getStringProperty(serviceName,
                                                                    OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                    anchorsClassification.getProperties(),
                                                                    methodName);

                    /*
                     * This is an attempt to trap an intermittent error recorded in issue #4680.
                     */
                    if ("<unknown>".equals(anchorGUID))
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

        return anchorGUID;
    }


    /**
     * Set up the anchors classification in an entity (and any child anchored entity connected to it).  This is done using the local server's user Id
     * and assumes these classifications are maintained in the local cohort.
     *
     * @param targetGUID unique identifier for the entity
     * @param targetEntity contents of the entity from the repository
     * @param anchorGUID unique identifier of the anchor
     * @param methodName calling method
     *
     * @throws InvalidParameterException problem with one of the properties
     * @throws PropertyServerException problem within the repository services
     * @throws UserNotAuthorizedException local server user not allowed to issue the request.
     */
    private void maintainAnchorGUIDInClassification(String        targetGUID,
                                                    EntitySummary targetEntity,
                                                    String        anchorGUID,
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
         * It is necessary to retrieve any existing classification to know whether it is a classify or reclassify method required.
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

                if (anchorsClassification == null)
                {
                    if (anchorGUID != null)
                    {
                        repositoryHandler.classifyEntity(localServerUserId,
                                                         null,
                                                         null,
                                                         targetGUID,
                                                         OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                         OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                         ClassificationOrigin.ASSIGNED,
                                                         null,
                                                         anchorsProperties,
                                                         methodName);
                    }
                }
                else
                {
                    if (anchorGUID != null)
                    {
                        repositoryHandler.reclassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           targetGUID,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                           anchorsClassification,
                                                           anchorsProperties,
                                                           methodName);
                    }
                    else
                    {
                        repositoryHandler.declassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           targetGUID,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_GUID,
                                                           OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                           anchorsClassification,
                                                           methodName);
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

            /*
             * Now need to ensure that the anchor's classification is pushed down to the dependent elements.  This is done by retrieving the
             * relationships.
             */
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           localServerUserId,
                                                                                           targetGUID,
                                                                                           targetEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                /*
                 * This if statement should always be true.
                 */
                if ((relationship != null) && (relationship.getType() != null) &&
                    (relationship.getEntityOneProxy() != null) && (relationship.getEntityTwoProxy() != null))
                {
                    String entityOneGUID = relationship.getEntityOneProxy().getGUID();
                    String entityTwoGUID = relationship.getEntityTwoProxy().getGUID();

                    switch (relationship.getType().getTypeDefGUID())
                    {
                        /*
                         * These relationships all have the parent at end one.  We then need to push the anchor guid down to the entity at end two.
                         */
                        case OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID:
                        case OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_GUID:
                        case OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID:
                        case OpenMetadataAPIMapper.PERSONAL_CONTRIBUTION_RELATIONSHIP_TYPE_GUID:
                            if (entityOneGUID.equals(targetGUID))
                            {
                                this.maintainAnchorGUIDInClassification(entityTwoGUID, relationship.getEntityTwoProxy(), anchorGUID, methodName);
                            }
                            break;

                        /*
                         * These relationships point to entities of types that may or may not be anchored to the parent.
                         * It is necessary to retrieve the entity and only update its Anchors classification if it is anchored.
                         * It is only allowed to be connected to one entity if it is anchored.  If it could be anchored to one entity and then
                         * connected to another then this logic does not work.
                         */
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_COLLECTION_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_REFERENCE_VALUE_TYPE_GUID:
                        case OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID:
                            if (entityOneGUID.equals(targetGUID))
                            {
                                final String proxyTwoGUIDParameterName = "proxyTwoGUID";
                                EntityDetail proxyTwoAnchor = this.validateAnchorEntity(localServerUserId,
                                                                                        entityTwoGUID,
                                                                                        proxyTwoGUIDParameterName,
                                                                                        relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                        true,
                                                                                        null,
                                                                                        methodName);
                                if (proxyTwoAnchor != null)
                                {
                                    this.maintainAnchorGUIDInClassification(entityTwoGUID, relationship.getEntityTwoProxy(), anchorGUID, methodName);
                                }
                            }

                            break;
                    }
                }
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws InvalidParameterException  the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException  there is a problem retrieving the instances from the property server or
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForSchemaType(String userId,
                                              String schemaTypeGUID,
                                              String methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * The most obvious test is that this schema type is attached directly to the asset.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  schemaTypeGUID,
                                                                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return proxy.getGUID();
        }

        /*
         * Next test to see if the type is connected to an attribute.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaAttribute(userId, proxy.getGUID(), methodName);
        }

        /*
         * Next test to see if the type is connected to a SchemaTypeChoice.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Next test to see if the type is connected to a MapSchemaType.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.MAP_FROM_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.MAP_TO_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Next test to see if the type is connected to an API operation or API schema type.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_HEADER_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_REQUEST_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.API_RESPONSE_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
        }

        /*
         * Finally test that this schema type is attached directly to a port.
         */
        relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                     schemaTypeGUID,
                                                                     OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                     OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                                     OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                                     methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return proxy.getGUID();
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForSchemaAttribute(String userId,
                                                   String attributeGUID,
                                                   String methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        /*
         * Is the schema attribute connected to a type.
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  attributeGUID,
                                                                                  OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                                  methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaType(userId, proxy.getGUID(), methodName);
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
                                                                           methodName);
        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();

            return getAnchorGUIDForSchemaAttribute(userId, proxy.getGUID(), methodName);
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForConnection(String userId,
                                              String connectionGUID,
                                              String methodName) throws PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * Is the connection connected to an asset?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  connectionGUID,
                                                                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return proxy.getGUID();
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Like.  Likes are attached directly to a referenceable.
     *
     * @param userId calling user
     * @param likeGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForLike(String userId,
                                        String likeGUID,
                                        String methodName) throws PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        /*
         * Is the like connected to a Referenceable?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  likeGUID,
                                                                                  OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return proxy.getGUID();
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Rating.  Ratings are attached directly to a referenceable.
     *
     * @param userId calling user
     * @param ratingGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForRating(String userId,
                                          String ratingGUID,
                                          String methodName) throws PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        /*
         * Is the rating connected to a Referenceable?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  ratingGUID,
                                                                                  OpenMetadataAPIMapper.RATING_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return proxy.getGUID();
            }
        }

        return null;
    }


    /**
     * Walk the graph to locate the anchor for a Rating.  Ratings are attached directly to a referenceable.
     *
     * @param userId calling user
     * @param ratingGUID unique identifier of the connection (it is assumed that the anchorGUID property of this instance is null)
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForOpenDiscoveryAnalysisReport(String userId,
                                                               String ratingGUID,
                                                               String methodName) throws PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        /*
         * Is the report connected to an Asset?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  ratingGUID,
                                                                                  OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return proxy.getGUID();
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForAnnotation(String userId,
                                              String annotationGUID,
                                              String methodName) throws PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * Is the comment connected to anything?
         */
        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    annotationGUID,
                                                                                    OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME,
                                                                                    null,
                                                                                    null,
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
                            return this.getAnchorGUIDForOpenDiscoveryAnalysisReport(userId, proxy.getGUID(), methodName);
                        }
                        else if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME))
                        {
                            String parentAnchorGUID = this.getAnchorGUIDForDataField(userId, proxy.getGUID(), methodName);

                            if (parentAnchorGUID != null)
                            {
                                return parentAnchorGUID;
                            }
                        }
                        else if (repositoryHelper.isTypeOf(serviceName, proxy.getType().getTypeDefName(), OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME))
                        {
                            return this.getAnchorGUIDForAnnotation(userId, proxy.getGUID(), methodName);
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForAnnotationReview(String userId,
                                                    String annotationReviewGUID,
                                                    String methodName) throws PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        /*
         * Is the annotation review connected to an annotation?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  annotationReviewGUID,
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_LINK_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return this.getAnchorGUIDForAnnotation(userId, proxy.getGUID(), methodName);
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
     * @param methodName calling method
     *
     * @return unique identifier of attached asset or null if there is no attached asset
     *
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the repositories or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    private String getAnchorGUIDForDataField(String userId,
                                             String dataFieldGUID,
                                             String methodName) throws PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        /*
         * Is the data field connected to an annotation?
         */
        Relationship relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                  dataFieldGUID,
                                                                                  OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME,
                                                                                  OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_GUID,
                                                                                  OpenMetadataAPIMapper.DISCOVERED_DATA_FIELD_TYPE_NAME,
                                                                                  methodName);

        if (relationship != null)
        {
            EntityProxy proxy = relationship.getEntityOneProxy();
            if ((proxy != null) && (proxy.getGUID() != null))
            {
                return this.getAnchorGUIDForAnnotation(userId, proxy.getGUID(), methodName);
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
     * @param methodName calling method
     *
     * @return unique identifier of attached anchor or null if there is no attached anchor
     *
     * @throws PropertyServerException  there is a problem retrieving the  properties from the repositories
     * @throws UserNotAuthorizedException  the requesting user is not authorized to issue this request
     */
    private String getAnchorGUIDForComment(String userId,
                                           String commentGUID,
                                           String methodName) throws PropertyServerException,
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
                            String parentAnchorGUID = this.getAnchorGUIDForComment(userId, proxy.getGUID(), methodName);

                            /*
                             * If the parent has no anchor then it is a detached chain of comments and the parent is the anchor.
                             */
                            if (parentAnchorGUID != null)
                            {
                                return parentAnchorGUID;
                            }
                        }

                        return proxy.getGUID();
                    }
                }
            }
        }

        return null;
    }


    /**
     * This method walks the relationships to determine if the entity identified by the targetGUID has an anchor.  It returns the GUID of
     * this anchor if it exists or null if it does not.  This method is used both as part of setting up the anchorGUID in a newly linked entity,
     * or to verify that the existing anchorGUID value is still valid.
     *
     * @param targetGUID unique identifier for the entity to test
     * @param targetTypeName type of the entity to test
     * @param methodName calling method
     * @return anchorGUID or null
     * @throws InvalidParameterException on of the properties is invalid which is suspicious since they have been validated earlier
     * @throws UserNotAuthorizedException the local server userId does not have access to the repository instances
     * @throws PropertyServerException something is wrong with the repository
     */
    private String deriveAnchorGUID(String targetGUID,
                                    String targetTypeName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        String anchorGUID = null;

        /*
         * This group of calls walks the chain of entities to detect the anchorGUID for specific types of entities.  There is scope for more
         * method calls added here, for example, for comments, note logs, connections etc.
         */
        if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForSchemaType(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForSchemaAttribute(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.CONNECTION_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForConnection(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.COMMENT_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForComment(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.RATING_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForRating(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.LIKE_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForLike(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForOpenDiscoveryAnalysisReport(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.ANNOTATION_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForAnnotation(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.ANNOTATION_REVIEW_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForAnnotationReview(localServerUserId, targetGUID, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, targetTypeName, OpenMetadataAPIMapper.DATA_FIELD_TYPE_NAME))
        {
            anchorGUID = this.getAnchorGUIDForDataField(localServerUserId, targetGUID, methodName);
        }

        return anchorGUID;
    }


    /**
     * Validates that the current anchorGUID is correct and updates it if it is not.
     *
     * @param targetGUID unique identifier of the element to validate
     * @param targetGUIDParameterName parameter that provided the guid
     * @param targetTypeName type of entity to validate
     * @param originalAnchorGUID the original anchor guid - may be null
     * @param methodName calling method
     *
     * @throws InvalidParameterException probably the type of the entity is not correct
     * @throws PropertyServerException there is a problem with the repository
     * @throws UserNotAuthorizedException the local server user id is not able to update the entity
     */
    private void reEvaluateAnchorGUID(String targetGUID,
                                      String targetGUIDParameterName,
                                      String targetTypeName,
                                      String originalAnchorGUID,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        /*
         * Find out the anchorGUID by following the relationships
         */
        String newAnchorGUID = this.deriveAnchorGUID(targetGUID, targetTypeName, methodName);

        /*
         * The anchorGUID has changed
         */
        if (((newAnchorGUID == null) && (originalAnchorGUID != null)) ||
            ((newAnchorGUID != null) && (! newAnchorGUID.equals(originalAnchorGUID))))
        {
            EntityDetail targetElement = repositoryHandler.getEntityByGUID(localServerUserId,
                                                                           targetGUID,
                                                                           targetGUIDParameterName,
                                                                           targetTypeName,
                                                                           methodName);

            if (targetElement != null)
            {
                this.maintainAnchorGUIDInClassification(targetGUID, targetElement, newAnchorGUID, methodName);
            }
        }
    }



    /**
     * Validates that the current anchorGUID is correct and updates it if it is not.
     *
     * @param targetGUID unique identifier of the element to validate
     * @param targetElement target entity already retrieved
     * @param targetTypeName type of entity to validate
     * @param originalAnchorGUID the original anchor guid - may be null
     * @param methodName calling method
     *
     * @return the new anchor GUID
     *
     * @throws InvalidParameterException probably the type of the entity is not correct
     * @throws PropertyServerException there is a problem with the repository
     * @throws UserNotAuthorizedException the local server user id is not able to update the entity
     */
    private String reEvaluateAnchorGUID(String       targetGUID,
                                        String       targetTypeName,
                                        EntityDetail targetElement,
                                        String       originalAnchorGUID,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * Find out the anchorGUID by following the relationships
         */
        String newAnchorGUID = this.deriveAnchorGUID(targetGUID, targetTypeName, methodName);

        /*
         * The anchorGUID has changed
         */
        if (((newAnchorGUID == null) && (originalAnchorGUID != null)) ||
            ((newAnchorGUID != null) && (! newAnchorGUID.equals(originalAnchorGUID))))
        {
            if (targetElement != null)
            {
                this.maintainAnchorGUIDInClassification(targetGUID, targetElement, newAnchorGUID, methodName);
            }
        }

        return newAnchorGUID;
    }


    /**
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an update.
     *
     * @param userId           userId of user making request.
     * @param connectToGUID       unique id for the object to connect the attachment to.
     * @param connectToGUIDParameterName  name of the parameter that passed the connect to guid
     * @param connectToType       type of the connect to element.
     * @param isUpdate         is this an update request?
     * @param suppliedSupportedZones supported zone list from calling service
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
                                             boolean      isUpdate,
                                             List<String> suppliedSupportedZones,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectToGUID, connectToGUIDParameterName, methodName);

        /*
         * This returns the entity for the connect to element and validates it is of the correct type.
         */
        EntityDetail  connectToEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          connectToGUID,
                                                                          connectToGUIDParameterName,
                                                                          connectToType,
                                                                          methodName);

        return this.validateAnchorEntity(userId,
                                         connectToGUID,
                                         connectToType,
                                         connectToEntity,
                                         connectToGUIDParameterName,
                                         isUpdate,
                                         suppliedSupportedZones,
                                         methodName);
    }


    /**
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an
     * update.
     *
     * The first part of this method is looking to see if the connectToEntity is an anchor entity. In which case it calls any specific validation
     * for that entity and returns the connectToEntity, assuming all is ok - exceptions are thrown if the entity is not valid or the user does not
     * have access to it.
     *
     * If the connectToEntity is of a type that has a lifecycle that is linked to the lifecycle of another entity - typically a referenceable -
     * then that other entity is its anchor (examples are schema elements, comments, connections).  The anchor entity needs to be retrieved and
     * validated.
     *
     * Some anchor entities have specific validation to perform.
     *
     * @param userId           userId of user making request.
     * @param connectToGUID    unique id for the object to connect the attachment to
     * @param connectToType    name of type of connectToEntity
     * @param connectToEntity  entity retrieved from the repository
     * @param connectToGUIDParameterName  name of the parameter that passed the connect to guid
     * @param isUpdate         is this an update request?
     * @param suppliedSupportedZones list of supported zones from the caller.
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
                                             boolean       isUpdate,
                                             List<String>  suppliedSupportedZones,
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
            this.validateUserForConnection(userId, connectToEntity, methodName);
        }
        else if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            /*
             * Even if the request is an update request, the security module is first called for read - the update
             * is validated once the properties have been updated.
             */
            this.validateUserForAssetRead(userId,
                                          connectToGUID,
                                          connectToGUIDParameterName,
                                          connectToEntity,
                                          suppliedSupportedZones,
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
        String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(connectToEntity, methodName);

        if (anchorGUID == null)
        {
            /*
             * The classification is missing - so walk the relationships to find the anchor if it exists.
             */
            anchorGUID = deriveAnchorGUID(connectToGUID, connectToEntity.getType().getTypeDefName(), methodName);

            if (anchorGUID != null)
            {
                /*
                 * The anchor has been found so store it in the classification so it is easy to find next time.
                 */
                maintainAnchorGUIDInClassification(connectToGUID, connectToEntity, anchorGUID, methodName);
            }
        }

        /*
         * If an anchor GUID has been found then validate it by retrieving the identified entity.  Note - anchorGUID may be null if the connectToEntity
         * is actually an anchor.
         */
        if (anchorGUID != null)
        {
            final String anchorGUIDParameterName = "anchorGUID";

            if (! anchorGUID.equals(connectToEntity.getGUID()))
            {
                anchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                 anchorGUID,
                                                                 anchorGUIDParameterName,
                                                                 OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
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
                /*
                 * Determine if the element is attached directly or indirectly to an asset (or is an asset) so it is possible to determine
                 * if this asset is in a supported zone or if the user is allowed to change its attachments.
                 */
                if (OpenMetadataAPIMapper.ASSET_TYPE_NAME.equals(anchorEntityType.getTypeDefName()))
                {
                    boolean isFeedbackEntity = false;

                    if ((repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME)) ||
                        (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.COMMENT_TYPE_NAME)) ||
                        (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.RATING_TYPE_NAME)) ||
                        (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataAPIMapper.LIKE_TYPE_NAME)))
                    {
                        isFeedbackEntity = true;
                    }

                    this.validateUserForAssetAttachment(userId,
                                                        connectToGUID,
                                                        connectToGUIDParameterName,
                                                        anchorEntity,
                                                        isFeedbackEntity,
                                                        isUpdate,
                                                        suppliedSupportedZones,
                                                        methodName);
                }

                /*
                 * This list is likely to expand as more anchor types get specialized visibility/security mechanisms.
                 */
            }
        }

        return anchorEntity;
    }


    /**
     * Validate that the user has permission to create a new entity
     *
     * @param userId           userId of user making request.
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param uniqueParameterValue value of unique parameter (or null if no unique properties)
     * @param uniqueParameterName name of unique parameter (or null if no unique properties)
     * @param newObjectBuilder builder to create new entity
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void validateNewEntityRequest(String                         userId,
                                          String                         entityTypeGUID,
                                          String                         entityTypeName,
                                          String                         uniqueParameterValue,
                                          String                         uniqueParameterName,
                                          OpenMetadataAPIGenericBuilder  newObjectBuilder,
                                          String                         methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        if (uniqueParameterValue != null)
        {
            List<String> propertyNames = new ArrayList<>();
            propertyNames.add(uniqueParameterName);

            /*
             * An entity with the Memento classification set is ignored
             */
            List<EntityDetail> existingBeans = this.getEntitiesByValue(userId,
                                                                       uniqueParameterValue,
                                                                       uniqueParameterName,
                                                                       entityTypeGUID,
                                                                       entityTypeName,
                                                                       propertyNames,
                                                                       true,
                                                                       null,
                                                                       null,
                                                                       false,
                                                                       supportedZones,
                                                                       0,
                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                       methodName);

            if ((existingBeans != null) && (!existingBeans.isEmpty()))
            {
                /*
                 * Throw exception containing details of the first non-null bean that
                 * is found with a matching unique parameter value.
                 */
                invalidParameterHandler.throwUniqueNameInUse(uniqueParameterValue,
                                                             uniqueParameterName,
                                                             entityTypeName,
                                                             serviceName,
                                                             methodName);
            }
        }

        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.ASSET_TYPE_NAME))
        {
            /*
             * Need to build a description of the asset to pass to the metadata security object.
             */
            Asset assetBeanForMetadataSecurity = new Asset();

            setupAssetBeanWithEntityProperties(assetBeanForMetadataSecurity,
                                               entityTypeGUID,
                                               entityTypeName,
                                               newObjectBuilder.getInstanceStatus(),
                                               null,
                                               newObjectBuilder.getInstanceProperties(methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.SECURITY_TAG_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.CONFIDENCE_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.CRITICALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.IMPACT_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.RETENTION_CLASSIFICATION_TYPE_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, methodName),
                                               newObjectBuilder.getEntityClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, methodName),
                                               methodName);

            assetBeanForMetadataSecurity.setZoneMembership(securityVerifier.setAssetZonesToDefault(defaultZones, assetBeanForMetadataSecurity));

            securityVerifier.validateUserForAssetCreate(userId, assetBeanForMetadataSecurity);
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
     * @param methodName calling method
     * @throws UserNotAuthorizedException local server user id not authorized to update latest change
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
                                         String       methodName) throws UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String  guidParameterName = "anchorEntity.getGUID()";

        InstanceProperties newProperties = null;

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
                                                   OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_GUID,
                                                   OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                                   classification,
                                                   newProperties,
                                                   methodName);
            }
        }
        catch (ClassificationErrorException newClassificationNeeded)
        {
            /*
             * This is not an error - it just means that the classification is not present on the anchor entity.
             */
            repositoryHandler.classifyEntity(localServerUserId,
                                             null,
                                             null,
                                             anchorEntity.getGUID(),
                                             OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_GUID,
                                             OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                             ClassificationOrigin.ASSIGNED,
                                             null,
                                             newProperties,
                                             methodName);
        }
        catch (InvalidParameterException | TypeErrorException error)
        {
            throw new PropertyServerException(error);
        }
    }


    /**
     * Retrieve the supplementary properties glossary object.  This is the anchor of all the supplementary properties
     * glossary terms.
     *
     * @param methodName calling method
     *
     * @return unique identifier of the supplementary properties glossary
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    private String getSupplementaryPropertiesGlossary(String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {

        String glossaryGUID = this.getEntityGUIDByValue(localServerUserId,
                                                        supplementaryPropertiesGlossaryName,
                                                        supplementaryPropertiesGlossaryParameterName,
                                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_GUID,
                                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                                        qualifiedNamePropertyNamesList,
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
                                                                      supplementaryPropertiesGlossaryDescription,
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
     * Set up the instance properties for a supplementary properties glossary term.
     *
     * @param existingProperties properties to add the new properties to
     * @param displayName  display name for the term
     * @param summary short description
     * @param description description of the term
     * @param abbreviation abbreviation used for the term
     * @param usage illustrations of how the term is used
     * @param methodName calling method
     * @return properties object or null
     */
    private InstanceProperties getSupplementaryInstanceProperties(InstanceProperties existingProperties,
                                                                  String             displayName,
                                                                  String             summary,
                                                                  String             description,
                                                                  String             abbreviation,
                                                                  String             usage,
                                                                  String             methodName)
    {
        InstanceProperties properties = existingProperties;

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (summary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
                                                                      summary,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (abbreviation != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
                                                                      abbreviation,
                                                                      methodName);
        }

        if (usage != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        return properties;
    }



    /**
     * Maintain the supplementary properties of a technical metadata element in a glossary term linked to the supplied element.
     * The glossary term needs to be connected to a glossary which may need to be created.
     *
     * @param userId calling user
     * @param elementGUID element for the
     * @param elementQualifiedName qualified name of the linked element
     * @param displayName  display name for the term
     * @param summary short description
     * @param description description of the term
     * @param abbreviation abbreviation used for the term
     * @param usage illustrations of how the term is used
     * @param isMergeUpdate should the new properties be merged with the existing properties or completely replace them?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    public void maintainSupplementaryProperties(String  userId,
                                                String  elementGUID,
                                                String  elementQualifiedName,
                                                String  displayName,
                                                String  summary,
                                                String  description,
                                                String  abbreviation,
                                                String  usage,
                                                boolean isMergeUpdate,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        if ((displayName != null) || (summary != null) || (description != null) || (abbreviation != null) || (usage != null))
        {
            EntityDetail glossaryTerm = this.getEntityByValue(localServerUserId,
                                                              elementQualifiedName + supplementaryPropertiesQualifiedNamePostFix,
                                                              supplementaryPropertiesQualifiedNameParameterName,
                                                              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                                              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                              qualifiedNamePropertyNamesList,
                                                              methodName);

            if (glossaryTerm == null)
            {
                String glossaryGUID = this.getSupplementaryPropertiesGlossary(methodName);

                if (glossaryGUID != null)
                {
                    InstanceProperties glossaryTermProperties = this.getSupplementaryInstanceProperties(null,
                                                                                                        displayName,
                                                                                                        summary,
                                                                                                        description,
                                                                                                        abbreviation,
                                                                                                        usage,
                                                                                                        methodName);

                    /*
                     * The glossary term is anchored to the element rather than the glossary.  This means that it deleted if/when
                     * the element is deleted.
                     */
                    List<Classification> initialClassifications = new ArrayList<>();
                    try
                    {
                        Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                              null,
                                                                                              null,
                                                                                              InstanceProvenanceType.LOCAL_COHORT,
                                                                                              userId,
                                                                                              OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                                                              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                                              ClassificationOrigin.ASSIGNED,
                                                                                              null,
                                                                                              repositoryHelper.addStringPropertyToInstance(
                                                                                                      serviceName,
                                                                                                      null,
                                                                                                      OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                                                      elementGUID,
                                                                                                      methodName));
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
            else
            {
                InstanceProperties glossaryTermProperties;

                if (isMergeUpdate)
                {
                    glossaryTermProperties = this.getSupplementaryInstanceProperties(glossaryTerm.getProperties(),
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
                                                         null,
                                                         null,
                                                         glossaryTermProperties,
                                                         methodName);
            }
        }
    }


    /**
     * Count up the number of elements of a certain type that are attached to a specific entity.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the entity that the object is attached to (anchor entity)
     * @param elementTypeName type of the anchor entity
     * @param attachmentTypeGUID unique identifier of the attachment relationship's type
     * @param attachmentTypeName unique name of the attachment's type
     * @param methodName calling method
     *
     * @return count of attached objects
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public int countAttachments(String userId,
                                String elementGUID,
                                String elementTypeName,
                                String attachmentTypeGUID,
                                String attachmentTypeName,
                                String methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String guidParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        int count = 0;

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       elementGUID,
                                                                                       elementTypeName,
                                                                                       attachmentTypeGUID,
                                                                                       attachmentTypeName,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
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
                                          String       methodName) throws InvalidParameterException,
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
                                      false,
                                      supportedZones,
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
     * @param forLineage is this part of al lineage request?
     * @param serviceSupportedZones supported zones for calling service
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
                                          boolean      forLineage,
                                          List<String> serviceSupportedZones,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                             startingElementGUID,
                                                                             startingElementTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             methodName);

        if (entity != null)
        {
            validateAnchorEntity(userId,
                                 startingElementGUID,
                                 startingElementTypeName,
                                 entity,
                                 startingElementGUIDParameterName,
                                 false,
                                 serviceSupportedZones,
                                 methodName);

            if (repositoryHelper.isTypeOf(serviceName, entity.getType().getTypeDefName(), resultingElementTypeName))
            {
                if (! forLineage)
                {
                    try
                    {
                        if (repositoryHelper.getClassificationFromEntity(serviceName, entity, OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME, methodName) != null)
                        {
                            entity = null;
                        }
                    }
                    catch (ClassificationErrorException error)
                    {
                        /*
                         * Since this classification is not supported, it can not be attached to the entity
                         */
                    }
                }

                return entity;
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
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                                  int          startingFrom,
                                                  int          pageSize,
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
                                   null,
                                   null,
                                   false,
                                   supportedZones,
                                   startingFrom,
                                   pageSize,
                                   methodName);
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
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
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
                                   null,
                                   null,
                                   false,
                                   serviceSupportedZones,
                                   startingFrom,
                                   pageSize,
                                   methodName);
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
     * @param forLineage is this part of a lineage request?
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                                  boolean      forLineage,
                                                  List<String> serviceSupportedZones,
                                                  int          startingFrom,
                                                  int          pageSize,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingElementGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingElementGUID,
                                  startingElementGUIDParameterName,
                                  startingElementTypeName,
                                  false,
                                  serviceSupportedZones,
                                  methodName);

        List<Relationship> visibleRelationships = this.getAttachmentLinks(userId,
                                                                          startingElementGUID,
                                                                          startingElementGUIDParameterName,
                                                                          startingElementTypeName,
                                                                          relationshipTypeGUID,
                                                                          relationshipTypeName,
                                                                          resultingElementTypeName,
                                                                          startingFrom,
                                                                          pageSize,
                                                                          methodName);

        if (visibleRelationships != null)
        {
            List<EntityDetail> visibleEntities = new ArrayList<>();

            for (Relationship  relationship : visibleRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingElementGUID, startingElementTypeName, relationship, methodName);

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
                                                                             supportedZones,
                                                                             methodName));
                        }
                        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                        {
                            // skip entities that are not visible to this user
                            log.debug("Skipping inaccessible entity", inaccessibleEntity);
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
                                                 String   methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return this.getUniqueAttachmentLink(userId,
                                            startingGUID,
                                            startingGUIDParameterName,
                                            startingTypeName,
                                            attachmentRelationshipTypeGUID,
                                            attachmentRelationshipTypeName,
                                            attachmentEntityGUID,
                                            attachmentEntityTypeName,
                                            0,
                                            methodName);
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
                                                 String   methodName) throws InvalidParameterException,
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
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
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
                                       0,
                                       invalidParameterHandler.getMaxPagingSize(),
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
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String   userId,
                                                  String   startingGUID,
                                                  String   startingGUIDParameterName,
                                                  String   startingTypeName,
                                                  String   attachmentRelationshipTypeGUID,
                                                  String   attachmentRelationshipTypeName,
                                                  String   attachmentEntityTypeName,
                                                  int      startingFrom,
                                                  int      pageSize,
                                                  String   methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return this.getAttachmentLinks(userId,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       startingTypeName,
                                       attachmentRelationshipTypeGUID,
                                       attachmentRelationshipTypeName,
                                       null,
                                       attachmentEntityTypeName,
                                       0,
                                       startingFrom,
                                       pageSize,
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
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String   userId,
                                                  String   startingGUID,
                                                  String   startingGUIDParameterName,
                                                  String   startingTypeName,
                                                  String   attachmentRelationshipTypeGUID,
                                                  String   attachmentRelationshipTypeName,
                                                  String   attachmentEntityGUID,
                                                  String   attachmentEntityTypeName,
                                                  int      startingFrom,
                                                  int      pageSize,
                                                  String   methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        return this.getAttachmentLinks(userId,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       startingTypeName,
                                       attachmentRelationshipTypeGUID,
                                       attachmentRelationshipTypeName,
                                       attachmentEntityGUID,
                                       attachmentEntityTypeName,
                                       0,
                                       startingFrom,
                                       pageSize,
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
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String   userId,
                                                  String   startingGUID,
                                                  String   startingGUIDParameterName,
                                                  String   startingTypeName,
                                                  String   attachmentRelationshipTypeGUID,
                                                  String   attachmentRelationshipTypeName,
                                                  String   attachmentEntityGUID,
                                                  String   attachmentEntityTypeName,
                                                  int      attachmentEntityEnd,
                                                  int      startingFrom,
                                                  int      pageSize,
                                                  String   methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       startingGUID,
                                                                                       startingTypeName,
                                                                                       attachmentRelationshipTypeGUID,
                                                                                       attachmentRelationshipTypeName,
                                                                                       startingFrom,
                                                                                       queryPageSize,
                                                                                       methodName);


        List<Relationship> visibleRelationships = new ArrayList<>();

        while ((iterator.moreToReceive() && ((queryPageSize == 0) || (visibleRelationships.size() < queryPageSize))))
        {
            Relationship relationship = iterator.getNext();

            if (this.visibleToUserThroughRelationship(userId, relationship, methodName))
            {
                EntityProxy otherEnd;

                if (attachmentEntityEnd == 1)
                {
                    otherEnd = relationship.getEntityOneProxy();

                    if (startingGUID.equals(otherEnd.getGUID()))
                    {
                        otherEnd = null;
                    }
                }
                else if (attachmentEntityEnd == 2)
                {
                    otherEnd = relationship.getEntityTwoProxy();

                    if (startingGUID.equals(otherEnd.getGUID()))
                    {
                        otherEnd = null;
                    }
                }
                else
                {
                    otherEnd = repositoryHandler.getOtherEnd(startingGUID, startingTypeName, relationship, methodName);
                }

                if (otherEnd != null)
                {
                    /*
                     * Does the relationship point to to appropriate type of entity?
                     */
                    if (attachmentEntityTypeName != null)
                    {
                        if (otherEnd.getType() != null)
                        {
                            if (repositoryHelper.isTypeOf(serviceName, otherEnd.getType().getTypeDefName(), attachmentEntityTypeName))
                            {
                                if ((attachmentEntityGUID == null) || (attachmentEntityGUID.equals(otherEnd.getGUID())))
                                {
                                    visibleRelationships.add(relationship);
                                }
                            }
                        }
                    }
                    else
                    {
                        /*
                         * Any type of entity attachment will do
                         */
                        visibleRelationships.add(relationship);
                    }
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
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                  int                   startingFrom,
                                                  int                   pageSize,
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
                                              supportedZones,
                                              methodName);

                    this.validateAnchorEntity(userId,
                                              relationship.getEntityTwoProxy().getGUID(),
                                              entityTwoParameterName,
                                              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
                                              false,
                                              supportedZones,
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param uniqueParameterValue value of unique parameter (or null if no unique properties)
     * @param uniqueParameterName name of unique parameter (or null if no unique properties)
     * @param propertyBuilder builder pre-populated with the properties and classifications of the new entity
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
                                         String                        uniqueParameterValue,
                                         String                        uniqueParameterName,
                                         OpenMetadataAPIGenericBuilder propertyBuilder,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        validateNewEntityRequest(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 uniqueParameterValue,
                                 uniqueParameterName,
                                 propertyBuilder,
                                 methodName);

        return repositoryHandler.createEntity(userId,
                                              entityTypeGUID,
                                              entityTypeName,
                                              externalSourceGUID,
                                              externalSourceName,
                                              propertyBuilder.getInstanceProperties(methodName),
                                              propertyBuilder.getEntityClassifications(),
                                              propertyBuilder.getInstanceStatus(),
                                              methodName);
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is pre-loaded with
     * properties that should override the properties from the the template.  This is the method to call from the specific handlers.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
     * @return unique identifier of the the new bean
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
                                                                   methodName);

        if (templateProgress != null)
        {
            /*
             * This relationship shows where the property values for the new bean came from.  It enables traceability.  Also, if the template is
             * updated, there is a possibility of making complementary changes to the entities that were derived from it.
             */
            repositoryHandler.createRelationship(localServerUserId,
                                                 OpenMetadataAPIMapper.SOURCED_FROM_RELATIONSHIP_TYPE_GUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 templateProgress.newBeanGUID,
                                                 templateGUID,
                                                 null,
                                                 methodName);

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
        String              newBeanGUID          = null; /* GUID of last new entity created - ultimately this is returned to the original caller*/
        String              previousTemplateGUID = null; /* GUID of last template entity processed - prevents processing a relationship twice */
        Map<String, String> coveredGUIDMap       = new HashMap<>(); /* Map of template GUIDs to new bean GUIDs that have been processed - prevents replicating the same entity twice */
        String              beanAnchorGUID       = null; /* value of the anchor to set into the new beans */
    }


    /**
     * Create a new entity in the repository based on the contents of an existing entity (the template). The supplied builder is pre-loaded with
     * properties that should override the properties from the the template.  This method is called iterative for each entity anchored to the
     * original template.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
     * @param methodName calling method
     *
     * @return current progress of the template replication
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException there is a problem in the repository services
     * @throws UserNotAuthorizedException the user is not authorized to access one of the elements.
     */
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
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String newEntityParameterName = "newEntityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        /*
         * This call ensures the template exists and is the correct type. An exception will be thrown if there are any problems.
         */
        EntityDetail templateEntity = repositoryHandler.getEntityByGUID(userId, templateGUID, templateGUIDParameterName, entityTypeName, methodName);

        if (templateEntity != null)
        {
            /*
             * Check that the template is visible to the calling user.  If the template is an anchor, its own entity is returned.
             */
            EntityDetail templateAnchorEntity = this.validateAnchorEntity(userId,
                                                                          templateGUID,
                                                                          entityTypeName,
                                                                          templateEntity,
                                                                          templateGUIDParameterName,
                                                                          false,
                                                                          supportedZones,
                                                                          methodName);
            String templateAnchorGUID = null;
            if (templateAnchorEntity != null)
            {
                templateAnchorGUID = templateAnchorEntity.getGUID();
            }


            /*
             * Verify that the user is permitted to create a new bean.
             */
            validateNewEntityRequest(userId,
                                     entityTypeGUID,
                                     entityTypeName,
                                     uniqueParameterValue,
                                     uniqueParameterName,
                                     propertyBuilder,
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
                            (! OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME.equals(templateClassification.getName())))
                        {
                            newClassificationMap.put(templateClassification.getName(), templateClassification);
                        }
                    }
                }
            }

            /*
             * If the template has another anchor set up then this same anchor needs to be established for the new bean.
             */
            if ((firstIteration) && (templateAnchorGUID != null) && (! templateGUID.equals(templateAnchorGUID)))
            {
                /*
                 * Need to use the same anchor as the template.  This occurs the first time through the iteration if the initial
                 * template object has an anchor.
                 */
                propertyBuilder.setAnchors(userId, templateAnchorGUID, methodName);
            }
            else if (! firstIteration)
            {
                /*
                 * A bean anchor has been set up on a previous iteration.
                 */
                propertyBuilder.setAnchors(userId, templateProgress.beanAnchorGUID, methodName);
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
                                                                  entityTypeGUID,
                                                                  entityTypeName,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  propertyBuilder.getInstanceProperties(methodName),
                                                                  newClassifications,
                                                                  propertyBuilder.getInstanceStatus(),
                                                                  methodName);

            /*
             * This is the first time through the iteration and so we need to capture the top level bean's guid to act as the anchor for all other
             * beans that are created as a result of this templated creation.
             */
            if (firstIteration)
            {
                templateProgress.beanAnchorGUID = newEntityGUID;
            }

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
                                                               templateAnchorGUID,
                                                               entityTypeName,
                                                               uniqueParameterValue,
                                                               methodName);


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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source - null for local
     * @param templateProgress current new bean, previous GUID and list of entities from the template that have been processed (so we only create new elements one-to-one when there are cyclic relationships)
     * @param startingGUID unique identifier of the newly created element
     * @param startingGUIDParameterName parameter providing the startingGUID value
     * @param templateGUID unique identifier of the template element
     * @param templateAnchorGUID unique identifier of the template's anchor
     * @param expectedTypeName type name of the new bean
     * @param qualifiedName unique name for this new bean - must not be null
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
                                                        String           templateAnchorGUID,
                                                        String           expectedTypeName,
                                                        String           qualifiedName,
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
         * Begin by retrieving all of the relationships attached to the template.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       templateGUID,
                                                                                       expectedTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
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

            if ((entityProxy != null) && (entityProxy.getType() != null) && (! entityProxy.getGUID().equals(previousTemplateGUID)))
            {
                EntityDetail nextTemplateEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                    entityProxy.getGUID(),
                                                                                    nextTemplateEntityGUIDParameterName,
                                                                                    null,
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
                                                                                      false,
                                                                                      supportedZones,
                                                                                      methodName);

                    String nextTemplateAnchorGUID = null;
                    if (nextTemplateEntityAnchor != null)
                    {
                        nextTemplateAnchorGUID = nextTemplateEntityAnchor.getGUID();
                    }
                    String nextBeanEntityGUID;

                    if (templateProgress.coveredGUIDMap.keySet().contains(nextTemplateEntity.getGUID()))
                    {
                        /*
                         * The template entity has already been replicated and so we just need to create the
                         * relationship from the equivalent new bean to the start bean.
                         */
                        nextBeanEntityGUID = templateProgress.coveredGUIDMap.get(nextTemplateEntity.getGUID());
                    }
                    else if ((nextTemplateAnchorGUID == null) || (! nextTemplateAnchorGUID.equals(templateAnchorGUID)))
                    {
                        /*
                         * The linked entity is either not got an anchorGUID or has a different anchorGUID.
                         * However we still need to create the relationship between the start bean and the linked entity.
                         */
                        nextBeanEntityGUID = nextTemplateEntity.getGUID();
                    }
                    else
                    {
                        /*
                         * This linked entity has the same anchorGUID so it need to be copied.
                         */
                        OpenMetadataAPIGenericBuilder builder;
                        String nextQualifiedName = null;
                        if (repositoryHelper.isTypeOf(serviceName, nextTemplateEntityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
                        {
                            nextQualifiedName = qualifiedName + "::" + nextTemplateEntityTypeName;
                            int    nextQualifiedNameCount = 0;

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
                                                  supportedZones,
                                                  relationship.getType().getTypeDefGUID(),
                                                  relationship.getType().getTypeDefName(),
                                                  relationship.getProperties(),
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
                                                  supportedZones,
                                                  relationship.getType().getTypeDefGUID(),
                                                  relationship.getType().getTypeDefName(),
                                                  relationship.getProperties(),
                                                  methodName);
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
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param propertyName name of bean property to update
     * @param propertyValue new value for bean property
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the properties tin the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanPropertyInRepository(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String entityGUID,
                                               String entityGUIDParameterName,
                                               String entityTypeGUID,
                                               String entityTypeName,
                                               String propertyName,
                                               String propertyValue,
                                               String methodName) throws InvalidParameterException,
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
                                    supportedZones,
                                    properties,
                                    true,
                                    methodName);
    }


    /**
     * Update one or more properties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
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
                                    supportedZones,
                                    properties,
                                    isMergeUpdate,
                                    methodName);
    }


    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param serviceSupportedZones supported zones for calling service
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing the just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
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
                                       List<String>       serviceSupportedZones,
                                       InstanceProperties updateProperties,
                                       boolean            isMergeUpdate,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        /*
         * This returns the entity for the connect to element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                                  entityGUID,
                                                                  entityTypeName,
                                                                  originalEntity,
                                                                  entityGUIDParameterName,
                                                                  true,
                                                                  serviceSupportedZones,
                                                                  methodName);

            /*
             * Sort out the properties
             */
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  updateProperties,
                                                                  originalEntity.getProperties());

            /*
             * There is an extra security check if the update is for an asset.
             */
            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
            {
                this.validateUserForAssetUpdate(userId,
                                                originalEntity,
                                                newProperties,
                                                originalEntity.getStatus(),
                                                methodName);
            }

            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     entityGUID,
                                                     originalEntity,
                                                     entityTypeGUID,
                                                     entityTypeName,
                                                     newProperties,
                                                     methodName);

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Updating properties in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             OpenMetadataAPIMapper.ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL,
                                             OpenMetadataAPIMapper.UPDATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
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
     * Update the instance status in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param newStatus new status value
     * @param newStatusParameterName parameter providing the new status value
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
                                             InstanceStatus     newStatus,
                                             String             newStatusParameterName,
                                             String             methodName) throws InvalidParameterException,
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
                                     supportedZones,
                                     newStatus,
                                     newStatusParameterName,
                                     methodName);
    }


    /**
     * Update the instance status in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param serviceSupportedZones supported zones for calling service
     * @param newStatus new status value
     * @param newStatusParameterName parameter providing the new status value
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
                                             List<String>       serviceSupportedZones,
                                             InstanceStatus     newStatus,
                                             String             newStatusParameterName,
                                             String             methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(newStatus, newStatusParameterName, methodName);

        /*
         * This returns the entity for the connect to element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                                  entityGUID,
                                                                  entityTypeName,
                                                                  originalEntity,
                                                                  entityGUIDParameterName,
                                                                  true,
                                                                  serviceSupportedZones,
                                                                  methodName);

            /*
             * There is an extra security check if the update is for an asset.
             */
            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataAPIMapper.ASSET_TYPE_NAME))
            {
                this.validateUserForAssetUpdate(userId,
                                                originalEntity,
                                                originalEntity.getProperties(),
                                                newStatus,
                                                methodName);
            }

            repositoryHandler.updateEntityStatus(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 entityGUID,
                                                 entityTypeGUID,
                                                 entityTypeName,
                                                 newStatus,
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
     * Classify as an Memento any entity if it is anchored to the anchor entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorEntity entity anchor to match against
     * @param potentialAnchoredEntity entity to validate
     * @param classificationOriginGUID original entity that the Memento classification  was attached to
     * @param classificationProperties properties for the classification
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
                                                                        methodName);

                String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                if ((anchorGUID != null) && (anchorGUID.equals(anchorEntity.getGUID())))
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
                                                 methodName);
                }
            }
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationProperties properties for the classification
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
                                     supportedZones,
                                     methodName);
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived. Note, this method is designed to work only on anchor entities or entities with no anchor.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationProperties properties for the classification
     * @param serviceSupportedZones supported zones for calling service
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
                                        List<String>       serviceSupportedZones,
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
                                                              false,
                                                              serviceSupportedZones,
                                                              methodName);

        /*
         * At this point, archiving is only supported on the anchor entity.  This needs to change (eg to be able to archive schema elements)
         * by adding logic very similar to the templating logic that makes sure the archive processing travels down the hiierarchy and does not
         * cover the whole anchored entity.
         */
        invalidParameterHandler.validateAnchorGUID(entityGUID,
                                                   entityGUIDParameterName,
                                                   anchorEntity,
                                                   entityGUID,
                                                   entityTypeName,
                                                   methodName);

        this.archiveBeanInRepository(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     entityGUID,
                                     entityGUIDParameterName,
                                     entityTypeName,
                                     ClassificationOrigin.ASSIGNED,
                                     entityGUID,
                                     classificationProperties,
                                     anchorEntity,
                                     methodName);

        /*
         * Update the the LatestChange in the archived entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
        {
            final String actionDescriptionTemplate = "Classifying as Memento %s %s";

            String actionDescription  = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);
            int    latestChangeTarget = OpenMetadataAPIMapper.ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL;

            this.addLatestChangeToAnchor(anchorEntity,
                                         latestChangeTarget,
                                         OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                         OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                         entityGUID,
                                         entityTypeName,
                                         null,
                                         userId,
                                         actionDescription,
                                         methodName);
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived.  Note that this classification is propagated to all elements with the same
     * AnchorGUID.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName parameter name supplying entityGUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationOrigin is this classification assigned or propagated?
     * @param classificationOriginGUID which entity did a propagated classification originate from?
     * @param classificationProperties properties for the classification
     * @param anchorEntity anchor entity for the bean (can be null)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void archiveBeanInRepository(String               userId,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         String               entityGUID,
                                         String               entityGUIDParameterName,
                                         String               entityTypeName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties,
                                         EntityDetail         anchorEntity,
                                         String               methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        /*
         * This is to pick up any errors in the iteration through the anchored elements.
         */
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        /*
         * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is archived.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       entityGUID,
                                                                                       entityTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            this.archiveAnchoredEntity(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       anchorEntity,
                                       repositoryHandler.getOtherEnd(entityGUID, entityTypeName, relationship, methodName),
                                       classificationOriginGUID,
                                       classificationProperties,
                                       methodName);
        }

        /*
         * This method explicitly removes all relationships attached to the entity before it deleted the entity.  This ensure that repository
         * events are created for all of the relationships.  This is why the code above needs to deal with the nested entities first.
         */
        repositoryHandler.classifyEntity(userId,
                                         null,
                                         null,
                                         entityGUID,
                                         OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_GUID,
                                         OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME,
                                         classificationOrigin,
                                         classificationOriginGUID,
                                         classificationProperties,
                                         methodName);


        /*
         * Update the qualified name in the archived entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME))
        {
            String qualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      anchorEntity.getProperties(),
                                                                      methodName) + "_archivedOn_" + new Date().toString();

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
                                                methodName);
        }
    }


    /**
     * Remove any entity if it is anchored to the anchor entity
     *
     * @param anchorEntity entity anchor to match against
     * @param potentialAnchoredEntity entity to validate
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorize to issue this request
     */
    private void deleteAnchoredEntity(EntityDetail anchorEntity,
                                      EntityProxy  potentialAnchoredEntity,
                                      String       methodName) throws InvalidParameterException,
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
                EntityDetail entity = repositoryHandler.getEntityByGUID(localServerUserId,
                                                                        potentialAnchoredEntity.getGUID(),
                                                                        guidParameterName,
                                                                        potentialAnchoredEntity.getType().getTypeDefName(),
                                                                        methodName);

                String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                if ((anchorGUID != null) && (anchorGUID.equals(anchorEntity.getGUID())))
                {
                    /*
                     * The entity is anchored to the anchor entity so it needs deleting.  This is done
                     */
                    String       externalSourceGUID = null;
                    String       externalSourceName = null;

                    if (entity.getInstanceProvenanceType() != InstanceProvenanceType.LOCAL_COHORT)
                    {
                        externalSourceGUID = entity.getMetadataCollectionId();
                        externalSourceName = entity.getMetadataCollectionName();
                    }

                    this.deleteBeanInRepository(localServerUserId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entity.getGUID(),
                                                guidParameterName,
                                                potentialAnchoredEntity.getType().getTypeDefGUID(),
                                                potentialAnchoredEntity.getType().getTypeDefName(),
                                                null,
                                                null,
                                                anchorEntity,
                                                methodName);
                }
            }
        }
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - of null if no verification is required
     * @param validatingPropertyValue value of property to verify
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
                                       String       methodName) throws InvalidParameterException,
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
                                    supportedZones,
                                    methodName);
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - of null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param serviceSupportedZones supported zones for calling service
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
                                       List<String> serviceSupportedZones,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              entityGUID,
                                                              entityGUIDParameterName,
                                                              entityTypeName,
                                                              false,
                                                              serviceSupportedZones,
                                                              methodName);

        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    validatingPropertyName,
                                    validatingPropertyValue,
                                    anchorEntity,
                                    methodName);
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param validatingPropertyName name of property to verify - null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param anchorEntity anchor entity for the bean (can be null)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void deleteBeanInRepository(String       userId,
                                        String       externalSourceGUID,
                                        String       externalSourceName,
                                        String       entityGUID,
                                        String       entityGUIDParameterName,
                                        String       entityTypeGUID,
                                        String       entityTypeName,
                                        String       validatingPropertyName,
                                        String       validatingPropertyValue,
                                        EntityDetail anchorEntity,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is deleted.
         * (This is why we explicitly delete the relationship to the parent element before calling this method).
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       entityGUID,
                                                                                       entityTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);

            this.deleteAnchoredEntity(anchorEntity,
                                      repositoryHandler.getOtherEnd(entityGUID,
                                                                    entityTypeName,
                                                                    relationship,
                                                                    methodName),
                                      methodName);
        }

        /*
         * This method explicitly removes all relationships attached to the entity before it deleted the entity.  This ensure that repository
         * events are created for all of the relationships.  This is why the code above needs to deal with the nested entities first.
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
                                       methodName);

        /*
         * Update the LatestChange in the anchor entity if it is not the instance we have just deleted.
         */
        if ((anchorEntity != null) && (! entityGUID.equals(anchorEntity.getGUID())))
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
                                         methodName);
        }
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
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                                int          startingFrom,
                                                int          pageSize,
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
                                            supportedZones,
                                            startingFrom,
                                            pageSize,
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
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                                List<String> serviceSupportedZones,
                                                int          startingFrom,
                                                int          pageSize,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  false,
                                  serviceSupportedZones,
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
                                                                    attachmentEntityTypeName,
                                                                    startingFrom,
                                                                    pageSize,
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
                EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingGUID,
                                                                        startingTypeName,
                                                                        relationship,
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
                                                   serviceSupportedZones,
                                                   methodName);

                        results.add(entityProxy.getGUID());
                    }
                    catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException nonAccessibleEntity)
                    {
                        // skip entities that are not visible to this user
                        log.debug("Skipping entity", nonAccessibleEntity);
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
                                         String       methodName) throws InvalidParameterException,
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
                                      supportedZones,
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
     * @param serviceSupportedZones supported zones for calling service
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
                                         List<String> serviceSupportedZones,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "relationship.end.guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  false,
                                  serviceSupportedZones,
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
                                                                    attachmentEntityTypeName,
                                                                    0,
                                                                    invalidParameterHandler.getMaxPagingSize(),
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
                EntityProxy entityProxy = null;

                if (selectionEnd == 0)
                {
                    entityProxy = repositoryHandler.getOtherEnd(startingGUID,
                                                                startingTypeName,
                                                                relationship,
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
                    try
                    {
                        this.validateAnchorEntity(userId,
                                                  entityProxy.getGUID(),
                                                  guidParameterName,
                                                  attachmentEntityTypeName,
                                                  false,
                                                  serviceSupportedZones,
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
                        log.debug("Skipping entity", nonAccessibleEntity);
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
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return getAttachedElement(userId,
                                  startingElementGUID,
                                  startingElementGUIDParameterName,
                                  startingElementTypeName,
                                  relationshipTypeGUID,
                                  relationshipTypeName,
                                  resultingElementTypeName,
                                  supportedZones,
                                  methodName);
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
     * @param serviceSupportedZones supported zones for calling service
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
                                List<String> serviceSupportedZones,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingElementGUIDParameterName, methodName);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                             startingElementGUID,
                                                                             startingElementTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             methodName);

        if (entity != null)
        {
            validateAnchorEntity(userId,
                                 startingElementGUID,
                                 startingElementTypeName,
                                 entity,
                                 startingElementGUIDParameterName,
                                 false,
                                 serviceSupportedZones,
                                 methodName);

            if (repositoryHelper.isTypeOf(serviceName, entity.getType().getTypeDefName(), resultingElementTypeName))
            {
                return converter.getNewBean(beanClass, entity, methodName);
            }
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
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String userId,
                                         String startingGUID,
                                         String startingGUIDParameterName,
                                         String startingTypeName,
                                         String attachmentRelationshipTypeGUID,
                                         String attachmentRelationshipTypeName,
                                         String attachmentEntityTypeName,
                                         int    startingFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
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
                                        null,
                                        null,
                                        0,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
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
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String       userId,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingTypeName,
                                         String       attachmentRelationshipTypeGUID,
                                         String       attachmentRelationshipTypeName,
                                         String       attachmentEntityTypeName,
                                         List<String> serviceSupportedZones,
                                         int          startingFrom,
                                         int          pageSize,
                                         String       methodName) throws InvalidParameterException,
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
                                        null,
                                        null,
                                        0,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
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
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String       userId,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingTypeName,
                                         String       attachmentRelationshipTypeGUID,
                                         String       attachmentRelationshipTypeName,
                                         String       attachmentEntityTypeName,
                                         String       requiredClassificationName,
                                         String       omittedClassificationName,
                                         int          selectionEnd,
                                         int          startingFrom,
                                         int          pageSize,
                                         String       methodName) throws InvalidParameterException,
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
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
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
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
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
                                         List<String> serviceSupportedZones,
                                         int          startingFrom,
                                         int          pageSize,
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
                                                              false,
                                                              serviceSupportedZones,
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
                                                                    startingFrom,
                                                                    pageSize,
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
                                                     false,
                                                     selectionEnd,
                                                     serviceSupportedZones,
                                                     methodName);
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
                catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                {
                    // skip entities that are not visible to this user
                    log.debug("Skipping inaccessible entity", inaccessibleEntity);
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
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAnchorsForAttachedElements(String       userId,
                                                   String       startingGUID,
                                                   String       startingGUIDParameterName,
                                                   String       startingTypeName,
                                                   String       attachmentRelationshipTypeGUID,
                                                   String       attachmentRelationshipTypeName,
                                                   String       attachmentEntityTypeName,
                                                   String       requiredClassificationName,
                                                   String       omittedClassificationName,
                                                   int          selectionEnd,
                                                   List<String> serviceSupportedZones,
                                                   int          startingFrom,
                                                   int          pageSize,
                                                   String       methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  false,
                                  serviceSupportedZones,
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
                                                                    attachmentEntityTypeName,
                                                                    startingFrom,
                                                                    pageSize,
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
                                                     false,
                                                     selectionEnd,
                                                     serviceSupportedZones,
                                                     methodName);
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
                catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                {
                    // skip entities that are not visible to this user
                    log.debug("Skipping inaccessible entity", inaccessibleEntity);
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
     * @param forLineage is this request part of lineage?
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param serviceSupportedZones supported zones for calling service
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
                                 boolean       forLineage,
                                 int           selectionEnd,
                                 List<String>  serviceSupportedZones,
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
                                                                        methodName);

                this.validateAnchorEntity(userId,
                                          entityProxy.getGUID(),
                                          attachmentEntityTypeName,
                                          entity,
                                          guidParameterName,
                                          false,
                                          serviceSupportedZones,
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


                if (! forLineage)
                {
                    try
                    {
                        if (repositoryHelper.getClassificationFromEntity(serviceName, entity, OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME, methodName) != null)
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
     * Return the keyword for the supplied unique identifier (guid).  The keyword is only returned if
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String       userId,
                                                String       requestedEntityGUID,
                                                String       requestedEntityGUIDParameterName,
                                                String       requestedEntityTypeName,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getEntityFromRepository(userId,
                                            requestedEntityGUID,
                                            requestedEntityGUIDParameterName,
                                            requestedEntityTypeName,
                                            null,
                                            null,
                                            false,
                                            supportedZones,
                                            methodName);
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
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String       userId,
                                                String       requestedEntityGUID,
                                                String       requestedEntityGUIDParameterName,
                                                String       requestedEntityTypeName,
                                                String       requiredClassificationName,
                                                String       omittedClassificationName,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getEntityFromRepository(userId,
                                            requestedEntityGUID,
                                            requestedEntityGUIDParameterName,
                                            requestedEntityTypeName,
                                            requiredClassificationName,
                                            omittedClassificationName,
                                            false,
                                            supportedZones,
                                            methodName);
    }


    /**
     * Return the entity for the supplied unique identifier (guid).  An exception is thrown if the entity does not exist.
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param serviceSupportedZones supported zones for calling service
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
                                                List<String> serviceSupportedZones,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return getEntityFromRepository(userId,
                                       requestedEntityGUID,
                                       requestedEntityGUIDParameterName,
                                       requestedEntityTypeName,
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       false,
                                       serviceSupportedZones,
                                       methodName);
    }


    /**
     * Return the entity for the supplied unique identifier (guid).  An exception is thrown if the entity does not exist.
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param serviceSupportedZones supported zones for calling service
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
                                                List<String> serviceSupportedZones,
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
                                                                          methodName);

        /*
         * This method validates that the entity is visible to the calling user.
         */
        this.validateAnchorEntity(userId,
                                  requestedEntityGUID,
                                  requestedEntityTypeName,
                                  retrievedEntity,
                                  requestedEntityGUIDParameterName,
                                  false,
                                  serviceSupportedZones,
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
                                          supportedZones,
                                          methodName);
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the entity to retrieve from the repository
     * @param guidParameterName name of the parameter supplying the GUID
     * @param entityTypeName name of type of entity to retrieve
     * @param serviceSupportedZones supported zones for calling service
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
                                   List<String> serviceSupportedZones,
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
                                                           serviceSupportedZones,
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
                                               String       methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        return this.getBeanStringPropertyFromRepository(userId,
                                                        entityGUID,
                                                        entityGUIDParameterName,
                                                        entityTypeName,
                                                        propertyName,
                                                        supportedZones,
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
                                               List<String>       serviceSupportedZones,
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
                                                                 serviceSupportedZones,
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param queryPageSize maximum number of values to return
     * @param methodName calling method
     * @return configured iterator
     */
    RepositoryIteratorForEntities getEntitySearchIterator(String       userId,
                                                          String       searchString,
                                                          String       resultTypeGUID,
                                                          String       resultTypeName,
                                                          List<String> specificMatchPropertyNames,
                                                          boolean      exactValueMatch,
                                                          int          startFrom,
                                                          int          queryPageSize,
                                                          String       methodName)
    {
        RepositoryIteratorForEntities iterator;

        if (searchString != null)
        {
            String searchValue = searchString;

            if (exactValueMatch)
            {
                searchValue = repositoryHelper.getExactMatchRegex(searchString);
            }

            if ((specificMatchPropertyNames == null) || (specificMatchPropertyNames.isEmpty()))
            {
                /*
                 * Search for value in any string property
                 */
                iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                  userId,
                                                                  resultTypeGUID,
                                                                  searchValue,
                                                                  startFrom,
                                                                  queryPageSize,
                                                                  methodName);
            }
            else
            {
                /*
                 * Search for value in specific properties
                 */
                iterator = new RepositorySelectedEntitiesIterator(repositoryHandler,
                                                                  userId,
                                                                  resultTypeGUID,
                                                                  this.getSearchInstanceProperties(searchValue, specificMatchPropertyNames, methodName),
                                                                  MatchCriteria.ANY,
                                                                  startFrom,
                                                                  queryPageSize,
                                                                  methodName);
            }
        }
        else
        {
            /*
             * Get all values of a specific type
             */
            iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                      userId,
                                                      resultTypeGUID,
                                                      resultTypeName,
                                                      startFrom,
                                                      queryPageSize,
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
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return this.getBeanGUIDByUniqueName(userId, name, nameParameterName, namePropertyName, resultTypeGUID, resultTypeName, supportedZones, methodName);
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
     * @param serviceSupportedZones list of supported zones for this service
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
                                          List<String> serviceSupportedZones,
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
                                                                         0,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        String guid = null;
        List<String>  duplicateEntities = new ArrayList<>();
        String entityParameterName = "Entity from search of value " + name;

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
                                         false,
                                         serviceSupportedZones,
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
                                   supportedZones,
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
     * @param serviceSupportedZones list of supported zones for this service
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
                                 List<String> serviceSupportedZones,
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
                                                                         0,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        B bean = null;
        List<String>  duplicateEntities = new ArrayList<>();
        String entityParameterName = "Entity from search of value " + name;

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
                                         false,
                                         serviceSupportedZones,
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
     * Return the bean that matches the requested value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
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
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
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
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                   int          startFrom,
                                   int          pageSize,
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
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return the list of beans matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
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
                             int          startFrom,
                             int          pageSize,
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
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Return the list of beans matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
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
                             String       requiredClassificationName,
                             String       omittedClassificationName,
                             int          startFrom,
                             int          pageSize,
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
                                    requiredClassificationName,
                                    omittedClassificationName,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
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
                             List<String> serviceSupportedZones,
                             int          startFrom,
                             int          pageSize,
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
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
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
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
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
                             int                   startingFrom,
                             int                   pageSize,
                             String                methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);


        /*
         * Now need to ensure that the anchor's classification is pushed down to the dependent elements.  This is done by retrieving the
         * relationships.
         */
        RepositoryFindEntitiesIterator iterator = new RepositoryFindEntitiesIterator(repositoryHandler,
                                                                                     userId,
                                                                                     metadataElementTypeName,
                                                                                     metadataElementSubtypeName,
                                                                                     searchProperties,
                                                                                     limitResultsByStatus,
                                                                                     searchClassifications,
                                                                                     asOfTime,
                                                                                     sequencingProperty,
                                                                                     sequencingOrder,
                                                                                     startingFrom,
                                                                                     queryPageSize,
                                                                                     methodName);

        List<B> results = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            if (entity != null)
            {
                results.add(converter.getNewBean(beanClass, entity, methodName));
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                   List<String> serviceSupportedZones,
                                   int          startFrom,
                                   int          pageSize,
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
                                    null,
                                    null,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                   int          startFrom,
                                   int          pageSize,
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
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                   List<String> serviceSupportedZones,
                                   int          startFrom,
                                   int          pageSize,
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
                                    false,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                   List<String> serviceSupportedZones,
                                   int          startFrom,
                                   int          pageSize,
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
                                                         requiredClassificationName,
                                                         omittedClassificationName,
                                                         forLineage,
                                                         serviceSupportedZones,
                                                         startFrom,
                                                         pageSize,
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                                List<String> serviceSupportedZones,
                                                int          startFrom,
                                                int          pageSize,
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
                                                                         startFrom,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<EntityDetail>  results = new ArrayList<>();
        String entityParameterName = "Entity of type" + resultTypeName;

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
                                         serviceSupportedZones,
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
                         * Valid entity to return since no exception occurred and it is not archived.
                         */
                        results.add(entity);
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
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
                                           int          startFrom,
                                           int          pageSize,
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
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       false,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                                 int          startFrom,
                                                 int          pageSize,
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
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       false,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
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
     * @param forLineage boolean indicating whether the entity is being retrieved for a lineage request or not.
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                                 List<String> serviceSupportedZones,
                                                 int          startFrom,
                                                 int          pageSize,
                                                 String       methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(value, valueParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         value,
                                                                         resultTypeGUID,
                                                                         resultTypeName,
                                                                         specificMatchPropertyNames,
                                                                         exactValueMatch,
                                                                         startFrom,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<EntityDetail>  results = new ArrayList<>();
        String entityParameterName = "Entity from search of value " + value;

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
                                         serviceSupportedZones,
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
                            /*
                             * Since this classification is not supported, it can not be attached to the entity
                             */
                            beanValid = true;
                        }
                    }

                    if (! forLineage)
                    {
                        try
                        {
                            if (repositoryHelper.getClassificationFromEntity(serviceName, entity, OpenMetadataAPIMapper.MEMENTO_CLASSIFICATION_TYPE_NAME, methodName) != null)
                            {
                                beanValid = false;
                            }
                        }
                        catch (ClassificationErrorException error)
                        {
                            /*
                             * Since this classification is not supported, it can not be attached to the entity
                             */
                            beanValid = true;
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                              List<String> serviceSupportedZones,
                                              int          startFrom,
                                              int          pageSize,
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
                                                              requiredClassificationName,
                                                              omittedClassificationName,
                                                              forLineage,
                                                              serviceSupportedZones,
                                                              startFrom,
                                                              pageSize,
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
                                                             false,
                                                             supportedZones,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
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
                                      int          startFrom,
                                      int          pageSize,
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
                                          false,
                                          supportedZones,
                                          startFrom,
                                          pageSize,
                                          methodName);
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByValue(String       userId,
                                            String       value,
                                            String       valueParameterName,
                                            String       resultTypeGUID,
                                            String       resultTypeName,
                                            List<String> specificMatchPropertyNames,
                                            boolean      exactValueMatch,
                                            List<String> serviceSupportedZones,
                                            int          startFrom,
                                            int          pageSize,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return this.getEntityGUIDsByValue(userId,
                                          value,
                                          valueParameterName,
                                          resultTypeGUID,
                                          resultTypeName,
                                          specificMatchPropertyNames,
                                          exactValueMatch,
                                          null,
                                          null,
                                          false,
                                          serviceSupportedZones,
                                          startFrom,
                                          pageSize,
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
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                     List<String> serviceSupportedZones,
                                     int          startFrom,
                                     int          pageSize,
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
                                                                         startFrom,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<B>  results = new ArrayList<>();
        String entityParameterName = "Entity from createdBy search of " + searchString;

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
                                             serviceSupportedZones,
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
                                results.add(bean);
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                  int          startFrom,
                                  int          pageSize,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        return getBeansByType(userId, resultTypeGUID, resultTypeName, supportedZones, startFrom, pageSize, methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied name.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                  List<String> serviceSupportedZones,
                                  int          startFrom,
                                  int          pageSize,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             serviceSupportedZones,
                                                             startFrom,
                                                             pageSize,
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
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                           int          startFrom,
                                           int          pageSize,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return this.getBeanGUIDsByType(userId, resultTypeGUID, resultTypeName, supportedZones, startFrom, pageSize, methodName);
    }

    /**
     * Return the list of beans of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
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
                                           List<String> serviceSupportedZones,
                                           int          startFrom,
                                           int          pageSize,
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
                                                                         startFrom,
                                                                         queryPageSize,
                                                                         methodName);

        /*
         * The loop is necessary because some of the entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        List<String>  results = new ArrayList<>();
        String entityParameterName = "Entity of type" + resultTypeName;

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
                                         serviceSupportedZones,
                                         methodName);

                    /*
                     * Valid entity to return since no exception occurred.
                     */
                    results.add(entity.getGUID());
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
     * Return the list of beans of the requested type that match the supplied name.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultClassificationName unique name of the classification that the results should match with
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByClassification(String       userId,
                                            String       resultTypeGUID,
                                            String       resultClassificationName,
                                            int          startFrom,
                                            int          pageSize,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         resultTypeGUID,
                                                                                         resultClassificationName,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         methodName);

        if (entities != null)
        {
            List<B>  softwareServerCapabilities = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    softwareServerCapabilities.add(converter.getNewBean(beanClass, entity, methodName));
                }
            }

            if (! softwareServerCapabilities.isEmpty())
            {
                return softwareServerCapabilities;
            }
        }

        return null;
    }


    /**
     * Return the list of beans of the requested type that match the supplied name.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultClassificationName unique name of the classification that the results should match with
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param methodName calling method
     *
     * @return list of guids representing beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getBeanGUIDsByClassification(String       userId,
                                                     String       resultTypeGUID,
                                                     String       resultClassificationName,
                                                     int          startFrom,
                                                     int          pageSize,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         resultTypeGUID,
                                                                                         resultClassificationName,
                                                                                         startFrom,
                                                                                         queryPageSize,
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
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param relationshipTypeGUID        unique identifier of type of the relationship to create
     * @param relationshipTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                       String             relationshipTypeGUID,
                                       String             relationshipTypeName,
                                       InstanceProperties relationshipProperties,
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
                                         supportedZones,
                                         relationshipTypeGUID,
                                         relationshipTypeName,
                                         relationshipProperties,
                                         methodName);
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                       String             startingGUID,
                                       String             startingGUIDParameterName,
                                       String             startingElementTypeName,
                                       String             attachingGUID,
                                       String             attachingGUIDParameterName,
                                       String             attachingElementTypeName,
                                       List<String>       suppliedSupportedZones,
                                       String             attachmentTypeGUID,
                                       String             attachmentTypeName,
                                       InstanceProperties relationshipProperties,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingGUID, attachingGUIDParameterName, methodName);

        EntityDetail  startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                startingGUID,
                                                                                startingGUIDParameterName,
                                                                                startingElementTypeName,
                                                                                methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             false,
                                                                             suppliedSupportedZones,
                                                                             methodName);

        EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                 attachingGUID,
                                                                                 attachingGUIDParameterName,
                                                                                 attachingElementTypeName,
                                                                                 methodName);

        EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                              attachingGUID,
                                                                              attachingElementTypeName,
                                                                              attachingElementEntity,
                                                                              attachingGUIDParameterName,
                                                                              false,
                                                                              suppliedSupportedZones,
                                                                              methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         */

        Relationship relationship = repositoryHandler.createRelationship(userId,
                                                                         attachmentTypeGUID,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         startingGUID,
                                                                         attachingGUID,
                                                                         relationshipProperties,
                                                                         methodName);

        /*
         * Final stage is to add the latest change classification to the anchor(s).
         * The act of creating the relationship may set up the anchor GUID in either element.
         */
        String startingElementAnchorGUID;

        if (startingElementAnchorEntity == null)
        {
            startingElementAnchorGUID = this.reEvaluateAnchorGUID(startingGUID,
                                                                  startingElementTypeName,
                                                                  startingElementEntity,
                                                                  null,
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
                                                                          attachingElementTypeName,
                                                                          attachingElementEntity,
                                                                          null,
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
                                                                                     methodName);
                }
            }
        }


        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */
        final String actionDescriptionTemplate = "Linking %s %s to %s %s";

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 startingGUID,
                                                 attachingElementTypeName,
                                                 attachingGUID);

        if (startingElementAnchorEntity != null)
        {
            this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                         OpenMetadataAPIMapper.ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL,
                                         OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                         null,
                                         attachingGUID,
                                         attachingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
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
                                             attachingGUID,
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
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
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             methodName);
            }
        }
        else if (! attachingGUID.equals(startingElementAnchorGUID))
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
                                             OpenMetadataAPIMapper.CREATED_LATEST_CHANGE_ACTION_ORDINAL,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             methodName);
            }
        }

        if (relationship != null)
        {
            return relationship.getGUID();
        }

        return null;
    }



    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                           String             attachmentTypeGUID,
                                           String             attachmentTypeName,
                                           InstanceProperties relationshipProperties,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        updateElementToElementLink(userId,
                                   externalSourceGUID,
                                   externalSourceName,
                                   startingGUID,
                                   startingGUIDParameterName,
                                   startingElementTypeName,
                                   attachingGUID,
                                   attachingGUIDParameterName,
                                   attachingElementTypeName,
                                   supportedZones,
                                   attachmentTypeGUID,
                                   attachmentTypeName,
                                   false,
                                   relationshipProperties,
                                   methodName);
    }


    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing the just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                           List<String>       suppliedSupportedZones,
                                           String             attachmentTypeGUID,
                                           String             attachmentTypeName,
                                           boolean            isMergeUpdate,
                                           InstanceProperties relationshipProperties,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingGUID, attachingGUIDParameterName, methodName);

        EntityDetail  startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                startingGUID,
                                                                                startingGUIDParameterName,
                                                                                startingElementTypeName,
                                                                                methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             false,
                                                                             suppliedSupportedZones,
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
                                                                                 methodName);

        EntityDetail attachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                              attachingGUID,
                                                                              attachingElementTypeName,
                                                                              attachingElementEntity,
                                                                              attachingGUIDParameterName,
                                                                              false,
                                                                              suppliedSupportedZones,
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
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID might be set up
                 */
                this.reEvaluateAnchorGUID(startingGUID,
                                          startingElementTypeName,
                                          startingElementEntity,
                                          null,
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
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID may now be set up
                 */
                this.reEvaluateAnchorGUID(attachingGUID,
                                          attachingElementTypeName,
                                          attachingElementEntity,
                                          null,
                                          methodName);
            }
        }
    }


    /**
     * Delete the existing relationship between the starting element and another element then create a new relationship
     * between the starting element element and the new attaching element.
     *
     * If successful this updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the relinking.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param startAtEnd1               is the starting element at end 1 of the relationship
     * @param newAttachingGUID             unique id of the entity for the element that is being attached
     * @param newAttachingGUIDParameterName name of the parameter supplying the newAttachingGUID
     * @param newAttachingElementTypeName  type name of the attaching element's entity
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                          String             attachmentTypeGUID,
                                          String             attachmentTypeName,
                                          InstanceProperties relationshipProperties,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        this.relinkElementToNewElement(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       startingElementTypeName,
                                       startAtEnd1,
                                       newAttachingGUID,
                                       newAttachingGUIDParameterName,
                                       newAttachingElementTypeName,
                                       supportedZones,
                                       attachmentTypeGUID,
                                       attachmentTypeName,
                                       relationshipProperties,
                                       methodName);
    }


    /**
     * Deleted the existing relationship between the starting element and another element then create a new relationship
     * between the starting element element and the new attaching element.
     *
     * If successful this updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the relinking.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param startAtEnd1               is the starting element at end 1 of the relationship
     * @param newAttachingGUID             unique id of the entity for the element that is being attached
     * @param newAttachingGUIDParameterName name of the parameter supplying the newAttachingGUID
     * @param newAttachingElementTypeName  type name of the attaching element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
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
                                          List<String>       suppliedSupportedZones,
                                          String             attachmentTypeGUID,
                                          String             attachmentTypeName,
                                          InstanceProperties relationshipProperties,
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
                                                                                methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             false,
                                                                             suppliedSupportedZones,
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
                                                                                    methodName);

        EntityDetail newAttachingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                                 newAttachingGUID,
                                                                                 newAttachingElementTypeName,
                                                                                 newAttachingElementEntity,
                                                                                 newAttachingGUIDParameterName,
                                                                                 false,
                                                                                 suppliedSupportedZones,
                                                                                 methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         *
         * The next step is to remove the relationship if it exists.
         */
        Relationship  relationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                   startingGUID,
                                                                                   startingElementTypeName,
                                                                                   startAtEnd1,
                                                                                   attachmentTypeGUID,
                                                                                   attachmentTypeName,
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

        repositoryHandler.createRelationship(userId,
                                             attachmentTypeGUID,
                                             externalSourceGUID,
                                             externalSourceName,
                                             startingGUID,
                                             newAttachingGUID,
                                             relationshipProperties,
                                             methodName);


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
                                         methodName);

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(startingGUID,
                                      startingElementTypeName,
                                      startingElementEntity,
                                      startingElementAnchorEntity.getGUID(),
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
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(startingGUID,
                                      startingElementTypeName,
                                      startingElementEntity,
                                      null,
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
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(newAttachingGUID,
                                      newAttachingElementTypeName,
                                      newAttachingElementEntity,
                                      newAttachingElementAnchorEntity.getGUID(),
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
                                             methodName);
            }

            /*
             * Now that this relationship has changed, the anchorGUID may now be wrong
             */
            this.reEvaluateAnchorGUID(newAttachingGUID,
                                      newAttachingElementTypeName,
                                      newAttachingElementEntity,
                                      null,
                                      methodName);
        }
    }


    /**
     * Removes a relationship between two elements.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeGUID  type identifier of the attaching element's entity
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
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
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
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
                                      supportedZones,
                                      attachmentTypeGUID,
                                      attachmentTypeName,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If the attaching element is anchored to the same anchor as the starting element, it is
     * unlinked from all other elements and deleted. This can cause a cascading effect if the anchored elements are organized in a hierarchy such
     * as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to remove
     * @param attachmentTypeName        unique name of type of the relationship to remove
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
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
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
                                      suppliedSupportedZones,
                                      attachmentTypeName,
                                      relationship,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If the attaching element is anchored to the same anchor as the starting element, it is
     * unlinked from all other elements and deleted. This can cause a cascading effect if the anchored elements are organized in a hierarchy such
     * as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param relationship              specific relationship to remove
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
                                         String       attachmentTypeName,
                                         Relationship relationship,
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
                                      supportedZones,
                                      attachmentTypeName,
                                      relationship,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If the attaching element is anchored to the same anchor as the starting element, it is
     * unlinked from all other elements and deleted. This can cause a cascading effect if the anchored elements are organized in a hierarchy such
     * as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param relationship              specific relationship to remove
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
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeName,
                                         Relationship relationship,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachedGUID, attachedGUIDParameterName, methodName);

        EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               startingGUID,
                                                                               startingGUIDParameterName,
                                                                               startingElementTypeName,
                                                                               methodName);

        EntityDetail startingElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             startingGUID,
                                                                             startingElementTypeName,
                                                                             startingElementEntity,
                                                                             startingGUIDParameterName,
                                                                             false,
                                                                             suppliedSupportedZones,
                                                                             methodName);

        String startingElementAnchorGUID = startingGUID;

        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail attachedElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               attachedGUID,
                                                                               attachedGUIDParameterName,
                                                                               attachedElementTypeName,
                                                                               methodName);

        EntityDetail attachedElementAnchorEntity = this.validateAnchorEntity(userId,
                                                                             attachedGUID,
                                                                             attachedElementTypeName,
                                                                             attachedElementEntity,
                                                                             attachedGUIDParameterName,
                                                                             false,
                                                                             suppliedSupportedZones,
                                                                             methodName);

        /*
         * The unlink only occurs if there is a relationship.
         */
        if (relationship != null)
        {
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
                                             methodName);

                /*
                 * Now that this relationship is gone, the anchorGUID may now be wrong
                 */
                this.reEvaluateAnchorGUID(startingGUID,
                                          startingGUIDParameterName,
                                          startingElementTypeName,
                                          startingElementAnchorEntity.getGUID(),
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
                                                 methodName);
                }

                /*
                 * Now that this relationship is gone, the anchorGUID may now be wrong
                 */
                this.reEvaluateAnchorGUID(attachedGUID,
                                          attachedGUIDParameterName,
                                          attachedElementTypeName,
                                          attachedElementAnchorEntity.getGUID(),
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
                                                 methodName);
                }
            }


            /*
             * If the attached element has the same anchor GUID as the starting element then the attached element should be deleted.
             */
            if ((attachedElementAnchorEntity != null) && (startingElementAnchorEntity != null) &&
                        (attachedElementAnchorEntity.getGUID().equals(startingElementAnchorEntity.getGUID())))
            {
                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            attachedGUID,
                                            attachedGUIDParameterName,
                                            attachedElementTypeGUID,
                                            attachedElementTypeName,
                                            null,
                                            null,
                                            attachedElementAnchorEntity,
                                            methodName);
            }
        }
    }


    /**
     * Calls unlinkElementFromElement for all relationships of a certain type emanating from the requested element.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
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
                                  String       attachmentTypeGUID,
                                  String       attachmentTypeName,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        this.unlinkAllElements(userId,
                               onlyCreatorPermitted,
                               externalSourceGUID,
                               externalSourceName,
                               startingGUID,
                               startingGUIDParameterName,
                               startingElementTypeName,
                               supportedZones,
                               attachmentTypeGUID,
                               attachmentTypeName,
                               methodName);
    }


    /**
     * Calls unlinkElementFromElement for all relationships of a certain type emanating from the requested element.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
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
                                  List<String> suppliedSupportedZones,
                                  String       attachmentTypeGUID,
                                  String       attachmentTypeName,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String entityProxyParameterName = "entityProxy.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingElementTypeName,
                                  true,
                                  suppliedSupportedZones,
                                  methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       startingGUID,
                                                                                       startingElementTypeName,
                                                                                       attachmentTypeGUID,
                                                                                       attachmentTypeName,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();
            EntityProxy  entityProxy  = repositoryHandler.getOtherEnd(startingGUID, startingElementTypeName, relationship, methodName);

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
                                              suppliedSupportedZones,
                                              attachmentTypeGUID,
                                              attachmentTypeName,
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
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param detachedElementTypeName   name of type of element that will be detached
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
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         String       detachedElementTypeName,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return unlinkConnectedElement(userId,
                                      onlyCreatorPermitted,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      supportedZones,
                                      attachmentTypeGUID,
                                      attachmentTypeName,
                                      detachedElementTypeName,
                                      methodName);
    }


    /**
     * Removes the relationship of a specific type attached to an entity.  If the connected entity is anchored to the starting entity
     * it is deleted (and linked dependent elements). There should be only one relationship. If there are more, an error is thrown.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param suppliedSupportedZones    list of zones that any asset must be a member of at least one to be visible
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param detachedElementTypeName   name of type of element that will be detached
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
                                         List<String> suppliedSupportedZones,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         String       detachedElementTypeName,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String entityProxyParameterName = "entityProxy.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        this.validateAnchorEntity(userId,
                                  startingGUID,
                                  startingGUIDParameterName,
                                  startingElementTypeName,
                                  true,
                                  suppliedSupportedZones,
                                  methodName);

        List<Relationship> links = this.getAttachmentLinks(userId,
                                                           startingGUID,
                                                           startingGUIDParameterName,
                                                           startingElementTypeName,
                                                           attachmentTypeGUID,
                                                           attachmentTypeName,
                                                           detachedElementTypeName,
                                                           0,
                                                           invalidParameterHandler.getMaxPagingSize(),
                                                           methodName);

        if (links == null)
        {
            return null;
        }

        if (links.size() > 1)
        {
            errorHandler.handleAmbiguousRelationships(startingGUID,
                                                      startingElementTypeName,
                                                      attachmentTypeName,
                                                      links,
                                                      methodName);
        }

        EntityProxy  entityProxy  = repositoryHandler.getOtherEnd(startingGUID, startingElementTypeName, links.get(0), methodName);

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
                                          suppliedSupportedZones,
                                          attachmentTypeGUID,
                                          attachmentTypeName,
                                          methodName);

        }

        return detachedElementGUID;
    }


    /**
     * Verify that the integrator identities are either null or refer to a valid software server capability.
     * These values will be used to set up the
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param methodName calling method
     * @throws InvalidParameterException the integrator GUID or name does not match what is in the metadata repository
     * @throws PropertyServerException problem accessing repositories
     * @throws UserNotAuthorizedException security access problem
     */
    public void verifyExternalSourceIdentity(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String methodName) throws InvalidParameterException,
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
                                                                   OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                   null,
                                                                   null,
                                                                   supportedZones,
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
}
