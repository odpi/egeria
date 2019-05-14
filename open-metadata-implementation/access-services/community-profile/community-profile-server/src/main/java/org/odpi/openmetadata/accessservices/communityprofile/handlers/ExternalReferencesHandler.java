/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;


import org.odpi.openmetadata.accessservices.communityprofile.api.ExternalReferenceManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ExternalReference;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * ExternalReferencesHandler retrieves external references objects that are linked to a specific entity
 * from the property server.  It runs server-side in the CommunityProfile
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class ExternalReferencesHandler implements ExternalReferenceManagementInterface
{
    private static final String externalReferenceTypeGUID               = "af536f20-062b-48ef-9c31-1ddd05b04c56";
    private static final String externalReferenceTypeName               = "ExternalReference";
    private static final String externalReferenceIdPropertyName         = "qualifiedName";
    private static final String displayNamePropertyName                 = "displayName";
    private static final String uriPropertyName                         = "url";
    private static final String versionPropertyName                     = "version";
    private static final String resourceDescriptionPropertyName         = "description";
    private static final String organizationPropertyName                = "organization";
    private static final String additionalPropertiesName                = "additionalProperties";


    private static final String externalReferenceLinkTypeGUID           = "7d818a67-ab45-481c-bc28-f6b1caf12f06";
    private static final String externalReferenceLinkTypeName           = "ExternalReferenceLink";
    private static final String localReferenceIdPropertyName            = "referenceId";
    private static final String linkDescriptionPropertyName             = "description";

    private static final Logger log = LoggerFactory.getLogger(ExternalReferencesHandler.class);


    private String                  serviceName;
    private String                  serverName;
    private RepositoryErrorHandler  errorHandler;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param errorHandler handler for repository service errors
     */
    public ExternalReferencesHandler(String                  serviceName,
                                     String                  serverName,
                                     InvalidParameterHandler invalidParameterHandler,
                                     OMRSRepositoryHelper    repositoryHelper,
                                     RepositoryHandler       repositoryHandler,
                                     RepositoryErrorHandler  errorHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.repositoryHandler = repositoryHandler;
    }


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
    public List<ExternalReference> findExternalReferencesById(String     userId,
                                                              String     resourceId,
                                                              int        startFrom,
                                                              int        pageSize) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String        methodName = "findExternalReferencesById";

        return null;
    }


    /**
     * Retrieve the list of external references for the supplied URL.
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
    public List<ExternalReference> findExternalReferencesByURL(String     userId,
                                                               String     url,
                                                               int        startFrom,
                                                               int        pageSize) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String        methodName = "findExternalReferencesByURL";

        return null;
    }


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
    public ExternalReference getExternalReference(String     userId,
                                                  String     externalReferenceGUID) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String        methodName = "getExternalReference";

        return null;
    }


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
    public List<ExternalReference> retrieveAttachedExternalReferences(String     userId,
                                                                      String     attachedToGUID,
                                                                      int        startFrom,
                                                                      int        pageSize) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String        methodName = "retrieveAttachedExternalReferences";

        // TODO
        log.warn(methodName + " not implemented");

        return null;
    }


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
    public void linkExternalReference(String               userId,
                                      String               attachedToGUID,
                                      String               linkId,
                                      String               linkDescription,
                                      String               externalReferenceGUID) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "linkExternalReference";

    }


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
    public void unlinkExternalReference(String               userId,
                                        String               attachedToGUID,
                                        String               externalReferenceGUID) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String        methodName = "unlinkExternalReference";

    }


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
    public String  storeExternalReference(String               userId,
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
                                                                                            UserNotAuthorizedException
    {
        final String        methodName = "storeExternalReferences";
        final String        guidParameterName = "attachedToGUID";
        final String        expectedType = "Referenceable";

        // TODO
        log.warn(methodName + " not implemented");
        return null;
    }


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
    public void deleteExternalReference(String               userId,
                                        String               externalReferenceGUID,
                                        String               externalReferenceId) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String        methodName = "linkExternalReference";

    }
}
