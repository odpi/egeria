/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.ExternalReferenceExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;


/**
 * DataAssetExchangeService is the context for managing data assets and associated elements such as schemas.
 */
public class ExternalReferenceExchangeService
{
    private ExternalReferenceExchangeClient externalReferenceClient;
    private String                          userId;
    private String                          assetManagerGUID;
    private String                          assetManagerName;
    private String                          connectorName;
    private SynchronizationDirection        synchronizationDirection;
    private AuditLog                        auditLog;

    private boolean assetManagerIsHome = true;

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
     * External references provide links to external sources.
     */


    /**
     * Create a definition of a external reference.
     *
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param referenceExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param referenceExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param referenceExternalIdentifierKeyPattern  pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
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
    public String createExternalReference(String                      referenceExternalIdentifier,
                                          String                      referenceExternalIdentifierName,
                                          String                      referenceExternalIdentifierUsage,
                                          KeyPattern                  referenceExternalIdentifierKeyPattern,
                                          Map<String, String>         mappingProperties,
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
                                                                   referenceExternalIdentifier,
                                                                   referenceExternalIdentifierName,
                                                                   referenceExternalIdentifierUsage,
                                                                   connectorName,
                                                                   referenceExternalIdentifierKeyPattern,
                                                                   mappingProperties,
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
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateExternalReference(String                      externalReferenceGUID,
                                        String                      referenceExternalIdentifier,
                                        boolean                     isMergeUpdate,
                                        ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateExternalReference";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.updateExternalReference(userId, assetManagerGUID, assetManagerName, externalReferenceGUID, referenceExternalIdentifier, isMergeUpdate, properties);
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
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deleteExternalReference(String externalReferenceGUID,
                                        String referenceExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "deleteExternalReference";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.deleteExternalReference(userId, assetManagerGUID, assetManagerName, externalReferenceGUID, referenceExternalIdentifier);
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
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void linkExternalReferenceToElement(String                          attachedToGUID,
                                               String                          externalReferenceGUID,
                                               ExternalReferenceLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkExternalReferenceToElement";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.linkExternalReferenceToElement(userId, assetManagerGUID, assetManagerName, attachedToGUID, externalReferenceGUID, linkProperties);
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
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void unlinkExternalReferenceFromElement(String attachedToGUID,
                                                   String externalReferenceGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "unlinkExternalReferenceFromElement";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            externalReferenceClient.unlinkExternalReferenceFromElement(userId, assetManagerGUID, assetManagerName, attachedToGUID, externalReferenceGUID);
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
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier for the external reference
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException externalReferenceGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ExternalReferenceElement getExternalReferenceByGUID(String userId,
                                                               String assetManagerGUID,
                                                               String assetManagerName,
                                                               String externalReferenceGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return externalReferenceClient.getExternalReferenceByGUID(userId, assetManagerGUID, assetManagerName, externalReferenceGUID);
    }


    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param resourceId unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
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
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferencesById(userId, assetManagerGUID, assetManagerName, resourceId, startFrom, pageSize);
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param url URL of the external resource.
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
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return externalReferenceClient.getExternalReferencesByURL(userId, assetManagerGUID, assetManagerName, url, startFrom, pageSize);
    }


    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalReferenceElement> getExternalReferencesForAssetManager(int    startFrom,
                                                                               int    pageSize) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return externalReferenceClient.getExternalReferencesForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize);
    }


    /**
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param searchString regular expression (RegEx) to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceElement> findExternalReferences(String searchString,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return externalReferenceClient.findExternalReferences(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReferenceLinkElement> retrieveAttachedExternalReferences(String attachedToGUID,
                                                                                 int    startFrom,
                                                                                 int    pageSize) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return externalReferenceClient.retrieveAttachedExternalReferences(userId, assetManagerGUID, assetManagerName, attachedToGUID, startFrom, pageSize);
    }
}
