/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.ExternalReferenceConverter;

import org.odpi.openmetadata.accessservices.assetmanager.converters.ExternalReferenceLinkConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalReferenceHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalReferenceLinkHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ExternalReferenceExchangeHandler is the server side handler for managing externalReference content.
 */
public class ExternalReferenceExchangeHandler extends ExchangeHandlerBase
{
    private final ExternalReferenceHandler<ExternalReferenceElement>         externalReferenceHandler;
    private final ExternalReferenceLinkHandler<ExternalReferenceLinkElement> externalReferenceLinkHandler;

    private final static String externalReferenceGUIDParameterName = "externalReferenceGUID";

    /**
     * Construct the externalReference exchange handler with information needed to work with externalReference related objects
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
    public ExternalReferenceExchangeHandler(String                             serviceName,
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
        super(serviceName,
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

        this.externalReferenceHandler = new ExternalReferenceHandler<>(new ExternalReferenceConverter<>(repositoryHelper, serviceName, serverName),
                                                                       ExternalReferenceElement.class,
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

        this.externalReferenceLinkHandler = new ExternalReferenceLinkHandler<>(new ExternalReferenceLinkConverter<>(repositoryHelper, serviceName, serverName),
                                                                               ExternalReferenceLinkElement.class,
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
    }



    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToExternalReferences(String                         userId,
                                                              String                         assetManagerGUID,
                                                              String                         assetManagerName,
                                                              List<ExternalReferenceElement> results,
                                                              boolean                        forLineage,
                                                              boolean                        forDuplicateProcessing,
                                                              Date                           effectiveTime,
                                                              String                         methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        if (results != null)
        {
            for (ExternalReferenceElement externalReference : results)
            {
                if ((externalReference != null) && (externalReference.getElementHeader() != null) && (externalReference.getElementHeader().getGUID() != null))
                {
                    externalReference.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                          externalReference.getElementHeader().getGUID(),
                                                                                          externalReferenceGUIDParameterName,
                                                                                          OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                          assetManagerGUID,
                                                                                          assetManagerName,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          effectiveTime,
                                                                                          methodName));
                }
            }
        }
    }



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToExternalReferenceLinks(String                             userId,
                                                                  String                             assetManagerGUID,
                                                                  String                             assetManagerName,
                                                                  List<ExternalReferenceLinkElement> results,
                                                                  boolean                            forLineage,
                                                                  boolean                            forDuplicateProcessing,
                                                                  Date                               effectiveTime,
                                                                  String                             methodName) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        if (results != null)
        {
            for (MetadataElement externalReference : results)
            {
                if ((externalReference != null) && (externalReference.getElementHeader() != null) && (externalReference.getElementHeader().getGUID() != null))
                {
                    externalReference.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                          externalReference.getElementHeader().getGUID(),
                                                                                          externalReferenceGUIDParameterName,
                                                                                          OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                          assetManagerGUID,
                                                                                          assetManagerName,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          effectiveTime,
                                                                                          methodName));
                }
            }
        }
    }


    /* ========================================================
     * The ExternalReference entity describes a link to a remote resource such as a document or web page.
     */


