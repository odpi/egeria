/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.ExternalReferenceExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * DataAssetExchangeService is the context for managing data assets and associated elements such as schemas.
 */
public class ExternalReferenceExchangeService
{
    private final ExternalReferenceExchangeClient externalReferenceClient;
    private final String                          userId;
    private final String                          assetManagerGUID;
    private final String                          assetManagerName;
    private final String                          connectorName;
    private final SynchronizationDirection        synchronizationDirection;
    private final AuditLog                        auditLog;

    private boolean assetManagerIsHome = true;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param externalReferenceClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    ExternalReferenceExchangeService(ExternalReferenceExchangeClient externalReferenceClient,
                                     SynchronizationDirection        synchronizationDirection,
                                     String                          userId,
                                     String                          assetManagerGUID,
                                     String                          assetManagerName,
                                     String                          connectorName,
                                     AuditLog                        auditLog)
    {

        this.externalReferenceClient  = externalReferenceClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }


    /* ========================================================
     * Set up whether metadata is owned by the asset manager
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this asset manager. Default is true.
     *
     * @param assetManagerIsHome should the metadata be marked as owned by the infrastructure manager so others can not update?
     */
    public void setAssetManagerIsHome(boolean assetManagerIsHome)
    {
        this.assetManagerIsHome = assetManagerIsHome;
    }



    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /* ========================================================
     * External references provide links to external sources.
     */


    /**
     * Create a definition of a external reference.
     *
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted (once the external reference is linked to the anchor).
     * @param properties properties for a external reference
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createExternalReference(ExternalIdentifierProperties externalIdentifierProperties,
                                          String                      anchorGUID,
                                          ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createExternalReference";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return externalReferenceClient.createExternalReference(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   assetManagerIsHome,
                                                                   externalIdentifierProperties,
                                                                   anchorGUID,
                                                                   properties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the definition of a external reference.
     *
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateExternalReference(String                      externalReferenceGUID,
                                        String                      referenceExternalIdentifier,
                                        boolean                     isMergeUpdate,
                                        ExternalReferenceProperties properties,
                                        Date                        effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "updateExternalReference";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.updateExternalReference(userId, assetManagerGUID, assetManagerName, externalReferenceGUID, referenceExternalIdentifier, isMergeUpdate, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the definition of a external reference.
     *
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteExternalReference(String externalReferenceGUID,
                                        String referenceExternalIdentifier,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "deleteExternalReference";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.deleteExternalReference(userId, assetManagerGUID, assetManagerName, externalReferenceGUID, referenceExternalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link an external reference to an object.
     *
     * @param attachedToGUID object linked to external references.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return Unique identifier for new relationship
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public String linkExternalReferenceToElement(String                          attachedToGUID,
                                                 String                          externalReferenceGUID,
                                                 ExternalReferenceLinkProperties linkProperties,
                                                 Date                            effectiveTime) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "linkExternalReferenceToElement";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return externalReferenceClient.linkExternalReferenceToElement(userId, assetManagerGUID, assetManagerName, assetManagerIsHome, attachedToGUID, externalReferenceGUID, linkProperties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }



    /**
     * Update the link between an external reference to an object.
     *
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceLinkGUID unique identifier (guid) of the external reference details.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void updateExternalReferenceToElementLink(String                          externalReferenceLinkGUID,
                                                     ExternalReferenceLinkProperties linkProperties,
                                                     Date                            effectiveTime) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkExternalReferenceToElement";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.updateExternalReferenceToElementLink(userId, assetManagerGUID, assetManagerName, externalReferenceLinkGUID, linkProperties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the link between a external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param externalReferenceLinkGUID identifier of the external reference relationship.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void unlinkExternalReferenceFromElement(String externalReferenceLinkGUID,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "unlinkExternalReferenceFromElement";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.unlinkExternalReferenceFromElement(userId, assetManagerGUID, assetManagerName, externalReferenceLinkGUID, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of external references sorted in open metadata.
     *
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> getExternalReferences(int  startFrom,
                                                                int  pageSize,
                                                                Date effectiveTime) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferences(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param resourceId unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> getExternalReferencesById(String resourceId,
                                                                    int    startFrom,
                                                                    int    pageSize,
                                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferencesById(userId, assetManagerGUID, assetManagerName, resourceId, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param url URL of the external resource.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> getExternalReferencesByURL(String url,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferencesByURL(userId, assetManagerGUID, assetManagerName, url, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }




    /**
     * Retrieve the list of external references for this name.
     *
     * @param name qualifiedName or displayName of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> getExternalReferencesByName(String name,
                                                                      int    startFrom,
                                                                      int    pageSize,
                                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferencesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }

    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement> getExternalReferencesForAssetManager(int    startFrom,
                                                                               int    pageSize,
                                                                               Date   effectiveTime) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        return externalReferenceClient.getExternalReferencesForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param searchString regular expression (RegEx) to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> findExternalReferences(String searchString,
                                                                 int    startFrom,
                                                                 int    pageSize,
                                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return externalReferenceClient.findExternalReferences(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceLinkElement> retrieveAttachedExternalReferences(String attachedToGUID,
                                                                                 int    startFrom,
                                                                                 int    pageSize,
                                                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        return externalReferenceClient.retrieveAttachedExternalReferences(userId, assetManagerGUID, assetManagerName, attachedToGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about a specific external reference.
     *
     * @param externalReferenceGUID unique identifier for the external reference
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException externalReferenceGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ExternalReferenceElement getExternalReferenceByGUID(String externalReferenceGUID,
                                                               Date   effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return externalReferenceClient.getExternalReferenceByGUID(userId, assetManagerGUID, assetManagerName, externalReferenceGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }

}
