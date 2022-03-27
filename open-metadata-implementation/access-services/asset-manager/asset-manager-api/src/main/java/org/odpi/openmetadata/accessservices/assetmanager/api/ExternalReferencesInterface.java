/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;


/**
 * The ExternalReferencesInterface is used by the governance team to define external references to elements within the governance program.
 */
public interface ExternalReferencesInterface
{
    /**
     * Create a definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param referenceExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param referenceExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param referenceExternalIdentifierSource component that issuing this request.
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
    String createExternalReference(String                      userId,
                                   String                      assetManagerGUID,
                                   String                      assetManagerName,
                                   boolean                     assetManagerIsHome,
                                   String                      referenceExternalIdentifier,
                                   String                      referenceExternalIdentifierName,
                                   String                      referenceExternalIdentifierUsage,
                                   String                      referenceExternalIdentifierSource,
                                   KeyPattern                  referenceExternalIdentifierKeyPattern,
                                   Map<String, String>         mappingProperties,
                                   String                      anchorGUID,
                                   ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Update the definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateExternalReference(String                      userId,
                                 String                      assetManagerGUID,
                                 String                      assetManagerName,
                                 String                      externalReferenceGUID,
                                 String                      referenceExternalIdentifier,
                                 boolean                     isMergeUpdate,
                                 ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Remove the definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteExternalReference(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String externalReferenceGUID,
                                 String referenceExternalIdentifier) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external references.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void linkExternalReferenceToElement(String                          userId,
                                        String                          assetManagerGUID,
                                        String                          assetManagerName,
                                        String                          attachedToGUID,
                                        String                          externalReferenceGUID,
                                        ExternalReferenceLinkProperties linkProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Remove the link between a external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void unlinkExternalReferenceFromElement(String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String attachedToGUID,
                                            String externalReferenceGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


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
    ExternalReferenceElement getExternalReferenceByGUID(String userId,
                                                        String assetManagerGUID,
                                                        String assetManagerName,
                                                        String externalReferenceGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;

    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
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
    List<ExternalReferenceElement> getExternalReferencesById(String userId,
                                                             String assetManagerGUID,
                                                             String assetManagerName,
                                                             String resourceId,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;

    /**
     * Retrieve the list of external references for this URL.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
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
    List<ExternalReferenceElement> getExternalReferencesByURL(String userId,
                                                              String assetManagerGUID,
                                                              String assetManagerName,
                                                              String url,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException;


    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ExternalReferenceElement> getExternalReferencesForAssetManager(String userId,
                                                                        String assetManagerGUID,
                                                                        String assetManagerName,
                                                                        int    startFrom,
                                                                        int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
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
    List<ExternalReferenceElement> findExternalReferences(String userId,
                                                          String assetManagerGUID,
                                                          String assetManagerName,
                                                          String searchString,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
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
    List<ExternalReferenceLinkElement> retrieveAttachedExternalReferences(String userId,
                                                                          String assetManagerGUID,
                                                                          String assetManagerName,
                                                                          String attachedToGUID,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException;
}