    /**
     * Create a new metadata element to represent the root of a externalReference.  All categories and terms are linked
     * to a single externalReference.  They are owned by this externalReference and if the externalReference is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param externalReferenceProperties properties to store
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted (once the external reference is linked to the anchor).
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createExternalReference(String                        userId,
                                          boolean                       assetManagerIsHome,
                                          MetadataCorrelationProperties correlationProperties,
                                          ExternalReferenceProperties   externalReferenceProperties,
                                          String                        anchorGUID,
                                          String                        methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String propertiesParameterName    = "externalReferenceProperties";
        final String qualifiedNameParameterName = "externalReferenceProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);

        invalidParameterHandler.validateObject(externalReferenceProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(externalReferenceProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String externalReferenceGUID = externalReferenceHandler.createExternalReference(userId,
                                                                                        this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                        this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                        anchorGUID,
                                                                                        externalReferenceProperties.getQualifiedName(),
                                                                                        externalReferenceProperties.getDisplayName(),
                                                                                        externalReferenceProperties.getDescription(),
                                                                                        externalReferenceProperties.getUrl(),
                                                                                        externalReferenceProperties.getVersion(),
                                                                                        externalReferenceProperties.getOrganization(),
                                                                                        externalReferenceProperties.getAdditionalProperties(),
                                                                                        externalReferenceProperties.getTypeName(),
                                                                                        externalReferenceProperties.getExtendedProperties(),
                                                                                        externalReferenceProperties.getEffectiveFrom(),
                                                                                        externalReferenceProperties.getEffectiveTo(),
                                                                                        new Date(),
                                                                                        methodName);

        if (externalReferenceGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          externalReferenceGUID,
                                          externalReferenceGUIDParameterName,
                                          OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return externalReferenceGUID;
    }


    /**
     * Update the metadata element representing an external reference.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param externalReferenceGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param externalReferenceProperties new properties for this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateExternalReference(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        String                        externalReferenceGUID,
                                        boolean                       isMergeUpdate,
                                        ExternalReferenceProperties   externalReferenceProperties,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String propertiesParameterName    = "externalReferenceProperties";
        final String qualifiedNameParameterName = "externalReferenceProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(externalReferenceProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(externalReferenceProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        externalReferenceGUID,
                                        externalReferenceGUIDParameterName,
                                        OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        externalReferenceHandler.updateExternalReference(userId,
                                                         this.getExternalSourceGUID(correlationProperties),
                                                         this.getExternalSourceName(correlationProperties),
                                                         externalReferenceGUID,
                                                         externalReferenceGUIDParameterName,
                                                         externalReferenceProperties.getQualifiedName(),
                                                         externalReferenceProperties.getDisplayName(),
                                                         externalReferenceProperties.getDescription(),
                                                         externalReferenceProperties.getUrl(),
                                                         externalReferenceProperties.getVersion(),
                                                         externalReferenceProperties.getOrganization(),
                                                         externalReferenceProperties.getAdditionalProperties(),
                                                         externalReferenceProperties.getTypeName(),
                                                         externalReferenceProperties.getExtendedProperties(),
                                                         externalReferenceProperties.getEffectiveFrom(),
                                                         externalReferenceProperties.getEffectiveTo(),
                                                         isMergeUpdate,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
    }


    /**
     * Remove the metadata element representing a externalReference.  This will delete the externalReference and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param externalReferenceGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeExternalReference(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        String                        externalReferenceGUID,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        externalReferenceGUID,
                                        externalReferenceGUIDParameterName,
                                        OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (correlationProperties != null)
        {
            externalReferenceHandler.removeExternalReference(userId,
                                                             this.getExternalSourceGUID(correlationProperties),
                                                             this.getExternalSourceName(correlationProperties),
                                                             externalReferenceGUID,
                                                             externalReferenceGUIDParameterName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
        }
        else
        {
            externalReferenceHandler.removeExternalReference(userId,
                                                             null,
                                                             null,
                                                             externalReferenceGUID,
                                                             externalReferenceGUIDParameterName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
        }
    }




    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external references
     * @param attachedToGUIDParameterName parameter name
     * @param externalReferenceGUID unique identifier (guid) of the external reference details
     * @param externalReferenceGUIDParameterName parameter name
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return Unique identifier for new relationship
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String linkExternalReferenceToElement(String                          userId,
                                                 String                          assetManagerGUID,
                                                 String                          assetManagerName,
                                                 String                          attachedToGUID,
                                                 String                          attachedToGUIDParameterName,
                                                 String                          externalReferenceGUID,
                                                 String                          externalReferenceGUIDParameterName,
                                                 ExternalReferenceLinkProperties linkProperties,
                                                 boolean                         forLineage,
                                                 boolean                         forDuplicateProcessing,
                                                 Date                            effectiveTime,
                                                 String                          methodName) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, attachedToGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        String relationshipGUID;
        if (linkProperties != null)
        {
            relationshipGUID = externalReferenceLinkHandler.setupExternalReferenceLink(userId,
                                                                                       assetManagerGUID,
                                                                                       assetManagerName,
                                                                                       attachedToGUID,
                                                                                       attachedToGUIDParameterName,
                                                                                       externalReferenceGUID,
                                                                                       externalReferenceGUIDParameterName,
                                                                                       linkProperties.getLinkId(),
                                                                                       linkProperties.getLinkDescription(),
                                                                                       linkProperties.getEffectiveFrom(),
                                                                                       linkProperties.getEffectiveTo(),
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName);
        }
        else
        {
            relationshipGUID = externalReferenceLinkHandler.setupExternalReferenceLink(userId,
                                                                                       assetManagerGUID,
                                                                                       assetManagerName,
                                                                                       attachedToGUID,
                                                                                       attachedToGUIDParameterName,
                                                                                       externalReferenceGUID,
                                                                                       externalReferenceGUIDParameterName,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName);
        }

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataType.REFERENCEABLE_TO_EXT_REF_TYPE_NAME,
                                                          attachedToGUID,
                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                          externalReferenceGUID,
                                                          OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                          methodName);

        return relationshipGUID;
    }


    /**
     * Update the link between an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceLinkGUID unique identifier (guid) of the external reference details
     * @param externalReferenceGUIDParameterName parameter name
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void updateExternalReferenceToElementLink(String                          userId,
                                                     String                          assetManagerGUID,
                                                     String                          assetManagerName,
                                                     String                          externalReferenceLinkGUID,
                                                     String                          externalReferenceGUIDParameterName,
                                                     ExternalReferenceLinkProperties linkProperties,
                                                     boolean                         forLineage,
                                                     boolean                         forDuplicateProcessing,
                                                     Date                            effectiveTime,
                                                     String                          methodName) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceLinkGUID, externalReferenceGUIDParameterName, methodName);

        if (linkProperties != null)
        {
            externalReferenceLinkHandler.updateExternalReferenceLink(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     externalReferenceLinkGUID,
                                                                     externalReferenceGUIDParameterName,
                                                                     linkProperties.getLinkId(),
                                                                     linkProperties.getLinkDescription(),
                                                                     linkProperties.getEffectiveFrom(),
                                                                     linkProperties.getEffectiveTo(),
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        }
        else
        {
            externalReferenceLinkHandler.updateExternalReferenceLink(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     externalReferenceLinkGUID,
                                                                     externalReferenceGUIDParameterName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);
        }

        externalIdentifierHandler.logRelationshipUpdate(assetManagerGUID,
                                                        assetManagerName,
                                                        OpenMetadataType.REFERENCEABLE_TO_EXT_REF_TYPE_NAME,
                                                        externalReferenceLinkGUID,
                                                        methodName);
    }


    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceLinkGUID identifier of the external reference relationship
     * @param externalReferenceGUIDParameterName parameter name
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void unlinkExternalReferenceFromElement(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  externalReferenceLinkGUID,
                                                   String  externalReferenceGUIDParameterName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceLinkGUID, externalReferenceGUIDParameterName, methodName);

        Relationship relationship = externalReferenceLinkHandler.clearExternalReferenceLink(userId,
                                                                                            assetManagerGUID,
                                                                                            assetManagerName,
                                                                                            externalReferenceLinkGUID,
                                                                                            externalReferenceGUIDParameterName,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing,
                                                                                            effectiveTime,
                                                                                            methodName);

        if (relationship != null)
        {
            externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.REFERENCEABLE_TO_EXT_REF_TYPE_NAME,
                                                             relationship.getEntityOneProxy().getGUID(),
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             relationship.getEntityTwoProxy().getGUID(),
                                                             OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                             methodName);
        }
    }


    /**
     * Retrieve the list of externalReference metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement> getExternalReferences(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        List<ExternalReferenceElement> results = externalReferenceHandler.getExternalReferences(userId,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                methodName);

        addCorrelationPropertiesToExternalReferences(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }




    /**
     * Retrieve the list of externalReference metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter for search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement> findExternalReferences(String  userId,
                                                                 String  assetManagerGUID,
                                                                 String  assetManagerName,
                                                                 String  searchString,
                                                                 String  searchStringParameterName,
                                                                 int     startFrom,
                                                                 int     pageSize,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        List<ExternalReferenceElement> results = externalReferenceHandler.findExternalReferences(userId,
                                                                                                 searchString,
                                                                                                 searchStringParameterName,
                                                                                                 startFrom,
                                                                                                 pageSize,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing,
                                                                                                 effectiveTime,
                                                                                                 methodName);

        addCorrelationPropertiesToExternalReferences(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the list of externalReference metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param nameParameterName name of parameter supplying name value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement>   getExternalReferencesByName(String  userId,
                                                                        String  assetManagerGUID,
                                                                        String  assetManagerName,
                                                                        String  name,
                                                                        String  nameParameterName,
                                                                        int     startFrom,
                                                                        int     pageSize,
                                                                        boolean forLineage,
                                                                        boolean forDuplicateProcessing,
                                                                        Date    effectiveTime,
                                                                        String  methodName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        List<ExternalReferenceElement> results = externalReferenceHandler.getExternalReferencesByName(userId,
                                                                                                      name,
                                                                                                      nameParameterName,
                                                                                                      startFrom,
                                                                                                      pageSize,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      methodName);

        addCorrelationPropertiesToExternalReferences(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }



    /**
     * Retrieve the list of externalReference metadata elements with a matching qualified or display referenceId.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique referenceId of software server capability representing the caller
     * @param referenceId referenceId to search for
     * @param referenceIdParameterName name of parameter supplying referenceId value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement>   getExternalReferencesById(String  userId,
                                                                      String  assetManagerGUID,
                                                                      String  assetManagerName,
                                                                      String  referenceId,
                                                                      String  referenceIdParameterName,
                                                                      int     startFrom,
                                                                      int     pageSize,
                                                                      boolean forLineage,
                                                                      boolean forDuplicateProcessing,
                                                                      Date    effectiveTime,
                                                                      String  methodName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        List<ExternalReferenceElement> results = externalReferenceHandler.getExternalReferencesById(userId,
                                                                                                    referenceId,
                                                                                                    referenceIdParameterName,
                                                                                                    startFrom,
                                                                                                    pageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing,
                                                                                                    effectiveTime,
                                                                                                    methodName);

        addCorrelationPropertiesToExternalReferences(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }




    /**
     * Retrieve the list of externalReference metadata elements with a matching qualified or display url.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique url of software server capability representing the caller
     * @param url url to search for
     * @param urlParameterName name of parameter supplying url value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement>   getExternalReferencesByURL(String  userId,
                                                                       String  assetManagerGUID,
                                                                       String  assetManagerName,
                                                                       String  url,
                                                                       String  urlParameterName,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing,
                                                                       Date    effectiveTime,
                                                                       String  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        List<ExternalReferenceElement> results = externalReferenceHandler.getExternalReferencesByURL(userId,
                                                                                                     url,
                                                                                                     urlParameterName,
                                                                                                     startFrom,
                                                                                                     pageSize,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveTime,
                                                                                                     methodName);

        addCorrelationPropertiesToExternalReferences(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the list of external references created by this caller.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement>   getExternalReferencesForAssetManager(String  userId,
                                                                                 String  assetManagerGUID,
                                                                                 String  assetManagerName,
                                                                                 int     startFrom,
                                                                                 int     pageSize,
                                                                                 boolean forLineage,
                                                                                 boolean forDuplicateProcessing,
                                                                                 Date    effectiveTime,
                                                                                 String  methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";
        final String externalReferenceEntityParameterName = "externalReferenceEntity";
        final String externalReferenceGUIDParameterName = "externalReferenceEntity.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);

        List<ExternalReferenceElement> results = new ArrayList<>();

        List<EntityDetail> externalReferenceEntities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                                            assetManagerGUID,
                                                                                                            assetManagerGUIDParameterName,
                                                                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                                            OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                                            startFrom,
                                                                                                            pageSize,
                                                                                                            effectiveTime,
                                                                                                            forLineage,
                                                                                                            forDuplicateProcessing,
                                                                                                            methodName);

        if (externalReferenceEntities != null)
        {
            for (EntityDetail externalReferenceEntity : externalReferenceEntities)
            {
                if (externalReferenceEntity != null)
                {
                    ExternalReferenceElement externalReferenceElement = externalReferenceHandler.getBeanFromEntity(userId,
                                                                                                                   externalReferenceEntity,
                                                                                                                   externalReferenceEntityParameterName,
                                                                                                                   methodName);

                    if (externalReferenceElement != null)
                    {
                        externalReferenceElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                                     externalReferenceEntity.getGUID(),
                                                                                                     externalReferenceGUIDParameterName,
                                                                                                     OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                                     assetManagerGUID,
                                                                                                     assetManagerName,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveTime,
                                                                                                     methodName));

                        results.add(externalReferenceElement);
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
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external reference
     * @param attachedToGUIDParameterName name of attachedToGUID parameter
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceLinkElement> retrieveAttachedExternalReferences(String  userId,
                                                                                 String  assetManagerGUID,
                                                                                 String  assetManagerName,
                                                                                 String  attachedToGUID,
                                                                                 String  attachedToGUIDParameterName,
                                                                                 int     startFrom,
                                                                                 int     pageSize,
                                                                                 boolean forLineage,
                                                                                 boolean forDuplicateProcessing,
                                                                                 Date    effectiveTime,
                                                                                 String  methodName) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        List<ExternalReferenceLinkElement> results = externalReferenceLinkHandler.getExternalReferences(userId,
                                                                                                        attachedToGUID,
                                                                                                        attachedToGUIDParameterName,
                                                                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                                                                        supportedZones,
                                                                                                        startFrom,
                                                                                                        pageSize,
                                                                                                        forLineage,
                                                                                                        forDuplicateProcessing,
                                                                                                        effectiveTime,
                                                                                                        methodName);

        addCorrelationPropertiesToExternalReferenceLinks(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         results,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);

        return results;
    }



    /**
     * Retrieve the externalReference metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName name of parameter for guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ExternalReferenceElement getExternalReferenceByGUID(String  userId,
                                                               String  assetManagerGUID,
                                                               String  assetManagerName,
                                                               String  guid,
                                                               String  guidParameterName,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing,
                                                               Date    effectiveTime,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        ExternalReferenceElement externalReference = externalReferenceHandler.getBeanFromRepository(userId,
                                                                                                    guid,
                                                                                                    guidParameterName,
                                                                                                    OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                                    false,
                                                                                                    false,
                                                                                                    effectiveTime,
                                                                                                    methodName);

        if (externalReference != null)
        {
            externalReference.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                  guid,
                                                                                  guidParameterName,
                                                                                  OpenMetadataType.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                                  assetManagerGUID,
                                                                                  assetManagerName,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName));
        }

        return externalReference;
    }


}
