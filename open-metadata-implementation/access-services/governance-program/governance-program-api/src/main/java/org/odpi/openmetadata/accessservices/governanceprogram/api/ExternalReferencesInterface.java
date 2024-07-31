/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;

import java.util.List;


/**
 * The ExternalReferencesInterface is used by the governance team to define external references to elements within the governance program.
 */
public interface ExternalReferencesInterface
{
    /**
     * Create a definition of an external reference.
     *
     * @param userId calling user
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
                                   String                      anchorGUID,
                                   ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Update the definition of an external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateExternalReference(String                      userId,
                                 String                      externalReferenceGUID,
                                 boolean                     isMergeUpdate,
                                 ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Remove the definition of an external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteExternalReference(String userId,
                                 String externalReferenceGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void linkExternalReferenceToElement(String                          userId,
                                        String                          attachedToGUID,
                                        String                          externalReferenceGUID,
                                        ExternalReferenceLinkProperties linkProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void unlinkExternalReferenceFromElement(String userId,
                                            String attachedToGUID,
                                            String externalReferenceGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException externalReferenceGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ExternalReferenceElement getExternalReferenceByGUID(String userId,
                                                        String externalReferenceGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;

    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param userId the name of the calling user.
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
    List<ExternalReferenceElement> findExternalReferencesById(String userId,
                                                              String resourceId,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException;

    /**
     * Retrieve the list of external references for this URL.
     *
     * @param userId the name of the calling user.
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
                                                              String url,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException;


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param userId the name of the calling user.
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
    List<ExternalReferenceElement> retrieveAttachedExternalReferences(String userId,
                                                                      String attachedToGUID,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException;


    /**
     * Return information about the elements linked to a externalReference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the externalReference
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the related elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getElementsForExternalReference(String userId,
                                                         String externalReferenceGUID,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;
}
