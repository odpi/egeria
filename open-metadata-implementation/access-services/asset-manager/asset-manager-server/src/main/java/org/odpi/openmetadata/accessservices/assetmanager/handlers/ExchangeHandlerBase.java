/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.ExternalIdentifierConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * ExchangeHandlerBase is the server side handler for managing the external identifiers and related correlators
 * as well as the supplementary properties for technical metadata elements.
 */
class ExchangeHandlerBase
{
    InvalidParameterHandler                                             invalidParameterHandler;
    ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> externalIdentifierHandler;

    String               serviceName;
    String               serverName;
    OMRSRepositoryHelper repositoryHelper;
    RepositoryHandler    repositoryHandler;
    List<String>         supportedZones;

    /**
     * Construct the base exchange handler with information needed to work with the external identifiers
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    ExchangeHandlerBase(String                             serviceName,
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
        externalIdentifierHandler = new ExternalIdentifierHandler<>(new ExternalIdentifierConverter<>(repositoryHelper, serviceName, serverName),
                                                                    MetadataCorrelationHeader.class,
                                                                    new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName),
                                                                    ElementHeader.class,
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

        this.invalidParameterHandler = invalidParameterHandler;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.supportedZones = supportedZones;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }


    /* ========================================================
     * Setting up whether the element is an external element (ie homed in the external asset manager) or local to the cohort.
     */

    /**
     * Retrieve the external source unique identifier from the correlation properties if available or needed.
     * Otherwise return null;
     *
     * @param correlationProperties correlation properties containing the asset manager information
     * @param assetManagerIsHome true if this element should be homed in the external asset manager - ie an external element
     * @return unique identifier or null
     */
    String getExternalSourceGUID(MetadataCorrelationProperties correlationProperties,
                                 boolean                       assetManagerIsHome)
    {
        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            return correlationProperties.getAssetManagerGUID();
        }

