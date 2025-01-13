/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.ExternalIdentifierConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.NoteConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.NoteLogConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldValuesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
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
    final protected static String elementGUIDParameterName = "elementGUID";

    final InvalidParameterHandler                                             invalidParameterHandler;
    final ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> externalIdentifierHandler;
    private final NoteLogHandler<NoteLogElement>                   noteLogHandler;
    private final NoteHandler<NoteElement>                         noteHandler;

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

        noteLogHandler = new NoteLogHandler<>(new NoteLogConverter<>(repositoryHelper, serviceName, serverName),
                                              NoteLogElement.class,
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

        noteHandler = new NoteHandler<>(new NoteConverter<>(repositoryHelper, serviceName, serverName),
                                        NoteElement.class,
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
        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getExternalScopeGUID() != null))
        {
            return correlationProperties.getExternalScopeGUID();
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
        if ((correlationProperties != null) && (correlationProperties.getExternalScopeGUID() != null))
        {
            return correlationProperties.getExternalScopeGUID();
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
        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getExternalScopeGUID() != null))
        {
            return correlationProperties.getExternalScopeName();
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
        if ((correlationProperties != null) && (correlationProperties.getExternalScopeGUID() != null))
        {
            return correlationProperties.getExternalScopeName();
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
            if ((correlationProperties.getExternalScopeGUID() != null) && (correlationProperties.getExternalIdentifier() != null))
            {
                invalidParameterHandler.validateName(correlationProperties.getExternalScopeName(), assetManagerNameParameterName, methodName);


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
                                                                  correlationProperties.getExternalInstanceTypeName(),
                                                                  correlationProperties.getExternalInstanceCreatedBy(),
                                                                  correlationProperties.getExternalInstanceCreationTime(),
                                                                  correlationProperties.getExternalInstanceLastUpdatedBy(),
                                                                  correlationProperties.getExternalInstanceLastUpdateTime(),
                                                                  correlationProperties.getExternalInstanceVersion(),
                                                                  correlationProperties.getExternalScopeGUID(),
                                                                  assetManagerGUIDParameterName,
                                                                  correlationProperties.getExternalScopeName(),
                                                                  OpenMetadataType.INVENTORY_CATALOG.typeName,
                                                                  OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                  getPermittedSynchronization(correlationProperties.getSynchronizationDirection()),
                                                                  correlationProperties.getSynchronizationDescription(),
                                                                  supportedZones,
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
    private int getPermittedSynchronization(PermittedSynchronization synchronizationDirection)
    {
        int permittedSynchronization = PermittedSynchronization.BOTH_DIRECTIONS.getOrdinal();

        if (synchronizationDirection != null)
        {
            permittedSynchronization = synchronizationDirection.getOrdinal();
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
        int keyPatternOrdinal = KeyPattern.LOCAL_KEY.getOrdinal();

        if (keyPattern != null)
        {
            keyPatternOrdinal = keyPattern.getOrdinal();
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
                    (correlationProperties.getExternalScopeGUID() != null) &&
                    (correlationProperties.getExternalScopeName() != null))
        {
            return externalIdentifierHandler.confirmSynchronization(userId,
                                                                    elementGUID,
                                                                    elementGUIDParameterName,
                                                                    elementTypeName,
                                                                    correlationProperties.getExternalIdentifier(),
                                                                    externalIdentifierParameterName,
                                                                    correlationProperties.getExternalScopeGUID(),
                                                                    scopeGUIDParameterName,
                                                                    correlationProperties.getExternalScopeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    supportedZones,
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
                                                                        supportedZones,
                                                                        assetManagerGUID,
                                                                        OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                        0,
                                                                        invalidParameterHandler.getMaxPagingSize(),
                                                                        forLineage,
                                                                        forDuplicateProcessing,
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
     * @param elementTypeName domain of element
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
                                         String                  elementDomainName,
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
                                                                      elementDomainName,
                                                                      elementQualifiedName,
                                                                      supplementaryProperties.getDisplayName(),
                                                                      supplementaryProperties.getDisplaySummary(),
                                                                      supplementaryProperties.getDisplayDescription(),
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
                                                                      elementDomainName,
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
                                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                      glossaryEntity.getProperties(),
                                                                                      methodName));

            supplementaryProperties.setDisplaySummary(repositoryHelper.getStringProperty(serviceName,
                                                                                         OpenMetadataProperty.SUMMARY.name,
                                                                                         glossaryEntity.getProperties(),
                                                                                         methodName));
            supplementaryProperties.setDisplayDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                                                             glossaryEntity.getProperties(),
                                                                                             methodName));
            supplementaryProperties.setAbbreviation(repositoryHelper.getStringProperty(serviceName,
                                                                                       OpenMetadataProperty.ABBREVIATION.name,
                                                                                       glossaryEntity.getProperties(),
                                                                                       methodName));
            supplementaryProperties.setUsage(repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.USAGE.name,
                                                                                glossaryEntity.getProperties(),
                                                                                methodName));
        }
    }


    /**
     * Write the supplied update description to the revision history note log for the element.
     *
     * @param userId calling user
     * @param elementGUID element to attach the comment to
     * @param elementQualifiedName qualified name of the element (or null if not known)
     * @param elementTypeName type name of element
     * @param updateTitle description of the type of update
     * @param updateDescription comment to attach
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    void updateRevisionHistory(String userId,
                               String elementGUID,
                               String elementQualifiedName,
                               String elementTypeName,
                               String updateTitle,
                               String updateDescription) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String elementParameterName = "elementGUID";
        final String noteLogParameterName = "noteLogGUID";
        final String methodName = "updateRevisionHistory";
        final String noteLogQualifiedNamePrefix = "RevisionHistory:";
        final String noteLogName = "Revision History";
        String noteText = "None";

        if (updateDescription != null)
        {
            noteText = updateDescription;
        }

        List<NoteLogElement> noteLogs = noteLogHandler.getAttachedNoteLogs(userId,
                                                                           elementGUID,
                                                                           elementParameterName,
                                                                           OpenMetadataType.REFERENCEABLE.typeName,
                                                                           0,
                                                                           0,
                                                                           false,
                                                                           false,
                                                                           null,
                                                                           methodName);

        String noteLogGUID = null;

        if (noteLogs != null)
        {
            for (NoteLogElement noteLogElement : noteLogs)
            {
                if ((noteLogElement != null) &&
                    (noteLogElement.getProperties() != null) &&
                    (noteLogElement.getProperties().getQualifiedName() != null) &&
                    (noteLogElement.getElementHeader() != null) &&
                    (noteLogElement.getElementHeader().getGUID() != null))
                {
                    if (noteLogElement.getProperties().getQualifiedName().startsWith(noteLogQualifiedNamePrefix))
                    {
                        noteLogGUID = noteLogElement.getElementHeader().getGUID();
                        break;
                    }
                }
            }
        }

        if (noteLogGUID == null)
        {
            noteLogGUID = noteLogHandler.attachNewNoteLog(userId,
                                                          null,
                                                          null,
                                                          elementGUID,
                                                          elementGUID,
                                                          elementParameterName,
                                                          noteLogQualifiedNamePrefix + elementTypeName + ":" + elementQualifiedName,
                                                          noteLogName,
                                                          null,
                                                          true,
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          null,
                                                          methodName);
        }

        if (noteLogGUID != null)
        {
            noteHandler.attachNewNote(userId,
                                      null,
                                      null,
                                      elementGUID,
                                      noteLogGUID,
                                      noteLogParameterName,
                                      null,
                                      updateTitle,
                                      noteText,
                                      null,
                                      null,
                                      false,
                                      false,
                                      null,
                                      methodName);
        }
    }


    /**
     * Classify the element to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param properties characterizations of the data values stored in the data field
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setElementAsDataField(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      String                        elementGUID,
                                      DataFieldValuesProperties     properties,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime,
                                      String                        methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            externalIdentifierHandler.addDataFieldValuesClassification(userId,
                                                                       elementGUID,
                                                                       elementGUIDParameterName,
                                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                                       properties.getDefaultValue(),
                                                                       properties.getSampleValues(),
                                                                       properties.getDataPattern(),
                                                                       properties.getNamePattern(),
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
        else
        {
            externalIdentifierHandler.addDataFieldValuesClassification(userId,
                                                                       elementGUID,
                                                                       elementGUIDParameterName,
                                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
    }


    /**
     * Remove the data field designation from the element.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearElementAsDataField(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        String                        elementGUID,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        externalIdentifierHandler.removeDataFieldValuesClassification(userId,
                                                                      elementGUID,
                                                                      elementGUIDParameterName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
    }
}
