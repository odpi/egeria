/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ExternalReference;

import java.util.List;
import java.util.Map;

/**
 * ExternalReferenceManagementInterface defines the Java API for managing external references.
 * External references are links to external resources such as documents and APIs.
 */
public interface ExternalReferenceManagementInterface
{
    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param userId the name of the calling user.
     * @param resourceId unique reference id assigned by the resource owner (supports wildcards).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<ExternalReference> findExternalReferencesById(String     userId,
                                                       String     resourceId,
                                                       int        startFrom,
                                                       int        pageSize) throws InvalidParameterException,
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
    List<ExternalReference> findExternalReferencesByURL(String     userId,
                                                        String     url,
                                                        int        startFrom,
                                                        int        pageSize) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException;


    /**
     * Retrieve the external reference for this unique identifier (guid).
     *
     * @param userId the name of the calling user.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    ExternalReference getExternalReference(String     userId,
                                           String     externalReferenceGUID) throws InvalidParameterException,
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
    List<ExternalReference> retrieveAttachedExternalReferences(String     userId,
                                                               String     attachedToGUID,
                                                               int        startFrom,
                                                               int        pageSize) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException;

    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param linkId identifier for the reference from the perspective of the object that the reference is being attached to.
     * @param linkDescription description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void linkExternalReference(String               userId,
                               String               attachedToGUID,
                               String               linkId,
                               String               linkDescription,
                               String               externalReferenceGUID) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /**
     * Unlink an external reference from an object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void unlinkExternalReference(String               userId,
                                 String               attachedToGUID,
                                 String               externalReferenceGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException;


    /**
     * Store a new external reference.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param linkId identifier for the reference from the perspective of the object that the reference is being attached to.
     * @param linkDescription description for the reference from the perspective of the object that the reference is being attached to.
     * @param resourceId unique reference id assigned by the resource owner.
     * @param resourceDisplayName display name for the resource.
     * @param resourceDescription generic description of the resource.
     * @param resourceURL URL to access the resource.
     * @param resourceVersion version number of the resource.
     * @param owningOrganization organization the owns the resource.
     * @param resourceProperties properties defined as part of a subclass of ExternalReference
     * @param additionalProperties arbitrary additional properties.
     *
     * @return unique identifier (guid) of the new ExternalReference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    String  storeExternalReference(String               userId,
                                   String               attachedToGUID,
                                   String               linkId,
                                   String               linkDescription,
                                   String               resourceId,
                                   String               resourceDisplayName,
                                   String               resourceDescription,
                                   String               resourceURL,
                                   String               resourceVersion,
                                   String               owningOrganization,
                                   Map<String, Object>  resourceProperties,
                                   Map<String, String>  additionalProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Delete an external reference.
     *
     * @param userId the name of the calling user.
     * @param externalReferenceGUID unique identifier (guid) of the external reference.
     * @param externalReferenceId unique name of the external reference.
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void deleteExternalReference(String               userId,
                                 String               externalReferenceGUID,
                                 String               externalReferenceId) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;
}