        return null;
    }


    /**
     * Retrieve the external source unique identifier if needed.
     * Otherwise, return null.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerIsHome true if this element should be homed in the external asset manager - ie an external element
     * @return unique name or null
     */
    String getExternalSourceGUID(String  assetManagerGUID,
                                 boolean assetManagerIsHome)
    {
        if (assetManagerIsHome)
        {
            return assetManagerGUID;
        }

        return null;
    }


    /**
     * Retrieve the external source unique identifier from the correlation properties if available or needed.
     * Otherwise return null;
     *
     * @param correlationProperties correlation properties containing the asset manager information
     * @return unique identifier or null
     */
    String getExternalSourceGUID(MetadataCorrelationProperties correlationProperties)
    {
        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            return correlationProperties.getAssetManagerGUID();
        }

        return null;
    }


    /**
     * Retrieve the external source unique name from the correlation properties if available or needed.
     * Otherwise, return null.
     *
     * @param correlationProperties correlation properties containing the asset manager information
     * @param assetManagerIsHome true if this element should be homed in the external asset manager - ie an external element
     * @return unique name or null
     */
    String getExternalSourceName(MetadataCorrelationProperties correlationProperties,
                                 boolean                       assetManagerIsHome)
    {
        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            return correlationProperties.getAssetManagerName();
        }

        return null;
    }


    /**
     * Retrieve the external source unique name if needed.
     * Otherwise, return null.
     *
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome true if this element should be homed in the external asset manager - ie an external element
     * @return unique name or null
     */
    String getExternalSourceName(String  assetManagerName,
                                 boolean assetManagerIsHome)
    {
        if (assetManagerIsHome)
        {
            return assetManagerName;
        }

        return null;
    }


    /**
     * Retrieve the external source unique name from the correlation properties if available or needed.
     * Otherwise, return null.
     *
     * @param correlationProperties correlation properties containing the asset manager information
     * @return unique name or null
     */
    String getExternalSourceName(MetadataCorrelationProperties correlationProperties)
    {
        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            return correlationProperties.getAssetManagerName();
        }

        return null;
    }


    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */


    /**
     * Save the external identifier and related correlation properties.
     *
     * @param userId calling user
     * @param elementGUID open metadata identifier of the element that is to be linked to the external identifier
     * @param elementGUIDParameterName name of the open metadata identifier
     * @param elementTypeName type name of the open metadata element
     * @param correlationProperties properties to store in the external identifier
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    void createExternalIdentifier(String                        userId,
                                  String                        elementGUID,
                                  String                        elementGUIDParameterName,
                                  String                        elementTypeName,
                                  MetadataCorrelationProperties correlationProperties,
                                  boolean                       forLineage,
                                  boolean                       forDuplicateProcessing,
                                  Date                          effectiveTime,
                                  String                        methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String guidParameterName             = "elementGUID";
        final String typeNameParameterName         = "elementTypeName";
        final String assetManagerGUIDParameterName = "correlationProperties.assetManagerGUID";
        final String assetManagerNameParameterName = "correlationProperties.assetManagerName";
        final String identifierParameterName       = "correlationProperties.externalIdentifier";

        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(elementTypeName, typeNameParameterName, methodName);

        if (correlationProperties != null)
        {
            if ((correlationProperties.getAssetManagerGUID() != null) && (correlationProperties.getExternalIdentifier() != null))
            {
                invalidParameterHandler.validateName(correlationProperties.getAssetManagerName(), assetManagerNameParameterName, methodName);


                externalIdentifierHandler.setUpExternalIdentifier(userId,
                                                                  elementGUID,
                                                                  elementGUIDParameterName,
                                                                  elementTypeName,
                                                                  correlationProperties.getExternalIdentifier(),
                                                                  identifierParameterName,
                                                                  getKeyPattern(correlationProperties.getKeyPattern()),
                                                                  correlationProperties.getExternalIdentifierName(),
                                                                  correlationProperties.getExternalIdentifierUsage(),
                                                                  correlationProperties.getExternalIdentifierSource(),
                                                                  correlationProperties.getMappingProperties(),
                                                                  correlationProperties.getAssetManagerGUID(),
                                                                  assetManagerGUIDParameterName,
                                                                  correlationProperties.getAssetManagerName(),
                                                                  OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                  getPermittedSynchronization(correlationProperties.getSynchronizationDirection()),
                                                                  correlationProperties.getSynchronizationDescription(),
                                                                  null,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);
            }
        }
    }


    /**
     * Retrieve the synchronization direction
     *
     * @param synchronizationDirection supplied direction
     * @return open metadata type ordinal - defaulting to "BOTH_DIRECTIONS"
     */
    private int getPermittedSynchronization(SynchronizationDirection synchronizationDirection)
    {
        int permittedSynchronization = SynchronizationDirection.BOTH_DIRECTIONS.getOpenTypeOrdinal();

        if (synchronizationDirection != null)
        {
            permittedSynchronization = synchronizationDirection.getOpenTypeOrdinal();
        }

        return permittedSynchronization;
    }


    /**
     * Retrieve the key pattern ordinal
     *
     * @param keyPattern supplied value
     * @return open metadata type ordinal - defaulting to "LOCAL_KEY"
     */
    private int getKeyPattern(KeyPattern keyPattern)
    {
        int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOpenTypeOrdinal();

        if (keyPattern != null)
        {
            keyPatternOrdinal = keyPattern.getOpenTypeOrdinal();
        }

        return keyPatternOrdinal;
    }


    /**
     * Retrieve the external identifier and check it is correct.
     *
     * @param userId calling user
     * @param elementGUID open metadata identifier of the element that is to be linked to the external identifier
     * @param elementGUIDParameterName name of the open metadata identifier
     * @param elementTypeName type name of the open metadata element
     * @param correlationProperties properties to store in the external identifier
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     * @return external identity (or null if none associated)
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    EntityDetail validateExternalIdentifier(String                        userId,
                                            String                        elementGUID,
                                            String                        elementGUIDParameterName,
                                            String                        elementTypeName,
                                            MetadataCorrelationProperties correlationProperties,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            Date                          effectiveTime,
                                            String                        methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String externalIdentifierParameterName = "correlationProperties.getExternalIdentifier()";
        final String scopeGUIDParameterName          = "correlationProperties.getAssetManagerGUID()";

        if ((correlationProperties != null) &&
                    (correlationProperties.getExternalIdentifier() != null) &&
                    (correlationProperties.getAssetManagerGUID() != null) &&
                    (correlationProperties.getAssetManagerName() != null))
        {
            return externalIdentifierHandler.confirmSynchronization(userId,
                                                                    elementGUID,
                                                                    elementGUIDParameterName,
                                                                    elementTypeName,
                                                                    correlationProperties.getExternalIdentifier(),
                                                                    externalIdentifierParameterName,
                                                                    correlationProperties.getAssetManagerGUID(),
                                                                    scopeGUIDParameterName,
                                                                    correlationProperties.getAssetManagerName(),
                                                                    OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Retrieve the external identifier for the supplied asset manager to pass to the caller. It is OK if there is no
     * external identifier since this is used for retrieve requests.
     *
     * @param userId calling user
     * @param elementGUID open metadata identifier of the element that is to be linked to the external identifier
     * @param elementGUIDParameterName name of the open metadata identifier
     * @param elementTypeName type name of the open metadata element
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     * @return list of correlation properties
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    List<MetadataCorrelationHeader> getCorrelationProperties(String  userId,
                                                             String  elementGUID,
                                                             String  elementGUIDParameterName,
                                                             String  elementTypeName,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing,
                                                             Date    effectiveTime,
                                                             String  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return externalIdentifierHandler.getExternalIdentifiersForScope(userId,
                                                                        elementGUID,
                                                                        elementGUIDParameterName,
                                                                        elementTypeName,
                                                                        assetManagerGUID,
                                                                        OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        0,
                                                                        invalidParameterHandler.getMaxPagingSize(),
                                                                        effectiveTime,
                                                                        methodName);
    }


    /* ========================================================
     * Managing the supplementary properties for technical metadata (assets, software server capabilities etc).
     */


    /**
     * Maintain the supplementary properties in a glossary term linked to the supplied element.
     * The glossary term needs to be connected to the
     * @param userId calling user
     * @param elementGUID unique identifier for the element connected to the supplementary properties
     * @param elementGUIDParameterName name of guid parameter
     * @param elementTypeName type of element
     * @param elementQualifiedName unique name for the element connected to the supplementary properties
     * @param supplementaryProperties properties to save
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    void maintainSupplementaryProperties(String                  userId,
                                         String                  elementGUID,
                                         String                  elementGUIDParameterName,
                                         String                  elementTypeName,
                                         String                  elementQualifiedName,
                                         SupplementaryProperties supplementaryProperties,
                                         boolean                 isMergeUpdate,
                                         boolean                 forLineage,
                                         boolean                 forDuplicateProcessing,
                                         Date                    effectiveTime,
                                         String                  methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        if (supplementaryProperties != null)
        {
            externalIdentifierHandler.maintainSupplementaryProperties(userId,
                                                                      elementGUID,
                                                                      elementGUIDParameterName,
                                                                      elementTypeName,
                                                                      elementQualifiedName,
                                                                      supplementaryProperties.getDisplayName(),
                                                                      supplementaryProperties.getSummary(),
                                                                      supplementaryProperties.getDescription(),
                                                                      supplementaryProperties.getAbbreviation(),
                                                                      supplementaryProperties.getUsage(),
                                                                      isMergeUpdate,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
        }
        else if (! isMergeUpdate)
        {
            externalIdentifierHandler.maintainSupplementaryProperties(userId,
                                                                      elementGUID,
                                                                      elementGUIDParameterName,
                                                                      elementTypeName,
                                                                      elementQualifiedName,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      false,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
        }
    }


    /**
     * Retrieve the supplementary properties for an element.
     *
     * @param elementGUID unique identifier for the element connected to the supplementary properties
     * @param elementGUIDParameterName name of guid parameter
     * @param elementTypeName type of element
     * @param supplementaryProperties properties to save
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    void getSupplementaryProperties(String                  elementGUID,
                                    String                  elementGUIDParameterName,
                                    String                  elementTypeName,
                                    SupplementaryProperties supplementaryProperties,
                                    boolean                 forLineage,
                                    boolean                 forDuplicateProcessing,
                                    Date                    effectiveTime,
                                    String                  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        EntityDetail glossaryEntity = externalIdentifierHandler.getSupplementaryProperties(elementGUID,
                                                                                           elementGUIDParameterName,
                                                                                           elementTypeName,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           effectiveTime,
                                                                                           methodName);

        if ((glossaryEntity != null) && (glossaryEntity.getProperties() != null))
        {
            supplementaryProperties.setDisplayName(repositoryHelper.getStringProperty(serviceName,
                                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                                      glossaryEntity.getProperties(),
                                                                                      methodName));

            supplementaryProperties.setSummary(repositoryHelper.getStringProperty(serviceName,
                                                                                  OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
                                                                                  glossaryEntity.getProperties(),
                                                                                  methodName));
            supplementaryProperties.setDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                      glossaryEntity.getProperties(),
                                                                                      methodName));
            supplementaryProperties.setAbbreviation(repositoryHelper.getStringProperty(serviceName,
                                                                                       OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
                                                                                       glossaryEntity.getProperties(),
                                                                                       methodName));
            supplementaryProperties.setUsage(repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                                glossaryEntity.getProperties(),
                                                                                methodName));
        }
    }
}
